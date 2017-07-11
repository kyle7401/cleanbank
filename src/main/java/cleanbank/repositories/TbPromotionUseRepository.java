package cleanbank.repositories;

import cleanbank.domain.TbPromotionUse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbPromotionUseRepository extends PagingAndSortingRepository<TbPromotionUse, Long>, JpaSpecificationExecutor<TbPromotionUse> {
//    TbPromotionUse findByMbCdAndPoCd(int mbCd, long poCd);

    @Query("SELECT t FROM TbPromotionUse as t where t.mbCd = :mbCd and t.tbPromotion.poCd = :poCd and (delYn is null or t.delYn != 'Y')")
    TbPromotionUse findByMbCdAndPoCd(@Param("mbCd") int mbCd, @Param("poCd") long poCd);

    @Query("SELECT u FROM TbPromotionUse u INNER JOIN u.tbPromotion p" +
            " where u.mbCd = :mbCd" +
            " and (u.delYn is null or u.delYn != 'Y')")
    List<TbPromotionUse> getPoCoup(@Param("mbCd") int mbCd);

    @Query("SELECT u FROM TbPromotionUse u INNER JOIN u.tbPromotion p" +
            " where u.mbCd = :mbCd" +
            " and u.orCd = :orCd" +
            " and (u.delYn is null or u.delYn != 'Y')")
    TbPromotionUse getOrdCoupInf(@Param("mbCd") int mbCd, @Param("orCd") Long orCd);

    @Query("SELECT u FROM TbPromotionUse u INNER JOIN u.tbPromotion p" +
            " where u.mbCd = :mbCd" +
            " and p.poCd = :poCd" +
            " and (u.delYn is null or u.delYn != 'Y')")
    TbPromotionUse getPoCoupInf(@Param("mbCd") int mbCd, @Param("poCd") long poCd);

    @Query("SELECT count(1) FROM TbPromotionUse as t where t.tbPromotion.poCd = :poCd and (delYn is null or t.delYn != 'Y')")
    Integer getPoUseCnt(@Param("poCd") long poCd);

    @Query("SELECT u FROM TbPromotionUse u INNER JOIN u.tbPromotion p" +
            " where u.mbCd = :mbCd" +
            " and u.orCd = :orCd" +
            " and (u.delYn is null or u.delYn != 'Y')")
    List<TbPromotionUse> getOrdCoupLIst(@Param("mbCd") int mbCd, @Param("orCd") Long orCd);
}
