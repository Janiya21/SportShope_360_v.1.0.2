package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.UserDetail;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginViewController implements Initializable {

    @FXML
    public JFXTextField txtPass;

    @FXML
    public JFXTextField txtUser;

    @FXML
    public JFXPasswordField txtConfirmPass;

    @FXML
    public JFXTextField txtUserName;

    @FXML
    public JFXComboBox<String> cmbAccount;

    @FXML
    public JFXPasswordField txtPassword;

    @FXML
    public JFXRadioButton adminRadBtn;

    @FXML
    public JFXRadioButton cashierRadBtn;

    @FXML
    public JFXTextField txtMail;

    @FXML
    public JFXTextField txtAddress;

    @FXML
    public Label lblUserId;

    @FXML
    private AnchorPane root;

    @FXML
    private VBox vb_content;

    @FXML
    private Button signBtn;

    @FXML
    private Button LoginBtn;

    public static int Number = 0;
    public static String userId=null;
    public static boolean mailAvailable = false;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();

    final Pattern all_REGEX = Pattern.compile("^[a-zA-Z0-9\\s]{2,32}$?");
    final Pattern phoneNo_regex = Pattern.compile("^[0-9]{2,24}$?");
    final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storeValidations();
    }

    @FXML
    void left(ActionEvent event) {
        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), vb_content);
        translate.setToX(root.getLayoutX());
        translate.play();
    }

    @FXML
    void right(ActionEvent event) {
        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), vb_content);
        translate.setToX(vb_content.getLayoutX() + (root.getPrefWidth() - vb_content.getPrefWidth()) + 3);
        translate.play();
    }

    @FXML
    void searchMailIsExistsOnAction(javafx.scene.input.KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        System.out.println(txtMail.getText());
        if(new LoginController().getMails(txtMail.getText())){
            mailAvailable = true;
            txtMail.setStyle("-fx-text-inner-color: red;");
        }else{
            mailAvailable = false;
            txtMail.setStyle("-fx-text-inner-color: black;");
        }
    }

    @FXML
    void signIn(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (txtConfirmPass.getText() != null && !mailAvailable && txtPass.getText() != null && txtMail.getText() != null
                && !lblUserId.getText().equals("X-XXXXX") && txtUserName.getText() != null && txtAddress!=null) {

           if(validate().isEmpty()){
               if (txtPass.getText().equals(txtConfirmPass.getText())) {
                   FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/AdminLoginConfirmation.fxml"));
                   Parent parent =loader.load();
                   AdminLoginConfirmationController controller = loader.<AdminLoginConfirmationController>getController();
                   Stage stage = new Stage();
                   stage.setScene(new Scene(parent));
                   stage.showAndWait();

                   if (controller.loginCorrect) {
                       UserDetail u1 = new UserDetail(lblUserId.getText(), txtUser.getText(), txtAddress.getText(), txtMail.getText(), "Cashier", txtConfirmPass.getText());

                       if (new LoginController().saveUser(u1)) {
                           new Alert(Alert.AlertType.CONFIRMATION, "User Saved..").show();

                           TranslateTransition translate = new TranslateTransition(Duration.seconds(2), vb_content);
                           translate.setToX(root.getLayoutX());
                           translate.play();

                           lblUserId.setText("X-XXXXX");
                           txtUser.setText(null);
                           txtAddress.setText(null);
                           txtMail.setText(null);
                           txtPass.setText(null);
                           txtConfirmPass.setText(null);

                       } else
                           new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                   }
               } else {
                   new Alert(Alert.AlertType.ERROR, "password Miss Match !!").show();
               }
           }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please fill all the Fields..").show();
        }

    }

    @FXML
    void generateUserIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new LoginController().generateUserId();
        lblUserId.setText(lastId);
    }

    @FXML
    public void ButtonLoginAction(ActionEvent event) throws Exception{
        final boolean[] goToIn = {false};

        ArrayList<UserDetail> receptionDetails = new LoginController().getAllUsers(cashierRadBtn.getText());
        receptionDetails.forEach(e->{
            if(e.getUserId().equals(txtUserName.getText()) && e.getPassword().equals(txtPassword.getText()) && cashierRadBtn.isSelected()){
                System.out.println(e.getAccType());

                loadUI("CustomerDetailsForm");
                goToIn[0] =true;
                userId = e.getUserId();
            }
        });

        ArrayList<UserDetail> adminDetails = new LoginController().getAllUsers(adminRadBtn.getText());
        adminDetails.forEach(e->{
            if(e.getUserId().equals(txtUserName.getText()) && e.getPassword().equals(txtPassword.getText()) && adminRadBtn.isSelected()){
                System.out.println(e.getAccType());

                loadUI("AdminMainRoot");
                goToIn[0] =true;
                userId = e.getUserId();
            }
        });

        if(!goToIn[0]){
            new Alert(Alert.AlertType.ERROR,"Invalid User !!").show();
            txtUserName.setStyle("-fx-text-inner-color: Red;");
            txtPassword.setStyle("-fx-text-inner-color: Red;");
        }
    }

    private void loadUI(String name){
        Stage primaryStage = (Stage) LoginBtn.getScene().getWindow();
        primaryStage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/"+name+".fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Validate Form ---------------------------------------------------------------------------------------------------

    private void storeValidations(){
        map.put(txtUser ,all_REGEX);
        map.put(txtAddress,all_REGEX);
        map.put(txtPass,phoneNo_regex);
        map.put(txtMail,EMAIL_REGEX);
    }

    public ArrayList<Object> validate(){
        ArrayList<Object> objTf = new ArrayList<>();
        for (TextField txtF : map.keySet()){
            Pattern regex = map.get(txtF);
            if(!regex.matcher(txtF.getText()).matches()){
                txtF.setStyle("-fx-text-inner-color: Red;");
                objTf.add(txtF);
            }else{
                txtF.setStyle("-fx-text-inner-color: black;");
            }
        }
        return objTf;
    }
}
