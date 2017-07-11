package cleanbank.viewmodel;

import java.util.Date;

/**
 * Created by hyoseop on 2016-03-08.
 */
public class MoOrder4 {
    private Long orCd;

    private Date orReqDt;
    private String reqAddr1;
    private String reqAddr2;
    private String reqLat;
    private String reqLng;

    private Date orDeliVisitDt;
    private String deliAddr1;
    private String deliAddr2;
    private String deliLat;
    private String deliLng;

    private String orStatus;

    public String getOrStatus() {
        return orStatus;
    }

    public void setOrStatus(String orStatus) {
        this.orStatus = orStatus;
    }

    public String getDeliLng() {
        return deliLng;
    }

    public void setDeliLng(String deliLng) {
        this.deliLng = deliLng;
    }

    public Long getOrCd() {
        return orCd;
    }

    public void setOrCd(Long orCd) {
        this.orCd = orCd;
    }

    public Date getOrReqDt() {
        return orReqDt;
    }

    public void setOrReqDt(Date orReqDt) {
        this.orReqDt = orReqDt;
    }

    public String getReqAddr1() {
        return reqAddr1;
    }

    public void setReqAddr1(String reqAddr1) {
        this.reqAddr1 = reqAddr1;
    }

    public String getReqAddr2() {
        return reqAddr2;
    }

    public void setReqAddr2(String reqAddr2) {
        this.reqAddr2 = reqAddr2;
    }

    public String getReqLat() {
        return reqLat;
    }

    public void setReqLat(String reqLat) {
        this.reqLat = reqLat;
    }

    public String getReqLng() {
        return reqLng;
    }

    public void setReqLng(String reqLng) {
        this.reqLng = reqLng;
    }

    public Date getOrDeliVisitDt() {
        return orDeliVisitDt;
    }

    public void setOrDeliVisitDt(Date orDeliVisitDt) {
        this.orDeliVisitDt = orDeliVisitDt;
    }

    public String getDeliAddr1() {
        return deliAddr1;
    }

    public void setDeliAddr1(String deliAddr1) {
        this.deliAddr1 = deliAddr1;
    }

    public String getDeliAddr2() {
        return deliAddr2;
    }

    public void setDeliAddr2(String deliAddr2) {
        this.deliAddr2 = deliAddr2;
    }

    public String getDeliLat() {
        return deliLat;
    }

    public void setDeliLat(String deliLat) {
        this.deliLat = deliLat;
    }
}
