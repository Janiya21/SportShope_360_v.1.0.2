package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainRootController implements Initializable {

    public AnchorPane adminRoot;

    public JFXButton goBackBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL resource = getClass().getResource("../view/DailyIncomeView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }


    @FXML
    void showReportOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/DailyIncomeView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }

    @FXML
    public void ItemManageOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/AdminItemManageView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }

    public void goBackOnAction(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) goBackBtn.getScene().getWindow();
        primaryStage.close();

        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void ReservationNoteOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/ReservationsManagerView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }

    public void AgentInfoOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/AgentDetailForm.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }

    @FXML
    public void showStockOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/StockView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adminRoot.getChildren().clear();
        adminRoot.getChildren().add(load);
    }
}
