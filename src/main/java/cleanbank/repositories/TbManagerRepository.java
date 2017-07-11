package cleanbank.repositories;

import cleanbank.domain.TbManager;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbManagerRepository extends CrudRepository<TbManager, Integer> {
    public interface TbManagerRepository extends PagingAndSortingRepository<TbManager, Integer>, JpaSpecificationExecutor<TbManager> {

//    http://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
//    List<TbManager> findByMgEmail(String mgEmail);
    TbManager findByMgEmail(String mgEmail);

    List<TbManager> findByBnCd(Integer bnCd);

    TbManager findByMgEmailAndMgType(String mgEmail, String mgType);
}
