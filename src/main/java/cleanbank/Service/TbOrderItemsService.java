package cleanbank.Service;

import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.repositories.TbBranchLocsRepository;
import cleanbank.repositories.TbOrderItemsRepository;
import cleanbank.repositories.TbOrderRepository;
import cleanbank.utils.SynapseCM;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbOrderItemsService implements ITbOrderItemsService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbOrderItemsRepository tbOrderItemsRepository;

    @Autowired
    public void setTbOrderItemsRepository(TbOrderItemsRepository tbOrderItemsRepository) {
        this.tbOrderItemsRepository = tbOrderItemsRepository;
    }

    @Override
    public Iterable<TbOrderItems> listAllTbOrderItemss() {
        return tbOrderItemsRepository.findAll();
    }

    @Override
    public TbOrderItems getTbOrderItemsById(Long id) {
        return tbOrderItemsRepository.findOne(id);
    }

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbBranchService tbBranchService;

    @Autowired
    private TbBranchLocsRepository tbBranchLocsRepository;

    @Autowired
    private TbOrderRepository tbOrderRepository;

//    --------------------------------------------------------------------------------------------------------------

    /*public int getCntTacNo() {
        return 0;
    }*/

    @Transactional
    @Override
    public TbOrderItems saveTbOrderItems(TbOrderItems orderitem) {
        if("new".equals(orderitem.getMode())) { // 신규
            orderitem.setRegiDt(new Date());

//            모바일 api에서는 아래에서 null pointer 에러 발생
            try {
                orderitem.setUser(SynapseCM.getAuthenticatedUserID());
            } catch (Exception ex) {}

//            진행상태를 화면에서 없애 달라고 하니 일단 "주문접수"로 입력
            if(StringUtils.isEmpty(orderitem.getItStatus())) {
                orderitem.setItStatus("0101");
            }

            orderitem.setItClaim("N");
            orderitem.setEvtNm("신규");
        } else { // 수정
            orderitem.setEvtNm("수정");
        }

//        클레임 여부 체크에 따라 IT_CLAIM_DT 값 설정
        if(orderitem.getItClaim() != null && orderitem.getItClaim().equals("Y")) {
            orderitem.setItClaimDt(new Date());
        } else {
            orderitem.setItClaimDt(null);
        }

        //            시작 택번호 : 모바일에서는 지점번호가 없을때 에러 발생
        /*
        2) 시작 택번호
           - 주문 상세 페이지에서 "시작 택번호"는 수정이 가능하며 수정된 택번호는 저장시 "지점관리" 화면의 "바코드 시작번호"에도 반영된다.
           - 주문 상세 페이지에서 "저장"시 "시작 택번호"는 +1 증가한다.
           - 주문 상세 페이지에서 "택번호"는 "시작 택번호"와 같은값이 표시되며 수정이 불가능 하다.
         */
        TbOrder tbOrder = null;

        try {
            tbOrder = tbOrderService.getTbOrderById(orderitem.getOrCd());
//          3월08일 택번호를 나중에 계산 하였으나 백민의 요청으로 화면에 먼저 보여준다

            if("new".equals(orderitem.getMode())) { // 신규
                if(StringUtils.isEmpty(orderitem.getItTac())) {
//                orderitem.setItTac(tbBranchService.setBnBarCd(tbOrder.getBnCd()));
                    String bncd = tbBranchService.getBnBarCd(tbOrder.getBnCd());
                    orderitem.setItTac(bncd);
                }

                String bncd = tbBranchService.setBnBarCd(tbOrder.getBnCd(), orderitem.getItTac()); // 택번호 1증가
            }
        } catch (Exception ex) {

        }

//        return tbOrderItemsRepository.save(orderitem);
        TbOrderItems saveItems = tbOrderItemsRepository.save(orderitem);

        //        8) 주문의 모든품목들이 백민입고상태가 되었을 경우 주문상태가 백민입고상태로 변경되어야합니다.
        if(tbOrder != null) {
            Object[][] cnt = tbOrderItemsRepository.getStatusCnt(tbOrder.getOrCd());

//            if("1".equals(cnt[0][0].toString()) && !"1".equals(cnt[0][1].toString())) {
            if("1".equals(cnt[0][0].toString())) {
                tbOrder.setOrStatus(cnt[0][2].toString());

                try {
//                    tbOrderService.saveTbOrder(tbOrder);
                    tbOrderRepository.save(tbOrder);
                } catch (Exception ex) {
                    log.error("품목 저장시 주문 상태정보 저장 에러!!! \n "+ ex);
                }
            }
        }

        return saveItems;
    }

    @Override
    public void deleteTbOrderItems(Long id) {
        tbOrderItemsRepository.delete(id);
    }

//    이하 추가

//    @Transactional
    @Override
    public void deleteTbOrderItems2(Long id) {
        TbOrderItems order = tbOrderItemsRepository.findOne(id);
        order.setDelYn("Y");
        tbOrderItemsRepository.save(order);

//        주문 금액 업데이트
/*        try {
            this.saveAmountCnt(order.getOrCd());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("주문 금액 업데이트 에러 {}", e);
        }*/
    }

    //    품목 리스트 그리드 삭제 처리
    @Modifying
    @Transactional
    @Override
    public int deleteGrid(ArrayList<Long> items) {
        for (int i = 0; i < items.size(); i++) {
            log.debug("삭제 처리 ID = {}", items.get(i).toString());
        }

        Query uptQuery = entityManager.createQuery("update TbOrderItems set delYn = 'Y' where id in (?1)")
                .setParameter(1, items);

        Integer result = uptQuery.executeUpdate();

        //        주문 금액 업데이트
/*        for (int i = 0; i < items.size(); i++) {
            try {
                this.saveAmountCnt(Long.parseLong(items.get(i).toString()));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("주문 금액 업데이트 에러 {}", e);
            }
        }*/

        return result;
    }

//    주문상태 변경 : 기존 품목기준 화면에서 주문기준 화면으로 변경됨
//@Transactional(readOnly=true)
    @Modifying
    @Transactional
    @Override
    public int changeStatus(ArrayList<Long> items, String status) {
        for(int i=0; i<items.size(); i++) {
            log.debug("접수완료 처리 ID = {}", items.get(i).toString());
        }

        Query uptQuery = entityManager.createQuery("update TbOrderItems set itStatus = ?1 where orCd in (?2)")
                .setParameter(1, status)
                .setParameter(2, items);

        Integer result = uptQuery.executeUpdate();

        Query uptQuery2 = entityManager.createQuery("update TbOrder set orStatus = ?1 where orCd in (?2)")
                .setParameter(1, status)
                .setParameter(2, items);

        Integer result2 = uptQuery2.executeUpdate();

        return result;
    }

    @Modifying
    @Transactional
    @Override
    public int changeStatus(final long orCd, String status) {
        Query uptQuery = entityManager.createQuery("update TbOrderItems set itStatus = ?1 where orCd = ?2")
                .setParameter(1, status)
                .setParameter(2, orCd);

        Integer result = uptQuery.executeUpdate();

        return result;
    }

//    공장비고 저장
    @Modifying
    @Transactional
    @Override
    public int saveFMemo(final Long no, final String memo) {
        Query uptQuery = entityManager.createQuery("update TbOrderItems set itFactoryMemo = ?1 where id = ?2")
                .setParameter(1, memo)
                .setParameter(2, no);

        Integer result = uptQuery.executeUpdate();
        return result;
    }

    /**
     * 품목 리스트의 가격 + 추가요금 수량을 계산하여 주문정보를 Update후 결과 리턴
     * @param no
     * @return
     */
    @Transactional
    public TbOrder saveAmountCnt(final Long no) throws NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException, InvocationTargetException {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                           ");
        strQuery.append("     SUM(IFNULL(IT_PRICE, 0)) + SUM(IFNULL(IT_ADD_CHG, 0)) AS amount, COUNT(1) AS cnt   ");
        strQuery.append(" FROM                                                             ");
        strQuery.append("     cleanbank.TB_ORDER_ITEMS                                       ");
        strQuery.append(" WHERE                                                            ");
        strQuery.append("     OR_CD = ?1                                                   ");
        strQuery.append("         AND (DEL_YN IS NULL OR DEL_YN <> 'Y')                    ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString()).setParameter(1, no);

        Map<Object, Object> objAmountCnt = (Map<Object, Object>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .uniqueResult();

        TbOrder tbOrder = tbOrderService.getTbOrderById(no);

        BigDecimal orPrice = BigDecimal.valueOf(0);
        BigInteger orCnt = BigInteger.valueOf(0);

        /*BigDecimal tot = (BigDecimal) objAmountCnt.get("amount");
        BigInteger cnt = (BigInteger) objAmountCnt.get("cnt");

        tbOrder.setOrPrice(tot.intValueExact());
        tbOrder.setOrCnt(cnt.intValueExact());*/

        try {
            if(objAmountCnt.get("amount") != null) orPrice = (BigDecimal) objAmountCnt.get("amount");
            if(objAmountCnt.get("cnt") != null) orCnt = (BigInteger) objAmountCnt.get("cnt");
        } catch (Exception e) {
            log.error("주문금액 계산 에러 {}", e);
        }

        try {
            tbOrder.setOrPrice(orPrice.intValueExact());
            tbOrder.setOrCnt(orCnt.intValueExact());

//            tbOrderService.saveTbOrder(tbOrder); // 이렇게 하면 해당 품목의 모든 주문상태가 변경된다
            tbOrderRepository.save(tbOrder);
        } catch (Exception e) {
            log.error("주문금액 업데이트 에러 {}", e);
        }

        return tbOrder;
    }
}