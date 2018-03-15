import java.io.IOException;
import java.rmi.*;

public interface ServerInterface extends Remote {
    double getPrice(String part) throws RemoteException;

    byte[] viewItems() throws RemoteException, IOException;

    boolean changePrice(String part, double price) throws RemoteException;

    boolean deletePart(String part) throws RemoteException;

    boolean ExtraFeature(String part,double cprice,double sprice,int quantity) throws  RemoteException;

    double totalProfit() throws RemoteException;
}
	 