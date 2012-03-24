package net.sqs2.omr.result.servlet;

import javax.servlet.http.HttpServletRequest;

import net.sqs2.omr.result.contents.ResultContentSelectorParamBuilder;

public class ResultBrowserServletParamBuilder extends ResultContentSelectorParamBuilder{
	

	public static ResultBrowserServletParam build(ResultBrowserServletParam param, HttpServletRequest req){
			
		param = (ResultBrowserServletParam)ResultContentSelectorParamBuilder.build(param, req);
		
		param.setUpdater(req.getParameter("u"));
		param.setViewMode(parseInt(req.getParameter("v")));
		param.setMSIE(HttpUtil.isMSIE(req));
		param.setSid(parseLong(req.getParameter("sid")));
		param.setCrossTableAxis(req.getParameter("axis"));

		param.setAnswerItemIndex(getIntegerParameter(req, "n"));
		if(param.getAnswerItemIndex() == -1){
			param.setAnswerItemIndex(0);
		}
		param.setNumMaxAnswerItems(getIntegerParameter(req, "N"));
		if(param.getNumMaxAnswerItems() == -1){
			param.setNumMaxAnswerItems(NUM_MAX_ANSWER_ITEMS);
		}
		return param;
	} 
}
