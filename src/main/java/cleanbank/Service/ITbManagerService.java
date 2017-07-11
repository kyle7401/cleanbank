package cleanbank.Service;

import cleanbank.domain.TbManager;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbManagerService {
    Iterable<TbManager> listAllTbManagers();

    TbManager getTbManagerById(Integer id);

    TbManager saveTbManager(TbManager TbManager);

    void deleteTbManager(Integer id);

    //    이하 추가
//    List<TbManager> findByMgEmail(String mgEmail);
    TbManager findByMgEmail(String mgEmail);

    void deleteTbManager2(Integer id);

    List<TbManager> findByBnCd(Integer bnCd);

    TbManager findByMgEmailAndMgType(String mgEmail, String mgType);
}
