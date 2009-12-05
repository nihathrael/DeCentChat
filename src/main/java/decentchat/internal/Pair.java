package decentchat.internal;

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable {

	private static final long serialVersionUID = 8455238310501534669L;
	
	public T1 first;
	public T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
}
