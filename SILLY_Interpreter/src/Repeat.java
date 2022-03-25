import java.util.ArrayList;

/**
 * Derived class that represents a repeat statement in the SILLY language.
 *   @author Nat Schwartzenberger
 *   @version 2/5/22
 */
public class Repeat extends Statement {
    private Expression expr;
    private ArrayList<Statement> stmts;
    
    /**
     * Reads in a repeat statement from the specified stream
     *   @param input the stream to be read from
     */
    public Repeat(TokenStream input) throws Exception {
        Token keyword = input.next();
        if (!keyword.toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malformed repeat statement");
        }
        this.expr = new Expression(input);   
        
        this.stmts = new ArrayList<Statement>();
        while (!input.lookAhead().toString().equals("end")) {
        	this.stmts.add(Statement.getStatement(input));
        }
        input.next();
    }

    /**
     * Executes the current while statement.
     */
    public void execute() throws Exception {
        if (this.expr.evaluate().getType() == DataValue.Type.INTEGER_VALUE) {
        	for (int i = 0; i < ((Integer) this.expr.evaluate().getValue()); i++) {
        		Interpreter.MEMORY.createScope(false);
        		for (Statement nextStmt : this.stmts) {
                	nextStmt.execute();
                }
        		Interpreter.MEMORY.destroyScope();
        	}
        }
            
        if (this.expr.evaluate().getType() != DataValue.Type.INTEGER_VALUE) {
            throw new Exception("RUNTIME ERROR: repeat statement requires Integer test.");
        }
    }

    /**
     * Converts the current while statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        String msg = "repeat " + this.expr + "\n";
        for (Statement nextStmt : this.stmts) {
        	msg += "    " + nextStmt + "\n";
        }
        return msg + "end";
    }
}
