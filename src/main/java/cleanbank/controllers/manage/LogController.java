package cleanbank.controllers.manage;

import cleanbank.Service.ITbOrderService;
import cleanbank.Service.TbBranchService;
import cleanbank.Service.TbCodeService;
import cleanbank.utils.ExcelView;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-02.
 */
@Controller
@RequestMapping("/manage/log")
public class LogController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    private TbBranchService tbBranchService;

    @Autowired
    public void setTbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

    private TbCodeService tbCodeService;

    @Autowired
    public void setTbCodeService(TbCodeService tbCodeService) {
        this.tbCodeService = tbCodeService;
    }

    @Autowired
    private ITbOrderService tbOrderService;

    /*private TbOrderService tbOrderService;

    @Autowired
    public void setTbOrderService(TbOrderService tbOrderService) {
        this.tbOrderService = tbOrderService;
    }*/

    //    ----------------------------------------------------------------------------------------------------------------

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

        return "manage/log/log_list";
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

        List<?> list = tbOrderService.getLogList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/log_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "로그현황"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }
}
