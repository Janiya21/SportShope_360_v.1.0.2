package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.ItemDetail;
import model.Order;
import view.TM.CartTm;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class PlaceOrderFormController implements Initializable {

    @FXML
    public Label lblTotal;

    @FXML
    public Label lblFullAmount;

    @FXML
    public Label lblFullDiscount;

    @FXML
    public Label lblTime;

    @FXML
    public Button btnCheckOut;

    @FXML
    public Label lblUserId;

    @FXML
    public Button goBackBtn;

    @FXML
    public JFXToggleButton radBtnCash;

    @FXML
    public JFXToggleButton radBtnCard;

    @FXML
    private AnchorPane ItemsRoot;

    @FXML
    private TableView<Item> tblAllItems;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    public TableColumn<?,?> colCategory;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private JFXTextField txtSearchItem;

    @FXML
    public JFXTextField txtWarrantyPeriod;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtDiscount;

    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblDate;

    //--------------------------------------

    @FXML
    private TableView<CartTm> orderTable;

    @FXML
    private TableColumn<?, ?> ordColItem;

    @FXML
    private TableColumn<?, ?> ordColDesc;

    @FXML
    private TableColumn<?, ?> ordColQty;

    @FXML
    private TableColumn<?, ?> ordColUnitPrice;

    @FXML
    private TableColumn<?, ?> orderColDiscount;

    @FXML
    private TableColumn<?, ?> orderColTot;

    @FXML
    private FontAwesomeIconView fntQtyWarning;

    @FXML
    private FontAwesomeIconView fntDiscountWarning;

    private boolean rowSelected=false;
    private int selectedIndex=-1;
    private Item selectedRowObject=null;
    public static String billTotal;
    public static String previousWindow=null;
    public static String externalOrderId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(previousWindow);

        loadDateAndTime();

        lblCustomerId.setText(CustomerDetailsFormController.selectedCustomer);
        lblUserId.setText(LoginViewController.userId);

        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        try {
            //cmbCategory.getItems().add("All");
            loadCategorizedItems();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        try {
            setItemsToTable(new ItemController().getAllItems());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblAllItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rowSelected=true;
            selectedRowObject=newValue;
            setItemDetailsToFields(newValue);
        });

        orderTable.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            selectedIndex = (int) newValue;
        }));

        loadDataToItemTable();

        btnCheckOut.setDisable(true);
        lblTotal.textProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println(newValue + " " + oldValue);
            btnCheckOut.setDisable(newValue.equals("00000.00"));
        }));

    }

    ObservableList<Item> itemObsList = FXCollections.observableArrayList();

    @FXML
    void addToCartOnAction(ActionEvent event) {
        if(!txtQty.getText().isEmpty() && !txtDiscount.getText().isEmpty() && rowSelected){

            boolean qtyOk=true;
            boolean discountOk = true;

            if(!isDisValid(txtDiscount.getText())){
                fntDiscountWarning.setVisible(true);
                discountOk=false;
            }else{
                fntDiscountWarning.setVisible(false);
            }
            if(!isQtyValid(txtQty.getText())){
                fntQtyWarning.setVisible(true);
                qtyOk=false;
            }else{
                fntQtyWarning.setVisible(false);
            }

            if(qtyOk && discountOk){
                String id = selectedRowObject.getItemCode();
                String desc = selectedRowObject.getDesc();
                int orderedQty = Integer.parseInt(txtQty.getText());
                int discountPercentage = Integer.parseInt(txtDiscount.getText());
                double unitPrice = selectedRowObject.getUnitPrice();

                double discountAmount = orderedQty*((unitPrice * discountPercentage)/100);
                double total = (unitPrice * orderedQty) - discountAmount;

                if (Integer.parseInt(txtQtyOnHand.getText()) < orderedQty){
                    new Alert(Alert.AlertType.WARNING,"The Store Can't Provide That Quantity !!").show();
                    return;
                }

                CartTm cart = new CartTm(id,desc,orderedQty,unitPrice,discountAmount,total);

                int rowNumber=isExists(cart);
                System.out.println("No   - " + rowNumber);
                if(rowNumber==-2){
                    new Alert(Alert.AlertType.WARNING,"The Store Can't Provide That Quantity !!").show();
                }else{
                    if (rowNumber==-1){
                        cartObsList.add(cart);
                    }else{
                        CartTm temp = cartObsList.get(rowNumber);

                        CartTm cart2 = new CartTm(temp.getItemCode(),temp.getDesc(),temp.getQty()+orderedQty,
                                temp.getUnitPrice(),temp.getDiscount()+discountAmount,temp.getTotal()+total);

                        cartObsList.remove(rowNumber);
                        cartObsList.add(cart2);
                    }

                    orderTable.setItems(cartObsList);

                    setTotal();
                }
                txtQtyOnHand.setText(null);txtQty.setText(null);txtDiscount.setText(null);txtPrice.setText(null);
                txtWarrantyPeriod.setText(null);
            }

        }else{
            new Alert(Alert.AlertType.ERROR,"Please Select a table row or fill the empty fields").show();
        }
    }

    ObservableList<CartTm> cartObsList = FXCollections.observableArrayList();

    public static boolean isQtyValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("^([1-9][0-9]?|100)$?");
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isDisValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("^([1-9][0-9]?|100)$?");
        return EMAIL_REGEX.matcher(email).matches();
    }

    @FXML
    void setOrderIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new OrderController().generateOrderId();
        lblOrderId.setText(lastId);
    }

    @FXML
    public void searchItemOnAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        if(containVarchar(txtSearchItem.getText())){
            String word = txtSearchItem.getText();

            itemObsList.clear();
            tblAllItems.getItems().removeAll();
            setItemsToTable((new ItemController().getRelevantItems(word)));
        }
    }

    @FXML
    public void CategorizingOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        itemObsList.clear();
        tblAllItems.getItems().removeAll();
        if(cmbCategory.getValue().equals("All")){
            setItemsToTable(new ItemController().getAllItems());
        }else{
            setCategorizedItems(new ItemController().getCategorizedItems(cmbCategory.getValue()));
        }
    }

    private void loadDataToItemTable(){
        ordColItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        ordColDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        ordColQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        ordColUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        orderColDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        orderColTot.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private int isExists(CartTm tm){
        for (int i = 0; i < cartObsList.size(); i++) {
            if (tm.getItemCode().equals(cartObsList.get(i).getItemCode())){
                if(Integer.parseInt(txtQtyOnHand.getText()) < (cartObsList.get(i).getQty() + Integer.parseInt(txtQty.getText()) )){
                    return -2;
                }
                return i;
            }
        }
        return -1;
    }

    public void checkOutOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        if(lblOrderId.getText().equals("x-xxxxx")){
            new Alert(Alert.AlertType.ERROR,"Please Generate an OrderId..").show();
        }else {

            externalOrderId=lblOrderId.getText();

            ArrayList<ItemDetail> items = new ArrayList<>();
            double total = 0;
            double totalOrderProfit = 0;
            for (CartTm tempTm : cartObsList) {
                total += tempTm.getTotal();
                double itemProfit = tempTm.getQty() * (new OrderController().getUnitProfit(tempTm.getItemCode()));
                totalOrderProfit += itemProfit;
                items.add(new ItemDetail(tempTm.getItemCode(), tempTm.getQty(), tempTm.getTotal(), itemProfit));
            }

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/paymentForm.fxml"));
            Parent parent =loader.load();
            PaymentFormController controller = loader.<PaymentFormController>getController();
            controller.orderId=lblOrderId.getText();
            controller.lblFullAmount.setText(lblFullAmount.getText());
            controller.lblDiscount.setText(lblFullDiscount.getText());
            controller.lblTotal.setText(lblTotal.getText());

            controller.order= new Order(
                    lblOrderId.getText(),
                    LoginViewController.userId,
                    CustomerDetailsFormController.selectedCustomer,
                    lblDate.getText(),
                    Double.parseDouble(lblFullAmount.getText()),
                    Double.parseDouble(lblFullDiscount.getText()),
                    Double.parseDouble(lblTotal.getText()),
                    totalOrderProfit,
                    items
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            if(previousWindow.equals("WarrantedItem_Repairs")){
                WarrantedItemRepairController.billTot=lblTotal.getText();
                Stage primaryStage = (Stage) goBackBtn.getScene().getWindow();
                primaryStage.close();
            }else if(previousWindow.equals("NewRepairs")){
                billTotal=lblTotal.getText();
                Stage primaryStage = (Stage) goBackBtn.getScene().getWindow();
                primaryStage.close();
            }else{
                System.out.println("Done");
            }

            txtQty.setText(null);
            txtDiscount.setText(null);

            lblFullAmount.setText("00000.00");
            lblFullDiscount.setText(" 0000.00");
            lblTotal.setText("00000.00");

            cartObsList.clear();
            orderTable.getItems().clear();
        }
    }

    @FXML
    void removeAllTblItems(ActionEvent event) {
        lblTotal.setText("00000.00");
        lblFullDiscount.setText("0000.00");
        lblFullAmount.setText("00000.00");
        cartObsList.clear();
        orderTable.getItems().clear();
        btnCheckOut.setDisable(true);
    }

    private void setItemsToTable(ArrayList<Item> items) {
        items.forEach(e->{
            if(e!=null){
                itemObsList.add(
                        new Item(e.getItemCode(),e.getAgentId(),e.getDesc(),e.getCategory(),e.getWarrantyMonths(),
                                e.getQtyOnHand(),e.getSupPrice(),e.getUnitPrice(),e.getUnitProfit()));
            }
        });
        tblAllItems.setItems(itemObsList);
    }

    private void setItemDetailsToFields(Item items){
        if(items!=null){
            txtWarrantyPeriod.setText(String.valueOf(items.getWarrantyMonths()));
            txtQtyOnHand.setText(String.valueOf(items.getQtyOnHand()));
            txtPrice.setText(String.valueOf(items.getUnitPrice()));
        }
    }

    private void loadCategorizedItems() throws SQLException, ClassNotFoundException {
        List<String> itemIds = new ItemController().getAllItemIds(cmbCategory.getValue());
        cmbCategory.getItems().addAll(itemIds);
    }

    private void setCategorizedItems(ArrayList<Item> items){
        items.forEach(e->{
            if(e!=null){
                itemObsList.add(
                        new Item(e.getItemCode(),e.getAgentId(),e.getDesc(),e.getCategory(),e.getWarrantyMonths(),
                                e.getQtyOnHand(),e.getSupPrice(),e.getUnitPrice(),e.getUnitProfit()));
            }
        });
        tblAllItems.setItems(itemObsList);
    }

    private void setTotal(){
        double total = 0;
        double actualFullCost = 0;
        double fullDiscount=0;
        for (CartTm i: cartObsList) {
            total+=i.getTotal();
            actualFullCost+=i.getQty()*i.getUnitPrice();
            fullDiscount+=i.getDiscount();
        }
        lblFullAmount.setText(String.valueOf(actualFullCost));
        lblFullDiscount.setText(String.valueOf(fullDiscount));
        lblTotal.setText(String.valueOf(total));
    }

    private void loadDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() +
                            " : " + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void removeCartItemOnAction(ActionEvent event) {
        System.out.println(selectedIndex);
        if(selectedIndex==-1){
            new Alert(Alert.AlertType.ERROR,"Please Select a Row").show();
        }else{
            cartObsList.remove(selectedIndex);
            setTotal();
        }
    }

    public void goBackOnAction(ActionEvent event) {
        if( previousWindow.equals("WarrantedItem_Repairs") || previousWindow.equals("NewRepairs")){
            Stage primaryStage = (Stage) goBackBtn.getScene().getWindow();
            primaryStage.close();
        }else if(previousWindow.equals("Customer_Order")){
            loadUI("CustomerDetailsForm");
        }
    }

    private void loadUI(String name){
        Stage primaryStage = (Stage) goBackBtn.getScene().getWindow();
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
