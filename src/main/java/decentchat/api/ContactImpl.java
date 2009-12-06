package decentchat.api;

import java.rmi.RemoteException;

public class ContactImpl implements Contact {
	
	private String pubKey = null;
	private Status status = null;
	private String statusMessage = "";
	
	public ContactImpl(String pubkey, Status status) {
		this.status = status;
		this.pubKey = pubkey;
	}

	@Override
	public String getPubKey() throws RemoteException {
		return pubKey;
	}

	@Override
	public Status getStatus() throws RemoteException {
		return status;
	}

	@Override
	public boolean message(String message) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String getStatusMessage() throws RemoteException {
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
