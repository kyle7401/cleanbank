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
public class FactoryService implements IFactoryService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

//    ----------------------------------------------------------------------------------------------------------------

//    목록 검색
    //    http://antop.tistory.com/30
    public List<?> getStockList(
            final String tac,
            final String from,
            final String to
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

//        주문일자 컬럼 필요
        if(StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

/*        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }*/

        /*if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        } else { // 디폴트로 주문상태가 접수완료(0104), 공장입고(0105)
//            strWhere = SynapseCM.whereConcatenator(strWhere, "(b.IT_STATUS in ('0104', '0105') OR b.IT_STATUS IS NULL)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0104', '0105')");
        }*/

//        상태가 ‘공장입고, 세탁 중’ 인 내역
        strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0105', '0106')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT                                                       ");
        strQuery.append("     b.ID,                                                    ");
        strQuery.append("     DATE_FORMAT(b.IT_IN_DT, '%Y-%m-%d %H:%i') AS IT_IN_DT,   ");
        strQuery.append("     b.IT_TAC,                                                ");
        strQuery.append("     b.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     f.PD_NM,                                                 ");
        strQuery.append("     b.IT_PRICE,                                              ");
        strQuery.append("     e.CD_NM AS IT_STATUS,                                    ");
        strQuery.append("     ifnull(b.IT_CLAIM, 'N') as IT_CLAIM,                                              ");
        strQuery.append("     b.IT_MEMO                                                ");
        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE                                                ");
        strQuery.append(" FROM                                                         ");
        strQuery.append("     TB_ORDER AS a                                            ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                 ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
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
//        strQuery.append(" ORDER BY b.IT_IN_DT ASC ");
//        3월 3일 공장 메뉴는 택번호 오름차순으로
        strQuery.append(" ORDER BY b.IT_TAC ASC ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
//            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
//            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
//            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    public List<?> getProcessList(
            final String tac,
            final String from,
            final String to,
            final String hour48
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
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= DATE_ADD(NOW(), INTERVAL - 48 HOUR)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= NOW()");
        } else {

//        주문일자 컬럼 필요
            if (StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
                ++iCnt;
            }

            if (StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
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

        /*if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        } else { // 디폴트로 주문상태가 접수완료(0104), 공장입고(0105)
//            strWhere = SynapseCM.whereConcatenator(strWhere, "(b.IT_STATUS in ('0104', '0105') OR b.IT_STATUS IS NULL)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0104', '0105')");
        }*/

//        상태가 ‘공장입고’ 인 내역
        strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = '0105'");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT                                                       ");
        strQuery.append("     b.ID,                                                    ");
        strQuery.append("     DATE_FORMAT(b.IT_IN_DT, '%Y-%m-%d %H:%i') AS IT_IN_DT,   ");
        strQuery.append("     b.IT_TAC,                                                ");
        strQuery.append("     b.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     f.PD_NM,                                                 ");
        strQuery.append("     b.IT_PRICE,                                              ");
        strQuery.append("     e.CD_NM AS IT_STATUS,                                    ");
        strQuery.append("     ifnull(b.IT_CLAIM, 'N') as IT_CLAIM,                                              ");
        strQuery.append("     b.IT_MEMO                                                ");
        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE                                                ");
        strQuery.append(" FROM                                                         ");
        strQuery.append("     TB_ORDER AS a                                            ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                 ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
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
//        strQuery.append(" ORDER BY b.IT_IN_DT ASC");
        //        3월 3일 공장 메뉴는 택번호 오름차순으로
        strQuery.append(" ORDER BY b.IT_TAC ASC ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
//            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
//            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
//            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    public List<?> getDeliveryList(
            final String tac,
            final String from,
            final String to,
            final String status
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
/*        if("Y".equals(hour48)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= DATE_ADD(NOW(), INTERVAL - 48 HOUR)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= NOW()");
        } else {*/

//        주문일자 컬럼 필요
            if (StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
                ++iCnt;
            }

            if (StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
                strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.IT_IN_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
                ++iCnt;
            }
//        }

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
        } else { // 디폴트로 상태가 ‘세탁 중’ 인 내역 => 공장입고/세탁중/공장출고
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in ('0105', '0106', '0107')");
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT                                                       ");
        strQuery.append("     b.ID,                                                    ");
        strQuery.append("     DATE_FORMAT(b.IT_IN_DT, '%Y-%m-%d %H:%i') AS IT_IN_DT,   ");
        strQuery.append("     b.IT_TAC,                                                ");
        strQuery.append("     b.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     f.PD_NM,                                                 ");
        strQuery.append("     b.IT_PRICE,                                              ");
        strQuery.append("     e.CD_NM AS IT_STATUS,                                    ");
        strQuery.append("     ifnull(b.IT_CLAIM, 'N') as IT_CLAIM,                                              ");
        strQuery.append("     b.IT_MEMO, b.IT_FACTORY_MEMO ");
        strQuery.append(" FROM                                                         ");
        strQuery.append("     TB_ORDER AS a                                            ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                 ");
        strQuery.append("         LEFT OUTER JOIN                                      ");
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
//        strQuery.append(" ORDER BY b.IT_IN_DT ASC");

        //        3월 3일 공장 메뉴는 택번호 오름차순으로
        strQuery.append(" ORDER BY b.IT_TAC ASC ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(tac)) selectQuery.setParameter("tac", tac); // 택번호
//            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
//            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

/*    @Modifying
    @Transactional
    @Override
    public int changeStatus(final ArrayList<Long> items, final String status) {
        for(int i=0; i<items.size(); i++) {
            log.debug("입고완료/취소 처리 ID = {}", items.get(i).toString());
        }

        Query uptQuery = entityManager.createQuery("update TbOrderItems set itStatus = ?1 where id in (?2)")
                .setParameter(1, status)
                .setParameter(2, items);

        Integer result = uptQuery.executeUpdate();

        return result;
    }*/
}
