import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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

    public void start() {
  	if(look.tag == '(' || look.tag == Tag.NUM){
  	   expr();
  	   match(Tag.EOF);
    } else error("syntax error");
	  }

    private void expr() {
  	  if(look.tag == '(' || look.tag == Tag.NUM){
        term();
        exprp();
      } else error("syntax error");
    }

    private void exprp() {
  	switch (look.tag) {
  		case '+':
  			move(); 
  			term();
  			exprp();
        break;
  		case '-':
			move();
			term();
			exprp();
        break;
		case Tag.EOF:
			// do nothing
        break;
		case ')':
			// do nothing
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
              move();
              fact();
              termp();
              break;

            case '/':
              move();
              fact();
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
          } else error("syntax error");
      }

      public static void main(String[] args) {
          Lexer lex = new Lexer();
          String path = "C:/LFT_Lab/Es3.1/Prova.txt"; // il percorso del file da leggere
          try {
              BufferedReader br = new BufferedReader(new FileReader(path));
              Parser parser = new Parser(lex, br);
              parser.start();
              System.out.println("Input OK");
              br.close();
          } catch (IOException e) {e.printStackTrace();}
      }
  }
