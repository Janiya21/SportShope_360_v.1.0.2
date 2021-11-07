package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.UserDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminLoginConfirmationController {

    public boolean loginCorrect=false;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        ArrayList<UserDetail> receptionDetails = new LoginController().getAllUsers("Admin");
        receptionDetails.forEach(e->{
            if(e.getUserId().equals(txtUserName.getText()) && e.getPassword().equals(txtPassword.getText())){
                loginCorrect=true;

                Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
                primaryStage.close();
            }
        });

        txtUserName.setStyle("-fx-text-inner-color: red;");
        txtPassword.setStyle("-fx-text-inner-color: red;");
    }
}
