package cleanbank.repositories;

import cleanbank.domain.TbPicture;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
public interface TbPictureRepository extends PagingAndSortingRepository<TbPicture, Integer>, JpaSpecificationExecutor<TbPicture> {

/*    @Query("SELECT t FROM TbPicture as t where t.orCd = :orCd and t.itCd = :itCd and (delYn is null or t.delYn != 'Y') ORDER BY t.ptCd")
    List<TbPicture> getPictureList(@Param("orCd") long orCd, @Param("itCd") long itCd);*/

    @Query("SELECT t FROM TbPicture as t where t.orCd = :orCd and t.itCd = :itCd and t.ptCd = :ptCd and (delYn is null or t.delYn != 'Y')")
    TbPicture getPictureInfo(@Param("orCd") long orCd, @Param("itCd") long itCd, @Param("ptCd") long ptCd);

    @Query("SELECT t FROM TbPicture as t where t.itCd = :itCd and (delYn is null or t.delYn != 'Y')")
    List<TbPicture> getPictureList(@Param("itCd") long itCd);
}
