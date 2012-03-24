package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;
import net.sqs2.omr.util.URLSafeRLEBase64.Selection;
import net.sqs2.omr.util.URLSafeRLEBase64.SingleSelection;

public class ImagePathFactory {
	public static ImagePath create(String pathInfo){
		String[] pathInfoArray = null;
		int paramDepth = 0;
		pathInfoArray = pathInfo.split("/");
		paramDepth = pathInfoArray.length;
		if(0 < paramDepth){
			long sessionID = URLSafeRLEBase64.decodeToLong(pathInfoArray[0]);
			if(1 < paramDepth){
				long masterIndex = URLSafeRLEBase64.decodeToLong(pathInfoArray[1]);
				if(2 < paramDepth){
					Selection spreadSheetEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[2]);
					if(3 < paramDepth){
						Selection rowEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[3]);
						if(4 < paramDepth){
							Selection imageEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[4]);
							if(5 < paramDepth){
								Selection questionEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[5]);
								if(spreadSheetEntry instanceof SingleSelection &&
										rowEntry instanceof SingleSelection){
									if(imageEntry instanceof MultiSelection){
										if(questionEntry instanceof MultiSelection){
											return new MultiImageMultiQuestionPath(sessionID, 
													masterIndex, 
													((SingleSelection)spreadSheetEntry).getValue(),
													((SingleSelection)rowEntry).getValue(), 
													((MultiSelection)imageEntry), 
													((MultiSelection)questionEntry));
										}else if(questionEntry instanceof SingleSelection){
											return new MultiImageSingleQuestionPath(sessionID, 
													masterIndex, 
													((SingleSelection)spreadSheetEntry).getValue(),
													((SingleSelection)rowEntry).getValue(), 
													((MultiSelection)imageEntry), 
													((SingleSelection)questionEntry).getValue());
										}else{
											throw new IllegalArgumentException(pathInfo);
										}
									}else if(imageEntry instanceof SingleSelection){
										if(questionEntry instanceof MultiSelection){
											return new SingleImageMultiQuestionPath(sessionID, 
													masterIndex, 
													((SingleSelection)spreadSheetEntry).getValue(),
													((SingleSelection)rowEntry).getValue(), 
													((SingleSelection)imageEntry).getValue(), 
													((MultiSelection)questionEntry));
										}else if(questionEntry instanceof SingleSelection){
											return new SingleImageSingleQuestionPath(sessionID, 
													masterIndex, 
													((SingleSelection)spreadSheetEntry).getValue(),
													((SingleSelection)rowEntry).getValue(), 
													((SingleSelection)imageEntry).getValue(), 
													((SingleSelection)questionEntry).getValue());
										}
									}									
								}									
							}
							throw new IllegalArgumentException(pathInfo);
						}
					}
				}
			}
		}
		throw new IllegalArgumentException(pathInfo);
	}
}
