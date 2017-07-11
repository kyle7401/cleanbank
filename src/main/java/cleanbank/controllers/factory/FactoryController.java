package cleanbank.controllers.factory;

import cleanbank.Service.IFactoryService;
import cleanbank.utils.ExcelView;
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
@RequestMapping("/factory")
public class FactoryController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    @Autowired
    private IFactoryService factoryService;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 입고현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/stock/list", method = RequestMethod.GET)
    public String list(Model model){
        return "factory/stock_list";
    }

    @RequestMapping(value = "/stock/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value="tac", required=false) final String tac,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to
    ){
        List<?> list = factoryService.getStockList(tac, from, to);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/factory_stock_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "입고 현황 파악"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    @RequestMapping(value = "/process/excel", method = RequestMethod.GET)
    public View excel2(Model model,
                      @RequestParam(value="tac", required=false) final String tac,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to,
                       @RequestParam(value="hour48", required=false) final String hour48
    ){
        List<?> list = factoryService.getProcessList(tac, from, to, hour48);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/factory_process_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "공장처리변경"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    @RequestMapping(value = "/delivery/excel", method = RequestMethod.GET)
    public View excel3(Model model,
                       @RequestParam(value="tac", required=false) final String tac,
                       @RequestParam(value="from", required=false) final String from,
                       @RequestParam(value="to", required=false) final String to,
                       @RequestParam(value="hour48", required=false) final String hour48
    ){
//        List<?> list = factoryService.getDeliveryList(tac, from, to, hour48);
        List<?> list = factoryService.getDeliveryList(tac, from, to, "0106");
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/factory_delivery_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "출고처리"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

    /**
     * 공장처리
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/process/list", method = RequestMethod.GET)
    public String list2(Model model){
        return "factory/process_list";
    }

    /**
     * 출고처리
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/delivery/list", method = RequestMethod.GET)
    public String list3(Model model){
        return "factory/delivery_list";
    }
}
