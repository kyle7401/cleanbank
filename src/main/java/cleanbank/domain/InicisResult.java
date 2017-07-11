package cleanbank.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INICIS_RESULT", catalog = "cleanbank")
public class InicisResult implements java.io.Serializable {

	private Integer id;
	private String PAmt;
	private String PAuthDt;
	private String PAuthNo;
	private String PFnCd1;
	private String PFnNm;
	private String PMid;
	private String PMname;
	private String POid;
	private String PRmesg1;
	private String PRmesg2;
	private String PStatus;
	private String PTid;
	private String PType;
	private String PUname;
	private String PFnCd2;
	private String PNoti;
	private String PCardIssuerCode;
	private String PCardNum;
	private String PCardMemberNum;
	private String PCardPurchaseCode;
	private String PPrtcCode;
	private String PIspCardcode;
	private String PCardPurchaseName;
	private String PCardIssuerName;
	private String PRmesg3;
	private String PMerchantReserved;

	public InicisResult() {
	}

	public InicisResult(String PAmt, String PAuthDt, String PAuthNo, String PFnCd1, String PFnNm, String PMid,
			String PMname, String POid, String PRmesg1, String PRmesg2, String PStatus, String PTid, String PType,
			String PUname, String PFnCd2, String PNoti, String PCardIssuerCode, String PCardNum, String PCardMemberNum,
			String PCardPurchaseCode, String PPrtcCode, String PIspCardcode, String PCardPurchaseName,
			String PCardIssuerName, String PRmesg3, String PMerchantReserved) {
		this.PAmt = PAmt;
		this.PAuthDt = PAuthDt;
		this.PAuthNo = PAuthNo;
		this.PFnCd1 = PFnCd1;
		this.PFnNm = PFnNm;
		this.PMid = PMid;
		this.PMname = PMname;
		this.POid = POid;
		this.PRmesg1 = PRmesg1;
		this.PRmesg2 = PRmesg2;
		this.PStatus = PStatus;
		this.PTid = PTid;
		this.PType = PType;
		this.PUname = PUname;
		this.PFnCd2 = PFnCd2;
		this.PNoti = PNoti;
		this.PCardIssuerCode = PCardIssuerCode;
		this.PCardNum = PCardNum;
		this.PCardMemberNum = PCardMemberNum;
		this.PCardPurchaseCode = PCardPurchaseCode;
		this.PPrtcCode = PPrtcCode;
		this.PIspCardcode = PIspCardcode;
		this.PCardPurchaseName = PCardPurchaseName;
		this.PCardIssuerName = PCardIssuerName;
		this.PRmesg3 = PRmesg3;
		this.PMerchantReserved = PMerchantReserved;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "P_AMT", length = 8)
	public String getPAmt() {
		return this.PAmt;
	}

	public void setPAmt(String PAmt) {
		this.PAmt = PAmt;
	}

	@Column(name = "P_AUTH_DT", length = 14)
	public String getPAuthDt() {
		return this.PAuthDt;
	}

	public void setPAuthDt(String PAuthDt) {
		this.PAuthDt = PAuthDt;
	}

	@Column(name = "P_AUTH_NO", length = 30)
	public String getPAuthNo() {
		return this.PAuthNo;
	}

	public void setPAuthNo(String PAuthNo) {
		this.PAuthNo = PAuthNo;
	}

	@Column(name = "P_FN_CD1", length = 4)
	public String getPFnCd1() {
		return this.PFnCd1;
	}

	public void setPFnCd1(String PFnCd1) {
		this.PFnCd1 = PFnCd1;
	}

	@Column(name = "P_FN_NM", length = 50)
	public String getPFnNm() {
		return this.PFnNm;
	}

	public void setPFnNm(String PFnNm) {
		this.PFnNm = PFnNm;
	}

	@Column(name = "P_MID", length = 10)
	public String getPMid() {
		return this.PMid;
	}

	public void setPMid(String PMid) {
		this.PMid = PMid;
	}

	@Column(name = "P_MNAME", length = 100)
	public String getPMname() {
		return this.PMname;
	}

	public void setPMname(String PMname) {
		this.PMname = PMname;
	}

	@Column(name = "P_OID", length = 100)
	public String getPOid() {
		return this.POid;
	}

	public void setPOid(String POid) {
		this.POid = POid;
	}

	@Column(name = "P_RMESG1", length = 500)
	public String getPRmesg1() {
		return this.PRmesg1;
	}

	public void setPRmesg1(String PRmesg1) {
		this.PRmesg1 = PRmesg1;
	}

	@Column(name = "P_RMESG2", length = 500)
	public String getPRmesg2() {
		return this.PRmesg2;
	}

	public void setPRmesg2(String PRmesg2) {
		this.PRmesg2 = PRmesg2;
	}

	@Column(name = "P_STATUS", length = 4)
	public String getPStatus() {
		return this.PStatus;
	}

	public void setPStatus(String PStatus) {
		this.PStatus = PStatus;
	}

	@Column(name = "P_TID", length = 40)
	public String getPTid() {
		return this.PTid;
	}

	public void setPTid(String PTid) {
		this.PTid = PTid;
	}

	@Column(name = "P_TYPE", length = 10)
	public String getPType() {
		return this.PType;
	}

	public void setPType(String PType) {
		this.PType = PType;
	}

	@Column(name = "P_UNAME", length = 30)
	public String getPUname() {
		return this.PUname;
	}

	public void setPUname(String PUname) {
		this.PUname = PUname;
	}

	@Column(name = "P_FN_CD2", length = 4)
	public String getPFnCd2() {
		return this.PFnCd2;
	}

	public void setPFnCd2(String PFnCd2) {
		this.PFnCd2 = PFnCd2;
	}

	@Column(name = "P_NOTI", length = 45)
	public String getPNoti() {
		return this.PNoti;
	}

	public void setPNoti(String PNoti) {
		this.PNoti = PNoti;
	}

	@Column(name = "P_CARD_ISSUER_CODE", length = 45)
	public String getPCardIssuerCode() {
		return this.PCardIssuerCode;
	}

	public void setPCardIssuerCode(String PCardIssuerCode) {
		this.PCardIssuerCode = PCardIssuerCode;
	}

	@Column(name = "P_CARD_NUM", length = 45)
	public String getPCardNum() {
		return this.PCardNum;
	}

	public void setPCardNum(String PCardNum) {
		this.PCardNum = PCardNum;
	}

	@Column(name = "P_CARD_MEMBER_NUM", length = 45)
	public String getPCardMemberNum() {
		return this.PCardMemberNum;
	}

	public void setPCardMemberNum(String PCardMemberNum) {
		this.PCardMemberNum = PCardMemberNum;
	}

	@Column(name = "P_CARD_PURCHASE_CODE", length = 45)
	public String getPCardPurchaseCode() {
		return this.PCardPurchaseCode;
	}

	public void setPCardPurchaseCode(String PCardPurchaseCode) {
		this.PCardPurchaseCode = PCardPurchaseCode;
	}

	@Column(name = "P_PRTC_CODE", length = 45)
	public String getPPrtcCode() {
		return this.PPrtcCode;
	}

	public void setPPrtcCode(String PPrtcCode) {
		this.PPrtcCode = PPrtcCode;
	}

	@Column(name = "P_ISP_CARDCODE", length = 45)
	public String getPIspCardcode() {
		return this.PIspCardcode;
	}

	public void setPIspCardcode(String PIspCardcode) {
		this.PIspCardcode = PIspCardcode;
	}

	@Column(name = "P_CARD_PURCHASE_NAME", length = 45)
	public String getPCardPurchaseName() {
		return this.PCardPurchaseName;
	}

	public void setPCardPurchaseName(String PCardPurchaseName) {
		this.PCardPurchaseName = PCardPurchaseName;
	}

	@Column(name = "P_CARD_ISSUER_NAME", length = 45)
	public String getPCardIssuerName() {
		return this.PCardIssuerName;
	}

	public void setPCardIssuerName(String PCardIssuerName) {
		this.PCardIssuerName = PCardIssuerName;
	}

	@Column(name = "P_RMESG3", length = 200)
	public String getPRmesg3() {
		return this.PRmesg3;
	}

	public void setPRmesg3(String PRmesg3) {
		this.PRmesg3 = PRmesg3;
	}

	@Column(name = "P_MERCHANT_RESERVED", length = 45)
	public String getPMerchantReserved() {
		return this.PMerchantReserved;
	}

	public void setPMerchantReserved(String PMerchantReserved) {
		this.PMerchantReserved = PMerchantReserved;
	}

}
