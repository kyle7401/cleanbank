package cleanbank.controllers.take;

import cleanbank.Service.*;
import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.domain.TbProduct;
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
@RequestMapping("/take")
public class TakeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbBranchService tbBranchService;

    @Autowired
    private ITbCodeService tbCodeService;

    @Autowired
    IOrderListService orderlistservice;

    @Autowired
    private ITbProductService tbProductService;

    @Autowired
    private ITbEmployeeService tbEmployeeService;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 수거 – 주문요청생성
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
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

        return "take/order_list";
    }

    /**
     * 수거 – 주문요청생성 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/order/excel", method = RequestMethod.GET)
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

//        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY a.OR_REQ_DT ASC");

        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/take_order_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "주문요청생성"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 수거 – 코디배정
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/assign/list", method = RequestMethod.GET)
    public String assignList(Model model){

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

        return "take/assign_list";
    }

    /**
     * 수거 – 코디배정 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/assign/excel", method = RequestMethod.GET)
    public View assignExcel(Model model,
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

        model.addAttribute("in_fname", "excel/take_assign_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "코디배정"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 수거 – 가용코디현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/extra/list", method = RequestMethod.GET)
    public String extraList(Model model){

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

        return "take/extra_list";
    }

    /**
     * 수거 – 가용코디현황 엑셀
     * @param model
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/extra/excel", method = RequestMethod.GET)
    public View extraExcel(Model model,
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

        model.addAttribute("in_fname", "excel/take_extra_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "가용코디현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/order/new")
    public String newLoc(Model model){

        // 뒤로가기시 url
        model.addAttribute("back_url", "/take/order/list");

//        품목 정보
        TbOrderItems orderitems = new TbOrderItems();
        orderitems.setItCnt(1);
        model.addAttribute("orderitems", orderitems);

//        품목 정보 -> 품목명 상위
        List<TbProduct> pdLvl1s = tbProductService.getPdLvl1s();
        model.addAttribute("pdLvl1s", pdLvl1s);

//        코디
        List<?> employee_cds = tbEmployeeService.getEmpCds();
        model.addAttribute("employee_cds", employee_cds);

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

        TbOrder order = new TbOrder();

        order.setMode("new");
        order.setOrChannel("3"); // 전화
//        order.setOrOrderDt(new Date());

        model.addAttribute("order", order);
        return "accept/order_form";
    }

}