package cleanbank.controllers.accept;

import cleanbank.Service.IStockService;
import cleanbank.Service.ITbCodeService;
import cleanbank.Service.TbBranchService;
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
 * Created by hyoseop on 2015-11-25.
 */
@Controller
@RequestMapping("/accept/stock")
public class StockController {
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

    @Autowired
    private ITbCodeService tbCodeService;

    @Autowired
    private IStockService stockService;
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

        return "accept/stock_list";
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value="tac", required=false) final String tac,
                      @RequestParam(value="bncd", required=false) final String bncd,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to,
            /*@RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,*/
                      @RequestParam(value="status", required=false) String status
    ){

//        2월24일 : 공장입고완료내역을 누르면 상태가 공장입고 내역인것만 조회가 되고 공장입고증 출력을 하면 엑셀에 출력되야합니다.(현재는 접수완료상태의 세탁물들도 엑셀에 출력되고있음.)
        status = "0105";

        List<?> list = stockService.getStockList(tac, bncd, from, to, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/accept_stock_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "입고증"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

}
