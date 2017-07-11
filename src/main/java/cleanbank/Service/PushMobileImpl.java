package cleanbank.Service;

import cleanbank.domain.*;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.repositories.*;
import cleanbank.viewmodel.MoOrder2;
import cleanbank.viewmodel.MoPush;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.beanutils.PropertyUtils.copyProperties;

@Service
public class PushMobileImpl implements PushMobileService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GcmService gcmService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private ITbMemberService tbMemberService;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbEmployeeService tbEmployeeService;

    @Autowired
    private TbPushHistoryService tbPushHistoryService;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 11."배송중 상태에서 '출발'버튼 클릭 시, 해당회원에게 푸시가 발송되어야 합니다.
         결제완료시: ""고객님께서 맡겨주신 옷들이 모두 세탁완료되었습니다.""
         미결제 시: ""고객님께서 맡겨주신 옷들이 모두 세탁완료되었습니다. 결제 부탁드립니다."""

     * @param orCd
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendDeliStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException {

//        푸시전송
        return this.sendStart(orCd);
    }

    /**
     * 10."수거중 상태에서 '출발'버튼 클릭 시, 해당회원에게 푸시가 발송되어야 합니다.
     "  "30분내로 백민코디가 방문예정입니다. 맡기실 옷들을 확인해주세요"

     1. 코디앱 출발버튼 터치 시, 주문상태 변경필요
     코디앱에서 주문상태가 "접수완료"일 경우에 출발버튼을 터치할 경우, 주문상태가 "수거중" 으로 변경됩니다.
     주문상태가 "백민입고"일 경우에 출발버튼을 터치할 경우, 주문상태는 "배송중"으로 변경됩니다.

     * @param orCd
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendReqStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException {

//        푸시전송
        return this.sendStart(orCd);
    }

    @Override
    public ResponseEntity<?> sendStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException {
        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

//        if("0104".equals(tbOrder.getOrStatus())) { // 접수완료
        if("0101".equals(tbOrder.getOrStatus())) { // 주문접수
            tbOrder.setOrStatus("0102"); // 수거중
            tbOrder = tbOrderRepository.save(tbOrder);
        } else if("0108".equals(tbOrder.getOrStatus())) { // 백민입고
            tbOrder.setOrStatus("0109"); // 배송중
            tbOrder = tbOrderRepository.save(tbOrder);
        }

        String strText = null;

//        수거중
        if("0102".equals(tbOrder.getOrStatus())) {
            strText = "30분내로 크린매니져가 방문예정입니다. 맡기실 옷들을 확인해주세요";
        } else if("0109".equals(tbOrder.getOrStatus())) { // 배송중
            strText = "고객님께서 맡겨주신 옷들이 모두 세탁완료되었습니다.";

            if(!"Y".equals(tbOrder.getOrChargeType())) strText += " 결제 부탁드립니다.";
        }

        if(StringUtils.isEmpty(strText)) return new ResponseEntity<>("주문상태가 주문접수(수거중) 또는 빨래통입고가(배송중이) 아닙니다!", HttpStatus.BAD_REQUEST);

        MoPush moPush = new MoPush();

        moPush.setPushGubun(9);
        moPush.setPushTitle(strText);
        moPush.setPushBody(strText);

        moPush.setPushData(tbOrder);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    @Override
    public ResponseEntity<?> sendPromotion(TbPromotion promotion) throws IOException, GcmMulticastLimitExceededException {
        log.error("sendPromotion 시작!============================================================");
        MoPush moPush = new MoPush();

        moPush.setPushGubun(9);
        moPush.setPushTitle("고객님 앞으로 "+ promotion.getPoNm() +" 쿠폰이 발급되었습니다.");
        moPush.setPushBody("고객님 앞으로 "+ promotion.getPoNm() +" 쿠폰이 발급되었습니다.");

        moPush.setPushData(promotion);

//        푸시전송
        return this.sendPushMsgAll(moPush);
    }

    @Override
    public ResponseEntity<?> sendJoinPromotion(TbMember tbMember, TbPromotion promotion) throws IOException, GcmMulticastLimitExceededException {
        log.error("회원가입 쿠폰 푸시 시작!============================================================");
        MoPush moPush = new MoPush();

        moPush.setPushGubun(9);
        moPush.setPushTitle("고객님 앞으로 "+ promotion.getPoNm() +"이 발급되었습니다.");
        moPush.setPushBody("고객님 앞으로 "+ promotion.getPoNm() +"이 발급되었습니다.");

        moPush.setPushData(promotion);

//        푸시전송
        return this.sendByMbId(tbMember.getMbCd(), moPush);
    }

    /**
     * 08.설문조사시: ""고객님 앞으로 100포인트가 발급되었습니다."""
     * @param tbMember
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendPollMsg(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException {
//        푸시전송
        return this.sendMbrPoint(tbMember, 8, 100);
    }

//

    /**
     * 07.추천코드 입력: ""고객님 앞으로 1000포인트가 발급되었습니다.""
     */
    @Override
    public ResponseEntity<?> sendSucode(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException {
//        푸시전송
        return this.sendMbrPoint(tbMember, 7, 1000);
    }

    /**
     * 06.회원가입: "고객님 앞으로 3000포인트가 발급되었습니다."
     * 신규회원 가입시 : 포인트 1000 + 쿠폰 2000
     */
    @Override
    public ResponseEntity<?> sendJoinPoint(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException {
//        푸시전송
        return this.sendMbrPoint(tbMember, 6, 1000);
    }

    @Override
    public ResponseEntity<?> sendMbrPoint(TbMember tbMember, final Integer pushNo, final Integer point) throws IOException, GcmMulticastLimitExceededException {
        MoPush moPush = new MoPush();

        moPush.setPushGubun(pushNo);
        moPush.setPushTitle("고객님 앞으로 "+ point +"포인트가 발급되었습니다.");
        moPush.setPushBody("고객님 앞으로 "+ point +"포인트가 발급되었습니다.");

        moPush.setPushData(tbMember);

//        푸시전송
        return this.sendByMbId(tbMember.getMbCd(), moPush);
    }

    /**
     * 05.배송완료 후 미결제 시 :  해당회원에게 "고객님께서 받아보신 서비스에 대한 결제가 이루어지지 않았습니다. 결제 부탁드립니다." 푸시가 발송되어야 합니다.
     * @param tbOrder
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendPayRequire(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException {
        MoPush moPush = new MoPush();

        moPush.setPushGubun(5);
        moPush.setPushTitle("고객님께서 받아보신 서비스에 대한 결제가 이루어지지 않았습니다. 결제 부탁드립니다.");
        moPush.setPushBody("고객님께서 받아보신 서비스에 대한 결제가 이루어지지 않았습니다. 결제 부탁드립니다.");

        moPush.setPushData(tbOrder);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    /**
     * 04.주문접수완료 푸시 : 주문이 "접수완료" 처리 될 경우 해당회원에게 "고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다." 푸시가 발송되어야 합니다.
     * @param tbOrder
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendStatus04(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException {
        MoPush moPush = new MoPush();

        moPush.setPushGubun(4);
        moPush.setPushTitle("고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다.");
        moPush.setPushBody("고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다.");

        moPush.setPushData(tbOrder);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    /**
     * 03.코디배정 수정 푸시 : 코디 수정 시, 해당회원에게 "고객님의 주문에 담당코디가 재배정되었습니다." 푸시가 발송
     *
     * @param tbOrder
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Override
    public ResponseEntity<?> sendCodiModify(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Integer epCd = tbOrder.getEpCd();
        if(StringUtils.isEmpty(epCd) || epCd <= 0) return new ResponseEntity<>("해당 주문에 수거 크린매니저가 배정되지 않았습니다!", HttpStatus.BAD_REQUEST);

        MoPush moPush = new MoPush();

        moPush.setPushGubun(3);
        moPush.setPushTitle("고객님의 주문에 담당크린매니저가 재배정되었습니다.");
        moPush.setPushBody("고객님의 주문에 담당크린매니저가 재배정되었습니다.");

        TbEmployee tbEmployee = tbEmployeeService.getTbEmployeeById(epCd);
        if(tbEmployee == null) return new ResponseEntity<>("해당 수거 크린매니저 정보를 찾을수 없습니다!", HttpStatus.BAD_REQUEST);

        moPush.setPushData(tbEmployee);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    /**
     * 02.코디배정 푸시 : 코디 배정 시, 해당회원에게 "고객님의 주문에 담당코디가 배정되었습니다." 푸시가 발송되어야 합니다.
     *
     * @param tbOrder
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Override
    public ResponseEntity<?> sendCodiAssign(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Integer epCd = tbOrder.getEpCd();
        if(StringUtils.isEmpty(epCd) || epCd <= 0) return new ResponseEntity<>("해당 주문에 수거 크린매니저가 배정되지 않았습니다!", HttpStatus.BAD_REQUEST);

        MoPush moPush = new MoPush();

        moPush.setPushGubun(2);
        moPush.setPushTitle("고객님의 주문에 담당크린매니저가 배정되었습니다.");
        moPush.setPushBody("고객님의 주문에 담당크린매니저가 배정되었습니다.");

        TbEmployee tbEmployee = tbEmployeeService.getTbEmployeeById(epCd);
        if(tbEmployee == null) return new ResponseEntity<>("해당 수거 크린매니저 정보를 찾을수 없습니다!", HttpStatus.BAD_REQUEST);

        moPush.setPushData(tbEmployee);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    /**
     * 설문조사-설문조사하기 : "설문에 참여하시고 100p 받아가세요~ (배송완료 시)""
     * @param tbOrder
     * @return
     */
    @Override
    public ResponseEntity<?> sendDeliComplete(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        주문상태를 배송완료로
        tbOrder.setOrStatus("0110");
        tbOrderRepository.save(tbOrder);

        MoPush moPush = new MoPush();

        moPush.setPushGubun(1);
        moPush.setPushTitle("설문에 참여하시고 100p 받아가세요~");
        moPush.setPushBody("설문에 참여하시고 100p 받아가세요~");
//        moPush.setPushData(tbOrder);
        MoOrder2 order = new MoOrder2();
        copyProperties(order, tbOrder);
        moPush.setPushData(order);

//        푸시전송
        return this.sendByMbId(tbOrder.getMbCd(), moPush);
    }

    @Override
    public ResponseEntity<?> sendPushMsgAll(MoPush moPush) throws IOException, GcmMulticastLimitExceededException {
        log.error("PushMObileImpl->sendPushMsgAll 시작!============================================================");
        List<String> regIdList = new ArrayList<String>();

        List<TbMember> memberList = tbMemberRepository.listAllMembers();

        for(TbMember tbMember : memberList) {
            String pushToken = tbMember.getMbPushToken();
            if(!StringUtils.isEmpty(pushToken) && pushToken.length() > 10) {
                regIdList.add(pushToken);
                //        푸시 전송 이력 저장
                tbPushHistoryService.addPushHistory(tbMember.getMbCd(), moPush.getPushTitle());
            }
        }

        log.debug("{} 명의 사용자에게 푸시 메세지 전송!", regIdList.size());

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(moPush);

        GcmMulticatResult result = gcmService.sendPushMsg(moPush.getPushTitle(), jsonInString, regIdList);

        moPush.setPushResult(result);

        log.error("PushMObileImpl->sendPushMsgAll 끝!============================================================");

        return new ResponseEntity<>(moPush, HttpStatus.OK);
    }

    /**
     * 회원id, MoPush 로 푸시 전송
     *
     * @param mbCd
     * @param moPush
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendByMbId(final Integer mbCd, MoPush moPush) throws IOException, GcmMulticastLimitExceededException {
        if(mbCd == null) return new ResponseEntity<>("mbCd(회원id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember member = tbMemberRepository.findOne(mbCd);
        if(member == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        String pushToken = member.getMbPushToken();
        if(StringUtils.isEmpty(pushToken) || pushToken.length() < 10) return new ResponseEntity<>(pushToken +" 해당 회원의 Push 토큰이 잘못되었습니다!", HttpStatus.NOT_FOUND);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(moPush);
//        moPush.setPushBody(jsonInString);

        log.debug("회원 id로 푸시 전송 : 제목 = {}, 내용 = {}", moPush.getPushTitle(), jsonInString);

        GcmMulticatResult result = gcmService.sendPushMsg(moPush.getPushTitle(), jsonInString, pushToken);

        log.debug("회원 id로 푸시 전송 결과 : {}", result);

        moPush.setPushResult(result);

        //        푸시 전송 이력 저장
        tbPushHistoryService.addPushHistory(mbCd, moPush.getPushTitle());

        return new ResponseEntity<>(moPush, HttpStatus.OK);
    }

    /**
     * 회원id, 제목, 본문으로 푸시 전송
     *
     * @param mbCd
     * @param pushTitle
     * @param pushBody
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @Override
    public ResponseEntity<?> sendByMbId(final Integer mbCd, final String pushTitle, final String pushBody) throws IOException, GcmMulticastLimitExceededException {
        if(mbCd == null) return new ResponseEntity<>("mbCd(회원id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember member = tbMemberRepository.findOne(mbCd);
        if(member == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        String pushToken = member.getMbPushToken();
        if(StringUtils.isEmpty(pushToken) || pushToken.length() < 10) return new ResponseEntity<>(pushToken +" 해당 회원의 Push 토큰이 잘못되었습니다!", HttpStatus.NOT_FOUND);

        log.debug("회원 id로 푸시 전송 : 제목 = {}, 내용 = {}", pushTitle, pushBody);

        GcmMulticatResult result = gcmService.sendPushMsg(pushTitle, pushBody, pushToken);

//        푸시 전송 이력 저장
        tbPushHistoryService.addPushHistory(mbCd, pushTitle);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
