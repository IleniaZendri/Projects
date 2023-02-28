public class TestUnionFind{
    
    public static void main(String[] args){
        UnionFind<Integer> prova=new UnionFind<Integer>();

        prova.add(0);
        prova.add(1);
        prova.add(2);
        prova.add(3);
        prova.add(4);

        prova.union(3,0);

        int n=prova.find(0);
        System.out.println(n);
    }

}