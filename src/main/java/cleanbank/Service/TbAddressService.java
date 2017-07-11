package cleanbank.Service;

import cleanbank.domain.TbAddress;
import cleanbank.repositories.TbAddressRepository;
import cleanbank.viewmodel.MoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbAddressService implements ITbAddressService {
    private TbAddressRepository tbAddressRepository;

    @Autowired
    private EntityManager entityManager;

//    ---------------------------------------------------------------------------------

    @Autowired
    public void setTbAddressRepository(TbAddressRepository tbAddressRepository) {
        this.tbAddressRepository = tbAddressRepository;
    }

    @Override
    public Iterable<TbAddress> listAllTbAddresss() {
        return tbAddressRepository.findAll();
    }

    @Override
    public TbAddress getTbAddressById(Integer id) {
        return tbAddressRepository.findOne(id);
    }

    @Override
    public TbAddress saveTbAddress(TbAddress TbAddress) {
        return tbAddressRepository.save(TbAddress);
    }

    @Override
    public void deleteTbAddress(Integer id) {
        tbAddressRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbAddress2(Integer id) {
        TbAddress address = tbAddressRepository.findOne(id);
        address.setDelYn("Y");
        tbAddressRepository.save(address);
    }

    @Override
    public List<?> getAddressCds(Integer mbCd) {
        return tbAddressRepository.getAddressCds(mbCd);
    }

    @Override
    public TbAddress saveMoAddress(MoOrder order) {

//        최근주소 검색
        Query selectQuery = entityManager.createQuery("FROM TbAddress as t" +
                " where t.mbCd = ?1 and t.mbLat = ?2 and t.mbLng = ?3")
                .setParameter(1, order.getMbCd())
                .setParameter(2, order.getMbLat())
                .setParameter(3, order.getMbLat());

        TbAddress address = (TbAddress) selectQuery.getSingleResult();

//        최근주소 Insert
        if(address == null) {
            address.setEvtNm("신규");
            address.setUser("모바일");
            address.setRegiDt(new Date());
            address.setMbAddr1(order.getMbAddr1());
            address.setMbAddr2(order.getMbAddr2());
            address.setMbLat(order.getMbLat());
            address.setMbLng(order.getMbLng());

            address = tbAddressRepository.save(address);
        }

        return tbAddressRepository.save(address);

//        return tbAddressRepository.save(TbAddress);
    }

    /**
     * 주소가 중복되지 않으면 추가
     * @param newAddress
     * @return
     */
    @Override
    public TbAddress addAddress(TbAddress newAddress) {

        TbAddress address = tbAddressRepository.getAddress(newAddress.getMbCd(), newAddress.getMbLat(), newAddress.getMbLng());

//        최근주소 Insert
        if(address == null) {
            address = new TbAddress();

            address.setEvtNm("신규");
            address.setUser("모바일");
            address.setMbCd(newAddress.getMbCd());
            address.setRegiDt(new Date());
            address.setMbAddr1(newAddress.getMbAddr1());
            address.setMbAddr2(newAddress.getMbAddr2());
            address.setMbLat(newAddress.getMbLat());
            address.setMbLng(newAddress.getMbLng());

            address = tbAddressRepository.save(address);
        }

        return address;
    }

}
