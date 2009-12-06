package decentchat.internal;

import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;

public class NodeKeyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testEquals() {
		assertEquals(NodeKey.MAX_KEY, NodeKey.MAX_KEY);
		assertFalse(NodeKey.MAX_KEY.equals(NodeKey.MIN_KEY));
	}
	
	@Test
	public void testIncOne() {
		byte[] hash = Arrays.copyOf(Hasher.MIN_HASH, Hasher.MIN_HASH.length);
		hash[0] += 1;
		NodeKey expected = new NodeKey(hash);
	    NodeKey inced = NodeKey.MIN_KEY.inc(1);
	    assertEquals(expected, inced);
	}
	
	@Test
	public void testIncWrap() {
		byte[] hash = Arrays.copyOf(Hasher.MIN_HASH, Hasher.MIN_HASH.length);
		hash[1] += 1;
		NodeKey expected = new NodeKey(hash);
	    NodeKey inced = NodeKey.MIN_KEY.inc(256);
	    assertEquals(expected, inced);
	}
	
	@Test
	public void testIncWrapMax() {
	    NodeKey inced = NodeKey.MAX_KEY.inc(1);
	    assertEquals(NodeKey.MIN_KEY, inced);
	}
	
	@Test
	public void testNormalization() {
		byte[] byte_max = Hasher.generateHash(Byte.MAX_VALUE);
	    NodeKey inced = new NodeKey(byte_max).inc(1);
	    byte_max[0] = -128;
	    NodeKey expected = new NodeKey(byte_max);
	    assertEquals(expected, inced);
	}
	
	@Test
	public void testCompareMinMax() {
        assertEquals(-1, NodeKey.MIN_KEY.compareTo(NodeKey.MAX_KEY));
	}
	
	@Test
	public void testCompareMinMin() {
        assertEquals(0, NodeKey.MIN_KEY.compareTo(NodeKey.MIN_KEY));
	}
	
	@Test
	public void testCompareWrap() {
		NodeKey less = new NodeKey(Hasher.generateHash((byte) 127));
		NodeKey more = new NodeKey(Hasher.generateHash((byte) -128));
        assertEquals(1, more.compareTo(less));
	}

}
