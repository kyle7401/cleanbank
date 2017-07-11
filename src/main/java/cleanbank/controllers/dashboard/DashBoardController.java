package cleanbank.controllers.dashboard;

import cleanbank.Service.IDashBoardService;
import cleanbank.Service.ITbPromotionService;
import cleanbank.domain.TbPromotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import cleanbank.utils.SynapseCM;

/**
 * Created by hyoseop on 2015-12-14.
 */
@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDashBoardService dashBoardService;

    @Autowired
    private ITbPromotionService tbPromotionService;

    @Autowired
    private SynapseCM synapseCM;;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     누적현황 조건
     전주대비 금일 현황 : 전주 평균(전주의 총주문 건수/전주의 영업일수) 대비 금일 건수
     전월대비 금일 현황 : 전월 평균(전달의 총주문 건수/전달의 영업일수) 대비 금일 건수
     전주대비 금일 누적 현황 : 지난주 평균*(금주 지난 일수+1) 대비 금주 총 건수
     전월대비 금일 누적 현황 : 전월 평균*(금월 지난 일수+1) 대비 금월 총 건수
     ex) 10월 3일
     9월 평균 하루 10건이, 금월 현재까지 누적 주문 건수가 23건이라면,
     전월대비 금일 누적 현황 : 23/30(2+1) 76.6%

     *
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){

        String authority = synapseCM.getAuthorityString();
        log.debug("## 로그인 사용자 권한 = {}", authority);

//        공장 사용자 이외는 대시보드 볼수 있음
        if(authority.equals(SynapseCM.AuthorityType.ROLE_FACTORY.getAuthorityType())) {
            return "redirect:/";
        }

//        수거요청현황
        List<?> take_req_list = dashBoardService.getTakeReqList();
        model.addAttribute("take_req_list", take_req_list);

//        배송예정현황
        List<?> deli_req_list = dashBoardService.getDeliReqList();
        model.addAttribute("deli_req_list", deli_req_list);

//        프로모션 현황
        Iterable<TbPromotion> p_list = tbPromotionService.listAllTbPromotions();

        List<TbPromotion> promotion_list = StreamSupport.stream(p_list.spliterator(), false).collect(Collectors.toList());
        model.addAttribute("promotion_list", promotion_list);

//        전체현황
        Object cnt_by_status = dashBoardService.getCntByStatus();
        model.addAttribute("cnt_by_status", cnt_by_status);

        long todayCnt = 0;

        // 지난주 월, 일 구하기
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String From = null;
        String To = null;
        String Monday = null;

//        http://stackoverflow.com/questions/17210839/get-last-week-date-range-for-a-date-in-java
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
//        c.add(Calendar.DATE, -i - 7);
        c.add(Calendar.DATE, -i - 6);
        Date start = c.getTime();
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();
        System.out.println(start + " - " + end);

        From = df.format(start.getTime());
        To = df.format(end.getTime());
        log.debug("start = {} ~ end = {}", From, To);

        //        http://stackoverflow.com/questions/17210839/get-last-week-date-range-for-a-date-in-java
        // Get calendar set to current date and time
//        Calendar c = Calendar.getInstance();

// Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

// Print dates of the current week starting on Monday
        Monday = df.format(c.getTime());
        log.debug("이번주 월요일 = {}", Monday);

//        전주 대비 금일 현황
        try {
            Object[] status1 = dashBoardService.getStatus1(From, To);
            long tot1 = Long.valueOf(status1[0].toString());
            long cnt1 = Long.valueOf(status1[1].toString());
            long lStatus1 = tot1 / cnt1;
            model.addAttribute("lStatus1", lStatus1);

            todayCnt = Long.valueOf(status1[2].toString());
            model.addAttribute("todayCnt", todayCnt);

            double iPer1 = ((double) todayCnt / lStatus1 * 100);
            model.addAttribute("iPer1", (int) iPer1);
        } catch (Exception ex) {
            log.error("전주 대비 금일 계산 에러\n{}"+ ex.toString());
        }

//        전월 대비 금일 현황
        try {
            Object[] status2 = dashBoardService.getStatus2();
            long tot2 = Long.valueOf(status2[0].toString());
            long cnt2 = Long.valueOf(status2[1].toString());
            long lStatus2 = tot2 / cnt2;
            model.addAttribute("lStatus2", lStatus2);

            double iPer2 = ((double) todayCnt / lStatus2 * 100);
            model.addAttribute("iPer2", (int) iPer2);
        } catch (Exception ex) {
            log.error("전월 대비 금일 계산 에러\n{}"+ ex.toString());
        }

        //        전주 대비 금일 누적 현황
        try {
            Object[] status3 = dashBoardService.getStatus3(From, To, Monday);
            long cnt1 = Long.valueOf(status3[0].toString());
            long cnt2 = Long.valueOf(status3[1].toString());
            model.addAttribute("cnt3_1", cnt1);
            model.addAttribute("cnt3_2", cnt2);

            double iPer3 = ((double) cnt1 / cnt2 * 100);
            model.addAttribute("iPer3", (int) iPer3);
        } catch (Exception ex) {
            log.error("전주 대비 금일 누적 현황 계산 에러\n{}"+ ex.toString());
        }

//        전월 대비 금일 누적 현황
        try {
            Object[] status4 = dashBoardService.getStatus4();

            long cnt1 = Long.valueOf(status4[0].toString());
            int days1 = Integer.valueOf(status4[1].toString());
            long cnt2 = Long.valueOf(status4[2].toString());
            int days2 = Integer.valueOf(status4[3].toString());

//            전월 일 평균
            double lastMonth = ((double) cnt1 / days1 * 100);
            log.debug("전월 일평균 = {}", lastMonth);
            Integer lastMonthSum = Integer.parseInt(String.valueOf(Math.round(lastMonth))) * days2;
            log.debug("전월 누적평균 = {}", lastMonthSum );
            model.addAttribute("cnt4_1", lastMonthSum);

            model.addAttribute("cnt4_2", cnt2);

//            전월대비 금월 %
//            double iPer4 = ((double) cnt2 / lastMonthSum * 100);
            double iPer4 = 0;
            if(cnt2 > 0 && lastMonthSum > 0) iPer4 = ((double) cnt2 / lastMonthSum * 100);
            log.debug("전월대비 금월 = {}", iPer4 );
            model.addAttribute("iPer4", (int) iPer4);

            /*model.addAttribute("cnt3_1", cnt1);
            model.addAttribute("cnt3_2", cnt2);*/
        } catch (Exception ex) {
            log.error("전월 대비 금일 누적 현황 계산 에러\n{}"+ ex.toString());
        }

        return "dashboard/dashboard";
    }
}