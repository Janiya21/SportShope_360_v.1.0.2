package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderTypeConfirmationController implements Initializable {

    public JFXButton btnWarrantedItemRepair;
    public JFXButton btnItemRepair;
    public JFXButton btnMakeOrder;
    public JFXButton btnMakeReservationNote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnWarrantedItemRepair.setFocusTraversable(false);
        btnItemRepair.setFocusTraversable(false);
        btnMakeOrder.setFocusTraversable(false);
        btnMakeReservationNote.setFocusTraversable(false);
    }

    @FXML
    public void goToWarrantedItemOnAction(ActionEvent event) {
        loadUI("OrderDetailView");
    }

    @FXML
    public void goToItemRepairOnAction(ActionEvent event) {
        loadUI("RepairOrderForm");
    }

    @FXML
    public void goToCustomerOrderOnAction(ActionEvent event) {
        PlaceOrderFormController.previousWindow="Customer_Order";
        loadUI("PlaceOrderForm");
    }

    public void makeAReservationNoteOnAction(ActionEvent event) {

        Stage primaryStage = (Stage) btnWarrantedItemRepair.getScene().getWindow();
        primaryStage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/ReservationNoteView.fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assert root != null;
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void loadUI(String name){

        Stage primaryStage = (Stage) btnWarrantedItemRepair.getScene().getWindow();
        primaryStage.close();

        CustomerDetailsFormController.customerPane.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/"+name+".fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assert root != null;
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
