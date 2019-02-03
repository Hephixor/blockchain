package network;

public class PayloadRegister extends Payload{
	private String eventHash;
	
	public PayloadRegister() {}
	
	public PayloadRegister(String evtHash) {
		this.eventHash = evtHash;
	}
	
	public String getEventHash() {
		return eventHash;
	}
	
	public void setEventHash(String evtHash) {
		this.eventHash = evtHash;
	}

}
