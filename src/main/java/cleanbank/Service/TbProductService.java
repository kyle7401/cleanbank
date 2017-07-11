package cleanbank.Service;

import cleanbank.domain.TbProduct;
import cleanbank.repositories.TbProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbProductService implements ITbProductService {
    private TbProductRepository tbProductRepository;

    @Autowired
    public void setTbProductRepository(TbProductRepository tbProductRepository) {
        this.tbProductRepository = tbProductRepository;
    }

    @Override
    public Iterable<TbProduct> listAllTbProducts() {
        return tbProductRepository.findAll();
    }

    @Override
    public TbProduct getTbProductById(Integer id) {
        return tbProductRepository.findOne(id);
    }

    @Override
    public TbProduct saveTbProduct(TbProduct TbProduct) {
        return tbProductRepository.save(TbProduct);
    }

    @Override
    public void deleteTbProduct(Integer id) {
        tbProductRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbProduct2(Integer id) {
        TbProduct product = tbProductRepository.findOne(id);
        product.setDelYn("Y");
        tbProductRepository.save(product);
    }


    /**
     * 상위코드 콤보
     *
     * @return
     */
    @Override
    public List<TbProduct> getPdLvl1s() {
        return tbProductRepository.getPdLvl1s();
    }

    /**
     * 중위코드 콤보
     *
     * @return
     */
    @Override
    public List<TbProduct> getPdLvl2s() {
        return tbProductRepository.getPdLvl2s();
    }

    /**
     * 하위 상품목록 : 모바일용
     * @return
     */
    @Override
    public List<TbProduct> getPdLvl3s() {
        return tbProductRepository.getPdLvl3s();
    }

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbProduct as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbProductRepository.getcdGps();
    }*/

/*    @Override
    public List<TbProduct> findByEpEmail(String epEmail) {
        return tbProductRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbProduct> findByBnCd(Integer bnCd) {
        return TbProductRepository.findByBnCd(bnCd);
    }*/
}
