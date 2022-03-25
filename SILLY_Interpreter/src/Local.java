/**
 * Derived class that represents an local assignment statement in the SILLY language.
 *   @author Nat Schwartzenberger
 *   @version 2/24/22
 */
public class Local extends Statement {
	private Assignment localAssign;
	
	/**
	 * Reads in a constant declaration from the given TokenStream.
	 * @param input the stream to be read from 
	 * @throws Exception
	 */
	public Local(TokenStream input) throws Exception {	
		if (!input.next().toString().equals("local")) {
	           throw new Exception("SYNTAX ERROR: Malformed local declaration");
	    } 	
		
		this.localAssign = new Assignment(input);	
	}
	
	/**
	 * Executes the current local statement
	 */
	public void execute() throws Exception {
		
		if (!Interpreter.MEMORY.isDeclaredLocal(localAssign.getVariable())) {
        	Interpreter.MEMORY.declareLocal(localAssign.getVariable());	 
		}
	   
       Pair<DataValue, Boolean> pair = new Pair<> (localAssign.getExpr().evaluate(), false); 
       Interpreter.MEMORY.store(localAssign.getVariable(), pair);
	}
	
	/**
	 * Converts the current constant statement into a string
	 * @return the String representation of this statement
	 */
	public String toString() {
		return "local " + this.localAssign.toString();
	}
}
