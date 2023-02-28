package unionfind;

import org.junit.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionFindTest{
    
	@Test
    public void test(){
        UnionFind<Integer> prova=new UnionFind<Integer>();

        prova.add(0);
        prova.add(1);
        prova.add(2);
        prova.add(3);
        prova.add(4);

        prova.union(3,0);
        int n=prova.find(0);
        System.out.println(n);
        
        prova.union(0, 1);
        int m=prova.find(1);
        System.out.println(m);
        
        prova.union(4, 3);
        int p=prova.find(3);
        System.out.println(p);
        
        n=prova.find(1);
        System.out.println(n);
	}

}