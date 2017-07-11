package cleanbank.repositories;

import cleanbank.domain.TbPoint;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbPointRepository extends PagingAndSortingRepository<TbPoint, Integer>, JpaSpecificationExecutor<TbPoint> {
}
