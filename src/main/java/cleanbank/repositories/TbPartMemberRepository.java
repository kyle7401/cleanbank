package cleanbank.repositories;

import cleanbank.domain.TbPartMember;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbPartMemberRepository extends PagingAndSortingRepository<TbPartMember, Integer>, JpaSpecificationExecutor<TbPartMember> {
    TbPartMember findByPmEmail(final String pmEmail);
}
