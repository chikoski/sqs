package net.sqs2.omr.task;

import junit.framework.TestCase;
import net.sqs2.omr.source.PageID;
import net.sqs2.util.FileResourceID;

public class TaskErrorTest extends TestCase {
	public void testCompareTo(){
		FileResourceID fileResourceID1 = new FileResourceID("/tmp", 0L); 
		FileResourceID fileResourceID2 = new FileResourceID("/tmp", 0L);
		
		assertEquals((fileResourceID1.compareTo(fileResourceID2) == 0), true);
		
		PageID pageID1 = new PageID(fileResourceID1, 0, 1);
		PageID pageID2 = new PageID(fileResourceID2, 0, 1);
		
		assertEquals((pageID1.compareTo(pageID2) == 0), true);
		
		TaskError taskError1 = new TaskError(pageID1, "");
		TaskError taskError2 = new TaskError(pageID2, "");
		
		assertEquals((taskError1.compareTo(taskError2) == 0), true);
	}
}
