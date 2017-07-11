package cleanbank.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TbPromoLocation generated by hbm2java
 */
@Entity
@Table(name = "TB_PROMO_LOCATION", catalog = "cleanbank")
public class TbPromoLocation implements java.io.Serializable {

	private Long id;
	private String plCd;
	private String delYn;
	private String evtNm;
	private Date regiDt;
	private String user;
	private String plNm;
	private long poCd;

	public TbPromoLocation() {
	}

	public TbPromoLocation(String plCd, long poCd) {
		this.plCd = plCd;
		this.poCd = poCd;
	}

	public TbPromoLocation(String plCd, String delYn, String evtNm, Date regiDt, String user, String plNm, long poCd) {
		this.plCd = plCd;
		this.delYn = delYn;
		this.evtNm = evtNm;
		this.regiDt = regiDt;
		this.user = user;
		this.plNm = plNm;
		this.poCd = poCd;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PL_CD", nullable = false, length = 6)
	public String getPlCd() {
		return this.plCd;
	}

	public void setPlCd(String plCd) {
		this.plCd = plCd;
	}

	@Column(name = "DEL_YN", length = 1)
	public String getDelYn() {
		return this.delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	@Column(name = "EVT_NM", length = 30)
	public String getEvtNm() {
		return this.evtNm;
	}

	public void setEvtNm(String evtNm) {
		this.evtNm = evtNm;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGI_DT", length = 19)
	public Date getRegiDt() {
		return this.regiDt;
	}

	public void setRegiDt(Date regiDt) {
		this.regiDt = regiDt;
	}

	@Column(name = "USER", length = 20)
	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Column(name = "PL_NM", length = 100)
	public String getPlNm() {
		return this.plNm;
	}

	public void setPlNm(String plNm) {
		this.plNm = plNm;
	}

	@Column(name = "PO_CD", nullable = false)
	public long getPoCd() {
		return this.poCd;
	}

	public void setPoCd(long poCd) {
		this.poCd = poCd;
	}

	//	이하 수정 혹은 추가 ------------------------------------------------------------------------
	private String mode;

	//	신규/수정
	@Transient
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	//	상/중/하 지역콤보 연동용
	private String loc1;
	private String loc2;
	private String loc2_cd;	// 그리드 표시용
	private String loc3;

	@Transient
	public String getLoc2_cd() {
		return this.getLoc1() + this.getLoc2();
	}

	public void setLoc2_cd(String loc2_cd) {
		this.loc2_cd = loc2_cd;
	}

	@Transient
	public String getLoc1() {
		String tmpLoc = this.loc1;

		if(StringUtils.isEmpty(tmpLoc) && StringUtils.isNotEmpty(this.getPlCd()) && this.getPlCd().length() >= 6) {
			tmpLoc = this.getPlCd().substring(0, 2);
		}

		return tmpLoc;
	}

	public void setLoc1(String loc1) {
		this.loc1 = loc1;
	}

	@Transient
	public String getLoc2() {
		String tmpLoc = this.loc2;

		if(StringUtils.isEmpty(tmpLoc) && StringUtils.isNotEmpty(this.getPlCd()) && this.getPlCd().length() >= 6) {
			tmpLoc = this.getPlCd().substring(2, 4);
		}

		return tmpLoc;
	}

	public void setLoc2(String loc2) {
		this.loc2 = loc2;
	}

	@Transient
	public String getLoc3() {
		String tmpLoc = this.loc3;

		if(StringUtils.isEmpty(tmpLoc) && StringUtils.isNotEmpty(this.getPlCd()) && this.getPlCd().length() >= 6) {
			tmpLoc = this.getPlCd().substring(4, 6);
		}

		return tmpLoc;
	}

	public void setLoc3(String loc3) {
		this.loc3 = loc3;
	}
}