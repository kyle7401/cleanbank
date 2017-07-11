package cleanbank.repositories;

import cleanbank.domain.TbMemberBillinfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbMemberBillinfoRepository extends PagingAndSortingRepository<TbMemberBillinfo, Integer>, JpaSpecificationExecutor<TbMemberBillinfo> {
    List<?> findByMbCd(@Param("mbCd") int mbCd);

    TbMemberBillinfo findByMbCdAndMbCardNo(@Param("mbCd") int mbCd, @Param("mbCardNo") String mbCardNo);
}