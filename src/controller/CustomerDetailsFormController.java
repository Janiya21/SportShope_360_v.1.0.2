package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CustomerDetailsFormController implements Initializable {

    public JFXTextField txtName;
    public TableView<Customer> tblCustomer;
    public TableColumn<Customer,String> colId;
    public TableColumn<Customer,String> colName;
    public TableColumn<Customer,String> colMail;
    public TableColumn<Customer,String> colTelephoneNo;
    public Button goToNextBtn;
    public Button goBackBtn;
    public JFXTextField txtTelNo;
    public JFXButton btnOrderDetails;
    public Label lblCustomerId;
    public JFXTextField txtEmail;
    @FXML
    private FontAwesomeIconView fntTelWarning;
    @FXML
    private FontAwesomeIconView fntNameWarning;

    @FXML
    private FontAwesomeIconView fntEmailWarning;

    public static Stage customerPane=null;
    int cartSelectedRowForRemove = -1;
    String deleteCustomerId = null;
    static String selectedCustomer;
    static boolean orderDesc=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tblCustomer.setFocusTraversable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephoneNo.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        try {
            setCustomersToTable(new CustomerController().getAllCustomers());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblCustomer.setItems(customerObsList);

        tblCustomer.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRowForRemove = (int) newValue;
        });

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteCustomerId = newValue.getCustomerId();

            fntNameWarning.setVisible(false);fntEmailWarning.setVisible(false);fntTelWarning.setVisible(false);

            lblCustomerId.setText(newValue.getCustomerId());
            txtName.setText(newValue.getName());
            txtEmail.setText(newValue.getEmail());
            txtTelNo.setText(newValue.getTelephone());
        });
    }

    ObservableList<Customer> customerObsList = FXCollections.observableArrayList();

    @FXML
    void addCustomerOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        boolean mailOk=true;
        boolean nameOk=true;
        boolean noOk=true;

        if(lblCustomerId.getText().equals("x-xxxxx")){
            new Alert(Alert.AlertType.ERROR,"Please Refresh a Customer Id").show();
        }else{
            if(! new CustomerController().customerExists(lblCustomerId.getText())){
                if(txtName.getText().equals("") || txtEmail.getText().equals("") || txtTelNo.getText().equals("")){
                    new Alert(Alert.AlertType.ERROR,"Please Fill All The Fields").show();
                    return;
                }else{
                    if(!isEmailValid(txtEmail.getText())){
                        fntEmailWarning.setVisible(true);
                        mailOk=false;
                    }else{
                        fntEmailWarning.setVisible(false);
                    }
                    if(!isNameValid(txtName.getText())){
                        fntNameWarning.setVisible(true);
                        nameOk=false;
                    }else{
                        fntNameWarning.setVisible(false);
                    }
                    if(!isPhoneValid(txtTelNo.getText())){
                        fntTelWarning.setVisible(true);
                        noOk=false;
                    }else{
                        fntTelWarning.setVisible(false);
                    }
                }

                if(mailOk && nameOk && noOk){
                    Customer c1 = new Customer(
                            lblCustomerId.getText(),txtName.getText(),txtEmail.getText(),txtTelNo.getText()
                    );

                    customerObsList.add(c1);

                    if(new CustomerController().saveCustomer(c1))
                        new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                    else
                        new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }else{
                new Alert(Alert.AlertType.ERROR, "This Id Already Exists..").show();
            }
        }
    }

    @FXML
    void deleteCustomerOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (cartSelectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        }else{
            deleteCustomerFromTable(deleteCustomerId);
            customerObsList.remove(cartSelectedRowForRemove);
            tblCustomer.refresh();
        }
    }

    @FXML
    public void generateIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String lastId = new CustomerController().generateCustomerId();
        lblCustomerId.setText(lastId);
        fntNameWarning.setVisible(false);fntEmailWarning.setVisible(false);fntTelWarning.setVisible(false);
        txtName.clear(); txtEmail.clear(); txtTelNo.clear();
    }

    @FXML
    public void updateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        Customer c1= new Customer(lblCustomerId.getText(),txtName.getText(),txtEmail.getText(),txtTelNo.getText());

        if(cartSelectedRowForRemove!= -1){
            customerObsList.remove(cartSelectedRowForRemove);
            customerObsList.add((cartSelectedRowForRemove),c1);
            if (new CustomerController().updateCustomer(c1,lblCustomerId.getText()))
                new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            else
                new Alert(Alert.AlertType.WARNING,"Try Again").show();

            tblCustomer.refresh();
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Select the update row").show();
        }
    }

    @FXML
    public void selectCustomerOnAction(ActionEvent event) throws IOException {
        orderDesc = true;

        if(lblCustomerId.getText().equals("x-xxxxx")){
            new Alert(Alert.AlertType.ERROR,"Please Select a Customer..").show();
        }else{
            selectedCustomer = lblCustomerId.getText();

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../view/OrderTypeConfirmation.fxml"));
                customerPane = (Stage) goToNextBtn.getScene().getWindow();
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

    @FXML
    public void goBackOnAction(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) btnOrderDetails.getScene().getWindow();
        primaryStage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
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

    @FXML
    public void goToOrderDetailsOnAction(ActionEvent event) throws IOException {
        orderDesc = false;

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/OrderDetailView.fxml"));
        Parent parent =loader.load();
        OrderDetailViewController controller = loader.<OrderDetailViewController>getController();
        controller.btnGoNext.setDisable(true);
        controller.forOrderHistory=true;
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();

    }

    private void setCustomersToTable(ArrayList<Customer> customers) {
        customers.forEach(e->{
            customerObsList.add(
                    new Customer(e.getCustomerId(),e.getName(),e.getEmail(),e.getTelephone()));
        });
        tblCustomer.setItems(customerObsList);
    }

    private void deleteCustomerFromTable(String customerId) throws SQLException, ClassNotFoundException {
        if (new CustomerController().deleteCustomer(customerId)){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public static boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isNameValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z]\\w{2,30}?");
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isPhoneValid(String email) {
        final Pattern phoneNo_regex = Pattern.compile("^[0-9]{10}$?");
        return phoneNo_regex.matcher(email).matches();
    }

}
