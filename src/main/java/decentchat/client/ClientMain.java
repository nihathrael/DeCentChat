package decentchat.client;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import decentchat.api.DeCentInstance;

public class ClientMain {

	static Logger logger = Logger.getLogger(ClientMain.class);

	private final DeCentInstance decentInstance;

	public ClientMain(String ip, int port, String bootstrapIP, int bootstrapPort) {
		logger.info("Starting Client...");
		decentInstance = new DeCentInstance();
		if (ip != null) {
			decentInstance.init(ip, port); // We want to create a new network
		} else {
			// Join an existing network
			decentInstance.init(bootstrapIP, bootstrapPort, port);
		}
		logger.info("Created the decentInstance");
		while(true) {
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		//Parse arguments
		String ip = null;
		int port = 1099;
		String bootstrapIP = null;
		int bootstrapPort = 1099;
        OptionParser parser = new OptionParser();
        
        // Command line options we accept
        parser.accepts("ip").withRequiredArg();
        parser.accepts("port").withRequiredArg();
        parser.accepts("conip").withRequiredArg();
        parser.accepts("conport").withRequiredArg();
        
       	OptionSet options = parser.parse(args);
        if(options.has("ip")) {
        	ip = (String) options.valueOf("ip");
        } 
        if(options.has("port")) {
        	port = Integer.valueOf((String)options.valueOf("port"));
        }
        if(options.has("conip")) {
        	bootstrapIP = (String) options.valueOf("conip");
        }
        if(options.has("conport")) {
        	bootstrapPort = Integer.valueOf((String)options.valueOf("conport"));
        }
        if(ip == null && bootstrapIP == null) {
        	logger.error("You must specifiy either an ip to create a new network,\n" +
        			"or an ip and port to connect to a network");
        	System.exit(1);
        }
        ClientMain main = new ClientMain(ip, port, bootstrapIP, bootstrapPort);
	}
}
