package cleanbank.Service;

import cleanbank.domain.TbFaq;
import cleanbank.repositories.TbFaqRepository;
import cleanbank.utils.SynapseCM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbFaqService implements ITbFaqService {
    private TbFaqRepository tbFaqRepository;

    @Autowired
    public void setTbFaqRepository(TbFaqRepository tbFaqRepository) {
        this.tbFaqRepository = tbFaqRepository;
    }

    @Override
    public Iterable<TbFaq> listAllTbFaqs() {
        return tbFaqRepository.findAll();
    }

    @Override
    public TbFaq getTbFaqById(Integer id) {
        return tbFaqRepository.findOne(id);
    }

    @Override
    public TbFaq saveTbFaq(TbFaq faq) {

        if("new".equals(faq.getMode())) { // 신규
            faq.setRegiDt(new Date());
            faq.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        return tbFaqRepository.save(faq);
    }

    @Override
    public void deleteTbFaq(Integer id) {
        tbFaqRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbFaq2(Integer id) {
        TbFaq Faq = tbFaqRepository.findOne(id);
        Faq.setDelYn("Y");
        tbFaqRepository.save(Faq);
    }

    /*@Override
    public List<?> getFaqCds() {
        return tbFaqRepository.getFaqCds();
    }*/

    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbFaq> getPdLvl1s() {
        return tbFaqRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbFaq> getPdLvl2s() {
        return tbFaqRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbFaq as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbFaqRepository.getcdGps();
    }*/

/*    @Override
    public List<TbFaq> findByEpEmail(String epEmail) {
        return tbFaqRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbFaq> findByBnCd(Integer bnCd) {
        return TbFaqRepository.findByBnCd(bnCd);
    }*/
}
