/*

 ImageContentServlet.java

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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.path.SingleImageSingleQuestionPath;
import net.sqs2.omr.result.servlet.writer.ContentsWriterUtil;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

public class FormAreaImageServlet extends HttpServlet {

	private static final long serialVersionUID = 0L;

	public static String getContextString() {
		return "i";
	}

	public FormAreaImageServlet() throws ServletException {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if (! new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}

		/*
		long sessionID = Long.parseLong(req.getParameter("sid"));
		int masterIndex = Integer.parseInt(req.getParameter("m"));
		int tableIndex = Integer.parseInt(req.getParameter("t"));
		int rowIndex = Integer.parseInt(req.getParameter("r"));
		int qIndex = Integer.parseInt(req.getParameter("q"));
		*/
		SingleImageSingleQuestionPath path = new SingleImageSingleQuestionPath(req.getPathInfo()); 
		long sessionID = path.getSessionID();
		int masterIndex = (int)path.getSingleRowPath().getMasterIndex();
		int flattenSourceDirectoryIndex = (int)path.getSingleRowPath().getFlattenSourceDirectoryIndex();
		int rowIndex = (int)path.getSingleRowPath().getRowIndex();
		int qIndex = (int)path.getQuestionIndex();
		
		SessionSource sessionSource = SessionSources.get(sessionID);

		FormMaster master = (FormMaster) sessionSource.getContentIndexer().getFormMasterList()
				.get(masterIndex);
		SourceDirectory sourceDirectory = sessionSource.getContentIndexer()
				.getFlattenSourceDirectoryList(master).get(flattenSourceDirectoryIndex);
		PageTaskAccessor pageTaskAccessor = sessionSource.getContentAccessor().getPageTaskAccessor();

		List<PageAreaResult> pageAreaResultList = ContentsWriterUtil.createPageAreaResultListParQuestion(
				master, sourceDirectory, pageTaskAccessor, rowIndex, qIndex);
		List<FormArea> formAreaList = master.getFormAreaList(qIndex);
		FormArea primaryFormArea = formAreaList.get(0);
		int itemIndex;
		if (primaryFormArea.isMarkArea()) {
			itemIndex = Integer.parseInt(req.getParameter("i"));
		} else if (primaryFormArea.isTextArea()) {
			itemIndex = 0;
		} else {
			throw new RuntimeException("invalid formArea type");
		}
		PageAreaResult result = pageAreaResultList.get(itemIndex);
		byte[] bytes = result.getImageByteArray();
		String type = result.getImageType();

		res.setContentType("image/" + type);
		OutputStream out = new BufferedOutputStream(res.getOutputStream());
		out.write(bytes);
		out.flush();

	}

}
