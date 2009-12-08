package decentchat.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProtocolInterface extends Remote {

	public boolean message(String message) throws RemoteException;

	/**
	 * Authorize with the other client to verify our IP This has to be done
	 * before {@link ProtocolInterface#notifyOnline(String)} or
	 * {@link ProtocolInterface#notifyOffline(String)} is called.
	 * 
	 * @param pubkey
	 *            The public key we are verifying the IP for.
	 * @param encryptedIP
	 *            The same IP as the IP parameter, but encrypted with our private
	 *            key. The client can use this to verify that we are who we are
	 *            saying we are.
	 */
	// TODO Witch data type should pubkey have?
	public boolean authorize(String pubkey, String encryptedIP) throws RemoteException;

	public void notifyOnline() throws RemoteException;
	public void notifyOffline() throws RemoteException;
	
	/** 
	 * Sets our {@link Status} at the client. 
	 * @param status {@link Status} we want to set.
	 */
	public void setStatus(Status status) throws RemoteException;
	
	/**
	 * Use this to retrieve the current Away/Online Message.
	 * @return The current Away/Online message.
	 */
	public String getAwayMessage() throws RemoteException;

}
