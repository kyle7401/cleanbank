package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2016-01-05.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 모바일 주문상세(결제대기/결제완료/배송중)
 */
public class MoOrderDetail2 implements java.io.Serializable {
    public MoOrderDetail2() {
    }

    public MoOrderDetail2(String orStatusTxt, String orStatus, Date orVisitDt, String orAddr, Integer orCnt, Integer orPoint, Integer orPrice, Integer orDelivery,
                          Integer orDiscount, Integer orExtMoney, Integer orCharge, Integer epCd, Date regiDt, Long orCd, String orChargeType) {
        this.orStatusTxt = orStatusTxt;
        this.orStatus = orStatus;
        this.orVisitDt = orVisitDt;
        this.orAddr = orAddr;
        this.orCnt = orCnt;
        this.orPoint = orPoint;
        this.orPrice = orPrice;
        this.orDelivery = orDelivery;
        this.orDiscount = orDiscount;
        this.orExtMoney = orExtMoney;
        this.orCharge = orCharge;
        /*this.epNm = epNm;
        this.epTel = epTel;
        this.epImg = epImg;*/
        this.epCd = epCd;
        this.regiDt = regiDt;
        this.orCd = orCd;
        this.orChargeType = orChargeType;
    }

    //    private String orCd2;
    private String orStatusTxt;
    private String orStatus;
    private Date orVisitDt;
    private String orAddr;
    private Integer orCnt;
    private Integer orPoint;
    private String epNm;
    private String epTel;
    private String epImg;
    private String orChargeType;

    @JsonIgnore
    private Date regiDt;

    @JsonIgnore
    private Long orCd;

    @JsonIgnore
    private Integer epCd;

    public Integer getEpCd() {
        return epCd;
    }

    public void setEpCd(Integer epCd) {
        this.epCd = epCd;
    }

    /**
     * [주문번호 조건] : 년(2자리)+월(2자리)+일(2자리)+시(2자리)+분(2자리)+일련번호(4자리)
     예) 1512071025000

     * @return
     */
    public String getOrCd2() {
        String tempDate = null;

        if(this.regiDt != null) {
            DateFormat sdFormat = new SimpleDateFormat("yyMMddHHMM");
            tempDate = sdFormat.format(this.regiDt);

            if(this.orCd != null) {
                String tempNumber = String.format("%04d", this.orCd);
                tempDate += tempNumber;
            }

        }

        return tempDate;
    }

/*    public void setOrCd2(String orCd2) {
        this.orCd2 = orCd2;
    }*/

    public String getOrStatusTxt() {
        return orStatusTxt;
    }

    public void setOrStatusTxt(String orStatusTxt) {
        this.orStatusTxt = orStatusTxt;
    }

    public String getOrStatus() {
        return orStatus;
    }

    public void setOrStatus(String orStatus) {
        this.orStatus = orStatus;
    }

    public Date getOrVisitDt() {
        return orVisitDt;
    }

    public void setOrVisitDt(Date orVisitDt) {
        this.orVisitDt = orVisitDt;
    }

    public String getOrAddr() {
        return orAddr;
    }

    public void setOrAddr(String orAddr) {
        this.orAddr = orAddr;
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

    public String getEpNm() {
        return epNm;
    }

    public void setEpNm(String epNm) {
        this.epNm = epNm;
    }

    public String getEpTel() {
        return epTel;
    }

    public void setEpTel(String epTel) {
        this.epTel = epTel;
    }

    public String getEpImg() {
        return epImg;
    }

    public void setEpImg(String epImg) {
        this.epImg = epImg;
    }

    public String getOrChargeType() {
        return orChargeType;
    }

    public void setOrChargeType(String orChargeType) {
        this.orChargeType = orChargeType;
    }

    private String epThumb;

    public String getEpThumb() {
//        return epThumb;
        String tempUrl = null;

        if(!StringUtils.isEmpty(this.epImg)) {
            tempUrl = "/api/getThumb?url="+ this.epImg;
        }

        return tempUrl;
    }

    public void setEpThumb(String epThumb) {
        this.epThumb = epThumb;
    }

    /*쿠폰	orCoupon
    추가금액	orAddMoney
    쿠폰	orCouponMoney
    포인트(계산)	orPointMoney*/

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

    private Integer orCoupon = 0; // 쿠폰
    private Integer orPrice; // 주문금액
    private Integer orDelivery; // 배송비
    private Integer orDiscount; // 할인금액
    private Integer orAddMoney = 0; // 추가금액
    private Integer orCouponMoney = 0; // 쿠폰
    private Integer orPointMoney = 0; // 포인트(계산)
    private Integer orExtMoney; // 기타금액
    private Integer orCharge; // 결제금액

    private String orPrice_t; // 주문금액
    private String orDelivery_t; // 배송비
    private String orDiscount_t; // 할인금액
    private String orAddMoney_t; // 추가금액
    private String orCouponMoney_t; // 쿠폰
    private String orPointMoney_t; // 포인트(계산)
    private String orExtMoney_t; // 기타금액
    private String orCharge_t; // 결제금액

//    주문금액
    public String getOrPrice_t() {
//        return orPrice_t;
        String tmpNumber = "0";

        if(this.orPrice == null) return tmpNumber;
        if(this.orPrice > 0) tmpNumber = String.format("%,d", this.orPrice);

        return tmpNumber;
    }

    public void setOrPrice_t(String orPrice_t) {
        this.orPrice_t = orPrice_t;
    }

    // 배송비
    public String getOrDelivery_t() {
//        return orDelivery_t;
        String tmpNumber = "0";

        if(this.orDelivery == null) return tmpNumber;
        if(this.orDelivery > 0) tmpNumber = String.format("+%,d", this.orDelivery);
        if(this.orDelivery < 0) tmpNumber = String.format("-%,d", this.orDelivery);

        return tmpNumber;
    }

    public void setOrDelivery_t(String orDelivery_t) {
        this.orDelivery_t = orDelivery_t;
    }

    // 할인금액
    public String getOrDiscount_t() {
//        return orDiscount_t;
        String tmpNumber = "0";

        if(this.orDiscount == null) return tmpNumber;
        if(this.orDiscount > 0) tmpNumber = String.format("+%,d", this.orDiscount);
//        if(this.orDiscount < 0) tmpNumber = String.format("-%,d", this.orDiscount);
        if(this.orDiscount < 0) tmpNumber = String.format("%,d", this.orDiscount);

        return tmpNumber;
    }

    public void setOrDiscount_t(String orDiscount_t) {
        this.orDiscount_t = orDiscount_t;
    }

    // 추가금액
    public String getOrAddMoney_t() {
//        return orAddMoney_t;
        String tmpNumber = "0";

        if(this.orAddMoney == null) return tmpNumber;
        if(this.orAddMoney > 0) tmpNumber = String.format("+%,d", this.orAddMoney);
//        if(this.orAddMoney < 0) tmpNumber = String.format("-%,d", this.orAddMoney);
        if(this.orAddMoney < 0) tmpNumber = String.format("%,d", this.orAddMoney);

        return tmpNumber;
    }

    public void setOrAddMoney_t(String orAddMoney_t) {
        this.orAddMoney_t = orAddMoney_t;
    }

    // 쿠폰
    public String getOrCouponMoney_t() {
//        return orCouponMoney_t;
        String tmpNumber = "0";

        if(this.orCouponMoney == null) return tmpNumber;
        if(this.orCouponMoney > 0) tmpNumber = String.format("+%,d", this.orCouponMoney);
//        if(this.orCouponMoney < 0) tmpNumber = String.format("-%,d", this.orCouponMoney);
        if(this.orCouponMoney < 0) tmpNumber = String.format("%,d", this.orCouponMoney);

        return tmpNumber;
    }

    public void setOrCouponMoney_t(String orCouponMoney_t) {
        this.orCouponMoney_t = orCouponMoney_t;
    }

    // 포인트(계산)
    public String getOrPointMoney_t() {
//        return orPointMoney_t;
        String tmpNumber = "0";

        if(this.orPointMoney == null) return tmpNumber;
        if(this.orPointMoney > 0) tmpNumber = String.format("+%,d", this.orPointMoney);
//        if(this.orPointMoney < 0) tmpNumber = String.format("-%,d", this.orPointMoney);
        if(this.orPointMoney < 0) tmpNumber = String.format("%,d", this.orPointMoney);

        return tmpNumber;
    }

    public void setOrPointMoney_t(String orPointMoney_t) {
        this.orPointMoney_t = orPointMoney_t;
    }

    // 기타금액
    public String getOrExtMoney_t() {
//        return orExtMoney_t;
        String tmpNumber = "0";

        if(this.orExtMoney == null) return tmpNumber;
        if(this.orExtMoney > 0) tmpNumber = String.format("+%,d", this.orExtMoney);
//        if(this.orExtMoney < 0) tmpNumber = String.format("-%,d", this.orExtMoney);
        if(this.orExtMoney < 0) tmpNumber = String.format("%,d", this.orExtMoney);

        return tmpNumber;
    }

    // 결제금액
    public void setOrExtMoney_t(String orExtMoney_t) {
        this.orExtMoney_t = orExtMoney_t;
    }

    public String getOrCharge_t() {
//        return orCharge_t;
        String tmpNumber = "0";

        if(this.orCharge == null) return tmpNumber;
        if(this.orCharge > 0) tmpNumber = String.format("+%,d", this.orCharge);
//        if(this.orCharge < 0) tmpNumber = String.format("-%,d", this.orCharge);
        if(this.orCharge < 0) tmpNumber = String.format("%,d", this.orCharge);

        return tmpNumber;
    }

    public void setOrCharge_t(String orCharge_t) {
        this.orCharge_t = orCharge_t;
    }

    private String orVisitDt2;

    public String getOrVisitDt2() {
//        return orVisitDt2;
        String strResult = null;

        if(this.orVisitDt != null) {
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTime(this.orVisitDt);
            tmpCal.add(Calendar.MINUTE, 30); // 수거시간 간격?

            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdFormat2 = new SimpleDateFormat("HH:mm");
            strResult = sdFormat.format(this.orVisitDt) +" ~ "+ sdFormat2.format(tmpCal.getTime());
        }

        return strResult;
    }

    public void setOrVisitDt2(String orVisitDt2) {
        this.orVisitDt2 = orVisitDt2;
    }
}