package cleanbank.repositories;

import cleanbank.domain.TbBranchLocs;
import cleanbank.viewmodel.CodeValue;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbBranchLocsRepository extends PagingAndSortingRepository<TbBranchLocs, Integer>, JpaSpecificationExecutor<TbBranchLocs> {
    @Query(value = "SELECT " +
            "    a.BL_CD, b.NAME" +
            " FROM" +
            "    TB_BRANCH_LOCS AS a" +
            "        INNER JOIN" +
            "    TB_LOCATION AS b" +
            " WHERE" +
            "    BN_CD = ?" +
            "        AND a.BL_CD = CONCAT(b.LOC1, b.LOC2, b.LOC3)" +
            "          AND (a.DEL_YN IS NULL OR a.DEL_YN <> 'Y')" +
            "        AND (b.DEL_YN IS NULL OR b.DEL_YN <> 'Y')", nativeQuery = true)
    List<CodeValue> getTbBranchLocsByBnCd(@Param("bnCd") Integer bnCd);

    @Query(value = "SELECT * FROM TB_BRANCH_LOCS as a inner join TB_BRANCH as b on a.BN_CD = b.BN_CD" +
            " WHERE BL_NM = ?" +
            " AND (a.DEL_YN IS NULL OR a.DEL_YN <> 'Y')" +
            " AND (b.DEL_YN IS NULL OR b.DEL_YN <> 'Y')", nativeQuery = true)
    List<TbBranchLocs> getTbBranchLocsByBlNm(@Param("bnNm") String bnNm);
}
