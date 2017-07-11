package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2016-01-30.
 */
public class MoInicisResult {
/*    $P_STATUS;			// 인증상태 : 성공시(00, 그 외 실패) 2~4자리
    $P_RMESG1;			// 결과메시지 char(500)
    $P_TID;				// 인증거래번호 : Char(40) / 성공시에만 반환
    $P_TYPE;			// 지불수단 char(10)
    $P_AUTH_DT;			// 승인일자 char(14)
    $P_MID;				// 상점아이디 char(10)
    $P_OID;				// 상점주문번호 char(100)
    $P_AMT;				// 거래금액 char(8)
    $P_UNAME;			// 결제고객성명 char(30)
    P_MNAME // 가맹점 이름
    $P_FN_CD1;			// 카드코드 : char(4)
    $P_FN_NM;			// 결제카드한글명
    $P_RMESG2;			// 메시지2 : char(500) 신용카드 할부 개월 수
    $P_AUTH_NO;			// 승인번호 : char(30) 신용카드거래에서만 사용*/

//    P_REQ_URL // 승인요청 Url
    //$P_NOTI;			// 노티메시지(상점에서 올린 메시지 - 사용안함)
//    P_NOTEURL // 가맹점 전달 NOTI URL
//    P_NEXT_URL // 가맹점 전달 NEXT URL

//    $P_FN_CD2;			// 금융사코드2

    String P_STATUS;			// 인증상태 : 성공시(00, 그 외 실패) 2~4자리
    String P_RMESG1;			// 결과메시지 char(500)
    String P_TID;				// 인증거래번호 : Char(40) / 성공시에만 반환
    String P_TYPE;			// 지불수단 char(10)
    String P_AUTH_DT;			// 승인일자 char(14)
    String P_MID;				// 상점아이디 char(10)
    String P_OID;				// 상점주문번호 char(100)
    String P_AMT;				// 거래금액 char(8)
    String P_UNAME;			// 결제고객성명 char(30)
    String P_MNAME; // 가맹점 이름
    String P_FN_CD1;			// 카드코드 : char(4)
    String P_FN_NM;			// 결제카드한글명
    String P_RMESG2;			// 메시지2 : char(500) 신용카드 할부 개월 수
    String P_AUTH_NO;			// 승인번호 : char(30) 신용카드거래에서만 사용

    public String getP_MNAME() {
        return P_MNAME;
    }

    public void setP_MNAME(String p_MNAME) {
        P_MNAME = p_MNAME;
    }

    public String getP_AUTH_NO() {
        return P_AUTH_NO;
    }

    public void setP_AUTH_NO(String p_AUTH_NO) {
        P_AUTH_NO = p_AUTH_NO;
    }

    public String getP_STATUS() {
        return P_STATUS;
    }

    public void setP_STATUS(String p_STATUS) {
        P_STATUS = p_STATUS;
    }

    public String getP_RMESG1() {
        return P_RMESG1;
    }

    public void setP_RMESG1(String p_RMESG1) {
        P_RMESG1 = p_RMESG1;
    }

    public String getP_TID() {
        return P_TID;
    }

    public void setP_TID(String p_TID) {
        P_TID = p_TID;
    }

    public String getP_TYPE() {
        return P_TYPE;
    }

    public void setP_TYPE(String p_TYPE) {
        P_TYPE = p_TYPE;
    }

    public String getP_AUTH_DT() {
        return P_AUTH_DT;
    }

    public void setP_AUTH_DT(String p_AUTH_DT) {
        P_AUTH_DT = p_AUTH_DT;
    }

    public String getP_MID() {
        return P_MID;
    }

    public void setP_MID(String p_MID) {
        P_MID = p_MID;
    }

    public String getP_OID() {
        return P_OID;
    }

    public void setP_OID(String p_OID) {
        P_OID = p_OID;
    }

    public String getP_AMT() {
        return P_AMT;
    }

    public void setP_AMT(String p_AMT) {
        P_AMT = p_AMT;
    }

    public String getP_UNAME() {
        return P_UNAME;
    }

    public void setP_UNAME(String p_UNAME) {
        P_UNAME = p_UNAME;
    }

    public String getP_FN_CD1() {
        return P_FN_CD1;
    }

    public void setP_FN_CD1(String p_FN_CD1) {
        P_FN_CD1 = p_FN_CD1;
    }

    public String getP_FN_NM() {
        return P_FN_NM;
    }

    public void setP_FN_NM(String p_FN_NM) {
        P_FN_NM = p_FN_NM;
    }

    public String getP_RMESG2() {
        return P_RMESG2;
    }

    public void setP_RMESG2(String p_RMESG2) {
        P_RMESG2 = p_RMESG2;
    }
}
