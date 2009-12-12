package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

public class PullInterfaceImpl extends UnicastRemoteObject implements PullInterface {
	
	private static final long serialVersionUID = 470475401844483791L;

	protected PullInterfaceImpl() throws RemoteException {
		super();
	}

	static Logger logger = Logger.getLogger(PullInterfaceImpl.class);

	@Override
	public String authenticate(int nonce) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ping() throws RemoteException {
		// TODO is there anything that needs to be done
		// here at all, except maybe logging?
	}

}
