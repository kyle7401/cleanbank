package cleanbank.Service;

import cleanbank.domain.TbManager;
import cleanbank.repositories.TbManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbManagerService implements ITbManagerService {
    private TbManagerRepository tbManagerRepository;

    @Autowired
    public void setTbManagerRepository(TbManagerRepository tbManagerRepository) {
        this.tbManagerRepository = tbManagerRepository;
    }

    @Override
    public Iterable<TbManager> listAllTbManagers() {
        return tbManagerRepository.findAll();
    }

    @Override
    public TbManager getTbManagerById(Integer id) {
        return tbManagerRepository.findOne(id);
    }

    @Override
    public TbManager saveTbManager(TbManager tbManager) {
        return tbManagerRepository.save(tbManager);
    }

    @Override
    public void deleteTbManager(Integer id) {
        tbManagerRepository.delete(id);
    }

//    이하 추가
    /*@Override
    public List<TbManager> findByMgEmail(String mgEmail) {
        return tbManagerRepository.findByMgEmail(mgEmail);
    }*/

    @Override
    public TbManager findByMgEmail(String mgEmail) {
        return tbManagerRepository.findByMgEmail(mgEmail);
    }

    @Override
    public void deleteTbManager2(Integer id) {
        TbManager manager = tbManagerRepository.findOne(id);

//        신규 추가시 이메일 중복체크에 걸리는걸 방지하기 위해서 변경한다
        String mgEmail = manager.getMgEmail();

        manager.setMgEmail(mgEmail +"_삭제"+ manager.getId());
        manager.setDelYn("Y");

        tbManagerRepository.save(manager);
    }

    @Override
    public List<TbManager> findByBnCd(Integer bnCd) {
        return tbManagerRepository.findByBnCd(bnCd);
    }

    public TbManager findByMgEmailAndMgType(String mgEmail, String mgType) {
        return tbManagerRepository.findByMgEmailAndMgType(mgEmail, mgType);
    }
}
