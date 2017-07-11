package cleanbank.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface IStockService {

    List<?> getStockList(
            final String tac,
            final String bncd,
            final String from,
            final String to,
            final String status
    );

    int changeStatus(final ArrayList<Long> items, final String status) throws Exception;
}
