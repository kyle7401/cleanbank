package cleanbank.repositories;

import cleanbank.domain.TbFaq;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbFaqRepository extends PagingAndSortingRepository<TbFaq, Integer>, JpaSpecificationExecutor<TbFaq> {
    /*@Query("SELECT t.bnCd as code, t.bnNm as value FROM TbFaq as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.bnNm")
    List<Object> getFaqCds();*/
}
