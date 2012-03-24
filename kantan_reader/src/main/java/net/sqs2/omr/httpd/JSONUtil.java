/*

 JSONUtil.java

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

 Created on 2008/01/13

 */

package net.sqs2.omr.httpd;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import net.sqs2.util.StringUtil;

public class JSONUtil{

	public static void printJSON(PrintWriter w, Object value) {
		if(value instanceof List){
			w.print("[");
			boolean comma = false;
			for(Object item: (List<Object>)value){
				if(comma){
					w.println(",");
				}
				JSONUtil.printJSON(w, item);
				comma = true;
			}
			w.println("]");

		}else if(value instanceof Map){
			w.print("{");
			boolean comma = false;
			for(Map.Entry<Object,Object> entry: ((Map<Object, Object>)value).entrySet()){
				if(comma){
					w.print(",");
				}
				w.print("'");
				w.print(entry.getKey());
				w.print("':");

				JSONUtil.printJSON(w, entry.getValue());
				comma = true;
			}
			w.println("}");
		}else if(value == null){
			w.print("''");
		}else if(value instanceof String){
			w.print("'");
			w.print(StringUtil.escapeTSV((String)value).replace("'", "\\'"));
			w.print("'");
		}else if(value instanceof Number){
			w.print(value);
		}
	}

	public static StringBuilder printJSON(StringBuilder sb, Object value) {
		if(value instanceof List){
			sb.append("[");
			boolean comma = false;
			for(Object item: (List<Object>)value){
				if(comma){
					sb.append(",");
				}
				JSONUtil.printJSON(sb, item);
				comma = true;
			}
			sb.append("]");

		}else if(value instanceof Map){
			sb.append("{");
			boolean comma = false;
			for(Map.Entry<Object,Object> entry: ((Map<Object, Object>)value).entrySet()){
				if(comma){
					sb.append(",");
				}
				sb.append("'");
				sb.append(entry.getKey().toString());
				sb.append("':");

				JSONUtil.printJSON(sb, entry.getValue());
				comma = true;
			}
			sb.append("}");
		}else if(value == null){
			sb.append("''");
		}else if(value instanceof String){
			sb.append("'");
			sb.append(StringUtil.escapeTSV((String)value).replace("'", "\\'"));
			sb.append("'");
		}else if(value instanceof Number){
			sb.append(value);
		}
		
		return sb;
	}
}
