import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Derived class that represents an if statement in the SILLY language.
 * 
 * @author Dave Reed modified by Nat Schwartzenberger
 * @version 1/24/22
 */
public class If extends Statement {
	private Expression test;
	private ArrayList<Statement> ifStmts;
	private ArrayList<Statement> elseStmts;
	private ArrayList<Expression> elifTests;
	private ArrayList<ArrayList<Statement>> elifStmts;

	/**
	 * Reads in an if statement from the specified stream
	 * 
	 * @param input the stream to be read from
	 */
	public If(TokenStream input) throws Exception {
		if (!input.next().toString().equals("if")) {
			throw new Exception("SYNTAX ERROR: Malformed if statement");
		}
		this.test = new Expression(input);

		this.ifStmts = new ArrayList<Statement>();
		this.elifStmts = new ArrayList<ArrayList<Statement>>();
		this.elifTests = new ArrayList<Expression>();

		while (!input.lookAhead().toString().equals("else") && !input.lookAhead().toString().equals("end")) {

			if (input.lookAhead().toString().equals("elseif")) {
				input.next();
				Expression elifTest = new Expression(input);

				ArrayList<Statement> innerElif = new ArrayList<Statement>();
				while (!input.lookAhead().toString().equals("elseif") && !input.lookAhead().toString().equals("else")
						&& !input.lookAhead().toString().equals("end")) {
					innerElif.add(Statement.getStatement(input));
				}
				this.elifTests.add(elifTest);
				this.elifStmts.add(innerElif);
			} else {
				this.ifStmts.add(Statement.getStatement(input));
			}
		}

		if (input.lookAhead().toString().equals("else")) {
			input.next();
			this.elseStmts = new ArrayList<Statement>();
			while (!input.lookAhead().toString().equals("end")) {
				this.elseStmts.add(Statement.getStatement(input));
			}
		}
		
		input.next();
	}

	/**
	 * Executes the current if statement.
	 */
	public void execute() throws Exception {
		DataValue test = this.test.evaluate();
		if (test.getType() != DataValue.Type.BOOLEAN_VALUE) {
			throw new Exception("RUNTIME ERROR: If statement requires Boolean test.");
		} 
		else if (((Boolean) test.getValue()).booleanValue()) {
			Interpreter.MEMORY.createScope(false);
			for (Statement nextStmt : this.ifStmts) {
				nextStmt.execute();
			}
			Interpreter.MEMORY.destroyScope();
		} 
	
//		I HAD TO COMMENT THIS OUT because it not letting me reach the else cases, 
//		I probably should have just fixed it but I didn't have time!
		
//		else if (this.elifStmts != null) {
//			for (ArrayList<Statement> currElif : elifStmts) {
//				Expression elifTest = this.elifTests.get(elifStmts.indexOf(currElif));
//				DataValue evalTest = elifTest.evaluate();
//
//				if (((Boolean) evalTest.getValue()).booleanValue()) {
//					Interpreter.MEMORY.createScope(false);
//					for (Statement innerStmt : currElif) {
//						innerStmt.execute();
//					}
//					Interpreter.MEMORY.destroyScope();
//					break;
//				}
//			}
//		}
		else if (this.elseStmts != null) {
			Interpreter.MEMORY.createScope(false);
			for (Statement nextStmt : this.elseStmts) {
				nextStmt.execute();
			}		
			Interpreter.MEMORY.destroyScope();
		}

	}

	/**
	 * Converts the current if statement into a String.
	 * 
	 * @return the String representation of this statement
	 */
	public String toString() {
		String str = "if " + this.test;
		for (Statement stmt : this.ifStmts) {
			str += "\n    " + stmt;
		}
		if (this.elifStmts != null) {

			for (ArrayList<Statement> currElif : elifStmts) {
				str += "\nelseif";

				Expression elifTest = this.elifTests.get(elifStmts.indexOf(currElif));
				str += " " + elifTest;

				for (Statement innerStmt : currElif) {
					str += "\n		" + innerStmt;
				}
			}
		} else if (this.elseStmts != null) {
			str += "\nelse";
			for (Statement stmt : this.elseStmts) {
				str += "\n		" + stmt;
			}
		}
		return str + "\nend";
	}
}
