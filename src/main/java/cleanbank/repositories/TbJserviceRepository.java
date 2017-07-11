package cleanbank.repositories;

import cleanbank.domain.TbJservice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbJserviceRepository extends PagingAndSortingRepository<TbJservice, Long>, JpaSpecificationExecutor<TbJservice> {
    /*@Query("SELECT t.bnCd as code, t.bnNm as value FROM TbJservice as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.bnNm")
    List<Object> getJserviceCds();*/
}
