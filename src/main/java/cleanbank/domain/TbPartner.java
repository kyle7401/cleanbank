package cleanbank.domain;

import java.util.Date;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TbPartner generated by hbm2java
 */
@Entity
@Table(name = "TB_PARTNER", catalog = "cleanbank")
public class TbPartner {

	private Integer ptCd;
	private String delYn;
	private String evtNm;
	private String ptFax;
	private String ptNm;
	private Integer ptPer;
	private String ptTel;
	private Date regiDt;
	private String user;

	public TbPartner() {
	}

	public TbPartner(String delYn, String evtNm, String ptFax, String ptNm, Integer ptPer, String ptTel, Date regiDt,
			String user) {
		this.delYn = delYn;
		this.evtNm = evtNm;
		this.ptFax = ptFax;
		this.ptNm = ptNm;
		this.ptPer = ptPer;
		this.ptTel = ptTel;
		this.regiDt = regiDt;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "PT_CD", unique = true, nullable = false)
	public Integer getPtCd() {
		return this.ptCd;
	}

	public void setPtCd(Integer ptCd) {
		this.ptCd = ptCd;
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

	@Column(name = "PT_FAX", length = 20)
	public String getPtFax() {
		return this.ptFax;
	}

	public void setPtFax(String ptFax) {
		this.ptFax = ptFax;
	}

	@Column(name = "PT_NM", length = 30)
	public String getPtNm() {
		return this.ptNm;
	}

	public void setPtNm(String ptNm) {
		this.ptNm = ptNm;
	}

	@Column(name = "PT_PER")
	public Integer getPtPer() {
		return this.ptPer;
	}

	public void setPtPer(Integer ptPer) {
		this.ptPer = ptPer;
	}

	@Column(name = "PT_TEL", length = 20)
	public String getPtTel() {
		return this.ptTel;
	}

	public void setPtTel(String ptTel) {
		this.ptTel = ptTel;
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
}
