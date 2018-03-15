import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static  ArrayList<Parts> items = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException, RemoteException, MalformedURLException {

        FileInputStream myStream = new FileInputStream("input.dat");
        Scanner in = new Scanner(myStream);

        while(in.hasNext()){
            String name = in.next();
            double costprice =  in.nextDouble();
            double sellingprice =  in.nextDouble();
            int qtysold =  in.nextInt();
            items.add(new Parts(name,costprice,sellingprice,qtysold));
        }

        System.out.println("RMI up and running");
        ServerImplementation Servers = new ServerImplementation();
        Naming.rebind("AddServer", Servers);


    }


}