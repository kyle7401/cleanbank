package cleanbank.Service;

import cleanbank.domain.*;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.repositories.*;
import cleanbank.utils.SynapseCM;
import cleanbank.viewmodel.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbOrderService implements ITbOrderService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager em;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    @Autowired
    private TbAddressRepository tbAddressRepository;

    @Autowired
    private TbEmployeeRepository tbEmployeeRepository;

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

/*    @Autowired
    public void setTbOrderRepository(TbOrderRepository tbOrderRepository) {
        this.tbOrderRepository = tbOrderRepository;
    }*/

    @Override
    public Iterable<TbOrder> listAllTbOrders() {
        return tbOrderRepository.findAll();
    }

    @Override
    public TbOrder getTbOrderById(Long id) {
        return tbOrderRepository.findOne(id);
    }

    @Autowired
    private InicisResultRepository inicisResultRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PushMobileService pushMobileService;

    @Autowired
    private TbBranchLocsRepository tbBranchLocsRepository;

    @Autowired
    private TbBranchLocsService tbBranchLocsService;

    @Autowired
    private ITbOrderItemsService tbOrderItemsService;

//    ------------------------------------------------------------------------------------------------------------------------


    /**
     * 8) 주문의 모든품목들이 백민입고상태가 되었을 경우 주문상태가 백민입고상태로 변경되어야합니다.
     * @param no
     * @param status
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Modifying
    @Transactional
    @Override
    public int changeStatus(Long no, String status) throws Exception {
        Query uptQuery = entityManager.createQuery("update TbOrder set orStatus = ?1 where orCd = ?2")
                .setParameter(1, status)
                .setParameter(2, no);

        Integer result = uptQuery.executeUpdate();
        return result;
    }

    /**
     * http://stackoverflow.com/questions/19302196/transaction-marked-as-rollback-only-how-do-i-find-the-cause
     * @param inicisResult
     */
    @Transactional(noRollbackFor = Exception.class)
    public String inicis_result(InicisResult inicisResult) throws MessagingException {
        String strResult = null;
//        이니시스 결제 파라메터 db에 입력
//        try {
//            em.persist(inicisResult);

            inicisResultRepository.save(inicisResult);
        /*} catch (Exception e) {
            log.error("\n\n########### 이니시스 결제 파라메터 저장 에러!!!\n {}"+ e.toString());
        }*/

        try {

            if ("00".equals(inicisResult.getPStatus())) { // 성공
                //        해당 주문건을 결제 완료 처리
                Long orCd = Long.parseLong(inicisResult.getPOid());
                TbOrder tbOrder = tbOrderRepository.findOne(orCd);

                if (tbOrder != null) {
                    tbOrder.setOrChargeType("Y");
                    TbOrder saveOrder = tbOrderRepository.save(tbOrder);
                    if("Y".equals(saveOrder.getOrChargeType())) {
                        strResult = saveOrder.getOrChargeType();
                    }
                } else {
                    log.error("이니시스 결제 승인 되었으나 해당 주문건을 찾지 못함 "+ orCd);
                }


//        100min 담당자에게 알림 메일 발송
            /*$email = 'order@100min.co.kr';
            $this->phpmailer->Subject = iconv('UTF-8', 'EUC-KR', '[백의민족] 새로운 주문이 들어왔습니다.');
            $this->phpmailer->Body = '주문번호 : ' . $orderCode;*/

                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);

//                helper.setTo("hyoseop@synapsetech.co.kr");
                helper.setTo("winnerhjh@synapsetech.co.kr");
//        helper.setFrom("order@100min.co.kr");
//        helper.setFrom("support@100min.co.kr");

                helper.setSubject("[백의민족] 주문번호 " + tbOrder.getOrCd2() + " 결제 되었습니다.");

                String strBody = "주문번호 : " + tbOrder.getOrCd2();
                strBody += "\\nP_AMT : " + inicisResult.getPAmt();
                strBody += "\\nP_AUTH_DT : " + inicisResult.getPAuthDt();
                strBody += "\\nP_AUTH_NO : " + inicisResult.getPAuthNo();
                strBody += "\\nP_TID : " + inicisResult.getPTid();

                helper.setText(strBody);

                javaMailSender.send(mail);

//            기존에는 고객에게 SMS로 전송, 푸쉬 메세지 전송으로 대체?
            /*$smsInfo = $this->model->findSmsInfo2($orderCode);
            //$msg = _orderStateMessage(10, $smsInfo['nickname']);
            $msg = '이용해주셔서 감사드립니다. 카톡에서 \'백의민족\' 을 친구추가하시고 친추할인 챙겨받으세요!';*/
            }
        } catch (Exception ex) {
            log.error("이니시스 승인성공 메일 전송 에러\n{}", ex);
        }

        return strResult;
    }

    /**
     * 18.주문상세(배송중) : 배송 중 상태의 주문상세정보를 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @Override
    public MoOrderDetail2 getOrderDetail3(final Integer mbCd, final Long orCd) {
        TypedQuery selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderDetail2(" +
                        "c.cdNm, t.orStatus, t.orDeliFuVisitDt, CONCAT(a.mbAddr1, COALESCE(a.mbAddr2, '')), t.orCnt, t.orPoint, t.orPrice, t.orDelivery, t.orDiscount, t.orExtMoney, t.orCharge," +
                        " e.epNm, e.epTel, e.epImg, t.regiDt, t.orCd, t.orChargeType)" +
                        " FROM TbOrder as t, TbCode as c, TbEmployee as e, TbAddress as a" +
                        " where t.mbCd = ?1 and t.orCd = ?2 and (t.delYn is null or t.delYn != 'Y')" +
                        " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                        " AND t.epCd = e.epCd" +
                        " AND t.orDeliAddr = a.id"
                , MoOrderDetail2.class)
                .setParameter(1, mbCd)
                .setParameter(2, orCd);

        MoOrderDetail2 order = (MoOrderDetail2) selectQuery.getSingleResult();

        return order;
    }

    /**
     * 17.주문상세(결제대기/결제완료/배송중) :	결제대기/결제완료 상태의 주문상세정보를 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @Override
    public MoOrderDetail2 getOrderDetail2(final Integer mbCd, final Long orCd) {
/*        TypedQuery selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderDetail2(" +
                        "c.cdNm, t.orStatus, t.orReqDt, CONCAT(a.mbAddr1, COALESCE(a.mbAddr2, '')), t.orCnt, t.orPoint, t.orPrice, t.orDelivery, t.orDiscount, t.orExtMoney, t.orCharge," +
                        " e.epNm, e.epTel, e.epImg, t.regiDt, t.orCd, t.orChargeType)" +
                        " FROM TbOrder as t, TbCode as c, TbEmployee as e, TbAddress as a" +
                        " where t.mbCd = ?1 and t.orCd = ?2 and (t.delYn is null or t.delYn != 'Y')" +
                        " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                        " AND t.epCd = e.epCd" +
                        " AND t.orReqAddr = a.id"
                , MoOrderDetail2.class)
                .setParameter(1, mbCd)
                .setParameter(2, orCd);*/

        //        코디가 배정되지 않으면 데이터를 찾지 못함
        TypedQuery selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderDetail2(" +
                        "c.cdNm, t.orStatus, t.orReqDt, CONCAT(a.mbAddr1, COALESCE(a.mbAddr2, '')), t.orCnt, t.orPoint, t.orPrice, t.orDelivery, t.orDiscount, t.orExtMoney, t.orCharge," +
//                        " t.epCd, t.regiDt, t.orCd, t.orChargeType)" +
                        " t.orDeliEpCd, t.regiDt, t.orCd, t.orChargeType)" +
                        " FROM TbOrder as t, TbCode as c, TbAddress as a" +
                        " where t.mbCd = ?1 and t.orCd = ?2 and (t.delYn is null or t.delYn != 'Y')" +
                        " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                        " AND t.orReqAddr = a.id"
                , MoOrderDetail2.class)
                .setParameter(1, mbCd)
                .setParameter(2, orCd);

        MoOrderDetail2 order = null;

        try {
            order = (MoOrderDetail2) selectQuery.getSingleResult();
            /*TbOrder tbOrder = selectQuery.getSingleResult();
            order = new MoOrderDetail2();
            copyProperties(tbOrderItems, newOrderItems);*/
        } catch (Exception ex) {
            log.error("주문 검색 에러 {}", ex.toString());
        }

        if(order == null) return order;

//        수거코디 -> 배송코디 변경
        if(order.getEpCd() != null) {
            TbEmployee emp = tbEmployeeRepository.findOne(order.getEpCd());

            order.setEpNm(emp.getEpNm());
            order.setEpTel(emp.getEpTel());
            order.setEpImg(emp.getEpImg());
        }

//        쿠폰 사용정보
        TbMember tbMember = tbMemberRepository.findOne(mbCd);

        List<TbPromotionUse> coupLIst = tbPromotionUseRepository.getOrdCoupLIst(mbCd, orCd);
        if(coupLIst != null && coupLIst.size() > 0) {
            order.setOrCoupon(coupLIst.size());

            int iCouponMoney = 0;

//            쿠폰번호별 할인금액 또는 회원등급별 할인금액 계산
            for(TbPromotionUse tbPromotionUse : coupLIst) {
                TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();
                if(tbPromotion != null) {
                    if(StringUtils.isEmpty(tbPromotion.getPoCoup())) {
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
                        if(tbPromotion.getPoDiscountAmt() != null) iCouponMoney += tbPromotion.getPoDiscountAmt(); // 3월 7일 null 에러 발생
                    }
                }
            }

            order.setOrCouponMoney(iCouponMoney);
        }

//        포인트 사용정보
        order.setOrPoint(order.getOrPoint());

        return order;
    }

    /**
     * 16.주문상세(수거중) : 수거 중 상태의 주문상태의 주문상세내역을 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @Override
    public MoOrderDetail1 getOrderDetail1(final Integer mbCd, final Long orCd) {
//        TypedQuery selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderDetail1(" +
//                "c.cdNm, t.orStatus, t.orReqDt, CONCAT(a.mbAddr1, COALESCE(a.mbAddr2, '')),  e.epNm, e.epTel, t.orCd, t.regiDt, e.epImg)" +
//                " FROM TbOrder as t, TbCode as c, TbEmployee as e, TbAddress as a" +
//                " where t.mbCd = ?1 and t.orCd = ?2 and (t.delYn is null or t.delYn != 'Y')" +
//                " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
//                " AND t.epCd = e.epCd" +
//                " AND t.orReqAddr = a.id"
//                , MoOrderDetail1.class)
//                .setParameter(1, mbCd)
//                .setParameter(2, orCd);

//        코디가 배정되지 않으면 데이터를 찾지 못함
        TypedQuery selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderDetail1(" +
                        "c.cdNm, t.orStatus, t.orReqDt, CONCAT(a.mbAddr1, ' ', COALESCE(a.mbAddr2, '')),  t.orCd, t.regiDt, t.epCd)" +
                        " FROM TbOrder as t, TbCode as c, TbAddress as a" +
                        " where t.mbCd = ?1 and t.orCd = ?2 and (t.delYn is null or t.delYn != 'Y')" +
                        " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                        " AND t.orReqAddr = a.id"
                , MoOrderDetail1.class)
                .setParameter(1, mbCd)
                .setParameter(2, orCd);

        MoOrderDetail1 order = (MoOrderDetail1) selectQuery.getSingleResult();

        if(order.getEpCd() != null) {
            TbEmployee emp = tbEmployeeRepository.findOne(order.getEpCd());

            order.setEpNm(emp.getEpNm());
            order.setEpTel(emp.getEpTel());
            order.setEpImg(emp.getEpImg());
        }

        return order;
    }

    /**
     * 15.이용내역 : 최근일자순으로 주문에서 배송까지 모두 완료된 내역을 가져온다.
     * @param mbCd
     * @param status
     * @return
     */
    @Override
    public List<?> getCompleteList(final Integer mbCd, final List<String> status) {

        Query selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderList(t.orCd, t.regiDt, t.orStatus, c.cdNm, t.orChargeType)" +
                " FROM TbOrder as t, TbCode as c" +
                " where t.mbCd = ?1 and t.orStatus in ?2 and (t.delYn is null or t.delYn != 'Y')" +
                " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                " ORDER BY t.orCd DESC")
                .setParameter(1, mbCd)
                .setParameter(2, status);

        List<?> list = selectQuery.getResultList();

        return list;
    }

    /**
     * 14.주문현황 : 주문상태가 완료되지 않은 내역들을 가져온다.
     * @param mbCd
     * @param status
     * @return
     */
    @Override
    public List<?> getMoOrderList(final Integer mbCd, final List<String> status) {

/*        Query selectQuery = entityManager.createQuery("SELECT t.mbCd AS mbCd FROM TbOrder as t where mbCd = ?1 and t.orStatus not in ?2 and (t.delYn is null or t.delYn != 'Y') ORDER BY t.orCd")
                .setParameter(1, mbCd)
                .setParameter(2, status);*/

//        List<?> list = selectQuery.getResultList();

/*        List<?> list = selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();*/

//        List<?> list = selectQuery.unwrap(SQLQuery.class).addEntity(TbOrder.class).list();

        Query selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderList(t.orCd, t.regiDt, t.orStatus, c.cdNm, t.orChargeType)" +
                " FROM TbOrder as t, TbCode as c" +
                " where t.mbCd = ?1 and t.orStatus not in ?2 and (t.delYn is null or t.delYn != 'Y')" +
                " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                " ORDER BY t.orCd")
                .setParameter(1, mbCd)
                .setParameter(2, status);

        List<?> list = selectQuery.getResultList();

        return list;
    }

    //    로그용 접수처리 검색
//    http://antop.tistory.com/30
/*    public Iterable<TbOrder> getLogList2(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            String status,
            final Pageable pageable
    ) {

        QTbOrder tbOrder = QTbOrder.tbOrder;
        QTbOrderItems tbOrderItems = QTbOrderItems.tbOrderItems;
        QTbBranch tbBranch = QTbBranch.tbBranch;
        QTbMember tbMember = QTbMember.tbMember;

        BooleanBuilder builder = new BooleanBuilder();

//        Iterable<TbOrder> list = tbOrderRepository.findAll(builder, pageable);

        JPAQuery query = new JPAQuery(entityManager);

//        http://social.gseosem.com/path-references-in-querydsl-join-to-subquery/

        Iterable<TbOrder> list = query.from(tbOrder)
                .leftJoin(tbOrderItems).on(tbOrder.orCd.eq(tbOrderItems.orCd))
//                .join(tbOrder.orCd, tbOrderItems)
//                .where(builder)
//                .orderBy(tbOrder.orCd.asc())
                .list(tbOrder);
                *//*.offset(10).limit(10)
                .listResults(tbOrder);*//*

        return list;
    }*/

    /**
     * 푸시메세지 전송 유형
     *
     * 02.코디배정 푸시 : 코디 배정 시, 해당회원에게 "고객님의 주문에 담당코디가 배정되었습니다." 푸시가 발송되어야 합니다.
     * 03.코디배정 수정 푸시 : 코디 수정 시, 해당회원에게 "고객님의 주문에 담당코디가 재배정되었습니다." 푸시가 발송되어야 합니다.
     * 04.주문접수완료 푸시 : 주문이 "접수완료" 처리 될 경우 해당회원에게 "고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다." 푸시가 발송되어야 합니다.
     *
     * @param order
     * @return
     */
    @Modifying
    @Transactional
    @Override
    public TbOrder saveTbOrder(TbOrder order) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException {

        // 푸시메세지 전송 유형
        Integer iPushGubun = 0;

        if("new".equals(order.getMode())) { // 신규
            order.setEvtNm("신규");

            order.setRegiDt(new Date());
            order.setUser(SynapseCM.getAuthenticatedUserID());
            order.setOrClaim("N");
//            order.setOrChannel("3"); // 접수채널

            if(!StringUtils.isEmpty(order.getOrReqAddr())) iPushGubun = 2; // 02.코디배정 푸시
            if("0104".equals(order.getOrStatus())) iPushGubun = 4; // 04.주문접수완료 푸시
            if("0110".equals(order.getOrStatus()) && !"Y".equals(order.getOrChargeType())) iPushGubun = 5; // 05.배송완료 후 미결제 시

        } else { // 수정
            order.setEvtNm("수정");

            Long no = order.getOrCd();

//        주문정보 수정시 일시및 주소가 변경되었을 경우, 하위 품목 리스트 모두 Update
//            TbOrder oldOrder = this.getTbOrderById(no);
            TbOrder oldOrder = tbOrderRepository.findOne(no);

        /*주문정보의 수거요청일시(orReqDt), 수거일시(orVisitDt), 배송요청일시(orDeliFuVisitDt), 배송일시(orDeliVisitDt)
        품목정보의 수거요청일시(itReqDt), 수거일시(itVisitDt), 배송예정일시(itDeliFuVisitDt), 배송일시(itDeliVisitDt)

        수거지 주소(orReqAddr), 배송지 주소(orDeliAddr) -> itReqAddr, itDeliAddr*/

            if(order.getOrReqDt() != null && !order.getOrReqDt().equals(oldOrder.getOrReqDt())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itReqDt = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrReqDt())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("수거요청일시(orReqDt) {} -> {} 변경됨. {} 건 업데이트", order.getOrReqDt(), oldOrder.getOrReqDt(), result);
            }

            if(order.getOrVisitDt() != null && !order.getOrVisitDt().equals(oldOrder.getOrVisitDt())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itVisitDt = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrVisitDt())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("수거일시(orVisitDt) {} -> {} 변경됨. {} 건 업데이트", order.getOrVisitDt(), oldOrder.getOrVisitDt(), result);
            }

            if(order.getOrDeliFuVisitDt() != null && !order.getOrDeliFuVisitDt().equals(oldOrder.getOrDeliFuVisitDt())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itDeliFuVisitDt = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrDeliFuVisitDt())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("배송요청일시(orDeliFuVisitDt) {} -> {} 변경됨. {} 건 업데이트", order.getOrDeliFuVisitDt(), oldOrder.getOrDeliFuVisitDt(), result);
            }

            if(order.getOrDeliVisitDt() != null && !order.getOrDeliVisitDt().equals(oldOrder.getOrDeliVisitDt())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itDeliVisitDt = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrDeliVisitDt())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("배송일시(orDeliVisitDt) {} -> {} 변경됨. {} 건 업데이트", order.getOrDeliVisitDt(), oldOrder.getOrDeliVisitDt(), result);
            }

            if(order.getOrReqAddr() != null && !order.getOrReqAddr().equals(oldOrder.getOrReqAddr())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itReqAddr = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrReqAddr())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("수거지 주소(orReqAddr) {} -> {} 변경됨. {} 건 업데이트", order.getOrReqAddr(), oldOrder.getOrReqAddr(), result);
            }

            if(order.getOrDeliAddr() != null && !order.getOrDeliAddr().equals(oldOrder.getOrDeliAddr())) {
                Query uptQuery = entityManager.createQuery("update TbOrderItems set itDeliAddr = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                        .setParameter(1, order.getOrDeliAddr())
                        .setParameter(2, no);

                Integer result = uptQuery.executeUpdate();

                log.debug("배송지 주소(orDeliAddr) {} -> {} 변경됨. {} 건 업데이트", order.getOrDeliAddr(), oldOrder.getOrDeliAddr(), result);
            }

//            02.코디배정 푸시
            if(order.getEpCd() != null && order.getEpCd() > 0) {
                if(oldOrder.getEpCd() == null || oldOrder.getEpCd() <= 0) { // 코디 신규 배정
                    iPushGubun = 2;
                } else if(!order.getEpCd().equals(oldOrder.getEpCd())) { // 코디 변경
                    iPushGubun = 3;
                }
            }

//            04.주문접수완료 푸시
            if(!StringUtils.isEmpty(order.getOrStatus())) {
                if(!order.getOrStatus().equals(oldOrder.getOrStatus()) && "0104".equals(order.getOrStatus())) { // 주문상태 변경및 접수완료
                    iPushGubun = 4;
                }
            }

//            05.배송완료
            if("0110".equals(order.getOrStatus())) {
                if(!order.getOrStatus().equals(oldOrder.getOrStatus())) { // 주문상태 변경일 때만
                    iPushGubun = 1; // 설문조사
                }

                if(!"Y".equals(order.getOrChargeType())) { // 05.배송완료 후 미결제 시
                    pushMobileService.sendPayRequire(order);
                }
            }

            //        주문 마스터 정보의 주문상태를 변경할 경우, 주문에 속한 품목정보도 모두 같은 주문상태로 변경되어야 합니다.
            if(order.getOrStatus() != null && !order.getOrStatus().equals(oldOrder.getOrStatus())) {
                Integer iResult = tbOrderItemsService.changeStatus(order.getOrCd(), order.getOrStatus());
            }
        }

        // 주문상태 : "수거완료" : "수거일시", "배송완료" : "배송일시"
        if("0103".equals(order.getOrStatus())) {
            if(order.getOrVisitDt() == null) {
                order.setOrVisitDt(new Date());
            }
        }

        if("0110".equals(order.getOrStatus())) {
            if(order.getOrDeliVisitDt() == null) {
                order.setOrDeliVisitDt(new Date());
            }
        }

//        return tbOrderRepository.save(order);
        order = tbOrderRepository.save(order);

        //        푸시실행
        if(iPushGubun > 0) {
            switch (iPushGubun) {
                case 1: // 설문조사
                    pushMobileService.sendDeliComplete(order);
                    break;

                case 2: // 코디 신규 배정
                    pushMobileService.sendCodiAssign(order);
                    break;

                case 3: // 코디 변경
                    pushMobileService.sendCodiModify(order);
                    break;

                case 4: // 주문접수완료
                    pushMobileService.sendStatus04(order);
                    break;

                case 5: // 배송완료 후 미결제 시
                    pushMobileService.sendPayRequire(order);
                    break;
            }
        }

        return order;
    }

    @Override
    public void deleteTbOrder(Long id) {
        tbOrderRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbOrder2(Long id) {
        TbOrder order = tbOrderRepository.findOne(id);
        order.setDelYn("Y");
        tbOrderRepository.save(order);
    }

    @Modifying
    @Transactional
    @Override
    public int changeSt2(Long no, String status) throws IOException, GcmMulticastLimitExceededException {
        Query uptQuery = entityManager.createQuery("update TbOrder set orStatus = ?1 where orCd = ?2")
                .setParameter(1, status)
                .setParameter(2, no);

        Integer result = uptQuery.executeUpdate();

//        12-16일 품목 리스트도 처리
        uptQuery = entityManager.createQuery("update TbOrderItems set itStatus = ?1 where orCd = ?2 and (delYn is null or delYn != 'Y')")
                .setParameter(1, status)
                .setParameter(2, no);

        result = uptQuery.executeUpdate();

//        04.주문접수완료 푸시 : 주문이 "접수완료" 처리 될 경우 해당회원에게 "고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다." 푸시가 발송되어야 합니다.
        if("0104".equals(status)) {
            pushMobileService.sendStatus04(tbOrderRepository.findOne(no));
        }

//        03월 11일 수거완료시 수거일시 입력
        if("0103".equals(status)) {
            TbOrder tbOrder = tbOrderRepository.findOne(no);
            tbOrder.setOrVisitDt(new Date());
            tbOrderRepository.save(tbOrder);
        }

        return result;
    }

/*//    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    @Bean
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    @Bean
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }*/

//    접수처리 검색
//    http://antop.tistory.com/30
    @Override
    public List<?> getOrderList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

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

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        } else {
//            strWhere = SynapseCM.whereConcatenator(strWhere, "(b.IT_STATUS in ('0101', '0104') OR b.IT_STATUS IS NULL)"); // 디폴트로 주문상태가 주문접수(0101), 접수완료(0104)
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0103', '0104')"); // 주문상태가 수거완료와 접수완료만 기본 표시 되게 필요
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT  a.OR_CD AS orCd, b.ID,                                                       ");
        strQuery.append("     DATE_FORMAT(b.IT_VISIT_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,      ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS IT_REGI_DT,        ");
        strQuery.append("     a.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     d.BN_NM,                                                    ");
        strQuery.append("     c.MB_NIC_NM,                                                ");
        strQuery.append("     c.MB_TEL,                                                   ");

//        strQuery.append("     MB_ADDR AS IT_REQ_ADDR,                                     ");
        strQuery.append("    CASE                                                         ");
        strQuery.append("        WHEN MB_ADDR1 IS NULL AND MB_ADDR2 IS NULL THEN NULL     ");
        strQuery.append("        WHEN                                                     ");
        strQuery.append("            MB_ADDR1 IS NOT NULL                                 ");
        strQuery.append("                AND MB_ADDR2 IS NULL                             ");
        strQuery.append("        THEN                                                     ");
        strQuery.append("            MB_ADDR1                                             ");
        strQuery.append("        WHEN                                                     ");
        strQuery.append("            MB_ADDR1 IS NOT NULL                                 ");
        strQuery.append("                AND MB_ADDR2 IS NOT NULL                         ");
        strQuery.append("        THEN                                                     ");
        strQuery.append("            MB_ADDR1 + ' ' + MB_ADDR1                            ");
        strQuery.append("    END AS IT_REQ_ADDR,                                          ");

        strQuery.append("     b.IT_CNT,                                                   ");
        strQuery.append("     e.EP_NM AS EP_CD,                                           ");
        strQuery.append("     b.IT_MEMO,                                                  ");
        strQuery.append("     g.CD_NM as IT_STATUS                                                 ");
        strQuery.append(" FROM                                                            ");
        strQuery.append("     TB_ORDER AS a                                               ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                    ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                         ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_BRANCH AS d ON a.BN_CD = d.BN_CD                         ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD                       ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_ADDRESS AS f ON b.IT_REQ_ADDR = f.ID                     ");

        strQuery.append("        LEFT OUTER JOIN                  ");
        strQuery.append("    (SELECT                              ");
        strQuery.append("        CD_GP, CD_IT, CD_NM              ");
        strQuery.append("    FROM                                 ");
        strQuery.append("        TB_CODE                          ");
        strQuery.append("    WHERE                                ");
        strQuery.append("        CD_GP = '01' AND CD_IT != '00'   ");
        strQuery.append("            AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");

        strQuery.append(strWhere);

//        수거완료일자 기준으로 오름차순 정렬 필요
        strQuery.append(" ORDER BY b.IT_VISIT_DT");

        Query selectQuery = em.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<TbOrderVO> list = (List<TbOrderVO>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    //    로그용 접수처리 검색
//    http://antop.tistory.com/30
    public List<?> getLogList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

//        주문일자 컬럼 필요
        if(StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.REGI_DT >= :from");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.REGI_DT <= :to");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "d.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "d.MB_TEL like :keyword");
            }
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        }

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                     ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS REGI_DT,   ");
        strQuery.append("     a.OR_CD,                                               ");
        strQuery.append("     CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     c.BN_NM,                                               ");
        strQuery.append("     d.MB_NIC_NM,                                           ");
        strQuery.append("     d.MB_TEL,                                              ");
        strQuery.append("     b.IT_TAC,                                              ");
        strQuery.append("     '상품명' AS PD_LVL,                                    ");
        strQuery.append("     b.IT_CNT AS CNT,                                       ");
        strQuery.append("     e.EP_NM AS EP_CD,                                      ");
        strQuery.append("     f.EP_NM AS DELI_EP,                                      ");
        strQuery.append("     a.OR_PRICE,                                            ");
        strQuery.append("     g.CD_NM AS IT_STATUS,                                  ");
        strQuery.append("     a.OR_MEMO,                                             ");
        strQuery.append("     a.EVT_NM AS evtNm,                                     ");
        strQuery.append("     DATE_FORMAT(a.REGI_DT, '%Y-%m-%d') AS regiDt,          ");
        strQuery.append("     a.USER AS user                                         ");
        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE                                                ");
        strQuery.append(" FROM                                                       ");
        strQuery.append("     TB_ORDER AS a                                          ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD               ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     TB_BRANCH AS c ON a.BN_CD = c.BN_CD                    ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     TB_MEMBER AS d ON a.MB_CD = d.MB_CD                    ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD                  ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     TB_EMPLOYEE AS f ON a.OR_DELI_EP_CD = f.EP_CD          ");
        strQuery.append("         LEFT OUTER JOIN                                    ");
        strQuery.append("     (SELECT                                                ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                ");
        strQuery.append("     FROM                                                   ");
        strQuery.append("         TB_CODE                                            ");
        strQuery.append("     WHERE                                                  ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00'                     ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");

        strQuery.append(strWhere);
        strQuery.append(" ORDER BY b.REGI_DT DESC");

        Query selectQuery = em.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<TbOrderVO> list = (List<TbOrderVO>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

//    https://adrianhummel.wordpress.com/2010/07/02/composed-specifications-using-jpa-2-0/
    public List<?> getOrderList2() {

/*        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TbOrder> q = cb.createQuery(TbOrder.class);
        Root<TbOrder> player = q.from(TbOrder.class);

        q.where(specification.toPredicate(cb, q, player));*/

        List<TbOrderVO> list = (List<TbOrderVO>) em.createNativeQuery("SELECT OR_CD as orCd, EP_CD as epCd from TB_ORDER")
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    public List<TbOrderVO> getOrderList0() {
//        List<TbOrder> list = (List<TbOrder>)em.createQuery("from TbOrder").getResultList();

        /*        List<TbOrder> list = (List<TbOrder>)currentSession().createSQLQuery("SELECT * from TB_ORDER")
                .addEntity(TbOrder.class)
                .list();*/

/*        List<TbOrder> list = (List<TbOrder>)em.createNativeQuery("SELECT * from TB_ORDER")
                .unwrap(SQLQuery.class)
                .addEntity(TbOrder.class)
                .getResultList();*/

/*        Query selectQuery = em.createNativeQuery("SELECT OR_CD, EP_CD from TB_ORDER");
        List<TbOrder> list = selectQuery
                .unwrap(SQLQuery.class)
//                .addEntity(TbOrder.class)
                .list();*/

        List<TbOrderVO> list = (List<TbOrderVO>) em.createNativeQuery("SELECT * from TB_ORDER")
                .unwrap(SQLQuery.class)
                .addEntity(TbOrder.class)
                .list();

        return list;
    }

    /**
     * 모바일 주문생성
     * @param order
     * @return
     */
    @Transactional
    @Override
    public TbOrder saveMoOrder(MoOrder order) {

//        1월22일 연락처를 일단 회원정보에 업데이트 한다 -> 추후 백민의 협의 결과에 따라 수정함
        TbMember tbMember = tbMemberRepository.findOne(order.getMbCd());
        tbMember.setMbTel(order.getMbTel());
        tbMemberRepository.save(tbMember);

//        최근주소 검색
        /*Query selectQuery = entityManager.createQuery("FROM TbAddress as t" +
                " where t.mbCd = ?1 and t.mbLat = ?2 and t.mbLng = ?3")
                .setParameter(1, order.getMbCd())
                .setParameter(2, order.getMbLat())
                .setParameter(3, order.getMbLat());

        TbAddress address = (TbAddress) selectQuery.getSingleResult();*/

        TbAddress address = tbAddressRepository.getAddress(order.getMbCd(), order.getMbLat(), order.getMbLng());

//        최근주소 Insert
        if(address == null) {
            address = new TbAddress();

            address.setEvtNm("신규");
            address.setUser("모바일");
            address.setMbCd(order.getMbCd());
            address.setRegiDt(new Date());
            address.setMbAddr1(order.getMbAddr1());
            address.setMbAddr2(order.getMbAddr2());
            address.setMbLat(order.getMbLat());
            address.setMbLng(order.getMbLng());

            address = tbAddressRepository.save(address);
        }

        //        주문 데이터 Insert
        TbOrder newOrder = new TbOrder();
        newOrder.setEvtNm("신규");
        newOrder.setUser("모바일");
        newOrder.setRegiDt(new Date());
        newOrder.setOrChannel("2"); // 접숙채널

        newOrder.setMbCd(order.getMbCd());
        newOrder.setOrStatus("0101"); // 주문상태 : 주문접수
        newOrder.setOrReqDt(order.getOrReqDt());
        newOrder.setOrReqMemo(order.getOrReqMemo());
        newOrder.setOrReqAddr(address.getId().toString());
        newOrder.setOrNonMberYn("M"); // 회원접수
        newOrder.setOrOrderDt(new Date());

//        3월 11일 배송지 주소 디폴트 처리
        newOrder.setOrDeliAddr(address.getId().toString());

        //            일단 주소의 마지막 문장을 가지고 속하는 지점 확인
        /*String[] split = address.getMbAddr1().split(" ");
        String strLastAddr1 = split[split.length - 1];
        log.debug("주문 주소 = {}", strLastAddr1);

        List<TbBranchLocs> tbBranchLocList = tbBranchLocsRepository.getTbBranchLocsByBlNm(strLastAddr1);
        if(tbBranchLocList != null && tbBranchLocList.size() > 0) {
            TbBranchLocs tbBranchLocs = tbBranchLocList.get(0);
            newOrder.setBnCd(tbBranchLocs.getBnCd());
            log.debug("주문 지점 = {}", newOrder.getBnCd());
        }*/

        TbBranchLocs tbBranchLocs = tbBranchLocsService.getTbBranchLocsByAddr1(address.getMbAddr1());
        if(tbBranchLocs != null) newOrder.setBnCd(tbBranchLocs.getBnCd());

        TbOrder saveOrder = tbOrderRepository.save(newOrder);

        return tbOrderRepository.save(newOrder);
    }
}
