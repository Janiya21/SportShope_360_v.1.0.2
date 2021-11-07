package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import db.DbConnection;
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
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class DailyIncomeViewController implements Initializable {

    @FXML
    public JFXButton btnShowAnnualReport;

    @FXML
    private PieChart pieChartView;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Label lblSalesProfit;

    @FXML
    private AnchorPane dailyIncomeRoot;

    @FXML
    private Label lblWarrantedOrderProfit;

    @FXML
    private Label lblOrderProfit;

    @FXML
    private Label lblDailyProfit;

    @FXML
    private JFXDatePicker dateProfitDate;

    @FXML
    private Label lblEmptyProfit;

    @FXML
    private JFXButton btnDetailedView;

    @FXML
    private JFXComboBox<String> cmbMonth;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbMonth.getItems().addAll("January","February","March","April","May","June","August","September","October",
                "November","December");

        setData(loadDate());

        //------------------------------------------------------------
        int month = getMonthNumber("September");
        setLineChartData(String.valueOf(month));

    }

    @FXML
    void dateProfitOnAction(ActionEvent event) {
        setData(String.valueOf(dateProfitDate.getValue()));
    }

    private void setData(String date){
        double ordProfit=0;
        double externalCharge=0;
        double warrantedCharge=0;

        try {
            ordProfit = new ReportController().getTotalOrderProfit(date);
            externalCharge = new ReportController().getTotalExternalChargeProfit(date);
            warrantedCharge = new ReportController().getTotalWarrantedItemProfit(date);

            lblDailyProfit.setText(String.valueOf((ordProfit+externalCharge+warrantedCharge)));
            lblOrderProfit.setText(String.valueOf(externalCharge));
            lblSalesProfit.setText(String.valueOf(ordProfit));
            lblWarrantedOrderProfit.setText(String.valueOf(warrantedCharge));

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if(ordProfit==0 && externalCharge==0 && warrantedCharge==0){
            lblEmptyProfit.setText("No Sales UpTo Now !");
        }else{
            lblEmptyProfit.setText(null);
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Sales", ordProfit),
                new PieChart.Data("Repair Orders",externalCharge),
                new PieChart.Data("Warrant orders",warrantedCharge)
        );

        pieChartView.setData(pieChartData);
    }

    @FXML
    public void loadMonthlyReportOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/MonthlyIncomeView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dailyIncomeRoot.getChildren().clear();
        dailyIncomeRoot.getChildren().add(load);
    }

    @FXML
    void setMonthOnAction(ActionEvent event) {
        int mon = getMonthNumber(cmbMonth.getValue());
        setLineChartData(String.valueOf(mon));
    }

    private String loadDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }

    public int getMonthNumber(String month){
        switch (month) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
        }
        return 0;
    }

    private void setLineChartData(String mon){
        XYChart.Series series = new XYChart.Series();
        double totalOrdersProfit=0;
        double totalExternalCharges=0;
        double totalWarrantedItemsProfit=0;
        double allProfits=0;
        String date=null;

        for(int i=1; i<=30; i++){
            try {
                date="2021-"+mon+"-".concat(String.valueOf(i));
                totalOrdersProfit = new ReportController().getTotalOrderProfit(date);
                totalExternalCharges = new ReportController().getTotalExternalChargeProfit(date);
                totalWarrantedItemsProfit = new ReportController().getTotalWarrantedItemProfit(date);

                allProfits = totalOrdersProfit + totalExternalCharges + totalWarrantedItemsProfit;

                series.getData().add(new XYChart.Data(String.valueOf(i),allProfits));

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }

        lineChart.getData().addAll(series);
    }
}
