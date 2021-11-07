package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Order;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;

public class PaymentFormController {
    @FXML
    private JFXButton btnConfirmOrder;

    @FXML
    private JFXToggleButton radBtnCash;

    @FXML
    private ToggleGroup grp01;

    @FXML
    private JFXToggleButton radBtnCard;

    @FXML
    public Label lblFullAmount;

    @FXML
    public Label lblDiscount;

    @FXML
    public Label lblTotal;

    public Order order=null;
    public String orderId=null;

    @FXML
    void confirmOrderOnAction(ActionEvent event) {
        if (new OrderController().placeOrder(order)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Success").show();

            if(PlaceOrderFormController.previousWindow.equals("Customer_Order")){
                try {
                    JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/Reports/BillReport.jrxml"));
                    JasperReport compileReport = JasperCompileManager.compileReport(design);

                    String lblTotal1 = String.valueOf(lblTotal.getText());

                    HashMap map = new HashMap();
                    map.put("lblTotal",lblTotal1);
                    map.put("lblOrderId",orderId);

                    JasperPrint print = JasperFillManager.fillReport(compileReport,map, DbConnection.getInstance().getConnection());
                    JasperViewer.viewReport(print,false);
                } catch (JRException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }

        Stage primaryStage = (Stage) btnConfirmOrder.getScene().getWindow();
        primaryStage.close();

    }

}
