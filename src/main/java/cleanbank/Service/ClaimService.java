package cleanbank.Service;

import cleanbank.utils.SynapseCM;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
@Service
public class ClaimService implements IClaimService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;
    //    ----------------------------------------------------------------------------------------------------------------

    @Override
    public List<?> getClaimList(
            final String tac,
            final String from,
            final String to,
            final String keyword,
            final String condition
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(tac)) { // 택번호
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_TAC = :tac");
            ++iCnt;
        }

//        주문일자 컬럼 필요
        if (StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if (StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

/*        if(StringUtils.isNotEmpty(keyword)) { // 검색어(품목명)
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }*/

        strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_CLAIM = 'Y'");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append("     SELECT                                                     ");
        strQuery.append("     a.OR_CD AS orCd,                                           ");
        strQuery.append("     b.ID,                                                      ");
        strQuery.append("     CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'),               ");
        strQuery.append("             LPAD(a.OR_CD, 4, '0')) AS OR_CD2,                  ");
        strQuery.append("     DATE_FORMAT(b.IT_CLAIM_DT, '%Y-%m-%d %H:%i') AS IT_CLAIM_DT, ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS IT_REGI_DT,    ");
        strQuery.append("     b.IT_TAC,                                                  ");
        strQuery.append("     f.PD_NM,                                                   ");
        strQuery.append("     c.MB_EMAIL, c.MB_NIC_NM, c.MB_TEL,                                               ");
        strQuery.append("     e.EP_NM AS EP_CD,                                          ");
        strQuery.append("     h.EP_NM AS DELI_EP,                                        ");
        strQuery.append("     g.CD_NM AS IT_STATUS,                                      ");
        strQuery.append("     b.IT_MEMO                                                  ");
        strQuery.append(" FROM                                                           ");
        strQuery.append("     TB_ORDER AS a                                              ");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                   ");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                        ");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD                      ");

//        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("         JOIN                                        ");
        strQuery.append("     (SELECT                                                    ");
        strQuery.append("         PD_LVL1, PD_LVL2, PD_LVL3, PD_NM                       ");
        strQuery.append("     FROM                                                       ");
        strQuery.append("         TB_PRODUCT                                             ");
        strQuery.append("     WHERE                                                      ");
        strQuery.append("         (DEL_YN IS NULL OR DEL_YN != 'Y') ");

        if(StringUtils.isNotEmpty(keyword) && "PD_NM".equals(condition)) { // 검색어(품목명)
            strQuery.append("      AND PD_NM LIKE :pd_nm ");
            ++iCnt;
        }

        strQuery.append("     ) AS f ON CONCAT(b.PD_LVL1, b.PD_LVL2, b.PD_LVL3) = CONCAT(f.PD_LVL1, f.PD_LVL2, f.PD_LVL3)");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     (SELECT                                                    ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                    ");
        strQuery.append("     FROM                                                       ");
        strQuery.append("         TB_CODE                                                ");
        strQuery.append("     WHERE                                                      ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00'                         ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS = CONCAT(g.CD_GP, g.CD_IT)");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     TB_EMPLOYEE AS h ON a.OR_DELI_EP_CD = h.EP_CD              ");
    /*    strQuery.append(" WHERE                                                          ");
        strQuery.append("     b.IT_CLAIM = 'Y'                                           ");
        strQuery.append("         AND (a.DEL_YN IS NULL OR a.DEL_YN != 'Y')              ");
        strQuery.append("         AND (b.DEL_YN IS NULL OR b.DEL_YN != 'Y')              ");*/

        strQuery.append(strWhere);

//        정렬
        strQuery.append(" ORDER BY b.IT_CLAIM_DT DESC , b.REGI_DT DESC                   ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(keyword) && "PD_NM".equals(condition)) selectQuery.setParameter("pd_nm",  "%"+ keyword +"%"); // 검색어(품목명)
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}
