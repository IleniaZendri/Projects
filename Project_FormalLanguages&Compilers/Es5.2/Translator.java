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
	    if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN) {
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
        }
        else {
            error("Error in variable prog");
        }
    }

    public void stat(int lnext) {
        switch(look.tag) {
            case Tag.ID:
                // Address dell'identificatore
                int id_id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_id_addr == -1) {
                    id_id_addr = count;
                    st.insert(((Word)look).lexeme, count++);
                }

                move();
                if (look.tag == '=') {
                    move();
                    expr();
                    code.emit(OpCode.istore, id_id_addr);
                }
                else
                    error("Error in grammar (stat)");
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
                    if (read_id_addr == -1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore, read_id_addr);
                }
                else
                    error("Error in grammar (stat) after read(");
                break;

            case Tag.IF:
                move();

                int b_true = code.newLabel();
                int b_false = code.newLabel();
                b_expr(b_true, b_false);
                code.emitLabel(b_true);

                if (look.tag == Tag.THEN) {
                    move();
                    stat(lnext);

                    if (look.tag == Tag.ELSE) {
                        code.emit(OpCode.GOto, lnext);
                        move();

                        code.emitLabel(b_false);
                        stat(lnext);
                    }
					else {
                        code.emitLabel(b_false);
                    }
                }
                else {
                    error("Error in variable stat");
                }
                break;


            case Tag.FOR:
                move();
                match('(');
                if (look.tag == Tag.ID) {
                    int for_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (for_id_addr == -1) {
                        for_id_addr = count;
                        st.insert(((Word)look).lexeme, count++);
                    }

                    // Inizializzazione variabile di ciclo
                    match(Tag.ID);
                    match('=');
                    expr();
                    code.emit(OpCode.istore, for_id_addr);
                    match(';');


                    // Condizione di ciclo
                    int ltrue = code.newLabel();
                    int lfalse = lnext;
                    int lcondition = code.newLabel();
                    int lincrement = code.newLabel();


                    code.emitLabel(lcondition);
                    b_expr(ltrue, lfalse);
                    code.emitLabel(ltrue);
                    match(')');

                    // Corpo del ciclo
                    if (look.tag == Tag.DO) {
                        match(Tag.DO);
                        stat(lincrement);

                        // Incremento variabile di ciclo
                        code.emitLabel(lincrement);
                        code.emit(OpCode.iload, for_id_addr);
                        code.emit(OpCode.ldc, 1);
                        code.emit(OpCode.iadd);
                        code.emit(OpCode.istore, for_id_addr);

                        // Ritorna alla condizione di ciclo
                        code.emit(OpCode.GOto, lcondition);
                    }
                    else
                        error("Error in grammar (stat) after for(ID=<expr>) with " + look);
                }
                else
                    error("Error in grammar (stat) after for");
                break;

			case Tag.BEGIN:
				move();
				int statlist_next = code.newLabel();
				statlist(statlist_next);
				code.emitLabel(statlist_next);
				match(Tag.END);
				break;

			default:
				error("Error in variable stat");
				break;
		}
    }


    private void b_expr(int ltrue, int lfalse) {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
    		expr();

            OpCode rel_operator = null;

    		if (look == Word.eq)
    			rel_operator = OpCode.if_icmpeq;
            else if (look == Word.lt)
                rel_operator = OpCode.if_icmplt;
            else if (look == Word.le)
                rel_operator = OpCode.if_icmple;
            else if (look == Word.gt)
                rel_operator = OpCode.if_icmpgt;
            else if (look == Word.ge)
                rel_operator = OpCode.if_icmpge;
            else if (look == Word.ne)
                rel_operator = OpCode.if_icmpne;
            else
                error("Error in variable b_expr: unknow relational operator.");

            match(Tag.RELOP);
            expr();
            code.emit(rel_operator, ltrue);
            code.emit(OpCode.GOto, lfalse);
        }
        else {
            error("Error in variable bexpr" + look.tag);
        }
    }


    private void statlist(int lnext) {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag.IF || look.tag == Tag.FOR || look.tag == Tag.BEGIN) {
            int stat_next = code.newLabel();
            stat(stat_next);
            code.emitLabel(stat_next);

            int statlistp_next = code.newLabel();
            statlistp(statlistp_next);
        }
        else {
            error("Error in variable statlist");
        }
    }

    private void statlistp(int lnext) {
        if (look.tag == ';') {
            move();
            stat(lnext);
            code.emitLabel(lnext);


            int statlistp_next = code.newLabel();
            statlistp(statlistp_next);
        }
        else if (look.tag == Tag.EOF || look.tag == Tag.END) {
            // do_nothing
        }
        else {
            error("Error in variable statlistp");
        }
    }

    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            term();
            exprp();
        }
        else {
            error("Error in variable expr");
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

            case Tag.ELSE:
            case ';':
            case ')':
            case Tag.EOF:
            case Tag.END:
            case Tag.THEN:
            case Tag.RELOP:
                // do_nothing
                break;

            default:
                error("Error in variable exprp");
                break;
        }
   }

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM  || look.tag == Tag.ID) {
            fact();
            termp();
        }
        else {
            error("Error in variable term");
        }
    }

    private void termp() {
        switch (look.tag) {
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
        case ')':
        case '-':
        case Tag.EOF:
        case ';':
        case Tag.RELOP:
        case Tag.ELSE:
        case Tag.END:
        case Tag.THEN:
            // do_nothing
            break;

        default:
            error("Error in variable termp");
            break;
        }
    }

    private void fact() {
        if (look.tag == '(') {
            move();
            expr();
            if (look.tag == ')')
                match(')');
            else
                error("Errore in variable fact");
        }
        else if (look.tag == Tag.NUM){
            code.emit(OpCode.ldc, ((NumberTok)look).numero);
            match(Tag.NUM);
        }
        else if (look.tag == Tag.ID) {
            int fact_id_addr = st.lookupAddress(((Word)look).lexeme);
            if (fact_id_addr != -1) {
                code.emit(OpCode.iload, fact_id_addr);
            }
            else {
                error("Error: variable " + ((Word)look).lexeme + " has not been declared");
            }

            match(Tag.ID);
        }
        else {
            error("Error in variable fact -> character: " + look.tag);
        }
    }

   public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/LFT_Lab/Es5.2/Prova"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
