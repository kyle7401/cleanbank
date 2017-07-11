package cleanbank.repositories;

import cleanbank.domain.TbPromoLocation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbPromoLocationRepository extends PagingAndSortingRepository<TbPromoLocation, Long>, JpaSpecificationExecutor<TbPromoLocation> {
    /*@Query("SELECT t.id as code, t.mbAddr as value FROM TbPromoLocation as t where t.mbCd = :mbCd and delYn is null or t.delYn != 'Y' ORDER BY t.id desc")
    List<CodeValue> getPromoLocationCds(@Param("mbCd") Integer mbCd);*/
}
