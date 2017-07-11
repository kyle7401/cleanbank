package cleanbank.controllers.accept;

import cleanbank.Service.IStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
@RestController
@RequestMapping("/stock")
public class StockRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IStockService stockService;

//    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list")
    public List<?> getList(
            @RequestParam(value="tac", required=false) final String tac,
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            /*@RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,*/
            @RequestParam(value="status", required=false) String status
    ) {

/*        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);*/

//        2월24일 : 검수-입고처리 : 기본적으로 주문상태가 접수완료 상태인것만 조회가 되어야함.
        if(StringUtils.isEmpty(status)) status = "0104";

        List<?> list = stockService.getStockList(tac, bncd, from, to, status);
        return list;
    }

    //    입고완료, 입고취소 처리
    @RequestMapping(value = "/changeSt", method = RequestMethod.POST)
    public String changeSt(
//            @ModelAttribute(value = "items0") ArrayList<String> items,
            @RequestParam(value="items") ArrayList<Long> items,
            @RequestParam(value="state") String state,
//            @RequestParam(value="items", required=false) String[] items3,
            HttpServletRequest request
    ) throws Exception {

        /*Enumeration em = request.getParameterNames();
        while(em.hasMoreElements()){
            String parameterName = (String)em.nextElement();
            String parameterValue = request.getParameter(parameterName);
            String[] parameterValues = request.getParameterValues(parameterName);
            if(parameterValues != null){
                for(int i=0; i<parameterValues.length; i++){
                    System.out.println("array_" + parameterName + "=" + parameterValues[i]);
                }
            } else {
                System.out.println(parameterName + "=" + parameterValue);
            }
        }*/

        if(items != null && items.size() > 0) {
            Integer iResult = stockService.changeStatus(items, state);
            log.debug("건수 = {}", items.size());
        }

        return "OK";
    }
}
