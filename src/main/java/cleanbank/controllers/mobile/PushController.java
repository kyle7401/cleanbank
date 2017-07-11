package cleanbank.controllers.mobile;

import cleanbank.Service.GcmService;
import cleanbank.Service.PushMobileService;
import cleanbank.domain.*;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.model.impl.GcmPushImpl2;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;
import cleanbank.repositories.TbMemberRepository;
import cleanbank.repositories.TbOrderRepository;
import cleanbank.repositories.TbPushHistoryRepository;
import cleanbank.viewmodel.MoPush;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/push")
public class PushController {

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar2;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar2.registerCustomEditors(binder);
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GcmService gcmService;

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    @Autowired
    private PushMobileService pushMobileService;

    @Resource
    private GcmPushImpl2 gcmPush2;

    @Autowired
    private TbPushHistoryRepository tbPushHistoryRepository;

//    ==========================================================================================

    @RequestMapping(value = "/sendByMbCd2", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> sendByMbCd2(@RequestParam(value="mbCd", required=false) final Integer mbCd,
                                        HttpServletRequest req) throws MessagingException, IOException, GcmMulticastLimitExceededException {

        if(mbCd == null) return new ResponseEntity<>("mbCd(회원id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        MoPush moPush = new MoPush();

        moPush.setPushGubun(2);
        moPush.setPushTitle("고객님의 주문에 담당코디가 배정되었습니다.");
        moPush.setPushBody("고객님의 주문에 담당코디가 배정되었습니다.");

        moPush.setPushData(null);

        return pushMobileService.sendByMbId(mbCd, moPush);
    }

/*    @RequestMapping(value = "/sendReqStart", method = RequestMethod.GET)
    public ResponseEntity<?> sendJoinPoint(@RequestParam(value="mbCd", required=false) final Long mbCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return pushMobileService.sendReqStart(mbCd);
    }*/

    /**
     * 회원별 푸시 전송이력(목록) 확인
     * @param spec
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "/getPushHistory", method = RequestMethod.GET)
    public ResponseEntity<?> getPushHistory(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbPushHistory> spec,
            @PageableDefault(sort = {"regDt"}, direction = Sort.Direction.DESC, size = 1000) Pageable pageable, Model model) {


        Iterable<TbPushHistory> list = tbPushHistoryRepository.findAll(spec, pageable);

        List<TbPushHistory> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @RequestMapping(value = "/sendCodiTest", method = RequestMethod.GET)
    public ResponseEntity<?> sendCodiTest() throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        GcmPushInfo info = new GcmPushInfo();

        info.setTitle("strTitle");
        info.setData("strBody");

        List<String> regIdList = new ArrayList<String>();
        regIdList.add("1");
        info.setRegIdList(regIdList);

        GcmMulticatResult result = gcmPush2.sendPush(info);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/sendDeliStart", method = RequestMethod.GET)
    public ResponseEntity<?> sendDeliStart(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return pushMobileService.sendDeliStart(orCd);
    }

    @RequestMapping(value = "/sendReqStart", method = RequestMethod.GET)
    public ResponseEntity<?> sendReqStart(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return pushMobileService.sendReqStart(orCd);
    }

    @RequestMapping(value = "/sendPayRequire", method = RequestMethod.GET)
    public ResponseEntity<?> sendPayRequire(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        pushMobileService.sendPayRequire(tbOrder); // 결제요청

        return pushMobileService.sendDeliComplete(tbOrder); // 설문조사
    }

    /**
     * 04.주문접수완료 푸시 : 주문이 "접수완료" 처리 될 경우 해당회원에게 "고객님의 옷들의 검수가 완료되었습니다. 내역을 확인해보세요. 결제 부탁드립니다." 푸시가 발송되어야 합니다.
     * @param orCd
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/sendStatus04", method = RequestMethod.GET)
    public ResponseEntity<?> sendStatus04(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return pushMobileService.sendStatus04(tbOrder);
    }

    /**
     * 03.코디배정 수정 푸시 : 코디 수정 시, 해당회원에게 "고객님의 주문에 담당코디가 재배정되었습니다." 푸시가 발송되어야 합니다.
     * @param orCd
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/sendCodiModify", method = RequestMethod.GET)
    public ResponseEntity<?> sendCodiModify(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return pushMobileService.sendCodiModify(tbOrder);
    }

    /**
     * 02.코디배정 푸시 : 코디 배정 시, 해당회원에게 "고객님의 주문에 담당코디가 배정되었습니다." 푸시가 발송되어야 합니다.
     * @param orCd
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/sendCodiAssign", method = RequestMethod.GET)
    public ResponseEntity<?> sendCodiAssign(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return pushMobileService.sendCodiAssign(tbOrder);
    }

    /**
     * 설문조사-설문조사하기 : "설문에 참여하시고 100p 받아가세요~ (배송완료 시)""
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/sendDeliComplete", method = RequestMethod.GET)
    public ResponseEntity<?> sendDeliComplete(@RequestParam(value="orCd", required=false) final Long orCd) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return pushMobileService.sendDeliComplete(tbOrder);
    }

    /**
     * 36.푸시전송(디바이스 token)
     * @param pushTitle
     * @param pushBody
     * @param deviceToken
     * @param req
     * @return
     * @throws MessagingException
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @RequestMapping(value = "/sendByToken", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> sendByToken(@RequestParam(value="pushTitle", required=false) final String pushTitle,
                                         @RequestParam(value="pushBody", required=false) final String pushBody,
                                         @RequestParam(value="deviceToken", required=false) final String deviceToken,
                                         HttpServletRequest req) throws MessagingException, IOException, GcmMulticastLimitExceededException {

        if(StringUtils.isEmpty(pushTitle)) return new ResponseEntity<>("pushTitle(제목) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(pushBody)) return new ResponseEntity<>("pushBody(본문) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(deviceToken)) return new ResponseEntity<>("deviceToken(디바이스 Instance ID의 token 값) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        log.debug("/push/sendByToken 제목 = {}, 내용 = {}", pushTitle, pushBody);

        GcmMulticatResult result = gcmService.sendPushMsg(pushTitle, pushBody, deviceToken);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 37.푸시전송(회원id)
     * @param pushTitle
     * @param pushBody
     * @param mbCd
     * @param req
     * @return
     * @throws MessagingException
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @RequestMapping(value = "/sendByMbCd", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> sendByMbId(@RequestParam(value="pushTitle", required=false) final String pushTitle,
                                        @RequestParam(value="pushBody", required=false) final String pushBody,
                                        @RequestParam(value="mbCd", required=false) final Integer mbCd,
                                        HttpServletRequest req) throws MessagingException, IOException, GcmMulticastLimitExceededException {

        if(StringUtils.isEmpty(pushTitle)) return new ResponseEntity<>("pushTitle(제목) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(pushBody)) return new ResponseEntity<>("pushBody(본문) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(mbCd == null) return new ResponseEntity<>("mbCd(회원id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        /*TbMember member = tbMemberRepository.findOne(mbCd);
        if(member == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        String deviceToken = member.getMbDeviceToken();
        if(StringUtils.isEmpty(deviceToken) || deviceToken.length() < 10) return new ResponseEntity<>(deviceToken +" 해당 회원의 디바이스 토큰이 잘못되었습니다!", HttpStatus.NOT_FOUND);

        log.debug("/push/sendByMbCd 제목 = {}, 내용 = {}", pushTitle, pushBody);

        GcmMulticatResult result = gcmService.sendPushMsg(pushTitle, pushBody, deviceToken);
        return new ResponseEntity<>(result, HttpStatus.OK);*/

        return pushMobileService.sendByMbId(mbCd, pushTitle, pushBody);
    }

    /**
     * 38.푸시전송(모든회원)
     * @param pushTitle
     * @param pushBody
     * @param req
     * @return
     * @throws MessagingException
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @RequestMapping(value = "/sendAllMbr", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> sendAllMbr(@RequestParam(value="pushTitle", required=false) final String pushTitle,
                                        @RequestParam(value="pushBody", required=false) final String pushBody,
                                        HttpServletRequest req) throws MessagingException, IOException, GcmMulticastLimitExceededException {

        if(StringUtils.isEmpty(pushTitle)) return new ResponseEntity<>("pushTitle(제목) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(pushBody)) return new ResponseEntity<>("pushBody(본문) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        log.debug("/push/sendAllMbr 제목 = {}, 내용 = {}", pushTitle, pushBody);

        GcmMulticatResult result = gcmService.sendPushMsgAll(pushTitle, pushBody);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
