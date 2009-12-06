package decentchat.api;

public interface ContactEventHandler {

	public void onMessageReceived(String message);
	public void onStatusChanged();
	
}
