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
	public void receiveMessage(final String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				eventHandler.onMessageReceived(message);
			}
		}).start();
	}
	
	/**
	 * Starts a thread that notifies the {@link ContactEventHandler}
	 * of this contact of a new status message.
	 * @param statusMessage The new status message of this
	 * contact.
	 */
	public void setStatusMessage(final String statusMessage) {
		this.statusMessage = statusMessage;
		new Thread(new Runnable() {
			@Override
			public void run() {
				eventHandler.onStatusMessageChanged(statusMessage);
			}
		}).start();
	}

	
	/**
	 * Starts a thread that notifies the {@link ContactEventHandler}
	 * of this contact of a new {@link Status}.
	 * @param statusMessage The new {@link Status} of this
	 * contact.
	 */
	public void setStatus(final Status newStatus) {
		this.status = newStatus;
		new Thread(new Runnable() {
			@Override
			public void run() {
				eventHandler.onStatusChanged(newStatus);
			}
		}).start();
	}

}
