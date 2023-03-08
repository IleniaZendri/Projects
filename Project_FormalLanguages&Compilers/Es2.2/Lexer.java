import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
				
			case '(':
				peek = ' ';
				return Token.lpt;
				
			case ')':
				peek = ' ';
				return Token.rpt;
				
			case '+':
				peek = ' ';
				return Token.plus;
				
			case '-':
				peek = ' ';
				return Token.minus;
				
			case '*':
				peek = ' ';
				return Token.mult;
				
			case '/':															//Nell'es 2.1 avevo solo    case '/';
				readch(br);														//								peek = ' ';
				if (peek == '/') {												//								return Token.semicolon;
					while(peek != '\n' && peek != (char)-1) {
						readch(br);
					}
					return lexical_scan(br);
				} else if (peek == '*') {
					boolean fine = false;
					while (peek != (char)-1 && !fine) {
						readch(br);
						if (peek == '*') {
							readch(br);
							if (peek == '/') 
								fine = true;
						} 
						if (peek == '\n')
							line++;		
					}
					if (peek == (char)-1) {
						System.out.println("Erroneous string ");
						return null;
					}
					return lexical_scan(br);
				} else return Token.div;
				
				
				
			case ';':
				peek = ' ';
				return Token.semicolon;
			
			
	// ... gestire i casi di (, ), +, -, *, /, ; ... //

				case '&':
                readch(br);
                if (peek == '&') {
					peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
				
				case '|':
                readch(br);
                if (peek == '|'){
					peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
				
				case '<':
                readch(br);
                if (peek == '>') {
					peek = ' '; //caso <>
                } else if (peek == '='){
                    return Word.le; //caso <=
				} else {
					return Word.lt; //caso <
                }
				
				case '>':
                readch(br);
                if (peek == '=') {
					peek = ' ';
                    return Word.ge; //caso >=
                } else {
					return Word.gt; //caso >
                }
				
				case '=':
                readch(br);
                if (peek == '=') {
					peek = ' ';
                    return Word.eq;
                } else {
				   return Token.assign;
                }

	// ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {

	// ... gestire il caso degli identificatori e delle parole chiave // 
			
					String s = "";
					boolean flag = false;
					
					while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
						s += peek;
						if (Character.isLetter(peek) || Character.isDigit(peek)) {
							flag = true;
						} 
						readch(br);	
					}
					
					if (!flag) {
						System.out.println("Erroneous string: " + s);
						return null;
					}
					
					if(s.equals("if")) {
						return Word.iftok;
					} else if(s.equals("then")) {
						return Word.then;
					} else if(s.equals("else")) {
						return Word.elsetok;
					} else if(s.equals("for")) {
						return Word.fortok;
					} else if(s.equals("do")) {
						return Word.dotok;
					} else if(s.equals("print")) {
						return Word.print;
					} else if(s.equals("read")) {
						return Word.read;
					} else if(s.equals("begin")) {
						return Word.begin;
					} else if(s.equals("end")) {
						return Word.end;
					} else {
						return new Word(Tag.ID, s);
					}
						
						
				} else if (Character.isDigit(peek)) {
					String s = "";
					while(Character.isDigit(peek) || Character.isLetter(peek)){
						if(Character.isLetter(peek)){
							System.out.println("Erroneous string: "+s+peek);
							return null;
						}
						s+= peek;
						readch(br);
					}
					if(s.equals("0")){
						return new NumberTok(Tag.NUM, 0);
					} else if (s.length() > 1 && s.substring(0,1).equals("0")) {
						System.out.println("Erroneous string: " + s);
						return null;
					} else {
						return new NumberTok(Tag.NUM, Integer.parseInt(s));
					}
	// ... gestire il caso dei numeri ... //

                } else {
                        System.err.println("Erroneous character: " 
                                + peek);
                        return null;
                }
         }
		 
}
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/LFT_Lab/Es2.2/Prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
}

