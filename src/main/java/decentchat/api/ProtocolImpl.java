package decentchat.api;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ProtocolImpl extends UnicastRemoteObject implements ProtocolInterface {
	
	private static final long serialVersionUID = -1975545716673200520L;
	
	static Logger logger = Logger.getLogger(ProtocolImpl.class);
	
	protected ProtocolImpl() throws RemoteException {
		super();
	}

	private Map<String, Contact> contacts = new HashMap<String, Contact>(); 

	@Override
	public boolean authorize(String pubkey, String encryptedIP)
			throws RemoteException {
		boolean ret = false;
		//
		// TODO Check ip by comparing the encrypted ip
		// TODO Add decryption here
		String decryptedIP = encryptedIP;
		Contact contact = getCurrentContact();
		try {
			if(decryptedIP == ProtocolImpl.getClientHost()) {
				contact.setIP(decryptedIP);
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
			currentContact.message(message);
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
		} 
	}

	@Override
	public void notifyOnline() throws RemoteException {
		Contact currentContact = getCurrentContact();
		if(currentContact != null) {
			currentContact.setStatus(Status.ONLINE);
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
