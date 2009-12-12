package decentchat.api;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ContactManager implements Serializable {
	
	private static final long serialVersionUID = 8151741692244991273L;

	public Map<PublicKey, Contact> contacts;
	
	static Logger logger = Logger.getLogger(ContactManager.class);

	public ContactManager() {
		contacts = new HashMap<PublicKey, Contact>(10);
	}

	public void addContact(Contact c) {
		contacts.put(c.getPublicKey(), c);
	}

	public void removeContact(Contact c) {
		contacts.remove(c.getPublicKey());
	}

	public Contact getContact(PublicKey pubkey) {
		if(contacts.containsKey(pubkey)) {
			return contacts.get(pubkey);
		} else {
			logger.debug("No contact found with contact: " + pubkey);
			return null;
		}
	}
}