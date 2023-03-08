import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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
		int lnext_prog = code.newLabel();
		statlist(lnext_prog);
		code.emitLabel(lnext_prog);
		match(Tag.EOF);
		try {
			code.toJasmin();
		}
		catch(java.io.IOException e) {
			System.out.println("IO error\n");
		};
	// ... completare ...
    }

    public void stat(int lnext) {
        switch(look.tag) {
	// ... completare ...
			case Tag.ID:
				int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
				match(Tag.ID);
				if (look.tag == '='){
					match('=');
		            expr();
					code.emit(OpCode.istore, id_addr);
				}
				else{
					error("Error in stat, missing \'=\' token");
				}
				break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                expr();
                code.emit(OpCode.invokestatic,1);
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (read_id_addr==-1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,read_id_addr);
                }
                else
                    error("Error in grammar (stat) after read( with " + look);
                break;
			case Tag.IF:
				match(Tag.IF);
				int l_true=code.newLabel(),l_false=code.newLabel();
				b_expr(l_true,l_false);
				code.emitLabel(l_true);
				match(Tag.THEN);
				stat(lnext);
				if( look.tag == Tag.ELSE )
					statelse(lnext,l_false);
				else
					code.emitLabel(l_false);
				break;
			case Tag.FOR:
				int cond_true=code.newLabel(),cond_false=lnext,statnext=code.newLabel(),next=code.newLabel();
				code.emitLabel(statnext);
				match(Tag.FOR);
				match('(');
				if (look.tag==Tag.ID) {
					int forid_addr = st.lookupAddress(((Word)look).lexeme);
						if (forid_addr==-1) {
							forid_addr = count;
							st.insert(((Word)look).lexeme,count++);
						}

				match(Tag.ID);
				if (look.tag == '='){
					match('=');
					expr();
					code.emit(OpCode.istore, forid_addr);
					match(';');
					code.emitLabel(statnext);
					b_expr(cond_true,cond_false);
					match(')');
					code.emitLabel(cond_true);
					match(Tag.DO);
					stat(next);
					code.emitLabel(next);
					code.emit(OpCode.ldc,1);
					code.emit(OpCode.iload,forid_addr);
					code.emit(OpCode.iadd);
					code.emit(OpCode.istore,forid_addr);
					code.emit(OpCode.GOto,statnext);
				}
				}
				else
					error("Error initializing or declaring variable in for");
				break;
			 case Tag.BEGIN :
				match(Tag.BEGIN);
				statlist(lnext);
				match(Tag.END);
				break;
			default:
				error("Error in stat");
				break;



	// ... completare ...
		}
	}
	private void statlist(int lnext){
		int lnext_stat;
		lnext_stat=code.newLabel();
		stat(lnext_stat);
		code.emitLabel(lnext_stat);
		statlistp(lnext);

	}

	private void statlistp(int lnext){
		if( look.tag == ';' ){
			match(';');
			int lnext_stat;
			lnext_stat=code.newLabel();
			stat(lnext_stat);
			code.emitLabel(lnext_stat);
			statlistp(lnext);
		}
		else if( look.tag == Tag.EOF || look.tag == Tag.END || look.tag == Tag.ELSE){}
		else{
			error("Error in statlistp");
		}
	}
    // ... completare ...
	private void statelse(int lnext, int lfalse){
		if( look.tag == Tag.ELSE ){
			match(Tag.ELSE);
			code.emit(OpCode.GOto,lnext);
			code.emitLabel(lfalse);
			stat(lnext);
		}
		else if( look.tag == Tag.EOF || look.tag == Tag.END || look.tag == ';' ){}
		else
			error("Error in statelse");
	}
    private void b_expr(int ltrue, int lfalse) {
	// ... completare ...
		if( look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID ){
			expr();
			if (look == Word.eq) {
				match(Tag.RELOP);
				expr();
				code.emit(OpCode.if_icmpeq,ltrue);
				code.emit(OpCode.GOto,lfalse);
			// ... completare ...
			}else if (look == Word.le) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmple,ltrue);
                code.emit(OpCode.GOto,lfalse);
          }else if (look == Word.lt) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmplt,ltrue);
                code.emit(OpCode.GOto,lfalse);
          }else if (look == Word.ne) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpne,ltrue);
                code.emit(OpCode.GOto,lfalse);
          }else if (look == Word.ge) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpge,ltrue);
                code.emit(OpCode.GOto,lfalse);
          }else if (look == Word.gt) {
                match(Tag.RELOP);
                expr();
                code.emit(OpCode.if_icmpgt,ltrue);
                code.emit(OpCode.GOto,lfalse);
          }
		  else
			  error("Error: no such Relop ");
		}
		else{
			error("Error in b_expr");
		}
	// ... completare ...
    }

    // ... completare ...
	private void expr(){
		if( look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID ){
			term();
			exprp();
		}
		else{
		error("Error in expr: missing ( or a number\n ");
		}
	}

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
			case ')' :
            case ';' :
            case Tag.RELOP :
            case Tag.THEN :
            case Tag.EOF:
            case Tag.ELSE:
            case Tag.END :
                break;
			default:
				error("Error in exprp: missing operators +/- ");

	// ... completare ...
     }
   }
   private void term(){
	   if( look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID ){
			fact();
			termp();
		}
		else{
			error("Error in term: missing ( or a number");
		}
   }
   private void termp(){
	   switch(look.tag) {
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
			case ')':
			case '+':
			case '-':
			case Tag.RELOP:
			case Tag.EOF :
			case Tag.END :
			case Tag.ELSE:
			case Tag.THEN:
			case ';':
				break;
			default:
				error("Error in termp: missing operators *,/");
	   }
   }
   private void fact(){
	   if( look.tag == '(' ){
			match('(');
			expr();
			if( look.tag == ')' ){
				match(')');
			}
			else{
				error("Error in fact: missing ) bracket");
			}
		}
		else if( look.tag == Tag.NUM ){
			NumberTok tmp = (NumberTok) look;
			code.emit(OpCode.ldc,tmp.numero);
			match(Tag.NUM);
		}
		else if ( look.tag == Tag.ID ){
			int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        error("ERROR in fact, address not valid");
                    }
				code.emit(OpCode.iload, id_addr);
				match(Tag.ID);
		}
		else{
			error("Error in fact: missing (expr) or a number");
		}
   }


    // ... completare ...
	public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/LFT_Lab/Es5.2/Prova"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator trans = new Translator(lex, br);
            trans.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
