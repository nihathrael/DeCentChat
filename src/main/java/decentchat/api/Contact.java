package decentchat.api;

import java.security.PublicKey;

import decentchat.exceptions.ContactOfflineException;

public interface Contact {
	
	public PublicKey getPublicKey();
	public Status getStatus();
	public String getStatusMessage();
	public void sendMessage(String message) throws ContactOfflineException;
	public void setEventHandler(ContactEventHandler eventHandler);
	public ContactEventHandler getEventHandler();
	
}
