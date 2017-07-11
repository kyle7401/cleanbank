package cleanbank.Service;

import cleanbank.domain.TbPromoLocation;
import cleanbank.repositories.TbPromoLocationRepository;
import cleanbank.utils.SynapseCM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbPromoLocationService implements ITbPromoLocationService {
    private TbPromoLocationRepository tbPromoLocationRepository;

    @Autowired
    public void setTbPromoLocationRepository(TbPromoLocationRepository tbPromoLocationRepository) {
        this.tbPromoLocationRepository = tbPromoLocationRepository;
    }

    @Override
    public Iterable<TbPromoLocation> listAllTbPromoLocations() {
        return tbPromoLocationRepository.findAll();
    }

    @Override
    public TbPromoLocation getTbPromoLocationById(Long id) {
        return tbPromoLocationRepository.findOne(id);
    }

    @Override
    public TbPromoLocation saveTbPromoLocation(TbPromoLocation promolocation) {

        //        상/중/하 지역코드를 6자리로 합침
        promolocation.setPlCd(promolocation.getLoc1() + promolocation.getLoc2() + promolocation.getLoc3());

        if("new".equals(promolocation.getMode())) { // 신규
            promolocation.setRegiDt(new Date());
            promolocation.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        return tbPromoLocationRepository.save(promolocation);
    }

    @Override
    public void deleteTbPromoLocation(Long id) {
        tbPromoLocationRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbPromoLocation2(Long id) {
        TbPromoLocation PromoLocation = tbPromoLocationRepository.findOne(id);
        PromoLocation.setDelYn("Y");
        tbPromoLocationRepository.save(PromoLocation);
    }

/*    @Override
    public List<?> getPromoLocationCds(Long mbCd) {
        return tbPromoLocationRepository.getPromoLocationCds(mbCd);
    }*/


    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPromoLocation> getPdLvl1s() {
        return tbPromoLocationRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPromoLocation> getPdLvl2s() {
        return tbPromoLocationRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbPromoLocation as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbPromoLocationRepository.getcdGps();
    }*/

/*    @Override
    public List<TbPromoLocation> findByEpEmail(String epEmail) {
        return tbPromoLocationRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbPromoLocation> findByBnCd(Integer bnCd) {
        return TbPromoLocationRepository.findByBnCd(bnCd);
    }*/
}
