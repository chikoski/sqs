/*

 MarkReaderSessionConfiguration.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package net.sqs2.omr.model;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class MarkReaderConfiguration{
	
	static Map<String,Boolean> DEFAULT_VALUES = new HashMap<String,Boolean>();
	transient Preferences prefs = null;

	public static final String KEY_SOUND = "sound";  
	public static final String KEY_PARALLEL = "parallel";  
	public static final String KEY_SEARCHANCESTOR = "searchancestor";
	public static final String KEY_TEXTAREA = "textarea";  
	public static final String KEY_CHART = "chart";
	public static final String KEY_CHARTIMAGE = "chartimage";
	public static final String KEY_SPREADSHEET = "spreadsheet";
	public static final String KEY_RESULTBROWSER = "resultbrowser";
	
	static{
		DEFAULT_VALUES.put(KEY_SOUND, true);
		DEFAULT_VALUES.put(KEY_PARALLEL, true);
		DEFAULT_VALUES.put(KEY_SEARCHANCESTOR, false);
		DEFAULT_VALUES.put(KEY_TEXTAREA, true);
		DEFAULT_VALUES.put(KEY_CHART, true);
		DEFAULT_VALUES.put(KEY_CHARTIMAGE, true);
		DEFAULT_VALUES.put(KEY_SPREADSHEET, true);
		DEFAULT_VALUES.put(KEY_RESULTBROWSER, true);
		
		for(Map.Entry<String,Boolean> entry: DEFAULT_VALUES.entrySet()){
			String key = entry.getKey();
			boolean value = entry.getValue();
			setEnabled(key, isEnabled(key, value));
		}
	}
	
	public static Preferences getSingleton() {
		return Preferences.userNodeForPackage(MarkReaderConfiguration.class);
	}
	
	private static boolean isEnabled(String key, boolean defaultValue){
		return getSingleton().getBoolean(key, defaultValue);
	}
	
	public static boolean isEnabled(String key){
		return isEnabled(key, DEFAULT_VALUES.get(key));
	}
	
	public static void setEnabled(String key, boolean value){
		getSingleton().putBoolean(key, value);
	}
}