package cleanbank.controllers.claim;

import cleanbank.Service.IClaimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-09.
 */
@RestController
@RequestMapping("/claim")
public class ClaimRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IClaimService claimService;

    //    ----------------------------------------------------------------------------------------------------------------

    //    고객 Claim 접수 및 처리
    @RequestMapping(value = "/claim_list")
    public List<?> claim_list(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition
    ) {

        List<?> list = claimService.getClaimList(tac, from, to, keyword, condition);
        return list;
    }

}
