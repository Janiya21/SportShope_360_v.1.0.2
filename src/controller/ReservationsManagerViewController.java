package controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import view.TM.ReservationsTm;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservationsManagerViewController implements Initializable {
    @FXML
    private TableView<ReservationsTm> tblReservations;

    @FXML
    private TableColumn<?, ?> colResId;

    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colTp;

    @FXML
    private TableColumn<?, ?> colContext;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private StackedBarChart<?, ?> barchart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private JFXButton btnSendMail;

    ReservationsTm selectedRec=null;
    int rowSelected=-1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        XYChart.Series set1 = new XYChart.Series<>();

        try {
            ResultSet brandsResultSet = new ReportController().getBrandDetails();
            while(brandsResultSet.next()){
                set1.getData().add(new XYChart.Data(brandsResultSet.getDouble(3),brandsResultSet.getString(1)));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        barchart.getData().addAll(set1);

        // table

        colResId.setCellValueFactory(new PropertyValueFactory<>("resId"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTp.setCellValueFactory(new PropertyValueFactory<>("tp"));
        colContext.setCellValueFactory(new PropertyValueFactory<>("context"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            setCustomersToTable(new ReservationController().getAllReservation_WithCustomers());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRec=newValue;
            rowSelected=tblReservations.getSelectionModel().selectedIndexProperty().getValue();
        });
    }

    ObservableList<ReservationsTm> reservationsObsList = FXCollections.observableArrayList();

    @FXML
    void SendMailOnAction(ActionEvent event) throws IOException {
        if(rowSelected !=-1){
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/EmailSenderView.fxml"));
            Parent parent =loader.load();
            EmailSenderViewController controller = loader.<EmailSenderViewController>getController();
            controller.txtMailTo.setText(selectedRec.getEmail());
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Select a Row").show();
        }
    }

    private void setCustomersToTable(ArrayList<ReservationsTm> reservations) {
        reservations.forEach(e->{
            reservationsObsList.add(
                    new ReservationsTm(e.getResId(),e.getCusName(),e.getResId(),e.getEmail(),e.getTp(),e.getContext(),e.getDate()));
        });
        tblReservations.setItems(reservationsObsList);
    }
}
