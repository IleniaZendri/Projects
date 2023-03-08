public class NumberTok extends Token {
    public static int numero = 0;
    public NumberTok(int tag, int n) { super(tag); numero = n; }
    public String toString() { return "<" + tag + ", " + numero + ">"; }
	}
