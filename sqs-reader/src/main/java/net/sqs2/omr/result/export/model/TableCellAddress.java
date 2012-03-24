/**
 * 
 */
package net.sqs2.omr.result.export.model;

import java.io.Serializable;

class TableCellAddress implements Comparable<TableCellAddress>,Serializable{

	private static final long serialVersionUID = 1L;
	private int tableIndex, rowIndex, questionIndex;
	
	TableCellAddress(int tableIndex, int rowIndex, int questionIndex){
		this.tableIndex = tableIndex;
		this.rowIndex = rowIndex;
		this.questionIndex = questionIndex;
	}
	
	@Override
	public int compareTo(TableCellAddress c) {
		int d0= c.tableIndex - this.tableIndex;
		int d1= c.rowIndex - this.rowIndex;
		int d2 = c.questionIndex - this.questionIndex;
		if(d0 == 0){
			if(d1 == 0){
				if(d2 == 0){
					return 0;
				}else{
					return d2;
				}
			}else{
				return d1;
			}
		}else{
			return d0;
		}
	}
	
	public boolean equalsTo(Object o){
		try{
			TableCellAddress c = (TableCellAddress)o;
			boolean d0 = (c.tableIndex - this.tableIndex) == 0;
			boolean d1 = (c.rowIndex - this.rowIndex) == 0;
			boolean d2 = (c.questionIndex - this.questionIndex) == 0;
			return d0 && d1 && d2;
		}catch(Exception e){
			return false;
		}
	}
	
	public String toString(){
		return "CellAddress("+tableIndex+","+rowIndex+","+questionIndex+")"; 
	}
	
	public int hashCode(){
		return (this.tableIndex * 65536)+ this.rowIndex * 1024 + this.questionIndex;
	}
}