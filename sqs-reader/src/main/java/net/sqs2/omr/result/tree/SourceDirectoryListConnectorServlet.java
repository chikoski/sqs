package net.sqs2.omr.result.tree;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.tree.PathInfoParser.SourceDirectoryListPathItem;
import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * 
 # jQuery File Tree Ruby Connector # # Version 1.01 # # Erik Lax #
 * http://datahack.se # 13 July 2008 # # History # # 1.01 Initial Release # #
 * Output a list of files for jQuery File Tree #
 * 
 * #<settings> #root = "/absolute/path/" # or root = File.expand_path(".")
 * #</settings>
 * 
 * #<code>
require "cgi"
cgi = CGI.new
cgi.header("type" => "text/html")
dir = cgi.params["dir"].to_s

puts "<ul class=\"jqueryFileTree\" style=\"display: none;\">"
begin
	path = root + "/" + dir 

	# chdir() to user requested dir (root + "/" + dir) 
	Dir.chdir(File.expand_path(path).untaint);
	
	# check that our base path still begins with root path
	if Dir.pwd[0,root.length] == root then

		#loop through all directories
		Dir.glob("*") {
			|x|
			if not File.directory?(x.untaint) then next end 
			puts "<li class=\"directory collapsed\"><a href=\"#\" rel=\"#{dir}#{x}/\">#{x}</a></li>";
		}

		#loop through all files
		Dir.glob("*") {
			|x|
			if not File.file?(x.untaint) then next end 
			ext = File.extname(x)[1..-1]
			puts "<li class=\"file ext_#{ext}\"><a href=\"#\" rel=\"#{dir}#{x}\">#{x}</a></li>"
		}
	else
		#only happens when someone tries to go outside your root directory...
		puts "You are way out of your league"
	end 
rescue 
	puts "Internal Error"
end
puts "</ul>"
#</code>
 * 
 * @author hiroya
 * 
 */
public class SourceDirectoryListConnectorServlet extends ConsoleConnectorServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void printPathTreeItems(final PrintWriter w, long sessionID, final String pathInfo) {
		printSourceFolderTreeItems(w, sessionID, pathInfo, new SourceDirectoryListPathInfoParser(pathInfo).parse());
	}

	private void printSourceFolderTreeItems(final PrintWriter w, long sessionID, final String rootPath, final SourceDirectoryListPathItem pathItem) {
		if(pathItem == null){
			return;
		}
		SourceDirectory sourceDirectory = pathItem.getSourceDirectory();
		final String SLASH_AS_FOLDER_DUMMY_EXT = "/";
		if(sourceDirectory == null){
			FormMaster formMaster = pathItem.getFormMaster();
			sourceDirectory = SessionSources.getInitializedInstance(pathItem.getSessionID()).getContentIndexer().getSourceDirectoryRoot(formMaster);
			printListItems(w, rootPath, new ModelToPathItemFactory<SourceDirectory>() {
				public ListItem create(int index, SourceDirectory sourceDirectoryRoot) {
					String ZERO_PATHITEM = "g/";
					return new ListItem(File.separator, ZERO_PATHITEM, SLASH_AS_FOLDER_DUMMY_EXT,
							ListItem.State.COLLAPSED_BRANCH);
				}
			}.create(0, sourceDirectory));
		}else{
			int index = 0;
			List<SourceDirectory> sourceDirectoryChildList = sourceDirectory.getChildSourceDirectoryList();
			if(sourceDirectoryChildList == null){
				return;
			}
			for(SourceDirectory sourceDirectoryChild: sourceDirectoryChildList){
				if(! sourceDirectoryChild.getDefaultFormMaster().equals(sourceDirectory.getDefaultFormMaster())){
					continue;
				}
				printListItems(w, rootPath, new ModelToPathItemFactory<SourceDirectory>() {
					public ListItem create(int index, SourceDirectory sourceDirectoryChild) {
						return new ListItem(sourceDirectoryChild.getDirectory().getName(), URLSafeRLEBase64.encode(index)+"/", SLASH_AS_FOLDER_DUMMY_EXT,
								ListItem.State.COLLAPSED_BRANCH);
					}
				}.create(index++, sourceDirectoryChild));
			}
			/*
			FormMaster formMaster = sourceDirectory.getFormMaster();
			int numQuestions = formMaster.getNumQuestions();
			for (int questionIndex = 0; questionIndex < numQuestions; questionIndex++) {
				List<FormArea> formAreaList = formMaster.getFormAreaList(questionIndex);
				new ModelToPathItemFactory<List<FormArea>>() {
					public ListItem create(int questionIndex, List<FormArea> formAreaList) {
						FormArea primaryFormArea = formAreaList.get(0);
						return new ListItem(primaryFormArea.getLabel()+" "+primaryFormArea.getHint(),
								Integer.toString(questionIndex), primaryFormArea.getType(),
								ListItem.State.COLLAPSED_BRANCH);
					}
				}.create(questionIndex, formAreaList);
			}*/
		}
	}
}

