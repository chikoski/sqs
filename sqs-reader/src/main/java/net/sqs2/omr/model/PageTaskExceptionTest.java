package net.sqs2.omr.model;

import junit.framework.TestCase;
import net.sqs2.util.FileResourceID;

public class PageTaskExceptionTest extends TestCase {
	public void testCompareTo() {
		FileResourceID fileResourceID1 = new FileResourceID("/tmp", 0L);
		FileResourceID fileResourceID2 = new FileResourceID("/tmp", 0L);

		assertEquals((fileResourceID1.compareTo(fileResourceID2) == 0), true);

		PageID pageID1 = new PageID(fileResourceID1, 0, 1);
		PageID pageID2 = new PageID(fileResourceID2, 0, 1);

		assertEquals((pageID1.compareTo(pageID2) == 0), true);

		PageTaskException taskException1 = new PageTaskException(new PageTaskExceptionModel(pageID1, null));
		PageTaskException taskException2 = new PageTaskException(new PageTaskExceptionModel(pageID2, null));

		assertEquals((taskException1.compareTo(taskException2) == 0), true);
	}
}
