/**
 * Class that represents a String value.
 *   @author Dave Reed
 *   @version 1/24/22
 */
public class StringValue implements DataValue {
    private String strVal;

    /**
     * Constructs a String value.
     *   @param str the String being stored
     */
    public StringValue(String str) {
        this.strVal = str;
    }

    /**
     * Accesses the stored String value.
     *   @return the String value (as an Object)
     */
    public Object getValue() {
        return this.strVal;
    }

    /**
     * Identifies the actual type of the value.
     *   @return Token.Type.STRING
     */
    public DataValue.Type getType() {
        return DataValue.Type.STRING_VALUE;
    }

    /**
     * Converts the String value to a String.
     *   @return the stored String value
     */
    public String toString() {
        return this.strVal;
    }

    /**
     * Comparison method for StringValues.
     *   @param other the value being compared with
     *   @return negative if <, 0 if ==, positive if >
     */
    public int compareTo(DataValue other) {
        return ((String)this.getValue()).compareTo((String)other.getValue());
    }
}
