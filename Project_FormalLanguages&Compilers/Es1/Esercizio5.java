public class Esercizio5{
	public static boolean scan(String s){
		int state = 0;
		int i = 0;

		while(state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);
			int value = (int)ch;

			switch(state){

					/* Distinguo Stringhe che iniziano con numeri e caratteri, e escludo quelle che iniziano per numeri */

				case 0:
					if(Character.isLetter(ch)){
						if(ch >= 'A' && ch <= 'K')
							state = 1;
						else
							state = 2;
					}
					else
						state = -1;
					System.out.println("case 0 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

					/* Divido a seconda dei gruppi di lettere, A-K e L-Z */

				case 1:
					if(Character.isLetter(ch)) //A-K || a-k
						state = 1;
					else if(Character.isDigit(ch)){
						if(value % 2 == 0)
							state = 3;
						else
							state = 1;
					} else
						state = -1;

					System.out.println("case 1 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

					/* Controllo  che i cognomi che iniziano per A-K siano seguiti da un numero pari */

				case 2:
					if(Character.isLetter(ch))
						state = 2;
					else if(Character.isDigit(ch)){
						if(value % 2 != 0)
							state = 4;
						else
							state = 2;
					} else
						state = -1;

					System.out.println("case 2 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

					/* Controllo  che i cognomi che iniziano per L-Z siano seguiti da un numero dispari */

				case 3:
					if(Character.isDigit(ch)){
						if(value % 2 == 0)
							state = 3;
						else
							state = 1;
					}	else
						state = -1;
					System.out.println("case 3 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;

				case 4:
					if(Character.isDigit(ch)){
						if(value % 2 != 0)
							state = 4;
						else
							state = 2;
					} else
						state = -1;
					System.out.println("case 4 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
					break;
			}
		}
		return state == 4 || state == 3;
	}

	public static void main(String[] args){
		System.out.println(scan("Bianchi123456") ? "ok" : "nope");
		System.out.println(scan("Rossi654321") ? "ok" : "nope");
		System.out.println(scan("654321Bianchi") ? "ok" : "nope");
		System.out.println(scan("123456Rossi") ? "ok" : "nope");
	}
}
