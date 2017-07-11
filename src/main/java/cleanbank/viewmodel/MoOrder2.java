package cleanbank.viewmodel;

import java.util.Date;

public class MoOrder2 implements java.io.Serializable {

	private String orStatus;
	private Date orOrderDt;
	private String mbNicNm;
	private String mbTel;
	private Integer orCnt;
	private Integer orPoint;
	private Integer orPrice;
	private Integer orDelivery;
	private Integer orDiscount;
	private Integer orExtMoney;
	private Integer orCharge;
	private String orChargeType;
	private String orMemo;
	private Date orReqDt;
	private String orReqAddr;

	public String getOrReqAddr1() {
		return orReqAddr1;
	}

	public void setOrReqAddr1(String orReqAddr1) {
		this.orReqAddr1 = orReqAddr1;
	}

	public String getOrReqAddr2() {
		return orReqAddr2;
	}

	public void setOrReqAddr2(String orReqAddr2) {
		this.orReqAddr2 = orReqAddr2;
	}

	private String orReqAddr1;
	private String orReqAddr2;

	private Date orVisitDt;
	private String orDeliAddr;
	private String orDeliAddr1;

	public String getOrDeliAddr2() {
		return orDeliAddr2;
	}

	public void setOrDeliAddr2(String orDeliAddr2) {
		this.orDeliAddr2 = orDeliAddr2;
	}

	public String getOrDeliAddr1() {
		return orDeliAddr1;
	}

	public void setOrDeliAddr1(String orDeliAddr1) {
		this.orDeliAddr1 = orDeliAddr1;
	}

	private String orDeliAddr2;

	private Integer orCoupon = 0; // 쿠폰
	private Integer orAddMoney = 0; // 추가금액
	private Integer orCouponMoney = 0; // 쿠폰
	private Integer orPointMoney = 0; // 포인트(계산)

	private Integer epCd;
	private Integer orDeliEpCd;
	private String orVisitPurpose = null;
	private Long orCd;

	private String mbLevel;
	private String mbBillinfo;
	private String mbBiilkey;

	private String mbReqLat;
	private String mbReqLng;
	private String mbDeliLat;
	private String mbDeliLng;

	public String getMbDeliLng() {
		return mbDeliLng;
	}

	public void setMbDeliLng(String mbDeliLng) {
		this.mbDeliLng = mbDeliLng;
	}

	public String getMbReqLat() {
		return mbReqLat;
	}

	public void setMbReqLat(String mbReqLat) {
		this.mbReqLat = mbReqLat;
	}

	public String getMbReqLng() {
		return mbReqLng;
	}

	public void setMbReqLng(String mbReqLng) {
		this.mbReqLng = mbReqLng;
	}

	public String getMbDeliLat() {
		return mbDeliLat;
	}

	public void setMbDeliLat(String mbDeliLat) {
		this.mbDeliLat = mbDeliLat;
	}

	public String getMbBillinfo() {
		return mbBillinfo;
	}

	public void setMbBillinfo(String mbBillinfo) {
		this.mbBillinfo = mbBillinfo;
	}

	public String getMbBiilkey() {
		return mbBiilkey;
	}

	public void setMbBiilkey(String mbBiilkey) {
		this.mbBiilkey = mbBiilkey;
	}

	public String getMbLevel() {
		return mbLevel;
	}

	public void setMbLevel(String mbLevel) {
		this.mbLevel = mbLevel;
	}

	public Long getOrCd() {
		return orCd;
	}

	public void setOrCd(Long orCd) {
		this.orCd = orCd;
	}

	public Integer getEpCd() {
		return epCd;
	}

	public void setEpCd(Integer epCd) {
		this.epCd = epCd;
	}

	public Integer getOrDeliEpCd() {
		return orDeliEpCd;
	}

	public void setOrDeliEpCd(Integer orDeliEpCd) {
		this.orDeliEpCd = orDeliEpCd;
	}

	public Integer getOrCoupon() {
		return orCoupon;
	}

	public void setOrCoupon(Integer orCoupon) {
		this.orCoupon = orCoupon;
	}

	public Integer getOrAddMoney() {
		return orAddMoney;
	}

	public void setOrAddMoney(Integer orAddMoney) {
		this.orAddMoney = orAddMoney;
	}

	public Integer getOrCouponMoney() {
		return orCouponMoney;
	}

	public void setOrCouponMoney(Integer orCouponMoney) {
		this.orCouponMoney = orCouponMoney;
	}

	public Integer getOrPointMoney() {
		return orPointMoney;
	}

	public void setOrPointMoney(Integer orPointMoney) {
		this.orPointMoney = orPointMoney;
	}

	public String getOrVisitPurpose() {
		return orVisitPurpose;
	}

	public void setOrVisitPurpose(String orVisitPurpose) {
		this.orVisitPurpose = orVisitPurpose;
	}

	public Integer getMbCd() {
		return mbCd;
	}

	public void setMbCd(Integer mbCd) {
		this.mbCd = mbCd;
	}

	private Integer mbCd;

	public MoOrder2() {
	}

	public MoOrder2(String orStatus, Date orOrderDt, String mbNicNm, String mbTel, Integer orCnt, Integer orPoint, Integer orPrice, Integer orDelivery, Integer orDiscount, Integer orExtMoney, Integer orCharge, String orChargeType, String orMemo, Date orReqDt, String orReqAddr, Date orVisitDt, String orDeliAddr) {
		this.orStatus = orStatus;
		this.orOrderDt = orOrderDt;
		this.mbNicNm = mbNicNm;
		this.mbTel = mbTel;
		this.orCnt = orCnt;
		this.orPoint = orPoint;
		this.orPrice = orPrice;
		this.orDelivery = orDelivery;
		this.orDiscount = orDiscount;
		this.orExtMoney = orExtMoney;
		this.orCharge = orCharge;
		this.orChargeType = orChargeType;
		this.orMemo = orMemo;
		this.orReqDt = orReqDt;
		this.orReqAddr = orReqAddr;
		this.orVisitDt = orVisitDt;
		this.orDeliAddr = orDeliAddr;
	}

	public String getOrStatus() {
		return orStatus;
	}

	public void setOrStatus(String orStatus) {
		this.orStatus = orStatus;
	}

	public Date getOrOrderDt() {
		return orOrderDt;
	}

	public void setOrOrderDt(Date orOrderDt) {
		this.orOrderDt = orOrderDt;
	}

	public String getMbNicNm() {
		return mbNicNm;
	}

	public void setMbNicNm(String mbNicNm) {
		this.mbNicNm = mbNicNm;
	}

	public String getMbTel() {
		return mbTel;
	}

	public void setMbTel(String mbTel) {
		this.mbTel = mbTel;
	}

	public Integer getOrCnt() {
		return orCnt;
	}

	public void setOrCnt(Integer orCnt) {
		this.orCnt = orCnt;
	}

	public Integer getOrPoint() {
		return orPoint;
	}

	public void setOrPoint(Integer orPoint) {
		this.orPoint = orPoint;
	}

	public Integer getOrPrice() {
		return orPrice;
	}

	public void setOrPrice(Integer orPrice) {
		this.orPrice = orPrice;
	}

	public Integer getOrDelivery() {
		return orDelivery;
	}

	public void setOrDelivery(Integer orDelivery) {
		this.orDelivery = orDelivery;
	}

	public Integer getOrDiscount() {
		return orDiscount;
	}

	public void setOrDiscount(Integer orDiscount) {
		this.orDiscount = orDiscount;
	}

	public Integer getOrExtMoney() {
		return orExtMoney;
	}

	public void setOrExtMoney(Integer orExtMoney) {
		this.orExtMoney = orExtMoney;
	}

	public Integer getOrCharge() {
		return orCharge;
	}

	public void setOrCharge(Integer orCharge) {
		this.orCharge = orCharge;
	}

	public String getOrChargeType() {
		return orChargeType;
	}

	public void setOrChargeType(String orChargeType) {
		this.orChargeType = orChargeType;
	}

	public String getOrMemo() {
		return orMemo;
	}

	public void setOrMemo(String orMemo) {
		this.orMemo = orMemo;
	}

	public Date getOrReqDt() {
		return orReqDt;
	}

	public void setOrReqDt(Date orReqDt) {
		this.orReqDt = orReqDt;
	}

	public String getOrReqAddr() {
		return orReqAddr;
	}

	public void setOrReqAddr(String orReqAddr) {
		this.orReqAddr = orReqAddr;
	}

	public Date getOrVisitDt() {
		return orVisitDt;
	}

	public void setOrVisitDt(Date orVisitDt) {
		this.orVisitDt = orVisitDt;
	}

	public String getOrDeliAddr() {
		return orDeliAddr;
	}

	public void setOrDeliAddr(String orDeliAddr) {
		this.orDeliAddr = orDeliAddr;
	}
}