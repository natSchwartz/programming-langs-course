import java.util.Scanner;

/**
 * The SILLY Batch Interpreter for executing instructions from a file.
 *   @author Dave Reed 
 *   @version 1/24/22
 */
public class Batch {
    public static void main(String[] args) throws Exception {        
        System.out.print("Enter the program file name: ");       
        Scanner input = new Scanner(System.in);

        String filename = input.next();
        TokenStream infile = new TokenStream(filename);
        
        while (infile.hasNext()) {
            Statement stmt = Statement.getStatement(infile);
            System.out.println(">>> " + stmt);
            try {
                stmt.execute();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        } 
        input.close();
    }    
}