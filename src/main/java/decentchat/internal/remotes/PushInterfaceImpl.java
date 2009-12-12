package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import decentchat.api.Status;
import decentchat.internal.ContactImpl;

public class PushInterfaceImpl extends UnicastRemoteObject implements PushInterface {

	private static final long serialVersionUID = -4017058237863632726L;

	protected PushInterfaceImpl() throws RemoteException {
		super();
	}

	static Logger logger = Logger.getLogger(PushInterfaceImpl.class);
	
	private ContactImpl getContact() {
		return null;
	}

	@Override
	public void notifyOffline() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyOnline() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String message) throws RemoteException {
		getContact().receiveMessage(message);
	}

	@Override
	public void setStatus(Status status) throws RemoteException {
		getContact().setStatus(status);
	}

	@Override
	public void setStatusMessage(String message) throws RemoteException {
		getContact().setStatusMessage(message);
	}

}
