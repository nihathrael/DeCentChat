package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PrivateKey;

import org.apache.log4j.Logger;

import decentchat.api.DeCentInstance;

public class PullInterfaceImpl extends UnicastRemoteObject implements PullInterface {
	
	private static final long serialVersionUID = 470475401844483791L;
	private DeCentInstance instance;

	public PullInterfaceImpl(DeCentInstance instance) throws RemoteException {
		super();
		this.instance = instance;
	}

	static Logger logger = Logger.getLogger(PullInterfaceImpl.class);

	@Override
	public String authenticate(int nonce) {
		String message = instance.getIP() + "/" + nonce;
		PrivateKey privkey = instance.getPrivateKey();
		// TODO encrypt message with private key
		return message;
	}

	@Override
	public void ping() throws RemoteException {
		// TODO is there anything that needs to be done
		// here at all, except maybe logging?
	}

}
