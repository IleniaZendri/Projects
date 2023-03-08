public class Esercizio4{
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
						} else if(ch == ' ')
							state = 0;
						else
							state = -1;
						System.out.println("case 0 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
						break;

					case 1:
						if(Character.isDigit(ch)){
							if(value % 2 == 0)
									state = 1;
							else if(value % 2 != 0)
									state = 2;
							} else if(ch >= 'A' && ch <= 'K')
								state = 5;
							else if (ch == ' ')
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
							} else if(ch >= 'L' && ch <= 'Z')
							state = 5;
							else if(ch == ' ')
								state = 4;
							else
								state = -1;
						System.out.println("case 2 : state = " + state + " ch = " + ch + " value = " + value);
						break;

						/* Nei casi pari controllo che la parte di stringa dei caratteri inizi con lettere comprese tra A-K || a-k*/

					case 3:
						if(ch >= 'A' && ch <= 'K')
							state = 5;
						else if (ch == ' ')
							state = 3;
						else
							state = -1;
						System.out.println("case 3 : state = " + state + " ch = " + ch + " value = " + value);
						break;

					case 4:
						if(ch >= 'L' && ch <= 'Z')
							state = 5;
						else if(ch == ' ')
							state = 4;
						else
							state = -1;
						System.out.println("case 4 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
						break;

					case 5:
						if(ch == ' ')
							state = 6;
						else if(ch >= 'a' && ch <= 'z')
							state = 5;
						else
							state = -1;
						System.out.println("case 5 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
						break;

					case 6:
						if(ch == ' ')
							state = 6;
						else if(ch >= 'A' && ch <= 'Z')
							state = 5;
						else
							state = -1;
						System.out.println("case 6 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
						break;

				}
			}
		return state == 5 || state == 6;
	}

	public static void main(String[] args){
		System.out.println(scan(" 123456 Bianchi ") ? "OK" : "NOPE");
		System.out.println(scan("654321 Rossi") ? "OK" : "NOPE");
		System.out.println(scan("123456De Gasperi") ? "OK" : "NOPE");

		System.out.println(scan("123456Bian chi") ? "OK" : "NOPE");
		System.out.println(scan("1234 56Bianchi") ? "OK" : "NOPE");
	}
}
