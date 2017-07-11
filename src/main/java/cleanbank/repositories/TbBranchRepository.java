package cleanbank.repositories;

import cleanbank.domain.TbBranch;
import cleanbank.viewmodel.CodeValue;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbBranchRepository extends CrudRepository<TbBranch, Integer> {
public interface TbBranchRepository extends PagingAndSortingRepository<TbBranch, Integer>, JpaSpecificationExecutor<TbBranch> {
    @Query("SELECT t.bnCd as code, t.bnNm as value FROM TbBranch as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.bnNm")
    List<CodeValue> getBranchCds();

    @Query("SELECT t.bnCd as code, t.bnNm as value FROM TbBranch as t where t.bnCd = ? and (t.delYn is null or t.delYn != 'Y') ORDER BY t.bnNm")
    List<CodeValue> getBranchCds(@Param("bnCd") int bnCd);

    @Query("SELECT t FROM TbBranch as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.bnNm")
    List<TbBranch> getBranchList();
}
