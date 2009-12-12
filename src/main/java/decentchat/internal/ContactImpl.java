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
