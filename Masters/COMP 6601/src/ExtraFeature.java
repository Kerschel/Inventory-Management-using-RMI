import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class ExtraFeature {

    ExtraFeature() throws IOException {
        display();


    }


    public static void display() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Part");


        Parent root = FXMLLoader.load(ExtraFeature.class.getResource("AddPart.fxml"));
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();

    }

}
