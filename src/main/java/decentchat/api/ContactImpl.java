package decentchat.api;


public class ContactImpl implements Contact {
	
	private String pubKey = null;
	private Status status = null;
	private String statusMessage = "";
	private ContactEventHandler eventHandler;
	private String ip = "";
	
	public ContactImpl(String pubkey, Status status, ContactEventHandler handler) {
		this.status = status;
		this.pubKey = pubkey;
		this.eventHandler = handler;
	}

	@Override
	public String getPubKey() {
		return pubKey;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void message(String message) {
		this.eventHandler.onMessageReceived(message);
	}

	@Override
	public void setStatus(Status newStatus) {
		this.status = newStatus;
		this.eventHandler.onStatusChanged();
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		this.eventHandler.onStatusMessageChanged(statusMessage);
	}

	@Override
	public String getIP() {
		return this.ip;
	}

	@Override
	public void setIP(String ip) {
		this.ip = ip;
	}

}
