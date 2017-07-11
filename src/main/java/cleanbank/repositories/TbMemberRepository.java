package cleanbank.repositories;

import cleanbank.domain.TbMember;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbMemberRepository extends CrudRepository<TbMember, Integer> {
public interface TbMemberRepository extends PagingAndSortingRepository<TbMember, Integer>, JpaSpecificationExecutor<TbMember> {
    @Query("SELECT t FROM TbMember as t where t.delYn is null or t.delYn != 'Y' order by t.mbNm")
    List<TbMember> listAllMembers();

    List<TbMember> findByMbEmail(@Param("mbEmail") String mbEmail);

    TbMember findByMbMycode(@Param("mbMycode") String mbMycode);
}
