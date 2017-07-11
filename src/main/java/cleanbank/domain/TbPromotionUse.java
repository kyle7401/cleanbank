package cleanbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "TB_PROMOTION_USE", catalog = "cleanbank")
public class TbPromotionUse implements java.io.Serializable {

	private Long id;
	private TbPromotion tbPromotion;
	private String delYn;
	private String evtNm;
	private int mbCd;
	private String puUse;
	private Date puUseDt;
	private Date regiDt;
	private String user;
	private Long orCd;
	private String useYn;

	public TbPromotionUse() {
	}

	public TbPromotionUse(TbPromotion tbPromotion, int mbCd) {
		this.tbPromotion = tbPromotion;
		this.mbCd = mbCd;
	}

	public TbPromotionUse(TbPromotion tbPromotion, String delYn, String evtNm, int mbCd, String puUse, Date puUseDt,
						  Date regiDt, String user, Long orCd, String useYn) {
		this.tbPromotion = tbPromotion;
		this.delYn = delYn;
		this.evtNm = evtNm;
		this.mbCd = mbCd;
		this.puUse = puUse;
		this.puUseDt = puUseDt;
		this.regiDt = regiDt;
		this.user = user;
		this.orCd = orCd;
		this.useYn = useYn;
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

	public void setTbPromotion(TbPromotion tbPromotion) {
		this.tbPromotion = tbPromotion;
	}

	@Column(name = "DEL_YN")
	public String getDelYn() {
		return this.delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	@Column(name = "EVT_NM")
	public String getEvtNm() {
		return this.evtNm;
	}

	public void setEvtNm(String evtNm) {
		this.evtNm = evtNm;
	}

	@Column(name = "MB_CD", nullable = false)
	public int getMbCd() {
		return this.mbCd;
	}

	public void setMbCd(int mbCd) {
		this.mbCd = mbCd;
	}

	@Column(name = "PU_USE", length = 100)
	public String getPuUse() {
		return this.puUse;
	}

	public void setPuUse(String puUse) {
		this.puUse = puUse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PU_USE_DT", length = 19)
	public Date getPuUseDt() {
		return this.puUseDt;
	}

	public void setPuUseDt(Date puUseDt) {
		this.puUseDt = puUseDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGI_DT", length = 19)
	public Date getRegiDt() {
		return this.regiDt;
	}

	public void setRegiDt(Date regiDt) {
		this.regiDt = regiDt;
	}

	@Column(name = "USER")
	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Column(name = "OR_CD")
	public Long getOrCd() {
		return this.orCd;
	}

	public void setOrCd(Long orCd) {
		this.orCd = orCd;
	}

	@Column(name = "USE_YN", length = 1)
	public String getUseYn() {
		return this.useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	//	이하 수정 혹은 추가 ------------------------------------------------------------------------

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PO_CD", nullable = false)
	public TbPromotion getTbPromotion() {
		return this.tbPromotion;
	}
}
