package cleanbank.repositories;

import cleanbank.domain.InicisResult;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface InicisResultRepository extends PagingAndSortingRepository<InicisResult, Integer>, JpaSpecificationExecutor<InicisResult> {
}
