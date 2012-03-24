/*

App.java

Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Created on 2007/09/05

 */

package net.sqs2.omr.model;

import java.io.File;

import net.sqs2.util.Resource;

public class AppConstants {
	// public static final String SKIN_ID = "smp";
	public static final String SKIN_ID = "sqs";

	private static String _(String key) {
		return Resource._("app-" + SKIN_ID, key);
	}

	
	public static final String BUILD_NAME = _("build.name");
	public static final String COPYRIGHT_NOTICE = _("copyright.notice");
	public static final String GROUP_ID = _("group.id");

	public static final String APP_NAME = _("app.name");
	public static final String APP_ID = _("app.id");
	public static final String BUILD_ID = _("build.id");

	public static final String MAIN_ICON = _("main.icon");
	public static final String SUB_ICON = _("sub.icon");
	
	public static final String RESULT_DIRNAME = _("result.dirname");
	
	public static final String RELEASE_ID = APP_ID + "_" + BUILD_ID;

	public static final String SOURCE_CONFIG_FILENAME = "config.xml";
	public static final String SOURCE_CONFIG_FILENAME_SUFFIX = ".xml";
	
	public static final File USER_CUSTOMIZED_CONFIG_DIR = new File(System.getProperty("user.home")+File.separatorChar+ '.' + AppConstants.GROUP_ID + File.separatorChar + AppConstants.APP_ID);

	public static final File USER_CUSTOMIZED_DEFAULT_CONFIG_FILE = new File(USER_CUSTOMIZED_CONFIG_DIR, SOURCE_CONFIG_FILENAME);

	public static final String MULTICAST_ADDRESS = _("MarkReaderConstants.MULTICAST_ADDRESS"); // FIXME: configurable address //$NON-NLS-1$
	public static final String TASK_EXECUTION_SOUND_FILENAME = _("MarkReaderConstants.TASK_EXECUTION_SOUND_FILENAME"); //$NON-NLS-1$
	public static final String SOURCE_DIRECTORY_ROOT_KEY_IN_PREFERENCES = _("MarkReaderConstants.SOURCE_DIRECTORY_ROOT_KEY_IN_PREFERENCES"); //$NON-NLS-1$
	public static final String SESSION_START_FANFARE_SOUND_FILENAME = _("MarkReaderConstants.SESSION_START_FANFARE_SOUND_FILENAME"); //$NON-NLS-1$
	public static final String FORMAREA_IMAGE_FORMAT = _("MarkReaderConstants.FORMAREA_IIMAGE_FORMAT"); //$NON-NLS-1$

}
