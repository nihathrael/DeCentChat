package decentchat.internal;

import java.rmi.RemoteException;
import java.security.PublicKey;

import decentchat.api.Contact;
import decentchat.api.ContactEventHandler;
import decentchat.api.Status;
import decentchat.exceptions.ContactOfflineException;
import decentchat.internal.remotes.ProtocolInterface;


public class ContactImpl implements Contact {
	
	private PublicKey publicKey;
	private Status status;
	private String statusMessage;
	private ContactEventHandler eventHandler;
	private ProtocolInterface protocolInterface;
	
	public ContactImpl(PublicKey pubkey, Status status) {
		this.status = status;
		this.publicKey = pubkey;
		eventHandler = null;
		status = null;
		statusMessage = null;
		protocolInterface = null;
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
	public void sendMessage(String message) throws ContactOfflineException {
		if (protocolInterface == null) {
			throw new ContactOfflineException();
		} else {
			try {
				protocolInterface.sendMessage(message);
			} catch (RemoteException e) {
				throw new ContactOfflineException();
			}
		}
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
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
	
	/**
	 * Stores the given {@link ProtocolInterface} and notifies the
	 * {@link ContactEventHandler} that this contact is now
	 * online.
	 * @param pushInterface The {@link ProtocolInterface} of this
	 * contact.
	 */
	public void setOnline(ProtocolInterface pushInterface) {
		this.protocolInterface = pushInterface;
		new Thread(new Runnable() {
			@Override
			public void run() {
				eventHandler.onOnline();
			}
		}).start();
	}

	/**
	 * Notifies the {@link ContactEventHandler} that this contact
	 * is now offline.
	 */
	public void setOffline() {
		protocolInterface = null;
		new Thread(new Runnable() {
			@Override
			public void run() {
				eventHandler.onOffline();
			}
		}).start();
	}

}
