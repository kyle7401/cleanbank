package cleanbank.Service;

import cleanbank.domain.TbLocation;
import cleanbank.domain.TbPromoLocation;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbLocationService {
    Iterable<TbLocation> listAllTbLocations();

    TbLocation getTbLocationById(Integer id);

    TbLocation saveTbLocation(TbLocation TbLocation);

    void deleteTbLocation(Integer id);

    //	이하 수정 혹은 추가 ------------------------------------------------------------------------
//    List<String> getcdGps();

    void deleteTbLocation2(Integer id);

    List<?> getLocationCds();

    List<TbPromoLocation> getPlCd1s();

    List<TbPromoLocation> getPlCd2s();

    List<TbPromoLocation> getPlCd2s(final String plCd1);
}
