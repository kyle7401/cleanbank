package cleanbank.controllers.deliver;

import cleanbank.Service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-09.
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDeliveryService deliveryService;

    //    ----------------------------------------------------------------------------------------------------------------

    //    공장 출고 확인
    @RequestMapping(value = "/delivery_list")
    public List<?> delivery_list(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="hour48", required=false) final String hour48,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition
    ) {

        List<?> list = deliveryService.getDeliveryList(tac, from, to, hour48, status, keyword, condition);
        return list;
    }

}
