package src.main;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

class GraphDB {
    public static void main(String[] args) throws IOException, Exception{
        Node n1 = new Node();
        int[] x= {1,2};
        n1.create("hello",x);
        n1.write();
        Node n2 = new Node();
        
        int[] y= {1,2,3,4};
        n2.create("Boby", y);
        n2.write();

        Node n_new = new Node();
        n_new.create_from_read(1);
        n_new.print();
    }
}