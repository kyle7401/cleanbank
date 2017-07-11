package cleanbank.repositories;

import cleanbank.domain.TbLocation;
import cleanbank.domain.TbPromoLocation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbLocationRepository extends PagingAndSortingRepository<TbLocation, Integer>, JpaSpecificationExecutor<TbLocation> {
    //    http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    @Query("SELECT t FROM TbLocation as t where t.loc1 = :loc1 and t.loc2 = :loc2 and t.loc3 = :loc3 and (t.delYn is null or t.delYn != 'Y')")
    List<TbLocation> checkDups(@Param("loc1") String loc1, @Param("loc2") String loc2, @Param("loc3") String loc3);

    @Query("SELECT t.loc1, t.loc2, t.loc3, t.name as value FROM TbLocation as t where t.loc2 != '00' and t.loc3 != '00' and (t.delYn is null or t.delYn != 'Y') ORDER BY t.name")
    List<?> getLocationCds();

    @Query("SELECT t FROM TbLocation as t where t.loc2 = '00' and t.loc3 = '00' and (t.delYn is null or t.delYn != 'Y') ORDER BY t.sortOrder, t.loc1")
    List<TbPromoLocation> getPlCd1s();

    @RestResource(path = "getPlCd2sByLoc1")
    @Query("SELECT t FROM TbLocation as t where t.loc1 = :loc1 and t.loc2 != '00' and t.loc3 = '00' and (t.delYn is null or t.delYn != 'Y') ORDER BY t.sortOrder, t.loc2")
    List<TbPromoLocation> getPlCd2s(@Param("loc1") final String loc1);

    @Query("SELECT t FROM TbLocation as t where t.loc2 != '00' and t.loc3 = '00' and (t.delYn is null or t.delYn != 'Y') ORDER BY t.loc1, t.sortOrder, t.loc2")
    List<TbPromoLocation> getPlCd2s();
}
