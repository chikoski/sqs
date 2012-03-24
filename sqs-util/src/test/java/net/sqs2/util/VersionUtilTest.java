package net.sqs2.util;

import static org.junit.Assert.*;


import org.junit.Test;

public class VersionUtilTest {

	@Test
	public void test(){
		VersionTag version11 = new VersionTag("1.1");
		VersionTag version123 = new VersionTag("1.2.3");
		VersionTag version132beta = new VersionTag("1.3.2-beta");
		VersionTag version132 = new VersionTag("1.3.2");
		VersionTag version133 = new VersionTag("1.3.3");
		VersionTag version110 = new VersionTag("1.10");
		VersionTag version1122 = new VersionTag("1.12.2");
				
		assertTrue(0 == version123.compareTo(version123));

		assertTrue(version123.isNewerThan(version11));
		assertTrue(version11.isOlderThan(version123));
		
		assertTrue(version133.isNewerThan(version123));
		assertTrue(version123.isOlderThan(version133));
		
		assertTrue(version132.isNewerThan(version132beta));
		assertTrue(version132beta.isOlderThan(version132));

		assertTrue(version110.isNewerThan(version132));
		assertTrue(version132.isOlderThan(version110));

		assertTrue(version1122.isNewerThan(version110));
		assertTrue(version110.isOlderThan(version1122));

		assertTrue(version1122.isNewerThan(version123));
		assertTrue(version123.isOlderThan(version1122));
		
		assertTrue(version132.isNewerThan(version132beta));
		assertTrue(version132beta.isOlderThan(version132));
		
		/*
		List<VersionTag> versionList = new ArrayList<VersionTag>();
		for(VersionTag version : new VersionTag[]{version11, version123, version132, version132beta, version133, version110, version1122}){
			versionList.add(version);
		}
		
		Collections.shuffle(versionList);
		Collections.sort(versionList);

		assertEquals(version11, versionList.get(0));
		assertEquals(version123, versionList.get(1));
		assertEquals(version132, versionList.get(2));
		assertEquals(version132beta, versionList.get(3));
		assertEquals(version133, versionList.get(4));
		assertEquals(version110, versionList.get(5));
		assertEquals(version1122, versionList.get(6));
		*/
		
	}
}
