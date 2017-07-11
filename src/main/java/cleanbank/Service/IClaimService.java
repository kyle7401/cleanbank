package cleanbank.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface IClaimService {

    List<?> getClaimList(
            final String tac,
            final String from,
            final String to,
            final String keyword,
            final String condition
            );
}
