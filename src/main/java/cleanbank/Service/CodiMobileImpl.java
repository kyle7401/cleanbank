package cleanbank.Service;

import cleanbank.domain.*;
import cleanbank.repositories.TbOrderItemsRepository;
import cleanbank.repositories.TbPictureRepository;
import cleanbank.repositories.TbPromotionUseRepository;
import cleanbank.viewmodel.MoOrder2;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.apache.commons.beanutils.PropertyUtils.copyProperties;

@Service
public class CodiMobileImpl implements CodiMobileService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbMemberService tbMemberService;

    @Autowired
    private ITbAddressService tbAddressService;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    @Autowired
    private TbOrderItemsRepository tbOrderItemsRepository;

    @Autowired
    private TbPictureRepository tbPictureRepository;

    @Autowired
    private ITbMemberBillinfoService tbMemberBillinfoService;

    //    ----------------------------------------------------------------------------------------------------------------

    @Override
    public TbAddress setDeliAddr(final TbOrder tbOrder, TbOrderItems tbOrderItems, TbAddress newAddress) {
        //        주소가 중복 확인후 추가
        TbAddress tbAddress = tbAddressService.addAddress(newAddress);

        tbOrderItems.setItDeliAddr(tbAddress.getId().toString());
        TbOrderItems saveItems = tbOrderItemsRepository.save(tbOrderItems);

        return tbAddress;
    }

    @Override
    public TbAddress setReqAddr(final TbOrder tbOrder, TbOrderItems tbOrderItems, TbAddress newAddress) {
        //        주소가 중복 확인후 추가
        TbAddress tbAddress = tbAddressService.addAddress(newAddress);

        tbOrderItems.setItReqAddr(tbAddress.getId().toString());
        TbOrderItems saveItems = tbOrderItemsRepository.save(tbOrderItems);

        return tbAddress;
    }

    @Override
    public TbOrderItems getItemDetail(final Long id) {

        TbOrderItems tbOrderItems = tbOrderItemsRepository.findOne(id);

        if(tbOrderItems != null) {
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tbOrderItems.getItReqAddr())) {
                TbAddress reqAddress = tbAddressService.getTbAddressById(Integer.parseInt(tbOrderItems.getItReqAddr()));

//                나누어진 주소도 추가
                if(reqAddress != null) {
//                    tbOrderItems.setItReqAddr(reqAddress.getMbAddr1() +" "+ reqAddress.getMbAddr2());
                    tbOrderItems.setItReqAddr(reqAddress.getMbAddr());
                    tbOrderItems.setItReqAddr1(reqAddress.getMbAddr1());
                    tbOrderItems.setItReqAddr2(reqAddress.getMbAddr2());
                    tbOrderItems.setItReqLat(reqAddress.getMbLat());
                    tbOrderItems.setItReqLng(reqAddress.getMbLng());
                }
            }
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tbOrderItems.getItDeliAddr())) {
                TbAddress deliAddress = tbAddressService.getTbAddressById(Integer.parseInt(tbOrderItems.getItDeliAddr()));

                if(deliAddress != null) {
                    tbOrderItems.setItDeliAddr(deliAddress.getMbAddr());
                    tbOrderItems.setItDeliAddr1(deliAddress.getMbAddr1());
                    tbOrderItems.setItDeliAddr2(deliAddress.getMbAddr2());
                    tbOrderItems.setItDeliLat(deliAddress.getMbLat());
                    tbOrderItems.setItDeliLng(deliAddress.getMbLng());
                }
            }

            List<TbPicture> pictures = tbPictureRepository.getPictureList(id);

            if(pictures != null && pictures.size() > 0) {
                tbOrderItems.setPictures(pictures);
            }
        }

        return tbOrderItems;
    }

    @Override
    public MoOrder2 getOrderDetail(final Long orCd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        MoOrder2 moOrder2 = null;
        TbOrder order = tbOrderService.getTbOrderById(orCd);

        if(order != null) {
            moOrder2 = new MoOrder2();
            copyProperties(moOrder2, order);

//            회원정보,
            Integer mbCd = order.getMbCd();
            TbMember tbMember = null;

            if(!StringUtils.isEmpty(mbCd)) {
                log.debug("회원 코드 = {}", mbCd);
                tbMember = tbMemberService.getTbMemberById(mbCd);

                if(tbMember != null) {
                    moOrder2.setMbNicNm(tbMember.getMbNicNm());
                    moOrder2.setMbTel(tbMember.getMbTel());
                    moOrder2.setMbCd(mbCd);
                    moOrder2.setMbLevel(tbMember.getMbLevel());

//                    빌링정보
                    TbMemberBillinfo tbMemberBillinfo = tbMemberBillinfoService.getTbMemberBillinfoById(mbCd);
                    if(tbMemberBillinfo != null) {
                        moOrder2.setMbBiilkey(tbMemberBillinfo.getMbBiilkey());
                        moOrder2.setMbBillinfo(tbMemberBillinfo.getMbBillinfo());
                    }
                }
            }

            // 배송요청 일시 컬럼명 사용이 잘못됨
            moOrder2.setOrVisitDt(order.getOrDeliFuVisitDt());

//            배송요청 일시가 null일 경우 수거요청일시의 +3일로(영업일 기준) 디폴트 값으로
            if(StringUtils.isEmpty(moOrder2.getOrVisitDt())) {
                if(!StringUtils.isEmpty(moOrder2.getOrReqDt())) {
                    Date orReqDt = moOrder2.getOrReqDt();
                    Calendar cal = Calendar.getInstance() ;
                    cal.setTime(orReqDt);

                    int iCount = 0;

//                    토/일 제외
                    while (iCount < 3) {
                        cal.add(Calendar.DAY_OF_WEEK, 1);

                        if(cal.get(Calendar.DAY_OF_WEEK) != 1 && cal.get(Calendar.DAY_OF_WEEK) != 7) {
                            ++iCount;
                        }
                        /*int dayNum = cal.get(Calendar.DAY_OF_WEEK);
                        log.debug("날짜 = {}, 요일 = {}", orReqDt, dayNum);*/
                    }

                    moOrder2.setOrVisitDt(new Date(cal.getTimeInMillis()));
                }
            }

//            배송지 주소가 null일 경우 수거지주소가 디폴트 값으로
            if(StringUtils.isEmpty(moOrder2.getOrDeliAddr())) {
                if(!StringUtils.isEmpty(moOrder2.getOrReqAddr())) {
                    moOrder2.setOrDeliAddr(moOrder2.getOrReqAddr());
                }
            }

//            주소
            String orReqAddr = moOrder2.getOrReqAddr();
            String orDeliAddr = moOrder2.getOrDeliAddr();

            if(!StringUtils.isEmpty(orReqAddr)) {
                TbAddress tbAddress1 = tbAddressService.getTbAddressById(Integer.parseInt(orReqAddr));
                if(tbAddress1 != null) {
                    moOrder2.setOrReqAddr(tbAddress1.getMbAddr());
                    moOrder2.setOrReqAddr1(tbAddress1.getMbAddr1());
                    moOrder2.setOrReqAddr2(tbAddress1.getMbAddr2());
                    moOrder2.setMbReqLat(tbAddress1.getMbLat());
                    moOrder2.setMbReqLng(tbAddress1.getMbLng());
                }
            }

            if(!StringUtils.isEmpty(orDeliAddr)) {
                TbAddress tbAddress2 = tbAddressService.getTbAddressById(Integer.parseInt(orDeliAddr));
                if(tbAddress2 != null) {
                    moOrder2.setOrDeliAddr(tbAddress2.getMbAddr());
                    moOrder2.setOrDeliAddr1(tbAddress2.getMbAddr1());
                    moOrder2.setOrDeliAddr2(tbAddress2.getMbAddr2());
                    moOrder2.setMbDeliLat(tbAddress2.getMbLat());
                    moOrder2.setMbDeliLng(tbAddress2.getMbLng());
                }
            }

//            쿠폰수
            List<TbPromotionUse> coupLIst = tbPromotionUseRepository.getOrdCoupLIst(mbCd, orCd);
            if(coupLIst != null && coupLIst.size() > 0) {
                moOrder2.setOrCoupon(coupLIst.size());

                int iCouponMoney = 0;

//            쿠폰번호별 할인금액 또는 회원등급별 할인금액 계산
                for(TbPromotionUse tbPromotionUse : coupLIst) {
                    TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();
                    if(tbPromotion != null) {
                        if(org.apache.commons.lang3.StringUtils.isEmpty(tbPromotion.getPoCoup())) {
                            switch (tbMember.getMbLevel().toString()) {
                                case "1":
                                    iCouponMoney += tbPromotion.getPoGreenPrice();
                                    break;

                                case "2":
                                    iCouponMoney += tbPromotion.getPoSilverPrice();
                                    break;

                                case "3":
                                    iCouponMoney += tbPromotion.getPoGoldPrice();
                                    break;
                            }
                        } else {
                            if(tbPromotion.getPoDiscountAmt() != null) iCouponMoney += tbPromotion.getPoDiscountAmt();
                        }
                    }
                }

                moOrder2.setOrCouponMoney(iCouponMoney);
            }
        }

        return moOrder2;
    }

    @Override
    public List<?> getBnSchedule(final Integer bnCd) {

        List<?> list = this.getSchedule("a.BN_CD", bnCd);

        return list;
    }

    @Override
    public List<?> getMySchedule(final Integer epCd) {

        List<?> list = this.getSchedule("e.EP_CD", epCd);

        return list;
    }

    private List<?> getSchedule(final String colName, final Integer Cd) {

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                                 ");
        strQuery.append("     e.EP_CD, a.OR_CD, ");

//        strQuery.append("     CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'),                       ");
        strQuery.append("     CONCAT(DATE_FORMAT(a.REGI_DT, '%y%m%d%H%i'),                       ");
        strQuery.append("             LPAD(a.OR_CD, 4, '0')) AS OR_CD2,                          ");

//        strQuery.append("     DATE_FORMAT(b.IT_VISIT_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,       ");
        strQuery.append("     DATE_FORMAT(a.OR_REQ_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,       ");

        strQuery.append("     c.MB_NIC_NM,                                                       ");
        strQuery.append("     c.MB_TEL,                                                          ");
        strQuery.append("     CASE                                                               ");
        strQuery.append("         WHEN MB_ADDR1 IS NULL AND MB_ADDR2 IS NULL THEN NULL           ");
        strQuery.append("         WHEN                                                           ");
        strQuery.append("             MB_ADDR1 IS NOT NULL                                       ");
        strQuery.append("                 AND MB_ADDR2 IS NULL                                   ");
        strQuery.append("         THEN                                                           ");
        strQuery.append("             MB_ADDR1                                                   ");
        strQuery.append("         WHEN                                                           ");
        strQuery.append("             MB_ADDR1 IS NOT NULL                                       ");
        strQuery.append("                 AND MB_ADDR2 IS NOT NULL                               ");
        strQuery.append("         THEN                                                           ");
        strQuery.append("             CONCAT(MB_ADDR1, ' ', MB_ADDR2)                                  ");
        strQuery.append("     END AS IT_REQ_ADDR,                                                ");
        strQuery.append("     g.CD_NM AS IT_STATUS, a.BN_CD, a.OR_DELI_EP_CD, a.MB_CD as mbCd                                              ");
       /* strQuery.append("     ,NULL,                                                              ");
        strQuery.append("     a.OR_CD AS orCd,                                                   ");
        strQuery.append("     b.ID,                                                              ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS IT_REGI_DT,            ");
        strQuery.append("     a.OR_CD,                                                           ");
        strQuery.append("     d.BN_NM,                                                           ");
        strQuery.append("     b.IT_CNT,                                                          ");
        strQuery.append("     e.EP_NM AS EP_CD,                                                  ");
        strQuery.append("     h.EP_NM AS DELI_EP,                                                ");
        strQuery.append("     b.IT_MEMO,                                                         ");
        strQuery.append("     b.IT_PRICE,                                                        ");
        strQuery.append("     f.MB_ADDR1 AS OR_REQ_ADDR,                                         ");
        strQuery.append("     IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE,          ");
        strQuery.append("     DATE_FORMAT(b.IT_REQ_DT, '%Y-%m-%d %H:%i') AS IT_REQ_DT,           ");
        strQuery.append("     a.OR_MEMO,                                                         ");
        strQuery.append("     DATE_FORMAT(b.IT_OUT_DT, '%Y-%m-%d %H:%i') AS IT_OUT_DT,           ");
        strQuery.append("     DATE_FORMAT(b.IT_BAEK_IN_DT, '%Y-%m-%d %H:%i') AS IT_BAEK_IN_DT    ");*/
        strQuery.append(" FROM                                                                   ");
        strQuery.append("     TB_ORDER AS a                                                      ");
        /*strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                           ");*/
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                                ");
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_BRANCH AS d ON a.BN_CD = d.BN_CD                                ");
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD                              ");
        strQuery.append("         LEFT OUTER JOIN                                                ");

//        strQuery.append("     TB_ADDRESS AS f ON b.IT_REQ_ADDR = f.ID                            ");
        strQuery.append("     TB_ADDRESS AS f ON a.OR_REQ_ADDR = f.ID                            ");

        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     (SELECT                                                            ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                            ");
        strQuery.append("     FROM                                                               ");
        strQuery.append("         TB_CODE                                                        ");
        strQuery.append("     WHERE                                                              ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00' "); // 배송완료 전단계 까지만 보여줌

//        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS= CONCAT(g.CD_GP, g.CD_IT) ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON a.OR_STATUS= CONCAT(g.CD_GP, g.CD_IT) ");

        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_EMPLOYEE AS h ON a.OR_DELI_EP_CD = h.EP_CD                      ");
        strQuery.append(" WHERE                                                                  ");
//        strQuery.append(" --    b.IT_STATUS = '0101'                                             ");

//        strQuery.append("     e.EP_CD = ?1                                                        ");

        strQuery.append(colName +" = ?1 ");

        if("a.BN_CD".equals(colName)) {
//            strQuery.append(" AND a.OR_DELI_EP_CD IS NULL ");
            strQuery.append(" AND a.EP_CD IS NULL ");
        }

        strQuery.append("         AND (a.DEL_YN IS NULL OR a.DEL_YN != 'Y')                      ");
        /*strQuery.append("         AND (b.DEL_YN IS NULL OR b.DEL_YN != 'Y')                      ");
        strQuery.append(" ORDER BY b.IT_REQ_DT ASC                                               ");*/
//        strQuery.append(" ORDER BY a.OR_REQ_DT ASC                                               ");

        strQuery.append(" AND a.OR_STATUS NOT IN ('0110', '0111') "); // 배송완료 전단계 까지만 보여줌
        strQuery.append(" AND a.OR_REQ_DT >= DATE_ADD(NOW(), INTERVAL - 7 DAY) ");
        strQuery.append(" ORDER BY a.OR_REQ_DT DESC                                               ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString()).setParameter(1, Cd);

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}
