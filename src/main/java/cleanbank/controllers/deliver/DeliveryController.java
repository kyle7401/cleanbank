package cleanbank.controllers.deliver;

import cleanbank.Service.IDeliveryService;
import cleanbank.Service.ITbCodeService;
import cleanbank.utils.ExcelView;
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
//배송-출고처리
@Controller
@RequestMapping("/delivery")
public class DeliveryController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbCodeService tbCodeService;

    @Autowired
    private IDeliveryService deliveryService;
    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
//        주문상태 콤보
        List<?> cmb_status = tbCodeService.getCdIts01();
        model.addAttribute("cmb_status", cmb_status);

        return "delivery/delivery_list";
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public View excel3(Model model,
                       @RequestParam(value="tac", required=false) final String tac,
                       @RequestParam(value="from", required=false) final String from,
                       @RequestParam(value="to", required=false) final String to,
                       @RequestParam(value="hour48", required=false) final String hour48,
                       @RequestParam(value="status", required=false) final String status,
                       @RequestParam(value="keyword", required=false) final String keyword,
                       @RequestParam(value="condition", required=false) final String condition
    ){
        log.debug("검색조건 = tac: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                tac, from, to, keyword, condition, status);

        List<?> list = deliveryService.getDeliveryList(tac, from, to, hour48, status, keyword, condition);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/delivery_factory_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "공장출고확인"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }
}
