package cleanbank.controllers.factory;

import cleanbank.Service.IFactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
@RestController
@RequestMapping("/factory")
public class FactoryRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFactoryService factoryService;

    //    ----------------------------------------------------------------------------------------------------------------

//    입고 현황 파악
    @RequestMapping(value = "/stock_list")
    public List<?> stock_list(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to
    ) {

        List<?> list = factoryService.getStockList(tac, from, to);
        return list;
    }

    //    공장처리변경
    @RequestMapping(value = "/process_list")
    public List<?> process_list(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        List<?> list = factoryService.getProcessList(tac, from, to, hour48);
        return list;
    }

    //    출고처리
    @RequestMapping(value = "/delivery_list")
    public List<?> delivery_list(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="status", required=false) final String status
    ) {

        List<?> list = factoryService.getDeliveryList(tac, from, to, status);
        return list;
    }
}
