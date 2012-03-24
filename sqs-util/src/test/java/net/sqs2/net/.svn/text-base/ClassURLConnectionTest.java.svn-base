package net.sqs2.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassURLConnectionTest {
	@Test
	public void test()throws Exception{
		
		URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		InputStream inputStream1 = new URL("class://net.sqs2.net.ClassURLConnectionTest/test.txt").openConnection().getInputStream();
		//InputStream inputStream1 = new FileInputStream(new File("src/test/resources/test.txt"));
		InputStream inputStream2 = new FileInputStream(new File("src/test/resources/test.txt"));
		byte[] tmp = new byte[4096];
		int length = 0;
		ByteArrayOutputStream data1 = new ByteArrayOutputStream(1024);
		while(0 < (length = inputStream1.read(tmp, 0, 4096))){
			data1.write(tmp, 0, length);
		}
		byte[] receivedBytes1 = data1.toByteArray();

		ByteArrayOutputStream data2 = new ByteArrayOutputStream(1024);
		while(0 < (length = inputStream2.read(tmp, 0, 4096))){
			data2.write(tmp, 0, length);
		}
		byte[] receivedBytes2 = data2.toByteArray();

		assertEquals(receivedBytes1.length, receivedBytes2.length);
		
		for(int i=0; i< receivedBytes1.length; i++){
			assertEquals(receivedBytes1[i], receivedBytes2[i]);
		}
	}
}
