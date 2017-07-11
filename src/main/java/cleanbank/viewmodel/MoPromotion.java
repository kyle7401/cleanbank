package cleanbank.viewmodel;

import java.util.Date;

/**
 * Created by hyoseop on 2016-01-25.
 */
/*@NoArgsConstructor
@AllArgsConstructor*/
/*@Data
@RequiredArgsConstructor*/
public class MoPromotion {
    private int mbCd;
    private String poCoup;
    private long orCd;
    private long poCd;
    private String puUse;
    private String poImg;
    private Long id;
    private String useYn;

    private Integer poGoldPrice;
    private Integer poSilverPrice;
    private Integer poGreenPrice;

    private Integer poGoldPer;
    private Integer poSilverPer;
    private Integer poGreenPer;

    private Integer poLimitAmount;

    private Date puUseDt;

    private Integer poDiscountAmt;

    public Integer getPoDiscountAmt() {
        return poDiscountAmt;
    }

    public void setPoDiscountAmt(Integer poDiscountAmt) {
        this.poDiscountAmt = poDiscountAmt;
    }

    public Date getPuUseDt() {
        return puUseDt;
    }

    public void setPuUseDt(Date puUseDt) {
        this.puUseDt = puUseDt;
    }

    public Integer getPoGoldPrice() {
        return poGoldPrice;
    }

    public void setPoGoldPrice(Integer poGoldPrice) {
        this.poGoldPrice = poGoldPrice;
    }

    public Integer getPoSilverPrice() {
        return poSilverPrice;
    }

    public void setPoSilverPrice(Integer poSilverPrice) {
        this.poSilverPrice = poSilverPrice;
    }

    public Integer getPoGreenPrice() {
        return poGreenPrice;
    }

    public void setPoGreenPrice(Integer poGreenPrice) {
        this.poGreenPrice = poGreenPrice;
    }

    public Integer getPoGoldPer() {
        return poGoldPer;
    }

    public void setPoGoldPer(Integer poGoldPer) {
        this.poGoldPer = poGoldPer;
    }

    public Integer getPoSilverPer() {
        return poSilverPer;
    }

    public void setPoSilverPer(Integer poSilverPer) {
        this.poSilverPer = poSilverPer;
    }

    public Integer getPoGreenPer() {
        return poGreenPer;
    }

    public void setPoGreenPer(Integer poGreenPer) {
        this.poGreenPer = poGreenPer;
    }

    public Integer getPoLimitAmount() {
        return poLimitAmount;
    }

    public void setPoLimitAmount(Integer poLimitAmount) {
        this.poLimitAmount = poLimitAmount;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    private String poNm;
    private Date poStartDt;
    private Date poFinishDt;
    private String poDubYn;

    public String getPoDubYn() {
        return poDubYn;
    }

    public void setPoDubYn(String poDubYn) {
        this.poDubYn = poDubYn;
    }

    public String getPoNm() {
        return poNm;
    }

    public void setPoNm(String poNm) {
        this.poNm = poNm;
    }

    public Date getPoStartDt() {
        return poStartDt;
    }

    public void setPoStartDt(Date poStartDt) {
        this.poStartDt = poStartDt;
    }

    public Date getPoFinishDt() {
        return poFinishDt;
    }

    public void setPoFinishDt(Date poFinishDt) {
        this.poFinishDt = poFinishDt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoImg() {
        return poImg;
    }

    public void setPoImg(String poImg) {
        this.poImg = poImg;
    }

    public String getPuUse() {
        return puUse;
    }

    public void setPuUse(String puUse) {
        this.puUse = puUse;
    }

    public long getPoCd() {
        return poCd;
    }

    public void setPoCd(long poCd) {
        this.poCd = poCd;
    }

    public int getMbCd() {
        return mbCd;
    }

    public void setMbCd(int mbCd) {
        this.mbCd = mbCd;
    }

    public String getPoCoup() {
        return poCoup;
    }

    public void setPoCoup(String poCoup) {
        this.poCoup = poCoup;
    }

    public long getOrCd() {
        return orCd;
    }

    public void setOrCd(long orCd) {
        this.orCd = orCd;
    }
}
