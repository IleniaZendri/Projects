public class Esercizio7{
  public static boolean scan(String s){
		int state = 0;
		int i = 0;

		while(state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);
			int value = (int)ch;

			switch(state){

        case 0:
          if(ch == '/')
            state = 1;
          else
            state = -1;
          //System.out.println("case 0 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
          break;

        case 1:
          if(ch == '*')
            state = 2;
          else
            state = -1;
          //System.out.println("case 1 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
          break;

        case 2:
          if(ch== '*')
            state = 3;
          else if(ch == 'a' || ch == '/')
            state = 2;
          else
            state = -1;
          //System.out.println("case 2 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
          break;

        case 3:
          if(ch == '*')
            state = 3;
          else if(ch == '/')
            state = 4;
          else if(ch == 'a')
            state = 2;
          else
            state =-1;
          //System.out.println("case 3 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
        break;

        case 4:
          state = -1;
        break;

      }
    }
    return state == 4;
  }
  public static void main(String[] args){
    //  /****/”, “/*a*a*/”, “/*a/**/”, “/**a///a/a**/”, “/**/” e “/*/*/”
    System.out.println(scan("/****/") ? "OK" : "NOPE");
    System.out.println(scan("/*a*a*/") ? "OK" : "NOPE");
    System.out.println(scan("/*a/**/") ? "OK" : "NOPE");
    System.out.println(scan("/**a///a/a**/") ? "OK" : "NOPE");
    System.out.println(scan("/**/") ? "OK" : "NOPE");
    System.out.println(scan("/*/*/") ? "OK" : "NOPE");

    System.out.println(scan("/*/") ? "OK" : "NOPE");
    System.out.println(scan("/**/***/") ? "OK" : "NOPE");
	}

}
