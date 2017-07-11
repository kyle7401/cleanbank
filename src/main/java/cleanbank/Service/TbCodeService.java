package cleanbank.Service;

import cleanbank.domain.TbCode;
import cleanbank.repositories.TbCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbCodeService implements ITbCodeService {
    private TbCodeRepository tbCodeRepository;

    @Autowired
    public void setTbCodeRepository(TbCodeRepository tbCodeRepository) {
        this.tbCodeRepository = tbCodeRepository;
    }

    /*@PersistenceContext
    private EntityManager entityManager; // 이거 있으면 톰캣 배포시 에러 발생?*/

    @Override
    public Iterable<TbCode> listAllTbCodes() {
        return tbCodeRepository.findAll();
    }

    @Override
    public TbCode getTbCodeById(Integer id) {
        return tbCodeRepository.findOne(id);
    }

    @Override
    public TbCode saveTbCode(TbCode TbCode) {
        return tbCodeRepository.save(TbCode);
    }

    @Override
    public void deleteTbCode(Integer id) {
        tbCodeRepository.delete(id);
    }

//    이하 추가
    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbCode as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

//    상위코드 검색
    @Override
    public List<TbCode> getcdGps() {
        return tbCodeRepository.getcdGps();
    }

    /**
     * 하위코드 검색 : 주문상태
     * @return
     */
    @Override
    public List<TbCode> getCdIts01() {
        return tbCodeRepository.getCdIts("01");
    }

//    하위코드 검색 : 클레임상태
    @Override
    public List<TbCode> getCdIts02() {
        return tbCodeRepository.getCdIts("02");
    }

//    하위코드 검색 : 환불상태
    @Override
    public List<TbCode> getCdIts03() {
        return tbCodeRepository.getCdIts("03");
    }

//    하위코드 검색 : 사고상태
    @Override
    public List<TbCode> getCdIts04() {
        return tbCodeRepository.getCdIts("04");
    }

//    하위코드 검색 : 결제상태
    @Override
    public List<TbCode> getCdIts05() {
        return tbCodeRepository.getCdIts("05");
    }

//    하위코드 검색 : 서비스 상태
    @Override
    public List<TbCode> getCdIts06() {
        return tbCodeRepository.getCdIts("06");
    }

//    하위코드 검색 : 담당업무
    @Override
    public List<TbCode> getCdIts07() {
        return tbCodeRepository.getCdIts("07");
    }

//    하위코드 검색 : 코디등급
    @Override
    public List<TbCode> getCdIts08() {
        return tbCodeRepository.getCdIts("08");
    }

    @Override
    public List<TbCode> getCdIts09() {
        return tbCodeRepository.getCdIts("09");
    }

/*    @Override
    public void deleteTbCode2(Integer id) {
        TbCode employee = tbCodeRepository.findOne(id);

    //        신규 추가시 이메일 중복체크에 걸리는걸 방지하기 위해서 변경한다
        String mgEmail = employee.getEpEmail();

        employee.setEpEmail(mgEmail + "_삭제" + employee.getEpCd());
        employee.setDelYn("Y");

        tbCodeRepository.save(employee);
    }*/

/*    @Override
    public List<TbCode> findByEpEmail(String epEmail) {
        return tbCodeRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbCode> findByBnCd(Integer bnCd) {
        return TbCodeRepository.findByBnCd(bnCd);
    }*/
}
