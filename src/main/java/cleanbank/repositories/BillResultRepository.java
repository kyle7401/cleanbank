package cleanbank.repositories;

import cleanbank.domain.BillResult;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface BillResultRepository extends PagingAndSortingRepository<BillResult, Integer>, JpaSpecificationExecutor<BillResult> {
}
