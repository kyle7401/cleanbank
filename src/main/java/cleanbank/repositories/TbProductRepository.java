package cleanbank.repositories;

import cleanbank.domain.TbProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbProductRepository extends CrudRepository<TbProduct, Integer> {
public interface TbProductRepository extends PagingAndSortingRepository<TbProduct, Integer>, JpaSpecificationExecutor<TbProduct> {

    //    http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
    @Query("SELECT t FROM TbProduct as t where t.pdLvl1 = :pdLvl1 and t.pdLvl2 = :pdLvl2 and t.pdLvl3 = :pdLvl3 and (t.delYn is null or t.delYn != 'Y')")
    List<TbProduct> checkDups(@Param("pdLvl1") String pdLvl1, @Param("pdLvl2") String pdLvl2, @Param("pdLvl3") String pdLvl3);


    @Query("SELECT t FROM TbProduct as t where t.pdLvl2 = '00' and t.pdLvl3 = '00' and (t.delYn is null or t.delYn != 'Y') order by pdSort, t.pdLvl1")
    List<TbProduct> getPdLvl1s();

    @Query("SELECT t FROM TbProduct as t where t.pdLvl2 != '00' and t.pdLvl3 = '00' and (t.delYn is null or t.delYn != 'Y')")
    List<TbProduct> getPdLvl2s();

    @Query("SELECT t FROM TbProduct as t where t.pdLvl1 != '00' and t.pdLvl2 != '00' and t.pdLvl3 != '00' and (t.delYn is null or t.delYn != 'Y')" +
            " order by t.pdLvl1, t.pdLvl2, t.pdLvl3, pdSort")
    List<TbProduct> getPdLvl3s();
}
