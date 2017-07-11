package cleanbank.Service;

import cleanbank.domain.TbLocation;
import cleanbank.domain.TbPromoLocation;
import cleanbank.repositories.TbLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbLocationService implements ITbLocationService {
    private TbLocationRepository tbLocationRepository;

    @Autowired
    public void setTbLocationRepository(TbLocationRepository tbLocationRepository) {
        this.tbLocationRepository = tbLocationRepository;
    }

    @Override
    public Iterable<TbLocation> listAllTbLocations() {
        return tbLocationRepository.findAll();
    }

    @Override
    public TbLocation getTbLocationById(Integer id) {
        return tbLocationRepository.findOne(id);
    }

    @Override
    public TbLocation saveTbLocation(TbLocation tbManager) {
        return tbLocationRepository.save(tbManager);
    }

    @Override
    public void deleteTbLocation(Integer id) {
        tbLocationRepository.delete(id);
    }

//    이하 추가
    @Override
    public void deleteTbLocation2(Integer id) {
        TbLocation manager = tbLocationRepository.findOne(id);
        manager.setDelYn("Y");
        tbLocationRepository.save(manager);
    }

    @Override
    public List<TbPromoLocation> getPlCd1s() {
        return tbLocationRepository.getPlCd1s();
    }

    @Override
    public List<TbPromoLocation> getPlCd2s() {
        return tbLocationRepository.getPlCd2s();
    }

    @Override
    public List<TbPromoLocation> getPlCd2s(final String plCd1) {
        return tbLocationRepository.getPlCd2s(plCd1);
    }

    @Override
    public List<?> getLocationCds() {
        return tbLocationRepository.getLocationCds();
    }

}
