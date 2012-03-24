/**
 * 
 */
package net.sqs2.omr.result.path;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.util.URLSafeRLEBase64;

public class MultiQuestionPath{
	FormMaster formMaster;
	URLSafeRLEBase64.MultiSelection entry;
	public MultiQuestionPath(FormMaster formMaster, URLSafeRLEBase64.MultiSelection entry){
		this.formMaster = formMaster;
		this.entry = entry;
	}
	
	public FormMaster getFormMaster(){
		return formMaster;
	}
	
	public URLSafeRLEBase64.MultiSelection getQuestionSelection(){
		return this.entry;
	}
}