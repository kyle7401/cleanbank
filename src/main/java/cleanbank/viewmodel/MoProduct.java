package cleanbank.viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by hyoseop on 2016-01-18.
 */
public class MoProduct {
    public MoProduct() {
    }

    public static class laundrie {
        private String lno;
        private String name;
        private List<item> items;

        public static class item {
            public item() {
            }

            public item(String pdNm, Integer pdPrice, Integer pdPer, String pdDesc) {
                this.pdNm = pdNm;
                this.pdPrice = pdPrice;
                this.pdPer = pdPer;
                this.pdDesc = pdDesc;
            }

            private String pdNm;
            private Integer pdPrice;
            private Integer pdPer;
            private String pdDesc;

            public String getPdNm() {
                return pdNm;
            }

            public void setPdNm(String pdNm) {
                this.pdNm = pdNm;
            }

            @JsonIgnore
            public String getPdDesc() {
                return pdDesc;
            }

            public void setPdDesc(String pdDesc) {
                this.pdDesc = pdDesc;
            }

            public Integer getPdPrice() {
                return pdPrice;
            }

            public void setPdPrice(Integer pdPrice) {
                this.pdPrice = pdPrice;
            }

            @JsonIgnore
            public Integer getPdPer() {
                return pdPer;
            }

            public void setPdPer(Integer pdPer) {
                this.pdPer = pdPer;
            }
        }

        public List<item> getItems() {
            return items;
        }

        public void setItems(List<item> items) {
            this.items = items;
        }

        public String getLno() {
            return lno;
        }

        public void setLno(String lno) {
            this.lno = lno;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private String group_no;
    private String group_name;

    public List<laundrie> getLaundries() {
        return laundries;
    }

    public void setLaundries(List<laundrie> laundries) {
        this.laundries = laundries;
    }

    public String getGroup_no() {
        return group_no;
    }

    public void setGroup_no(String group_no) {
        this.group_no = group_no;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<laundrie> laundries;
}
