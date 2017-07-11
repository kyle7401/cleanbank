package cleanbank.repositories;

import cleanbank.domain.Gcmregids;
import org.springframework.data.repository.PagingAndSortingRepository;

//import org.springframework.data.repository.CrudRepository;

/**
 * Created by hyoseop on 2016-01-16.
 */
//public interface MobnoticeRepository extends CrudRepository<Mobnotice, Integer> {
public interface GcmregidsRepository extends PagingAndSortingRepository<Gcmregids, Integer> {
    Gcmregids findByRegId(String regId);
}

