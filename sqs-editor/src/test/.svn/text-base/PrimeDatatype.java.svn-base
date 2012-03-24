import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.DatatypeStreamingValidator;
import org.relaxng.datatype.ValidationContext;
import org.relaxng.datatype.helpers.StreamingValidatorImpl;

public class PrimeDatatype implements Datatype {

	public void checkValid(String literal, ValidationContext context) throws DatatypeException {
		if (!isValid(literal, context)) {
			throw new DatatypeException(literal + " is not a prime number");
		}
	}

	public DatatypeStreamingValidator createStreamingValidator(ValidationContext context) {
	    return new StreamingValidatorImpl(this, context);
	}

	public Object createValue(String literal, ValidationContext context) {
	    if (isPrime(literal)) {
	        return Integer.valueOf(literal);
	    }
	    return null;
	}
	
	private boolean isPrime(String literal) {
	      
		try {
			int candidate = Integer.parseInt(literal); 
			if (candidate < 2){
				return false;
			}
			double max = Math.sqrt(candidate);
			for (int i = 2; i <= max; i++) {
				if (candidate % i == 0){
					return false;
				}
			}
			return true;
		} catch (NumberFormatException ex) {
			return false;      
		}
	}

	public int getIdType() {
		return ID_TYPE_NULL;
	}

	public boolean isContextDependent() {
		return false;
	}

	public boolean isValid(String literal, ValidationContext context) {
		return isPrime(literal);
	}

	public boolean sameValue(Object value1, Object value2) {
		if (value1 == null) {
			return value2 == null;
		} else {
			return value1.equals(value2);
		}
	}

	public int valueHashCode(Object value) {
	    return value.hashCode();
	}

}
