package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import decentchat.api.Contact;
import decentchat.api.DeCentInstance;
import decentchat.api.Status;

public class ProtocolImpl extends UnicastRemoteObject implements ProtocolInterface {
	
	private static final long serialVersionUID = -1975545716673200520L;
	
	static Logger logger = Logger.getLogger(ProtocolImpl.class);
	
	private Map<String, Contact> contacts = new HashMap<String, Contact>();
	private final DeCentInstance decentInstance;
	
	public ProtocolImpl(DeCentInstance deCentInstance) throws RemoteException {
		super();
		this.decentInstance = deCentInstance;
	}


	@Override
	public boolean authorize(PublicKey pubkey, String encryptedIP)
			throws RemoteException {
		boolean ret = false;
		//
		// TODO Check IP by comparing the encrypted IP
		// TODO Add decryption here
		String decryptedIP = encryptedIP;
		Contact contact = this.decentInstance.getContactManager().getContact(pubkey);
		try {
			if(contact != null && decryptedIP == ProtocolImpl.getClientHost()) {
				// Contact is now correctly signed on
				contact.setIP(decryptedIP);
				contact.setStatus(Status.ONLINE);
				// Add contact to the interface list.
				this.contacts.put(decryptedIP, contact);
				ret = true;
			}
		} catch (ServerNotActiveException e) {
			logger.debug("Problem retrieving IP for authorization.", e);
		}
		logger.debug("Authorization faild for contact with pubkey: " + pubkey);
		return ret;
	}

	@Override
	public String getAwayMessage() throws RemoteException {
		Contact currentContact = getCurrentContact();
		if(currentContact != null) {
			return currentContact.getStatusMessage();
		} else {
			return "";
		}
	}

	@Override
	public boolean message(String message) throws RemoteException {
		Contact currentContact = getCurrentContact();
		if(currentContact != null) {
			currentContact.sendMessage(message);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void notifyOffline() throws RemoteException {
		Contact currentContact = getCurrentContact();
		if(currentContact != null) {
			currentContact.setStatus(Status.OFFLINE);
			this.contacts.remove(currentContact);
		} 
	}

	@Override
	public void setStatus(Status status) throws RemoteException {
		Contact currentContact = getCurrentContact();
		if(currentContact != null) {
			currentContact.setStatus(status);
		} 
	}
	
	private Contact getCurrentContact() {
		try {
			return this.contacts.get(ProtocolImpl.getClientHost());
		} catch (ServerNotActiveException e) {
			logger.debug("Couldn't retrieve client ip.", e);
		}
		logger.debug("Couldn't find Contact for this client IP.");
		return null;
	}

}
