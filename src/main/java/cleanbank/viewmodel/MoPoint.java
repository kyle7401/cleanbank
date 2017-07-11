package cleanbank.viewmodel;

import java.util.Date;

/**
 * Created by hyoseop on 2016-01-25.
 */
/*@NoArgsConstructor
@AllArgsConstructor*/
/*@Data
@RequiredArgsConstructor*/
public class MoPoint {
    private int mbCd;
    private long orCd;
    private int point;
    private String plUseMemo;
    private Date piUseDt;

    public Date getPiUseDt() {
        return piUseDt;
    }

    public void setPiUseDt(Date piUseDt) {
        this.piUseDt = piUseDt;
    }

    public String getPlUseMemo() {
        return plUseMemo;
    }

    public void setPlUseMemo(String plUseMemo) {
        this.plUseMemo = plUseMemo;
    }

    public int getMbCd() {
        return mbCd;
    }

    public void setMbCd(int mbCd) {
        this.mbCd = mbCd;
    }

    public long getOrCd() {
        return orCd;
    }

    public void setOrCd(long orCd) {
        this.orCd = orCd;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
