import java.util.ArrayList;

/**
 * Derived class that represents a subroutine call statement in the SILLY
 * language.
 * 
 * @author Nat Schwartzenberger
 * @version 3/24/22
 */
public class subCall extends Statement {
	private ArrayList<Expression> paramVals;
	private Token id;

	/**
	 * Reads in a subroutine call in the SILLY language
	 *
	 * @param input the stream to be read from
	 * @throws Exception
	 */
	public subCall(TokenStream input) throws Exception {
		
		Token keyword = input.next();
		this.id = input.next();

		if (!keyword.toString().equals("call") || id.getType() != Token.Type.IDENTIFIER
				|| !input.next().toString().equals("(")) {
			throw new Exception("SYNTAX ERROR: Malformed subroutine call statement");
		}

		this.paramVals = new ArrayList<Expression>();
		while (!input.lookAhead().toString().equals(")")) {
			Expression param = new Expression(input);
			this.paramVals.add(param);
		}

		input.next();
	}

	/**
	 * Calls the current subroutine from memory and executes it
	 */
	public void execute() throws Exception {
		ArrayList<DataValue> paramList = new ArrayList<DataValue>();
		for (Expression param : paramVals) {
				paramList.add(param.evaluate());
		}

		if (!Interpreter.MEMORY.isSubDeclared(this.id)) {
			throw new Exception("SYNTAX ERROR: Subroutine, ( " + this.id + " ) had not be declared.");
		}
		else {
			Interpreter.MEMORY.createScope(true);
			
			Pair<ArrayList<Token>, ArrayList<Statement>> subPair = Interpreter.MEMORY.callSubroutine(id);
			ArrayList<Statement> stmts = subPair.getValue();
			ArrayList<Token> params = subPair.getKey();

			if (paramVals.size() == params.size()) {
				for (int i = 0; i < params.size(); i++) {
					if (!Interpreter.MEMORY.isDeclaredLocal(params.get(i))) {
						Interpreter.MEMORY.declareLocal(params.get(i));
					}	
					
					Pair<DataValue, Boolean> pair = new Pair<>(paramList.get(i), false);
					Interpreter.MEMORY.store(params.get(i), pair);	
				}	
			} 
			else {
				throw new Exception("SYNTAX ERROR: Incorrect number of parameters.");
			}

			for (Statement nextStmt : stmts) {
				nextStmt.execute();
			}
			Interpreter.MEMORY.destroyScope();
		}
	}

	/**
	 * Converts the current subroutine call into a String.
	 * 
	 * @return the String representation of this statement
	 */
	public String toString() {
		String msg = "call " + this.id.toString() + " ( ";
		for (Expression nextExpr : this.paramVals) {
			msg += nextExpr + " ";
		}
		return msg + ")\n";
	}
}
