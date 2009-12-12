package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import decentchat.api.Status;
import decentchat.exceptions.ContactNotFoundException;
import decentchat.internal.ContactImpl;

public class PushInterfaceImpl extends UnicastRemoteObject implements PushInterface {

	private static final long serialVersionUID = -4017058237863632726L;

	static Logger logger = Logger.getLogger(PushInterfaceImpl.class);

	private Registry registry;
	
	public PushInterfaceImpl(Registry registry) throws RemoteException {
		super();
		this.registry = registry;
	}
	
	/**
	 * Looks up the contact that belongs to the
	 * calling node. It does so in a two step
	 * process:
	 * 1. It tries to find it by IP
	 * 2. If that fails, it gets it's push
	 *    interface and from there it's public key
	 *    and tries to find it by that.
	 * @return The contact associated with the
	 * calling node.
	 */
	private ContactImpl getContact() throws ContactNotFoundException {
		assertAuthenticated();
		// TODO implement
		return null;
	}

	private void assertAuthenticated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyOffline() throws RemoteException {
		try {
			getContact().setOffline();
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		}
	}

	@Override
	public void notifyOnline() throws RemoteException {
		try {
			ContactImpl contact = getContact();
			contact.setOnline(getPushInterfaceForContact(contact));
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		}
	}

	private PushInterface getPushInterfaceForContact(ContactImpl contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		try {
			getContact().receiveMessage(message);
		} catch (ContactNotFoundException e) {
			// TODO client needs to be notified that someone
			// unknown tries to contact him
		}
	}

	@Override
	public void setStatus(Status status) throws RemoteException {
		try {
			getContact().setStatus(status);
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		}
	}

	@Override
	public void setStatusMessage(String message) throws RemoteException {
		try {
			getContact().setStatusMessage(message);
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		}
	}

}
