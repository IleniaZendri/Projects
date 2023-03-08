public class Esercizio2{
	public static boolean scan(String s){
		int state = 0;
		int i = 0;
		
		while(state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);
			
			switch(state){
			case 0:
				if(ch == '_')
					state = 1;
				else if(ch != '_')
					state = 0;
				else 
					state = -1;  
				break;
				
			case 1:
				if(Character.isLetter(ch) || ch == '_')
					state = 2;
				else if (Character.isDigit(ch))
					state = 0;
				else
					state = -1;
				break;
				
			case 2:
				if(Character.isLetterOrDigit(ch))
					state = 3;
				else if(ch == '_')
					state = 2;
				else 
					state = -1;
				break;
				
			}
		}
		return state == 3;
	}
	
	public static void main(String[] args){
		System.out.println(scan("_eadw") ? "ok" : "no");
		System.out.println(scan("_e__adw") ? "ok" : "no");
		System.out.println(scan("_2eadw") ? "ok" : "no");
		System.out.println(scan("2eadw") ? "ok" : "no");
		System.out.println(scan("eadw") ? "ok" : "no");
		System.out.println(scan("_2e?ìì/*adw") ? "ok" : "no");
		/* Character.is---- non fa distinzione tra numeri e simboli, quindi non posso escludere questi ultimi */
		System.out.println(scan("_/?'+*eadw") ? "ok" : "no");
		System.out.println(scan("____") ? "ok" : "no");
		System.out.println(scan("_") ? "ok" : "no");
		
		
	}
}

