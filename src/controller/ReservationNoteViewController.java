package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Customer;
import view.TM.ReservationNoteTm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ReservationNoteViewController implements Initializable {

    public Pane saveDetailPane;

    public JFXButton btnSaveReservation;

    @FXML
    private TableView<ReservationNoteTm> tblReservations;

    @FXML
    private TableColumn<?, ?> colResNo;

    @FXML
    private TableColumn<?, ?> colCustId;

    @FXML
    private TableColumn<?, ?> colContext;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private Label lblCustomerId;

    @FXML
    private Label lblReservationNo;

    @FXML
    private JFXDatePicker txtDate;

    @FXML
    private JFXTextArea txtContext;

    @FXML
    private JFXButton btnOpenMailSender;

    @FXML
    private JFXButton tbnGoBack;

    @FXML
    private FontAwesomeIconView fntDateWarning;

    @FXML
    private FontAwesomeIconView fntContextWarning;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblCustomerId.setText(CustomerDetailsFormController.selectedCustomer);

        colResNo.setCellValueFactory(new PropertyValueFactory<>("resId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        colContext.setCellValueFactory(new PropertyValueFactory<>("context"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            setCustomersToTable(new ReservationController().getAllReservations());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void goBackOnAction(ActionEvent event) {
        Stage primaryStage = (Stage) tbnGoBack.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    void saveReservationOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if(!lblReservationNo.getText().equals("X-XXXXX") ){
            if(txtDate.getValue()!=null && txtContext.getText()!=null){
                boolean dateOk=true;
                boolean contextOk=true;

                if(txtDate.getValue()==null){
                    fntDateWarning.setVisible(true);
                    dateOk=false;
                }else{
                    fntDateWarning.setVisible(false);
                }
                if(!isValidContext(txtContext.getText())){
                    fntContextWarning.setVisible(true);
                    contextOk=false;
                }else{
                    fntContextWarning.setVisible(false);
                }
                if(dateOk && contextOk){
                    ReservationNoteTm n1 = new ReservationNoteTm(lblReservationNo.getText(), lblCustomerId.getText(), String.valueOf(txtDate.getValue()), txtContext.getText());
                    if(new ReservationController().saveCustomer(n1)){
                        new Alert(Alert.AlertType.INFORMATION,"Note Saved Successful").show();
                        resObsList.add(n1);
                        tblReservations.refresh();
                        lblReservationNo.setText("X-XXXXX"); txtContext.setText(null);
                    }else{
                        new Alert(Alert.AlertType.ERROR,"Error. Not Saved").show();
                    }
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Please Fll All the Fields..").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Generate a Reservation No").show();
        }
    }

    @FXML
    void generateNoteId(ActionEvent event) throws SQLException, ClassNotFoundException {
        lblReservationNo.setText(new ReservationController().generateNoteId());
    }

    private void setCustomersToTable(ArrayList<ReservationNoteTm> reservations) {
        reservations.forEach(e->{
            resObsList.add(
                    new ReservationNoteTm(e.getResId(),e.getCustomerId(),e.getContext(),e.getDate()));
        });
        tblReservations.setItems(resObsList);
    }

    ObservableList<ReservationNoteTm> resObsList = FXCollections.observableArrayList();

    public static boolean isValidContext(String note) {
        final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z\\s]{2,100}$?");
        return EMAIL_REGEX.matcher(note).matches();
    }
}
