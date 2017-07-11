package cleanbank.viewmodel;

import java.util.List;

/*주문번호
포인트
쿠폰번호(n건)
최종 결재금액*/
public class MoOrder3 implements java.io.Serializable {
	private Long orCd;
	private Integer orCharge;
	private Integer orPoint;
	private List<Integer> poCds;
	private Integer orExtMoney;

	public Integer getOrExtMoney() {
		return orExtMoney;
	}

	public void setOrExtMoney(Integer orExtMoney) {
		this.orExtMoney = orExtMoney;
	}

	public List<Integer> getPoCds() {
		return poCds;
	}

	public void setPoCds(List<Integer> poCds) {
		this.poCds = poCds;
	}

	public Long getOrCd() {
		return orCd;
	}

	public void setOrCd(Long orCd) {
		this.orCd = orCd;
	}

	public Integer getOrPoint() {
		return orPoint;
	}

	public void setOrPoint(Integer orPoint) {
		this.orPoint = orPoint;
	}

	public Integer getOrCharge() {
		return orCharge;
	}

	public void setOrCharge(Integer orCharge) {
		this.orCharge = orCharge;
	}
}