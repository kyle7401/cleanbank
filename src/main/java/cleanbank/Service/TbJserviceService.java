package cleanbank.Service;

import cleanbank.domain.TbJservice;
import cleanbank.repositories.TbJserviceRepository;
import cleanbank.utils.SynapseCM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbJserviceService implements ITbJserviceService {
    private TbJserviceRepository tbJserviceRepository;

    @Autowired
    public void setTbJserviceRepository(TbJserviceRepository tbJserviceRepository) {
        this.tbJserviceRepository = tbJserviceRepository;
    }

    @Override
    public Iterable<TbJservice> listAllTbJservices() {
        return tbJserviceRepository.findAll();
    }

    @Override
    public TbJservice getTbJserviceById(Long id) {
        return tbJserviceRepository.findOne(id);
    }

    @Override
    public TbJservice saveTbJservice(TbJservice faq) {

        if("new".equals(faq.getMode())) { // 신규
            faq.setRegiDt(new Date());
            faq.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        return tbJserviceRepository.save(faq);
    }

    @Override
    public void deleteTbJservice(Long id) {
        tbJserviceRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbJservice2(Long id) {
        TbJservice Jservice = tbJserviceRepository.findOne(id);
        Jservice.setDelYn("Y");
        tbJserviceRepository.save(Jservice);
    }

    /*@Override
    public List<?> getJserviceCds() {
        return tbJserviceRepository.getJserviceCds();
    }*/

    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbJservice> getPdLvl1s() {
        return tbJserviceRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbJservice> getPdLvl2s() {
        return tbJserviceRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbJservice as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbJserviceRepository.getcdGps();
    }*/

/*    @Override
    public List<TbJservice> findByEpEmail(String epEmail) {
        return tbJserviceRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbJservice> findByBnCd(Long bnCd) {
        return TbJserviceRepository.findByBnCd(bnCd);
    }*/
}
