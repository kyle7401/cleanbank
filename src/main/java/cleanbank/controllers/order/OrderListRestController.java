package cleanbank.controllers.order;

import cleanbank.Service.IOrderListService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/orderlist")
public class OrderListRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IOrderListService orderlistservice;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 접수처리 검색
     *
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/list")
    public List<?> getList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList(bncd, from, to, keyword, condition, status);
        return list;
    }

//    주문전체 현황
    @RequestMapping(value = "/list3")
    public List<?> getList3(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList5(bncd, from, to, keyword, condition, status, null, null);
        return list;
    }

    @RequestMapping(value = "/receive_list")
    public List<?> receive_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY a.OR_REQ_DT ASC");
        return list;
    }

//    검수완료 현황
    @RequestMapping(value = "/finish_list")
    public List<?> finish_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY a.OR_VISIT_DT ASC");
        return list;
    }

    //    검수완료 현황
    @RequestMapping(value = "/plan_list")
    public List<?> plan_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        String status2 = null;

//        공장입고/세탁중/공장출고
        if(StringUtils.isEmpty(status)) {
            status2 = "('0105', '0106', '0107')";
        }

//        List<?> list = orderlistservice.getTotalList3(bncd, from, to, keyword, condition, status, status2, hour48, " ORDER BY b.IT_OUT_DT ASC");
        List<?> list = orderlistservice.getTotalList3(bncd, from, to, keyword, condition, status, status2, hour48, " ORDER BY b.IT_IN_DT ASC"); // 3월3일 공장입고일시
        return list;
    }

//    백민입고 현황
    @RequestMapping(value = "/stock_list")
    public List<?> stock_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

//        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY b.IT_BAEK_IN_DT ASC");
        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY IT_BAEK_IN_DT ASC");
        return list;
    }

//    주문요청생성
    @RequestMapping(value = "/order_list")
    public List<?> order_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

//        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY b.IT_REQ_DT ASC");
        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY a.OR_REQ_DT ASC");
        return list;
    }

    //    코디배정
    @RequestMapping(value = "/assign_list")
    public List<?> assign_list(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @RequestParam(value="hour48", required=false) final String hour48
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

//        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY b.IT_REQ_DT ASC");
        List<?> list = orderlistservice.getTotalList4(bncd, from, to, keyword, condition, status, " ORDER BY a.OR_REQ_DT ASC");
        return list;
    }

    /**
     * 가용코디현황 검색 : 주문상태가 주문접수/수거중/수거완료를 디폴트로 검색
     *
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/extralist")
    public List<?> getExtraList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        String status2 = null;

        if(StringUtils.isEmpty(status)) {
            status2 = "('0101', '0102', '0103')";
        }

        List<?> list = orderlistservice.getTotalList2(bncd, from, to, keyword, condition, status, status2);
        return list;
    }
}
