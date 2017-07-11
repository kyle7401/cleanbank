package cleanbank.Service;

import cleanbank.domain.TbSalesInfo;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbSalesInfoService {
    Iterable<TbSalesInfo> listAllTbSalesInfos();

    TbSalesInfo getTbSalesInfoById(Integer id);

    TbSalesInfo saveTbSalesInfo(TbSalesInfo TbSalesInfo);

    void deleteTbSalesInfo(Integer id);

    //    이하 추가
    void deleteTbSalesInfo2(Integer id);

    List<TbSalesInfo> getSaList(final int bnCd);
}
