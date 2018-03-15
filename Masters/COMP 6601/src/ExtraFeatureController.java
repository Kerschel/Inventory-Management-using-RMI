import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ExtraFeatureController implements Initializable{
    public TextField addName;
    public TextField addSPrice;
    public TextField addCPrice;
    public TextField addQty;
    public Button Submit;
    public Label added;
    public static Parts p;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Submit.setOnMouseClicked(e->{
        int qty = Integer.parseInt(addQty.getText());
        double cprice = Double.parseDouble(addCPrice.getText());
        double sprice = Double.parseDouble(addSPrice.getText());
        p =new Parts(addName.getText(),cprice,sprice,qty);
            try {
                Client.extraFeature(p);
                added.setText("Part Added to Server");
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
    }

}
