import java.util.ArrayList;

/**
 * Derived class that represents a while statement in the SILLY language.
 *   @author Dave Reed
 *   @version 1/24/22
 */
public class While extends Statement {
    private Expression expr;
    private ArrayList<Statement> stmts;
    
    /**
     * Reads in a while statement from the specified stream
     *   @param input the stream to be read from
     */
    public While(TokenStream input) throws Exception {
        Token keyword = input.next();
        if (!keyword.toString().equals("while")) {
            throw new Exception("SYNTAX ERROR: Malformed while statement");
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
        while (this.expr.evaluate().getType() == DataValue.Type.BOOLEAN_VALUE
                && ((Boolean) (this.expr.evaluate().getValue())).booleanValue()) {
        	Interpreter.MEMORY.createScope(false);
            for (Statement nextStmt : this.stmts) {
            	nextStmt.execute();
            }
            Interpreter.MEMORY.destroyScope();
        }
        if (this.expr.evaluate().getType() != DataValue.Type.BOOLEAN_VALUE) {
            throw new Exception("RUNTIME ERROR: while statement requires Boolean test.");
        }
    }

    /**
     * Converts the current while statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        String msg = "while " + this.expr + "\n";
        for (Statement nextStmt : this.stmts) {
        	msg += "    " + nextStmt + "\n";
        }
        return msg + "end";
    }
}
