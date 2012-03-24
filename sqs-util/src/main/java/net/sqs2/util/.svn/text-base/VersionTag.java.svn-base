package net.sqs2.util;

import java.util.List;

public class VersionTag implements Comparable<VersionTag>{
	List<String> versionNumberList;
	public VersionTag(String versionString){
		versionNumberList = StringUtil.split(versionString, '.');
	}
	
	public boolean isNewerThan(VersionTag targetVersionTag){
		return 0 < compareTo(targetVersionTag);
	}
	
	public boolean isOlderThan(VersionTag targetVersionTag){
		return compareTo(targetVersionTag) < 0;
	}
	
	public boolean isSameOrNewerThan(VersionTag targetVersionTag){
		return 0 <= compareTo(targetVersionTag);
	}
	
	public boolean isSameOrOlderThan(VersionTag targetVersionTag){
		return compareTo(targetVersionTag) <= 0;
	}
	
	@Override
	public int compareTo(VersionTag targetVersionTag){
		for(int index = 0; index < versionNumberList.size(); index++){
			String thisVersionNumberComponent = versionNumberList.get(index);
			String targetVersionNumberComponent = null;
			if(targetVersionTag.versionNumberList.size() <= index){
				targetVersionNumberComponent = "0"; 
			}else{
				targetVersionNumberComponent = targetVersionTag.versionNumberList.get(index);
			}
			int comp = 0;
			try{
				comp = Integer.parseInt(thisVersionNumberComponent) - 
							Integer.parseInt(targetVersionNumberComponent);
			}catch(NumberFormatException e){
				comp = -1 * thisVersionNumberComponent.compareTo(targetVersionNumberComponent); // adhoc
			}
			if(comp != 0){
				return comp;
			}
		}
		return versionNumberList.size() - targetVersionTag.versionNumberList.size();
	}
	
	@Override
	public boolean equals(Object o){
		try{
			return this.versionNumberList.equals(((VersionTag)o).versionNumberList);
		}catch(ClassCastException e){
			return false;
		}
	}

	@Override
	public int hashCode(){
		return this.versionNumberList.hashCode();
	}
	
	@Override
	public String toString(){
		return StringUtil.join(this.versionNumberList, ".");
	}
}