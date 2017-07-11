package cleanbank.Service;

import cleanbank.domain.*;
import cleanbank.repositories.TbMemberRepository;
import cleanbank.repositories.TbPointRepository;
import cleanbank.repositories.TbPromotionRepository;
import cleanbank.repositories.TbPromotionUseRepository;
import cleanbank.utils.SynapseCM;
import cleanbank.viewmodel.MoOrder3;
import cleanbank.viewmodel.MoPoint;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbMemberService implements ITbMemberService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private TbPointRepository tbPointRepository;

    @Autowired
    private PushMobileService pushMobileService;

    @Autowired
    private ITbPromotionService tbPromotionService;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    @Autowired
    private ITbPointService tbPointService;

    @Autowired
    private TbPromotionRepository tbPromotionRepository;

    /*@Autowired
    public void setTbMemberRepository(TbMemberRepository tbMemberRepository) {
        this.tbMemberRepository = tbMemberRepository;
    }*/

    @Override
    public Iterable<TbMember> listAllTbMembers() {
        return tbMemberRepository.findAll();
    }

    @Override
    public TbMember getTbMemberById(Integer id) {
        return tbMemberRepository.findOne(id);
    }

    public TbOrder setChargeInfo(MoOrder3 moOrder3, TbOrder tbOrder) {
        //        결재정보 저장
        tbOrder.setOrCharge(moOrder3.getOrCharge());

//        포인트
        if(org.springframework.util.StringUtils.isEmpty(moOrder3.getOrPoint())) {
            tbOrder.setOrPoint(moOrder3.getOrPoint());

            MoPoint moPoint = new MoPoint();
            moPoint.setMbCd(tbOrder.getMbCd());
            moPoint.setOrCd(tbOrder.getOrCd());
            moPoint.setPoint(moOrder3.getOrPoint());
            moPoint.setPlUseMemo("주문결제");

            this.setMbrPoint(moPoint);
        }

        //        쿠폰 사용내역 저장
        if(moOrder3.getPoCds().size() > 0) {
            for(Integer poCd : moOrder3.getPoCds()) {
                TbPromotionUse tbPromotionUse = new TbPromotionUse();
                tbPromotionUse.setOrCd(tbOrder.getOrCd());
                tbPromotionUse.setUseYn("Y");
                tbPromotionUse.setPuUseDt(new Date());
                tbPromotionUse.setEvtNm("수정");
                tbPromotionUse.setPuUse("주문결제");

                tbPromotionUse = tbPromotionUseRepository.save(tbPromotionUse);
            }
        }

        return tbOrder;
    }

    @Override
    public TbMember saveTbMember(TbMember tbMember) {

        if("new".equals(tbMember.getMode())) { // 신규
            tbMember.setRegiDt(new Date());

//            api에서 호출시 에러 발생
            try {
                tbMember.setUser(SynapseCM.getAuthenticatedUserID());
            } catch (Exception e) {}

            tbMember.setEvtNm("추가");

            tbMember.setMbPoint(1000); // 첫 가입 시, 1000포인트가 기본적으로 주어지고 푸시메세지 발송

        } else { // 수정
            tbMember.setEvtNm("수정");
        }

        if(!org.springframework.util.StringUtils.hasText(tbMember.getMbMycode())) {
            String mbMycode = this.createMbCode();
            tbMember.setMbMycode(mbMycode);
        }

//        return tbMemberRepository.save(tbMember);
        TbMember SaveMember = tbMemberRepository.save(tbMember);

        if("new".equals(tbMember.getMode())) { // 신규가입 1000 포인트 푸시 메세지

            //        포인트 적립내역 저장
            TbPoint tbPoint = new TbPoint();

            tbPoint.setMbCd(SaveMember.getMbCd());
            tbPoint.setPiPoint(1000);
            tbPoint.setPiUseDt(new Date());
            tbPoint.setPlUseMemo("신규가입 포인트 적립");

            tbPointService.savePointUse(tbPoint);

            try {
                pushMobileService.sendJoinPoint(SaveMember);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("신규가입 포인트 푸시 메세지 에러 {}", e);
            }

//            신규가입 2000 쿠폰 저장
            log.error("신규가입 쿠폰 푸시발송 시작!============================================================");
            TbPromotionUse tbPromotionUse = new TbPromotionUse();
            tbPromotionUse.setMbCd(tbMember.getMbCd());
            tbPromotionUse.setDelYn("N");
            tbPromotionUse.setEvtNm("신규");
            tbPromotionUse.setRegiDt(new Date());
            tbPromotionUse.setUser("모바일");

            TbPromotion promotion = tbPromotionRepository.findOne((long) 1); // 신규가입 2000 쿠폰(고정)
            tbPromotionUse.setTbPromotion(promotion);
            tbPromotionUse = tbPromotionUseRepository.save(tbPromotionUse);

            try {
//                pushMobileService.sendPromotion(promotion); // 이러면 모든 회원에게 발송됨??
                pushMobileService.sendJoinPromotion(SaveMember, promotion);
                log.error("신규가입 쿠폰 푸시발송 끝!============================================================");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("신규가입 쿠폰 푸시발송 에러 {}", e);
            }

        }

        return SaveMember;
    }

//    회원코드 작성
    public String createMbCode() {

        //실행시 ???개 쿠폰 생성
//        int couponSize = 20;
        int iLen = 18;
        String couponnum = null;
        boolean isOk = true;

        final char[] possibleCharacters =
                {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F',
                        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                        'W', 'X', 'Y', 'Z'};

        final int possibleCharacterCount = possibleCharacters.length;
//        String[] arr = new String[couponSize];
        Random rnd = new Random();
//        int currentIndex = 0;
//        int i = 0;

        while (isOk) {

//        while (currentIndex < couponSize) {
        StringBuffer buf = new StringBuffer(iLen);
        //i는 8자리의 랜덤값을 의미
//            for (i = 8; i > 0; i--) {
            for (int i = iLen; i > 0; i--) {
                buf.append(possibleCharacters[rnd.nextInt(possibleCharacterCount)]);
            }
//            String couponnum = buf.toString();
            couponnum = buf.toString();
//            System.out.println("couponnum==>" + couponnum);
            log.debug("회원코드==> {}", couponnum);
//            arr[currentIndex] = couponnum;
//            currentIndex++;

//            코드가 중복되는지 확인
            TbMember tbMember = tbMemberRepository.findByMbMycode(couponnum);
            if(tbMember == null) break;
        }

        return couponnum;
    }

    @Override
    public void deleteTbMember(Integer id) {
        tbMemberRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbMember2(Integer id) {
        TbMember member = tbMemberRepository.findOne(id);
        member.setDelYn("Y");
        tbMemberRepository.save(member);
    }

    @Override
    public List<TbMember> listAllMembers() {
        return tbMemberRepository.listAllMembers();
    }

    @Autowired
    private EntityManager em;

    @Override
    public List<?> getList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    ) {

//        검색조건
        int iCnt = 0;
        String strWhere = "";

        strWhere = SynapseCM.whereConcatenator(strWhere, "(a.DEL_YN IS NULL OR a.DEL_YN != 'Y')");

        if(StringUtils.isNotEmpty(bncd)) { // 지점
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.BN_CD = :bncd");
            ++iCnt;
        }

        // 가입일자
        if (StringUtils.isNotEmpty(from)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(a.MB_JOIN_DT) >= STR_TO_DATE(:from, '%Y-%m-%d')");
            ++iCnt;
        }

        if (StringUtils.isNotEmpty(to)) {
            strWhere = SynapseCM.whereConcatenator(strWhere, "DATE(a.MB_JOIN_DT) <= STR_TO_DATE(:to, '%Y-%m-%d')");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(status)) { // 서비스 상태
            strWhere = SynapseCM.whereConcatenator(strWhere, "a.MB_STATUS = :status");
            ++iCnt;
        }

        if(StringUtils.isNotEmpty(keyword)) { // 검색어
            if("N".equals(condition)) {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.MB_NIC_NM like :keyword");
            } else {
                strWhere = SynapseCM.whereConcatenator(strWhere, "a.MB_TEL like :keyword");
            }
            ++iCnt;
        }

        StringBuilder strQuery = new StringBuilder();

        strQuery.append("    SELECT                                                                                            ");
        strQuery.append("    a.MB_CD AS mbCd,                                                                                  ");
        strQuery.append("    b.BN_NM AS bnNm,                                                                                  ");
        strQuery.append("    a.MB_NIC_NM,                                                                                      ");
        strQuery.append("    CASE                                                                                              ");
        strQuery.append("        WHEN a.MB_LEVEL = '1' THEN 'Gold'                                                             ");
        strQuery.append("        WHEN a.MB_LEVEL = '2' THEN 'Silver'                                                           ");
        strQuery.append("        WHEN a.MB_LEVEL = '3' THEN 'Green'                                                            ");
        strQuery.append("    END AS mbLevel,                                                                                   ");
        strQuery.append("    a.MB_EMAIL AS mbEmail,                                                                            ");
        strQuery.append("    a.MB_NIC_NM AS mbNicNm,                                                                           ");
        strQuery.append("    a.MB_TEL AS mbTel,                                                                                ");
        strQuery.append("    DATE_FORMAT(a.MB_JOIN_DT, '%Y-%m-%d') AS mbJoinDt,                                                ");
        strQuery.append("    d.CD_NM AS mbStatus,                                                                              ");
        strQuery.append("    DATE_FORMAT(a.MB_BIRTH, '%Y/%m/%d') AS MB_BIRTH,                                                  ");
        strQuery.append("    CASE a.MB_PATH                                                                                    ");
        strQuery.append("        WHEN 'W' THEN '웹'                                                                            ");
        strQuery.append("        WHEN 'A' THEN '앱'                                                                            ");
        strQuery.append("        ELSE '-'                                                                                      ");
        strQuery.append("    END AS MB_PATH,                                                                                   ");
        strQuery.append("    e.MB_ADDR,                                                                                        ");
        strQuery.append("    DATE_FORMAT(f.regi_dt, '%Y/%m/%d') AS LAST_BUY                                                                             ");
        strQuery.append("FROM                                                                                                  ");
        strQuery.append("    TB_MEMBER AS a                                                                                    ");
        strQuery.append("        LEFT OUTER JOIN                                                                               ");
        strQuery.append("    TB_BRANCH AS b ON a.BN_CD = b.BN_CD                                                               ");
        strQuery.append("        LEFT OUTER JOIN                                                                               ");
        strQuery.append("    (SELECT                                                                                           ");
        strQuery.append("        CD_GP, CD_IT, CD_NM                                                                           ");
        strQuery.append("    FROM                                                                                              ");
        strQuery.append("        TB_CODE                                                                                       ");
        strQuery.append("    WHERE                                                                                             ");
        strQuery.append("        CD_GP = '06' AND CD_IT != '00'                                                                ");
        strQuery.append("            AND (DEL_YN IS NULL OR DEL_YN != 'Y')) AS d ON a.MB_STATUS = CONCAT(d.CD_GP, d.CD_IT)     ");
        strQuery.append("        LEFT OUTER JOIN                                                                               ");
        strQuery.append("    (SELECT                                                                                           ");
        strQuery.append("        MB_CD, MAX(MB_ADDR) AS MB_ADDR                                                                                ");
        strQuery.append("    FROM                                                                                              ");
        strQuery.append("        (SELECT                                                                                       ");

//        strQuery.append("        MB_CD, ID, MB_ADDR, REGI_DT                                                                   ");
        strQuery.append("        MB_CD, ID, REGI_DT,                                                                   ");
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
        strQuery.append("    END AS MB_ADDR                                          ");

        strQuery.append("    FROM                                                                                              ");
        strQuery.append("        TB_ADDRESS                                                                                    ");
        strQuery.append("    ORDER BY MB_CD , ID DESC) AS t                                                                    ");
        strQuery.append("    GROUP BY MB_CD) AS e ON a.MB_CD = e.MB_CD                                                         ");
        strQuery.append("        LEFT OUTER JOIN                                                                               ");
        strQuery.append("    (SELECT                                                                                           ");
        strQuery.append("        mb_cd, MAX(REGI_DT) AS REGI_DT                                                                                ");
        strQuery.append("    FROM                                                                                              ");
        strQuery.append("        (SELECT                                                                                       ");
        strQuery.append("        mb_cd, regi_dt                                                                                ");
        strQuery.append("    FROM                                                                                              ");
        strQuery.append("        cleanbank.TB_ORDER                                                                              ");
        strQuery.append("    ORDER BY mb_cd , regi_dt DESC) AS t                                                               ");
        strQuery.append("    GROUP BY mb_cd) AS f ON a.MB_CD = f.MB_CD                                                         ");

        strQuery.append(strWhere);

//        가입일자 기준 내림차순 정렬
        strQuery.append("    ORDER BY a.MB_JOIN_DT desc");

        Query selectQuery = em.createNativeQuery(strQuery.toString());


        if(iCnt > 0) {
            if(StringUtils.isNotEmpty(bncd)) selectQuery.setParameter("bncd", bncd); // 지점
            if(StringUtils.isNotEmpty(from)) selectQuery.setParameter("from", from); // 가입일
            if(StringUtils.isNotEmpty(to)) selectQuery.setParameter("to", to); // 가입일
            if(StringUtils.isNotEmpty(keyword)) selectQuery.setParameter("keyword", "%"+ keyword +"%"); // 검색어(닉네임, 연락처)
            if(StringUtils.isNotEmpty(status)) selectQuery.setParameter("status", status); // 서비스 상태
        }

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return list;
    }

//    고객관리 상단 고객현황
    @Override
    public Object getStatistic() {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                      ");
        strQuery.append("     COUNT(1) AS tot,                                        ");
        strQuery.append("     (SELECT                                                 ");
        strQuery.append("             COUNT(1)                                        ");
        strQuery.append("         FROM                                                ");
        strQuery.append("             TB_MEMBER                                       ");
        strQuery.append("         WHERE                                               ");
        strQuery.append("             DEL_YN = 'Y') AS leave_cnt,                     ");
        strQuery.append("     SUM(IF(MB_LEVEL = '1', 1, 0)) AS gold,                  ");
        strQuery.append("     SUM(IF(MB_LEVEL = '2', 1, 0)) AS silver,                ");
        strQuery.append("     SUM(IF(MB_LEVEL = '3', 1, 0)) AS green,                 ");
        strQuery.append("     (SELECT                                                 ");
        strQuery.append("             COUNT(1)                                        ");
        strQuery.append("         FROM                                                ");
        strQuery.append("             TB_MEMBER m                                     ");
        strQuery.append("         WHERE                                               ");
        strQuery.append("             NOT EXISTS( SELECT                              ");
        strQuery.append("                     NULL                                    ");
        strQuery.append("                 FROM                                        ");
        strQuery.append("                     TB_ORDER o                              ");
        strQuery.append("                 WHERE                                       ");
        strQuery.append("                     m.MB_CD = o.MB_CD)) AS not_order        ");
        strQuery.append(" FROM                                                        ");
        strQuery.append("     TB_MEMBER AS m                                          ");
        strQuery.append(" WHERE                                                       ");
        strQuery.append("     DEL_YN IS NULL OR DEL_YN != 'Y'                         ");

        Query selectQuery = em.createNativeQuery(strQuery.toString());
        Object statistic = selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .uniqueResult();

        return statistic;
    }

    @Override
    public ResponseEntity<?> setMbrPoint(MoPoint moPoint) {
        //        주문정보 확인
        TbOrder tbOrder = tbOrderService.getTbOrderById(moPoint.getOrCd());
        if (tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//        회원정보 확인
        TbMember mbr = tbMemberRepository.findOne(moPoint.getMbCd());
        if(mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

//        회원 포인트 차감
        Integer mbPoint = mbr.getMbPoint();

        if(mbPoint != null && mbPoint > 0 && mbPoint >= moPoint.getPoint()) {
            mbPoint -= moPoint.getPoint();
            mbr.setMbPoint(mbPoint);
            tbMemberRepository.save(mbr);

//            사용내역 등록
            TbPoint tbPoint = new TbPoint();

            tbPoint.setMbCd(moPoint.getMbCd());
            tbPoint.setPiPoint(moPoint.getPoint());
            tbPoint.setPiUseDt(new Date());

            if(StringUtils.isEmpty(moPoint.getPlUseMemo()))
            {
                tbPoint.setPlUseMemo("포인트 결제");
            } else {
                tbPoint.setPlUseMemo(moPoint.getPlUseMemo());
            }

            tbPoint.setDelYn("N");
            tbPoint.setEvtNm("신규");
            tbPoint.setRegiDt(new Date());
            tbPoint.setUser("모바일");

            tbPointRepository.save(tbPoint);

            return new ResponseEntity<>(tbPoint, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("보유 포인트가 부족합니다!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public List<?> getPointList(Integer mbcd) {

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                      ");
        strQuery.append("     IFNULL(PL_USE_MEMO, PI_CHARGE) AS plUseMemo,            ");
        strQuery.append("     IFNULL(PI_USE_DT, PI_CHA_DT) AS piUseDt,                ");
        strQuery.append("     IFNULL(PI_POINT, 0) AS point,                           ");
        strQuery.append("     CONCAT(CASE                                             ");
        strQuery.append("                 WHEN PI_POINT IS NOT NULL AND PI_POINT > 0 and PL_USE_MEMO like '%적립%' THEN '+'       ");
        strQuery.append("                 WHEN PI_POINT IS NOT NULL AND PI_POINT > 0 THEN '-'       ");
//        strQuery.append("                 ELSE '+'                                    ");
        strQuery.append("             END,                                            ");
        strQuery.append("             FORMAT(IFNULL(PI_POINT, 0), 0)) AS point_txt    ");
        strQuery.append(" FROM                                                        ");
        strQuery.append("     TB_POINT                                                ");
        strQuery.append(" WHERE                                                       ");
        strQuery.append("     MB_CD = :mbcd                                              ");

        Query selectQuery = em.createNativeQuery(strQuery.toString()).setParameter("mbcd", mbcd);

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }

    @Override
    public ResponseEntity<?> setMbrPointInf(MoPoint moPoint) {

//        회원정보 확인
        TbMember mbr = tbMemberRepository.findOne(moPoint.getMbCd());
        if (mbr == null) return new ResponseEntity<>("해당 회원정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        //            적립내역 등록
        TbPoint tbPoint = new TbPoint();

        tbPoint.setMbCd(moPoint.getMbCd());
        tbPoint.setPiPoint(moPoint.getPoint());
        tbPoint.setPiChaDt(new Date());
        tbPoint.setPiCharge(moPoint.getPlUseMemo());
        tbPoint.setDelYn("N");
        tbPoint.setEvtNm("신규");
        tbPoint.setRegiDt(new Date());
        tbPoint.setUser("모바일");

        tbPointRepository.save(tbPoint);

        return new ResponseEntity<>(tbPoint, HttpStatus.OK);
    }

    @Override
    public List<?> getUsePoCoup(Integer mbcd) {

        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT                                                            ");
        strQuery.append("     a.PO_CD AS poCd,                                              ");
        strQuery.append("     b.PO_NM AS poNm,                                              ");
        strQuery.append("     DATE_FORMAT(a.PU_USE_DT, '%Y년 %m월 %d일') AS puUseDt,        ");
        strQuery.append("     DATE_FORMAT(b.PO_START_DT,                                    ");
        strQuery.append("             '%Y년 %m월 %d일 부터') AS poStartDt,                  ");
        strQuery.append("     DATE_FORMAT(b.PO_FINISH_DT, '%Y년 %m월 %d일 까지') AS poFinishDt,");

        /*strQuery.append("     CASE                                                          ");
        strQuery.append("         WHEN a.USE_YN = 'Y' THEN '사용'                           ");
        strQuery.append("         WHEN b.PO_FINISH_DT < NOW() THEN '만료'                   ");
        strQuery.append("         ELSE '미사용'                                             ");
        strQuery.append("     END AS poStatus                                                 ");*/
        strQuery.append("     CASE                                                          ");
        strQuery.append("         WHEN a.USE_YN = 'Y' THEN '사용'                           ");
        strQuery.append("         WHEN DATE(b.PO_FINISH_DT) < DATE(NOW())  THEN '만료'                   ");
        strQuery.append("         ELSE '미사용'                                             ");
        strQuery.append("     END AS poStatus                                                 ");

        strQuery.append(" FROM                                                              ");
        strQuery.append("     TB_PROMOTION_USE a                                            ");
        strQuery.append("         INNER JOIN                                                ");
        strQuery.append("     TB_PROMOTION b ON a.PO_CD = b.PO_CD                           ");
        strQuery.append(" WHERE                                                             ");
        strQuery.append("     a.MB_CD = :mbcd                                                 ");

        Query selectQuery = em.createNativeQuery(strQuery.toString()).setParameter("mbcd", mbcd);

        List<?> list = (List<?>) selectQuery.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

        return list;
    }
}