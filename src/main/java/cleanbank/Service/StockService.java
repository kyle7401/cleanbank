package cleanbank.Service;

import cleanbank.domain.TbOrderItems;
import cleanbank.repositories.TbOrderItemsRepository;
import cleanbank.utils.SynapseCM;
import cleanbank.viewmodel.TbOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
@Service
public class StockService implements IStockService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TbOrderItemsRepository tbOrderItemsRepository;

    @Autowired
    private ITbOrderService tbOrderService;

//    ----------------------------------------------------------------------------------------------------------------

//    목록 검색
    //    http://antop.tistory.com/30
    public List<?> getStockList(
            final String tac,
            final String bncd,
            final String from,
            final String to,
            final String status
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(tac)) { // 택번호
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_TAC = :tac");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

//        주문일자 컬럼 필요
        if(StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

/*        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }*/

        if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        } else { // 디폴트로 주문상태가 접수완료(0104), 공장입고(0105)
//            strWhere = SynapseCM.whereConcatenator(strWhere, "(b.IT_STATUS in ('0104', '0105') OR b.IT_STATUS IS NULL)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0104', '0105')");
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT b.ID,                                                     ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS IT_REGI_DT, ");
        strQuery.append("     IT_TAC,                                                 ");
        strQuery.append("     f.PD_NM,                                                ");
        strQuery.append("     b.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     c.BN_NM,                                                ");
        strQuery.append("     d.EP_NM AS EP_CD,                                       ");
        strQuery.append("     b.USER,                                                 ");
        strQuery.append("     e.CD_NM AS IT_STATUS,                                   ");
        strQuery.append("     b.IT_MEMO, a.OR_PRICE                                                 ");
        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE, MB_NIC_NM  ");
        strQuery.append(" FROM                                                        ");
        strQuery.append("     TB_ORDER AS a                                           ");
        strQuery.append("         LEFT OUTER JOIN                                     ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                ");
        strQuery.append("         LEFT OUTER JOIN                                     ");
        strQuery.append("     TB_BRANCH AS c ON a.BN_CD = c.BN_CD                     ");
        strQuery.append("         LEFT OUTER JOIN                                     ");
        strQuery.append("     TB_EMPLOYEE AS d ON a.EP_CD = d.EP_CD                   ");
        strQuery.append("         LEFT OUTER JOIN                                     ");
        strQuery.append("     (SELECT                                                 ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                 ");
        strQuery.append("     FROM                                                    ");
        strQuery.append("         TB_CODE                                             ");
        strQuery.append("     WHERE                                                   ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00' ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS e ON b.IT_STATUS = CONCAT(e.CD_GP, e.CD_IT) ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("     (SELECT                                                  ");
        strQuery.append("         PD_LVL1, PD_LVL2, PD_LVL3, PD_NM                     ");
        strQuery.append("     FROM                                                     ");
        strQuery.append("         TB_PRODUCT                                           ");
        strQuery.append("     WHERE                                                    ");
        strQuery.append("         DEL_YN IS NULL OR DEL_YN != 'Y') AS f ON CONCAT(b.PD_LVL1, b.PD_LVL2, b.PD_LVL3) = CONCAT(f.PD_LVL1, f.PD_LVL2, f.PD_LVL3) ");

        strQuery.append("     LEFT OUTER JOIN TB_MEMBER AS g ON a.MB_CD = g.MB_CD ");

        strQuery.append(strWhere);

//        strQuery.append(" ORDER BY b.IT_VISIT_DT DESC "); // 최신 수거완료 일시 순으로 리스트를 정렬
        strQuery.append(" ORDER BY b.IT_TAC "); // 3월2일 택번호 순서로 정렬

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
//            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<TbOrderVO> list = (List<TbOrderVO>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    /**
     * 입고완료(0105) 처리시 공장입고일시(IT_IN_DT)를 현재 일시로
     * 입고취소(0104) 처리시 공장입고일시(IT_IN_DT)를 null로
     *
     * @param items
     * @param status
     * @return
     */
    @Modifying
    @Transactional
    @Override
    public int changeStatus(final ArrayList<Long> items, final String status) throws Exception {
        /*for(int i=0; i<items.size(); i++) {
            log.debug("입고완료/취소 처리 ID = {}", items.get(i).toString());
        }*/

        String strQuery = null;

        if("0107".equals(status)) { // 공장출고
            strQuery = "update TbOrderItems set itStatus = ?1, itOutDt = current_timestamp() where id in (?2)";
        } else if("0106".equals(status)) { // 2월15일 세탁중 누락되어 추가
            strQuery = "update TbOrderItems set itStatus = ?1, itInDt = current_timestamp() where id in (?2)";
        } else if("0105".equals(status)) {
            strQuery = "update TbOrderItems set itStatus = ?1, itInDt = current_timestamp() where id in (?2)";
        } else if("0108".equals(status)) { // 2월 18일 백민입고 추가
            strQuery = "update TbOrderItems set itStatus = ?1, itInDt = current_timestamp() where id in (?2)";
        } else if("0104".equals(status)) {
            strQuery = "update TbOrderItems set itStatus = ?1, itInDt = null where id in (?2)";
        } else {
            log.error("구분값 없음!!!");
        }

        Query uptQuery = entityManager.createQuery(strQuery)
                .setParameter(1, status)
                .setParameter(2, items);

        Integer result = uptQuery.executeUpdate();

//        품목의 주문상태가 모두 동일하게 변경되면 주문의 주문 상태도 변경함
//        8) 주문의 모든품목들이 백민입고상태가 되었을 경우 주문상태가 백민입고상태로 변경되어야합니다.
        long id = Long.parseLong(items.get(0).toString());
        TbOrderItems tbOrderItems = tbOrderItemsRepository.findOne(id);
        long orCd = tbOrderItems.getOrCd();

        Object[][] cnt = tbOrderItemsRepository.getStatusCnt(orCd);

//        if("1".equals(cnt[0][0].toString()) && !"1".equals(cnt[0][1].toString())) {
        if("1".equals(cnt[0][0].toString())) {
            int iCnt = tbOrderService.changeStatus(orCd, status);
        }

        return result;
    }
}
