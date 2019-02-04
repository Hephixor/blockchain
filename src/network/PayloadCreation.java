package network;

public class PayloadCreation extends Payload{
	private String name;
	private String description;
	private String dateBegin;
	private String dateEnd;
	private String dateEndSubscription;
	private String location;
	private int limitMin;
	private int limitMax;
	
	
	public PayloadCreation() {}
	
	public PayloadCreation(String name, String description, String location, String dateBegin, String dateEnd, String dateEndSubscription,  int limitMin, int limitMax) {
		this.name = name;
		this.description = description;
		this.dateBegin = dateBegin;
		this.dateEnd = dateEnd;
		this.dateEndSubscription = dateEndSubscription;
		this.location = location;
		this.limitMin = limitMin;
		this.limitMax = limitMax;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDateEndSubscription() {
		return dateEndSubscription;
	}
	public void setDateEndSubscription(String dateEndSubscription) {
		this.dateEndSubscription = dateEndSubscription;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(int limitMin) {
		this.limitMin = limitMin;
	}
	public int getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(int limitMax) {
		this.limitMax = limitMax;
	}
	

}
