package net.sqs2.omr.result.servlet;

import javax.servlet.http.HttpServletRequest;

public class ChartServletParamBuilder extends ResultBrowserServletParamBuilder {
	public static ChartServletParam build(ChartServletParam chartServletParam, HttpServletRequest req){
		ChartServletParam param = (ChartServletParam)ResultBrowserServletParamBuilder.build(chartServletParam, req);
		param.setWidth(ResultBrowserServletParamBuilder.getIntegerParameter(req, "w"));
		param.setHeight(ResultBrowserServletParamBuilder.getIntegerParameter(req, "h"));
		param.setWidth((param.width == -1)? 400:param.getWidth());
		param.setHeight((param.height == -1)? 350:param.getHeight());
		param.setType(req.getParameter("type"));
		param.setQuestionIndex(ResultBrowserServletParamBuilder.getIntegerParameter(req, "Q"));
		return param;
	}
}
