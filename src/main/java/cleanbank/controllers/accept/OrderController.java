package cleanbank.controllers.accept;

import cleanbank.Service.*;
import cleanbank.domain.TbMember;
import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.domain.TbProduct;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.utils.ExcelView;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-19.
 */
@Controller
@RequestMapping("/accept/order")
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar2;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar2.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
    @Autowired
    private ITbOrderService tbOrderService;

   /* @Autowired
    public void setTbOrderService(TbOrderService tbOrderService) {
        this.tbOrderService = tbOrderService;
    }*/

    private TbBranchService tbBranchService;

    @Autowired
    public void setTbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

    private TbEmployeeService tbEmployeeService;

    @Autowired
    public void setTbEmployeeService(TbEmployeeService tbEmployeeService) {
        this.tbEmployeeService = tbEmployeeService;
    }

    private TbMemberService tbMemberService;

    @Autowired
    public void setTbMemberService(TbMemberService tbMemberService) {
        this.tbMemberService = tbMemberService;
    }

    private TbProductService tbProductService;

    @Autowired
    public void setTbProductService(TbProductService tbProductService) {
        this.tbProductService = tbProductService;
    }

    private TbAddressService tbAddressService;

    @Autowired
    public void setTbAddressService(TbAddressService tbAddressService) {
        this.tbAddressService = tbAddressService;
    }

    @Autowired
    private TbCodeService tbCodeService;
    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 고객주소 추가/수정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/member/{id}")
    public String showManager(@PathVariable Integer id, Model model){
//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        //        서비스 상태 콤보
        List<?> cmb_service = tbCodeService.getCdIts06();
        model.addAttribute("cmb_service", cmb_service);

        TbMember member = tbMemberService.getTbMemberById(id);
        member.setMode("edit");

        model.addAttribute("member", member);
        return "manage/member/member_form2";
    }

    /**
     * 목록
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
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

        return "accept/order_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){

        // 뒤로가기시 url
        model.addAttribute("back_url", "/accept/order/list");

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
        model.addAttribute("order", order);

        model.addAttribute("bnBarCd", "0");

        return "accept/order_form";
    }

    /**
     * 접수 - 입고처리 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view1/{id}")
    public String showManager1(@PathVariable Long id, Model model){
        return view(id, "/accept/stock/list", "accept/order_view_factory", model);
    }

    /**
     * 공장 - 입고 현황 파악 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view2/{id}")
    public String showManager2(@PathVariable Long id, Model model){
        return view(id, "/factory/stock/list", "accept/order_view_factory", model);
    }

    /**
     * 공장 - 공장처리변경 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view3/{id}")
    public String showManager3(@PathVariable Long id, Model model){
        return view(id, "/factory/process/list", "accept/order_view_factory", model);
    }

    /**
     * 공장 - 출고처리 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view4/{id}")
    public String showManager4(@PathVariable Long id, Model model){
        return view(id, "/factory/delivery/list", "accept/order_view_factory", model);
    }

    /**
     * 배송 - 공장 출고 확인 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view5/{id}")
    public String showManager5(@PathVariable Long id, Model model){
        return view(id, "/delivery/list", "accept/order_view_factory", model);
    }

    /**
     * 주문조회 - 주문전체 현황 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view6/{id}")
    public String showManager6(@PathVariable Long id, Model model){
        return view(id, "/order/total/list", "accept/order_view", model);
    }

    /**
     * 주문조회 - 주문접수 현황 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view7/{id}")
    public String showManager7(@PathVariable Long id, Model model){
        return view(id, "/order/receive/list", "accept/order_view", model);
    }

    /**
     * 주문조회 - 접수완료 현황 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view8/{id}")
    public String showManager8(@PathVariable Long id, Model model){
        return view(id, "/order/finish/list", "accept/order_view", model);
    }

    /**
     * 주문조회 - 빨래통 입고 예정 현황 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view9/{id}")
    public String showManager9(@PathVariable Long id, Model model){
        return view(id, "/order/plan/list", "accept/order_view", model);
    }

    /**
     * 주문조회 - 빨래통 입고 현황 화면의 상세
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view10/{id}")
    public String showManager10(@PathVariable Long id, Model model){
        return view(id, "/order/stock/list", "accept/order_view", model);
    }

    /**
     * 수거 – 주문요청생성
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view11/{id}")
    public String showManager11(@PathVariable Long id, Model model){
//        return view(id, "/take/order/list", "accept/order_view", model);
        return view(id, "/take/order/list", "accept/order_form", model);
    }

    /**
     * 수거 – 코디배정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view12/{id}")
    public String showManager12(@PathVariable Long id, Model model){
//        return view(id, "/take/assign/list", "accept/order_view", model);
//        2월 25일 수정
        return view(id, "/take/assign/list", "accept/order_form", model);
    }

    /**
     * 수거 – 가용코디현황
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view13/{id}")
    public String showManager13(@PathVariable Long id, Model model){
        return view(id, "/take/extra/list", "accept/order_view", model);
    }

    /**
     * 고객 Claim 접수 및 처리
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/view14/{id}")
    public String showManager14(@PathVariable Long id, Model model){
        return view(id, "/claim/list", "accept/order_view", model);
    }

    /**
     * 수정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}")
    public String showManager(@PathVariable Long id, Model model){
        return view(id, "/accept/order/list", "accept/order_form", model);
    }

    private String view(final Long id, final String back_url, final String template, Model model) {
        // 뒤로가기시 url
        model.addAttribute("back_url", back_url);

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

        TbOrder order = tbOrderService.getTbOrderById(id);

//        고객정보
        if(order != null) {
            order.setMode("edit");

            TbMember tbMember = tbMemberService.getTbMemberById(order.getMbCd());

//            고객 최근 배송주소
            if(tbMember != null) {
                order.setMbNicNm(tbMember.getMbNicNm());
                order.setMbTel(tbMember.getMbTel());
                order.setMbEmail(tbMember.getMbEmail());

                List<?> address = tbAddressService.getAddressCds(tbMember.getMbCd());
                model.addAttribute("address_list", address);
            }

//            바코드 시작번호
            model.addAttribute("bnBarCd", tbBranchService.getBnBarCd(order.getBnCd()));
        }

        model.addAttribute("order", order);

        return template;
    }

/*    @RequestMapping("/{id}")
    public String showManager(@PathVariable Long id, Model model){

        // 뒤로가기시 url
        model.addAttribute("back_url", "/accept/order/list");

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

        TbOrder order = tbOrderService.getTbOrderById(id);
        order.setMode("edit");

//        고객정보
        if(order != null) {
            TbMember tbMember = tbMemberService.getTbMemberById(order.getMbCd());
            order.setMbNicNm(tbMember.getMbNicNm());
            order.setMbTel(tbMember.getMbTel());
            order.setMbEmail(tbMember.getMbEmail());

//            고객 최근 배송주소
            if(tbMember != null) {
                List<?> address = tbAddressService.getAddressCds(tbMember.getMbCd());
                model.addAttribute("address_list", address);
            }
        }

        model.addAttribute("order", order);

        return "accept/order_form";
    }*/

    /**
     * 저장
     * @param order
     * @return
     */
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public String saveOrder(@Valid TbOrder order, BindingResult bindingResult) throws NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException, InvocationTargetException {
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbOrderService.saveTbOrder(order);
        return "redirect:/accept/order/" + order.getOrCd();
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
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

        List<?> list = tbOrderService.getOrderList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/accept_order_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "접수처리목록"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }
}
