import org.relaxng.datatype.DatatypeLibrary;
import org.relaxng.datatype.DatatypeLibraryFactory;

public class PrimeDatatypeLibraryFactory 
 implements DatatypeLibraryFactory {

  public final static String namespace 
    = "http://ns.cafeconleche.org/relaxng/primes";

  public DatatypeLibrary createDatatypeLibrary(String namespace) {
    if (PrimeDatatypeLibraryFactory.namespace.equals(namespace)) {
      return new PrimeDatatypeLibrary();
    }
    return null;
  }

}
