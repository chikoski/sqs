package net.sqs2.omr.result.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.chart.Legend;
import net.sqs2.omr.result.chart.LegendHandler;
import net.sqs2.omr.result.context.ChartImageParam;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.result.path.MultiRowMultiQuestionPath;
import net.sqs2.omr.result.path.MultiRowMultiQuestionSelection;

public class CrossTabularChartImageServlet extends SimpleChartImageServlet {
	private static final long serialVersionUID = 1L;

	public CrossTabularChartImageServlet() throws ServletException {
		super();
	}

	protected LegendHandler createLegendHandler(ResultBrowserContext context, ChartImageParam param) {
		
		List<Legend> legendList = new ArrayList<Legend>(2);

		MultiRowMultiQuestionPath path = new MultiRowMultiQuestionPath(context.getPathInfo());
		MultiRowMultiQuestionSelection selection = new MultiRowMultiQuestionSelection(path);
		
		for (List<FormArea> formAreaList:selection.getMultiQuestionSelection().getFormAreaListList()) {
			legendList.add(LegendFactory.create(selection.getFormMaster(), formAreaList.get(0).getQuestionIndex()));
		}
		String axis = context.getResultBrowserParam().getCrossTableAxis();
		if(legendList.size()==2){
			if ("1,0".equals(axis)) {
				Legend tmp = legendList.get(0);
				legendList.set(0, legendList.get(1));
				legendList.set(1, tmp);
			}
		}
		return new LegendHandler(legendList, axis);
	}

}
