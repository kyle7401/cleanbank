package cleanbank.controllers.mobile;

import cleanbank.Service.*;
import cleanbank.domain.*;
import cleanbank.repositories.*;
import cleanbank.viewmodel.*;
import com.inicis.inipay4.INIpay;
import com.inicis.inipay4.util.INIdata;
import net.coobird.thumbnailator.Thumbnails;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hyoseop on 2015-12-28.
 */

@RestController
@RequestMapping("/api")
public class ClientController {

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar2;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar2.registerCustomEditors(binder);
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbMemberService tbMemberService;

    @Autowired
    private TbAddressRepository tbAddressRepository;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    /*@Autowired
    TbMemberBillinfoService tbMemberBillinfoService;*/

    @Autowired
    private TbMemberBillinfoRepository tbMemberBillinfoRepository;

    @Autowired
    private TbFaqRepository tbFaqRepository;

    @Autowired
    private TbAddressService tbAddressService;

    @Autowired
    private TbProductRepository tbProductRepository;

    @Autowired
    private TbCodeRepository tbCodeRepository;

/*    @Autowired
    private TbOrderItemsService tbOrderItemsService;*/

    @Autowired
    private TbOrderItemsRepository tbOrderItemsRepository;

    @Autowired
    private TbPictureRepository tbPictureRepository;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private TbPromotionRepository tbPromotionRepository;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    @Autowired
    private ITbPromotionService tbPromotionService;

    @Autowired
    private GcmregidsRepository gcmregidsRepository;

    @Autowired
    private JavaMailSender javaMailSender;

//      상점아이디
    final static String P_MID = "cleanbank0";

    @Autowired
    private PushMobileService pushMobileService;

    @Autowired
    private BillResultRepository billResultRepository;

    @Autowired
    private TbBranchLocsService tbBranchLocsService;

    @Autowired
    private TbBranchService tbBranchService;

    @Autowired
    private TbBranchRepository tbBranchRepository;

    @Autowired
    private OrderListService orderListService;

    @Autowired
    private TbSalesInfoService tbSalesInfoService;

    @Autowired
    private ITbPointService tbPointService;

    /**
     * INIpay 환경 파일
     */
    String inipayHome = "/home/cleanbank/INIpay4JAVA";
    String logMode = "DEBUG"; // 반드시 대문자로 INFO, DEBUG로 설정
    String keyPW = "1111";
//    ==========================================================================================

    @RequestMapping(value = "/client/getServicePossible", method = RequestMethod.GET)
    public ResponseEntity<?> getServicePossible(@RequestParam(value="mbAddr1", required=false) final String mbAddr1) throws ParseException {
        Integer bnCd = 0;
        TbBranch tbBranch = null;

//        주소동으로 지점번호 확인
        if (!StringUtils.isEmpty(mbAddr1)) {
            TbBranchLocs tbBranchLocs = tbBranchLocsService.getTbBranchLocsByAddr1(mbAddr1);
            if (tbBranchLocs != null) {
                bnCd = tbBranchLocs.getBnCd();
                tbBranch = tbBranchRepository.findOne(bnCd);
            }
        }

//        지점이 없으면 첫번째 지점으로
        /*if(tbBranch == null) {
            List<TbBranch> tbBranchs = tbBranchRepository.getBranchList();
            tbBranch = tbBranchs.get(0);
        }*/

//        if(bnCd != null && bnCd > 0) tbBranch = tbBranchService.getTbBranchById(bnCd);
        if (tbBranch == null) return new ResponseEntity<>("서비스 지역이 아닙니다!", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tbBranch, HttpStatus.OK);
    }

    /*코드  카드사 이름  코드  카드사 이름
    01  하나 (구 외환)
    03  롯데
    04  현대
    06  국민
    11  BC
    12  삼성
    14  신한
    15  한미
    16  NH
    17  하나 SK
    21  해외비자
    22  해외마스터
    23  JCB
    24  해외아멕스
    25  해외다이너스*/

    /**
     * 모바일 앱에서 본인인증 승인 처리가 완료 되었기 때문에 db 처리만 하면 된다
     * @param request
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws UnknownHostException
     */
    @RequestMapping(value = "/client/inicis-authbill", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_authbill(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, UnknownHostException {
        String strResult = "inicis-authbill!!!!";

//        이니시스 결제결과 파라메터 확인
        StringBuilder strLog = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            strLog.append(paramName);
            strLog.append("\n");

            String[] paramValues = request.getParameterValues(paramName);

            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                strLog.append("\t" + paramValue);
                strLog.append("\n");
            }
        }

        log.debug("이니시스 nicis-authbill \n {}", strLog.toString());

        String resultcode = request.getParameter("resultcode");

        if("00".equals(resultcode)) {
//            db 처리

            String orderid = request.getParameter("orderid");
            int mbCd = Integer.parseInt(orderid);
            String mbCardNo = request.getParameter("cardno");

//            기존 등록여부 확인
            TbMemberBillinfo tbMemberBillinfo = tbMemberBillinfoRepository.findByMbCdAndMbCardNo(mbCd, mbCardNo);

            if(tbMemberBillinfo == null) {
                String mbCardCd = request.getParameter("cardcd");
                String mbCardNm = null;

                tbMemberBillinfo = new TbMemberBillinfo();

                tbMemberBillinfo.setMbCd(mbCd);
                tbMemberBillinfo.setMbBiilkey(request.getParameter("billkey"));
                tbMemberBillinfo.setMbCardCd(mbCardCd);
                tbMemberBillinfo.setMbCardNo(request.getParameter("cardno"));

                switch (mbCardCd) {
                    case "01":
                        mbCardNm = "하나 (구 외환)";
                        break;

                    case "03":
                        mbCardNm = "롯데";
                        break;

                    case "04":
                        mbCardNm = "현대";
                        break;

                    case "06":
                        mbCardNm = "국민";
                        break;

                    case "11":
                        mbCardNm = "BC";
                        break;

                    case "12":
                        mbCardNm = "삼성";
                        break;

                    case "14":
                        mbCardNm = "신한";
                        break;

                    case "15":
                        mbCardNm = "한미";
                        break;

                    case "16":
                        mbCardNm = "NH";
                        break;

                    case "17":
                        mbCardNm = "하나 SK";
                        break;

                    case "21":
                        mbCardNm = "해외비자";
                        break;

                    case "22":
                        mbCardNm = "해외마스터";
                        break;

                    case "23":
                        mbCardNm = "JCB";
                        break;

                    case "24":
                        mbCardNm = "해외아멕스";
                        break;

                    case "25":
                        mbCardNm = "해외다이너스";
                        break;
                }

                tbMemberBillinfo.setMbCardNm(mbCardNm);
                tbMemberBillinfo = tbMemberBillinfoRepository.save(tbMemberBillinfo);
            }

        } else {
            log.error("빌링 본인확인 실패!!!!");
        }

        strResult = "<script>window.location = 'cleanbank://billing';</script>";

        return strResult;
    }

    @RequestMapping(value = "/client/inicis-reqrealbill", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> inicis_reqrealbill(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, UnknownHostException {
//        String strResult = null;
        BillResult billResult = null;

        String billkey = request.getParameter("billkey");
        if(StringUtils.isEmpty(billkey)) return new ResponseEntity<>("billkey를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        주문번호
        String moid = request.getParameter("moid");
        if(StringUtils.isEmpty(moid)) return new ResponseEntity<>("주문번호(moid)를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        가격
        String price = request.getParameter("price");
        if(StringUtils.isEmpty(price)) return new ResponseEntity<>("price를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        주문정보
        TbOrder tbOrder = tbOrderService.getTbOrderById(Long.parseLong(moid));
        if(tbOrder == null) return new ResponseEntity<>("주문정보 존재하지 않음", HttpStatus.NOT_FOUND);

//        회원정보
        TbMember tbMember = tbMemberService.getTbMemberById(tbOrder.getMbCd());
        if(tbMember == null) return new ResponseEntity<>("회원정보 존재하지 않음", HttpStatus.NOT_FOUND);

//        이니시스 결제결과 파라메터 확인
        StringBuilder strLog = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            strLog.append(paramName);
            strLog.append("\n");

            String[] paramValues = request.getParameterValues(paramName);

            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                strLog.append("\t" + paramValue);
                strLog.append("\n");
            }
        }

        log.debug("이니시스 nicis-reqrealbill \n {}", strLog.toString());

        String resultcode = request.getParameter("resultcode");

//       request.setCharacterEncoding("euc-kr");
        //#############################################################################
        //# 1. 인스턴스 생성 #
        //####################
        INIpay inipay = new INIpay();
        INIdata data = new INIdata();

        //#############################################################################
        //# 2. 정보 설정 #
        //################
        data.setData("type", "reqrealbill");                             // 결제 type, 고정
        data.setData("inipayHome", inipayHome);                           // 이니페이가 설치된 절대경로
        data.setData("logMode", logMode);                                 // logMode
        data.setData("keyPW",keyPW);                                      // 키패스워드
        data.setData("subPgip","203.238.37.3");                           // Sub PG IP (고정)
        /*data.setData("mid", request.getParameter("mid"));                 // 상점아이디
        data.setData("uid", request.getParameter("mid") );                // INIpay User ID*/

        data.setData("mid", "100min0001");                 // 상점아이디
        data.setData("uid", "100min0001");                // INIpay User ID

//        data.setData("goodname", request.getParameter("goodname"));       // 상품명 (최대 40자)
        data.setData("goodname", "백의민족 정기결제");       // 상품명 (최대 40자)

//        data.setData("currency", request.getParameter("currency"));       // 화폐단위
        data.setData("currency", "WON");       // 화폐단위

//        data.setData("price", request.getParameter("price"));             // 가격
        data.setData("price", price);             // 가격

//        TODO : 회원정보 연동 필요
        /*data.setData("buyername", request.getParameter("buyername"));     // 구매자 (최대 15자)
        data.setData("buyertel", request.getParameter("buyertel"));       // 구매자이동전화
        data.setData("buyeremail", request.getParameter("buyeremail"));   // 구매자이메일*/

        data.setData("buyername", tbMember.getMbNicNm());     // 구매자 (최대 15자)
        data.setData("buyertel", tbMember.getMbTel());       // 구매자이동전화
        data.setData("buyeremail", tbMember.getMbEmail());   // 구매자이메일

        data.setData("paymethod", "Card");                                // 지불방법, 카드빌링
        data.setData("billkey", request.getParameter("billkey"));         // 빌링등록 키(빌키)

//        data.setData("moid", request.getParameter("moid"));               // 빌링등록 키(빌키)
        data.setData("moid", moid);               // 빌링등록 키(빌키)

        data.setData("cardpass", request.getParameter("cardpass"));       // 카드비번 앞자리 2자리
        data.setData("regnumber", request.getParameter("regnumber"));     // 주민번호 및 사업자번호
        data.setData("url", "http://cms.100min.co.kr/");              // 홈페이지 주소(URL)
        data.setData("uip", request.getRemoteAddr());                     // IP Addr

//        data.setData("cardquota", request.getParameter("cardquota"));
        data.setData("cardquota", "00");

//        data.setData("authentification", request.getParameter("authentification"));
        data.setData("authentification", "01");

        data.setData("authField1", request.getParameter("authField1"));
        data.setData("authField2", request.getParameter("authField2"));
        data.setData("authField3", request.getParameter("authField3"));
        data.setData("crypto", "execure");                                // Extrus 암호화 모듈 적용(고정)

        //###############################################################################
        //# 3. 지불 요청 #
        //################
        data = inipay.payRequest(data);

        //###############################################################################
        // # 4. ACK 요청 및 DB처리 #
        //################

        //###############################################################################
        //# 5. 요청 결과 #
        //################
        String resultCode = data.getData("ResultCode");      // "00"이면 신용카드 빌링요청 성공
        String resultMsg  = data.getData("ResultMsg");       // 결과에 대한 설명
        String authCode   = data.getData("CardAuthCode");  // 승인번호
        String pgAuthDate = data.getData("PGauthdate");      // 이니시스 승인날짜(YYYYMMDD)
        String pgAuthTime = data.getData("PGauthtime");      // 이니시스 승인시간(HHMMSS)
        String tid        = data.getData("tid");             // 거래번호

        if (!"00".equals(resultCode)) {
            log.error("빌링 지불요청 실패!!!!");
            return new ResponseEntity<>("빌링 지불요청 실패!!!!", HttpStatus.BAD_REQUEST);
        }

        // TODO : DB 처리(주문여부 Y및 결제유형 지정, 승인내역 저장)
//            주문번호(MOID), 회원번호, 금액(PRICE)

        billResult = new BillResult();

        billResult.setMoid(moid);
        billResult.setMbCd(tbMember.getMbCd().toString());
        billResult.setPrice(price);
        billResult.setResultmsg(resultMsg);
        billResult.setCardauthcode(authCode);
        billResult.setPgauthdate(pgAuthDate);
        billResult.setPgauthtime(pgAuthTime);
        billResult.setTid(tid);
        billResult.setRegDt(new Date());

        billResultRepository.save(billResult);

        tbOrder.setOrChargeType("Y");
        tbOrder.setOrChargeGubun("2");

        try {
            tbOrderService.saveTbOrder(tbOrder);
        } catch (Exception e) {
            log.error("정기결제 주문정보 변경 에러 {}", e);
            e.printStackTrace();
        }

        return new ResponseEntity<>(billResult, HttpStatus.OK);

        /*log.debug("빌링 지불요청 resultCode = {}", resultCode);
        log.debug("빌링 지불요청 resultMsg = {}", resultMsg);
        log.debug("빌링 지불요청 authCode = {}", authCode);
        log.debug("빌링 지불요청 pgAuthDate = {}", pgAuthDate);
        log.debug("빌링 지불요청 pgAuthTime = {}", pgAuthTime);
        log.debug("빌링 지불요청 tid = {}", tid);*/

        /*strResult = "\n빌링 지불요청 resultCode = " + resultCode;
        strResult += "\n빌링 지불요청 resultMsg = " + resultMsg;
        strResult += "\n빌링 지불요청 authCode = " + authCode;
        strResult += "\n빌링 지불요청 pgAuthDate = " + pgAuthDate;
        strResult += "\n빌링 지불요청 pgAuthTime = " + pgAuthTime;
        strResult += "\n빌링 지불요청 tid = " + tid;

        return new ResponseEntity<>(strResult, HttpStatus.OK);*/
    }

    /**
     * 이니시스 승인결과 수신 테스트(빌링)
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/client/inicis-result", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_result(/*@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
                                     @RequestParam(value="P_RMESG1", required=false) final String P_RMESG1,
                                     @RequestParam(value="P_TID", required=false) final String P_TID,
                                     @RequestParam(value="P_TYPE", required=false) final String P_TYPE,
                                     @RequestParam(value="P_AUTH_DT", required=false) final String P_AUTH_DT,
                                     @RequestParam(value="P_MID", required=false) final String P_MID,
                                     @RequestParam(value="P_OID", required=false) final String P_OID,
                                     @RequestParam(value="P_AMT", required=false) final String P_AMT,
                                     @RequestParam(value="P_UNAME", required=false) final String P_UNAME,
                                     @RequestParam(value="P_MNAME", required=false) final String P_MNAME,
                                     @RequestParam(value="P_FN_CD1", required=false) final String P_FN_CD1,
                                     @RequestParam(value="P_FN_NM", required=false) final String P_FN_NM,
                                     @RequestParam(value="P_RMESG2", required=false) final String P_RMESG2,
                                     @RequestParam(value="P_AUTH_NO", required=false) final String P_AUTH_NO,*/
                                HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, UnknownHostException {


        String strResult = "inicis-result!!!!";

//        이니시스 결제결과 파라메터 확인
        StringBuilder strLog = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            strLog.append(paramName);
            strLog.append("\n");

            String[] paramValues = request.getParameterValues(paramName);

            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                strLog.append("\t" + paramValue);
                strLog.append("\n");
            }
        }

        log.debug("이니시스 nicis-result \n {}", strLog.toString());

        String resultcode = request.getParameter("resultcode");

        if("00".equals(resultcode)) {
            String orderid = request.getParameter("orderid");
            String cardno = request.getParameter("cardno");
            String billkey = request.getParameter("billkey");

            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();
            log.debug("Current IP address : {}", ip.getHostAddress());
            log.debug("Current IP address : {}", ipAddress);

//            테스트
            try {
                String tid = com.inicis.inipay4.util.Tool.makeTid("card", P_MID);
                log.error("makeTid 테스트 결과 {}", tid);
            } catch (Exception e) {
                log.error("makeTid 테스트 에러 {}", e);
            }

//        ii.  INIpay41 클래스의 인스턴스 생성
            INIpay inipay = new INIpay();
            INIdata data = new INIdata();

/*//        iii.  정보 설정
            data.setData("type", "authbill");   // 결제 type, 고정
            data.setData("inipayHome", inipayHome);  // 이니페이가 설치된 절대경로
            data.setData("logMode", logMode);  // logMode
            data.setData("keyPW", keyPW);  // 키패스워드
            data.setData("subPgip", "203.238.37.3");  // Sub PG IP (고정)
            data.setData("mid", request.getParameter("mid")); // 상점아이디
            data.setData("paymethod", request.getParameter("paymethod"));
// 지불방법, 빌링등록 고정
            data.setData("billtype", "Card");  // 빌링유형 고정
            data.setData("price", request.getParameter("price"));  // 상품금액
            data.setData("currency", "WON");  // 화폐단위
            data.setData("goodname", request.getParameter("goodname"));
// 상품명 (최대 40자)
            data.setData("buyername", request.getParameter("buyername"));

//        주문번호가 파라메터로 넘어오기 때문에 회원정보로 검색
// 구매자 (최대 15자)
            data.setData("buyertel", request.getParameter("buyertel"));
// 구매자이동전화
            data.setData("buyeremail", request.getParameter("buyeremail"));
// 구매자이메일
            data.setData("url", "http://www.your_domain.co.kr");
// 홈페이지 주소(URL)
            data.setData("uip", request.getRemoteAddr()); // IP Addr
            data.setData("encrypted", request.getParameter("encrypted"));
            data.setData("sessionkey", request.getParameter("sessionkey"));

//        iv.  본인인증 절차를 통한 실시간 빌링 등록 요청
        data = inipay.payRequest(data);*/

            // 이하 jsp 소스 참조
            data.setData("type", "reqrealbill");                             // 결제 type, 고정
            data.setData("inipayHome", inipayHome);                           // 이니페이가 설치된 절대경로
            data.setData("logMode", logMode);                                 // logMode
            data.setData("keyPW",keyPW);                                      // 키패스워드
            data.setData("subPgip","203.238.37.3");                           // Sub PG IP (고정)
            data.setData("mid", request.getParameter("mid"));                 // 상점아이디
            data.setData("uid", request.getParameter("mid") );                // INIpay User ID
            data.setData("goodname", request.getParameter("goodname"));       // 상품명 (최대 40자)
            data.setData("currency", request.getParameter("currency"));       // 화폐단위
            data.setData("price", request.getParameter("price"));             // 가격
            data.setData("buyername", request.getParameter("buyername"));     // 구매자 (최대 15자)
            data.setData("buyertel", request.getParameter("buyertel"));       // 구매자이동전화
            data.setData("buyeremail", request.getParameter("buyeremail"));   // 구매자이메일
            data.setData("paymethod", "Card");                                // 지불방법, 카드빌링
            data.setData("billkey", request.getParameter("billkey"));         // 빌링등록 키(빌키)
            data.setData("moid", request.getParameter("moid"));               // 빌링등록 키(빌키)
            data.setData("cardpass", request.getParameter("cardpass"));       // 카드비번 앞자리 2자리
            data.setData("regnumber", request.getParameter("regnumber"));     // 주민번호 및 사업자번호
            data.setData("url", "http://www.your_domain.co.kr");              // 홈페이지 주소(URL)
            data.setData("uip", request.getRemoteAddr());                     // IP Addr
            data.setData("cardquota", request.getParameter("cardquota"));
            data.setData("authentification", request.getParameter("authentification"));
            data.setData("authField1", request.getParameter("authField1"));
            data.setData("authField2", request.getParameter("authField2"));
            data.setData("authField3", request.getParameter("authField3"));
            data.setData("crypto", "execure");                                // Extrus 암호화 모듈 적용(고정)

//            iv.  빌링 승인 요청
            data = inipay.payRequest(data);

            log.debug("빌링요청 결과 {}", data);
        } else {
            log.error("빌링 본인확인 실패!!!!");
        }

//        strResult = "OK";
//        strResult = strLog.toString();

        strResult = "<script>window.location = 'cleanbank://billing';</script>";

        return strResult;
    }

    /**
     * 38.약관동의 : 최초 가입시 DelYn값을 X로 하고, 약관 동의시 정상 사용하도록 N으로 변경
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/setTermAgree", method = RequestMethod.PATCH)
    public ResponseEntity<?> setTermAgree(@RequestParam(value = "mbCd", required = false) final Integer mbCd) {

        if (mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember member = tbMemberRepository.findOne(mbCd);
        if (member == null) return new ResponseEntity<>("해당 회원 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        member.setDelYn("N");
        member = tbMemberRepository.save(member);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * 37.결제정보 전송
     * @param moOrder3
     * @return
     * @throws ParseException
     */

    @RequestMapping(value = "/client/setChargeInfo", method = RequestMethod.POST)
    public ResponseEntity<?> setChargeInfo(@RequestBody MoOrder3 moOrder3) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(moOrder3.getOrCd())) return new ResponseEntity<>("orCd(주문번호)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moOrder3.getOrCharge())) return new ResponseEntity<>("orCharge(결제금액)을 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        주문정보 확인
        Long orCd = moOrder3.getOrCd();
        TbOrder tbOrder = tbOrderService.getTbOrderById(orCd);

        if (tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//        쿠폰정보 확인
        if(moOrder3.getPoCds().size() > 0) {
            for(Integer poCd : moOrder3.getPoCds()) {
                TbPromotion promotion = tbPromotionService.getTbPromotionById(poCd.longValue());
                if (promotion == null) return new ResponseEntity<>(poCd +"는 존재하지 않는 쿠폰번호 입니다!", HttpStatus.NOT_FOUND);

//                해당 사용자가 소유한 코드인지와 사용여부 확인
                TbPromotionUse tbPromotionUse = tbPromotionUseRepository.findByMbCdAndPoCd(tbOrder.getMbCd(), poCd);
                if(tbPromotionUse == null) return new ResponseEntity<>(poCd + " : poCd(쿠폰코드)는 해당회원이 소유한 쿠폰이 아닙니다!", HttpStatus.NOT_FOUND);
                if("Y".equals(tbPromotionUse.getUseYn())) return new ResponseEntity<>(poCd + " : poCd(쿠폰코드)는 이미 사용 하였습니다!", HttpStatus.CONFLICT);
                tbPromotionUse.setUseYn("Y");
                tbPromotionUse.setOrCd(orCd);
                tbPromotionUse.setPuUse("쿠폰 결제");
                tbPromotionUseRepository.save(tbPromotionUse);
            }
        }

//        결재정보 저장
        tbOrder.setOrCharge(moOrder3.getOrCharge());
        if(!StringUtils.isEmpty(moOrder3.getOrPoint())) tbOrder.setOrPoint(moOrder3.getOrPoint());
        tbOrder.setOrChargeType("Y");

//        기타금액
        if(!StringUtils.isEmpty(moOrder3.getOrExtMoney())) tbOrder.setOrExtMoney(moOrder3.getOrExtMoney());

        tbOrderRepository.save(tbOrder);

//        회원 포인트 사용내역 등록
        if(!StringUtils.isEmpty(moOrder3.getOrPoint()) && moOrder3.getOrPoint() > 0) {
            MoPoint moPoint = new MoPoint();
            moPoint.setMbCd(tbOrder.getMbCd());
            moPoint.setPoint(moOrder3.getOrPoint());
            moPoint.setOrCd(moOrder3.getOrCd());
            ResponseEntity<?> result = tbMemberService.setMbrPoint(moPoint);
            log.debug(result.toString());
        }
        return new ResponseEntity<>(moOrder3, HttpStatus.OK);
    }

    /*
    2016-02-17T17:14:53,558 DEBUG [controllers.mobile.ClientController] 554 [http-nio-8084-exec-7] ?대땲?쒖뒪 card-next
     P_STATUS
        00
    P_RMESG1

    P_TID
        INIMX_AISP100min000020160217171355761282
    P_REQ_URL
        https://drmobile.inicis.com/smart/pay_req_url.php
    P_NOTI
     */

    /**
     * get과 post를 동시에 http://okky.kr/article/228474
     *
     * 1) 이니시스 카드결제 : P_NEXT_URL(/client/inicis/card-next) 만 사용 나머지는 테스트 코드
     *  - INIpayMobile_WEB_manual_v.4.06.pdf 17페이지 P_NEXT_URL에서 처리후 안드로이드(cleanbank://result) 호출하면 해당주문건의 결재 여부 api를 확인하여 결재 성공/실패 화면 표시
     */
    /**
     *
     * @param P_TID : 인증거래번호
     * @param P_REQ_URL : 승인요청 Url
     * @param req
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/client/inicis/card-next", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> inicis_card_next(@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
                                    @RequestParam(value="P_TID", required=false) final String P_TID,
                                   @RequestParam(value="P_REQ_URL", required=false) final String P_REQ_URL,
                                   @RequestParam(value="P_RMESG1", required=false) final String P_RMESG1,
                                   @RequestParam(value="P_NOTI", required=false) final String P_NOTI,
                                   HttpServletRequest req) throws MessagingException, IOException {

        // 이니시스 결제 완료 정보를 리턴
        /*public function inicis_result() {
            $PGIP = $_SERVER['HTTP_X_FORWARDED_FOR'];
            if ($PGIP != "211.219.96.165" && $PGIP != "118.129.210.25" && $PGIP != "121.131.26.231") {
                echo "FAIL";
                return;
            }*/

//        String strResult = "card-next!!!!";
//        String strResult = "<script>window.location = 'baekmin_fail://result_fail';</script>";
        String strResult = "<script>window.location = 'resultfail://baekminfail';</script>";

        StringBuilder strLog = new StringBuilder();

//        이니시스 결제결과 파라메터 확인
        Enumeration<String> parameterNames = req.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            strLog.append(paramName);
            strLog.append("\n");

            String[] paramValues = req.getParameterValues(paramName);

            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                strLog.append("\t" + paramValue);
                strLog.append("\n");
            }
        }

        log.debug("이니시스 card-next \n {}", strLog.toString());

//        인증결과가 실패일 경우
        if("01".equals(P_STATUS)) {
//            strResult = "Auth Fail" + "<br>" + P_RMESG1;
            log.debug("Auth Fail" + "<br>" + P_RMESG1);
            strResult = ""; // 결제화면에서 뒤로가기 클릭시 에러 메세지가 보여진다
            return new ResponseEntity<>(strResult, HttpStatus.OK);
        }

        // 승인요청 하기
        String strReqUrl = P_REQ_URL + "?P_TID=" + P_TID + "&P_MID=" + P_MID;

        // 2016-12-19 추가
        strReqUrl += "&P_CHARSET=utf8&accept-charset=euc-kr";

//        승인결과
        HashMap<String, String> map = new HashMap<String, String>();

        /** HttpComponent 4.x **/
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(strReqUrl);

            log.debug("이니시스 P_REQ_URL 호출 {}", httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };

            String responseBody = httpclient.execute(httpget, responseHandler);
            log.debug("처리 결과 시작 ----------------------------------------");
            log.debug(responseBody);

            String decoded_result1 = new String(responseBody.getBytes("euc-kr"), "euc-kr");
            log.debug("디코딩1 = " + decoded_result1);

            String decoded_result2 = new String(responseBody.getBytes("euc-kr"), "UTF-8");
            log.debug("디코딩2 = " + decoded_result2);

            String decoded_result3 = new String(responseBody.getBytes("UTF-8"), "UTF-8");
            log.debug("디코딩3 = " + decoded_result3);

            //        승인결과 파싱
            String[] values = new String(responseBody).split("&");

            for (int x = 0; x < values.length; x++) {
                log.debug(values[x]);  // 승인결과를 출력
//                out.print("<br>");

                // 승인결과를 파싱값 잘라 hashmap에 저장
                int i = values[x].indexOf("=");
                String key1 = values[x].substring(0, i);
                String value1 = values[x].substring(i + 1);
                /*String key1 = arr1[0].trim();
                String value1 = arr1[1].trim();*/
                map.put(key1, value1);
            }

            log.debug("처리 결과 끝 ----------------------------------------");

        } catch (Exception ex) {
            log.debug("이니시스 결제처리 에러!!!! ----------------------------------------\n{}", ex);
        } finally {
            httpclient.close();
        }

        log.debug("<br>승인결과 저장 및 출력---------------------------------------------------------------------------<br>");

        log.debug("<br>공통변수---------------------------------------------------------------------------<br>");
        log.debug("P_TID : "+map.get("P_TID")+"<br>");  // 거래번호
        log.debug("P_STATUS : "+map.get("P_STATUS")+"<br>");                           // 거래상태 - 지불결과 성공:00, 실패:00 이외 실패
        log.debug("P_RMESG1 : "+map.get("P_RMESG1")+"<br>");                              // 지불 결과 메시지
        log.debug("P_TYPE : "+map.get("P_TYPE")+"<br>");                                 // 지불수단
        log.debug("P_MID : "+map.get("P_MID")+"<br>");                                       // 상점아이디
        log.debug("P_OID : "+map.get("P_OID")+"<br>");                             // 상점주문번호
        log.debug("P_AMT : "+map.get("P_AMT")+"<br>");                          // 거래금액
        log.debug("P_UNAME : "+map.get("P_UNAME")+"<br>");                 // 구매자명
        log.debug("P_NEXT_URL : "+map.get("P_NEXT_URL")+"<br>");  	// 가맹점 전달 P_NEXT_URL
        log.debug("P_NOTEURL : "+map.get("P_NOTEURL")+"<br>");				// 가맹점 전달 NOTE_URL
        log.debug("P_AUTH_DT : "+map.get("P_AUTH_DT")+"<br>");        // 승인일자(YYYYmmddHHmmss)
        log.debug("P_MNAME : "+map.get("P_MNAME")+"<br>");			// 가맹점명 -->> 요청정보 필드내 명시 안됨
        log.debug("P_NOTI : "+map.get("P_NOTI")+"<br>");		 // 기타주문정보

        InicisResult inicisResult = new InicisResult();

        inicisResult.setPStatus(map.get("P_STATUS"));
//        inicisResult.setPRmesg1(map.get("P_RMESG1"));
        inicisResult.setPTid(map.get("P_TID"));
        inicisResult.setPType(map.get("P_TYPE"));
        inicisResult.setPAuthDt(map.get("P_AUTH_DT"));
        inicisResult.setPMid(map.get("P_MID"));
        inicisResult.setPOid(map.get("P_OID"));
        inicisResult.setPAmt(map.get("P_AMT"));
//        inicisResult.setPUname(map.get("P_UNAME"));
        inicisResult.setPMname(map.get("P_MNAME"));
        inicisResult.setPFnCd1(map.get("P_FN_CD1"));
        inicisResult.setPFnNm(map.get("P_FN_NM"));
        inicisResult.setPRmesg2(map.get("P_RMESG2"));
        inicisResult.setPAuthNo(map.get("P_AUTH_NO"));

//        이니시스 결제 파라메터 db에 입력, 해당 주문건을 결제 완료 처리, 100min 담당자에게 알림 메일 발송
        String strResult2 = tbOrderService.inicis_result(inicisResult);

        if(!"00".equals(inicisResult.getPStatus())) { // 실패
            log.debug("결제실패!=====================================================");
        } else { // 성공
            /*$SCRIPT = '<SCRIPT>';
            IF ($IOS) {
                $SCRIPT .= "WINDOW.LOCATION = 'BAEKMIN:OID=".$OID."';";
            } ELSE  {
                $SCRIPT .= "WINDOW.LOCATION = 'BAEKMIN://RESULT';";
            }
            $SCRIPT .= '</SCRIPT>';*/

            log.debug("결제성공!");
        }

        /*결제 성공시
        android:host="result"
        android:scheme="cleanbank"

        결제 실패시
        android:host="result_fail"
        android:scheme="baekmin_fail"*/
//        strResult = "<script>window.location = 'baekmin_fail://result_fail';</script>";
        if("Y".equals(strResult2)) {
            strResult = "<script>window.location = 'cleanbank://result';</script>";
        }

        log.debug("<br>승인결과 저장 및 출력---------------------------------------------------------------------------<br>");

//        http://brad2014.tistory.com/355
        /*URL obj = new URL(P_REQ_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

// 승인 결과 받음 (이니시스에서 euc-kr 로 던저줌, 따라서 euc-kr 로 디코딩해서 String에 저장)
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "euc-kr"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        String result = response.toString();  // 이니시스에서 던진 결과를 문자열로 엮어서 result 변수로
        String decoded_result = new String(result.getBytes("euc-kr"), "UTF-8");  --- (2)
        mav.setViewName("/payment/evms_payment_credit_success.jsp?" + result); ---  (3)*/

//        strResult = "OK";

        log.debug("신용카드 승인결과"+ strResult);
        System.out.println("신용카드 승인결과"+ strResult);
        return new ResponseEntity<>(strResult, HttpStatus.OK);
    }

    /**
     * 34.쿠폰정보 상세정보	: 포인트 및 쿠폰관리 화면에서 보여지는 쿠폰상세정보를 가져온다.
     * @param mbCd
     * @param poCd
     * @return
     */
    @RequestMapping(value = "/client/getPoCoupInf", method = RequestMethod.GET)
    public ResponseEntity<?> getPoCoupInf(@RequestParam(value="mbCd", required=false) final Integer mbCd, @RequestParam(value="poCd", required=false) final long poCd) throws ParseException {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(poCd <= 0) return new ResponseEntity<>("poCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbPromotionUse tbPromotionUse = tbPromotionUseRepository.getPoCoupInf(mbCd, poCd);
        if(tbPromotionUse == null) return new ResponseEntity<>("해당 쿠폰 정보가 존재하지 않습니다!", HttpStatus.BAD_REQUEST);

        TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();
        MoPromotion moPromotion = new MoPromotion();

        moPromotion.setId(tbPromotionUse.getId());
        moPromotion.setPoNm(tbPromotion.getPoNm());
        moPromotion.setPuUse(tbPromotionUse.getPuUse());
        moPromotion.setPoStartDt(tbPromotion.getPoStartDt());
//        moPromotion.setPoFinishDt(tbPromotion.getPoFinishDt());
        moPromotion.setPoFinishDt(tbPromotion.getPoFinishDtTm());
        moPromotion.setPoDubYn(tbPromotion.getPoDubYn());
        moPromotion.setPoImg(tbPromotion.getPoImg());

        moPromotion.setMbCd(mbCd);
        moPromotion.setPoCd(tbPromotion.getPoCd());
        moPromotion.setUseYn(tbPromotionUse.getUseYn());

        moPromotion.setPoGoldPrice(tbPromotion.getPoGoldPrice());
        moPromotion.setPoSilverPrice(tbPromotion.getPoSilverPrice());
        moPromotion.setPoGreenPrice(tbPromotion.getPoGreenPrice());

        moPromotion.setPoGoldPer(tbPromotion.getPoGoldPer());
        moPromotion.setPoSilverPer(tbPromotion.getPoSilverPer());
        moPromotion.setPoGreenPer(tbPromotion.getPoGreenPer());

        moPromotion.setPoLimitAmount(tbPromotion.getPoLimitAmount());

        if(!StringUtils.isEmpty(tbPromotionUse.getOrCd())) moPromotion.setOrCd(tbPromotionUse.getOrCd());
        if(!StringUtils.isEmpty(tbPromotionUse.getPuUse())) moPromotion.setPuUse(tbPromotionUse.getPuUse());
        if(!StringUtils.isEmpty(tbPromotionUse.getPuUseDt())) moPromotion.setPuUseDt(tbPromotionUse.getPuUseDt());

        if(!StringUtils.isEmpty(tbPromotion.getPoCoup())) {
            moPromotion.setPoCoup(tbPromotion.getPoCoup());
            moPromotion.setPoDiscountAmt(tbPromotion.getPoDiscountAmt());
        }

        return new ResponseEntity<>(moPromotion, HttpStatus.OK);
    }

    /**
     * 33.사용 및 만료된 쿠폰
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/getUsePoCoup", method = RequestMethod.GET)
    public ResponseEntity<?> getUsePoCoup(@RequestParam(value = "mbCd", required = false) final Integer mbCd) {

        if (mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<?> list = tbMemberService.getUsePoCoup(mbCd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 32.포인트 정보 전송
     * @param moPoint
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/setMbrPointInf", method = RequestMethod.POST)
    public ResponseEntity<?> setMbrPointInf(@RequestBody MoPoint moPoint) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(moPoint.getMbCd())) return new ResponseEntity<>("mbCd(고객코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPoint.getPlUseMemo())) return new ResponseEntity<>("plUseMemo(사용내역)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPoint.getPoint())) return new ResponseEntity<>("poCd(포인트)를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        회원 포인트 적립내역 등록
        return tbMemberService.setMbrPointInf(moPoint);
//        return new ResponseEntity<>(moPoint, HttpStatus.OK);
    }

    /**
     * 31.적립 및 사용된 포인트 : 적립 및 사용된 포인트 내역을 가져온다.
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/getUsePoint", method = RequestMethod.GET)
    public ResponseEntity<?> getUsePoint(@RequestParam(value = "mbCd", required = false) final Integer mbCd) {

        if (mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<?> list = tbMemberService.getPointList(mbCd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 30.포인트사용	: 포인트내역에서 포인트를 사용한다.
     * @param moPoint
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/setMbrPoint", method = RequestMethod.POST)
    public ResponseEntity<?> setMbrPoint(@RequestBody MoPoint moPoint) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(moPoint.getMbCd())) return new ResponseEntity<>("mbCd(고객코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPoint.getOrCd())) return new ResponseEntity<>("orCd(주문코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPoint.getPoint())) return new ResponseEntity<>("Point(포인트)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPoint.getPlUseMemo())) return new ResponseEntity<>("plUseMemo(사용내역)를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        회원 포인트 차감및 사용내역 등록
        return tbMemberService.setMbrPoint(moPoint);
//        return new ResponseEntity<>(moPoint, HttpStatus.OK);
    }

    /**
     * 26.포인트정보	: 포인트 내역을 가져온다.
     * @param spec1
     * @param spec2
     * @return
     */
    @RequestMapping(value = "/client/getMbrPoint", method = RequestMethod.GET)
    public ResponseEntity<?> getMbrPoint(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2) {

        if (spec1 == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
        TbMember member = tbMemberRepository.findOne(spec);

        if (member == null) return new ResponseEntity<>("해당 회원 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//        return new ResponseEntity<>(member.getMbPoint(), HttpStatus.OK);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * 28.주문쿠폰정보
     * @param mbCd
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/client/getOrdCoupInf", method = RequestMethod.GET)
    public ResponseEntity<?> getOrdCoupInf(@RequestParam(value = "mbCd", required = false) final Integer mbCd, @RequestParam(value = "orCd", required = false) final Long orCd) {

        if (mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbPromotionUse tbPromotionUse = tbPromotionUseRepository.getOrdCoupInf(mbCd, orCd);
        if (tbPromotionUse == null) return new ResponseEntity<>("해당 주문쿠폰정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//            log.debug("id = {}", tbPromotionUse.getId());
        TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();
        MoPromotion moPromotion = new MoPromotion();

        moPromotion.setId(tbPromotionUse.getId());
        moPromotion.setPoNm(tbPromotion.getPoNm());
        moPromotion.setPuUse(tbPromotionUse.getPuUse());
        moPromotion.setPoStartDt(tbPromotion.getPoStartDt());
        moPromotion.setPoFinishDt(tbPromotion.getPoFinishDt());
        moPromotion.setPoDubYn(tbPromotion.getPoDubYn());
        moPromotion.setPoImg(tbPromotion.getPoImg());

        moPromotion.setMbCd(mbCd);
        moPromotion.setPoCoup(tbPromotion.getPoCoup());
        moPromotion.setPoCd(tbPromotion.getPoCd());
        moPromotion.setUseYn(tbPromotionUse.getUseYn());

        if (!StringUtils.isEmpty(tbPromotionUse.getOrCd())) moPromotion.setOrCd(tbPromotionUse.getOrCd());
        if (!StringUtils.isEmpty(tbPromotionUse.getPuUse())) moPromotion.setPuUse(tbPromotionUse.getPuUse());

        return new ResponseEntity<>(moPromotion, HttpStatus.OK);
    }

    /**
     * 27.쿠폰사용 : 쿠폰내역에서 쿠폰을 사용한다.
     * @param moPromotion
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/setPoCoupUse", method = RequestMethod.POST)
    public ResponseEntity<?> setPoCoupUse(@RequestBody MoPromotion moPromotion) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(moPromotion.getMbCd())) return new ResponseEntity<>("mbCd(고객코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPromotion.getOrCd())) return new ResponseEntity<>("orCd(주문코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPromotion.getPoCd())) return new ResponseEntity<>("poCd(쿠폰코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        해당 사용자가 소유한 코드인지와 사용여부 확인
        TbPromotionUse tbPromotionUse = tbPromotionUseRepository.findByMbCdAndPoCd(moPromotion.getMbCd(), moPromotion.getPoCd());
        if(tbPromotionUse == null) return new ResponseEntity<>(moPromotion.getPoCd() + " : poCd(쿠폰코드)는 해당회원이 소유한 쿠폰이 아닙니다!", HttpStatus.NOT_FOUND);

        if("Y".equals(tbPromotionUse.getUseYn())) return new ResponseEntity<>(moPromotion.getPoCd() + " : poCd(쿠폰코드)는 이미 사용 하였습니다!", HttpStatus.CONFLICT);

//        쿠폰 사용내역 저장
        tbPromotionUse.setOrCd(moPromotion.getOrCd());
        tbPromotionUse.setUseYn("Y");
        tbPromotionUse.setPuUseDt(new Date());
        tbPromotionUse.setEvtNm("수정");
        tbPromotionUse.setPuUse(moPromotion.getPuUse());

        tbPromotionUse = tbPromotionUseRepository.save(tbPromotionUse);

        return new ResponseEntity<>(tbPromotionUse, HttpStatus.OK);
    }

    /**
     * 26.디바이스 토큰 저장
     * @param gcmregids
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/saveToken", method = RequestMethod.POST)
    public ResponseEntity<?> setPoCoupUse(@RequestBody Gcmregids gcmregids) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(gcmregids.getRegId())) return new ResponseEntity<>("regId(디바이스 토큰)을 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        이미 존재하는지 확인
        Gcmregids token = gcmregidsRepository.findByRegId(gcmregids.getRegId());
        if(token != null) return new ResponseEntity<>("이미 등록됨", HttpStatus.OK);

//        저장
        gcmregids = gcmregidsRepository.save(gcmregids);

        return new ResponseEntity<>(gcmregids, HttpStatus.OK);
    }


    /**
     * 25.쿠폰정보 : 쿠폰정보를 가져온다.
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/getPoCoup", method = RequestMethod.GET)
    public ResponseEntity<?> getPoCoup(@RequestParam(value="mbCd", required=false) final Integer mbCd) throws ParseException {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        List<?> poCoups = tbPromotionService.getPoCoup(mbCd);
        List<TbPromotionUse> poCoups = tbPromotionUseRepository.getPoCoup(mbCd);

        List<MoPromotion> list = new ArrayList<MoPromotion>();

        for (TbPromotionUse tbPromotionUse : poCoups) {
//            log.debug("id = {}", tbPromotionUse.getId());

            TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();

//            사용하지 않은 쿠폰만
            if(!"Y".equals(tbPromotionUse.getUseYn())) {
                MoPromotion moPromotion = new MoPromotion();

                moPromotion.setId(tbPromotionUse.getId());
                moPromotion.setPoNm(tbPromotion.getPoNm());
                moPromotion.setPuUse(tbPromotionUse.getPuUse());
                moPromotion.setPoStartDt(tbPromotion.getPoStartDt());
//                moPromotion.setPoFinishDt(tbPromotion.getPoFinishDt());
                moPromotion.setPoFinishDt(tbPromotion.getPoFinishDtTm());
                moPromotion.setPoDubYn(tbPromotion.getPoDubYn());
                moPromotion.setPoImg(tbPromotion.getPoImg());

                moPromotion.setMbCd(mbCd);
                moPromotion.setPoCd(tbPromotion.getPoCd());
                moPromotion.setUseYn(tbPromotionUse.getUseYn());

                moPromotion.setPoGoldPrice(tbPromotion.getPoGoldPrice());
                moPromotion.setPoSilverPrice(tbPromotion.getPoSilverPrice());
                moPromotion.setPoGreenPrice(tbPromotion.getPoGreenPrice());

                moPromotion.setPoGoldPer(tbPromotion.getPoGoldPer());
                moPromotion.setPoSilverPer(tbPromotion.getPoSilverPer());
                moPromotion.setPoGreenPer(tbPromotion.getPoGreenPer());

                moPromotion.setPoLimitAmount(tbPromotion.getPoLimitAmount());

                if (!StringUtils.isEmpty(tbPromotionUse.getOrCd())) moPromotion.setOrCd(tbPromotionUse.getOrCd());
                if (!StringUtils.isEmpty(tbPromotionUse.getPuUse())) moPromotion.setPuUse(tbPromotionUse.getPuUse());
                if (!StringUtils.isEmpty(tbPromotionUse.getPuUseDt()))
                    moPromotion.setPuUseDt(tbPromotionUse.getPuUseDt());

                if (!StringUtils.isEmpty(tbPromotion.getPoCoup())) {
                    moPromotion.setPoCoup(tbPromotion.getPoCoup());
                    moPromotion.setPoDiscountAmt(tbPromotion.getPoDiscountAmt());
                }

                list.add(moPromotion);
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 24.쿠폰등록 : 쿠폰번호를 등록한다.
     * @param moPromotion
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/setPoCoup", method = RequestMethod.POST)
    public ResponseEntity<?> setPoCoup(@RequestBody MoPromotion moPromotion) throws ParseException {
//        필수 컬럼 체크
        if(StringUtils.isEmpty(moPromotion.getMbCd())) return new ResponseEntity<>("mbCd(고객코드)를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(moPromotion.getPoCoup())) return new ResponseEntity<>("poCoup(쿠폰번호)를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        존재하는 쿠폰번호인지 확인
        TbPromotion tbPromotion = tbPromotionRepository.findByPoCoup(moPromotion.getPoCoup());
        if(tbPromotion == null) return new ResponseEntity<>(moPromotion.getPoCoup() + " : poCoup(쿠폰번호)는 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//        해당 사용자가 이미 해당 코드를 등록했는지 확인
        TbPromotionUse tbPromotionUse = tbPromotionUseRepository.findByMbCdAndPoCd(moPromotion.getMbCd(), tbPromotion.getPoCd());
        if(tbPromotionUse != null) return new ResponseEntity<>(moPromotion.getPoCoup() + " : poCoup(쿠폰번호)는 이미 등록 하였습니다!", HttpStatus.CONFLICT);

//        쿠폰 저장
        tbPromotionUse = new TbPromotionUse();
        tbPromotionUse.setMbCd(moPromotion.getMbCd());
        tbPromotionUse.setDelYn("N");
        tbPromotionUse.setEvtNm("신규");
        tbPromotionUse.setRegiDt(new Date());
        tbPromotionUse.setUser("모바일");

//        tbPromotionUse.setPoCd(tbPromotion.getPoCd());
        tbPromotionUse.setTbPromotion(tbPromotion);

        tbPromotionUse = tbPromotionUseRepository.save(tbPromotionUse);

        moPromotion.setId(tbPromotionUse.getId());
        return new ResponseEntity<>(moPromotion, HttpStatus.OK);
    }

    @RequestMapping(value = "/client/getTimeTable", method = RequestMethod.GET)
    public ResponseEntity<?> getTimeTable(@RequestParam(value="mbAddr1", required=false) final String mbAddr1) throws ParseException {
        Integer bnCd = 0;
        TbBranch tbBranch = null;

//        주소동으로 지점번호 확인
        if(!StringUtils.isEmpty(mbAddr1)) {
            TbBranchLocs tbBranchLocs = tbBranchLocsService.getTbBranchLocsByAddr1(mbAddr1);
            if (tbBranchLocs != null) {
                bnCd = tbBranchLocs.getBnCd();
                tbBranch = tbBranchRepository.findOne(bnCd);
            }
        }

//        지점이 없으면 첫번째 지점으로
        /*if(tbBranch == null) {
            List<TbBranch> tbBranchs = tbBranchRepository.getBranchList();
            tbBranch = tbBranchs.get(0);
        }*/

//        if(bnCd != null && bnCd > 0) tbBranch = tbBranchService.getTbBranchById(bnCd);
        if(tbBranch == null) return new ResponseEntity<>("서비스 지역이 아닙니다!", HttpStatus.NOT_FOUND);

//        시작시간
        String StartTime = "10:00";
        if(!StringUtils.isEmpty(tbBranch.getbnOpenTm0())) {
            StartTime = tbBranch.getbnOpenTm0();
        }

        //        종료시간
        String EndTime = "23:00";
        if(!StringUtils.isEmpty(tbBranch.getbnCloseTm0())) {
            EndTime = tbBranch.getbnCloseTm0();
        }

//        수거시간간격
        Integer iTimeSpan = 30;
        if(!StringUtils.isEmpty(tbBranch.getBnTransTm())) {
            iTimeSpan = tbBranch.getBnTransTm();
        }

        Calendar startCal = Calendar.getInstance();
        Date dtNow = new Date();
        startCal.setTime(dtNow);

/*

//        토요일 : 추후 해당 지점의 영업 종료일로 처리해야 함
        startCal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // 7
//        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 1
//        Date end = c.getTime();

        int iDays = startCal.get(Calendar.DAY_OF_WEEK) - startCal.getFirstDayOfWeek();
*/

        int iDays = 5; // 안드로이드 앱에서 5일치만 보여준다

        //        날짜 목록
        DateFormat df1 = new SimpleDateFormat("MM/dd");
        DateFormat df7 = new SimpleDateFormat("E", Locale.KOREA);

        MoTimeTable table = new MoTimeTable();

        List<MoTimeTable.tt_header> headers = new ArrayList<MoTimeTable.tt_header>();
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTime(dtNow);

        for(int i=0; i<iDays; i++) {
            MoTimeTable.tt_header h = new MoTimeTable.tt_header();
            h.setDate(df1.format(tmpCal.getTime()));
            h.setDay_of_week(df7.format(tmpCal.getTime()));
            headers.add(h);

            tmpCal.add(Calendar.DATE, 1);
        }

        table.setTime_table_header(headers);

//        시간목록
        List<MoTimeTable.time_table> tables = new ArrayList<MoTimeTable.time_table>();

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd HH:mm");
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df4 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df5 = new SimpleDateFormat("HHmm");
        SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmm");

        Date startDt = df2.parse(df4.format(dtNow) +" "+ StartTime);
        startCal.setTime(startDt);

//        종료시간
        Date endDt = df2.parse(df4.format(dtNow) +" "+ EndTime);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDt);

        Calendar startTmCal = Calendar.getInstance();
        Calendar endTmCal = Calendar.getInstance();

        startTmCal.setTime(startCal.getTime());

        Calendar calNextHr = Calendar.getInstance();

//        기존 주문 목록
        List<TbOrder> list = orderListService.getOrderList();
        log.debug("list.size() = "+ list.size());

//        비영업일
        List<TbSalesInfo> listSa = tbSalesInfoService.getSaList(tbBranch.getBnCd());

//        for(int i=0; i< 24; i++) { // 24번만 실행하니 영업종료 시간 전에 Loop를 종료한다.
        for(int i=0; i< 40; i++) {
            endTmCal.setTime(startTmCal.getTime());
            endTmCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?
            String strTitle = df3.format(startTmCal.getTime()) + "~" + df3.format(endTmCal.getTime());

//            log.debug("i = "+ i);
            int compare = endCal.getTime().compareTo(endTmCal.getTime());
            if(compare < 0) {
                break;
            }

            MoTimeTable.time_table t_table = new MoTimeTable.time_table();
            t_table.setTitle(strTitle);

//            시간
            List<MoTimeTable.time_table.col> cols = new ArrayList<MoTimeTable.time_table.col>();

            tmpCal.setTime(startCal.getTime());

            for (int j = 0; j < iDays; j++) {
//                요일별 영업여부
                String workYn = "N";
                Integer iDayTo = 0;

                switch (tmpCal.get(Calendar.DAY_OF_WEEK)) {
                    case 1: // 일
                        workYn = tbBranch.getBnSun();
                        iDayTo = tbBranch.getBnSunTo();
                        break;

                    case 2: // 월
                        workYn = tbBranch.getBnMon();
                        iDayTo = tbBranch.getBnMonTo();
                        break;

                    case 3: // 화
                        workYn = tbBranch.getBnTue();
                        iDayTo = tbBranch.getBnTueTo();
                        break;

                    case 4: // 수
                        workYn = tbBranch.getBnWed();
                        iDayTo = tbBranch.getBnWedTo();
                        break;

                    case 5: // 목
                        workYn = tbBranch.getBnThu();
                        iDayTo = tbBranch.getBnThuTo();
                        break;

                    case 6: // 금
                        workYn = tbBranch.getBnFri();
                        iDayTo = tbBranch.getBnFriTo();
                        break;

                    case 7: // 토
                        workYn = tbBranch.getBnSat();
                        iDayTo = tbBranch.getBnSatTo();
                        break;
                }

//                비영업일
                if(listSa != null && listSa.size() > 0) {
                    String currDay = df4.format(tmpCal.getTime());

                    for(TbSalesInfo tbSalesInfo : listSa) {
                        String saDay = df4.format(tbSalesInfo.getSaDate());

                        if(currDay.equals(saDay)) {
                            workYn = "N";
                        }
                    }
                }

                MoTimeTable.time_table.col col = new MoTimeTable.time_table.col();

                String strStartTime = df4.format(tmpCal.getTime()) + df5.format(startTmCal.getTime());
                col.setStart_time(strStartTime);

                String strEndTime = df4.format(tmpCal.getTime()) + df5.format(endTmCal.getTime());
                col.setEnd_time(strEndTime);

                if("Y".equals(workYn)) {
//                +1 시간 미리 마감 처리
                    calNextHr.setTime(dtNow);
                    calNextHr.add(Calendar.HOUR, 1);

                    int compare2 = calNextHr.getTime().compareTo(df6.parse(strStartTime));

// System.out.println(df2.format(calNextHr.getTime()) +" @ "+ df2.format(df6.parse(strStartTime)) +" @ "+ compare2);

                    if (compare2 > 0) {
                        col.setState("N");
                    } else {
                        col.setState("Y");

                        //        해당 수거시간의 주문건수 확인
                        Date tmpStart = df6.parse(strStartTime);
                        Date tmpEnd = df6.parse(strEndTime);

                        List<TbOrder> filterList = StreamSupport.stream(list.spliterator(), false)
                                .filter(t -> (t.getOrReqDt().getTime() >= tmpStart.getTime() && t.getOrReqDt().before(tmpEnd)))
                                .collect(Collectors.toList());

                        if(filterList.size() > 0 && filterList.size() >= iDayTo) {
                            log.debug("filterList.size() = " + filterList.size());
//                            col.setState("N"+filterList.size());
                            col.setState("N");
                        }
                    }
                } else {
                    col.setState("N");
                }

                cols.add(col);
                tmpCal.add(Calendar.DATE, 1);
            }

            t_table.setCols(cols);
            tables.add(t_table);

            startTmCal.setTime(endTmCal.getTime());
        }

        table.setTime_table_list(tables);

        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    /**
     * 23.수거시간표 : 현재시간 +1 시간 미리 마감 처리
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/client/getTimeTable_OLD", method = RequestMethod.GET)
    public ResponseEntity<?> getTimeTable_OLD() throws ParseException {
//        시작시간
        String StartTime = "10:00";

//        날짜 목록
        DateFormat df1 = new SimpleDateFormat("MM/dd");
        DateFormat df2 = new SimpleDateFormat("E", Locale.KOREA);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

//        오늘
        Date start = c.getTime();

//        토요일 : 추후 해당 지점의 영업 종료일로 처리해야 함
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date end = c.getTime();

        int iDays = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        log.debug("{} - {}", start, end);

        MoTimeTable table = new MoTimeTable();

        List<MoTimeTable.tt_header> headers = new ArrayList<MoTimeTable.tt_header>();
        c.setTime(start);

        for(int i=0; i<iDays; i++) {
            MoTimeTable.tt_header h = new MoTimeTable.tt_header();
//            h.setDate("01/05");
            h.setDate(df1.format(c.getTime()));
//            h.setDay_of_week(String.valueOf(i));
            h.setDay_of_week(df2.format(c.getTime()));
            headers.add(h);

            c.add(Calendar.DATE, 1);
        }

        table.setTime_table_header(headers);

//        시간목록
        Integer iTimeSpan = 30;
        List<MoTimeTable.time_table> tables = new ArrayList<MoTimeTable.time_table>();
        DateFormat df4 = new SimpleDateFormat("yyyyMMdd");
        DateFormat df5 = new SimpleDateFormat("HHmm");
        DateFormat df6 = new SimpleDateFormat("yyyyMMddHHmm");

        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        Date d = df3.parse(StartTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        Calendar tmpCal = Calendar.getInstance();

//        for(int i=0; i< 2; i++) {
        for(int i=0; i< 24; i++) {
            MoTimeTable.time_table t_table = new MoTimeTable.time_table();

            tmpCal.setTime(cal.getTime());
            tmpCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?

            String strTitle = df3.format(cal.getTime()) +"~"+ df3.format(tmpCal.getTime());
            t_table.setTitle(strTitle);

//            시간
            List<MoTimeTable.time_table.col> cols = new ArrayList<MoTimeTable.time_table.col>();
            c.setTime(start);

            for(int j=0; j<iDays; j++) {
                MoTimeTable.time_table.col col = new MoTimeTable.time_table.col();

                String strStartTime = df4.format(c.getTime()) + df5.format(cal.getTime());
                col.setStart_time(strStartTime);

                tmpCal.setTime(cal.getTime());
                tmpCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?

                String strEndTime = df4.format(c.getTime()) + df5.format(tmpCal.getTime());
                col.setEnd_time(strEndTime);

//                마감시간 지났는지 확인
//                int compare = start.compareTo(df6.parse(strEndTime));

//                +1 시간 미리 마감 처리
                Calendar calNextHr = Calendar.getInstance();
                calNextHr.setTime(start);
                calNextHr.add(Calendar.HOUR, 1);

//                int compare = calNextHr.getTime().compareTo(df6.parse(strEndTime));
                int compare = calNextHr.getTime().compareTo(df6.parse(strStartTime));

                if(compare > 0) {
                    col.setState("N");
                } else {
                    col.setState("Y");
                }

                c.add(Calendar.DATE, 1);
                cols.add(col);
            }

            t_table.setCols(cols);
            tables.add(t_table);

            cal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?
        }

        table.setTime_table_list(tables);

        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    /**
     * http://api.100min.co.kr/api/1.0/order/price-table
     * @return
     */
    @RequestMapping(value = "/client/getProducts", method = RequestMethod.GET)
    public ResponseEntity<?> getProducts() {

//        품목 정보 -> 품목명 상위
        List<TbProduct> pdLvl1s = tbProductService.getPdLvl1s();
//        품목 정보 -> 품목명 중위
        List<TbProduct> pdLvl2s = tbProductService.getPdLvl2s();
        //        품목 정보 -> 품목명 하위
        List<TbProduct> pdLvl3s = tbProductService.getPdLvl3s();

        List<MoProduct> moProducts = new ArrayList<MoProduct>();

//        상위
        if(pdLvl1s.size() > 0 ) {
            for(TbProduct tbProduct1 : pdLvl1s) {
                MoProduct moProduct = new MoProduct();
                moProduct.setGroup_no(tbProduct1.getPdLvl1() + tbProduct1.getPdLvl2());
                moProduct.setGroup_name(tbProduct1.getPdNm());

//                중위
                List<TbProduct> pdLvl2s_list = pdLvl2s.stream().filter(p -> p.getPdLvl1().equals(tbProduct1.getPdLvl1())).collect(Collectors.toList());

                if(pdLvl2s_list.size() > 0) {
                    List<MoProduct.laundrie> laundries = new ArrayList<MoProduct.laundrie>();

                    for(TbProduct tbProduct2 : pdLvl2s_list) {
                        MoProduct.laundrie laundrie = new MoProduct.laundrie();

                        laundrie.setLno(tbProduct2.getPdLvl1() + tbProduct2.getPdLvl2());
                        laundrie.setName(tbProduct2.getPdNm());

//                        하위
                        List<TbProduct> pdLvl3s_list = pdLvl3s.stream().filter(p -> p.getPdLvl1().equals(tbProduct1.getPdLvl1()) && p.getPdLvl2().equals(tbProduct2.getPdLvl2()))
                                .collect(Collectors.toList());

                        if(pdLvl3s_list.size() > 0) {
                            List<MoProduct.laundrie.item> items = new ArrayList<MoProduct.laundrie.item>();

                            for (TbProduct tbProduct3 : pdLvl3s_list) {
                                MoProduct.laundrie.item item = new MoProduct.laundrie.item();

                                item.setPdNm(tbProduct3.getPdNm());

                                Integer pdPrice = tbProduct3.getPdPrice();
                                if (pdPrice == null) pdPrice = 0;
                                item.setPdPrice(pdPrice);

                                item.setPdPer(tbProduct3.getPdPer());
                                item.setPdDesc(tbProduct3.getPdDesc());

                                items.add(item);
                            }

                            laundrie.setItems(items);
                        }

                        laundries.add(laundrie);
                    }

                    moProduct.setLaundries(laundries);
                    moProducts.add(moProduct);
                }
            }
        }

        return new ResponseEntity<>(moProducts, HttpStatus.OK);
    }

    @RequestMapping(value = "/client/getItemPictures", method = RequestMethod.GET)
    public ResponseEntity<?> getItemPictures(@And({@Spec(path = "orCd", spec = Equal.class), @Spec(path = "itCd", spec = Equal.class)}) Specification<TbPicture> spec1,
                    @Or({@Spec(path = "delYn", spec = notEqual.class, constVal = "Y"),
                            @Spec(path = "delYn", spec = IsNull.class)}) Specification<TbPicture> spec2,
                    @PageableDefault(sort = {"name"}, size = 1000) Pageable pageable,
                    HttpServletRequest request) {

        if (spec1 == null) return new ResponseEntity<>("orCd, itCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 파라메터 확인
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String strOrCd = request.getParameter("orCd");
        String strItCd = request.getParameter("itCd");
        if(strOrCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(strItCd == null) return new ResponseEntity<>("itCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
//        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbPicture> spec = Specifications.where(spec1).and(spec2);
        List<TbPicture> list = StreamSupport.stream(tbPictureRepository.findAll(spec, pageable).spliterator(), false).collect(Collectors.toList());

//        TODO : TbPicture class에서 처리 해도 될듯
/*        for (TbPicture image : list) {
//            image.setUrl("/file/picture/" + image.getId());
//            image.setThumbnailUrl("/file/thumbnail/" + image.getId());
//            image.setDeleteUrl("/file/delete/" + image.getId());
//            image.setDeleteType("DELETE");
        }*/

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 20.품목내역	: 주문코드에 해당하는 품목내역을 가져온다.
     * @param mbCd
     * @return
     */
/*    @RequestMapping(value = "/client/getOrderItems", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderItems(@RequestParam(value="mbCd", required=false) final Integer mbCd,
                                           @RequestParam(value="orCd", required=false) final Long orCd){
//        필수 컬럼 체크
        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);*/
    @RequestMapping(value = "/client/getOrderItems", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderItems(@RequestParam(value = "mbCd", required = false) final Integer mbCd,
                                           @And({@Spec(path = "orCd", spec = Equal.class)}) Specification<TbOrderItems> spec1,
                                           @Or({@Spec(path = "delYn", spec = notEqual.class, constVal = "Y"),
                                                   @Spec(path = "delYn", spec = IsNull.class)}) Specification<TbOrderItems> spec2,
                                           @PageableDefault(sort = {"id", "pdLvl1", "pdLvl3", "pdLvl3"}, size = 1000) Pageable pageable) {

        //        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String strOrCd = request.getParameter("orCd");
        if(strOrCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        주문정보
        long orCd = Long.parseLong(strOrCd);
        TbOrder tbOrder = tbOrderService.getTbOrderById(orCd);

        if (tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);
        if (!tbOrder.getMbCd().equals(mbCd)) return new ResponseEntity<>("mbCd가 일치하지 않습니다!", HttpStatus.NOT_FOUND);

        MoOrderItems moOrderItems = new MoOrderItems();
        List<MoOrderItems.item> items = new ArrayList<MoOrderItems.item>();

//        moOrderItems.setOrCnt(tbOrder.getOrCnt());
        moOrderItems.setOrPrice(tbOrder.getOrPrice());

        Specification<TbOrderItems> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbOrderItems> list = tbOrderItemsRepository.findAll(spec, pageable);
        List<TbOrderItems> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());

        moOrderItems.setOrCnt(tbOrder.getOrCnt());
        moOrderItems.setOrCnt(itemList.size());

        for(TbOrderItems item : itemList) {
            MoOrderItems.item i = new MoOrderItems.item();

            i.setItCd(item.getId());
            i.setPdLvl1(item.getPdLvl1());
            i.setPdLvl2(item.getPdLvl2());
            i.setPdLvl3(item.getPdLvl3());
            i.setItTac(item.getItTac());
            i.setItPrice(item.getItPrice());

            List<TbProduct> tbProducts = tbProductRepository.checkDups(item.getPdLvl1(), item.getPdLvl2(), item.getPdLvl3());

            if(tbProducts != null && tbProducts.size() > 0) {
                i.setItDiscount(tbProducts.get(0).getPdPer());
            } else {
                i.setItDiscount(0);
            }

            String itStatus = item.getItStatus();

            if(!StringUtils.isEmpty(itStatus)) {
                i.setItStatus(itStatus);
                TbCode tbCode = tbCodeRepository.getCdNm(itStatus.substring(0, 2), itStatus.substring(2, 4));
                if(tbCode != null) i.setItStatusTxt(tbCode.getCdNm());
            }

            List<TbProduct> products = tbProductRepository.checkDups(item.getPdLvl1(), item.getPdLvl2(), item.getPdLvl3());
            if(products.size() > 0) i.setPdNm(products.get(0).getPdNm());

            items.add(i);
        }

        moOrderItems.setItems(items);
        return new ResponseEntity<>(moOrderItems, HttpStatus.OK);
    }

    @RequestMapping(value = "/client/cancelOrder", method = RequestMethod.PATCH)
    public ResponseEntity<?> cancelOrder(@RequestBody MoOrder order){
//        필수 컬럼 체크
        if(StringUtils.isEmpty(order.getOrCd())) return new ResponseEntity<>("orCd(주문코드) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        if(StringUtils.isEmpty(order.getMbCd()) || "0".equals(order.getMbCd())) {
            return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

        TbOrder tbOrder = tbOrderRepository.findOne(order.getOrCd());

        if(tbOrder == null) return new ResponseEntity<>(order.getOrCd() +" orCd(주문코드) 값에 해당하는 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);
        if(!tbOrder.getMbCd().equals(order.getMbCd())) new ResponseEntity<>("mbCd 값이 주문정보와 일치하지 않습니다!", HttpStatus.BAD_REQUEST);

        tbOrder.setOrStatus("0111"); // 주문취소
        tbOrderRepository.save(tbOrder);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    http://baekmin.synapsetech.co.kr:8080/img/example/plofile.png
    @RequestMapping("/getThumb")
    public ResponseEntity<?> getArticleImage(@RequestParam(value = "url", required = false) final String url) throws IOException {
//    public ResponseEntity<byte[]> getArticleImage(@RequestParam(value = "url", required = false) final String url) throws IOException {

        String strFilePath = this.getFilePath(url);
        if(StringUtils.isEmpty(strFilePath)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Path path = Paths.get(strFilePath);
        String contentType = Files.probeContentType(path);
//        FileSystemResource file = new FileSystemResource(strFilePath);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(contentType));

//        return new ResponseEntity(new InputStreamResource(file.getInputStream()), httpHeaders, HttpStatus.OK);

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        Thumbnails.of(strFilePath)
                .size(100, 100)
                .outputFormat("png")
                .toOutputStream(bStream);

        return new ResponseEntity(bStream.toByteArray(), httpHeaders, HttpStatus.OK);
    }

    @Value("${emp_img_dir}")
    String emp_img_dir;

    private String getFilePath(final String url) {
        String strFilePath = null;

        if(url.startsWith("/emp_img/")) {
            strFilePath = url.replace("/emp_img/", emp_img_dir);
        }

        return strFilePath;
    }

    /**
     * 19.결제여부전송 : 고객이 결제한 정보를 전송한다.
     * @param ChargeOrder
     * @return
     */
    @RequestMapping(value = "/client/setChargeType", method = RequestMethod.PATCH)
    public ResponseEntity<?> setChargeType(@RequestBody TbOrder ChargeOrder) {

        //        필수 컬럼 체크
        if (StringUtils.isEmpty(ChargeOrder.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(ChargeOrder.getOrCd())) return new ResponseEntity<>("orCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(ChargeOrder.getOrChargeType())) return new ResponseEntity<>("orChargeType 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder order = tbOrderRepository.findOne(ChargeOrder.getOrCd());
        if (order == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);
        if (!order.getMbCd().equals(ChargeOrder.getMbCd())) return new ResponseEntity<>("주문자 정보가 일치하지 않습니다!", HttpStatus.BAD_REQUEST);

        order.setOrChargeType(ChargeOrder.getOrChargeType());

//        결재유형
        if (!StringUtils.isEmpty(ChargeOrder.getOrChargeGubun())) order.setOrChargeGubun(ChargeOrder.getOrChargeGubun());

        tbOrderRepository.save(order);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 19.결제여부검색 : 주문에 대한 결제 여부를 검색한다.
     * @return
     */
    @RequestMapping(value = "/client/getChargeType", method = RequestMethod.GET)
    public ResponseEntity<?> getChargeType(@RequestParam(value="orCd", required=false) final Long orCd) {

        //        필수 컬럼 체크
        if(orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder order = tbOrderService.getTbOrderById(orCd);
        if(order == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        /*String orChargeType = order.getOrChargeType();
        if(StringUtils.isEmpty(orChargeType)) orChargeType = "N";
        return new ResponseEntity<>(orChargeType, HttpStatus.OK);*/

        if(StringUtils.isEmpty(order.getOrChargeType())) order.setOrChargeType("N");

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 18.주문상세(배송중) : 배송 중 상태의 주문상세정보를 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/client/getOrderDetail3", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderDetail3(@RequestParam(value="mbCd", required=false) final Integer mbCd,
                                             @RequestParam(value="orCd", required=false) final Long orCd) {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        MoOrderDetail2 order = tbOrderService.getOrderDetail3(mbCd, orCd);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 17.주문상세(결제대기/결제완료/배송중) :	결제대기/결제완료 상태의 주문상세정보를 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/client/getOrderDetail2", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderDetail2(@RequestParam(value="mbCd", required=false) final Integer mbCd,
                                             @RequestParam(value="orCd", required=false) final Long orCd) {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        MoOrderDetail2 order = tbOrderService.getOrderDetail2(mbCd, orCd);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 16.주문상세(수거중) : 수거 중 상태의 주문상태의 주문상세내역을 가져온다.
     * @param mbCd
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/client/getOrderDetail1", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderDetail1(@RequestParam(value="mbCd", required=false) final Integer mbCd,
                                             @RequestParam(value="orCd", required=false) final Long orCd) {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(orCd == null) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        MoOrderDetail1 order = tbOrderService.getOrderDetail1(mbCd, orCd);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 15.이용내역 : 최근일자순으로 주문에서 배송까지 모두 완료된 내역을 가져온다.
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/getCompleteList", method = RequestMethod.GET)
    public ResponseEntity<?> getCompleteList(@RequestParam(value="mbCd", required=false) final Integer mbCd) {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<String> status = new ArrayList<String>();
        status.add("0110"); // 배송완료
        status.add("0111"); // 주문취소

        List<?> list = tbOrderService.getCompleteList(mbCd, status);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 14.주문현황 : 주문상태가 완료되지 않은 내역들을 가져온다.
     * @param mbCd
     * @return
     */
    @RequestMapping(value = "/client/getOrderList", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderList(@RequestParam(value="mbCd", required=false) final Integer mbCd) {

        if(mbCd == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<String> status = new ArrayList<String>();
        status.add("0110"); // 배송완료
        status.add("0111"); // 주문취소

//        List<?> list = tbOrderRepository.getOrdByNotStatus(status);
        List<?> list = tbOrderService.getMoOrderList(mbCd, status);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 13.만족도설문
     * @param pollOrder
     * @return
     */
    @RequestMapping(value = "/client/setPoll", method = RequestMethod.PATCH)
    public ResponseEntity<?> setPoll(@RequestBody TbOrder pollOrder) {

        //        필수 컬럼 체크
        if (StringUtils.isEmpty(pollOrder.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(pollOrder.getOrCd())) return new ResponseEntity<>("orCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(pollOrder.getOrFeedbackSvr())) return new ResponseEntity<>("세탁물만족도 : orFeedbackSvr 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(pollOrder.getOrFeedbackEmp())) return new ResponseEntity<>("코디만족도 : orFeedbackEmp 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(pollOrder.getOrFeedbackSat())) return new ResponseEntity<>("서비스만족도 : orFeedbackSat 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(pollOrder.getOrFeedbackMemo())) return new ResponseEntity<>("기타하실말씀 : orFeedbackMemo 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder order = tbOrderRepository.findOne(pollOrder.getOrCd());
        if (order == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);
        if (!order.getMbCd().equals(pollOrder.getMbCd())) return new ResponseEntity<>("주문자 정보가 일치하지 않습니다!", HttpStatus.BAD_REQUEST);

        TbMember member = tbMemberRepository.findOne(pollOrder.getMbCd());
        if(member == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        order.setOrFeedbackSvr(pollOrder.getOrFeedbackSvr());
        order.setOrFeedbackEmp(pollOrder.getOrFeedbackEmp());
        order.setOrFeedbackSat(pollOrder.getOrFeedbackSat());
        order.setOrFeedbackMemo(pollOrder.getOrFeedbackMemo());

        tbOrderRepository.save(order);

//        설문조사시: 100포인트
        Integer mbPoint = member.getMbPoint();
        if(mbPoint == null || mbPoint < 0) mbPoint = 0;
        mbPoint += 100;
        member.setMbPoint(mbPoint);
        tbMemberRepository.save(member);

        //        포인트 적립내역 저장
        TbPoint tbPoint = new TbPoint();

        tbPoint.setMbCd(member.getMbCd());
        tbPoint.setPiPoint(100);
        tbPoint.setPiUseDt(new Date());
        tbPoint.setPlUseMemo("설문조사 포인트 적립");

        tbPointService.savePointUse(tbPoint);

//        08.설문조사시: "고객님 앞으로 100포인트가 발급되었습니다."
        try {
            pushMobileService.sendPollMsg(member);
        } catch (Exception e) {
            log.error("설문조사시 푸시 메세지 에러 {}", e);
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * FAQ
     * @param spec
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/client/listFaq", method = RequestMethod.GET)
    public ResponseEntity<?> listFaq(
                        @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbFaq> spec,
            @PageableDefault(sort = {"faTitle"}, size = 1000) Pageable pageable) {

        Iterable<TbFaq> list0 = tbFaqRepository.findAll(spec, pageable);
        List<TbFaq> list = StreamSupport.stream(list0.spliterator(), false).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 추천인코드 등록
     * @param member
     * @return
     */
    @RequestMapping(value = "/client/setMbSucode", method = RequestMethod.PATCH)
    public ResponseEntity<?> setMbSucode(@RequestBody TbMember member){
        //        필수 컬럼 체크
        if(StringUtils.isEmpty(member.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(member.getMbSucode())) return new ResponseEntity<>("mbSucode 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember mbr = tbMemberRepository.findOne(member.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

//        존재하는 추천인 코드인지 확인
        TbMember recMember = tbMemberRepository.findByMbMycode(member.getMbSucode());
        if(recMember == null) return new ResponseEntity<>("해당 추천인 코드를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        mbr.setMbSucode(member.getMbSucode());

//        추천인 등록한 사람/등록된 사람 모두 1000포인트가 주어짐
        Integer mbPoint = mbr.getMbPoint();
        if(mbPoint == null || mbPoint < 0) mbPoint = 0;
        mbPoint += 1000;
        mbr.setMbPoint(mbPoint);

        tbMemberService.saveTbMember(mbr);

//        포인트 적립내역 저장
        TbPoint tbPoint = new TbPoint();

        tbPoint.setMbCd(mbr.getMbCd());
        tbPoint.setPiPoint(1000);
        tbPoint.setPiUseDt(new Date());
        tbPoint.setPlUseMemo("추천인코드 등록 포인트 적립");

        tbPointService.savePointUse(tbPoint);

        try {
            pushMobileService.sendSucode(mbr); // 07.추천코드 입력: ""고객님 앞으로 1000포인트가 발급되었습니다.""
        } catch (Exception ex) {
            log.error("추천인 코드 푸시 메세지 전송 에러 {}", ex);
        }

//        if(recMember != null) {
        Integer recPoint = recMember.getMbPoint();
        if (recPoint == null || mbPoint < 0) recPoint = 0;
        recPoint += 1000;
        recMember.setMbPoint(recPoint);
        tbMemberService.saveTbMember(recMember);

        //        포인트 적립내역 저장
        tbPoint = new TbPoint();

        tbPoint.setMbCd(recMember.getMbCd());
        tbPoint.setPiPoint(1000);
        tbPoint.setPiUseDt(new Date());
        tbPoint.setPlUseMemo("추천인코드 등록 포인트 적립");

        tbPointService.savePointUse(tbPoint);

            try {
                pushMobileService.sendSucode(recMember); // 07.추천코드 입력: ""고객님 앞으로 1000포인트가 발급되었습니다.""
            } catch (Exception ex) {
                log.error("추천인 코드 푸시 메세지 전송 에러 {}", ex);
            }

//        }

        return new ResponseEntity<>(mbr, HttpStatus.OK);
    }

    /**
     * 광고수신거부
     * @param member
     * @return
     */
    @RequestMapping(value = "/client/setMbSms", method = RequestMethod.PATCH)
    public ResponseEntity<?> setMbSms(@RequestBody TbMember member){
        //        필수 컬럼 체크
        if(StringUtils.isEmpty(member.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(member.getMbSms())) return new ResponseEntity<>("mbSms 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember mbr = tbMemberRepository.findOne(member.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        mbr.setMbSms(member.getMbSms());
        tbMemberService.saveTbMember(mbr);

        return new ResponseEntity<>(mbr, HttpStatus.OK);
    }

    /**
     * 기념일 변경
     * @param member
     * @return
     */
    @RequestMapping(value = "/client/setMbBirth", method = RequestMethod.PATCH)
    public ResponseEntity<?> setMbBirth(@RequestBody TbMember member){
        //        필수 컬럼 체크
        if(StringUtils.isEmpty(member.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(member.getMbBirth())) return new ResponseEntity<>("mbBirth 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember mbr = tbMemberRepository.findOne(member.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        mbr.setMbBirth(member.getMbBirth());
        tbMemberService.saveTbMember(mbr);

        return new ResponseEntity<>(mbr, HttpStatus.OK);
    }

    /**
     * 전화번호 변경
     * @param member
     * @return
     */
    @RequestMapping(value = "/client/setMbTel", method = RequestMethod.PATCH)
    public ResponseEntity<?> setMbTel(@RequestBody TbMember member){
        //        필수 컬럼 체크
        if(StringUtils.isEmpty(member.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(member.getMbTel())) return new ResponseEntity<>("mbTel 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember mbr = tbMemberRepository.findOne(member.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        mbr.setMbTel(member.getMbTel());
        tbMemberService.saveTbMember(mbr);

        return new ResponseEntity<>(mbr, HttpStatus.OK);
    }

    /**
     * 닉네임 변경
     * @param member
     * @return
     */
    @RequestMapping(value = "/client/setMbNicNm", method = RequestMethod.PATCH)
    public ResponseEntity<?> setMbNicNm(@RequestBody TbMember member){
        //        필수 컬럼 체크
        if(StringUtils.isEmpty(member.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(member.getMbNicNm())) return new ResponseEntity<>("mbNicNm 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember mbr = tbMemberRepository.findOne(member.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        mbr.setMbNicNm(member.getMbNicNm());
        tbMemberService.saveTbMember(mbr);

        return new ResponseEntity<>(mbr, HttpStatus.OK);
    }

    /**
     * 결제정보를 추가
     **/
    @RequestMapping(value = "/client/setBillinfo", method = RequestMethod.POST)
    public ResponseEntity<?> setBillinfo(@RequestBody MoBillinfo billinfo){
//        필수 컬럼 체크
        if(StringUtils.isEmpty(billinfo.getMbCd())) return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(billinfo.getMbBillinfo())) return new ResponseEntity<>("mbBillinfo 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbMember member = tbMemberRepository.findOne(billinfo.getMbCd());
        if(member == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        /*member.setMbBillinfo(billinfo.getMbBillinfo());
        tbMemberRepository.save(member);*/

        TbMemberBillinfo newBillinfo = new TbMemberBillinfo();
        newBillinfo.setMbCd(billinfo.getMbCd());
        newBillinfo.setMbBillinfo(billinfo.getMbBillinfo());

        tbMemberBillinfoRepository.save(newBillinfo);
        return new ResponseEntity<>(newBillinfo, HttpStatus.OK);

        /*List<?> billinfo_list = tbMemberBillinfoRepository.findByMbCd(billinfo.getMbCd());
        return new ResponseEntity<>(billinfo_list, HttpStatus.OK);*/
    }

//    @RequestMapping(value = "/client/setBillinfo", method = RequestMethod.PATCH)
//    public ResponseEntity<?> setBillinfo(
//            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbMember> spec1,
//            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
//                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2
//    ){
//        if (spec1 == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
//
//        //        db 검색
//        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
//        TbMember member = tbMemberRepository.findOne(spec);
//
////        tbMemberRepository
//
//        // 존재하지 않는 사용자일경우
//        if(member == null)  return new ResponseEntity<>("해당 사용자 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);
//
//        /*MoBillinfo Billinfo = new MoBillinfo();
//        Billinfo.setMbCd(member.getMbCd());
//        Billinfo.setMbBillinfo(member.getMbBillinfo());*/
//
//        return new ResponseEntity<>("Billinfo", HttpStatus.OK);
//    }

    /**
     * 결제정보 가져온다.
     * @return
     */
    @RequestMapping(value = "/client/getBillinfo", method = RequestMethod.GET)
    public ResponseEntity<?> getBillinfo(@And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbMemberBillinfo> spec){

        if (spec == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        //        db 검색
        List<?> list = tbMemberBillinfoRepository.findAll(spec);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 결제정보 가져온다.
     * @param spec1
     * @param spec2
     * @return
     */
/*    @RequestMapping(value = "/client/getBillinfo", method = RequestMethod.GET)
    public ResponseEntity<?> getBillinfo(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2
    ){
        if (spec1 == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        //        db 검색
        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
        TbMember member = tbMemberRepository.findOne(spec);

        // 존재하지 않는 사용자일경우
        if(member == null)  return new ResponseEntity<>("해당 사용자 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        MoBillinfo Billinfo = new MoBillinfo();
        Billinfo.setMbCd(member.getMbCd());
        Billinfo.setMbBillinfo(member.getMbBillinfo());

        return new ResponseEntity<>(Billinfo, HttpStatus.OK);
    }*/

/*    @RequestMapping(value = "/client/createOrder", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@Valid TbOrder order, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }*/

    /**
     * 주문생성
     고객코드	: mbCd
     방문요청시간 :	orReqDt
     수거요청사항 :	orReqMemo
     주소1 :	mbAddr1
     주소2 :	mbAddr2
     위도 :	mbLat
     경도 :	mbLng
     *
     * @param order
     * @return
     */
    @RequestMapping(value = "/client/createOrder", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody MoOrder order){

//        필수 컬럼 체크
        if(StringUtils.isEmpty(order.getMbCd()) || "0".equals(order.getMbCd())) {
            return new ResponseEntity<>("mbCd 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(order.getOrReqDt())) return new ResponseEntity<>("orReqDt 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
//        if(StringUtils.isEmpty(order.getOrReqMemo())) return new ResponseEntity<>("orReqMemo 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(order.getMbAddr1())) return new ResponseEntity<>("mbAddr1 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(order.getMbAddr2())) return new ResponseEntity<>("mbAddr2 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(order.getMbTel())) return new ResponseEntity<>("mbTel(연락처) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        if(StringUtils.isEmpty(order.getMbLat()) || "0.0".equals(order.getMbLat().trim())) {
            return new ResponseEntity<>("mbLat 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(order.getMbLng()) || "0.0".equals(order.getMbLng().trim())) {
            return new ResponseEntity<>("mbLng 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

//        회원 존재여부 체크
        if(!tbMemberRepository.exists(order.getMbCd())) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        /*최근주소
        Query selectQuery = entityManager.createQuery("SELECT new cleanbank.viewmodel.MoOrderList(t.orCd, t.regiDt, t.orStatus, c.cdNm)" +
                " FROM TbOrder as t, TbCode as c" +
                " where t.mbCd = ?1 and t.orStatus in ?2 and (t.delYn is null or t.delYn != 'Y')" +
                " AND t.orStatus = CONCAT(c.cdGp, c.cdIt)" +
                " ORDER BY t.orCd DESC")
                .setParameter(1, mbCd)
                .setParameter(2, status);

        List<?> list = selectQuery.getResultList();


//        최근주소 Insert
        TbAddress address = new TbAddress();

        address.setEvtNm("신규");
        address.setUser("모바일");
        address.setRegiDt(new Date());
        address.setMbAddr1(order.getMbAddr1());
        address.setMbAddr2(order.getMbAddr2());
        address.setMbLat(order.getMbLat());
        address.setMbLng(order.getMbLng());

        TbAddress savedAddress = tbAddressRepository.save(address);*/

        /*TbAddress savedAddress = tbAddressService.saveMoAddress(order);

//        주문 데이터 Insert
        TbOrder newOrder = new TbOrder();
        newOrder.setEvtNm("신규");
        newOrder.setUser("모바일");
        newOrder.setRegiDt(new Date());

        newOrder.setMbCd(order.getMbCd());
        newOrder.setOrReqDt(order.getOrReqDt());
        newOrder.setOrReqMemo(order.getOrReqMemo());
        newOrder.setOrReqAddr(savedAddress.getId().toString());

        TbOrder saveOrder = tbOrderRepository.save(newOrder);*/
//        TbOrder saveOrder = tbOrderService.saveTbOrder(order);

        TbOrder saveOrder = tbOrderService.saveMoOrder(order);

        return new ResponseEntity<>(saveOrder, HttpStatus.OK);
    }

    /**
     * 최근 이용한 주소내역을 가져온다.
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/client/listAddress", method = RequestMethod.GET)
    public ResponseEntity<?> listAddress(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbAddress> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbAddress> spec2,
            @PageableDefault(sort = {"mbAddr1", "id"}, size = 1000) Pageable pageable) {

        if (spec1 == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbAddress> spec = Specifications.where(spec1).and(spec2);

//        회원id 검색조건이 있을때만 검색한다
        Iterable<TbAddress> list0 = tbAddressRepository.findAll(spec, pageable);
        List<TbAddress> list = StreamSupport.stream(list0.spliterator(), false).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/client/deleteAddress", method = RequestMethod.DELETE)
    public ResponseEntity<?> filterAddress(
            @And({
                    @Spec(path = "mbCd", spec = Equal.class),
                    @Spec(path = "id", spec = Equal.class)
            }) Specification<TbAddress> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbAddress> spec2,
            @PageableDefault(sort = {"mbAddr1", "id"}, size = 1000) Pageable pageable) {

        if (spec1 == null) {
            return new ResponseEntity<>("mbCd및 id 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

        //        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String mbCd = request.getParameter("mbCd");
        String id = request.getParameter("id");
//        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(mbCd)) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbAddress> spec = Specifications.where(spec1).and(spec2);

//        회원id 검색조건이 있을때만 검색한다
        TbAddress address = tbAddressRepository.findOne(spec);

        if (address == null) {
            return new ResponseEntity<>("해당 주소 데이터를 찾을수 없습니다!", HttpStatus.NOT_FOUND);
        }

//        삭제여부를 Y로
        address.setDelYn("Y");
        tbAddressRepository.save(address);

        return new ResponseEntity<>("삭제하였습니다!", HttpStatus.NO_CONTENT);
    }

    /**
     * 01.로그인 및 회원가입
     *
     * 로그인(카카오톡/네이버/페이스북)을 한다. 각 외부플랫폼에서 소셜미디어 로그인 결과를 서버에 전송한다.
     * 서버에서는 아이디 및 디바이스토큰 값을 기준으로 회원가입 여부를 확인 후, 로그인 혹은 가입처리를 한다.
     */
    @RequestMapping(value = "/client/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(
            @And({
                    @Spec(path = "mbEmail", spec = Equal.class),
                    @Spec(path = "mbDeviceToken", spec = Equal.class)
            }) Specification<TbMember> spec1,
            @Or({
                    @Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)
            }) Specification<TbMember> spec2,
            @RequestParam(value="mbNicNm", required=false) final String mbNicNm,
            @RequestParam(value="mbPath", required=false) final String mbPath
    ) {

        TbMember member = new TbMember();

//        검색조건을 모두 입력하지 않았을 경우
        if(spec1 == null) return new ResponseEntity<>("mbEmail, mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String mbEmail = request.getParameter("mbEmail");
        String mbDeviceToken = request.getParameter("mbDeviceToken");
        String mbPushToken = request.getParameter("mbPushToken");
//        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(mbEmail)) return new ResponseEntity<>("mbEmail 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbDeviceToken)) return new ResponseEntity<>("mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbPushToken)) return new ResponseEntity<>("mbPushToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbNicNm)) return new ResponseEntity<>("mbNicNm 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbPath)) return new ResponseEntity<>("mbPath 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
        member = tbMemberRepository.findOne(spec);

        if(member == null) { // 존재하지 않는 사용자일경우 자동 회원가입(insert) 처리
            member = new TbMember();

            member.setMbEmail(mbEmail);
            member.setMbDeviceToken(mbDeviceToken);
            member.setMbNicNm(mbNicNm);
            member.setMbPath(mbPath);

            member.setBnCd(null);
            member.setMbJoinDt(new Date());
            member.setMbPush("Y");
            member.setMbSms("Y");
            member.setMbStatus(null);
            member.setMbTel(null);
            member.setUser("mobile");
            member.setMbLevel("3"); // 회원등급 Green
            member.setMbNm(mbNicNm);
            member.setMode("new");
            member.setMbPushToken(mbPushToken);

            member.setDelYn("X"); // 신규가입후 아직 약관 동의를 하지 않음

//            member = tbMemberRepository.save(member); mbMycode를 생성해야 되므로 직접 호출하면 안됨
            member = tbMemberService.saveTbMember(member);

            member.setMbNewYn("Y"); // 자동 회원가입 여부 최초에만 Y
        } else {

            if("X".equals(member.getDelYn())) { // 신규가입후 아직 약관 동의를 하지 않음
                member.setMbNewYn("Y");
            } else {
                member.setMbNewYn("N");
            }

            //        검색된 사용자의 최근 주소 검색
            List<TbAddress> list = tbAddressRepository.getAddressList(member.getMbCd());

            if(list.size() > 0) {
                TbAddress tbAddress = list.get(0);

//                member.setMbAddr(tbAddress.getMbAddr());
                member.setMbAddr1(tbAddress.getMbAddr1());
                member.setMbAddr2(tbAddress.getMbAddr2());
                member.setMbLat(tbAddress.getMbLat());
                member.setMbLng(tbAddress.getMbLng());
            }

            member.setMbPushToken(mbPushToken);
            member = tbMemberService.saveTbMember(member);
        }

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /*@RequestMapping(value = "/client/login2", method = RequestMethod.GET)
    public ResponseEntity<?> login2(
            @And({
                    @Spec(path = "mbEmail", spec = Equal.class),
                    @Spec(path = "mbDeviceToken", spec = Equal.class)
            }) Specification<moMember> spec1,
            @Or({
                    @Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)
            }) Specification<moMember> spec2,
            @RequestParam(value="mbNicNm", required=false) final String mbNicNm,
            @RequestParam(value="mbPath", required=false) final String mbPath
    ) {

        moMember member = new moMember();

//        검색조건을 모두 입력하지 않았을 경우
        if(spec1 == null) return new ResponseEntity<>("mbEmail, mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String mbEmail = request.getParameter("mbEmail");
        String mbDeviceToken = request.getParameter("mbDeviceToken");
//        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(mbEmail)) return new ResponseEntity<>("mbEmail 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbDeviceToken)) return new ResponseEntity<>("mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbNicNm)) return new ResponseEntity<>("mbNicNm 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbPath)) return new ResponseEntity<>("mbPath 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        Specification<moMember> spec = Specifications.where(spec1).and(spec2);
        member = tbMemberRepository.findOne(spec);

        if(member == null) { // 존재하지 않는 사용자일경우 자동 회원가입(insert) 처리
            member = new moMember();

            member.setMbEmail(mbEmail);
            member.setMbDeviceToken(mbDeviceToken);
            member.setMbNicNm(mbNicNm);
            member.setMbPath(mbPath);

            member.setBnCd(null);
            member.setEvtNm("추가");
            member.setMbJoinDt(new Date());
            member.setMbLevel(null);
            member.setMbPush("Y");
            member.setMbSms("Y");
            member.setMbStatus(null);
            member.setMbTel(null);
            member.setRegiDt(new Date());
            member.setUser("mobile");

            member.setMbNm(mbNicNm);

//            member = tbMemberRepository.save(member); mbMycode를 생성해야 되므로 직접 호출하면 안됨
            member = tbMemberService.saveTbMember(member);

        } else {
            //        검색된 사용자의 최근 주소 검색
            List<TbAddress> list = tbAddressRepository.getAddressList(member.getMbCd());

            if(list.size() > 0) {
                TbAddress tbAddress = list.get(0);

                member.setMbAddr(tbAddress.getMbAddr());
                member.setMbLat(tbAddress.getMbLat());
                member.setMbLng(tbAddress.getMbLng());
            }
        }

        return new ResponseEntity<>(member, HttpStatus.OK);
    }*/

/**
 * 01.로그인 및 회원가입
 *
 * 로그인(카카오톡/네이버/페이스북)을 한다. 각 외부플랫폼에서 소셜미디어 로그인 결과를 서버에 전송한다.
 * 서버에서는 아이디 및 디바이스토큰 값을 기준으로 회원가입 여부를 확인 후, 로그인 혹은 가입처리를 한다.
 */
/*@RestController
@RequestMapping("/m/member")
public class TbMemberController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbAddressRepository tbAddressRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(
            @And({
                    @Spec(path = "mbEmail", spec = Equal.class),
                    @Spec(path = "mbDeviceToken", spec = Equal.class),
                    @Spec(path = "mbPath", spec = Equal.class)
            }) Specification<TbMember> spec1,
            @Or({
                    @Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)
            }) Specification<TbMember> spec2
    ) {

        TbMember member = new TbMember();

//        검색조건을 모두 입력하지 않았을 경우
        if(spec1 == null) return new ResponseEntity<>("mbEmail, mbDeviceToken, mbPath 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 검색조건 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String mbEmail = request.getParameter("mbEmail");
        String mbDeviceToken = request.getParameter("mbDeviceToken");
        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(mbEmail)) return new ResponseEntity<>("mbEmail 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbDeviceToken)) return new ResponseEntity<>("mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbPath)) return new ResponseEntity<>("mbPath 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
        member = tbMemberRepository.findOne(spec);

        if(member == null)  return new ResponseEntity<>("해당 사용자 정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

//        검색된 사용자의 최근 주소 검색
        List<TbAddress> list = tbAddressRepository.getAddressList(member.getMbCd());

        if(list.size() > 0) {
            TbAddress tbAddress = list.get(0);

            member.setMbAddr(tbAddress.getMbAddr());
            member.setMbLat(tbAddress.getMbLat());
            member.setMbLng(tbAddress.getMbLng());
        }

        return new ResponseEntity<>(member, HttpStatus.OK);
    }*/

/*    @RequestMapping(value = "/login")
    public TbMember filterList(
            @And({
                    @Spec(path = "mbEmail", spec = Equal.class),
                    @Spec(path = "mbDeviceToken", spec = Equal.class),
                    @Spec(path = "mbPath", spec = Equal.class)
            }) Specification<TbAddress> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbAddress> spec2
    ) {

        TbMember member = new TbMember();

        if(spec1 == null) {
            member.setMsg("mbEmail, mbDeviceToken, mbPath 파라메터를 입력해 주세요!");
        }

        Specification<TbAddress> spec = Specifications.where(spec1).and(spec2);

        if(member == null) member = new TbMember();

        return member;
    }*/

    /**
     * 모바일 앱에서 본인인증 승인 처리가 완료 되었기 때문에 db 처리만 하면 된다
     * @param request
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws UnknownHostException
     */
    @RequestMapping(value = "/client/inicis-authbill-old", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_authbill_old(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, UnknownHostException {
        String strResult = "inicis-authbill!!!!";

//        이니시스 결제결과 파라메터 확인
        StringBuilder strLog = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            strLog.append(paramName);
            strLog.append("\n");

            String[] paramValues = request.getParameterValues(paramName);

            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                strLog.append("\t" + paramValue);
                strLog.append("\n");
            }
        }

        log.debug("이니시스 nicis-authbill \n {}", strLog.toString());

        String resultcode = request.getParameter("resultcode");

        if("00".equals(resultcode)) {
/*            String orderid = request.getParameter("orderid");
            String cardno = request.getParameter("cardno");
            String billkey = request.getParameter("billkey");

            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();
            log.debug("Current IP address : {}", ip.getHostAddress());
            log.debug("Current IP address : {}", ipAddress);

//            테스트
            try {
                String tid = com.inicis.inipay4.util.Tool.makeTid("card", P_MID);
                log.error("makeTid 테스트 결과 {}", tid);
            } catch (Exception e) {
                log.error("makeTid 테스트 에러 {}", e);
            }*/

            //#############################################################################
            //# 1. 인스턴스 생성 #
            //####################
            INIpay inipay = new INIpay();
            INIdata data = new INIdata();

            //#############################################################################
            //# 2. 정보 설정 #
            //################
            data.setData("type", "authbill");                                 // 결제 type, 고정
            data.setData("inipayHome", inipayHome);                           // 이니페이가 설치된 절대경로
            data.setData("logMode", logMode);                                 // logMode
            data.setData("keyPW",keyPW);                                      // 키패스워드
            data.setData("subPgip","203.238.37.3");                           // Sub PG IP (고정)
            data.setData("mid", request.getParameter("mid"));                 // 상점아이디
            data.setData("paymethod", request.getParameter("paymethod"));     // 지불방법, 빌링등록
            data.setData("billtype", "Card");                                 // 빌링유형  고정
            data.setData("price", "1000");                                    // 가격, 결제되지 않는 고정설정값
            data.setData("currency", "WON");                                  // 화폐단위
            data.setData("goodname", request.getParameter("goodname"));       // 상품명 (최대 40자)
            data.setData("buyername", request.getParameter("buyername"));     // 구매자 (최대 15자)
            data.setData("buyertel", request.getParameter("buyertel"));       // 구매자이동전화
            data.setData("buyeremail", request.getParameter("buyeremail"));   // 구매자이메일
            data.setData("url", "http://www.your_domain.co.kr");              // 홈페이지 주소(URL)
            data.setData("uip", request.getRemoteAddr());                     // IP Addr
            data.setData("encrypted", request.getParameter("encrypted"));
            data.setData("sessionkey", request.getParameter("sessionkey"));
            data.setData("crypto", "execure");                                // Extrus 암호화 모듈 적용(고정)

            //###############################################################################
            //# 3. 지불 요청 #
            //################
            data = inipay.payRequest(data);

            log.debug("빌링요청 결과 {}", data);

            //###############################################################################
            // # 4. DB처리 #
            //################
            if ("00".equals(data.getData("ResultCode")))
            {
                // TODO : DB 처리
            }

            //###############################################################################
            //# 5. 요청 결과 #
            //################
            String resultCode = data.getData("ResultCode");    // "00"이면 신용카드 등록 및 빌키생성 성공
            String resultMsg  = data.getData("ResultMsg");     // 결과에 대한 설명
            String CardCode   = data.getData("CardResultCode");// 카드사 코드
            String BillKey    = data.getData("BillKey");       // BILL KEY
            String CardPass   = data.getData("CardPass");      // 카드 비밀번호 앞 두자리
            String CardKind   = data.getData("CardKind");      // 카드 종류 ( 개인 -0 , 법인 -1)
            String tid        = data.getData("tid");           // 거래번호

            log.debug("빌링 본인확인 resultCode = {}", resultCode);
            log.debug("빌링 본인확인 resultMsg = {}", resultMsg);
            log.debug("빌링 본인확인 CardCode = {}", CardCode);
            log.debug("빌링 본인확인 BillKey = {}", BillKey);
            log.debug("빌링 본인확인 CardPass = {}", CardPass);
            log.debug("빌링 본인확인 CardKind = {}", CardKind);
            log.debug("빌링 본인확인 tid = {}", tid);

        } else {
            log.error("빌링 본인확인 실패!!!!");
        }

        strResult = "<script>window.location = 'cleanbank://billing';</script>";

        return strResult;
    }
}
