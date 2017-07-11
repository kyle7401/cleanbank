package cleanbank.Service;

import cleanbank.domain.TbProduct;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbProductService {
    Iterable<TbProduct> listAllTbProducts();

    TbProduct getTbProductById(Integer id);

    TbProduct saveTbProduct(TbProduct TbProduct);

    void deleteTbProduct(Integer id);

    //    이하 추가
    void deleteTbProduct2(Integer id);

    List<TbProduct> getPdLvl1s();

    List<TbProduct> getPdLvl2s();

    List<TbProduct> getPdLvl3s();

//    List<String> getcdGps();

//    List<TbProduct> findByEpEmail(String epEmail);
//    List<TbProduct> findByBnCd(Integer bnCd);
}
