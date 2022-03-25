/**
 * Interface that defines the data types for the SILLY language. 
 *   @author Dave Reed
 *   @version 1/24/22
 */
public interface DataValue extends Comparable<DataValue> {   
    public static enum Type { INTEGER_VALUE, STRING_VALUE, BOOLEAN_VALUE }
    
    public Object getValue();
    public DataValue.Type getType();
    public String toString(); 
}