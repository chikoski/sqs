package net.sqs2.lang;

public class JVMUtil {

	/**
	 * JVM constant for any Windows 9x JVM
	 */
	public final static int WINDOWS_9x = 1;

	/**
	 * JVM constant for any Windows NT JVM
	 */
	public final static int WINDOWS_NT = 2;

	/**
	 * JVM constant for any other platform
	 */
	public final static int OTHER = -1;

	private static int jvmTypeCode;

	static {
		String osName = System.getProperty("os.name");

		if (osName.startsWith("Mac OS")) {
			jvmTypeCode = OTHER;
		} else if (osName.startsWith("Windows")) {
			if (osName.indexOf("9") != -1) {
				jvmTypeCode = WINDOWS_9x;
			} else {
				jvmTypeCode = WINDOWS_NT;
			}
		} else {
			jvmTypeCode = OTHER;
		}
	}

	public static int getJVMTypeCode() {
		return jvmTypeCode;
	}
}
