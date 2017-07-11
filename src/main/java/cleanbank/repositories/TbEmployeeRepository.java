package cleanbank.repositories;

import cleanbank.domain.TbEmployee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbEmployeeRepository extends CrudRepository<TbEmployee, Integer> {
    public interface TbEmployeeRepository extends PagingAndSortingRepository<TbEmployee, Integer>, JpaSpecificationExecutor<TbEmployee> {

//    http://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
//    List<TbEmployee> findByEpEmail(String epEmail);
    TbEmployee findByEpEmail(@Param("epEmail") String epEmail);

    @Query("SELECT t.epCd, t.epNm, t.epDriveYn as value FROM TbEmployee as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.epNm")
    List<?> getEmpCds();

    TbEmployee findByEpEmailAndEpPass(@Param("epEmail") String epEmail, @Param("epPass") String epPass);
}
