import java.io.*;

public class ExpressionTranslator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    CodeGenerator code = new CodeGenerator();

    public ExpressionTranslator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
      look = lex.lexical_scan(pbr);
      System.out.println("token = " + look);
    }

    void error(String s) {
    	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
      if (look.tag == t) {
    	    if (look.tag != Tag.EOF) move();
    	} else error("syntax error");
    }

    public void prog() {
	// ... completare ...
        	match(Tag.PRINT);
        	match('(');
        	expr();
        	code.emit(OpCode.invokestatic,1);
        	match(')');
        	match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                    System.out.println("IO error\n");
                };
	// ... completare ...
    }

    private void expr() {
  	  if(look.tag == '(' || look.tag == Tag.NUM){
        term();
        exprp();
      } else error("syntax error");
    }

    // ... completare ...

    private void exprp() {
        switch(look.tag) {

            case '+':
                match('+');
                term();
                code.emit(OpCode.iadd);
                exprp();
                break;

            case '-':
              match('-');
              term();
              code.emit(OpCode.isub);
              exprp();
              break;

            case Tag.EOF:
              //do nothing
              break;

            case ')':
              //do nothing
              break;

            default:
            error("syntax error");
     }
   }

	private void term() {
		if(look.tag == '(' || look.tag == Tag.NUM){
			fact();
		termp();
		} else error("syntax error");
	}

    private void termp() {
        switch(look.tag){

			case '*':
				match('*');
				fact();
				code.emit(OpCode.imul);
				termp();
			break;

			case '/':
				match('/');
				fact();
				code.emit(OpCode.idiv);
				termp();
			break;

			case '+':
				//do nothing
			break;

			case '-':
				//do nothing
			break;

			case Tag.EOF:
				//do nothing
			break;

			case ')':
				//do nothing
			break;

            default:
				error("syntax error");
       }
   }

	private void fact() {
       if(look.tag == '('){
         move();
         expr();
         if(look.tag == ')')
           move();
         else error ("syntax error");
       } else if(look.tag == Tag.NUM){
           move();
           code.emit(OpCode.ldc, NumberTok.numero);
       } else error("syntax error");

   }

   public static void main(String[] args) {
       Lexer lex = new Lexer();
       String path = "C:/LFT_Lab/Es5.1/Prova"; // il percorso del file da leggere
       try {
           BufferedReader br = new BufferedReader(new FileReader(path));
           ExpressionTranslator expression = new ExpressionTranslator(lex, br);
           expression.prog();
           br.close();
       } catch (IOException e) {e.printStackTrace();}
   }
}
