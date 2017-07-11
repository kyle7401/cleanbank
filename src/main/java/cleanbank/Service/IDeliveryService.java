package cleanbank.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface IDeliveryService {

    List<?> getDeliveryList(
            final String tac,
            final String bncd,
            final String from,
            final String hour48,
            final String status,
            final String keyword, final String condition
    );

//    int changeStatus(final ArrayList<Long> items, final String status);
}
