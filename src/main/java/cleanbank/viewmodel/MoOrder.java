package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2016-01-05.
 */

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 모바일 주문 생성용
 */
public class MoOrder implements java.io.Serializable {
    public MoOrder() {
    }

    public MoOrder(Integer mbCd, Date orReqDt, String orReqMemo, String mbAddr1, String mbAddr2, String mbLat, String mbLng) {
        this.mbCd = mbCd;
        this.orReqDt = orReqDt;
        this.orReqMemo = orReqMemo;
        this.mbAddr1 = mbAddr1;
        this.mbAddr2 = mbAddr2;
        this.mbLat = mbLat;
        this.mbLng = mbLng;
    }

    private Integer mbCd;
    private Date orReqDt;
    private String orReqMemo;
    private String mbAddr1;
    private String mbAddr2;
    private String mbLat;
    private String mbLng;
    private String mbTel;

    public String getMbTel() {
        return mbTel;
    }

    public void setMbTel(String mbTel) {
        this.mbTel = mbTel;
    }

    public Integer getMbCd() {
        return mbCd;
    }

    public void setMbCd(Integer mbCd) {
        this.mbCd = mbCd;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getOrReqDt() {
        return orReqDt;
    }

    public void setOrReqDt(Date orReqDt) {
        this.orReqDt = orReqDt;
    }

    public String getOrReqMemo() {
        return orReqMemo;
    }

    public void setOrReqMemo(String orReqMemo) {
        this.orReqMemo = orReqMemo;
    }

    public String getMbAddr1() {
        return mbAddr1;
    }

    public void setMbAddr1(String mbAddr1) {
        this.mbAddr1 = mbAddr1;
    }

    public String getMbAddr2() {
        return mbAddr2;
    }

    public void setMbAddr2(String mbAddr2) {
        this.mbAddr2 = mbAddr2;
    }

    public String getMbLat() {
        return mbLat;
    }

    public void setMbLat(String mbLat) {
        this.mbLat = mbLat;
    }

    public String getMbLng() {
        return mbLng;
    }

    public void setMbLng(String mbLng) {
        this.mbLng = mbLng;
    }

    private Long orCd;

    public Long getOrCd() {
        return orCd;
    }

    public void setOrCd(Long orCd) {
        this.orCd = orCd;
    }
}
