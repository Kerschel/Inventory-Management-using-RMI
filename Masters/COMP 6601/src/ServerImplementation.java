import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface,Serializable {

    public ServerImplementation() throws RemoteException{
    }


    @Override
    public double getPrice(String part)  throws RemoteException{
        Server.items.indexOf(part);
        for(int i =0;i<Server.items.size();i++){
            if(Server.items.get(i).getName().toLowerCase().equals(part.toLowerCase())){
                return Server.items.get(i).getSellingPrice();
            }
        }
        return -1;
    }



    @Override
    public byte[] viewItems() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(new ArrayList<Parts>(Server.items));
        oo.close();
        return bStream.toByteArray();
    }


    @Override
    public boolean changePrice(String part, double price)  throws RemoteException{
        for(int i =0;i<Server.items.size();i++){
            if(Server.items.get(i).getName().toLowerCase().equals(part.toLowerCase())){
                Server.items.get(i).setSellingPrice(price);
                return true;
            }
        }
        return false;
    }

    @Override public boolean deletePart(String part)  throws RemoteException{
        for(int i =0;i<Server.items.size();i++){
            if(Server.items.get(i).getName().toLowerCase().equals(part.toLowerCase())){
                Server.items.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ExtraFeature(String part, double cprice, double sprice, int quantity) throws RemoteException {
        Server.items.add(new Parts(part,cprice,sprice,quantity));
        return true;
    }

    @Override
    public double totalProfit()  throws RemoteException{
        double profit = 0;
        for(int i =0;i<Server.items.size();i++){
           profit += (Server.items.get(i).getSellingPrice() - Server.items.get(i).getCostPrice()) * Server.items.get(i).getQuantity();
        }
        return profit;
    }

}
