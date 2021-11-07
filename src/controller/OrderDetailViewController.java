package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Customer;
import model.Item;
import model.Order;
import view.TM.OrderDetailTm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class OrderDetailViewController implements Initializable {

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colBillTotal;

    @FXML
    private JFXTextField txtSearchId;

    @FXML
    private JFXTextField txtCustId;

    @FXML
    private JFXTextField txtTelephone;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtMail;

    @FXML
    private JFXDatePicker txtDateView;

    @FXML
    private TableView<OrderDetailTm> tblItem;

    @FXML
    public Button btnGoNext;

    @FXML
    public Button btnGoBack;

    @FXML
    public TableColumn<?, ?> colItemCode;

    @FXML
    public TableColumn<?, ?> colItemDesc;

    @FXML
    public TableColumn<?, ?> colItemQty;

    @FXML
    public TableColumn<?, ?> colItemWarranty;

    @FXML
    public TableColumn<?, ?> colItemTotal;

    @FXML
    public TableColumn<?, ?> colItemProfit;

    @FXML
    private FontAwesomeIconView fntIdWarning;

    public String itemCode=null;
    public String orderId=null;
    boolean forOrderHistory=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("fullDiscount"));
        colBillTotal.setCellValueFactory(new PropertyValueFactory<>("absoluteTotal"));

        try {
            setOrdersToTable(new OrderController().getOrderDetails());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {

                /*tblItem.getSelectionModel().select(-1);*/

                tblItem.getItems().clear();
                orderedItemObsList.clear();
                loadCustomersToFields(newValue);
                orderId=newValue.getOrderId();
                loadItemsToTable(new OrderController().getItem(newValue.getOrderId()));
                //calculateTotal();

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        tblItem.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            /*tblItem.getSelectionModel().select(-1);*/
            if(newValue!=null){
                itemCode=newValue.getItemCode();
            }

        }));
    }

    ObservableList<Order> orderObsList = FXCollections.observableArrayList();
    ObservableList<OrderDetailTm> orderedItemObsList = FXCollections.observableArrayList();

    @FXML
    void goBackOnAction(ActionEvent event) {

        if(forOrderHistory){
            Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
            primaryStage.close();
        }else{
            loadUI("CustomerDetailsForm");
        }
    }

    @FXML
    void goNextOnAction(ActionEvent event) throws IOException {
        if(itemCode!=null){
            if(CustomerDetailsFormController.orderDesc){
                Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
                primaryStage.close();

                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/WarrantedItemRepairView.fxml"));
                Parent parent =loader.load();
                WarrantedItemRepairController controller = loader.<WarrantedItemRepairController>getController();
                controller.lblOrderId.setText(orderId);
                controller.txtItemId.setText(itemCode);
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Item not Selected in Table").show();
        }
    }

    @FXML
    void searchDateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(txtDateView.getValue()!=null){
            orderObsList.clear();
            tblOrders.getItems().removeAll();

            setCategorizedItems(new OrderController().getRelevantDateItems(String.valueOf(txtDateView.getValue())));
        }
    }

    @FXML
    void searchIdOnAction(KeyEvent event) throws SQLException, ClassNotFoundException {

        fntIdWarning.setVisible(false);

        if(containVarchar(txtSearchId.getText())){
            String word = txtSearchId.getText();

            orderObsList.clear();
            tblOrders.getItems().removeAll();
            setItemsToTable((new OrderController().getRelevantOrder(word)));
        }else{
            fntIdWarning.setVisible(true);
        }
    }

    @FXML
    public void reloadTableOnAction(ActionEvent event) {
        try {
            setOrdersToTable(new OrderController().getOrderDetails());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        tblOrders.refresh();
    }

    private void setOrdersToTable(ArrayList<Order> orders) {
        orders.forEach(e->{
            orderObsList.add(
                    new Order(e.getOrderId(),e.getUserId(),e.getFullDiscount(),e.getAbsoluteTotal()));
        });
        tblOrders.setItems(orderObsList);
    }

    private void loadCustomersToFields(Order order) throws SQLException, ClassNotFoundException {
        Customer customer = new CustomerController().getCustomer(order.getOrderId());
        txtCustId.setText(customer.getCustomerId());
        txtCustomerName.setText(customer.getName());
        txtTelephone.setText(customer.getTelephone());
        txtMail.setText(customer.getEmail());
    }

    private void loadItemsToTable(ArrayList<OrderDetailTm> order) throws SQLException, ClassNotFoundException {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colItemWarranty.setCellValueFactory(new PropertyValueFactory<>("warranty"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colItemProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));

        order.forEach(e->{
            orderedItemObsList.add(new OrderDetailTm(e.getItemCode(),e.getDesc(),e.getQty(),e.getWarranty(),e.getTotal(),e.getProfit()));
        });
        tblItem.setItems(orderedItemObsList);
    }

    private void setCategorizedItems(ArrayList<Order> orders){
        orders.forEach(e->{
            if(e!=null){
                orderObsList.add(
                        new Order(e.getOrderId(),e.getUserId(),e.getFullDiscount(),e.getAbsoluteTotal()));
            }
        });
        tblOrders.setItems(orderObsList);
    }

    private void setItemsToTable(ArrayList<Order> orders) {
        orders.forEach(e->{
            if(e!=null){
                orderObsList.add(
                        new Order(e.getOrderId(),e.getUserId(),e.getFullDiscount(),e.getAbsoluteTotal()));
            }
        });
        tblOrders.setItems(orderObsList);
    }

    private void loadUI(String name){
        Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
        primaryStage.close();

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

    public boolean containVarchar(String source){
        boolean result = false;
        Pattern pattern = Pattern.compile("[\\w]*");
        result = pattern.matcher(source).matches();
        return result;
    }
}
