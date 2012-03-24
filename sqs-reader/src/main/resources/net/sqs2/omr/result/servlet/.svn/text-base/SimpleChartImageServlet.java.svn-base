/*

 ChartServlet.java

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
package net.sqs2.omr.result.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.builder.StatisticsResult;
import net.sqs2.omr.result.chart.BarChart;
import net.sqs2.omr.result.chart.Legend;
import net.sqs2.omr.result.chart.PieChart;
import net.sqs2.omr.result.context.ChartImageParam;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.result.path.MultiRowSingleQuestionPath;
import net.sqs2.omr.result.path.MultiRowSingleQuestionSelection;
import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

public class SimpleChartImageServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public SimpleChartImageServlet() throws ServletException {
		super();
	}

	public static String getContextString() {
		return "c";
	}

	static class LegendFactory {
		static Legend create(FormMaster master, int questionIndex) {
			return new Legend(questionIndex, master.getFormAreaList(questionIndex));
		}
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (! new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}

		res.setContentType("image/png");
		Map<String,String> params = req.getParameterMap();
		String userAgnetName = req.getHeader("User-Agent");
		MultiRowSingleQuestionPath multiRowSingleQuestionPath = new MultiRowSingleQuestionPath(req.getPathInfo());
		ResultBrowserContext resultBrowserContext = 
			new ResultBrowserContext(req.getPathInfo(), params, userAgnetName); 
		MultiRowSingleQuestionSelection selection = new MultiRowSingleQuestionSelection(multiRowSingleQuestionPath);
		Legend legend = LegendFactory.create(selection.getFormMaster(), selection.getPrimaryFormArea().getQuestionIndex());
		
		StatisticsResult statisticsResult = null;//FIXME: new StatisticsResult(selection); 
		writeSimpleChart(res, new ChartImageParam(params), statisticsResult, legend);
		/*
		if (ViewModeUtil.isSimpleChartViewMode(context.getViewMode()) && legendArray.length == 1) {
		} else if (ViewModeUtil.isGroupSimpleChartViewMode(context.getViewMode()) && legendArray.length == 1) {
			writeSimpleChart(res, param, statisticsContentsFactory, legendArray);
		} else if (ViewModeUtil.isCrossChartViewMode(context.getViewMode()) && legendArray.length == 2) {
			StackedBarChart.write(res.getOutputStream(), param.width, param.height, legendHandler,
					statisticsContentsFactory);
		} else if (ViewModeUtil.isGroupCrossChartViewMode(context.getViewMode()) && legendArray.length == 2) {
			StackedBarChart.write(res.getOutputStream(), param.width, param.height, legendHandler,
					statisticsContentsFactory);
		}
		*/
		res.getOutputStream().flush();
	}
	
	protected void writeSimpleChart(HttpServletResponse res,
			ChartImageParam param,
			StatisticsResult statisticsResult,
			Legend legend) throws IOException {
		if (legend.getPrimaryFormArea().isMarkArea()) {
			if ("pie".equals(param.getType())
					|| (param.getType() == null && legend.getPrimaryFormArea().isSelectSingle())) {
				PieChart.write(res.getOutputStream(), param.getWidth(), param.getHeight(), legend
						.getQuestionIndex(), legend.getFormAreaList(), statisticsResult);
			} else if ("bar".equals(param.getType())
					|| (param.getType() == null && legend.getPrimaryFormArea().isSelectMultiple())) {
				BarChart.write(res.getOutputStream(), param.getWidth(), param.getHeight(), legend,
						statisticsResult);
			}
		}
	}
	

}
