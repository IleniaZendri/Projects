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

	// ... gestire i casi di (, ), +, -, *, /, ; ... //

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

			case '/':  		/* Forse devo incrementare line */
				boolean comm = false;
				boolean aster = false;
				readch(br);
				if(peek == '*'){
					peek = ' ';
					while(peek != '*' || !comm){
						if(peek == '*')
							aster = true;
						readch(br);
						if(peek == '/' && aster){
							peek = ' ';
							comm = true;
							return lexical_scan(br);

							}
						else if(peek != '/' && aster){
							aster = false;
						}
						else if(peek == (char)-1){
							System.out.println("Err, commento non chiuso");
							return null;
							}
					}
				}else if(peek == '/'){
					while(peek != '\n' && peek != (char)-1){
						readch(br);
					}
				return lexical_scan(br);
				}
				else
					return Token.div;

			case ';':
				peek = ' ';
				return Token.semicolon;


/**************************************************************************************************************************************/

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }

	// ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //

			case '|':
				readch(br);
				if(peek == '|'){
					peek = ' ';
					return Word.or;
				} else{
					System.err.println("Erroneous character" + "after & : " + peek );
					return null;
				}

			case '<':
				readch(br);
				if(peek == '='){
					peek = ' ';
					return Word.le;
				} else if(peek == '>'){
					peek = ' ';
					return Word.ne;
				} else
					return Word.lt; // non metto peek = ' '; perchè sono gia andato avanti con readch(br) se ne mettessi peek = ' '; non leggeri il carattere succesivo


			case '>':
				readch(br);
				if(peek == '='){
					peek = ' ';
					return Word.ge;
				} else {
					return Word.gt;
				}

			case '=':
				readch(br);
				if(peek == '='){
					peek = ' ';
					return Word.eq;
				} else{
					return Token.assign;
				}

       /*****************************************************************/

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {

	// ... gestire il caso degli identificatori e delle parole chiave //

					String parola = "";

					while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'){
						parola += peek;
						readch(br);
					}

					boolean soloUnderscore = false;
					int i = 0;
					for(i = 0; i < parola.length() && soloUnderscore == false; i++){
						if(parola.charAt(i) != '_')
							soloUnderscore = true;
					}

					if(parola.equals("if"))
						return Word.iftok;
					else if(parola.equals("then"))
						return Word.then;
					else if(parola.equals("else"))
						return Word.elsetok;
					else if(parola.equals("for"))
						return Word.fortok;
					else if(parola.equals("do"))
						return Word.dotok;
					else if(parola.equals("print"))
						return Word.print;
					else if(parola.equals("read"))
						return Word.read;
					else if(parola.equals("begin"))
						return Word.begin;
					else if(parola.equals("end"))
						return Word.end; 
					else if(soloUnderscore == true)
						return new Word(Tag.ID, parola);
					else {
						System.err.println("Erroneous character: "
                                + peek );
                        return null;
                	}


				} else if (Character.isDigit(peek)) {

	// ... gestire il caso dei numeri ... //

					String number = "";

					while(Character.isDigit(peek)){
						number += peek;
						readch(br);
					}

					return new NumberTok(Tag.NUM, Integer.valueOf(number));


                } else {
                        System.err.println("Erroneous character: "
                                + peek );
                        return null;
                }
         }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:/LFT_Lab/Es4.1/Prova"; // il percorso del file da leggere
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
