/**
 * 
 */
package net.sqs2.omr.result.contents;

import net.sqs2.omr.result.servlet.ResultBrowserServletParam;
import net.sqs2.omr.source.SessionSource;

abstract class AbstractJSONFactory{
	SessionSource sessionSource;
	AbstractJSONFactory(SessionSource sessionSource){
		this.sessionSource = sessionSource;
	}
	public abstract String create(ResultBrowserServletParam param);
}