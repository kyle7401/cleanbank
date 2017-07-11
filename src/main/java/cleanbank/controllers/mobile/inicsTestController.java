package cleanbank.controllers.mobile;

import cleanbank.domain.InicisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import com.inicis.inipay4.INIpay;
import com.inicis.inipay4.util.INIdata;

//import org.apache.log4j.Appender;

/**
 * Created by hyoseop on 2016-02-17.
 */
@RestController
@RequestMapping("/api")
public class inicsTestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * INIpay 환경 파일
     */
    String inipayHome = "/home/cleanbank/INIpay4JAVA";
    String logMode = "DEBUG"; // 반드시 대문자로 INFO, DEBUG로 설정
    String keyPW = "1111";

    @RequestMapping(value = "/client/inicis-result2", method = {RequestMethod.GET, RequestMethod.POST})
    public void test1(HttpServletRequest request) throws UnknownHostException {

        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        log.debug("Current IP address : {}", ip.getHostAddress());
        log.debug("Current IP address : {}", ipAddress);


//        ii.  INIpay41 클래스의 인스턴스 생성
        INIpay inipay = new INIpay();
        INIdata data = new INIdata();

//        iii.  정보 설정
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
//        data = inipay.payRequest(data);

        log.debug("빌링요청 결과 {}", data);
    }

    /**
     P_TID
     INIMX_ISP_100min000020160215185206411824
     P_MID
     100min0000
     P_AUTH_DT
     20160215185206
     P_STATUS
     01
     P_TYPE
     ISP
     P_OID
     69
     P_AMT
     100
     P_UNAME
     jin
     P_RMESG1
     ??? ????(1000?? ??? ?????? ???? ???)
     P_RMESG2
     00

     P_ISP_CARDCODE
     050204040016656

     P_RMESG3
     RM3_DISC_AMT^|RM3_PRICE^100|RM3_ORG_AMT^|RM3_EVENT_CODE^|RM3_INTEREST^0

     P_MERCHANT_RESERVED
     dXNlcG9pbnQ9MCY=

     P_FN_CD1

     P_FN_CD2

     P_FN_NM

     P_NOTI

     P_AUTH_NO

     P_CARD_ISSUER_CODE

     P_CARD_NUM

     P_CARD_MEMBER_NUM

     P_CARD_PURCHASE_CODE

     P_PRTC_CODE

     P_CARD_PURCHASE_NAME

     P_CARD_ISSUER_NAME

     */

    static String convert(String str, String encoding) throws IOException {
        ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
        requestOutputStream.write(str.getBytes(encoding));
        return requestOutputStream.toString(encoding);
    }

    // 한글 인코딩 테스트
    public static void charSet(String str_kr) throws UnsupportedEncodingException {
        String charset[] = {"euc-kr", "ksc5601", "iso-8859-1", "8859_1", "ascii", "UTF-8"};

        for(int i=0; i<charset.length ; i++){
            for(int j=0 ; j<charset.length ; j++){
                if(i==j) continue;

                System.out.println(charset[i]+" : "+charset[j]+" :"+new String(str_kr.getBytes(charset[i]),charset[j]));
            }
        }
    }

    @RequestMapping(value = "/client/inicis/card-noti", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_card_noti(@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
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
                                   @RequestParam(value="P_AUTH_NO", required=false) final String P_AUTH_NO,
                                   HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {

//        req.setCharacterEncoding("euc-kr");
        /*String str2 = new String(req.getParameter("P_FN_CD1").getBytes("EUC-KR"));
        String decoded_result = new String(req.getParameter("P_FN_CD1").getBytes("euc-kr"), "UTF-8");

        String strUrlDecode = URLDecoder.decode(P_FN_CD1, "UTF-8");
        charSet(P_FN_CD1);

        //euc_kr_str - euc-kr 문자열
       *//* CharBuffer cbuffer = CharBuffer.wrap((new String(P_FN_CD1, "EUC-KR")).toCharArray());
        Charset utf8charset = Charset.forName("UTF-8");
        ByteBuffer bbuffer = utf8charset.encode(cbuffer);

        //변환된 UTF-8 문자열
        String tmpDecode = new String(bbuffer.array());*//*

        String[] a = new String[4];
        a[0] = "000000000조희경         555555555555555555555555555555555555555555555555555555555";
        a[1] = "111111111조희경         555555555555555555555555555555555555555555555555555555555";
        a[2] = P_FN_CD1;
        a[3] = req.getParameter("P_FN_CD1");
        Charset euckrcharset = Charset.forName("EUC-KR");
        Charset utf8charset = Charset.forName("UTF-8");

        for (int j = 0; j < a.length; j++) {
//            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"), 1, 50);
            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"));

            CharBuffer data = euckrcharset.decode(inputBuffer);
            ByteBuffer outputBuffer = utf8charset.encode(data);
            byte[] outputData = outputBuffer.array();
            System.out.println(new String(outputData));
        }*/

        String strResult = "card-noti!!!!";

        StringBuilder strLog = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

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

        log.debug("이니시스 card-noti \n {}", strLog.toString());

        MimeMessage mail = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo("hyoseop@synapsetech.co.kr");
        helper.setTo("hyoseop@synapsetech.co.kr");
//        helper.setReplyTo("hyoseop@gmail.com");
//        helper.setFrom("support@100min.co.kr");
        helper.setSubject("이니시스 결제 파라메터 확인 "+ dtNow);
        helper.setText(strLog.toString());

//        javaMailSender.send(mail);

//        이니시스 결제 파라메터 db에 입력
/*        MoInicisResult moInicisResult = new MoInicisResult();

        moInicisResult.setP_STATUS(P_STATUS);
        moInicisResult.setP_RMESG1(P_RMESG1);
        moInicisResult.setP_TID(P_TID);
        moInicisResult.setP_TYPE(P_TYPE);
        moInicisResult.setP_AUTH_DT(P_AUTH_DT);
        moInicisResult.setP_MID(P_MID);
        moInicisResult.setP_OID(P_OID);
        moInicisResult.setP_AMT(P_AMT);
        moInicisResult.setP_UNAME(P_UNAME);
        moInicisResult.setP_MNAME(P_MNAME);
        moInicisResult.setP_FN_CD1(P_FN_CD1);
        moInicisResult.setP_FN_NM(P_FN_NM);
        moInicisResult.setP_RMESG2(P_RMESG2);
        moInicisResult.setP_AUTH_NO(P_AUTH_NO);

        tbOrderService.inicis_result(moInicisResult);*/

        InicisResult inicisResult = new InicisResult();
        inicisResult.setPStatus(P_STATUS);
        inicisResult.setPRmesg1(P_RMESG1);
        inicisResult.setPTid(P_TID);
        inicisResult.setPType(P_TYPE);
        inicisResult.setPAuthDt(P_AUTH_DT);
        inicisResult.setPMid(P_MID);
        inicisResult.setPOid(P_OID);
        inicisResult.setPAmt(P_AMT);
        inicisResult.setPUname(P_UNAME);
        inicisResult.setPMname(P_MNAME);
        inicisResult.setPFnCd1(P_FN_CD1);
        inicisResult.setPFnNm(P_FN_NM);
        inicisResult.setPRmesg2(P_RMESG2);
        inicisResult.setPAuthNo(P_AUTH_NO);

//        해당 주문건을 결제 완료 처리및 100min 담당자에게 알림 메일 발송 필요
//        이니시스에서 실체 호출시 javax.persistence.RollbackException: Transaction marked as rollbackOnly 에러 발생하며 db에 결과가 저장되지 않음
//        tbOrderService.inicis_result(inicisResult);

        strResult = "OK";

        return strResult;
    }

    @RequestMapping(value = "/client/inicis/card-cancel", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_card_cancel(@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
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
                                     @RequestParam(value="P_AUTH_NO", required=false) final String P_AUTH_NO,
                                     HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {

//        req.setCharacterEncoding("euc-kr");
        /*String str2 = new String(req.getParameter("P_FN_CD1").getBytes("EUC-KR"));
        String decoded_result = new String(req.getParameter("P_FN_CD1").getBytes("euc-kr"), "UTF-8");

        String strUrlDecode = URLDecoder.decode(P_FN_CD1, "UTF-8");
        charSet(P_FN_CD1);

        //euc_kr_str - euc-kr 문자열
       *//* CharBuffer cbuffer = CharBuffer.wrap((new String(P_FN_CD1, "EUC-KR")).toCharArray());
        Charset utf8charset = Charset.forName("UTF-8");
        ByteBuffer bbuffer = utf8charset.encode(cbuffer);

        //변환된 UTF-8 문자열
        String tmpDecode = new String(bbuffer.array());*//*

        String[] a = new String[4];
        a[0] = "000000000조희경         555555555555555555555555555555555555555555555555555555555";
        a[1] = "111111111조희경         555555555555555555555555555555555555555555555555555555555";
        a[2] = P_FN_CD1;
        a[3] = req.getParameter("P_FN_CD1");
        Charset euckrcharset = Charset.forName("EUC-KR");
        Charset utf8charset = Charset.forName("UTF-8");

        for (int j = 0; j < a.length; j++) {
//            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"), 1, 50);
            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"));

            CharBuffer data = euckrcharset.decode(inputBuffer);
            ByteBuffer outputBuffer = utf8charset.encode(data);
            byte[] outputData = outputBuffer.array();
            System.out.println(new String(outputData));
        }*/

        String strResult = "fail!!!!";

        StringBuilder strLog = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

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

        log.debug("이니시스 card-cancel \n {}", strLog.toString());

        MimeMessage mail = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo("hyoseop@synapsetech.co.kr");
        helper.setTo("hyoseop@synapsetech.co.kr");
//        helper.setReplyTo("hyoseop@gmail.com");
//        helper.setFrom("support@100min.co.kr");
        helper.setSubject("이니시스 결제 파라메터 확인 "+ dtNow);
        helper.setText(strLog.toString());

//        javaMailSender.send(mail);

//        이니시스 결제 파라메터 db에 입력
/*        MoInicisResult moInicisResult = new MoInicisResult();

        moInicisResult.setP_STATUS(P_STATUS);
        moInicisResult.setP_RMESG1(P_RMESG1);
        moInicisResult.setP_TID(P_TID);
        moInicisResult.setP_TYPE(P_TYPE);
        moInicisResult.setP_AUTH_DT(P_AUTH_DT);
        moInicisResult.setP_MID(P_MID);
        moInicisResult.setP_OID(P_OID);
        moInicisResult.setP_AMT(P_AMT);
        moInicisResult.setP_UNAME(P_UNAME);
        moInicisResult.setP_MNAME(P_MNAME);
        moInicisResult.setP_FN_CD1(P_FN_CD1);
        moInicisResult.setP_FN_NM(P_FN_NM);
        moInicisResult.setP_RMESG2(P_RMESG2);
        moInicisResult.setP_AUTH_NO(P_AUTH_NO);

        tbOrderService.inicis_result(moInicisResult);*/

        InicisResult inicisResult = new InicisResult();
        inicisResult.setPStatus(P_STATUS);
        inicisResult.setPRmesg1(P_RMESG1);
        inicisResult.setPTid(P_TID);
        inicisResult.setPType(P_TYPE);
        inicisResult.setPAuthDt(P_AUTH_DT);
        inicisResult.setPMid(P_MID);
        inicisResult.setPOid(P_OID);
        inicisResult.setPAmt(P_AMT);
        inicisResult.setPUname(P_UNAME);
        inicisResult.setPMname(P_MNAME);
        inicisResult.setPFnCd1(P_FN_CD1);
        inicisResult.setPFnNm(P_FN_NM);
        inicisResult.setPRmesg2(P_RMESG2);
        inicisResult.setPAuthNo(P_AUTH_NO);

//        해당 주문건을 결제 완료 처리및 100min 담당자에게 알림 메일 발송 필요
//        이니시스에서 실체 호출시 javax.persistence.RollbackException: Transaction marked as rollbackOnly 에러 발생하며 db에 결과가 저장되지 않음
//        tbOrderService.inicis_result(inicisResult);

//        strResult = "OK";

        return strResult;
    }

    /**
     * get과 post를 동시에 http://okky.kr/article/228474
     */
    @RequestMapping(value = "/client/inicis/card-next-old", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_card_next_old(@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
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
                                   @RequestParam(value="P_AUTH_NO", required=false) final String P_AUTH_NO,
                                   HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {

//        req.setCharacterEncoding("euc-kr");
        /*String str2 = new String(req.getParameter("P_FN_CD1").getBytes("EUC-KR"));
        String decoded_result = new String(req.getParameter("P_FN_CD1").getBytes("euc-kr"), "UTF-8");

        String strUrlDecode = URLDecoder.decode(P_FN_CD1, "UTF-8");
        charSet(P_FN_CD1);

        //euc_kr_str - euc-kr 문자열
       *//* CharBuffer cbuffer = CharBuffer.wrap((new String(P_FN_CD1, "EUC-KR")).toCharArray());
        Charset utf8charset = Charset.forName("UTF-8");
        ByteBuffer bbuffer = utf8charset.encode(cbuffer);

        //변환된 UTF-8 문자열
        String tmpDecode = new String(bbuffer.array());*//*

        String[] a = new String[4];
        a[0] = "000000000조희경         555555555555555555555555555555555555555555555555555555555";
        a[1] = "111111111조희경         555555555555555555555555555555555555555555555555555555555";
        a[2] = P_FN_CD1;
        a[3] = req.getParameter("P_FN_CD1");
        Charset euckrcharset = Charset.forName("EUC-KR");
        Charset utf8charset = Charset.forName("UTF-8");

        for (int j = 0; j < a.length; j++) {
//            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"), 1, 50);
            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"));

            CharBuffer data = euckrcharset.decode(inputBuffer);
            ByteBuffer outputBuffer = utf8charset.encode(data);
            byte[] outputData = outputBuffer.array();
            System.out.println(new String(outputData));
        }*/

        String strResult = "card-next!!!!";

        StringBuilder strLog = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

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

        MimeMessage mail = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo("hyoseop@synapsetech.co.kr");
//        helper.setTo("hyoseop@synapsetech.co.kr");
//        helper.setReplyTo("hyoseop@gmail.com");
//        helper.setFrom("support@100min.co.kr");
        helper.setSubject("이니시스 결제 파라메터 확인 "+ dtNow);
        helper.setText(strLog.toString());

//        javaMailSender.send(mail);

//        이니시스 결제 파라메터 db에 입력
/*        MoInicisResult moInicisResult = new MoInicisResult();

        moInicisResult.setP_STATUS(P_STATUS);
        moInicisResult.setP_RMESG1(P_RMESG1);
        moInicisResult.setP_TID(P_TID);
        moInicisResult.setP_TYPE(P_TYPE);
        moInicisResult.setP_AUTH_DT(P_AUTH_DT);
        moInicisResult.setP_MID(P_MID);
        moInicisResult.setP_OID(P_OID);
        moInicisResult.setP_AMT(P_AMT);
        moInicisResult.setP_UNAME(P_UNAME);
        moInicisResult.setP_MNAME(P_MNAME);
        moInicisResult.setP_FN_CD1(P_FN_CD1);
        moInicisResult.setP_FN_NM(P_FN_NM);
        moInicisResult.setP_RMESG2(P_RMESG2);
        moInicisResult.setP_AUTH_NO(P_AUTH_NO);

        tbOrderService.inicis_result(moInicisResult);*/

        InicisResult inicisResult = new InicisResult();
        inicisResult.setPStatus(P_STATUS);
        inicisResult.setPRmesg1(P_RMESG1);
        inicisResult.setPTid(P_TID);
        inicisResult.setPType(P_TYPE);
        inicisResult.setPAuthDt(P_AUTH_DT);
        inicisResult.setPMid(P_MID);
        inicisResult.setPOid(P_OID);
        inicisResult.setPAmt(P_AMT);
        inicisResult.setPUname(P_UNAME);
        inicisResult.setPMname(P_MNAME);
        inicisResult.setPFnCd1(P_FN_CD1);
        inicisResult.setPFnNm(P_FN_NM);
        inicisResult.setPRmesg2(P_RMESG2);
        inicisResult.setPAuthNo(P_AUTH_NO);

//        해당 주문건을 결제 완료 처리및 100min 담당자에게 알림 메일 발송 필요
//        이니시스에서 실체 호출시 javax.persistence.RollbackException: Transaction marked as rollbackOnly 에러 발생하며 db에 결과가 저장되지 않음
//        tbOrderService.inicis_result(inicisResult);

//        strResult = "OK";

        return strResult;
    }

    /**
     * get과 post를 동시에 http://okky.kr/article/228474
     * 결재진행시 곧바로 아래의 파라메터만 리턴됨
     oid
     69
     vpresult
     00
     */
    @RequestMapping(value = "/client/inicis/card-return", method = {RequestMethod.GET, RequestMethod.POST})
    public String inicis_card_result(@RequestParam(value="P_STATUS", required=false) final String P_STATUS,
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
                                     @RequestParam(value="P_AUTH_NO", required=false) final String P_AUTH_NO,
                                     HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {

//        req.setCharacterEncoding("euc-kr");
        /*String str2 = new String(req.getParameter("P_FN_CD1").getBytes("EUC-KR"));
        String decoded_result = new String(req.getParameter("P_FN_CD1").getBytes("euc-kr"), "UTF-8");

        String strUrlDecode = URLDecoder.decode(P_FN_CD1, "UTF-8");
        charSet(P_FN_CD1);

        //euc_kr_str - euc-kr 문자열
       *//* CharBuffer cbuffer = CharBuffer.wrap((new String(P_FN_CD1, "EUC-KR")).toCharArray());
        Charset utf8charset = Charset.forName("UTF-8");
        ByteBuffer bbuffer = utf8charset.encode(cbuffer);

        //변환된 UTF-8 문자열
        String tmpDecode = new String(bbuffer.array());*//*

        String[] a = new String[4];
        a[0] = "000000000조희경         555555555555555555555555555555555555555555555555555555555";
        a[1] = "111111111조희경         555555555555555555555555555555555555555555555555555555555";
        a[2] = P_FN_CD1;
        a[3] = req.getParameter("P_FN_CD1");
        Charset euckrcharset = Charset.forName("EUC-KR");
        Charset utf8charset = Charset.forName("UTF-8");

        for (int j = 0; j < a.length; j++) {
//            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"), 1, 50);
            ByteBuffer inputBuffer = ByteBuffer.wrap(a[j].getBytes("EUC-KR"));

            CharBuffer data = euckrcharset.decode(inputBuffer);
            ByteBuffer outputBuffer = utf8charset.encode(data);
            byte[] outputData = outputBuffer.array();
            System.out.println(new String(outputData));
        }*/

        String strResult = "card-return!!!!";

        StringBuilder strLog = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

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

        log.debug("이니시스 card-return \n {}", strLog.toString());

        MimeMessage mail = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo("hyoseop@synapsetech.co.kr");
        helper.setTo("hyoseop@synapsetech.co.kr");
//        helper.setReplyTo("hyoseop@gmail.com");
//        helper.setFrom("support@100min.co.kr");
        helper.setSubject("이니시스 결제 파라메터 확인 "+ dtNow);
        helper.setText(strLog.toString());

//        javaMailSender.send(mail);

//        이니시스 결제 파라메터 db에 입력
/*        MoInicisResult moInicisResult = new MoInicisResult();

        moInicisResult.setP_STATUS(P_STATUS);
        moInicisResult.setP_RMESG1(P_RMESG1);
        moInicisResult.setP_TID(P_TID);
        moInicisResult.setP_TYPE(P_TYPE);
        moInicisResult.setP_AUTH_DT(P_AUTH_DT);
        moInicisResult.setP_MID(P_MID);
        moInicisResult.setP_OID(P_OID);
        moInicisResult.setP_AMT(P_AMT);
        moInicisResult.setP_UNAME(P_UNAME);
        moInicisResult.setP_MNAME(P_MNAME);
        moInicisResult.setP_FN_CD1(P_FN_CD1);
        moInicisResult.setP_FN_NM(P_FN_NM);
        moInicisResult.setP_RMESG2(P_RMESG2);
        moInicisResult.setP_AUTH_NO(P_AUTH_NO);

        tbOrderService.inicis_result(moInicisResult);*/

        InicisResult inicisResult = new InicisResult();
        inicisResult.setPStatus(P_STATUS);
        inicisResult.setPRmesg1(P_RMESG1);
        inicisResult.setPTid(P_TID);
        inicisResult.setPType(P_TYPE);
        inicisResult.setPAuthDt(P_AUTH_DT);
        inicisResult.setPMid(P_MID);
        inicisResult.setPOid(P_OID);
        inicisResult.setPAmt(P_AMT);
        inicisResult.setPUname(P_UNAME);
        inicisResult.setPMname(P_MNAME);
        inicisResult.setPFnCd1(P_FN_CD1);
        inicisResult.setPFnNm(P_FN_NM);
        inicisResult.setPRmesg2(P_RMESG2);
        inicisResult.setPAuthNo(P_AUTH_NO);

//        해당 주문건을 결제 완료 처리및 100min 담당자에게 알림 메일 발송 필요
//        이니시스에서 실체 호출시 javax.persistence.RollbackException: Transaction marked as rollbackOnly 에러 발생하며 db에 결과가 저장되지 않음
//        tbOrderService.inicis_result(inicisResult);

//        strResult = "OK";

        /*$script = '<script>';
        if ($ios) {
            $script .= "window.location = 'cleanbank:oid=".$oid."';";
        } else  {
            $script .= "window.location = 'cleanbank://result';";
        }
        $script .= '</script>';*/

        strResult = "<script>window.location = 'cleanbank://result';</script>";

        return strResult;
    }

    /**
     * 이니시스 승인결과 수신(POST)
     * @return
     */
/*    @RequestMapping(value = "/client/inicis-result", method = RequestMethod.POST)
    public String inicis_result(@RequestBody MoInicisResult moInicisResult) {

        return "OK";
    }*/

}
