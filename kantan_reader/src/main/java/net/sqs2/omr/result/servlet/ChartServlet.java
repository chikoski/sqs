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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.contents.StatisticsContentsFactory;
import net.sqs2.omr.result.contents.ViewMode;
import net.sqs2.omr.result.contents.chart.BarChart;
import net.sqs2.omr.result.contents.chart.Legend;
import net.sqs2.omr.result.contents.chart.LegendHandler;
import net.sqs2.omr.result.contents.chart.PieChart;
import net.sqs2.omr.result.contents.chart.StackedBarChart;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;


public class ChartServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;
	
	public ChartServlet() throws ServletException{
		super();
	}
	
	public static String getContextString(){
		return "c";
	}
	
	static class LegendFactory{
		static Legend create(FormMaster master, int questionIndex){
			return new Legend(questionIndex, master.getFormAreaList(questionIndex));
		}
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		if(! HttpUtil.isLocalNetworkAccess(req)){
			res.sendError(403, "Forbidden");
			return;
		}

		SessionSource sessionSource = SessionSources.get(Long.parseLong(req.getParameter("sid")));
		int masterIndex = Integer.parseInt(req.getParameter("m"));

		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex);
		
		ChartServletParam param = ChartServletParamBuilder.build(new ChartServletParam(), req); 
		StatisticsContentsFactory statisticsContentsFactory = new StatisticsContentsFactory(sessionSource, param.getViewMode()); 
		statisticsContentsFactory.create(
				master,
				param.getSelectedTableIndexSet(),
				param.getSelectedRowIndexSet(),
				param.getSelectedQuestionIndexSet()		
		);

		LegendHandler legendHandler = createLegendHandler(master, param);
		Legend[] legendArray = legendHandler.getLegendArray();
		
		res.setContentType("image/png");

		if(ViewMode.isSimpleChartViewMode(param.getViewMode()) && legendArray.length == 1){
			Legend legend = legendArray[0];
			if(legend.getDefaultFormArea().isMarkArea()){
				if("pie".equals(param.type) || (param.type == null && legend.getDefaultFormArea().isSelect1())){
					PieChart.write(res.getOutputStream(), param.width, param.height, legend.getQuestionIndex(),
							legend.getFormAreaList(), statisticsContentsFactory);
				}else if("bar".equals(param.type) || (param.type == null && legend.getDefaultFormArea().isSelect())){
					BarChart.write(res.getOutputStream(), param.width, param.height, legend, statisticsContentsFactory);
				}
			}
		}else if(ViewMode.isGroupSimpleChartViewMode(param.getViewMode()) && legendArray.length == 1){
				Legend legend = legendArray[0];
				if(legend.getDefaultFormArea().isMarkArea()){
					if("pie".equals(param.type) || (param.type == null && legend.getDefaultFormArea().isSelect1())){
						PieChart.write(res.getOutputStream(), param.width, param.height, legend.getQuestionIndex(),
								legend.getFormAreaList(), statisticsContentsFactory);
					}else if("bar".equals(param.type) || (param.type == null && legend.getDefaultFormArea().isSelect())){
						BarChart.write(res.getOutputStream(), param.width, param.height, legend, statisticsContentsFactory);
					}
				}
		}else if(ViewMode.isCrossChartViewMode(param.getViewMode()) && legendArray.length == 2){
			StackedBarChart.write(res.getOutputStream(), param.width, param.height, legendHandler, statisticsContentsFactory);
		}else if(ViewMode.isGroupCrossChartViewMode(param.getViewMode()) && legendArray.length == 2){
			StackedBarChart.write(res.getOutputStream(), param.width, param.height, legendHandler, statisticsContentsFactory);
		}
		res.getOutputStream().flush();
	}

	private LegendHandler createLegendHandler(FormMaster master, ChartServletParam param) {
		Legend[] legendArray = null;
		
		int qindex = param.questionIndex;
		if(qindex != -1){
			legendArray = new Legend[1];
			legendArray[0] = LegendFactory.create(master, qindex);
			return new LegendHandler(legendArray, null);
		}else{
			int i = 0;
			legendArray = new Legend[2];
			for(int questionIndex: param.getSelectedQuestionIndexSet()){
				legendArray[i++] = LegendFactory.create(master, questionIndex);
			}
			String axis = param.getCrossTableAxis();
			if("1,0".equals(axis)){
				Legend tmp = legendArray[0];
				legendArray[0] = legendArray[1];
				legendArray[1] = tmp;
			}
			return new LegendHandler(legendArray, axis);
		}
	}

}
