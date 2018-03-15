import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class Client extends Application implements Initializable {
    public TextField textinput;
    public Label error;
    public Button extra;
    public Button search;
    public Button display;
    public Button delete;
    public Button pricechange;
    public Button profit;
    public TextField changeValue;
    public TextField profitValue;
    public TableView<Parts> tableview;
    public TableColumn<Parts,String> partImage;
    public TableColumn<Parts,String> partName;
    public TableColumn<Parts,Double> partCPrice;
    public TableColumn<Parts,Double> partSPrice;
    public TableColumn<Parts,Integer> partQty;
    public static String ServerURL;
    public static ServerInterface remote;
    public ObservableList<Parts> products =FXCollections.observableArrayList();
    public static void main(String[] args ) throws UnknownHostException {
        if(args.length == 0){
            ServerURL = "rmi://" + "localhost" + "/AddServer";
        }
        else
            ServerURL = "rmi:/" + InetAddress.getByName(args[0]) + "/AddServer";


        System.out.println(ServerURL);
        launch(args);


    }
    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException, ClassNotFoundException {
        Stage window = primaryStage;
        window.setTitle("RMI");
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        Scene app = new Scene(root);
        window.getIcons().add(new Image("inventory.jpg"));
        window.setScene(app);
        window.show();

    }

    public void ViewItems() throws IOException, ClassNotFoundException {

        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(remote.viewItems()));

        ArrayList<Parts> List = (ArrayList<Parts>) iStream.readObject();
        products = FXCollections.observableArrayList();
        products.addAll(List);
        tableview.setItems(products);

    }

    public double getPrice(String partname) throws RemoteException {
        return remote.getPrice(partname);
    }

    public boolean deletePart(String partname) throws RemoteException {
        return remote.deletePart(partname);
    }

    public boolean changePrice(String partname,double price) throws RemoteException{
        return remote.changePrice(partname,price);
    }

    public double calculateProfit() throws RemoteException {
        return remote.totalProfit();
    }

    public static void extraFeature(Parts p) throws RemoteException { // Adds a new part to the parts array on the server
        String part = p.getName();
        double costprice = p.getCostPrice();
        double sellingprice = p.getSellingPrice();
        int quantity = p.getQuantity();
        remote.ExtraFeature(part,costprice,sellingprice,quantity);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            remote= (ServerInterface) Naming.lookup(ServerURL);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        tableview.setItems(products);

        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partCPrice.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        partSPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        partQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        display.setOnMouseClicked(e-> {
            try {
                Date startTime = new Date();
                ViewItems();
                Date endTime = new Date();
                writeFile("Command 1 takes: " + getDifference(startTime,endTime)+"ms");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        search.setOnMouseClicked(e->{
            String partname = textinput.getText();
            try {
                Date startTime = new Date();
                if(getPrice(partname) < 0){

                    error.setText("Part not found");
                }
                else{
                    Date endTime = new Date();
                    error.setText(capital(partname) +": $"+getPrice(partname));
                    writeFileT("Command 2 takes " +getDifference(startTime,endTime)+"ms \t");
                    writeFile("Price of " + capital(partname) + " is $"+getPrice(partname));

                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });


        delete.setOnMouseClicked(e->{
            String partname = textinput.getText();
            try {
                Date startTime = new Date();
                if(deletePart(partname) == false){
                    error.setText("Part not found");
                }
                else {
                    error.setText(capital(partname) + " deleted");
                    Date endTime = new Date();
                    writeFileT("Command 4 takes: " + getDifference(startTime,endTime) + "ms \t");
                    writeFile("Deleted: " + capital(partname));
                    ViewItems();
                }

            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        pricechange.setOnMouseClicked(e->{
            String partname = textinput.getText();
            String a = changeValue.getText();
            a =a.replaceFirst("\\$","");
            if(a.length()>0) {
                double val = Double.parseDouble(a);
                try {
                    Date startTime = new Date();
                    if (changePrice(partname, val) == false) {
                        error.setText("Part not found!");
                    } else {
                        Date endTime = new Date();
                        error.setText("");
                        error.setText("Price changed");
                        writeFileT("Command 3 takes: " + getDifference(startTime, endTime) + "ms \t");


                        writeFile(capital(partname) + " Price was changed to " + val);
                    }
                    ViewItems();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        profit.setOnMouseClicked(e->{
            try {
                Date startTime = new Date();
                double prof = calculateProfit();
                Date endTime = new Date();
                writeFileT("Command 5 takes: " + getDifference(startTime,endTime) + "ms \t");
                writeFile("Profit: $ " + String.valueOf(prof));
                profitValue.setText("Profit: $ " + String.valueOf(prof));
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });



        extra.setOnMouseClicked(e->{
            System.out.println("hi");
            try {
                ExtraFeature ext =new ExtraFeature();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void writeFile(String message) throws FileNotFoundException {
        FileOutputStream fos=new FileOutputStream(new File("output.out"),true);
        try {
            fos.write(message.getBytes());
            fos.write('\n');
            fos.write('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFileT(String message) throws FileNotFoundException {
        FileOutputStream fos=new FileOutputStream(new File("output.out"),true);
        try {
            fos.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDifference(Date startTime,Date endTime){

        long diff = endTime.getTime() - startTime.getTime();
        long milisec = TimeUnit.MILLISECONDS.toMillis(diff);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        return milisec;
    }

    public String capital(String input){
        return (input.substring(0, 1).toUpperCase() + input.substring(1));
    }


    }


