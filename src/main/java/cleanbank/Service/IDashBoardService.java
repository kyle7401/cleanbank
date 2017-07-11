package cleanbank.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface IDashBoardService {

    //    수거요청현황
    List<?> getTakeReqList();

    //    배송요청현황
    List<?> getDeliReqList();

    //    전체현황
    Object getCntByStatus();

    //    전주 대비 금일 현황
    Object[] getStatus1(final String from, final String to);

    //    전월 대비 금일 현황
    Object[] getStatus2();

//    전주 대비 금일 누적 현황
    Object[] getStatus3(final String from, final String to, final String today);

//    전월 대비 금일 누적 현황
    Object[] getStatus4();
}