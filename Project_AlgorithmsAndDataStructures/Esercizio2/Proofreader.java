package editdistance;

/**
*
* @author Murazzano Luca, Zendri Ilenia
*/

import java.io.*;
import java.util.StringTokenizer;


class Proofreader {

	static int capacity = 20;
	static int size;
	static String[] wordList = new String[capacity];
	
	//It takes the worlds from the file correcte.txt
	public static void main(String args[]) throws IOException{
		FileReader f;
		f = new FileReader("correctme.txt");

		BufferedReader b;
		b = new BufferedReader(f);

		String s;
		s = b.readLine();

		StringTokenizer multiTokenizer = new StringTokenizer(s, " .,:");
		while (multiTokenizer.hasMoreTokens())
		{
			spellChecker(multiTokenizer.nextToken().toLowerCase());
		}

		b.close();
	}

	//It compares the words taken from correctme.txt to the words
	//taken from dictionary.txt. If a word is wrong, it show the correct
	//word using the method print.
	public static void spellChecker(String str1) throws IOException{
		int minValue;
		size = 0;
		capacity = 20;
		
		FileReader g;
		g = new FileReader("dictionary.txt");

		BufferedReader a;
		a = new BufferedReader(g);

		System.out.println(str1);

		String str2;
		str2 = a.readLine();
		int min = minValue = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		str2 = a.readLine();
		while(str2 != null && min != 0){
			minValue = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
			if(minValue < min){
				min = minValue;
				deleteList();
				addWord(str2);
			}else if(minValue == min){
				addWord(str2);
			}
			str2 = a.readLine();
		}

		if(min != 0){
			printList();
		}
		a.close();
	}

	//It prints the list of the correct words
	public static void printList(){
		int i;
		System.out.println('\n' + "advices:");
		for(i = 0; i < size; i++){
			System.out.println(wordList[i]);
		}
		System.out.println('\n');
	}

	//It deletes a list
	public static void deleteList(){
		int i = 0;
		while(i < size){
			wordList[i] = null;
			i++;
		}
		size=0;
	}

	//It adds a word in a list
	public static void addWord(String word){
		if(size == capacity){
			int i;
			capacity = capacity * 2;
			String[] newWordList = new String[capacity];
			for(i = 0; i < size; i++){
				newWordList[i]=wordList[i];
			}
			wordList = newWordList;
			wordList[size] = word;
			size++;
		}else{
			wordList[size] = word;
			size++;
		}
	}

}