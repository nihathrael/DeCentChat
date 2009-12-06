package decentchat.internal;

import org.junit.*;
import static org.junit.Assert.*;

public class HasherTest {

	@Test
	public void testHash() {
		byte[] hash = Hasher.generateHash("foobar");
		assertEquals("8843d7f92416211de9ebb963ff4ce28125932878",
				new NodeKey(hash).toString());
	}

}
