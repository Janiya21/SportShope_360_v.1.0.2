package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Item;
import model.RepairOrder;
import model.Warranted_RepairOrders;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.TM.RepairItemTm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RepairOrderFormController implements Initializable {

    public JFXTextField btnSrchBill;
    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblBillNo;

    @FXML
    private JFXTextField txtExternalCost;

    @FXML
    private JFXTextField txtExternalItemCharges;

    @FXML
    private Button btnExternalOrder;

    @FXML
    private JFXDatePicker dateReturnDate;

    @FXML
    private JFXTextArea txtRepairNote;

    @FXML
    private Button btnGenerateBill;

    @FXML
    private JFXTextField txtExternalOrderId;

    @FXML
    private Label lblItemNo;

    @FXML
    private TableView<RepairItemTm> tblRepairItem;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colBillNo;

    @FXML
    private TableColumn<?, ?> colRepairNote;

    @FXML
    private TableColumn<?, ?> colExternalOrderId;

    @FXML
    private TableColumn<?, ?> colExternalCharge;

    @FXML
    private TableColumn<?, ?> colReceivedDate;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private TableColumn<?, ?> colBillAmount;

    @FXML
    private FontAwesomeIconView fntExternalCost;

    @FXML
    private FontAwesomeIconView fntRepairNote;

    @FXML
    private FontAwesomeIconView fntDateWarning;

    @FXML
    private Button btnGoBack;

    public static boolean fromRepairPane=false;
    public static String billTot="0.00";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblCustomerId.setText(CustomerDetailsFormController.selectedCustomer);
        //
        //dateReturnDate.setPromptText(lblDate.getText());

        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        colRepairNote.setCellValueFactory(new PropertyValueFactory<>("repairNote"));
        colExternalOrderId.setCellValueFactory(new PropertyValueFactory<>("externalOrderId"));
        colExternalCharge.setCellValueFactory(new PropertyValueFactory<>("externalCharge"));
        colReceivedDate.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colBillAmount.setCellValueFactory(new PropertyValueFactory<>("billTotal"));

        loadDateAndTime();

        try {
            setDataToTable(new OrderController().getRepairOrderDetails());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblRepairItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            /*rowSelected=true;
            selectedRowObject=newValue;*/
            //setItemDetailsToFields(newValue);
        });

    }



    @FXML
    void generateBillNoOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new OrderController().generateRepairBillNo();
        lblBillNo.setText(lastId);
    }

    @FXML
    void generateBillOnAction(ActionEvent event) {
        if(!lblBillNo.getText().equals("X-XXXXX") && !lblItemNo.getText().equals("X-XXXXX")){
            if(dateReturnDate.getValue()!=null && !txtRepairNote.getText().isEmpty() && txtExternalCost.getText()!=null){

                boolean externalCostOk=true;
                boolean repairNoteOk=true;
                boolean dateOk=true;

                if(dateReturnDate.getValue()==null){
                    fntDateWarning.setVisible(true);
                    dateOk=false;
                }else{
                    fntDateWarning.setVisible(false);
                }
                if(!isExtraCostValid(txtExternalCost.getText())){
                    fntExternalCost.setVisible(true);
                    externalCostOk=false;
                }else{
                    fntExternalCost.setVisible(false);
                }
                if(!isValidRepairNote(txtRepairNote.getText())){
                    fntRepairNote.setVisible(true);
                    repairNoteOk=false;
                }else{
                    fntRepairNote.setVisible(false);
                }

               if(externalCostOk && repairNoteOk && dateOk){

                   RepairOrder o1= new RepairOrder(lblBillNo.getText(),lblCustomerId.getText(),Double.parseDouble(txtExternalCost.getText())+Double.parseDouble(txtExternalItemCharges.getText())); // Loooooooooook
                   RepairItemTm t1 = new RepairItemTm(lblItemNo.getText(),lblBillNo.getText(),txtExternalOrderId.getText(),txtRepairNote.getText(),
                           lblDate.getText(),String.valueOf(dateReturnDate.getValue()),Double.parseDouble(txtExternalItemCharges.getText()),
                           Double.parseDouble(txtExternalCost.getText()),Double.parseDouble(txtExternalItemCharges.getText())+
                           Double.parseDouble(txtExternalCost.getText()));

                   repairOrderObsList.add(t1);

                   if(new OrderController().placeRepairOrder(o1,t1)) {

                       //viewReport();
                       double tot = Double.parseDouble(txtExternalItemCharges.getText())+
                               Double.parseDouble(txtExternalCost.getText());

                       new Alert(Alert.AlertType.CONFIRMATION, "Success..").show();

                       try {
                           JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/Reports/orderReport01.jrxml"));
                           JasperReport compileReport = JasperCompileManager.compileReport(design);

                           String lblTotal1 = String.valueOf(tot);

                           HashMap map = new HashMap();
                           map.put("lblTotal",lblTotal1);
                           map.put("lblOrderId",lblBillNo.getText());

                           JasperPrint print = JasperFillManager.fillReport(compileReport,map, DbConnection.getInstance().getConnection());
                           JasperViewer.viewReport(print,false);
                       } catch (JRException | ClassNotFoundException | SQLException e) {
                           e.printStackTrace();
                       }

                       txtExternalItemCharges.setText("00.00");txtExternalCost.setText("00.00");txtExternalOrderId.setText(null);txtRepairNote.setText(null);
                       lblItemNo.setText("X-XXXXX");lblBillNo.setText("X-XXXXX");
                   }
                   else {
                       new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                   }
                   tblRepairItem.refresh();
               }
            }else{
                new Alert(Alert.AlertType.ERROR,"Please Fill All Fields..").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Generate Bill/Item Number..").show();
        }
    }

    @FXML
    void goBackOnAction(ActionEvent event) {

        Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
        primaryStage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/CustomerDetailsForm.fxml"));
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
    void placeExtenalOrderOnAction(ActionEvent event) {
        PlaceOrderFormController.previousWindow="NewRepairs";

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/PlaceOrderForm.fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assert root != null;
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

        System.out.println("Total : " + PlaceOrderFormController.billTotal);
        txtExternalOrderId.setText(PlaceOrderFormController.externalOrderId);
        txtExternalItemCharges.setText(PlaceOrderFormController.billTotal);
    }

    @FXML
    void srchTxtBillNo(KeyEvent event) throws SQLException, ClassNotFoundException {
        if(containVarchar(btnSrchBill.getText())){
            repairOrderObsList.clear();
            tblRepairItem.getItems().removeAll();
            setDataToTable(new OrderController().srchRepairBillNo(btnSrchBill.getText()));
        }
    }

    public boolean containVarchar(String source){
        boolean result = false;
        Pattern pattern = Pattern.compile("[\\w]*");
        result = pattern.matcher(source).matches();
        return result;
    }

    ObservableList<RepairItemTm> repairOrderObsList = FXCollections.observableArrayList();

    @FXML
    public void generateItemCodeOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new ItemController().generateRepairItemCode();
        lblItemNo.setText(lastId);
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

    private void setDataToTable(ArrayList<RepairItemTm> RepairOrders){
        RepairOrders.forEach(e->{
            repairOrderObsList.add(new RepairItemTm(e.getItemCode(),e.getBillNo(),e.getExternalOrderId(),e.getRepairNote(),e.getReceivedDate(),
                    e.getReturnDate(),e.getExternalOrder_cost(),e.getExternalCharge(),e.getExternalOrder_cost()+e.getExternalCharge()));
        });
        tblRepairItem.setItems(repairOrderObsList);
    }

    /*private void viewReport(){
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/ItemSales.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint print = JasperFillManager.fillReport(compileReport,null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(print,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }*/

    public static boolean isExtraCostValid(String cost) {
        final Pattern EMAIL_REGEX = Pattern.compile("\\d{1,8}\\.\\d{0,8}?");
        return EMAIL_REGEX.matcher(cost).matches();
    }

    public static boolean isValidRepairNote(String note) {
        final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z\\s]{2,100}$?");
        return EMAIL_REGEX.matcher(note).matches();
    }
}
