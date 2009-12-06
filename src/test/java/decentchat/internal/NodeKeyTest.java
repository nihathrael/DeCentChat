package decentchat.internal;

import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;

public class NodeKeyTest {
	
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
	public void testIncNormalization() {
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
	
	@Test
	public void testIsWithinSame() {
		assertTrue(NodeKey.MIN_KEY.isWithin(NodeKey.MIN_KEY, NodeKey.MIN_KEY));
	}
	
	@Test
	public void testIsWithinZeroBasedRange() {
		assertTrue(NodeKey.MIN_KEY.isWithin(NodeKey.MIN_KEY, NodeKey.MIN_KEY.inc(20)));
	}
	
	@Test
	public void testIsWithinSmallRange() {
		assertTrue(NodeKey.MIN_KEY.inc(9).isWithin(NodeKey.MIN_KEY.inc(8), NodeKey.MIN_KEY.inc(10)));
	}
	
	@Test
	public void testIsWithinOutOfRange() {
		assertFalse(NodeKey.MAX_KEY.isWithin(NodeKey.MIN_KEY, NodeKey.MIN_KEY.inc(20)));
	}
	
	@Test
	public void testIsWithinTwoFileds() {
		assertTrue(NodeKey.MIN_KEY.inc(38).isWithin(NodeKey.MIN_KEY.inc(1), NodeKey.MIN_KEY.inc(257)));
	}
	
	@Test
	public void testIsWithinWrapAround() {
		assertTrue(NodeKey.MAX_KEY.isWithin(NodeKey.MIN_KEY.inc(20), NodeKey.MIN_KEY));
	}

}
