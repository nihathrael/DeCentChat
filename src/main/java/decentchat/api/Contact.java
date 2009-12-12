package decentchat.api;

public interface Contact {
	
	public String getPubKey();
	public Status getStatus();
	public void setStatus(Status newStatus);
	public String getStatusMessage();
	public void setIP(String ip);
	public String getIP();
	public void message(String message);
	
}
