import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.kohsuke.rngom.ast.util.CheckingSchemaBuilder;
import org.kohsuke.rngom.binary.SchemaBuilderImpl;
import org.kohsuke.rngom.parse.IllegalSchemaException;
import org.kohsuke.rngom.parse.Parseable;
import org.kohsuke.rngom.parse.xml.SAXParseable;
import org.kohsuke.rngom.ast.builder.SchemaBuilder;
import org.kohsuke.rngom.ast.om.ParsedPattern;
import org.kohsuke.rngom.binary.SchemaPatternBuilder;
import org.relaxng.datatype.DatatypeLibraryFactory;
import org.relaxng.datatype.helpers.DatatypeLibraryLoader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;


public class RNGOMTest extends TestCase {
	public void test(){
		try{
			String filename = "/tmp/hoge.xml";
			
			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			
			ErrorHandler errorHandler = new ErrorHandler(){
				public void warning(SAXParseException ex){
					showError(ex);
				}
				public void error(SAXParseException ex){
					showError(ex);
				}
				public void fatalError(SAXParseException ex){
					showError(ex);
				}
				private void showError(SAXParseException ex){
					Logger.getLogger(class).severe(ex.getSystemId()+"["+ex.getLineNumber()+":"+ex.getColumnNumber()+"] "+ex.getMessage());
				}
			};
		
			InputSource source = new InputSource(in);
			Parseable parseable = new SAXParseable(source, errorHandler);
		
			DatatypeLibraryFactory datatypeLibraryFactory = new DatatypeLibraryLoader();
			SchemaBuilder schemaBuilder = new SchemaBuilderImpl(errorHandler, datatypeLibraryFactory, new SchemaPatternBuilder());
			ParsedPattern pattern = parseable.parse(new CheckingSchemaBuilder(schemaBuilder, errorHandler, datatypeLibraryFactory));
			
			in.close();
		}catch(IllegalSchemaException ex){
			fail(ex.getMessage());
		}catch(IOException ex){
			fail(ex.getMessage());
		}
	}
}
