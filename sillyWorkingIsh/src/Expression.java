/**
 * Derived class that represents an expression in the SILLY language.
 *   @author Dave Reed modified by Nat Schwartzenberger
 *   @version 2/5/22
 */
public class Expression {
    private Term term1;
    private Token op;
    private Term term2;

    /**
     * Creates an expression from the specified TokenStream.
     *   @param input the TokenStream from which the program is read
     */
    public Expression(TokenStream input) throws Exception {
    	if (input.lookAhead().toString().equals("(")) {
    		input.next();
    		
    		if(input.lookAhead().getType() == Token.Type.UNARY_OP) {
    			this.term1 = null;
    		} else {
    			this.term1 = new Term(input);
    		}	
    		
    		this.op = input.next();
	    		
	    	if (this.op.getType() != Token.Type.BINARY_OP && this.op.getType() != Token.Type.UNARY_OP) {
	    		throw new Exception("RUNTIME ERROR: malformed expression.");
	   		}
	   		this.term2 = new Term(input);
	   		
	   	    if (!input.next().toString().equals(")")) {
    	    	throw new Exception("RUNTIME ERROR: malformed expression.");
    	    }
    	}
    	else {
    		this.term1 = new Term(input); 
    	}
    }

    /**
     * Evaluates the current expression.
     *   @return the value represented by the expression
     */
    public DataValue evaluate() throws Exception {
        if (this.op == null) { 
            return this.term1.evaluate(); 
            
        } else if (this.op.getType() == Token.Type.UNARY_OP){
        	DataValue rhs = this.term2.evaluate();
        	
        		if (this.op.toString().equals("not")) {
	        		if (rhs.getType() == DataValue.Type.BOOLEAN_VALUE) {
	        			boolean trueOrFalse = ((boolean) rhs.getValue());
	        			if(trueOrFalse == true) {
	        				return new BooleanValue(false); 
	        			} else {
	        				return new BooleanValue(true);
	        			}
	        		} else {
	        			throw new Exception("RUNTIME ERROR: Type mismatch in unary expression."); 
	        		}
        		} else if (this.op.toString().equals("#")) {
        			if (rhs.getType() == DataValue.Type.STRING_VALUE) {
        				String inputStr = ((String) rhs.getValue());
        				return new IntegerValue(inputStr.length() - 2);
        					
        			} else {
        				throw new Exception("RUNTIME ERROR: Type mismatch in unary expression.");
        			}
        		}
   
        } else if (this.op.getType() == Token.Type.BINARY_OP) {
            DataValue lhs = this.term1.evaluate();
            DataValue rhs = this.term2.evaluate();

            if (lhs.getType() == DataValue.Type.STRING_VALUE
                    && rhs.getType() == DataValue.Type.INTEGER_VALUE) {
                if (op.toString().equals("@")) {
                    String str = lhs.toString();
                    int index = ((Integer) (rhs.getValue()));
                    if (index >= 0 || index < str.length() - 2) {
                        return new StringValue("\"" + str.substring(index + 1, index + 2) + "\"");
                    }
                }
            } else if (lhs.getType() == rhs.getType()) {
                if (op.toString().equals("==")) {
                    return new BooleanValue(lhs.compareTo(rhs) == 0);
                } else if (op.toString().equals("!=")) {
                    return new BooleanValue(lhs.compareTo(rhs) != 0);
                } else if (op.toString().equals(">")) {
                    return new BooleanValue(lhs.compareTo(rhs) > 0);
                } else if (op.toString().equals(">=")) {
                    return new BooleanValue(lhs.compareTo(rhs) >= 0);
                } else if (op.toString().equals("<")) {
                    return new BooleanValue(lhs.compareTo(rhs) < 0);
                } else if (op.toString().equals("<=")) {
                    return new BooleanValue(lhs.compareTo(rhs) <= 0);
                } else if (lhs.getType() == DataValue.Type.STRING_VALUE) {
                    if (op.toString().equals("+")) {
                        String str1 = lhs.toString();
                        String str2 = rhs.toString();
                        return new StringValue(str1.substring(0, str1.length() - 1)
                                + str2.substring(1));
                    }
                } else if (lhs.getType() == DataValue.Type.INTEGER_VALUE) {
                    int num1 = ((Integer) (lhs.getValue()));
                    int num2 = ((Integer) (rhs.getValue()));

                    if (op.toString().equals("+")) {
                        return new IntegerValue(num1 + num2);
                    } else if (op.toString().equals("-")) {
                        return new IntegerValue(num1 - num2);
                    } else if (op.toString().equals("*")) {
                        return new IntegerValue(num1 * num2);
                    } else if (op.toString().equals("/")) {
                        return new IntegerValue(num1 / num2);
                    } else if (op.toString().equals("%")) {
                        return new IntegerValue(num1 % num2);
                    } else if (op.toString().equals("^")) {
                        return new IntegerValue((int)Math.pow(num1, num2));
                    }
                } else if (lhs.getType() == DataValue.Type.BOOLEAN_VALUE) {
                    boolean b1 = ((Boolean) (lhs.getValue()));
                    boolean b2 = ((Boolean) (rhs.getValue()));

                    if (op.toString().equals("and")) {
                        return new BooleanValue(b1 && b2);
                    } else if (op.toString().equals("or")) {
                        return new BooleanValue(b1 || b2);
                    }
                }
            }
            throw new Exception("RUNTIME ERROR: Type mismatch in binary expression");
        }
        return null;
    }

    /**
     * Converts the current expression into a String.
     *   @return the String representation of this expression
     */
    public String toString() {
        if (this.op == null) {
        	return this.term1.toString();
        }
        else if (this.op.getType() == Token.Type.BINARY_OP){
        	return "( " + this.term1 + " " + this.op + " " + this.term2 + " )";
        } else {
        	return "( " + this.op + " " + this.term2 + " )";
        }
    }
}
