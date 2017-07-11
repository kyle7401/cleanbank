package cleanbank.Service;

import cleanbank.domain.TbBranch;
import cleanbank.repositories.TbBranchRepository;
import cleanbank.utils.SynapseCM;
import cleanbank.viewmodel.CodeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbBranchService implements ITbBranchService {
    private TbBranchRepository tbBranchRepository;

    @Autowired
    public void setTbBranchRepository(TbBranchRepository tbBranchRepository) {
        this.tbBranchRepository = tbBranchRepository;
    }

    @Autowired
    private SynapseCM synapseCM;

    @Override
    public Iterable<TbBranch> listAllTbBranchs() {
        return tbBranchRepository.findAll();
    }

    @Override
    public TbBranch getTbBranchById(Integer id) {
        return tbBranchRepository.findOne(id);
    }

    @Override
    public TbBranch saveTbBranch(TbBranch TbBranch) {
        return tbBranchRepository.save(TbBranch);
    }

    @Override
    public void deleteTbBranch(Integer id) {
        tbBranchRepository.delete(id);
    }

//    이하 추가

//    지점의 바코드 시작번호 얻기
    @Override
    public String getBnBarCd(final Integer id) {
        if(StringUtils.isEmpty(id)) return  null;

        TbBranch tbBranch = this.getTbBranchById(id);
        return tbBranch.getBnBarCd();
    }

    /**
     * 택번호 나중에 계산
     * @param id
     * @return
     */
    @Override
    public String setBnBarCd(final Integer id) {
        if(id == null) return null;
        TbBranch tbBranch = this.getTbBranchById(id);

        String bnBarCd = tbBranch.getBnBarCd();
        long lngBarCd = Long.parseLong(bnBarCd);
        ++lngBarCd;
        tbBranch.setBnBarCd(String.valueOf(lngBarCd));

        tbBranchRepository.save(tbBranch);

        return tbBranch.getBnBarCd();
    }

    @Override
    public String setBnBarCd(final Integer id, String itTac) {
        if(id == null) return null;
        if(itTac == null) return null;

        TbBranch tbBranch = this.getTbBranchById(id);
        tbBranch.setBnBarCd(itTac);

        String bnBarCd = tbBranch.getBnBarCd();
        long lngBarCd = Long.parseLong(bnBarCd);
        ++lngBarCd;
        tbBranch.setBnBarCd(String.valueOf(lngBarCd));

        tbBranchRepository.save(tbBranch);

        return tbBranch.getBnBarCd();
    }

    @Override
    public void deleteTbBranch2(Integer id) {
        TbBranch branch = tbBranchRepository.findOne(id);
        branch.setDelYn("Y");
        tbBranchRepository.save(branch);
    }

    /**
     * - .’지점’ 검색 조건은 로그인된 사용자 소속 지점으로  고정, 본사의 경우 ‘지점’을 변경 가능
     *
     *
     * @return
     */
    @Override
    public List<?> getBranchCds() {
//        return tbBranchRepository.getBranchCds();

//        List<CodeValue> branchCds = tbBranchRepository.getBranchCds();
        List<CodeValue> branchCds = null;

        String authority = synapseCM.getAuthorityString();
        Integer userBnCd = null;

        switch (authority) {
            case "ROLE_ADMIN":
                branchCds = tbBranchRepository.getBranchCds();
                break;

            case "ROLE_BRANCH":
                userBnCd = synapseCM.getAuthenticatedBnCd();
                branchCds = tbBranchRepository.getBranchCds(userBnCd);
                break;

            case "ROLE_CODI":
                userBnCd = synapseCM.getAuthenticatedBnCd();
                branchCds = tbBranchRepository.getBranchCds(userBnCd);
                break;

            case "ROLE_FACTORY":
                userBnCd = synapseCM.getAuthenticatedBnCd();
                branchCds = tbBranchRepository.getBranchCds();
                break;
        }

        return branchCds;
    }


    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbBranch> getPdLvl1s() {
        return tbBranchRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbBranch> getPdLvl2s() {
        return tbBranchRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbBranch as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbBranchRepository.getcdGps();
    }*/

/*    @Override
    public List<TbBranch> findByEpEmail(String epEmail) {
        return tbBranchRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbBranch> findByBnCd(Integer bnCd) {
        return TbBranchRepository.findByBnCd(bnCd);
    }*/
}
