package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PrivateKey;

import org.apache.log4j.Logger;

import decentchat.api.DeCentInstance;
import decentchat.api.Status;
import decentchat.exceptions.ContactNotFoundException;
import decentchat.internal.ContactImpl;

public class ProtocolInterfaceImpl extends UnicastRemoteObject implements ProtocolInterface {

	private static final long serialVersionUID = -4017058237863632726L;

	static Logger logger = Logger.getLogger(ProtocolInterfaceImpl.class);

	private DeCentInstance instance;
	
	public ProtocolInterfaceImpl(DeCentInstance instance) throws RemoteException {
		super();
		this.instance = instance;
	}
	
	/**
	 * Looks up the contact that belongs to the
	 * calling node. It does so in a two step
	 * process:
	 * 1. It tries to find it by IP
	 * 2. If that fails, it gets it's protocol
	 *    interface and from there it's public key
	 *    and tries to find it by that.
	 * @return The contact associated with the
	 * calling node.
	 */
	private ContactImpl getContact() throws ContactNotFoundException {
		if (!authenticate()) {
			throw new ContactNotFoundException();
		}
		// TODO implement
		return null;
	}

	private boolean authenticate() {
		// TODO don't do this everytime
		// TODO maybe there already is some java method for this?
		int nonce = 0; // TODO generate real nonce
		String message = authenticate(nonce);
		// TODO decrypt message
		try {
			if (!message.equals(getClientHost() + "/" + nonce)) {
				return false;
			} else {
				return true;
			}
		} catch (ServerNotActiveException e) {
			return false;
		}
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
			contact.setOnline(getProtocolInterfaceForContact(contact));
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		}
	}

	private ProtocolInterface getProtocolInterfaceForContact(ContactImpl contact) {
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

	@Override
	public String authenticate(int nonce) {
		String message = instance.getIP() + "/" + nonce;
		PrivateKey privkey = instance.getPrivateKey();
		// TODO encrypt message with private key
		return message;
	}

	@Override
	public void ping() throws RemoteException {
		// TODO is there anything that needs to be done
		// here at all, except maybe logging?
	}


}
