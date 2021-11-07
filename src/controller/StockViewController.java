package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Item;
import model.UserDetail;
import view.TM.BestUsers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StockViewController implements Initializable {


    @FXML
    private PieChart mostSoldItems;

    @FXML
    private TableView<UserDetail> tblUsers;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colMail;

    @FXML
    private TableColumn<?, ?> colAccType;

    @FXML
    private TableColumn<?, ?> colpass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAccType.setCellValueFactory(new PropertyValueFactory<>("accType"));
        colpass.setCellValueFactory(new PropertyValueFactory<>("password"));

        try {
            setItemsToTable(new LoginController().getUsersToTable());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        try {
            setData();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    private void setData() throws SQLException, ClassNotFoundException {

        /*ArrayList<Item> topItems = new ItemController().getBestItemsProfit();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        topItems.forEach(e->{
            pieChartData.addAll(new PieChart.Data(e.getDesc(), e.getUnitProfit()));
        });

        mostSoldItems.setData(pieChartData);*/

        ArrayList<BestUsers> topItems = new LoginController().getBestItemsUsers();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        topItems.forEach(e->{
            System.out.println(e.getUserId()+"  "+e.getTot());
            pieChartData.addAll(new PieChart.Data(e.getUserId(),e.getTot()));
        });

        mostSoldItems.setData(pieChartData);

    }

    private void setItemsToTable(ArrayList<UserDetail> items) {
        items.forEach(e->{
            if(e!=null){
                userObsList.add(
                        new UserDetail(e.getUserId(),e.getUserName(),e.getAddress(),e.getEmail(),e.getAccType(),"********"));
            }
        });
        tblUsers.setItems(userObsList);
    }

    ObservableList<UserDetail> userObsList = FXCollections.observableArrayList();

    public void searchItemOnAction(KeyEvent keyEvent) {
    }

    public void CategorizingOnAction(ActionEvent event) {
    }
}
