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
import model.Warranted_RepairOrders;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

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

public class WarrantedItemRepairController implements Initializable {

    @FXML
    public JFXTextField txtExternalOrderId;

    @FXML
    public Label lblOrderId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<Warranted_RepairOrders> tblRepairWarrantedItem;

    @FXML
    private TableColumn<?, ?> colBillNo;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colExternalOrder;

    @FXML
    private TableColumn<?, ?> colRepairNote;

    @FXML
    private TableColumn<?, ?> colReceivedDate;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    public JFXTextField txtItemId;

    @FXML
    private Label lblBillNo;

    @FXML
    private JFXTextField txtExternalCost;

    @FXML
    public JFXTextField txtExternalItemCharges;

    @FXML
    private Button btnExternalOrder;

    @FXML
    private JFXDatePicker dateReturnDate;

    @FXML
    private JFXTextArea txtRepairNote;

    @FXML
    private Button btnGenerateBill;

    @FXML
    private Button btnGoBack;

    @FXML
    private FontAwesomeIconView fntChargeWarning;

    @FXML
    private FontAwesomeIconView fntNoteWarning;

    @FXML
    private FontAwesomeIconView fntReturnnDateWarnng;

    public static boolean fromWarrantedPane=false;
    public static String billTot="0.00";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colExternalOrder.setCellValueFactory(new PropertyValueFactory<>("externalItemOrder"));
        colRepairNote.setCellValueFactory(new PropertyValueFactory<>("repairNote"));
        colReceivedDate.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("externalCost"));

        loadDateAndTime();

        try {
            setDataToTable(new OrderController().getWarrantedItem_Orders());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<Warranted_RepairOrders> repairOrderObsList = FXCollections.observableArrayList();

    @FXML
    void generateBillNoOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new OrderController().generateBillNo();
        lblBillNo.setText(lastId);
    }

    @FXML
    void generateBillOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(!lblBillNo.getText().equals("X-XXXXX")){
            if(dateReturnDate.getValue()!=null && !txtRepairNote.getText().isEmpty() && txtExternalCost.getText()!=null){

                boolean externalCostOk=true;
                boolean repairNoteOk=true;
                boolean dateOk=true;

                if(dateReturnDate.getValue()==null){
                    fntReturnnDateWarnng.setVisible(true);
                    dateOk=false;
                }else{
                    fntReturnnDateWarnng.setVisible(false);
                }
                if(!isExtraCostValid(txtExternalCost.getText())){
                    fntChargeWarning.setVisible(true);
                    externalCostOk=false;
                }else{
                    fntChargeWarning.setVisible(false);
                }
                if(!isValidRepairNote(txtRepairNote.getText())){
                    fntNoteWarning.setVisible(true);
                    repairNoteOk=false;
                }else{
                    fntNoteWarning.setVisible(false);
                }

                if(externalCostOk && repairNoteOk && dateOk) {

                    Warranted_RepairOrders i1 = new Warranted_RepairOrders(
                            lblBillNo.getText(), lblOrderId.getText(), txtItemId.getText(), txtExternalOrderId.getText(), txtRepairNote.getText(),
                            lblDate.getText(), String.valueOf(dateReturnDate.getValue()), Double.parseDouble(txtExternalCost.getText())
                    );

                    repairOrderObsList.add(i1);

                    if (new OrderController().saveWarrantedOrder(i1)) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

                        double tot = Double.parseDouble(txtExternalItemCharges.getText())+ Double.parseDouble(txtExternalCost.getText());

                        try {
                            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/Reports/orderReport2.jrxml"));
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

                        txtExternalItemCharges.setText("00.00");txtExternalCost.setText("00.00");txtRepairNote.setText(null);txtExternalItemCharges.setText(null);
                        lblBillNo.setText("X-XXXXX");
                    }
                    else
                        new Alert(Alert.AlertType.WARNING, "Try Again..").show();

                    tblRepairWarrantedItem.refresh();
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Please Fill All Fields..").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Generate Bill Number..").show();
        }
    }

    @FXML
    void goBackOnAction(ActionEvent event) {
        Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
        primaryStage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/OrderDetailView.fxml"));
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

        PlaceOrderFormController.previousWindow="WarrantedItem_Repairs";

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

        txtExternalOrderId.setText(PlaceOrderFormController.externalOrderId);
        txtExternalItemCharges.setText(billTot);
    }

    @FXML
    void srchTxtBillNo(KeyEvent event) {

    }

    private void setDataToTable(ArrayList<Warranted_RepairOrders> warrantedRepairOrders){
        warrantedRepairOrders.forEach(e->{
            repairOrderObsList.add(new Warranted_RepairOrders(e.getBillNo(),e.getOrderId(),e.getItemCode(),e.getExternalItemOrder(),e.getRepairNote(),
                    e.getReceivedDate(),e.getReturnedDate(),e.getExternalCost()));
        });
        tblRepairWarrantedItem.setItems(repairOrderObsList);
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

    public static boolean isExtraCostValid(String cost) {
        final Pattern EMAIL_REGEX = Pattern.compile("\\d{1,8}\\.\\d{0,8}?");
        return EMAIL_REGEX.matcher(cost).matches();
    }

    public static boolean isValidRepairNote(String note) {
        final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z\\s]{2,100}$?");
        return EMAIL_REGEX.matcher(note).matches();
    }

}
