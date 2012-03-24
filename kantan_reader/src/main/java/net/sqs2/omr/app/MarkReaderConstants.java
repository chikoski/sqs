/**
 *  MarkReaderConstants.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/08/29
 Author hiroya
 */

package net.sqs2.omr.app;

import java.io.File;
import java.util.Random;

import net.sqs2.omr.source.GenericSourceConfig;

public class MarkReaderConstants {
/*
	private static ResourceBundle omr = ResourceBundle.getBundle("omr");
	public static ResourceBundle getResourceBundle() {
		return omr;
	}
*/
	public static final File USER_CUSTOMIZE_CONSTANTS_DIR = new File(System.getProperty("user.home")+File.separatorChar+'.'+App.GROUP_ID+File.separatorChar+App.APP_ID);
	public static final File USER_CUSTOMIZE_DEFAULT_CONFIG_FILE = new File(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, GenericSourceConfig.SOURCE_CONFIG_FILENAME);
	public static final Random RANDOM = new Random();
	public static final String SESSION_SERVICE_NAME = "SessionService";
	
	public static final int CLIENT_TIMEOUT_IN_SEC = 30;//Integer.parseInt(MarkReaderConstants.getResourceBundle().getString("service.client.timeout"));

    public static final int PAGETASK_POLLING_TIMEOUT_IN_SEC = 29;// sec
    public static final int SESSION_SOURCE_DIRECTORY_SCAN_DELAY_IN_SEC = 60 * 10;// sec

    //public static final int SESSION_SOURCE_ADVERTISE_DELAY_IN_SEC = 3;// sec
    //public static final int ADVERTISE_SERVICE_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;

    public static final int DISCOVERY_SERVICE_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;
    public static final int PAGETASK_EXECUTORS_MAX_EXECUTORS = 5;
    public static final int PAGETASK_EXECUORS_EXEC_DELAY_IN_SEC = 1; // sec

    public static final int SESSION_SOURCE_ADVERTISE_DATAGRAM_PACKET_BUFFER_LENGTH = 512;// len

    public static final int ADVERTISE_SERVICE_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;
    public static final int SESSION_SOURCE_ADVERTISE_DELAY_IN_SEC = 3;// sec
	public static final int SESSION_SOURCE_NEWFILE_IGNORE_THRESHOLD_IN_SEC = 3;

    
    public static final String MULTICAST_ADDRESS = "239.0.0.100"; // FIXME: configurable address
    public static final int MULTICAST_PORT = 1091;// FIXME: configurable port

	public static final String TASK_EXECUTION_SOUND_FILENAME = "push55.wav";
	
	public static final String SOURCE_DIRECTORY_ROOT_KEY = "sourceDirectoryRoot";
	//public static final String DEFAULT_SOURCEDIRECTORY_PATH = System.getProperty("user.dir");
	public static final String DEFAULT_SOURCEDIRECTORY_PATH = null;

	public static final long SESSION_FOLDER_TYPE_INVALID = -1;
	public static final long SESSION_FOLDER_IO_ERROR = -2;
	public static final long SESSION_FOLDER_NO_ERROR = -4;
	public static final long SESSION_FOLDER_ALREADY_STARTED_BASE = 0;
	
	public static final String SESSION_START_FANFARE_SOUND_FILENAME = "whistle00.wav";
	public static final String FORMAREA_IMAGE_FORMAT = "png";
	public static final String SESSION_SERVICE_PATH =  App.APP_ID + '/' + App.BUILD_ID + '/' + MarkReaderConstants.SESSION_SERVICE_NAME;
	
}

