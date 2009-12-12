package decentchat.api;

import java.security.PublicKey;

public interface Contact {
	
	public PublicKey getPublicKey();
	public Status getStatus();
	public void setStatus(Status newStatus);
	public String getStatusMessage();
	public void setIP(String ip);
	public String getIP();
	public void sendMessage(String message);
	
}
