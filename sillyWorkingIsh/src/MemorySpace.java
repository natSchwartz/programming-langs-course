import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Class that defines the memory space for the SILLY interpreter.
 * 
 * @author Dave Reed modified by Nat Schwartzenberger
 * @version 3/24/22
 */
public class MemorySpace {
	private Scope globalScope;
	private Stack<Scope> globalStack;
	private HashMap<Token, Pair<ArrayList<Token>, ArrayList<Statement>>> subroutines;

	/**
	 * Constructs an empty memory space.
	 */
	public MemorySpace() {
		this.subroutines = new HashMap<Token, Pair<ArrayList<Token>, ArrayList<Statement>>>();
		this.globalStack = new Stack<>();
		this.globalScope = new Scope(null, false);
		this.globalStack.push(globalScope);
	}

	/**
	 * Declares a variable globally (without storing an actual value).
	 * 
	 * @param variable the variable to be declared
	 */
	public void declareGlobal(Token variable) {
		Scope currScope = this.globalStack.peek();
		if (currScope.isSub()) {
			currScope.declare(variable);
		} else {
			this.globalScope.declare(variable);
		}
	}

	/**
	 * Declares a variable locally (without storing an actual value)
	 * 
	 * @param variable the variable to be declared
	 */
	public void declareLocal(Token variable) {
		Scope currScope = this.globalStack.peek();
		currScope.declare(variable);
	}

	/**
	 * Determines if a variable is already declared.
	 * 
	 * @param variable the variable to be found
	 * @return true if it is declared and/or assigned
	 */
	public boolean isDeclared(Token variable) {
		Scope currScope = this.globalStack.peek();

		while (!currScope.isCurrDeclared(variable)) {
			if (!currScope.isSub()) {
				if (currScope.getParent() == null) {
					return this.globalScope.isCurrDeclared(variable);
				} else {
					currScope = currScope.getParent();
				}
			} else {
				break;
			}
		}
		return currScope.isCurrDeclared(variable);
	}

	/**
	 * Checks to see if a variable has been declared in the local scope.
	 * 
	 * @param variable the variable to be declared
	 * @return true if the variable is declared locally, false if not
	 */
	public boolean isDeclaredLocal(Token variable) {
		Scope currScope = this.globalStack.peek();
		return currScope.isCurrDeclared(variable);
	}

	/**
	 * Determines the value associated with a variable in memory.
	 * 
	 * @param variable the variable to look up
	 * @return the value associated with that variable
	 */
	public DataValue lookup(Token variable) {
		Scope currScope = this.globalStack.peek();
		return this.stackLookup(variable, currScope);
	}

	/**
	 * A helper method for lookup() that searches through the scopes in the stack to
	 * find the scope that the variable belongs to and returns the DataValue stored
	 * at that location
	 * 
	 * @param variable the variable to look ups
	 * @return the value associated with that variable
	 */
	private DataValue stackLookup(Token variable, Scope startScope) {
		if (startScope.getParent() != null) { 
			if (startScope.isCurrDeclared(variable)) {
				return startScope.lookup(variable);
			} else {
				return stackLookup(variable, startScope.getParent());
			}
		} else {
			return this.globalScope.lookup(variable);
		}
	}

	/**
	 * Stores a variable/value in the stack segment.
	 * 
	 * @param variable the variable name
	 * @param pair     the value of the variable and if it is a constant or not
	 */
	public void store(Token variable, Pair<DataValue, Boolean> pair) {
		Scope currScope = this.globalStack.peek();
		stackStore(variable, pair, currScope);
	}

	/**
	 * A helper method for Store(), goes through the stack to find the correct
	 * scope/location to store the given variable/pair
	 * 
	 * @param variable   the variable to stored
	 * @param pair       the value of the variable and if it is a constant or not
	 * @param startScope the scope to start at
	 */
	private void stackStore(Token variable, Pair<DataValue, Boolean> pair, Scope startScope) {
		if (startScope.getParent() != null) {
			if (startScope.isCurrDeclared(variable)) {
				startScope.store(variable, pair);
			} else {
				stackStore(variable, pair, startScope.getParent());
			}
		} else {
			this.globalScope.store(variable, pair);
		}
	}

	/**
	 * Checks to see if a variable has been declared as a constant.
	 * 
	 * @param variable the variable to look up
	 * @return true if the variable is a constant, false if not
	 */
	public boolean isConstant(Token variable) {
		Scope currScope = this.globalStack.peek();
		return constLookup(variable, currScope);
	}

	/**
	 * Helper method for isConstant, searches through the stack using recursion to
	 * find where a variable is declared and if it is constant or not
	 * 
	 * @param variable
	 * @param startScope
	 * @return true if the given variable is a constant, false if not
	 */
	private boolean constLookup(Token variable, Scope startScope) {
		if (startScope.getParent() != null) {
			if (startScope.isCurrDeclared(variable)) {
				return startScope.isConstant(variable);
			} else {
				return constLookup(variable, startScope.getParent());
			}
		} else {
			return this.globalScope.isConstant(variable);
		}
	}

	/**
	 * Creates a new scope and adds it to the global memory stack
	 */
	public void createScope(boolean isSub) {
		Scope parent = this.globalStack.peek();
		Scope newScope = new Scope(parent, isSub);
		this.globalStack.push(newScope);
	}

	/**
	 * Removes the topmost scope from the global memory stack
	 */
	public void destroyScope() {
		this.globalStack.pop();
	}

	/**
	 * Stores a subroutine in memory.
	 * 
	 * @param id    the name of the subroutine
	 * @param stmts the list of statements in the subroutine
	 */
	public void decSubroutine(Token id, Pair<ArrayList<Token>, ArrayList<Statement>> pair) {
		this.subroutines.put(id, pair);
	}

	/**
	 * Determines if a subroutine is already declared.
	 * 
	 * @param id the name of the subroutine
	 * @return true if the subroutine has been declared, false if not
	 */
	public boolean isSubDeclared(Token id) {
		return this.subroutines.containsKey(id);
	}

	/**
	 * Executes a subroutine call
	 * 
	 * @param id the name of the subroutine to be called
	 * @return an Pair<E,T> of ArrayLists (Parameters, Statements) to be executed by
	 *         the subroutine
	 */
	public Pair<ArrayList<Token>, ArrayList<Statement>> callSubroutine(Token id) {
		return this.subroutines.get(id);
	}
}
