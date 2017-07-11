package cleanbank.repositories;

import cleanbank.domain.TbSalesInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbSalesInfoRepository extends PagingAndSortingRepository<TbSalesInfo, Integer>, JpaSpecificationExecutor<TbSalesInfo> {
}
