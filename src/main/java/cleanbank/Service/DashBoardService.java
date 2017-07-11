package cleanbank.Service;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hyoseop on 2015-12-08.
 */
@Service
public class DashBoardService implements IDashBoardService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 수거요청현황
     * @return
     */
    @Override
    public List<?> getTakeReqList() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                              ");
        strQuery.append("     c.MB_NIC_NM,                                                    ");
        strQuery.append("     DATE_FORMAT(b.IT_VISIT_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,    ");
        strQuery.append("     b.IT_REQ_ADDR,                                                  ");
        strQuery.append("     d.EP_NM AS EP_CD                                                ");
        strQuery.append(" FROM                                                                ");
        strQuery.append("     TB_ORDER AS a                                                   ");
        strQuery.append("         LEFT OUTER JOIN                                             ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                        ");
        strQuery.append("         LEFT OUTER JOIN                                             ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                             ");
        strQuery.append("         LEFT OUTER JOIN                                             ");
        strQuery.append("     TB_EMPLOYEE AS d ON a.EP_CD = d.EP_CD                           ");
        strQuery.append(" WHERE                                                               ");
        strQuery.append("     b.IT_STATUS = '0102'                                            ");
        strQuery.append(" ORDER BY b.IT_VISIT_DT DESC;                                        ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    /**
     * 배송요청현황
     * @return
     */
    @Override
    public List<?> getDeliReqList() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                                 ");
        strQuery.append("     c.MB_NIC_NM,                                                       ");
        strQuery.append("     DATE_FORMAT(b.IT_VISIT_DT, '%Y-%m-%d %H:%i') AS IT_VISIT_DT,       ");
        strQuery.append("     b.IT_REQ_ADDR,                                                     ");
        strQuery.append("     d.EP_NM AS EP_CD,                                                  ");
        strQuery.append("     a.OR_CNT,                                                          ");
        strQuery.append("     a.OR_CHARGE, a.OR_CHARGE_TYPE                                                        ");
        strQuery.append(" FROM                                                                   ");
        strQuery.append("     TB_ORDER AS a                                                      ");
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                           ");
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_MEMBER AS c ON a.MB_CD = c.MB_CD                                ");
        strQuery.append("         LEFT OUTER JOIN                                                ");
        strQuery.append("     TB_EMPLOYEE AS d ON a.EP_CD = d.EP_CD                              ");
        strQuery.append(" WHERE                                                                  ");
        strQuery.append("     b.IT_STATUS = '0107'                                               ");
        strQuery.append(" ORDER BY b.IT_VISIT_DT                                             ");
//        strQuery.append(" ORDER BY b.IT_VISIT_DT DESC                                            ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    /**
     * 전체현황
     * @return
     */
    @Override
    public Object getCntByStatus() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                           ");
        strQuery.append("     IFNULL(SUM(CASE b.IT_STATUS                  ");
        strQuery.append("                 WHEN '0101' THEN 1               ");
        strQuery.append("             END),                                ");
        strQuery.append("             0) AS CNT1,                          ");
        strQuery.append("     IFNULL(SUM(CASE b.IT_STATUS                  ");
        strQuery.append("                 WHEN '0103' THEN 1               ");
        strQuery.append("             END),                                ");
        strQuery.append("             0) AS CNT2,                          ");
        strQuery.append("     IFNULL(SUM(CASE b.IT_STATUS                  ");
        strQuery.append("                 WHEN '0104' THEN 1               ");
        strQuery.append("             END),                                ");
        strQuery.append("             0) AS CNT3,                          ");
        strQuery.append("     IFNULL(SUM(CASE WHEN b.IT_STATUS = '0105' AND b.IT_IN_DT > DATE_ADD(NOW(), INTERVAL - 48 HOUR) THEN 1               ");
        strQuery.append("             END),0) AS CNT4,                          ");
        strQuery.append("     IFNULL(SUM(CASE b.IT_STATUS                  ");
        strQuery.append("                 WHEN '0108' THEN 1               ");
        strQuery.append("             END),                                ");
        strQuery.append("             0) AS CNT5                           ");
        strQuery.append(" FROM                                             ");
        strQuery.append("     TB_ORDER AS a                                ");
        strQuery.append("         LEFT OUTER JOIN                          ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD     ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());

        /*List<?> list = selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list.get(0);*/

        Object list = selectQuery.unwrap(SQLQuery.class).uniqueResult();
        return list;
    }

//    전주 대비 금일 현황
    @Override
    public Object[] getStatus1(final String from, final String to) {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                        ");
        strQuery.append("     COUNT(1) AS tot,                                          ");
        strQuery.append("     COUNT(DISTINCT (DATE(b.REGI_DT))) AS cnt,                 ");
        strQuery.append("     (SELECT                                                   ");
        strQuery.append("             COUNT(1)                                          ");
        strQuery.append("         FROM                                                  ");
        strQuery.append("             TB_ORDER AS a                                     ");
        strQuery.append("                 LEFT OUTER JOIN                               ");
        strQuery.append("             TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD          ");
        strQuery.append("         WHERE                                                 ");
        strQuery.append("             DATE(b.REGI_DT) = DATE(NOW())) AS today           ");
        strQuery.append(" FROM                                                          ");
        strQuery.append("     TB_ORDER AS a                                             ");
        strQuery.append("         LEFT OUTER JOIN                                       ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                  ");
        strQuery.append(" WHERE ");
//        strQuery.append("     DATE(b.REGI_DT) BETWEEN DATE(DATE_ADD(NOW(), INTERVAL - 7 DAY)) AND DATE(DATE_ADD(NOW(), INTERVAL - 1 DAY)) ");
        strQuery.append("     DATE(b.REGI_DT) BETWEEN DATE(str_to_date(:from,'%Y/%m/%d')) AND DATE(str_to_date(:to,'%Y/%m/%d')) ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());
        selectQuery.setParameter("from", from);
        selectQuery.setParameter("to", to);

        Object[] list = (Object[]) selectQuery.unwrap(SQLQuery.class).uniqueResult();
        return list;
    }

//    전월 대비 금일 현황
    @Override
    public Object[] getStatus2() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                         ");
        strQuery.append("     COUNT(1) AS tot, COUNT(DISTINCT (DATE(b.REGI_DT))) AS cnt  ");
        strQuery.append(" FROM                                                           ");
        strQuery.append("     TB_ORDER AS a                                              ");
        strQuery.append("         LEFT OUTER JOIN                                        ");
        strQuery.append("     TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                   ");
        strQuery.append(" WHERE                                                          ");
        strQuery.append("     DATE(b.REGI_DT) BETWEEN DATE(DATE_ADD(NOW(), INTERVAL - 31 DAY)) AND DATE(DATE_ADD(NOW(), INTERVAL - 1 DAY))");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());
        Object[] list = (Object[]) selectQuery.unwrap(SQLQuery.class).uniqueResult();
        return list;
    }

//    전주 대비 금일 누적 현황
    @Override
    public Object[] getStatus3(final String from, final String to, final String today) {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                                                                          ");
        strQuery.append("     (SELECT                                                                                                     ");
        strQuery.append("             COUNT(1) AS cnt1                                                                                    ");
        strQuery.append("         FROM                                                                                                    ");
        strQuery.append("             TB_ORDER AS a                                                                                       ");
        strQuery.append("                 LEFT OUTER JOIN                                                                                 ");
        strQuery.append("             TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                                                            ");
        strQuery.append("         WHERE                                                                                                   ");
        strQuery.append("             DATE(b.REGI_DT) BETWEEN DATE(STR_TO_DATE(:today, '%Y/%m/%d')) AND DATE(NOW())) AS cnt1,       ");
        strQuery.append("     (SELECT                                                                                                     ");
        strQuery.append("             COUNT(1) AS cnt2                                                                                    ");
        strQuery.append("         FROM                                                                                                    ");
        strQuery.append("             TB_ORDER AS a                                                                                       ");
        strQuery.append("                 LEFT OUTER JOIN                                                                                 ");
        strQuery.append("             TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD                                                            ");
        strQuery.append("         WHERE                                                                                                   ");
        strQuery.append("             DATE(b.REGI_DT) BETWEEN DATE(STR_TO_DATE(:from, '%Y/%m/%d')) AND DATE(STR_TO_DATE(:to, '%Y/%m/%d'))) AS cnt2 ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());
        selectQuery.setParameter("today", today);
        selectQuery.setParameter("from", from);
        selectQuery.setParameter("to", to);

        Object[] list = (Object[]) selectQuery.unwrap(SQLQuery.class).uniqueResult();
        return list;
    }

//    http://www.coderanch.com/t/385759/java/java/date-date-month
    @Override
    public Object[] getStatus4() {

        String Start = null;
        String From = null;
        String To = null;
        Integer Days1 = 0;
        Integer Days2 = 0;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        Calendar cal = Calendar.getInstance ();

        Days1 = cal.get(Calendar.DAY_OF_MONTH);
        log.debug("오늘까지 날짜수 = {}", Days1);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Start = df.format(cal.getTime());
        log.debug("이번달 시작일자 = {}", Start);

//        지난달
        cal.add ( cal.MONTH, -1 );

        cal.set(Calendar.DAY_OF_MONTH, 1);
        From = df.format(cal.getTime());
        log.debug("지난달 시작일자 = {}", From);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        To = df.format(cal.getTime());
        log.debug("지난달 종료일자 = {}", To);

        Days2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        log.debug("지난달 날짜수 = {}", Days2);

//        log.debug("cal = {}", df.format(cal.getTime()));
//        log.debug("지난달 시작일자 = {}", df.format(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                  ");
        strQuery.append("     (SELECT                                             ");
        strQuery.append("             COUNT(1)                                    ");
        strQuery.append("         FROM                                            ");
        strQuery.append("             TB_ORDER AS a                               ");
        strQuery.append("                 LEFT OUTER JOIN                         ");
        strQuery.append("             TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD    ");
        strQuery.append("         WHERE                                           ");
        strQuery.append("             DATE(b.REGI_DT) BETWEEN DATE(STR_TO_DATE(:from1, '%Y/%m/%d')) AND DATE(STR_TO_DATE(:to, '%Y/%m/%d'))) AS cnt1, ");

        strQuery.append("         :day1 AS day1,                                           ");

        strQuery.append("     (SELECT                                             ");
        strQuery.append("             COUNT(1) AS cnt1                            ");
        strQuery.append("         FROM                                            ");
        strQuery.append("             TB_ORDER AS a                               ");
        strQuery.append("                 LEFT OUTER JOIN                         ");
        strQuery.append("             TB_ORDER_ITEMS AS b ON a.OR_CD = b.OR_CD    ");
        strQuery.append("         WHERE                                           ");
        strQuery.append("             DATE(b.REGI_DT) BETWEEN DATE(STR_TO_DATE(:from2, '%Y/%m/%d')) AND DATE(NOW())) AS cnt2, ");

        strQuery.append("         :day2 AS day2                                           ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString());
        selectQuery.setParameter("from1", From);
        selectQuery.setParameter("to", To);
        selectQuery.setParameter("day1", Days2);

        selectQuery.setParameter("from2", Start);
        selectQuery.setParameter("day2", Days1);

        Object[] list = (Object[]) selectQuery.unwrap(SQLQuery.class).uniqueResult();
        return list;
    }
}
