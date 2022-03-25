import java.util.ArrayList;

/**
 * Derived class that represents an output statement in the SILLY language.
 *   @author Dave Reed
 *   @version 1/24/22
 */
public class Output extends Statement {
    private ArrayList<Expression> expr;

    /**
     * Reads in an output statement from the specified TokenStream.
     *   @param input the stream to be read from
     */
    public Output(TokenStream input) throws Exception {
        this.expr = new ArrayList<Expression>();

        if (!input.next().toString().equals("output") || !input.next().toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Malformed output statement");
        }
        
        while (!input.lookAhead().toString().equals(")")) {
            this.expr.add(new Expression(input));
        }
        input.next();
    }

    /**
     * Executes the current output statement.
     */
    public void execute() throws Exception {
        for (Expression e : this.expr) {
            DataValue result = e.evaluate();
            // System.out.println(e + " " + result);
            if (result.getType() == DataValue.Type.STRING_VALUE) {
                String str = (String)(result.getValue());
                System.out.print(str.substring(1, str.length()-1) + " ");
            }
            else {
                System.out.print(result.getValue() + " ");
            }
        }
        System.out.println();
    }

    /**
     * Converts the current output statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        String outval = "output ( ";
        for (Expression e : this.expr) {
            outval += e + " ";
        }
        return outval + ")";
    }
}


