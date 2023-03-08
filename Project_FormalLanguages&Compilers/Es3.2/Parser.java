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

    public void prog() {
  	if(look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN){
  	   statlist();
  	   match(Tag.EOF);
    } else error("syntax error");
	  }

    private void stat(){
      switch (look.tag) {
        case Tag.ID:
          match(Tag.ID);
          match('=');
          expr();
          break;

        case Tag.PRINT:
          match(Tag.PRINT);
          match('(');
          expr();
          match(')');
          break;

        case Tag.READ:
          match(Tag.READ);
          match('(');
          match(Tag.ID);
          match(')');
          break;

        case Tag.IF:
          match(Tag.IF);
          bexpr();
          match(Tag.THEN);
          stat();
          if(look.tag == Tag.ELSE){
            match(Tag.ELSE);
            stat();
          }
          break;

        case Tag.FOR:
          match(Tag.FOR);
          match('(');
          match(Tag.ID);
          match('=');
          expr();
          match(';');
          bexpr();
          match(')');
          match(Tag.DO);
          stat();
          break;

        case Tag.BEGIN:
          match(Tag.BEGIN);
          statlist();
          match(Tag.END);
          break;

        default:
          error("syntax error");
      }
    }

    private void statlist(){
      if(look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN){
        stat();
        statlistp();
      } else error("syntax error");
    }

    private void statlistp(){
      if(look.tag == ';'){
        match(';');
        stat();
        statlistp();
      } else if(look.tag == Tag.EOF || look.tag == Tag.END){
        //do nothing
      } else error ("syntax error");
    }

    private void bexpr(){
      if(look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID){
        expr();
        match(Tag.RELOP);
        expr();
      } else error ("syntax error in bexpr: missing RELOP token");
    }

    private void expr() {
  	  if(look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID){
        term();
        exprp();
      } else error("syntax error");
    }

    private void exprp() {
  	switch (look.tag) {
  		case '+':
  			match('+');
  			term();
  			exprp();
        break;

      case '-':
        match('-');
        term();
        exprp();
        break;

      case Tag.ELSE:
        //do nothing
        break;

      case ';':
        //do nothing
        break;

      case Tag.EOF:
        // do nothing
        break;

      case Tag.END:
        //do nothing
        break;

      case ')':
        // do nothing
        break;

      case Tag.RELOP:
        //do nothing
        break;

      case Tag.THEN:
        //do nothing
        break;

      default:
        error("syntax error");
      }
    }

      private void term() {
        if(look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID){
          fact();
          termp();
        } else error("syntax error");
      }

      private void termp() {
          switch(look.tag){

            case '*':
              match('*');
              fact();
              termp();
              break;

            case '/':
              match('/');
              fact();
              termp();
              break;

            case '+':
              //do nothing
              break;

            case '-':
              //do nothing
              break;

            case Tag.ELSE:
              //do nothing
              break;

            case ';':
              //do nothing
              break;

            case Tag.EOF:
              //do nothing
              break;

            case Tag.END:
              //do nothing
              break;

            case ')':
              //do nothing
              break;

            case Tag.RELOP:
              //do nothing
              break;

            case Tag.THEN:
              //do nothing
              break;

            default:
              error("syntax error");
          }
      }

      private void fact() {
          if(look.tag == '('){
            match('(');
            expr();
            if(look.tag == ')')//posso togliere l'if e l'else, perchè se non trova ')' match darà comunque errore
              match(')');
            else
              error ("syntax error");
          } else if(look.tag == Tag.NUM){
              match(Tag.NUM);
          } else if(look.tag == Tag.ID){
              match(Tag.ID);
          } else error("syntax error");
      }

      public static void main(String[] args) {
          Lexer lex = new Lexer();
          String path = "C:/LFT_Lab/Es3.2/Prova"; // il percorso del file da leggere
          try {
              BufferedReader br = new BufferedReader(new FileReader(path));
              Parser parser = new Parser(lex, br);
              parser.prog();
              System.out.println("Input OK");
              br.close();
          } catch (IOException e) {e.printStackTrace();}
      }
  }
