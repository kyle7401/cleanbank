package cleanbank.Service;

import cleanbank.domain.TbPartMember;
import cleanbank.repositories.TbPartMemberRepository;
import cleanbank.utils.SynapseCM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbPartMemberService implements ITbPartMemberService {
    /*private TbPartMemberRepository tbPartMemberRepository;

    @Autowired
    public void setTbPartMemberRepository(TbPartMemberRepository tbPartMemberRepository) {
        this.tbPartMemberRepository = tbPartMemberRepository;
    }*/

    @Autowired
    private TbPartMemberRepository tbPartMemberRepository;

    @Override
    public Iterable<TbPartMember> listAllTbPartMembers() {
        return tbPartMemberRepository.findAll();
    }

    @Override
    public TbPartMember getTbPartMemberById(Integer id) {
        return tbPartMemberRepository.findOne(id);
    }

    @Override
    public TbPartMember saveTbPartMember(TbPartMember tbPartMember) {

        if("new".equals(tbPartMember.getMode())) { // 신규
            tbPartMember.setRegiDt(new Date());
            tbPartMember.setUser(SynapseCM.getAuthenticatedUserID());

//            신규 비밀번호 변경
            String password = tbPartMember.getPmPass();

            if(StringUtils.isNotEmpty(password)) {
                String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
                tbPartMember.setPmPass(hashedPassword);
            }
        } else { // 수정
            //        비밀번호 변경
            String password = tbPartMember.getNewPass();

            if(StringUtils.isNotEmpty(password)) {
                String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
                tbPartMember.setPmPass(hashedPassword);
            }
        }

        return tbPartMemberRepository.save(tbPartMember);
    }

    @Override
    public void deleteTbPartMember(Integer id) {
        tbPartMemberRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbPartMember2(Integer id) {
        TbPartMember product = tbPartMemberRepository.findOne(id);
        product.setDelYn("Y");
        tbPartMemberRepository.save(product);
    }

    @Override
    public TbPartMember findByPmEmail(final String pmEmail) {
        return tbPartMemberRepository.findByPmEmail(pmEmail);
    }

    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPartMember> getPdLvl1s() {
        return tbPartMemberRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPartMember> getPdLvl2s() {
        return tbPartMemberRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbPartMember as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbPartMemberRepository.getcdGps();
    }*/

/*    @Override
    public List<TbPartMember> findByEpEmail(String epEmail) {
        return tbPartMemberRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbPartMember> findByBnCd(Integer bnCd) {
        return TbPartMemberRepository.findByBnCd(bnCd);
    }*/
}
