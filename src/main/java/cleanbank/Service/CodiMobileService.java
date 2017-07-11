package cleanbank.Service;

import cleanbank.domain.TbAddress;
import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.viewmodel.MoOrder2;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface CodiMobileService {

    TbAddress setDeliAddr(final TbOrder tbOrder, final TbOrderItems tbOrderItems, TbAddress newAddress);

    TbAddress setReqAddr(final TbOrder tbOrder, final TbOrderItems tbOrderItems, TbAddress newAddress);

    TbOrderItems getItemDetail(final Long id);

    MoOrder2 getOrderDetail(final Long orCd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    List<?> getBnSchedule(final Integer bnCd);

    List<?> getMySchedule(final Integer epCd);
}
