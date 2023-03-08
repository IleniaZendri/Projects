public class Esercizio8{
  public static boolean scan(String s){
		int state = 0;
		int i = 0;

		while(state >= 0 && i < s.length()){
			final char ch = s.charAt(i++);
			int value = (int)ch;

			switch(state){

        case 0:
          if(ch == 'a' || ch == '*' )
            state = 0;
          else if(ch == '/') // codifica ASCII per /
            state = 1;
          else
            state = -1;
          //System.out.println("case 0 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
          break;

        case 1:
          if(ch == '*') //codifica ASCII per *
            state = 2;
          else if (ch == '/')
            state = 1;
          else if(ch == 'a')
            state = 0;
          else
            state = -1;
          //System.out.println("case 1 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
          break;

        case 2:
          if(ch == '/' || ch == 'a')
            state = 2;
          else if(ch == '*')
            state = 3;
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
            state = -1;
          //System.out.println("case 3 : state = " + state + " ch = " + ch + " value = " + value + " i = " + i);
        break;

        case 4:
          if(ch == 'a' || ch == '*')
            state = 4;
          else if(ch == '/')
            state = 1;
          else
            state = -1;
          break;


      }
    }
    return state == 0 || state == 1 || state == 4;
  }
  public static void main(String[] args){
    //“aaa/****/aa”, “aa/*a*a*/”, “aaaa”, “/****/”, “/*aa*/”, “*/a”, “a/**/***a”, “a/**/***/a” e “a/**/aa/***/a”, ma non “aaa/*/aa” “aa/*aa”
    System.out.println(scan("aaa/****/aa") ? "OK" : "NOPE");
    System.out.println(scan("aa/*a*a*/") ? "OK" : "NOPE");
    System.out.println(scan("aaaa") ? "OK" : "NOPE"); //
    System.out.println(scan("/****/") ? "OK" : "NOPE");
    System.out.println(scan("/*aa*/") ? "OK" : "NOPE");
    System.out.println(scan("*/a") ? "OK" : "NOPE"); //
    System.out.println(scan("a/**/***a") ? "OK" : "NOPE");
    System.out.println(scan("a/**/***/a") ? "OK" : "NOPE");
    System.out.println(scan("a/**/aa/***/a") ? "OK" : "NOPE");

    System.out.println(scan("aaa/*/aa") ? "OK" : "NOPE");
    System.out.println(scan("aa/*aa") ? "OK" : "NOPE");
	}

}
