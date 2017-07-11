package cleanbank.viewmodel;

public class MoOrderItems2 implements java.io.Serializable {

	public MoOrderItems2() {
	}

	public MoOrderItems2(Long id, String itTac, String itName, String itStatus, int itPrice) {
		this.id = id;
		this.itTac = itTac;
		this.itName = itName;
		this.itStatus = itStatus;
		this.itPrice = itPrice;
	}

	private Long id;
	private String itTac;
	private String itName;
	private String itStatus;
	private int itPrice;

	public int getItPrice() {
		return itPrice;
	}

	public void setItPrice(int itPrice) {
		this.itPrice = itPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItTac() {
		return itTac;
	}

	public void setItTac(String itTac) {
		this.itTac = itTac;
	}

	public String getItName() {
		return itName;
	}

	public void setItName(String itName) {
		this.itName = itName;
	}

	public String getItStatus() {
		return itStatus;
	}

	public void setItStatus(String itStatus) {
		this.itStatus = itStatus;
	}
}