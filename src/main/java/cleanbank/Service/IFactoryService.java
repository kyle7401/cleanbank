package cleanbank.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface IFactoryService {

    List<?> getStockList(
            final String tac,
            final String bncd,
            final String from
    );

    List<?> getProcessList(
            final String tac,
            final String bncd,
            final String from,
            final String hour48
    );

    List<?> getDeliveryList(
            final String tac,
            final String bncd,
            final String from,
            final String status
    );

//    int changeStatus(final ArrayList<Long> items, final String status);
}
