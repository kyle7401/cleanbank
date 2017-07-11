package cleanbank.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_TRACE_INFO", catalog = "cleanbank")
public class TbTraceInfo implements java.io.Serializable {

	private Long id;
	private Long orCd;
	private Integer epCd;
	private String tiLati;
	private String tiLong;
	private Date regiDt;
	private String addr1;

	public TbTraceInfo() {
	}

	public TbTraceInfo(Long orCd, Integer epCd, String tiLati, String tiLong, Date regiDt, String addr1) {
		this.orCd = orCd;
		this.epCd = epCd;
		this.tiLati = tiLati;
		this.tiLong = tiLong;
		this.regiDt = regiDt;
		this.addr1 = addr1;
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

	@Column(name = "OR_CD")
	public Long getOrCd() {
		return this.orCd;
	}

	public void setOrCd(Long orCd) {
		this.orCd = orCd;
	}

	@Column(name = "EP_CD")
	public Integer getEpCd() {
		return this.epCd;
	}

	public void setEpCd(Integer epCd) {
		this.epCd = epCd;
	}

	@Column(name = "TI_LATI", length = 50)
	public String getTiLati() {
		return this.tiLati;
	}

	public void setTiLati(String tiLati) {
		this.tiLati = tiLati;
	}

	@Column(name = "TI_LONG", length = 50)
	public String getTiLong() {
		return this.tiLong;
	}

	public void setTiLong(String tiLong) {
		this.tiLong = tiLong;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGI_DT", length = 19)
	public Date getRegiDt() {
		return this.regiDt;
	}

	public void setRegiDt(Date regiDt) {
		this.regiDt = regiDt;
	}

	@Column(name = "ADDR1", length = 100)
	public String getAddr1() {
		return this.addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

}
