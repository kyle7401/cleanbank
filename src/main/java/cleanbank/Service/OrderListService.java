package cleanbank.Service;

import cleanbank.domain.TbOrder;
import cleanbank.utils.SynapseCM;
import cleanbank.viewmodel.TbOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-09.
 */
@Service
public class OrderListService implements IOrderListService {

    @Autowired
    private EntityManager entitymanager;

//    ---------------------------------------------------------------------------------------------------------------------

//    수거 시간표 요일별 TO 확인용
    @Override
    public List<TbOrder> getOrderList() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT * ");
        /*strQuery.append("     DATE(NOW()) AS dtFrom,                                                                   ");
        strQuery.append("     DATE(DATE_ADD(NOW(), INTERVAL + 4 DAY)) AS dtTo,                                         ");
        strQuery.append("     OR_REQ_DT                                                                                ");*/
        strQuery.append(" FROM                                                                                         ");
        strQuery.append("     TB_ORDER                                                                                 ");
        strQuery.append(" WHERE                                                                                        ");
        strQuery.append("     DATE(OR_REQ_DT) BETWEEN DATE(NOW()) AND DATE(DATE(DATE_ADD(NOW(), INTERVAL + 4 DAY)))    ");
        strQuery.append("     AND (DEL_YN IS NULL OR DEL_YN <> 'Y') ");
        strQuery.append(" ORDER BY OR_REQ_DT                                                                           ");

        Query selectQuery = entitymanager.createNativeQuery(strQuery.toString());

        List<TbOrder> list = selectQuery.unwrap(SQLQuery.class)
//                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .addEntity(TbOrder.class)
                .list();

        return list;
    }

    @Override
    public List<?> getTotalList(String bncd, String from, String to, String keyword, String condition, String status) {
        return this.getList(bncd, from, to, keyword, condition, status, null, null, null);
    }

    @Override
    public List<?> getTotalList2(String bncd, String from, String to, String keyword, String condition, String status, String status2) {
//        return this.getList(bncd, from, to, keyword, condition, status, status2, null, null);
        return this.getTotalList5(bncd, from, to, keyword, condition, status, null, status2);
    }

    @Override
    public List<?> getTotalList3(String bncd, String from, String to, String keyword, String condition, String status, String status2, String hour48, final String OrderBy) {

//        주문조회-백민입고예정현황 : 48시간은 공장입고기준(IT_IN_DT)
        if("Y".equals(hour48)) hour48 = "Y2";

        return this.getList(bncd, from, to, keyword, condition, status, status2, hour48, OrderBy);
    }

    @Override
    public List<?> getTotalList4(String bncd, String from, String to, String keyword, String condition, String status, final String OrderBy) {
//        return this.getList(bncd, from, to, keyword, condition, status, null, null, OrderBy);
        return this.getTotalList5(bncd, from, to, keyword, condition, status, OrderBy, null);
    }

    private List<?> getList(final String bncd, final String from, final String to, final String keyword, final String condition, final String status,
                            final String status2, final String hour48, final String OrderBy) {
        //        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

//        48시간은 “백민입고” 상태변경 후 48시간이상이 된 내역들을 대상으로 한다.
        if("Y".equals(hour48)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_BAEK_IN_DT <= DATE_ADD(NOW(), INTERVAL - 48 HOUR)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_BAEK_IN_DT <= NOW()");
        }

//        주문조회-백민입고예정현황 : 48시간은 공장입고기준(IT_IN_DT)
        if("Y2".equals(hour48)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= DATE_ADD(NOW(), INTERVAL - 48 HOUR)");
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_IN_DT <= NOW()");
        }

//        주문일자 컬럼 필요
        if(StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(b.REGI_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            }
            ++iCnt;
        }

//        가용코디현황은 주문상태가 주문접수/수거중/수거완료를 디폴트로 검색
        if(StringUtils.isNotEmpty(status2)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS in "+ status2);
        } else if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "b.IT_STATUS = :status");
            ++iCnt;
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

//        접수처리 검색
        strQuery.append(" SELECT  a.OR_CD AS orCd, b.ID,                                                       ");

/*
1.금일기준으로 주문 상태가 주문접수/수거 중일 때 수거요청일시가 지난 주문은 붉은색으로 표시된다.
2.금일기준으로 주문 상태가 배송 완료가 아닐 때 배송예정일시가 지난 주문은 붉은색으로 표시된다.
*/
        /*strQuery.append("    IF((b.IT_REQ_DT IS NOT NULL                        ");
        strQuery.append("            AND b.IT_REQ_DT < NOW())                   ");
        strQuery.append("            AND (b.IT_STATUS IN ('0101' , '0102')),    ");
        strQuery.append("        'R',                                           ");
        strQuery.append("        NULL) AS red1,                                 ");
        strQuery.append("    IF((b.IT_VISIT_DT IS NOT NULL                      ");
        strQuery.append("            AND b.IT_VISIT_DT < NOW())                 ");
        strQuery.append("            AND (b.IT_STATUS IN ('0110')),             ");
        strQuery.append("        'R',                                           ");
        strQuery.append("        NULL) AS red2,                                 ");*/

        strQuery.append("     DATE_FORMAT(b.IT_VISIT_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,      ");
        strQuery.append("     DATE_FORMAT(b.REGI_DT, '%Y-%m-%d %H:%i') AS IT_REGI_DT,        ");
        strQuery.append("     a.OR_CD, CONCAT(DATE_FORMAT(b.REGI_DT, '%y%m%d%H%i'), LPAD(a.OR_CD, 4, '0')) AS OR_CD2, ");
        strQuery.append("     d.BN_NM,                                                    ");
        strQuery.append("     c.MB_NIC_NM,                                                ");
        strQuery.append("     c.MB_TEL,                                                   ");

//        strQuery.append("     MB_ADDR AS IT_REQ_ADDR,                                     ");
        strQuery.append("    CASE                                                         ");
        strQuery.append("        WHEN MB_ADDR1 IS NULL AND MB_ADDR2 IS NULL THEN NULL     ");
        strQuery.append("        WHEN                                                     ");
        strQuery.append("            MB_ADDR1 IS NOT NULL                                 ");
        strQuery.append("                AND MB_ADDR2 IS NULL                             ");
        strQuery.append("        THEN                                                     ");
        strQuery.append("            MB_ADDR1                                             ");
        strQuery.append("        WHEN                                                     ");
        strQuery.append("            MB_ADDR1 IS NOT NULL                                 ");
        strQuery.append("                AND MB_ADDR2 IS NOT NULL                         ");
        strQuery.append("        THEN                                                     ");
        strQuery.append("            MB_ADDR1 + ' ' + MB_ADDR1                            ");
        strQuery.append("    END AS IT_REQ_ADDR,                                          ");

        strQuery.append("     b.IT_CNT,                                                   ");
        strQuery.append("     e.EP_NM AS EP_CD, h.EP_NM AS DELI_EP,                                          ");
        strQuery.append("     b.IT_MEMO,                                                  ");

//        strQuery.append("     g.CD_NM as IT_STATUS, b.IT_PRICE, a.OR_REQ_ADDR                                                 ");
        strQuery.append("     g.CD_NM as IT_STATUS, b.IT_PRICE, f.MB_ADDR1 as OR_REQ_ADDR                                                 ");

        strQuery.append("     , IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE                                                ");

//        2015-01-04 추가
        strQuery.append("     , DATE_FORMAT(b.IT_REQ_DT, '%Y-%m-%d %H:%i') AS IT_REQ_DT, a.OR_MEMO      ");
        strQuery.append("     , DATE_FORMAT(b.IT_OUT_DT, '%Y-%m-%d %H:%i') AS IT_OUT_DT      ");
        strQuery.append("     , DATE_FORMAT(b.IT_BAEK_IN_DT, '%Y-%m-%d %H:%i') AS IT_BAEK_IN_DT      ");
        strQuery.append("     , b.IT_IN_DT      ");

        strQuery.append(" FROM                                                            ");
        strQuery.append("     TB_ORDER AS a                                               ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                    ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                         ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_BRANCH AS d ON a.BN_CD = d.BN_CD                         ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD                       ");
        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_ADDRESS AS f ON b.IT_REQ_ADDR = f.ID                     ");

        strQuery.append("        LEFT OUTER JOIN                  ");
        strQuery.append("    (SELECT                              ");
        strQuery.append("        CD_GP, CD_IT, CD_NM              ");
        strQuery.append("    FROM                                 ");
        strQuery.append("        TB_CODE                          ");
        strQuery.append("    WHERE                                ");
        strQuery.append("        CD_GP = '01' AND CD_IT != '00'   ");
        strQuery.append("            AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");

        strQuery.append("         LEFT OUTER JOIN                                         ");
        strQuery.append("     TB_EMPLOYEE AS h ON a.OR_DELI_EP_CD = h.EP_CD                       ");

        strQuery.append(strWhere);

//        2015-01-04 "주문전체 현황" 화면에서 주문일시 역순 정렬로 수정 : 다른 화면에서 영향 받는지 확인 필요
        if(!StringUtils.isEmpty(OrderBy)) {
            strQuery.append(OrderBy);
//            strQuery.append(" ORDER BY b.REGI_DT DESC ");
        } else {
            //        최신 수거완료 일시 순으로 리스트를 정렬
            strQuery.append(" ORDER BY b.IT_REGI_DT DESC ");
        }

        Query selectQuery = entitymanager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<TbOrderVO> list = (List<TbOrderVO>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

//    TODO : 주문 기준으로 검색
//    1월 15일 주문전체 현황은 주문 기준으로 검색
    public List<?> getTotalList5(final String bncd, final String from, final String to, final String keyword, final String condition, final String status
            , final String OrderBy, final String status2) {
        //        검색조건
        int iCnt = 0;
        String strWhere = "";

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

//        주문일자 컬럼 필요
        if(StringUtils.isNotEmpty(from)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(a.REGI_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(to)) { // 주문기간(시작일)
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(a.REGI_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition) || "MB_NIC_NM".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_NIC_NM like :keyword");
            } else if("T".equals(condition) || "MB_TEL".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "c.MB_TEL like :keyword");
            } else if("MB_ADDR1".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "f.MB_ADDR1 like :keyword");
            } else if("IT_TAC".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_CD IN (SELECT OR_CD FROM TB_ORDER_ITEMS WHERE IT_TAC LIKE :keyword)");
            }
            ++iCnt;
        }

        /*if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_STATUS = :status");
            ++iCnt;
        }*/

        //        가용코디현황은 주문상태가 주문접수/수거중/수거완료를 디폴트로 검색
        if(StringUtils.isNotEmpty(status2)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_STATUS in "+ status2);
        } else if(StringUtils.isNotEmpty(status)) { // 주문상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.OR_STATUS = :status");
            ++iCnt;
        }

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");
//        strWhere = SynapseCM.whereConcatenator(strWhere, "(b.DEL_YN IS NULL OR b.DEL_YN != 'Y')");

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                          ");
        strQuery.append("     a.REGI_DT,                                                  ");
        strQuery.append("     CONCAT(DATE_FORMAT(a.REGI_DT, '%y%m%d%H%i'),                ");
        strQuery.append("             LPAD(a.OR_CD, 4, '0')) AS OR_CD3,                   ");
        strQuery.append("     d.BN_NM,                                                    ");
        strQuery.append("     c.MB_NIC_NM,                                                ");
        strQuery.append("     c.MB_TEL,                                                   ");
        strQuery.append("     e.EP_NM AS EP_CD,                                           ");

//        strQuery.append("     OR_DELI_EP_CD AS DELI_EP,                                   ");
        strQuery.append("     h.EP_NM AS DELI_EP,                                   ");

//        strQuery.append("     IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0) AS TOT_PRICE,   ");

        strQuery.append(" IFNULL((SELECT                                                              ");
        strQuery.append("                     SUM(IFNULL(IT_PRICE, 0) + IFNULL(IT_ADD_CHG, 0))        ");
        strQuery.append("                 FROM                                                        ");
        strQuery.append("                     TB_ORDER_ITEMS AS x                                     ");
        strQuery.append("                 WHERE                                                       ");
        strQuery.append("                     a.OR_CD = x.OR_CD),                                     ");
        strQuery.append("             0) AS TOT_PRICE,                                                ");

//        2월 22일 접수수량은 품목수량으로 표시
//        strQuery.append("     b.IT_CNT,                                                   ");
        strQuery.append("        (SELECT                             ");
        strQuery.append("            COUNT(1)                        ");
        strQuery.append("        FROM                                ");
        strQuery.append("            TB_ORDER_ITEMS AS x             ");
        strQuery.append("        WHERE                               ");
        strQuery.append("            a.OR_CD = x.OR_CD) as IT_CNT,   ");

        strQuery.append("     g.CD_NM AS IT_STATUS,                                       ");
//        strQuery.append("     b.IT_MEMO,                                                   ");
        strQuery.append("     a.OR_CD as orCd,                                              ");
/*
1.금일기준으로 주문 상태가 주문접수/수거 중일 때 수거요청일시가 지난 주문은 붉은색으로 표시된다.
2.금일기준으로 주문 상태가 배송 완료가 아닐 때 배송예정일시가 지난 주문은 붉은색으로 표시된다.
*/
/*        strQuery.append("     IF((b.IT_REQ_DT IS NOT NULL                             ");
        strQuery.append("             AND b.IT_REQ_DT < NOW())                        ");
        strQuery.append("             AND (b.IT_STATUS IN ('0101' , '0102')),         ");
        strQuery.append("         'R',                                                ");
        strQuery.append("         NULL) AS red1,                                      ");
        strQuery.append("     IF((b.IT_VISIT_DT IS NOT NULL                           ");
        strQuery.append("             AND b.IT_VISIT_DT < NOW())                      ");
        strQuery.append("             AND (b.IT_STATUS IN ('0110')),                  ");
        strQuery.append("         'R',                                                ");
        strQuery.append("         NULL) AS red2,                                       ");*/
        strQuery.append(" a.OR_REQ_DT, f.MB_ADDR1, a.OR_MEMO, a.OR_VISIT_DT, ");

        strQuery.append(" (SELECT ");
        strQuery.append(" MAX(IT_BAEK_IN_DT) ");
        strQuery.append(" FROM ");
        strQuery.append(" TB_ORDER_ITEMS AS x ");
        strQuery.append("         WHERE ");
        strQuery.append(" a.OR_CD = x.OR_CD) AS IT_BAEK_IN_DT ");

        strQuery.append(" FROM                                               ");
        strQuery.append("     TB_ORDER AS a                                  ");
        /*strQuery.append("         LEFT OUTER JOIN                            ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD       ");*/
        strQuery.append("         LEFT OUTER JOIN                            ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD            ");
        strQuery.append("         LEFT OUTER JOIN                            ");
        strQuery.append("     TB_BRANCH AS d ON a.BN_CD = d.BN_CD            ");
        strQuery.append("         LEFT OUTER JOIN                            ");
        strQuery.append("     TB_EMPLOYEE AS e ON a.EP_CD = e.EP_CD          ");
        strQuery.append("         LEFT OUTER JOIN                            ");

//        strQuery.append("     TB_ADDRESS AS f ON b.IT_REQ_ADDR = f.ID        ");
        strQuery.append("     TB_ADDRESS AS f ON a.OR_REQ_ADDR = f.ID        ");

        strQuery.append("         LEFT OUTER JOIN                            ");
        strQuery.append("     (SELECT                                        ");
        strQuery.append("         CD_GP, CD_IT, CD_NM                        ");
        strQuery.append("     FROM                                           ");
        strQuery.append("         TB_CODE                                    ");
        strQuery.append("     WHERE                                          ");
        strQuery.append("         CD_GP = '01' AND CD_IT != '00'             ");
//        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON a.OR_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");

//        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON b.IT_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");
        strQuery.append("             AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS g ON a.OR_STATUS = CONCAT(g.CD_GP, g.CD_IT) ");

        strQuery.append("         LEFT OUTER JOIN                            ");

//        strQuery.append("     TB_EMPLOYEE AS h ON a.EP_CD = h.EP_CD          ");
        strQuery.append("     TB_EMPLOYEE AS h ON a.OR_DELI_EP_CD = h.EP_CD          "); // 2월 13일 h 사용하는 곳이 없어 OR_DELI_EP_CD 로 변경


        strQuery.append(strWhere);

        if(StringUtils.isEmpty(OrderBy)) {
            strQuery.append(" ORDER BY a.REGI_DT DESC ");
        } else {
            strQuery.append(OrderBy);
        }


        Query selectQuery = entitymanager.createNativeQuery(strQuery.toString());

        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 주문상태
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 주문기간(시작일)
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 주문기간(종료일)
        }

        List<TbOrderVO> list = (List<TbOrderVO>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}
