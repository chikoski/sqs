package net.sqs2.omr.util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;


public class URLSafeRLEBase64Test {
	
	@Test
	public void testEncodeBasic(){
		
		assertEquals(1, "".split("/").length);
		assertEquals(1, "a".split("/").length);
		assertEquals(2, "a/b".split("/").length);
		assertEquals(3, "/a/b".split("/").length);
		assertEquals(3, "/a/b/".split("/").length);
		assertEquals(3, "a//b".split("/").length);
		assertEquals(4, "//a/b".split("/").length);
		
		assertEquals(1, 1<<0);
		assertEquals(2, 1<<1);
		assertEquals(4, 1<<2);
		assertEquals(0xf0, 0x0f << 4);
	}
		
	@Test
	public void testEncodeIntValue(){
		assertEquals("g", URLSafeRLEBase64.encode(0));
		assertEquals("h", URLSafeRLEBase64.encode(1));
		assertEquals("i", URLSafeRLEBase64.encode(2));
		assertEquals("j", URLSafeRLEBase64.encode(3));
		assertEquals("k", URLSafeRLEBase64.encode(4));
		assertEquals("l", URLSafeRLEBase64.encode(5));
		assertEquals("m", URLSafeRLEBase64.encode(6));
		assertEquals("n", URLSafeRLEBase64.encode(7));
		assertEquals("o", URLSafeRLEBase64.encode(8));
		assertEquals("p", URLSafeRLEBase64.encode(9));
		assertEquals("q", URLSafeRLEBase64.encode(10));
		assertEquals("r", URLSafeRLEBase64.encode(11));
		assertEquals("s", URLSafeRLEBase64.encode(12));
		assertEquals("t", URLSafeRLEBase64.encode(13));
		assertEquals("u", URLSafeRLEBase64.encode(14));
		assertEquals("v", URLSafeRLEBase64.encode(15));
		assertEquals("w", URLSafeRLEBase64.encode(16));
		assertEquals("x", URLSafeRLEBase64.encode(17));
		assertEquals("y", URLSafeRLEBase64.encode(18));
		assertEquals("z", URLSafeRLEBase64.encode(19));
		assertEquals("0", URLSafeRLEBase64.encode(20));
		assertEquals("1", URLSafeRLEBase64.encode(21));
		assertEquals("2", URLSafeRLEBase64.encode(22));
		assertEquals("3", URLSafeRLEBase64.encode(23));
		assertEquals("4", URLSafeRLEBase64.encode(24));
		assertEquals("5", URLSafeRLEBase64.encode(25));
		assertEquals("6", URLSafeRLEBase64.encode(26));
		assertEquals("7", URLSafeRLEBase64.encode(27));
		assertEquals("8", URLSafeRLEBase64.encode(28));
		assertEquals("9", URLSafeRLEBase64.encode(29));
		assertEquals("_", URLSafeRLEBase64.encode(30));
		assertEquals("-", URLSafeRLEBase64.encode(31));
		assertEquals("hg", URLSafeRLEBase64.encode(32));
		assertEquals("ho", URLSafeRLEBase64.encode(40));
		assertEquals("--", URLSafeRLEBase64.encode(1023));
	}
		
	public void testEncodeLongValue(){
		assertEquals("g", URLSafeRLEBase64.encode(0L));
		assertEquals("h", URLSafeRLEBase64.encode(1L));
		assertEquals("i", URLSafeRLEBase64.encode(2L));
		assertEquals("j", URLSafeRLEBase64.encode(3L));
		assertEquals("k", URLSafeRLEBase64.encode(4L));
		assertEquals("l", URLSafeRLEBase64.encode(5L));
		assertEquals("m", URLSafeRLEBase64.encode(6L));
		assertEquals("n", URLSafeRLEBase64.encode(7L));
		assertEquals("o", URLSafeRLEBase64.encode(8L));
		assertEquals("p", URLSafeRLEBase64.encode(9L));
		assertEquals("q", URLSafeRLEBase64.encode(10L));
		assertEquals("r", URLSafeRLEBase64.encode(11L));
		assertEquals("s", URLSafeRLEBase64.encode(12L));
		assertEquals("t", URLSafeRLEBase64.encode(13L));
		assertEquals("u", URLSafeRLEBase64.encode(14L));
		assertEquals("v", URLSafeRLEBase64.encode(15L));
		assertEquals("w", URLSafeRLEBase64.encode(16L));
		assertEquals("x", URLSafeRLEBase64.encode(17L));
		assertEquals("y", URLSafeRLEBase64.encode(18L));
		assertEquals("z", URLSafeRLEBase64.encode(19L));
		assertEquals("0", URLSafeRLEBase64.encode(20L));
		assertEquals("1", URLSafeRLEBase64.encode(21L));
		assertEquals("2", URLSafeRLEBase64.encode(22L));
		assertEquals("3", URLSafeRLEBase64.encode(23L));
		assertEquals("4", URLSafeRLEBase64.encode(24L));
		assertEquals("5", URLSafeRLEBase64.encode(25L));
		assertEquals("6", URLSafeRLEBase64.encode(26L));
		assertEquals("7", URLSafeRLEBase64.encode(27L));
		assertEquals("8", URLSafeRLEBase64.encode(28L));
		assertEquals("9", URLSafeRLEBase64.encode(29L));
		assertEquals("_", URLSafeRLEBase64.encode(30L));
		assertEquals("-", URLSafeRLEBase64.encode(31L));
		assertEquals("hg", URLSafeRLEBase64.encode(32L));
		assertEquals("ho", URLSafeRLEBase64.encode(40L));
		assertEquals("--", URLSafeRLEBase64.encode(1023L));
		assertEquals("h--_", URLSafeRLEBase64.encode(65534L));
		assertEquals("h---", URLSafeRLEBase64.encode(65535L));
		assertEquals("iggg", URLSafeRLEBase64.encode(65536L));
		assertEquals("iggh", URLSafeRLEBase64.encode(65537L));
		assertEquals("zxgltg", URLSafeRLEBase64.encode(655365536L));
		assertEquals("9------", URLSafeRLEBase64.encode(2147483647L));
		assertEquals("nx1-wvx",URLSafeRLEBase64.encode(1272977539569L));
		assertEquals("vx1zrmr", URLSafeRLEBase64.encode(1272977140939L));
		assertEquals("zx1zrms", URLSafeRLEBase64.encode(1272977140940L));
	}
	
	@Test
	public void testEncodeSequencialByteArray(){
		assertEquals("Ai", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 0,0,0,0,0}));
		assertEquals("Aj~~~~", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 0,0,0,0,0, 0}));
		
		assertEquals("Ak", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0}));		
		assertEquals("Al~", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0}));
		
		assertEquals("Ciej", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1}));
		assertEquals("CiejA~~~~", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1, 0}));
		assertEquals("CiejC~~~", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1, 0,1}));
		assertEquals("CiejG~~", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1, 0,1,1}));
		assertEquals("CiejO~", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1}));
		assertEquals("Ciek", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0, 0,1,0,0,0, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1, 0,1,1,1,1}));
	}
	
	@Test
	public void testEncodeByteArray(){
		assertEquals("", URLSafeRLEBase64.encode(new byte[]{}));
		assertEquals("A~~~~", URLSafeRLEBase64.encode(new byte[]{0}));
		assertEquals("A~~~", URLSafeRLEBase64.encode(new byte[]{0,0}));
		assertEquals("A~~", URLSafeRLEBase64.encode(new byte[]{0,0,0}));
		assertEquals("A~", URLSafeRLEBase64.encode(new byte[]{0,0,0,0}));
		assertEquals("A", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0}));
		assertEquals("B~~~~", URLSafeRLEBase64.encode(new byte[]{1}));
		assertEquals("B~~~", URLSafeRLEBase64.encode(new byte[]{1,0}));
		assertEquals("B~~", URLSafeRLEBase64.encode(new byte[]{1,0,0}));
		assertEquals("B~", URLSafeRLEBase64.encode(new byte[]{1,0,0,0}));
		assertEquals("B", URLSafeRLEBase64.encode(new byte[]{1,0,0,0,0}));
		assertEquals("C~~~", URLSafeRLEBase64.encode(new byte[]{0,1}));
		assertEquals("C~~", URLSafeRLEBase64.encode(new byte[]{0,1,0}));
		assertEquals("C~", URLSafeRLEBase64.encode(new byte[]{0,1,0,0}));
		assertEquals("C", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,0}));
		assertEquals("D~~~", URLSafeRLEBase64.encode(new byte[]{1,1}));
		assertEquals("D", URLSafeRLEBase64.encode(new byte[]{1,1,0,0,0}));
		assertEquals("E~~", URLSafeRLEBase64.encode(new byte[]{0,0,1}));
		assertEquals("F~~", URLSafeRLEBase64.encode(new byte[]{1,0,1}));
		assertEquals("G~~", URLSafeRLEBase64.encode(new byte[]{0,1,1}));
		assertEquals("H~~", URLSafeRLEBase64.encode(new byte[]{1,1,1}));
		assertEquals("I~", URLSafeRLEBase64.encode(new byte[]{0,0,0,1}));
		assertEquals("J~", URLSafeRLEBase64.encode(new byte[]{1,0,0,1}));
		assertEquals("K~", URLSafeRLEBase64.encode(new byte[]{0,1,0,1}));
		assertEquals("L~", URLSafeRLEBase64.encode(new byte[]{1,1,0,1}));
		assertEquals("M~", URLSafeRLEBase64.encode(new byte[]{0,0,1,1}));
		assertEquals("N~", URLSafeRLEBase64.encode(new byte[]{1,0,1,1}));
		assertEquals("O~", URLSafeRLEBase64.encode(new byte[]{0,1,1,1}));
		assertEquals("P~", URLSafeRLEBase64.encode(new byte[]{1,1,1,1}));
		assertEquals("Q", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,1}));
		assertEquals("R", URLSafeRLEBase64.encode(new byte[]{1,0,0,0,1}));
		assertEquals("S", URLSafeRLEBase64.encode(new byte[]{0,1,0,0,1}));
		assertEquals("T", URLSafeRLEBase64.encode(new byte[]{1,1,0,0,1}));
		assertEquals("U", URLSafeRLEBase64.encode(new byte[]{0,0,1,0,1}));
		assertEquals("V", URLSafeRLEBase64.encode(new byte[]{1,0,1,0,1}));
		assertEquals("W", URLSafeRLEBase64.encode(new byte[]{0,1,1,0,1}));
		assertEquals("X", URLSafeRLEBase64.encode(new byte[]{1,1,1,0,1}));
		assertEquals("Y", URLSafeRLEBase64.encode(new byte[]{0,0,0,1,1}));
		assertEquals("Z", URLSafeRLEBase64.encode(new byte[]{1,0,0,1,1}));
		assertEquals("a", URLSafeRLEBase64.encode(new byte[]{0,1,0,1,1}));
		assertEquals("b", URLSafeRLEBase64.encode(new byte[]{1,1,0,1,1}));
		assertEquals("c", URLSafeRLEBase64.encode(new byte[]{0,0,1,1,1}));
		assertEquals("d", URLSafeRLEBase64.encode(new byte[]{1,0,1,1,1}));
		assertEquals("e", URLSafeRLEBase64.encode(new byte[]{0,1,1,1,1}));
		assertEquals("f", URLSafeRLEBase64.encode(new byte[]{1,1,1,1,1}));
		assertEquals("AB~~~~", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0,1}));
		assertEquals("AfA", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 1,1,1,1,1, 0,0,0,0,0}));
		assertEquals("AfAi~~~~", URLSafeRLEBase64.encode(new byte[]{0,0,0,0,0, 1,1,1,1,1, 0,0,0,0,0, 0}));
	}

	@Test
	public void testEncodeSequencialString(){
		assertEquals("Ak", URLSafeRLEBase64.encode("00000000000000000000"));//4
		assertEquals("Al", URLSafeRLEBase64.encode("0000000000000000000000000"));//5
		assertEquals("A5", URLSafeRLEBase64.encode("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"));//25
		assertEquals("A_", URLSafeRLEBase64.encode("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"));//30
		assertEquals("Ahg", URLSafeRLEBase64.encode("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000"));//32

		assertEquals("fk", URLSafeRLEBase64.encode("11111111111111111111"));
		assertEquals("Ciej", URLSafeRLEBase64.encode("0100001000011110111101111"));

		assertEquals("VAhg", URLSafeRLEBase64.encode("10101"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000"));//32
		assertEquals("VAhgB", URLSafeRLEBase64.encode("10101"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000"+"10000"));//32
		
		assertEquals("00000000000000000000",//4*5
				URLSafeRLEBase64.decodeToString("Ak"));

		assertEquals("000000000000000000000",//4*5+1
				URLSafeRLEBase64.decodeToString("AkA~~~~"));

		assertEquals("0000000000000000000000000",//5
				URLSafeRLEBase64.decodeToString("Al"));
		assertEquals("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000",//25
				URLSafeRLEBase64.decodeToString("A5"));
		assertEquals("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000",//30
				URLSafeRLEBase64.decodeToString("A_"));
		
		assertEquals("0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000",//40 
				URLSafeRLEBase64.decodeToString("Aho"));
		
		assertEquals("11111111111111111111", URLSafeRLEBase64.decodeToString("fk"));
		
		assertEquals("0100001000011110111101111", URLSafeRLEBase64.decodeToString("Ciej"));

		assertEquals("10101"+
				     "0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000",
				URLSafeRLEBase64.decodeToString("VA_"));

		assertEquals("10101"+
			     "0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000000000000000000"+"0000000000"+"10000", 
			     URLSafeRLEBase64.decodeToString("VAhgB"));

		assertEquals(0L, URLSafeRLEBase64.decodeToLong("g"));
		assertEquals(31L, URLSafeRLEBase64.decodeToLong("-"));
		assertEquals(1023L, URLSafeRLEBase64.decodeToLong("--"));
		assertEquals(32767L, URLSafeRLEBase64.decodeToLong("---"));
		assertEquals(1048575L, URLSafeRLEBase64.decodeToLong("----"));
		assertEquals(33554431L, URLSafeRLEBase64.decodeToLong("-----"));
		assertEquals(1073741823L, URLSafeRLEBase64.decodeToLong("------"));
		assertEquals(34359738367L, URLSafeRLEBase64.decodeToLong("-------"));
		assertEquals(1099511627775L, URLSafeRLEBase64.decodeToLong("--------"));
		assertEquals(8109670897L, URLSafeRLEBase64.decodeToLong("nx1-wvx"));
		assertEquals(16699206859L, URLSafeRLEBase64.decodeToLong("vx1zrmr"));
		assertEquals("nx1-wvx",URLSafeRLEBase64.encode(8109670897L));
		assertEquals("hlhx1-wvx",URLSafeRLEBase64.encode(1272977539569L));
		assertEquals("vx1zrmr", URLSafeRLEBase64.encode(16699206859L));
	}
	
	//@Test
	public void testEncodeRandomValue(){
		// Random r = new Random();
		for(long i = 1<<31; i< 1<<32; i++){
			long l = i;
			String encodedString = URLSafeRLEBase64.encode(l);
			long ret = URLSafeRLEBase64.decodeToLong(encodedString);
			System.err.println(encodedString);
			System.err.println(Long.toBinaryString(l)+"\t"+l);
			System.err.println(Long.toBinaryString(ret)+"\t"+ret);
			assertEquals(l, ret);
		}
	}
	
}
