package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.log4j.Logger;

import decentchat.api.DeCentInstance;
import decentchat.api.Status;
import decentchat.exceptions.ContactNotFoundException;
import decentchat.internal.ContactImpl;
import decentchat.util.Encryption;

public class ProtocolInterfaceImpl extends UnicastRemoteObject implements ProtocolInterface {

	private static final long serialVersionUID = -4017058237863632726L;

	static Logger logger = Logger.getLogger(ProtocolInterfaceImpl.class);

	private DeCentInstance instance;
	private Map<String, ContactImpl> contacts = new HashMap<String, ContactImpl>();
	
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
		try {
			String ip = getClientHost();
			if(contacts.containsKey(ip)) {
				ContactImpl contact = contacts.get(ip);
				if (!isAuthenticated(contact.getProtocolInterface())) {
					throw new ContactNotFoundException();
				}
				return contact;
			}
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		throw new ContactNotFoundException();
	}
	
	/**
	 * Looks up a contact by using it's {@link ProtocolInterface}.
	 * @return The contact associated with the
	 * calling node.
	 */
	private ContactImpl getContact(ProtocolInterface prot) throws ContactNotFoundException {
		if (!isAuthenticated(prot)) {
			throw new ContactNotFoundException();
		}
		PublicKey key = null;
		try {
			key = prot.getPubKey();
		} catch (RemoteException e) {
			throw new ContactNotFoundException();
		}
		ContactImpl ret = null;
		ret = (ContactImpl) instance.getContactManager().getContact(key);
		return ret;
	}

	/**
	 * Performs the authentication challenge on the currently
	 * connected client. 
	 * @return <code>true</code> on successful authentication
	 * and <code>false</code> on a failure (of any kind).
	 */
	private boolean isAuthenticated(ProtocolInterface protocolInterface) {
		// TODO don't do this everytime
		String ip;
		try {
			ip = getClientHost();
		} catch (ServerNotActiveException e) {
			logger.debug("getClientHost failed in isAuthenticated", e);
			return false;
		}
		long nonce = new SecureRandom().nextLong();
		byte[] message = null;
		try {
			message = protocolInterface.authenticate(nonce);
			PublicKey pubkey = protocolInterface.getPubKey();
			Cipher cipher = Encryption.getCipher();
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			message = cipher.doFinal(message);
			return new String(message).equals(ip + "/" + nonce);
		} catch (RemoteException e) {
			logger.debug("Authentication failed for " + ip, e);
			return false;
		} catch (InvalidKeyException e) {
			logger.debug("Authentication failed for " + ip, e);
			return false;
		} catch (IllegalBlockSizeException e) {
			logger.debug("Authentication failed for " + ip, e);
			return false;
		} catch (BadPaddingException e) {
			logger.debug("Authentication failed for " + ip, e);
			return false;
		}
	}

	@Override
	public void notifyOffline() throws RemoteException {
		try {
			getContact().setOffline();
			contacts.remove(getClientHost());
		} catch (ContactNotFoundException e) {
			// We're not interested then.
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyOnline(ProtocolInterface protocolInterface) throws RemoteException {
		try {
			ContactImpl contact = getContact(protocolInterface);
			if(contact != null) {
				contact.setOnline(protocolInterface);
				contacts.put(getClientHost(), contact);
			}
		} catch (ContactNotFoundException e) {
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
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
	public byte[] authenticate(long nonce) throws RemoteException {
		String message = instance.getIP() + "/" + nonce;
		PrivateKey privkey = instance.getPrivateKey();
		Cipher cipher = Encryption.getCipher();
		try {
			cipher.init(Cipher.ENCRYPT_MODE, privkey);
			return cipher.doFinal(message.getBytes());
		} catch (InvalidKeyException e) {
			logger.debug("Authentication encryption failed", e);
			return null;
		} catch (IllegalBlockSizeException e) {
			logger.debug("Authentication encryption failed", e);
			return null;
		} catch (BadPaddingException e) {
			logger.debug("Authentication encryption failed", e);
			return null;
		}
	}

	@Override
	public void ping() throws RemoteException {
		// TODO is there anything that needs to be done
		// here at all, except maybe logging?
	}

	@Override
	public PublicKey getPubKey() throws RemoteException {
		return instance.getPublicKey();
	}


}
