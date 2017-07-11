package cleanbank.repositories;

import cleanbank.domain.TbAddress;
import cleanbank.viewmodel.CodeValue;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbAddressRepository extends CrudRepository<TbAddress, Integer> {
public interface TbAddressRepository extends PagingAndSortingRepository<TbAddress, Integer>, JpaSpecificationExecutor<TbAddress> {
//    TODO : 추후 Distinct 처리해야 함
//    @Query("SELECT t.id as code, t.mbAddr as value FROM TbAddress as t where t.mbCd = :mbCd and delYn is null or t.delYn != 'Y' ORDER BY t.id desc")
    @Query(value = "SELECT" +
            "        t.ID as code," +
            "        CASE        " +
            "            WHEN MB_ADDR1 IS NULL " +
            "            AND MB_ADDR2 IS NULL THEN NULL        " +
            "            WHEN            MB_ADDR1 IS NOT NULL                " +
            "            AND MB_ADDR2 IS NULL        THEN            MB_ADDR1        " +
            "            WHEN            MB_ADDR1 IS NOT NULL                " +
            "            AND MB_ADDR2 IS NOT NULL        THEN CONCAT(MB_ADDR1, ' ', MB_ADDR2) " +
            "        END as value " +
            "    FROM" +
            "        TB_ADDRESS as t " +
            "    where" +
            "        t.MB_CD = ? " +
            "        and DEL_YN is null " +
            "        or t.DEL_YN != 'Y' " +
            "    ORDER BY" +
            "        t.ID desc", nativeQuery = true)
    List<CodeValue> getAddressCds(@Param("mbCd") Integer mbCd);

    @Query("SELECT t FROM TbAddress as t where t.mbCd = :mbCd and (delYn is null or t.delYn != 'Y') ORDER BY t.id desc")
    List<TbAddress> getAddressList(@Param("mbCd") Integer mbCd);

    @Query("FROM TbAddress as t where t.mbCd = :mbCd and t.mbLat = :mbLat and t.mbLng = :mbLng and (delYn is null or t.delYn != 'Y')")
    TbAddress getAddress(@Param("mbCd") Integer mbCd, @Param("mbLat") String mbLat, @Param("mbLng") String mbLng);
}
