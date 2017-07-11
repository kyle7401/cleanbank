package cleanbank.repositories;

import cleanbank.domain.TbPartner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 *  http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-five-querydsl/
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbPartnerRepository extends PagingAndSortingRepository<TbPartner, Integer>, JpaSpecificationExecutor<TbPartner> {
public interface TbPartnerRepository extends PagingAndSortingRepository<TbPartner, Integer>, QueryDslPredicateExecutor<TbPartner> {
    @Query("SELECT t.ptCd, t.ptNm as value FROM TbPartner as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.ptNm")
    List<Object> getPartnerCds();
}
