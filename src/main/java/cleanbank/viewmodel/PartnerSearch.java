package cleanbank.viewmodel;

/**
 * Created by hyoseop on 2015-12-18.
 */
public class PartnerSearch {
    private String ptNm;
    private String ptTel;
    private String ptFax;
    private String pmKeyWord;   // 담당자 정보 검색 키워드
    /*private String pmNm;
    private String pmEmail;
    private String pmTel;*/

    public String getPmKeyWord() {
        return pmKeyWord;
    }

    public void setPmKeyWord(String pmKeyWord) {
        this.pmKeyWord = pmKeyWord;
    }

    public String getPtNm() {
        return ptNm;
    }

    public void setPtNm(String ptNm) {
        this.ptNm = ptNm;
    }

    public String getPtTel() {
        return ptTel;
    }

    public void setPtTel(String ptTel) {
        this.ptTel = ptTel;
    }

    public String getPtFax() {
        return ptFax;
    }

    public void setPtFax(String ptFax) {
        this.ptFax = ptFax;
    }
}
