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
 * Created by hyoseop on 2015-12-09.
 */
@Service
public class DeliveryService implements IDeliveryService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;
    //    ----------------------------------------------------------------------------------------------------------------

    @Override
    public List<?> getDeliveryList(
            final String tac,
            final String from,
            final String to,
            final String hour48,
            final String status,
            final String keyword, final String condition
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(tac)) { // 택번호
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_TAC = :tac");
            ++iCnt;
        }

        /*if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }*/

//        48시간은 “공장입고” 상태변경 후 48시간이상이 된 내역들을 대상으로 한다.
        if("Y".equals(hour48)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_OUT_DT <= DATE_ADD(NOW(), INTERVAL - 48 HOUR)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_OUT_DT <= NOW()");
        } else {

//        주문일자 컬럼 필요
            if (StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_OUT_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
                ++iCnt;
            }

            if (StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_OUT_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
                ++iCnt;
            }

        }

/*        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }*/

        if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        }/* else { // 디폴트로 주문상태가 공장출고,백민입고
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0107', '0108')");
        }*/

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("MB_NIC_NM".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else if("MB_TEL".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            } else if("MB_ADDR1".equals(condition)) {
//                strWhere = SynapseCM.whereConcatenator(strWhere, "f.MB_ADDR1 like :keyword");
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_REQ_ADDR IN (SELECT ID FROM TB_ADDRESS WHERE MB_ADDR1 LIKE :keyword)");
            }/* else if("IT_TAC".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_CD IN (SELECT OR_CD FROM TB_ORDER_ITEMS WHERE IT_TAC LIKE :keyword)");
            }*/
            ++iCnt;
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT                                                       ");
        strQuery.append("     b.ID,                                                    ");
        strQuery.append("     DATE_FORMAT(b.IT_OUT_DT, '%Y-%m-%d %H:%i') AS IT_OUT_DT,   ");
        strQuery.append("     b.IT_TAC,                                                ");
        strQuery.append("     b.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     f.PD_NM,                                                 ");
        strQuery.append("     b.IT_PRICE,                                              ");
        strQuery.append("     e.CD_NM AS IT_STATUS,                                    ");
        strQuery.append("     ifnull(b.IT_CLAIM, 'N') as IT_CLAIM,                                              ");
        strQuery.append("     b.IT_MEMO                                                ");
        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE, c.MB_NIC_NM, b.IT_FACTORY_MEMO ");
        strQuery.append(" FROM                                                         ");
        strQuery.append("     TB_ORDER AS a                                            ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                 ");

//        2월 22일 닉네임 추가
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("        TB_MEMBER AS c           ");
        strQuery.append("            ON a.MB_CD = c.MB_CD      ");

        strQuery.append("    LEFT OUTER JOIN                   ");
        strQuery.append("     (SELECT                                                  ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                                  ");
        strQuery.append("     FROM                                                     ");
        strQuery.append("         TB_CODE                                              ");
        strQuery.append("     WHERE                                                    ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00'                       ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS e ON b.IT_STATUS = CONCAT(e.CD_GP, e.CD_IT) ");
        strQuery.append("         LEFT OUTER JOIN                                       ");
        strQuery.append("     (SELECT                                                   ");
        strQuery.append("         PD_LVL1, PD_LVL2, PD_LVL3, PD_NM                      ");
        strQuery.append("     FROM                                                      ");
        strQuery.append("         TB_PRODUCT                                            ");
        strQuery.append("     WHERE                                                     ");
        strQuery.append("         DEL_YN IS NULL OR DEL_YN != 'Y') AS f ON CONCAT(b.PD_LVL1, b.PD_LVL2, b.PD_LVL3) = CONCAT(f.PD_LVL1, f.PD_LVL2, f.PD_LVL3) ");

        strQuery.append(strWhere);

//        최신 수거완료 일시 순으로 리스트를 정렬
        strQuery.append(" ORDER BY b.IT_OUT_DT ASC");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
//            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
//            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)

            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}
