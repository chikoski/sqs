import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeBuilder;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.DatatypeLibrary;
import org.relaxng.datatype.helpers.ParameterlessDatatypeBuilder;

public class PrimeDatatypeLibrary implements DatatypeLibrary {

  public Datatype createDatatype(String typeLocalName)
   throws DatatypeException {
    if ("prime".equals(typeLocalName)) {
      return new PrimeDatatype();
    }
    throw new DatatypeException("Unsupported type: " + typeLocalName);
  }

  public DatatypeBuilder createDatatypeBuilder(String baseTypeLocalName) 
   throws DatatypeException {
    return new ParameterlessDatatypeBuilder(
     createDatatype("prime")
    );
  }

}
