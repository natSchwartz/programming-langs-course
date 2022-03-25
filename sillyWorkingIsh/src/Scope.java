import java.util.HashMap;

/**
 * Class that defines a Scope object for the SILLY interpreter.
 *   @author Nat Schwartzenberger
 *   @version 2/16/22
 */
public class Scope {
	private HashMap<Token, Pair<DataValue, Boolean>> currentScope; 
	public Scope parentScope;
	public boolean sub;
	
	
	 /**
     * Constructs an empty local scope. 
     * Links the current scope to its parent scope.
     */
	public Scope(Scope parent, boolean isSub) {
		this.parentScope = parent;
		this.sub = isSub;
		this.currentScope = new HashMap<Token,  Pair<DataValue, Boolean>>(); 
	}
	
	/**
	 * Retrieves the current scope's parent scope
	 * @return the parent scope to the current scope.
	 */
	public Scope getParent() {
		return this.parentScope;
	}
	
	  /**
     * Declares a variable (without storing an actual value).
     *   @param variable the variable to be declared
     */
    public void declare(Token variable) {
        this.currentScope.put(variable, null); 
    }
    
    /** 
     * Determines if a variable is already declared.
     * @param variable the variable to be found
     * @return true if it is declared and/or assigned
     */
    public boolean isCurrDeclared(Token variable) {
    	return this.currentScope.containsKey(variable);
    }

    /**
     * Determines the value associated with a variable in memory.
     *   @param variable the variable to look up
     *   @return the value associated with that variable
     */      
    public DataValue lookup(Token variable) { 
        return (this.currentScope.get(variable).getKey()); //breaking here because n = null with the recursive call
    }
    
    /**
     * Checks to see if a variable has been declared as a constant.
     * @param variable the variable to look up
     * @return true if the variable is a constant, false if not 
     */
    public boolean isConstant(Token variable) {
    	return (this.currentScope.get(variable).getValue()); 
    }
    
    /**
     * Checks to see if a variable has been declared as a local.
     * @param variable the variable to look up
     * @return true if the variable is a constant, false if not 
     */
    public boolean isLocal(Token variable) {
    	return (this.currentScope.get(variable).getValue()); 
    }
    
    /**
     * Stores a variable/value in the stack segment.
     *   @param variable the variable name
     *   @param val the value to be stored under that name
     */
    public void store(Token variable, Pair<DataValue,Boolean> pair) {
        this.currentScope.put(variable, pair);
    }
    
    
    /**
     * 
     * @return
     */
    public boolean isSub() {
    	return this.sub;
    }
}


