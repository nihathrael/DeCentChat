package decentchat.api;

import java.rmi.Naming;

import decentchat.internal.nodes.Node;

public class DeCentInstance {
	
	private String ip;
	private int port;

	/**
	 * The {@link DeCentInstance} is the main instance the client will be 
	 * using when using the DeCentChat protocol. It defines the main
	 * public interface.
	 *   
	 */
	public DeCentInstance() {		
	}
	
	public boolean init(String bootstrap_ip, int port) {
		this.port = port;
		try {
			Node bootstrapNode = (Node)Naming.lookup("rmi://" + bootstrap_ip +":" +this.port+ "/node");
			ip = bootstrapNode.getIp();
		} catch (Exception e) {
			System.err.println("Problem connecting to " + bootstrap_ip + ":" + port);
			return false;
		}
		if(ip == null) return false;
		return true;
	}
	
	public int getPort() {
		return this.port;
	}
	
}
