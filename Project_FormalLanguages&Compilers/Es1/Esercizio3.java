public class Esercizio3{
	public static boolean scan(String s){
		int state = 0;
		int i = 0;

		while(state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);
			int value = (int)ch;

			switch(state){

				case 0:
					if(Character.isDigit(ch)){
						if(value % 2 == 0)
							state = 1;
						else
							state = 2;
					} else
						state = -1;
					System.out.println("case 0 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

				case 1:
					if(Character.isDigit(ch)){
						if(value % 2 == 0)
								state = 1;
						else if (value % 2 != 0)
								state = 2;
					} else if(ch >= 'A' && ch <= 'K' || ch >= 'a' && ch <= 'k' )
						state = 3;
					else
						state = -1;
					System.out.println("case 1 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

				case 2:
					if(Character.isDigit(ch)){
						if(value % 2 != 0)
							state = 2;
						else if (value % 2 == 0)
							state = 1;
					} else if(ch >= 'L' && ch <= 'Z' || ch >= 'l' && ch <= 'z')
						state = 3;
					else
						state = -1;
					System.out.println("case 2 : state = " + state + " ch = " + ch + " value = " + value);
					break;

					/* Nei casi pari controllo che la parte di stringa dei caratteri inizi con lettere comprese tra A-K || a-k (state = 5)*/

				case 3:
					if(Character.isLetter(ch))
						state = 3;
					else
						state = -1;
					System.out.println("case 3 : state = " + state + " ch = " + ch + " value = " + value);
					break;

			}
		}
		return state == 3;
	}

	public static void main(String[] args){
		System.out.println(scan("123456Bianchi") ? "OK" : "NOPE");
		System.out.println(scan("654321Rossi") ? "OK" : "NOPE");
		System.out.println(scan("654321Bianchi") ? "OK" : "NOPE");
		System.out.println(scan("123456Rossi") ? "OK" : "NOPE");
	}
}
