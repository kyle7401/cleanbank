package cleanbank.repositories;

import cleanbank.domain.TbOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbOrderRepository extends PagingAndSortingRepository<TbOrder, Integer>, JpaSpecificationExecutor<TbOrder> {
public interface TbOrderRepository extends PagingAndSortingRepository<TbOrder, Long>, JpaSpecificationExecutor<TbOrder> {
//public interface TbOrderRepository extends PagingAndSortingRepository<TbOrder, Long>, QueryDslPredicateExecutor<TbOrder> {

    @Query("SELECT t FROM TbOrder as t where (t.delYn is null or t.delYn != 'Y') and t.orStatus not in ?1 ORDER BY t.orCd")
    List<?> getOrdByNotStatus(@Param("status") List<String> status);
}
