package cleanbank.controllers.claim;

import cleanbank.Service.IClaimService;
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
 * Created by hyoseop on 2015-12-11.
 */
@Controller
@RequestMapping("/claim")
public class ClaimController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IClaimService claimService;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 입고현황
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        return "claim/claim_list";
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value="tac", required=false) final String tac,
                      @RequestParam(value="from", required=false) final String from,
                      @RequestParam(value="to", required=false) final String to,
                      @RequestParam(value="keyword", required=false) final String keyword,
                      @RequestParam(value="condition", required=false) final String condition
    ){
        List<?> list = claimService.getClaimList(tac, from, to, keyword, condition);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/claim_list_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "고객 Claim 접수 및 처리"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

}
