package decentchat.api;

import java.security.PublicKey;

public interface Contact {
	
	public PublicKey getPublicKey();
	public Status getStatus();
	public String getStatusMessage();
	public String getIP();
	public void sendMessage(String message);
	public void setEventHandler(ContactEventHandler eventHandler);
	public ContactEventHandler getEventHandler();
	
}
