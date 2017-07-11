package cleanbank.Service;

import cleanbank.domain.TbEmployee;
import cleanbank.repositories.TbEmployeeRepository;
import cleanbank.utils.SynapseCM;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbEmployeeService implements ITbEmployeeService {
    private TbEmployeeRepository tbEmployeeRepository;

    @Autowired
    public void setTbEmployeeRepository(TbEmployeeRepository tbEmployeeRepository) {
        this.tbEmployeeRepository = tbEmployeeRepository;
    }

    @Override
    public Iterable<TbEmployee> listAllTbEmployees() {
        return tbEmployeeRepository.findAll();
    }

    @Override
    public TbEmployee getTbEmployeeById(Integer id) {
        return tbEmployeeRepository.findOne(id);
    }

    @Override
    public TbEmployee saveTbEmployee(TbEmployee TbEmployee) {
        return tbEmployeeRepository.save(TbEmployee);
    }

    @Override
    public void deleteTbEmployee(Integer id) {
        tbEmployeeRepository.delete(id);
    }

//    이하 추가
    /*@Override
    public List<TbEmployee> findByEpEmail(String epEmail) {
        return tbEmployeeRepository.findByEpEmail(epEmail);
    }*/
    @Override
    public TbEmployee findByEpEmail(String epEmail) {
        return tbEmployeeRepository.findByEpEmail(epEmail);
    }

    @Override
    public void deleteTbEmployee2(Integer id) {
        TbEmployee employee = tbEmployeeRepository.findOne(id);

//        신규 추가시 이메일 중복체크에 걸리는걸 방지하기 위해서 변경한다
        String mgEmail = employee.getEpEmail();

        employee.setEpEmail(mgEmail + "_삭제" + employee.getEpCd());
        employee.setDelYn("Y");

        tbEmployeeRepository.save(employee);
    }

    @Override
    public List<?> getEmpCds() {
        return tbEmployeeRepository.getEmpCds();
    }

    @Autowired
    private EntityManager em;

    @Override
    public List<?> getList(String bncd, String keyword, String condition, String epPart) {
        //        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(epPart)) { // 담당업무
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.EP_PART = :part");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.EP_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.EP_TEL like :keyword");
            }
            ++iCnt;
        }

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                                                                 ");
        strQuery.append("     a.EP_CD AS epCd,                                                                                   ");
        strQuery.append("     e.BN_NM AS bnCd,                                                                                   ");
        strQuery.append("     EP_IMG AS epImg,                                                                                   ");
        strQuery.append("     EP_NM AS epNm,                                                                                     ");
        strQuery.append("     CASE                                                                                               ");
        strQuery.append("         WHEN a.EP_SEX = 'M' THEN '남'                                                                  ");
        strQuery.append("         WHEN a.EP_SEX = 'F' THEN '여'                                                                  ");
        strQuery.append("     END AS epSex,                                                                                      ");
        strQuery.append("     EP_TEL AS epTel,                                                                                   ");
        strQuery.append("     c.CD_NM AS epPart,                                                                                 ");
        strQuery.append("     f.NAME AS epLoc,                                                                                   ");
        strQuery.append("     DATE_FORMAT(a.EP_JOIN_DT, '%Y-%m-%d') AS epJoinDt,                                                 ");
        strQuery.append("     d.CD_NM AS epLevel,                                                                                ");
        strQuery.append("     a.EP_EMAIL, g.CD_NM AS EP_DRIVE_YN                                                                                           ");
        strQuery.append(" FROM                                                                                                   ");
        strQuery.append("     TB_EMPLOYEE AS a                                                                                   ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     TB_BRANCH AS b ON a.BN_CD = b.BN_CD                                                                ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     (SELECT                                                                                            ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                                                            ");
        strQuery.append("     FROM                                                                                               ");
        strQuery.append("         TB_CODE                                                                                        ");
        strQuery.append("     WHERE                                                                                              ");
        strQuery.append("         CD_GP = '07' AND CD_IT != '00'                                                                 ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS c ON a.EP_PART = CONCAT(c.CD_GP, c.CD_IT)        ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     (SELECT                                                                                            ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                                                            ");
        strQuery.append("     FROM                                                                                               ");
        strQuery.append("         TB_CODE                                                                                        ");
        strQuery.append("     WHERE                                                                                              ");
        strQuery.append("         CD_GP = '08' AND CD_IT != '00'                                                                 ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS d ON a.EP_LEVEL = CONCAT(d.CD_GP, d.CD_IT)       ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     (SELECT                                                                                            ");
        strQuery.append("         BN_CD, BN_NM                                                                                   ");
        strQuery.append("     FROM                                                                                               ");
        strQuery.append("         TB_BRANCH                                                                                      ");
        strQuery.append("     WHERE                                                                                              ");
        strQuery.append("         DEL_YN IS NULL OR DEL_YN != 'Y') AS e ON a.BN_CD = e.BN_CD                                     ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     (SELECT                                                                                            ");
        strQuery.append("         LOC1, LOC2, LOC3, NAME                                                                         ");
        strQuery.append("     FROM                                                                                               ");
        strQuery.append("         TB_LOCATION                                                                                    ");
        strQuery.append("     WHERE                                                                                              ");
        strQuery.append("         LOC2 != '00' AND LOC3 != '00'                                                                  ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')                                                      ");
        strQuery.append("     ORDER BY NAME) AS f ON a.EP_LOC = CONCAT(f.LOC1, f.LOC2, f.LOC3)                                   ");
        strQuery.append("         LEFT OUTER JOIN                                                                                ");
        strQuery.append("     (SELECT                                                                                            ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                                                            ");
        strQuery.append("     FROM                                                                                               ");
        strQuery.append("         TB_CODE                                                                                        ");
        strQuery.append("     WHERE                                                                                              ");
        strQuery.append("         CD_GP = '09' AND CD_IT != '00'                                                                 ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON a.EP_DRIVE_YN = CONCAT(g.CD_GP, g.CD_IT)    ");

        strQuery.append(strWhere);

        strQuery.append(" ORDER BY a.EP_JOIN_DT DESC ");

        Query selectQuery = em.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(epPart)) selectQuery.setParameter("part", epPart); // 담당업무
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}
