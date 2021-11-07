package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class MonthlyIncomeViewController implements Initializable {

    @FXML
    private Label lblMonthlyProfit;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Label lblSalesProfit;

    @FXML
    private Label lblWarrantedOrderProfit;

    @FXML
    private Label lblOrderProfit;

    @FXML
    private PieChart pieChartView;

    @FXML
    private JFXButton btnShowDailyIncomeView;

    @FXML
    private AnchorPane monthlyRoot;

    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private Label lblEmptyProfit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbMonth.getItems().addAll("January","February","March","April","May","June","August","September","October",
        "November","December");

        /*String month = String.valueOf(ZonedDateTime.now().getMonth());*/
        setData("October");

        //---------------------------------------------------------------------------------------------------------------

        XYChart.Series series = new XYChart.Series();

        double totalOrdersProfit=0;
        double totalExternalCharges=0;
        double totalWarrantedItemsProfit=0;
        double allProfits=0;

        for(int i=1; i<=12; i++){

            try {

                System.out.println(i);

                totalOrdersProfit =  new ReportController().getMonthly_TotalOrderProfit_toMonthNo(String.valueOf(i));
                totalExternalCharges = new ReportController().getMonthly_TotalExternalChargeProfit_toMonthNo(String.valueOf(i));
                totalWarrantedItemsProfit = new ReportController().getMonthly_TotalWarrantedItemProfit_toMonthNo(String.valueOf(i));

                System.out.println(totalOrdersProfit + "  " + totalExternalCharges +"  " + totalWarrantedItemsProfit);

                allProfits = totalOrdersProfit + totalExternalCharges + totalWarrantedItemsProfit;

                series.getData().add(new XYChart.Data(String.valueOf(i),allProfits));

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }

        lineChart.getData().addAll(series);
    }

    public void selectMonthOnAction(ActionEvent event) {
        System.out.println(cmbMonth.getValue()+" //");
        setData(cmbMonth.getValue());
    }

    @FXML
    void showDailyIncomeOnAction(ActionEvent event) {
        URL resource = getClass().getResource("../view/DailyIncomeView.fxml");
        Parent load = null;
        try {
            load = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        monthlyRoot.getChildren().clear();
        monthlyRoot.getChildren().add(load);
    }

    private void setData(String month){

        String mon =String.valueOf( getMonthNumber(month));

        double ordProfit=0;
        double externalCharge=0;
        double warrantedCharge=0;

        try {
            ordProfit = new ReportController().getMonthly_TotalOrderProfit_toMonthNo(mon);
            externalCharge = new ReportController().getMonthly_TotalExternalChargeProfit_toMonthNo(mon);
            warrantedCharge = new ReportController().getMonthly_TotalWarrantedItemProfit_toMonthNo(mon);

            lblMonthlyProfit.setText(String.valueOf((ordProfit+externalCharge+warrantedCharge)));
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

    public int getMonthNumber(String  month){
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
}
