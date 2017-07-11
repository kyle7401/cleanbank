package cleanbank.controllers;

import cleanbank.Service.GcmService;
import cleanbank.Service.OrderListService;
import cleanbank.Service.TbBranchLocsService;
import cleanbank.domain.*;
import cleanbank.repositories.TbBranchRepository;
import cleanbank.repositories.TbPartnerRepository;
import cleanbank.utils.CharSetTest;
import cleanbank.utils.ExcelView;
import cleanbank.viewmodel.MoTimeTable;
import cleanbank.viewmodel.PartnerSearch;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.model.GcmPush;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hyoseop on 2015-11-05.
 */
@Api(value = "test1Controller", description = "swagger test1",basePath = "/")
@RestController
public class test1Controller {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

/*    @Autowired
    UserRepository customerRepo;*/

//    http://blog.kaczmarzyk.net/2014/03/23/alternative-api-for-filtering-data-with-spring-mvc-and-spring-data/
/*    @RequestMapping(value = "/s1", params = {"username"})
    @ResponseBody
    public Iterable<User> filterCustomersByFirstName(
            @Spec(path = "username", spec = Like.class) Specification<User> spec) {

        return customerRepo.findAll(spec);
    }*/

/*    @RequestMapping(value = "", params = { "registeredBefore" })
    @ResponseBody
    public Iterable<User> findCustomersRegisteredBefore(
            @Spec(path = "registrationDate", params = "registeredBefore", config = "yyyy-MM-dd", spec = DateBefore.class) Specification<User> spec) {

        return customerRepo.findAll(spec);
    }*/

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TbPartnerRepository tbPartnerRepository;

//    @Autowired
@Resource
private GcmPush gcmPush;

/*    public GcmPush gcmPush() {
        GcmPushImpl pushImpl = new GcmPushImpl();
        return pushImpl;
    }*/

    //    ----------------------------------------------------------------------------------------------------------------

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private GcmService gcmService;

    @Autowired
    private TbBranchLocsService tbBranchLocsService;

    @Autowired
    private TbBranchRepository tbBranchRepository;

    @Autowired
    private OrderListService orderListService;

    @RequestMapping(value = "/test/getTimeTable2", method = RequestMethod.GET)
    public ResponseEntity<?> getTimeTable2(@RequestParam(value="mbAddr1", required=false) final String mbAddr1) throws Exception, ParseException {
        /*List<TbBranch> tbBranchs = tbBranchRepository.getBranchList();
        TbBranch tbBranch = tbBranchs.get(0);*/
        TbBranch tbBranch = tbBranchRepository.findOne(4);

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

        int iDays = startCal.get(Calendar.DAY_OF_WEEK) - startCal.getFirstDayOfWeek();*/

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

        List<TbOrder> list = orderListService.getOrderList();
        log.debug("list.size() = "+ list.size());

        for(int i=0; i< 24; i++) {
            endTmCal.setTime(startTmCal.getTime());
            endTmCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?
            String strTitle = df3.format(startTmCal.getTime()) + "~" + df3.format(endTmCal.getTime());

            int compare = endCal.getTime().compareTo(endTmCal.getTime());
            if(compare < 0) break;

            MoTimeTable.time_table t_table = new MoTimeTable.time_table();
            t_table.setTitle(strTitle);

//            시간
            List<MoTimeTable.time_table.col> cols = new ArrayList<MoTimeTable.time_table.col>();

            tmpCal.setTime(startCal.getTime());

            for (int j = 0; j < iDays; j++) {
                MoTimeTable.time_table.col col = new MoTimeTable.time_table.col();

                String strStartTime = df4.format(tmpCal.getTime()) + df5.format(startTmCal.getTime());
                col.setStart_time(strStartTime);

                String strEndTime = df4.format(tmpCal.getTime()) + df5.format(endTmCal.getTime());
                col.setEnd_time(strEndTime);

//                +1 시간 미리 마감 처리
                calNextHr.setTime(dtNow);
                calNextHr.add(Calendar.HOUR, 1);

                int compare2 = calNextHr.getTime().compareTo(df6.parse(strStartTime));

//                System.out.println(df2.format(calNextHr.getTime()) +" @ "+ df2.format(df6.parse(strStartTime)) +" @ "+ compare2);

                if (compare2 > 0) {
                    col.setState("N");
                } else {
                    col.setState("Y");

                    //        해당 수거시간의 주문건수
                    Date tmpStart = df6.parse(strStartTime);
                    Date tmpEnd = df6.parse(strEndTime);

                    List<TbOrder> filterList = StreamSupport.stream(list.spliterator(), false)
                            .filter(t -> (t.getOrReqDt().getTime() >= tmpStart.getTime() && t.getOrReqDt().before(tmpEnd)))
                            .collect(Collectors.toList());

                    if(filterList.size() > 0) {
                        log.debug("filterList.size() = " + filterList.size());
                    }
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

    @RequestMapping(value = "/test/getTimeTable", method = RequestMethod.GET)
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
            StartTime = tbBranch.getbnCloseTm0();
        }

//        수거시간간격
        Integer iTimeSpan = 30;
        if(!StringUtils.isEmpty(tbBranch.getBnTransTm())) {
            iTimeSpan = tbBranch.getBnTransTm();
        }

//        날짜 목록
        DateFormat df1 = new SimpleDateFormat("MM/dd");
        DateFormat df2 = new SimpleDateFormat("E", Locale.KOREA);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

//        오늘
        Date start = c.getTime();

//        토요일 : 추후 해당 지점의 영업 종료일로 처리해야 함
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // 7
//        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 1
        Date end = c.getTime();

        int iDays = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        log.debug("{} - {}", start, end);

        MoTimeTable table = new MoTimeTable();

        List<MoTimeTable.tt_header> headers = new ArrayList<MoTimeTable.tt_header>();
        c.setTime(start);

        for(int i=0; i<iDays; i++) {
//            일단 비영업일도 채워서 보여주자
            /*String workYn = "N";

            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1: // 일
                    workYn = tbBranch.getBnSun();
                    break;

                case 2: // 월
                    workYn = tbBranch.getBnMon();
                    break;

                case 3: // 화
                    workYn = tbBranch.getBnTue();
                    break;

                case 4: // 수
                    workYn = tbBranch.getBnWed();
                    break;

                case 5: // 목
                    workYn = tbBranch.getBnThu();
                    break;

                case 6: // 금
                    workYn = tbBranch.getBnFri();
                    break;

                case 7: // 토
                    workYn = tbBranch.getBnSat();
                    break;
            }

            if("Y".equals(workYn)) {*/
                MoTimeTable.tt_header h = new MoTimeTable.tt_header();
                h.setDate(df1.format(c.getTime()));
                h.setDay_of_week(df2.format(c.getTime()));
                headers.add(h);
//            }

            c.add(Calendar.DATE, 1);
        }

        table.setTime_table_header(headers);

//        시간목록
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
                String workYn = "N";

                switch (c.get(Calendar.DAY_OF_WEEK)) {
                    case 1: // 일
                        workYn = tbBranch.getBnSun();
                        break;

                    case 2: // 월
                        workYn = tbBranch.getBnMon();
                        break;

                    case 3: // 화
                        workYn = tbBranch.getBnTue();
                        break;

                    case 4: // 수
                        workYn = tbBranch.getBnWed();
                        break;

                    case 5: // 목
                        workYn = tbBranch.getBnThu();
                        break;

                    case 6: // 금
                        workYn = tbBranch.getBnFri();
                        break;

                    case 7: // 토
                        workYn = tbBranch.getBnSat();
                        break;
                }

//                if("Y".equals(workYn)) {
                    MoTimeTable.time_table.col col = new MoTimeTable.time_table.col();

                    String strStartTime = df4.format(c.getTime()) + df5.format(cal.getTime());
                    col.setStart_time(strStartTime);

                    tmpCal.setTime(cal.getTime());
                    tmpCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?

                    String strEndTime = df4.format(c.getTime()) + df5.format(tmpCal.getTime());
                    col.setEnd_time(strEndTime);

                if("Y".equals(workYn)) {

//                마감시간 지났는지 확인
//                int compare = start.compareTo(df6.parse(strEndTime));

//                +1 시간 미리 마감 처리
                    Calendar calNextHr = Calendar.getInstance();
                    calNextHr.setTime(start);
                    calNextHr.add(Calendar.HOUR, 1);

//                int compare = calNextHr.getTime().compareTo(df6.parse(strEndTime));
                    int compare = calNextHr.getTime().compareTo(df6.parse(strStartTime));

                    if (compare > 0) {
                        col.setState("N");
                    } else {
                        col.setState("Y");
                    }
                } else {
                    col.setState("N");
                }

//                    c.add(Calendar.DATE, 1);
                    cols.add(col);
//                }

                c.add(Calendar.DATE, 1);
            }

            t_table.setCols(cols);
            tables.add(t_table);

            cal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?
        }

        table.setTime_table_list(tables);

        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/t", method = RequestMethod.GET)
    public ResponseEntity<?> t() throws IOException, GcmMulticastLimitExceededException {

        CharSetTest.test();

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/gcmtest2", method = RequestMethod.GET)
    public ResponseEntity<?> gcmtest2() throws IOException, GcmMulticastLimitExceededException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String content = "Gcm 테스트. " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + seconds;

        GcmMulticatResult result = gcmService.sendPushMsg("cleanbank.gcm 테스트 제목3!", content, "ebsW7AmXBS4:APA91bF0mcR5KAmH64pRgN2eHPL6g4h4MIDRLn_bn51g6lAwHUmyUyPtBtIcP587bFFJ0vN62gHtcR7k_PCrDkytTcAYVn4Dw6W7FkiUG7fcg6BoynwjizkfRkOq4DkhA9sTMQv9E4KM");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/gcmtest", method = RequestMethod.GET)
    public ResponseEntity<?> gcmtest() throws IOException, GcmMulticastLimitExceededException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String content = "Gcm 테스트. " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + seconds;

//        gcmPush = new gcmPush();
        GcmPushInfo info = new GcmPushInfo();

        info.setTitle("cleanbank.gcm 테스트 제목2!");
        info.setData(content);

        List<String> regIdList = new ArrayList<String>();
        regIdList.add("ebsW7AmXBS4:APA91bF0mcR5KAmH64pRgN2eHPL6g4h4MIDRLn_bn51g6lAwHUmyUyPtBtIcP587bFFJ0vN62gHtcR7k_PCrDkytTcAYVn4Dw6W7FkiUG7fcg6BoynwjizkfRkOq4DkhA9sTMQv9E4KM");
        info.setRegIdList(regIdList);

        GcmMulticatResult result = gcmPush.sendPush(info);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/sendMail2", method = RequestMethod.GET)
    public ResponseEntity<?> sendMail2() throws MessagingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

        MimeMessage mail = javaMailSender.createMimeMessage();

//        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo("hyoseop@gmail.com");
            helper.setReplyTo("hyoseop@gmail.com");
            helper.setFrom("support@100min.co.kr");
            helper.setSubject("메일 테스트 "+ dtNow);
            helper.setText("메일 테스트 내용");

            javaMailSender.send(mail);
/*        } catch (Exception e) {
            e.printStackTrace();
        } finally {}*/

        return new ResponseEntity<>(dtNow, HttpStatus.OK);
    }

    /**
     * https://blog.outsider.ne.kr/1051
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    @RequestMapping(value = "/test/sendMail", method = RequestMethod.GET)
    public ResponseEntity<?> sendMail() throws IOException, GcmMulticastLimitExceededException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dtNow = dateFormat.format(new Date());

        // 템플릿 메시지를 스레드 세이프하게 "복사본"을 생성하고 커스터마이징한다
//        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo("hyoseop@gmail.com");
        msg.setText("메엘 테스트");
        try{
//            this.mailSender.send(msg);

//            / 당연히 실제 프로젝트에서는 DI를 사용할 것이다
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
//            sender.setHost();
            sender.send(msg);
        }
//        catch(MailException ex) {
        catch(Exception ex) {
            // 로깅 등...
            System.err.println(ex.getMessage());
        }

        return new ResponseEntity<>(dtNow, HttpStatus.OK);
    }

    @RequestMapping("/picture/{id}")
    public ResponseEntity<byte[]> getArticleImage(@PathVariable String id) throws IOException {

        Path path = Paths.get("d:/1080p-1.jpg");
        String contentType = Files.probeContentType(path);
        FileSystemResource file = new FileSystemResource("d:/1080p-1.jpg");

        /*return ResponseEntity
                .ok()
                .contentLength(file.contentLength())
                .contentType(
                        MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(file.getInputStream()));*/

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(contentType));
        return new ResponseEntity(new InputStreamResource(file.getInputStream()),
                httpHeaders, HttpStatus.OK);

        // read it
//        CrmService.ProfilePhoto profilePhoto = crmService.readUserProfilePhoto (user);

//        if (null != profilePhoto){
        // send it back to the client
/*            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(profilePhoto.getMediaType());
            return new ResponseEntity&lt;byte[]&gt;(profilePhoto.getPhoto(),
                    httpHeaders, HttpStatus.OK);*/
//        }

//        throw new UserProfilePhotoReadException(user);

        // 1. download img from http://internal-picture-db/id.jpg ...
        /*byte[] image = ...

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);

        return new ResponseEntity<byte[]>(image, headers);*/
    }

    /**
     * 수거 시간표 테스트
     * @return
     */
    @RequestMapping(value = "/t/l2", method = RequestMethod.GET)
    public ResponseEntity<?> getTimeTable2() throws ParseException {
//        날짜 목록
        DateFormat df1 = new SimpleDateFormat("MM/dd");
        DateFormat df2 = new SimpleDateFormat("E");

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

        String StartTime = "10:00";
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

                col.setStart_time(df4.format(c.getTime()) + df5.format(cal.getTime()));

                tmpCal.setTime(cal.getTime());
                tmpCal.add(Calendar.MINUTE, iTimeSpan); // 수거시간 간격?

                col.setEnd_time(df4.format(c.getTime()) + df5.format(tmpCal.getTime()));
                col.setState("Y");

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
     * 수거 시간표 테스트
     * @return
     */
    @RequestMapping(value = "/t/l", method = RequestMethod.GET)
    public ResponseEntity<?> getTimeTable1(){

        MoTimeTable table = new MoTimeTable();

        List<MoTimeTable.tt_header> headers = new ArrayList<MoTimeTable.tt_header>();

        MoTimeTable.tt_header h = new MoTimeTable.tt_header();
        h.setDate("01/05");
        h.setDay_of_week("화");
        headers.add(h);

        h = new MoTimeTable.tt_header();
        h.setDate("01/06");

        h.setDay_of_week("수");
        headers.add(h);

        table.setTime_table_header(headers);

        return new ResponseEntity<>(table, HttpStatus.OK);
    }


    @RequestMapping(value = "/file/upload0", method = RequestMethod.POST)
    public void upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        log.debug("uploadPost called");
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;

        while (itr.hasNext()) {
            /*mpf = request.getFile(itr.next());
            log.debug("Uploading {}", mpf.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;
            String storageDirectory = ord_items_img_dir;
            String contentType = mpf.getContentType();

            File newFile = new File(storageDirectory + newFilename);*/
            /*try {
                mpf.transferTo(newFile);

                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);
                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                ImageIO.write(thumbnail, "png", thumbnailFile);*/

/*                Image image = new Image();
                image.setName(mpf.getOriginalFilename());
                image.setThumbnailFilename(thumbnailFilename);
                image.setNewFilename(newFilename);
                image.setContentType(contentType);
                image.setSize(mpf.getSize());
                image.setThumbnailSize(thumbnailFile.length());
                image = imageDao.create(image);

                image.setUrl("/picture/"+image.getId());
                image.setThumbnailUrl("/thumbnail/"+image.getId());
                image.setDeleteUrl("/delete/"+image.getId());
                image.setDeleteType("DELETE");

                list.add(image);*/

            /*} catch(IOException e) {
                log.error("Could not upload file "+mpf.getOriginalFilename(), e);
            }*/
        }
    }

    /*@ApiOperation(value = "swagger GET 테스트", notes = "swagger 테스트 설명")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success request")
    })*/
    @ApiOperation(value = "swagger GET 테스트")
    @RequestMapping(value = "/plist2", method = RequestMethod.GET)
    public Iterable<TbPartner> filterList2(final PartnerSearch partnerSearch) {
        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

        BooleanBuilder builder = new BooleanBuilder();

//        공장명
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtNm())) {
            builder.and(tbPartner.ptNm.contains(partnerSearch.getPtNm()));
        }

//        전화번호
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtTel())) {
            builder.and(tbPartner.ptTel.contains(partnerSearch.getPtTel()));
        }

//        담당자 정보
        if (org.springframework.util.StringUtils.hasText(partnerSearch.getPmKeyWord())) {
            builder.and(tbPartner.ptCd.in(
                    new JPASubQuery().from(tbPartMember)
                            .where(tbPartMember.pmNm.contains(partnerSearch.getPmKeyWord())
                                    .or(tbPartMember.pmEmail.contains(partnerSearch.getPmKeyWord()))
                                    .or(tbPartMember.pmTel.contains(partnerSearch.getPmKeyWord()))
                            )
                            .list(tbPartMember.ptCd)
            ));
        }

        JPAQuery query = new JPAQuery(entityManager);

        Iterable<TbPartner> list = query.from(tbPartner)
                .where(builder)
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner);

//        log.debug("plist2");

        return list;
    }

    @RequestMapping(value = "/plist")
    public Iterable<TbPartner> filterList(
            @And({@Spec(path = "ptNm", spec = Like.class), @Spec(path = "ptTel", spec = Like.class)}) Specification<TbPartner> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPartner> spec2,
            @PageableDefault(sort = {"ptNm", "ptCd"}, size = 1000) Pageable pageable) {

        Specification<TbPartner> spec = Specifications.where(spec1).and(spec2);

//        Iterable<TbPartner> list = tbPartnerRepository.findAll(spec, pageable);

        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

//        BooleanBuilder builder = new BooleanBuilder();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TbPartner> q = cb.createQuery(TbPartner.class);
        Root<TbPartner> order = q.from(TbPartner.class);
//        spec.toPredicate(order, q, cb)

        JPAQuery query = new JPAQuery(entityManager);

        Iterable<TbPartner> list = query.from(tbPartner)
//                .where(builder)
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner);

        return list;
    }

    @RequestMapping("/test4")
    public String test4() {

        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

        BooleanBuilder builder = new BooleanBuilder();

/*        builder.and(tbPartner.ptCd.in(
                new JPASubQuery().from(tbPartMember)
                        .where(tbPartMember.pmNm.contains("효섭"))
                        .list(tbPartMember.ptCd)
        ));*/

//
        JPAQuery query = new JPAQuery(entityManager);

        List<TbPartner> partners = query.from(tbPartner)
                .where(builder)
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner);

        partners.forEach(item->log.debug("공장명 = {}", item.getPtNm()));

        return "OK4";
    }

    @RequestMapping("/test3")
    public String test3() {

        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

        JPAQuery query = new JPAQuery(entityManager);

        List<TbPartner> partners = query.from(tbPartner)
                .where(tbPartner.ptCd.in(
                        new JPASubQuery().from(tbPartMember)
                                .where(tbPartMember.pmNm.contains("효섭"))
                                .list(tbPartMember.ptCd)
                ))
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner);

        partners.forEach(item->log.debug("공장명 = {}", item.getPtNm()));

        return "OK3";
    }

    @RequestMapping("/test2")
    public String test2() {

        QTbPartner tbPartner = QTbPartner.tbPartner;
        JPAQuery query = new JPAQuery(entityManager);

        List<Integer> partners = query.from(tbPartner)
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner.ptCd);

        partners.forEach(item->log.debug("공장코드 = {}", item));

        return "OK2";
    }


    @RequestMapping("/test1")
    public String test1() {

/*        List<String> uniqueUserNames = new JPAQuery(entityManager).from(tbPartner)
                .where(user.name.like("%ov"))
                .groupBy(user.name)
                .list(user.name);*/

        JPAQuery query = new JPAQuery(entityManager);
        QTbPartner qTbPartner = new QTbPartner("p");

        List<TbPartner> partners = query.from(qTbPartner)
                .orderBy(qTbPartner.ptNm.asc())
                .list(qTbPartner);

//        http://www.mkyong.com/java8/java-8-foreach-examples/
//        partners.forEach(System.out::println);
        partners.forEach(item->log.debug("공장명 = {}", item.getPtNm()));

        return "OK1";
    }

    //    http://syaku.tistory.com/301
    // 데이터를 엑셀로 추출하여 프론트엔드에 전달한다.
    @RequestMapping(value = "/excel3", method = RequestMethod.GET)
    public View viewExcel3(Model model) throws ParseException {
/*
        // 임의의 데이터를 만듬.
        List<String> listData = new ArrayList<String>();
        listData.add("홍길동");
        listData.add("나그네");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");

        // 차트를 만들기 위한 통계자료도 구한다.
        List<Map<String, Object>> listStat = new ArrayList<Map<String, Object>>();

        Map<String, Object> mapStat = new HashMap<String, Object>();
        mapStat.put("name", "홍길동");
        mapStat.put("count", 6);
        listStat.add(mapStat);

        mapStat = new HashMap<String, Object>();
        mapStat.put("name", "나그네");
        mapStat.put("count", 1);
        listStat.add(mapStat);

        // 데이터를 담는 다.
        model.addAttribute("data", listData);
        model.addAttribute("stat", listStat);
*/
        /*List<Employee> employees = generateSampleEmployeeData();
        model.addAttribute("list", employees);

        model.addAttribute("in_fname", "excel/codi_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "코디목록"); // 다운로드 받을 엑셀 파일명*/

        // 엑셀을 출력한다.
        return new ExcelView();
    }

/*    public static List<Employee> generateSampleEmployeeData() throws ParseException {
        List<Employee> employees = new ArrayList<Employee>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        employees.add( new Employee("Elsa", dateFormat.parse("1970-Jul-10"), 1500, 0.15) );
        employees.add( new Employee("Oleg", dateFormat.parse("1973-Apr-30"), 2300, 0.25) );
        employees.add( new Employee("Neil", dateFormat.parse("1975-Oct-05"), 2500, 0.00) );
        employees.add( new Employee("Maria", dateFormat.parse("1978-Jan-07"), 1700, 0.15) );
        employees.add( new Employee("John", dateFormat.parse("1969-May-30"), 2800, 0.20) );
        return employees;
    }*/

    /*@RequestMapping(value = "/excel2", method = RequestMethod.GET)
    public String excel2(Model model) throws ParseException {
//        String strP = new ClassPathResource("config.properties").getPath();
        List<Employee> employees = generateSampleEmployeeData();

//        try(InputStream is = this.getClass().getResourceAsStream("object_collection_template.xls")) {
        try(InputStream is = new ClassPathResource("excel/object_collection_template.xls").getInputStream()) {

            if(is == null) {
                log.debug("InputStream null !!!");
            } else {
                try (OutputStream os = new FileOutputStream("object_collection_output.xls")) {
                    Context context = new Context();
                    context.putVar("employees", employees);
                    JxlsHelper.getInstance().processTemplate(is, os, context);
                } catch (Exception ex) {
                    log.debug("엑셀 에러1 {}", ex.toString());
                }
            }
        } catch (Exception ex) {
            log.debug("엑셀 에러2 {}", ex.toString());
        }
        log.debug("\n\n\n############### 테스트 끝");
        return "a";
    }*/

    // 데이터를 엑셀로 추출하여 프론트엔드에 전달한다.
    @RequestMapping(value = "/excel1", method = RequestMethod.GET)
    public View viewExcel(Model model) {

        // 임의의 데이터를 만듬.
        List<String> listData = new ArrayList<String>();
        listData.add("홍길동");
        listData.add("나그네");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");
        listData.add("홍길동");

        // 차트를 만들기 위한 통계자료도 구한다.
        List<Map<String, Object>> listStat = new ArrayList<Map<String, Object>>();

        Map<String, Object> mapStat = new HashMap<String, Object>();
        mapStat.put("name", "홍길동");
        mapStat.put("count", 6);
        listStat.add(mapStat);

        mapStat = new HashMap<String, Object>();
        mapStat.put("name", "나그네");
        mapStat.put("count", 1);
        listStat.add(mapStat);

        // 데이터를 담는 다.
        model.addAttribute("data", listData);
        model.addAttribute("stat", listStat);

        // 엑셀을 출력한다.
        return new ExcelView();
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /*@RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }*/

    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload_old", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {

//            폴더가 없으면 만들자
//            http://stackoverflow.com/questions/14666170/create-intermediate-folders-if-one-doesnt-exist
            File f = new File("/Temp/upload/employee/");
            f.mkdirs();
//            f.mkdir();

            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}


