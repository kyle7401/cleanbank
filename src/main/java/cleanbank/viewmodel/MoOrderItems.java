package cleanbank.viewmodel;

import java.util.List;

/**
 * Created by hyoseop on 2016-01-18.
 */

/**
 * 품목내역	: 주문코드에 해당하는 품목내역을 가져온다.
 */
public class MoOrderItems implements java.io.Serializable {
    public MoOrderItems() {
    }

    public MoOrderItems(String info, Integer orCnt, Integer orPrice, List<item> items) {
        this.info = info;
        this.orCnt = orCnt;
        this.orPrice = orPrice;
        this.items = items;
    }

    public String info;
    private Integer orCnt;
    private Integer orPrice;
    private List<item> items;

    public List<item> getItems() {
        return items;
    }

    public void setItems(List<item> items) {
        this.items = items;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getOrCnt() {
        return orCnt;
    }

    public void setOrCnt(Integer orCnt) {
        this.orCnt = orCnt;
    }

    public Integer getOrPrice() {
        return orPrice;
    }

    public void setOrPrice(Integer orPrice) {
        this.orPrice = orPrice;
    }

    public static class item {
        public item() {
        }

        public item(long itCd, String pdLvl1, String pdLvl2, String pdLvl3, String pdNm, String itTac, String itStatus, String itStatusTxt, int itPrice, Integer itDiscount) {
            this.itCd = itCd;
            this.pdLvl1 = pdLvl1;
            this.pdLvl2 = pdLvl2;
            this.pdLvl3 = pdLvl3;
            this.pdNm = pdNm;
            this.itTac = itTac;
            this.itStatus = itStatus;
            this.itStatusTxt = itStatusTxt;
            this.itPrice = itPrice;
            this.itDiscount = itDiscount;
        }

        private long itCd;
        private String pdLvl1;
        private String pdLvl2;
        private String pdLvl3;
        private String pdNm;
        private String itTac;
        private String itStatus;
        private String itStatusTxt;
        private int itPrice;
        private Integer itDiscount;

        public long getItCd() {
            return itCd;
        }

        public void setItCd(long itCd) {
            this.itCd = itCd;
        }

        public String getItStatusTxt() {
            return itStatusTxt;
        }

        public void setItStatusTxt(String itStatusTxt) {
            this.itStatusTxt = itStatusTxt;
        }

        public Integer getItDiscount() {
            return itDiscount;
        }

        public void setItDiscount(Integer itDiscount) {
            this.itDiscount = itDiscount;
        }

        public String getPdLvl1() {
            return pdLvl1;
        }

        public void setPdLvl1(String pdLvl1) {
            this.pdLvl1 = pdLvl1;
        }

        public String getPdLvl2() {
            return pdLvl2;
        }

        public void setPdLvl2(String pdLvl2) {
            this.pdLvl2 = pdLvl2;
        }

        public String getPdLvl3() {
            return pdLvl3;
        }

        public void setPdLvl3(String pdLvl3) {
            this.pdLvl3 = pdLvl3;
        }

        public String getPdNm() {
            return pdNm;
        }

        public void setPdNm(String pdNm) {
            this.pdNm = pdNm;
        }

        public String getItTac() {
            return itTac;
        }

        public void setItTac(String itTac) {
            this.itTac = itTac;
        }

        public String getItStatus() {
            return itStatus;
        }

        public void setItStatus(String itStatus) {
            this.itStatus = itStatus;
        }

        public int getItPrice() {
            return itPrice;
        }

        public void setItPrice(int itPrice) {
            this.itPrice = itPrice;
        }
    }
}
