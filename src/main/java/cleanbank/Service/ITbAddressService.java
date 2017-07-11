package cleanbank.Service;

import cleanbank.domain.TbAddress;
import cleanbank.viewmodel.MoOrder;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbAddressService {
    Iterable<TbAddress> listAllTbAddresss();

    TbAddress getTbAddressById(Integer id);

    TbAddress saveTbAddress(TbAddress TbAddress);

    void deleteTbAddress(Integer id);

    //    이하 추가
    void deleteTbAddress2(Integer id);

    List<?> getAddressCds(Integer mbCd);

    TbAddress saveMoAddress(MoOrder order);

    TbAddress addAddress(TbAddress newAddress);
}
