package net.sqs2.omr.result.export;

public class MarkReadStatus {
	
	public class Select1Status{
		int numOneSelectedQuestions = 0;
		int numNoAnsweredQuestions = 0;
		int numMultiPleSelectedQuestions = 0;
		int numQuestions = 0;
		
		public int getNumOneSelectedQuestions() {
			return numOneSelectedQuestions;
		}
		public int getNumNoAnsweredQuestions() {
			return numNoAnsweredQuestions;
		}
		public int getNumMultipleAnsweredQuestions() {
			return numMultiPleSelectedQuestions;
		}
		public int getNumQuestions() {
			return numQuestions;
		}
		
		public void add(Select1Status target){
			numOneSelectedQuestions += target.getNumOneSelectedQuestions();
			numNoAnsweredQuestions += target.getNumNoAnsweredQuestions();
			numMultiPleSelectedQuestions += target.getNumMultipleAnsweredQuestions();
			numQuestions += target.getNumQuestions();
		}
	}

	public class SelectStatus{
		int numMultipleSelectedMarks = 0;
		int numQuestions = 0;
		public int getNumMultipleSelectedMarks() {
			return numMultipleSelectedMarks;
		}
		public int getNumQuestions() {
			return numQuestions;
		}
		
		public void add(SelectStatus target){
			numMultipleSelectedMarks += target.getNumMultipleSelectedMarks();
			numQuestions += target.getNumQuestions();
		}
	}
	
	int numPages = 0;
	int numErrorPages = 0;
	Select1Status select1Status;
	SelectStatus selectStatus;
	
	MarkReadStatus(){
		select1Status = new Select1Status(); 
		selectStatus = new SelectStatus(); 
	}
	
	public int getNumPages(){
		return numPages;
	}
	
	public void setNumPages(int numPages){
		this.numPages = numPages;
	}


	public void addNumErrorPage(){
		numErrorPages++;
	}

	public int getNumErrorPages(){
		return numErrorPages;
	}

	public int getNumQuestions(){
		return this.select1Status.getNumQuestions() + this.selectStatus.getNumQuestions();
	}
	
	public Select1Status getSelect1Status(){
		return this.select1Status;
	}
	
	public SelectStatus getSelectStatus(){
		return this.selectStatus;
	}
	
	public void add(MarkReadStatus target){
		select1Status.add(target.getSelect1Status());
		selectStatus.add(target.getSelectStatus());
	}
}
