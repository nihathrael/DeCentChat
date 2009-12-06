package decentchat.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Contact extends Remote {
	
	public String getPubKey() throws RemoteException;
	public Status getStatus() throws RemoteException;
	public String getStatusMessage() throws RemoteException;
	public boolean message(String message) throws RemoteException;
	
}
