/**
 * Derived class that represents an constant assignment statement in the SILLY language.
 *   @author Nat Schwartzenberger
 *   @version 2/24/22
 */
public class ConstantDeclaration extends Statement {
	private Assignment constAssign;
	
	/**
	 * Reads in a constant declaration from the given TokenStream.
	 * @param input the stream to be read from 
	 * @throws Exception
	 */
	public ConstantDeclaration(TokenStream input) throws Exception {	
		if (!input.next().toString().equals("const")) {
	           throw new Exception("SYNTAX ERROR: Malformed constant declaration");
	    } 	
		
		this.constAssign = new Assignment(input);	
	}
	
	/**
	 * Executes the current constant statement
	 */
	public void execute() throws Exception {
	   if (!Interpreter.MEMORY.isDeclaredLocal(constAssign.getVariable())) {
		   Interpreter.MEMORY.declareLocal(constAssign.getVariable());
       } 
	   else if (Interpreter.MEMORY.isConstant(constAssign.getVariable())) {
	       	throw new Exception("SYNTAX ERROR: Variable, (" + constAssign.getVariable() + ") already declared as a constant.");
	   } 
	   	   
       Pair<DataValue, Boolean> pair = new Pair<> (constAssign.getExpr().evaluate(), true);
       Interpreter.MEMORY.store(constAssign.getVariable(), pair);
	}
	
	/**
	 * Converts the current constant statement into a string
	 * @return the String representation of this statement
	 */
	public String toString() {
		return "const " + this.constAssign.toString();
	}
}


