package cleanbank.Service;

import cleanbank.domain.TbPromoLocation;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPromoLocationService {
    Iterable<TbPromoLocation> listAllTbPromoLocations();

    TbPromoLocation getTbPromoLocationById(Long id);

    TbPromoLocation saveTbPromoLocation(TbPromoLocation TbPromoLocation);

    void deleteTbPromoLocation(Long id);

    //    이하 추가
    void deleteTbPromoLocation2(Long id);

//    List<?> getPromoLocationCds(Long mbCd);
}
