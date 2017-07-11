package cleanbank.Service;

import cleanbank.domain.TbMemberBillinfo;
import cleanbank.repositories.TbMemberBillinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbMemberBillinfoService implements ITbMemberBillinfoService {
    private TbMemberBillinfoRepository tbMemberBillinfoRepository;

    @Autowired
    public void setTbMemberBillinfoRepository(TbMemberBillinfoRepository tbMemberBillinfoRepository) {
        this.tbMemberBillinfoRepository = tbMemberBillinfoRepository;
    }

    @Override
    public Iterable<TbMemberBillinfo> listAllTbMemberBillinfos() {
        return tbMemberBillinfoRepository.findAll();
    }

    @Override
    public TbMemberBillinfo getTbMemberBillinfoById(Integer id) {
        return tbMemberBillinfoRepository.findOne(id);
    }

    @Override
    public TbMemberBillinfo saveTbMemberBillinfo(TbMemberBillinfo TbMemberBillinfo) {
        return tbMemberBillinfoRepository.save(TbMemberBillinfo);
    }

    @Override
    public void deleteTbMemberBillinfo(Integer id) {
        tbMemberBillinfoRepository.delete(id);
    }

//    이하 추가

/*    @Override
    public void deleteTbMemberBillinfo2(Integer id) {
        TbMemberBillinfo MemberBillinfo = tbMemberBillinfoRepository.findOne(id);
        MemberBillinfo.setDelYn("Y");
        tbMemberBillinfoRepository.save(MemberBillinfo);
    }*/
}
