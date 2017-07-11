package cleanbank.Service;

import cleanbank.domain.InicisResult;
import cleanbank.domain.TbOrder;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.viewmodel.MoOrder;
import cleanbank.viewmodel.MoOrderDetail1;
import cleanbank.viewmodel.MoOrderDetail2;

import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbOrderService {
    int changeStatus(Long no, String status) throws Exception;

    Iterable<TbOrder> listAllTbOrders();

    TbOrder getTbOrderById(Long id);

    TbOrder saveTbOrder(TbOrder TbOrder) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException;

    void deleteTbOrder(Long id);

    //    이하 추가
    void deleteTbOrder2(Long id);

    List<?> getOrderList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    );

    //    로그용 접수처리 검색
//    http://antop.tistory.com/30
    List<?> getLogList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            String status
    );

/*    Iterable<TbOrder> getLogList2(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            String status,
            final Pageable pageable
    );*/

    int changeSt2(final Long no, final String status) throws IOException, GcmMulticastLimitExceededException;

    List<?> getMoOrderList(final Integer mbCd, final List<String> status);

    List<?> getCompleteList(final Integer mbCd, final List<String> status);

    MoOrderDetail1 getOrderDetail1(final Integer mbCd, final Long orCd);

    MoOrderDetail2 getOrderDetail2(final Integer mbCd, final Long orCd);

    MoOrderDetail2 getOrderDetail3(final Integer mbCd, final Long orCd);

    TbOrder saveMoOrder(MoOrder order);

    String inicis_result(InicisResult inicisResult) throws MessagingException;
}
