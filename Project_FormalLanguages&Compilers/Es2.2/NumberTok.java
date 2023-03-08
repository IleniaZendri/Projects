public class NumberTok extends Token{
	public int intero = 0;
	public NumberTok(int tag, int s) { super(tag); intero = s; }
	public String toString() { return "<" + tag + "," + intero + ">"; }
	public static final NumberTok zero = new NumberTok(Tag.NUM, 0);
}