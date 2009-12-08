package decentchat.api;

/**
 * Every client has to implement the {@link ContactEventHandler}. It
 * has to supply it, so that the library can generate events to notify
 * the client about changes to the contacts. 
 */
public interface ContactEventHandler {

	/**
	 * This event is generated when the client receives a message from
	 * the contact that this {@link ContactEventHandler} is registered to.
	 * @param message Message that has been sent by the client.
	 */
	public void onMessageReceived(String message);
	
	/**
	 * On status changed is called when the contact changes it's status.
	 */
	public void onStatusChanged();
	
	/**
	 * {@link #onStatusMessageChanged(String)} is called when the contact 
	 * changes it's status message.
	 */
	public void onStatusMessageChanged(String message);
	
}
