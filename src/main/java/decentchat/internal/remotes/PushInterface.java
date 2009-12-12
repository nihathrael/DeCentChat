package decentchat.internal.remotes;

import java.rmi.Remote;
import java.rmi.RemoteException;

import decentchat.api.Status;

/**
 * Encapsulates all push services, i.e. all services
 * where we want to inform a contact of certain events.
 * 
 * @author k
 */
public interface PushInterface extends Remote {

	/**
	 * Sends a message to a contact.
	 * @param message The message we want to send.
	 * @throws RemoteException
	 */
	public void sendMessage(String message) throws RemoteException;

	/**
	 * Tells a contact that we want to go offline soon.
	 * @throws RemoteException
	 */
	public void notifyOffline() throws RemoteException;
	
	/**
	 * Tells a contact that we are now online.
	 * @throws RemoteException
	 */
	public void notifyOnline() throws RemoteException;

	/**
	 * Tells a contact that our {@link Status} has changed.
	 * @param status Our current @link Status}.
	 */
	public void setStatus(Status status) throws RemoteException;

	/**
	 * Tells a contact that our status message has changed.
	 * @param message Our current status message.
	 */
	public void setStatusMessage(String message) throws RemoteException;

}
