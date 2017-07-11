package cleanbank.repositories;

import cleanbank.domain.TbCode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbCodeRepository extends CrudRepository<TbCode, Integer> {
public interface TbCodeRepository extends PagingAndSortingRepository<TbCode, Integer>, JpaSpecificationExecutor<TbCode> {
//    http://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
//    List<TbCode> findByEpEmail(String epEmail);

    /*List<TbCode> findByBnCd(Integer bnCd);*/

//    http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    /*@Query("select distinct cdGp from TbCode as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
    List<String> getcdGps();*/

//    상위코드 검색
    @Query("SELECT t FROM TbCode as t where t.cdIt = '00' and (t.delYn is null or t.delYn != 'Y')")
    List<TbCode> getcdGps();

//    하위코드 검색
    @Query("SELECT t FROM TbCode as t where t.cdGp = :cdGp and t.cdIt != '00' and (t.delYn is null or t.delYn != 'Y') ORDER BY t.cdGp, t.cdSort, t.cdIt")
    List<TbCode> getCdIts(@Param("cdGp") String cdGp);

    @Query("SELECT t FROM TbCode as t where t.cdGp = :cdGp and t.cdIt = :cdIt and (t.delYn is null or t.delYn != 'Y')")
    TbCode getCdNm(@Param("cdGp") String cdGp, @Param("cdIt") String cdIt);
}
