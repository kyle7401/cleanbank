package cleanbank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TbFaq generated by hbm2java
 */
@Entity
@Table(name = "TB_FAQ", catalog = "cleanbank")
public class TbFaq {

	private Integer id;
	private String faTitle;
	private String faMemo;

	public TbFaq() {
	}

	public TbFaq(String faTitle, String faMemo, String delYn, String evtNm, Date regiDt, String user) {
		this.faTitle = faTitle;
		this.faMemo = faMemo;
		this.delYn = delYn;
		this.evtNm = evtNm;
		this.regiDt = regiDt;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "FA_TITLE", length = 100)
	public String getFaTitle() {
		return this.faTitle;
	}

	public void setFaTitle(String faTitle) {
		this.faTitle = faTitle;
	}

	@Column(name = "FA_MEMO", length = 2000)
	public String getFaMemo() {
		return this.faMemo;
	}

	public void setFaMemo(String faMemo) {
		this.faMemo = faMemo;
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
	@JsonIgnore
	@Transient
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGI_DT", length = 19)
	public Date getRegiDt() {
		return this.regiDt;
	}

	@JsonIgnore
	private String delYn;

	@JsonIgnore
	private String evtNm;

//	@JsonIgnore
	private Date regiDt;

	@JsonIgnore
	private String user;
}