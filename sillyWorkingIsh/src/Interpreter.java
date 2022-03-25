/**
 * Main method for the interactive SILLY Interpreter. 
 *   @author Dave Reed 
 *   @version 1/24/22
 */
public class Interpreter {
    public static MemorySpace MEMORY = new MemorySpace();
    
    public static void main(String[] args) throws Exception {        
        TokenStream input = new TokenStream();       
        while (true) {
            System.out.print(">>> ");
            Statement stmt = Statement.getStatement(input);
//            System.out.println(stmt.toString());
            stmt.execute();
        } 

    }
}
