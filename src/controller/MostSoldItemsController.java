package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import model.Item;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MostSoldItemsController implements Initializable {
    @FXML
    private PieChart mostSoldItems;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setData();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setData() throws SQLException, ClassNotFoundException {

        ArrayList<Item> topItems = new ItemController().getBestItemsProfit();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        topItems.forEach(e->{
            pieChartData.addAll(new PieChart.Data(e.getDesc(), e.getUnitProfit()));
        });

        mostSoldItems.setData(pieChartData);
    }
}
