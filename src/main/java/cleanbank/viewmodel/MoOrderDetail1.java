package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2016-01-05.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 모바일 주문상세(수거중)
 */
public class MoOrderDetail1 implements java.io.Serializable {
    public MoOrderDetail1() {
    }

    public MoOrderDetail1(String orStatusTxt, String orStatus, Date orReqDt, String orReqAddr, Long orCd, Date regiDt, Integer epCd) {
        this.orStatusTxt = orStatusTxt;
        this.orStatus = orStatus;
        this.orReqDt = orReqDt;
        this.orReqAddr = orReqAddr;
        this.orCd = orCd;
        this.regiDt = regiDt;
        this.epCd = epCd;
    }

    private String orCd2;
    private String orStatusTxt;
    private String orStatus;
    private Date orReqDt;
    private String orReqAddr;
    private String epNm;
    private String epTel;

    @JsonIgnore
    private Long orCd;

    @JsonIgnore
    private Date regiDt;

    public String getEpImg() {
        return epImg;
    }

    public void setEpImg(String epImg) {
        this.epImg = epImg;
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

    public void setOrCd2(String orCd2) {
        this.orCd2 = orCd2;
    }

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

    private String epImg;

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

    private String epThumb;

    @JsonIgnore
    private Integer epCd;

    public Integer getEpCd() {
        return epCd;
    }

    public void setEpCd(Integer epCd) {
        this.epCd = epCd;
    }

//    수거일시 From ~ To
    private String orReqDt2;

    @Transient
    public String getOrReqDt2() {
//        return orReqDt2;
        String strResult = null;

        if(this.orReqDt != null) {
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTime(this.orReqDt);
            tmpCal.add(Calendar.MINUTE, 30); // 수거시간 간격?

            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdFormat2 = new SimpleDateFormat("HH:mm");
            strResult = sdFormat.format(this.orReqDt) +" ~ "+ sdFormat2.format(tmpCal.getTime());
        }

        return strResult;
    }

    public void setOrReqDt2(String orReqDt2) {
        this.orReqDt2 = orReqDt2;
    }
}
