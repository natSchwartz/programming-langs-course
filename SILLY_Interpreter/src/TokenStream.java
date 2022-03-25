import java.util.Scanner;
import java.io.File;

/**
 * Class for reading SILLY language tokens from an input stream, either
 * standard input or from a file.
 *   @author Dave Reed
 *   @version 1/24/22
 */
public class TokenStream {
    private Scanner input;
    private Token nextToken;

    /**
     * Constructs a TokenStream connected to System.in.
     */
    public TokenStream() {
        this.input = new Scanner(System.in);
    }
    
    /**
     * Constructs a TokenStream connected to a file.
     *   @param filename the file to read from
     */
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        this.input = new Scanner(new File(filename));
    }

    /**
     * Returns the next token in the TokenStream (without removing it).
     *   @return the next token
     */
    public Token lookAhead() {
        if (this.nextToken == null && this.input.hasNext()) {
            this.nextToken = new Token(this.input.next());
        }
        return this.nextToken;
    }
    
    /**
     * Returns the next token in the TokenStream (and removes it).
     *   @return the next token
     */
    public Token next() {
        if (this.nextToken == null && this.input.hasNext()) {
            this.nextToken = new Token(this.input.next());
        }
        Token safe = this.nextToken;
        this.nextToken = null;
        return safe;
     }
     
     /**
      * Determines whether there are any more tokens to read.
      *   @return true if tokens remaining, else false
      */
     public boolean hasNext() {
        return (this.nextToken != null || this.input.hasNext());
     }
}
