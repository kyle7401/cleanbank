package cleanbank.Service;

import cleanbank.domain.TbOrder;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-09.
 */
public interface IOrderListService {

    List<TbOrder> getOrderList();

    List<?> getTotalList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    );

    List<?> getTotalList2(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status,
            final String status2
    );

    List<?> getTotalList3(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status,
            final String status2,
            final String hour48,
            final String OrderBy
    );

    List<?> getTotalList4(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status,
            final String OrderBy
    );

    List<?> getTotalList5(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status,
            final String OrderBy,
            final String status2
    );
}
