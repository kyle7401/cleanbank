package cleanbank.Service;

import cleanbank.domain.TbPartner;
import cleanbank.viewmodel.PartnerSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPartnerService {
    Iterable<TbPartner> listAllTbPartners();

    TbPartner getTbPartnerById(Integer id);

    TbPartner saveTbPartner(TbPartner TbPartner);

    void deleteTbPartner(Integer id);

    //    이하 추가
    void deleteTbPartner2(Integer id);

    List<Object> getPartnerCds();

    Iterable<TbPartner> List(final PartnerSearch partnerSearch, final Pageable pageable);

    Iterable<TbPartner> List2(final PartnerSearch partnerSearch, final Pageable pageable);

/*    List<TbPartner> getPdLvl1s();

    List<TbPartner> getPdLvl2s();*/

//    List<String> getcdGps();

//    List<TbPartner> findByEpEmail(String epEmail);
//    List<TbPartner> findByBnCd(Integer bnCd);
}
