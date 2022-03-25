import java.util.ArrayList;

/**
 * Derived class that represents a subroutine declaration statement in the SILLY
 * language.
 * 
 * @author Nat Schwartzenberger
 * @version 3/21/22
 */
public class subDec1 extends Statement {
	private ArrayList<Token> params;
	private ArrayList<Statement> stmts;
	private Token id;

	/**
	 * Reads in a subroutine statement from the specified stream
	 * 
	 * @param input the stream to be read from
	 * @throws Exception
	 */
	public subDec1(TokenStream input) throws Exception {
		Token keyword = input.next();
		this.id = input.next();

		if (!keyword.toString().equals("sub") || id.getType() != Token.Type.IDENTIFIER
				|| !input.next().toString().equals("(")) {
			throw new Exception("SYNTAX ERROR: Malformed subroutine declaration statement");
		}

		this.params = new ArrayList<Token>();
		while (!input.lookAhead().toString().equals(")")) {
			Token param = input.next();
			if (param.getType() != Token.Type.IDENTIFIER) {
				throw new Exception("SYNTAX ERROR: Malformed subroutine parameter(s).");
			} else {
				this.params.add(param);
			}
		}

		input.next();

		this.stmts = new ArrayList<Statement>();
		while (!input.lookAhead().toString().equals("end")) {
			this.stmts.add(Statement.getStatement(input));	
		}
		
		input.next();
	}

	/**
	 * Declares the current subroutine in memory
	 */
	public void execute() throws Exception {
		if (!Interpreter.MEMORY.isSubDeclared(this.id)) {
			Pair<ArrayList<Token>, ArrayList<Statement>> pair = new Pair<>(this.params, this.stmts);
			Interpreter.MEMORY.decSubroutine(this.id, pair);
		} else {
			throw new Exception("SYNTAX ERROR: Subroutine, ( " + this.id + " ) already exists.");
		}
	}

	/**
	 * Converts the current subroutine declaration into a String.
	 * 
	 * @return the String representation of this statement
	 */
	public String toString() {
		String msg = "sub " + this.id.toString() + " ( ";
		for (Token nextTerm : this.params) {
			msg += nextTerm + " ";
		}
		msg += " )\n";

		for (Statement nextStmt : this.stmts) {
			msg += "    " + nextStmt + "\n";
		}
		return msg + "end";
	}

}
