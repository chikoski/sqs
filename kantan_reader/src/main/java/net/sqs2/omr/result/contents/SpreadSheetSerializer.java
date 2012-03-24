package net.sqs2.omr.result.contents;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;

public class SpreadSheetSerializer{
	private void exportSpreadSheet(SessionSource sessionSource, SourceDirectory sourceDirectory, int masterIndex, int tableIndex)throws FileNotFoundException, IOException{
		File dir = new File(sourceDirectory.getRoot().getAbsolutePath(), sourceDirectory.getPath());
		String name = dir.getAbsolutePath().substring(dir.getAbsolutePath().lastIndexOf(File.separator)+1);
		Logger.getAnonymousLogger().info("Export: "+dir.getAbsolutePath()+File.separator+name);
		Set<Integer> selectedTableSet = new TreeSet<Integer>();
		selectedTableSet.add(tableIndex);
		exportXLS(sessionSource, masterIndex, dir, name, selectedTableSet);
		exportCSV(sessionSource, masterIndex, dir, name, selectedTableSet);
	}

	private void exportXLS(SessionSource sessionSource, int masterIndex,
			File dir, String name, Set<Integer> selectedTableSet) throws FileNotFoundException, IOException {
		Logger.getAnonymousLogger().info("export xls");
		OutputStream xlsOutputStream = new BufferedOutputStream(new FileOutputStream(new File(dir, name+".xls")));
		//XLSContentsFactory xlsContentsFactory = new XLSContentsFactory(xlsOutputStream, sessionSource);
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex); 
		//xlsContentsFactory.create(master, selectedTableSet, null, null);
		xlsOutputStream.close();
		Logger.getAnonymousLogger().info("export xls done.");
	}
	
	private void exportCSV(SessionSource sessionSource, int masterIndex,
			File dir, String name, Set<Integer> selectedTableSet) throws FileNotFoundException, IOException {
		Logger.getAnonymousLogger().info("export csv");
		PrintWriter csvWriter = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(new File(dir, name+"-tsv.txt"))), "MS932"));
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex); 
		//CSVContentsFactory csvContentsFactory = new CSVContentsFactory(csvWriter, sessionSource);
		//csvContentsFactory.create(master, selectedTableSet, null, null);
		csvWriter.close();
		Logger.getAnonymousLogger().info("export csv done.");
	}

	public void serialize(SessionSource sessionSource) {
		Logger.getAnonymousLogger().info("Start export spreadsheet.");
		int masterIndex = 0;

		for(PageMaster master: sessionSource.getSessionSourceContentIndexer().getPageMasterList()){
			int tableIndex = 0;
			for(SourceDirectory sourceDirectory: sessionSource.getSessionSourceContentIndexer().getSourceDirectoryDepthOrderedListMap().get(master)){
				/*
				if(! sourceDirectory.isLeaf()){
					tableIndex++;
					continue;
				}
				*/
				try{
					exportSpreadSheet(sessionSource, sourceDirectory, masterIndex, tableIndex);
				}catch(IOException ex){
					Logger.getAnonymousLogger().severe(ex.getLocalizedMessage());
				}
				tableIndex++;
			}
			masterIndex++;
		}
		Logger.getAnonymousLogger().info("End export spreadsheet.");
	}
}
