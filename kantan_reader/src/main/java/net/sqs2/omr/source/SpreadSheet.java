package net.sqs2.omr.source;

public class SpreadSheet {
	
	private SourceDirectory sourceDirectory;
	private int numRows = -1;
		
	public SpreadSheet(SourceDirectory sourceDirectory){
		this.sourceDirectory = sourceDirectory;
	}
	
	public SourceDirectory getSourceDirectory(){
		return this.sourceDirectory;
	}
	
	public int getNumRows(){
		if(this.numRows == -1){
			return this.numRows = this.sourceDirectory.getNumPageIDs() / this.sourceDirectory.getPageMaster().getNumPages();
		}else{
			return this.numRows;
		}
	}
	
	public boolean equals(Object o){
		if(!(o instanceof SpreadSheet)){
			return false;
		}
		return sourceDirectory.equals(((SpreadSheet)o).getSourceDirectory()); 
	}
}
