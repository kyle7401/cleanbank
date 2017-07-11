package cleanbank.controllers.order;

import cleanbank.Service.IOrderListService;
import cleanbank.Service.ITbBranchService;
import cleanbank.Service.ITbCodeService;
import cleanbank.utils.ExcelView;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-09.
 */
@Controller
@RequestMapping("/order")
public class OrderListController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbBranchService tbBranchService;

    @Autowired
    private ITbCodeService tbCodeService;

    @Autowired
    IOrderListService orderlistservice;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 주문조회 – 주문전체 현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/total/list", method = RequestMethod.GET)
    public String list(Model model){

        //        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

//        오늘및 주및 월 from
        model.addAttribute("dt_today", SynapseCM.getToday());
        model.addAttribute("week_from", SynapseCM.getWeekFrom());
        model.addAttribute("month_from", SynapseCM.getMonthFrom());

        return "order/total_list";
    }

    /**
     * 주문조회 – 주문전체 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/total/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value="bncd", required=false) final String bncd,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to,
                      @RequestParam(value="keyword", required=false) final String keyword,
                      @RequestParam(value="condition", required=false) final String condition,
                      @RequestParam(value="status", required=false) final String status
    ){
        /*List<TbMember> employees = (List<TbMember>) tbMemberService.listAllMembers();
        model.addAttribute("list", employees);*/

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

//        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        List<?> list = orderlistservice.getTotalList5(bncd, from, to, keyword, condition, status, null, null);

        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/orderlist_tot_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "주문전체 현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 주문조회 – 주문접수현황 현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/receive/list", method = RequestMethod.GET)
    public String receiveList(Model model){

        //        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

//        오늘및 주및 월 from
        model.addAttribute("dt_today", SynapseCM.getToday());
        model.addAttribute("week_from", SynapseCM.getWeekFrom());
        model.addAttribute("month_from", SynapseCM.getMonthFrom());

        return "order/receive_list";
    }

    /**
     * 주문조회 – 주문전체 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/receive/excel", method = RequestMethod.GET)
    public View receiveExcel(Model model,
                      @RequestParam(value="bncd", required=false) final String bncd,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to,
                      @RequestParam(value="keyword", required=false) final String keyword,
                      @RequestParam(value="condition", required=false) final String condition,
                      @RequestParam(value="status", required=false) final String status
    ){
        /*List<TbMember> employees = (List<TbMember>) tbMemberService.listAllMembers();
        model.addAttribute("list", employees);*/

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/orderlist_receive_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "주문접수 현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 주문조회 – 접수완료 현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/finish/list", method = RequestMethod.GET)
    public String finishList(Model model){

        //        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

//        오늘및 주및 월 from
        model.addAttribute("dt_today", SynapseCM.getToday());
        model.addAttribute("week_from", SynapseCM.getWeekFrom());
        model.addAttribute("month_from", SynapseCM.getMonthFrom());

        return "order/finish_list";
    }

    /**
     * 주문조회 – 접수완료 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/finish/excel", method = RequestMethod.GET)
    public View finishExcel(Model model,
                             @RequestParam(value="bncd", required=false) final String bncd,
                             @RequestParam(value="from", required=false) final String from,
                             @RequestParam(value="to", required=false) final String to,
                             @RequestParam(value="keyword", required=false) final String keyword,
                             @RequestParam(value="condition", required=false) final String condition,
                             @RequestParam(value="status", required=false) final String status
    ){
        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/orderlist_finish_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "접수완료 현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 주문조회 – 빨래통입고예정현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/plan/list", method = RequestMethod.GET)
    public String planList(Model model){

        //        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

//        오늘및 주및 월 from
        model.addAttribute("dt_today", SynapseCM.getToday());
        model.addAttribute("week_from", SynapseCM.getWeekFrom());
        model.addAttribute("month_from", SynapseCM.getMonthFrom());

        return "order/plan_list";
    }

    /**
     * 주문조회 – 빨래통입고 예정 현황 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/plan/excel", method = RequestMethod.GET)
    public View planExcel(Model model,
                            @RequestParam(value="bncd", required=false) final String bncd,
                            @RequestParam(value="from", required=false) final String from,
                            @RequestParam(value="to", required=false) final String to,
                            @RequestParam(value="keyword", required=false) final String keyword,
                            @RequestParam(value="condition", required=false) final String condition,
                            @RequestParam(value="status", required=false) final String status
    ){
        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/orderlist_plan_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "빨래통입고예정 현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 주문조회 – 빨래통입고 현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/stock/list", method = RequestMethod.GET)
    public String stockList(Model model){

        //        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

//        오늘및 주및 월 from
        model.addAttribute("dt_today", SynapseCM.getToday());
        model.addAttribute("week_from", SynapseCM.getWeekFrom());
        model.addAttribute("month_from", SynapseCM.getMonthFrom());

        return "order/stock_list";
    }

    /**
     * 주문조회 – 빨래통입고 현황 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/stock/excel", method = RequestMethod.GET)
    public View stockExcel(Model model,
                          @RequestParam(value="bncd", required=false) final String bncd,
                          @RequestParam(value="from", required=false) final String from,
                          @RequestParam(value="to", required=false) final String to,
                          @RequestParam(value="keyword", required=false) final String keyword,
                          @RequestParam(value="condition", required=false) final String condition,
                          @RequestParam(value="status", required=false) final String status
    ){
        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/orderlist_stock_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "빨래통입고 현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }
}