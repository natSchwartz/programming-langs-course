/**
 * Derived class that represents an assignment statement in the SILLY language.
 *   @author Dave Reed modified by Nat Schwartzenberger 
 *   @version 2/24/22
 */
public class Assignment extends Statement {
    private Token vbl;
    private Expression expr;
    
    /**
     * Reads in a assignment statement from the specified TokenStream.
     *   @param input the stream to be read from
     */
    public Assignment(TokenStream input) throws Exception {
        this.vbl = input.next();
        if (this.vbl.getType() != Token.Type.IDENTIFIER) { 
	        throw new Exception("SYNTAX ERROR: Illegal lhs of assignment statement (" + this.vbl + ")");
        } 
        
        if (!input.next().toString().equals("=")) {
	        throw new Exception("SYNTAX ERROR: Malformed assignment statement (expecting '=')");
        } 

        this.expr = new Expression(input);
    }
    
    /**
     * Executes the current assignment statement.
     */
    public void execute() throws Exception {
    	
        if (!Interpreter.MEMORY.isDeclared(this.vbl)) { 
        	Interpreter.MEMORY.declareGlobal(this.vbl);   
        }
        else if (Interpreter.MEMORY.isConstant(this.vbl)) {
	       	throw new Exception("SYNTAX ERROR: Variable, (" + this.vbl + ") declared as a constant.");
	   } 
          
       Pair<DataValue, Boolean> pair = new Pair<> (this.expr.evaluate(), false);
       Interpreter.MEMORY.store(this.vbl, pair);
    }
    
    /**
     * Returns the variable of the current assignment statement
     * @return the variable of the assignment 
     */
    public Token getVariable() {
    	return this.vbl;
    }
    
    /**
     * Returns the expression of the current assignment statement
     * @return the expression of the assignment 
     */
    public Expression getExpr() {
    	return this.expr;
    }
    
    /**
     * Converts the current assignment statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        return this.vbl + " = " + this.expr;
    }
}
