package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2016-01-05.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 모바일 주문 검색용
 */
public class MoOrderList implements java.io.Serializable {
    public MoOrderList() {
    }

    public MoOrderList(Long orCd, Date regiDt, String orStatus, String orStatusTxt, String orChargeType) {
        this.orCd = orCd;
        this.regiDt = regiDt;
        this.orStatus = orStatus;
        this.orStatusTxt = orStatusTxt;
        this.orChargeType = orChargeType;
    }

    private Long orCd;
    private Date regiDt;
    private String orStatus;
    private String orStatusTxt;
//    private String orCd2;

    public Long getOrCd() {
        return orCd;
    }

    public void setOrCd(Long orCd) {
        this.orCd = orCd;
    }

    public Date getRegiDt() {
        return regiDt;
    }

    public void setRegiDt(Date regiDt) {
        this.regiDt = regiDt;
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

    /*public void setOrCd2(String orCd2) {
        this.orCd2 = orCd2;
    }*/

    private String orChargeType;

    public String getOrChargeType() {
        return orChargeType;
    }

    public void setOrChargeType(String orChargeType) {
        this.orChargeType = orChargeType;
    }
}