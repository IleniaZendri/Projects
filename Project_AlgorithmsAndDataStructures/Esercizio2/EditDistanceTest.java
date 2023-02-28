package editdistance;

import static org.junit.Assert.*;

import org.junit.Test;

/**
*
* @author Murazzano, Zendri
*/

public class EditDistanceTest {

	//test: one string is not empty and the other is empty
	@Test
	public void testOneEmptyString1() {
		String str1 = "";
		String str2 = "ciao";

		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assert((tmp1 == str1.length()) || (tmp1 == str2.length()));
		assert((tmp2 == str2.length()) || (tmp2 == str2.length()));
		
		assertEquals(4, tmp1);
		assertEquals(4, tmp2);
	}
	
	
	//test: one string is not empty and the other is empty
		@Test
	public void testOneEmptyStrings2(){
		String str1 = "casca";
		String str2 = "";
			
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assert((tmp1 == str1.length()) || (tmp1 == str2.length()));
		assert((tmp2 == str1.length()) || (tmp2 == str2.length()));
		
		assertEquals(5, tmp1);
		assertEquals(5, tmp2);
	}
	
	//test: two empty strings 
	@Test
	public void testTwoEmptyString(){
		String str1 ="";
		String str2 ="";
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assert((tmp1 == str1.length()) && (tmp1 == str2.length()));
		assert((tmp2 == str1.length()) && (tmp2 == str2.length()));
		
		assertEquals(0, tmp1);
		assertEquals(0, tmp2);
		assert(tmp1 == tmp2);
	}
	
	
	//test: two strings with almost one common character
	@Test
	public void testEditDistance1(){
		String str1= "casa";
		String str2= "cassa";
		
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assertEquals(1, tmp1);
		assertEquals(1, tmp2);
		assert(tmp1 == tmp2);
	}
	
	
	//test: two strings with almost one common character 
	@Test
	public void testEditDistance2(){
		String str1= "casa";
		String str2= "cara";
		
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assertEquals(1, tmp1);
		assertEquals(1, tmp2);
		assert(tmp1 == tmp2);
	}
	
	
	//test: two strings whit almost one common character 
	@Test
	public void testEditDistance3(){
		String str1="tassa";
		String str2="passato";
		
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assertEquals(3, tmp1);
		assertEquals(3, tmp2);
		assert(tmp1 == tmp2);
	}
	
	
	//test: two equal strings
	@Test
	public void testEqualStrings(){
		String str1 ="pioppo";
		String str2 ="pioppo";
		
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());
		
		assertEquals(0, tmp1);
		assertEquals(0, tmp2);
		assert(tmp1 == tmp2);
	}
	
	
	//test: two different strings with any common character 
	@Test
	public void testTotalDifferentStrings(){
		String str1 = "mamma";
		String str2 = "nonno";
		int tmp1 = EditDistance.editDist(str1, str2, str1.length(), str2.length());
		int tmp2 = EditDistance.editDistDP(str1, str2, str1.length(), str2.length());

		assert((tmp1 == (str1.length())) || tmp1 == (str2.length()));
		assert((tmp2 == (str1.length())) || tmp2 == (str2.length()));
		
		assert(tmp1 == tmp2);
	}
	
	
	//test function min()
	@Test
	public void test_min_value(){
		int x = 2;
		int y = 5;
		int z = 100;
		int tmp = EditDistance.min(x, y, z);
		assertEquals(x, tmp);
	}
	
	
}
