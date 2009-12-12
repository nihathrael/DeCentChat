package decentchat.internal;

import java.security.PublicKey;

import decentchat.api.Contact;
import decentchat.api.ContactEventHandler;
import decentchat.api.Status;


public class ContactImpl implements Contact {
	
	private PublicKey publicKey = null;
	private Status status = null;
	private String statusMessage = "";
	private ContactEventHandler eventHandler;
	private String ip = "";
	
	public ContactImpl(PublicKey pubkey, Status status, ContactEventHandler handler) {
		this.status = status;
		this.publicKey = pubkey;
		this.eventHandler = handler;
	}
	
	@Override
	public ContactEventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public void setEventHandler(ContactEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}


	@Override
	public PublicKey getPublicKey() {
		return publicKey;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void sendMessage(String message) {
		this.eventHandler.onMessageReceived(message);
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	@Override
	public String getIP() {
		return this.ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	/**
	 * Receives a message and starts a thread to pass it
	 * to the event handler. 
	 * @param message The message received.
	 */
	public void receiveMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		this.eventHandler.onStatusMessageChanged(statusMessage);
	}

	public void setStatus(Status newStatus) {
		this.status = newStatus;
		this.eventHandler.onStatusChanged();
	}

}
