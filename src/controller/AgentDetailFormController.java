package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Agent;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AgentDetailFormController implements Initializable {
    @FXML
    public TextArea txtSubject;

    @FXML
    public AnchorPane rootAgents;

    @FXML
    public Label lblAgentId;

    @FXML
    private JFXTextField txtAgentName;

    @FXML
    private JFXTextField txtPhoneNo;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private TableView<Agent> tblAgentDetails;

    @FXML
    private TableColumn<?, ?> colAgentId;

    @FXML
    private TableColumn<?, ?> colAgentName;

    @FXML
    private TableColumn<?, ?> colAgentTelephone;

    @FXML
    private TableColumn<?, ?> colAgentMail;

    @FXML
    private TableColumn<?, ?> colAgentCompany;

    @FXML
    private TableColumn<?, ?> colRating;

    @FXML
    private Button btnLoadEmailPane;

    @FXML
    private JFXTextField txtReceiver;

    @FXML
    public Button btnGoBack;

    int cartSelectedRowForRemove = -1;
    String deleteAgent=null;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();

    final Pattern agent_REGEX = Pattern.compile("^[a-zA-Z\\s]{2,32}$?");
    final Pattern phoneNo_regex = Pattern.compile("^[0-9]{10}$?");
    final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colAgentId.setCellValueFactory(new PropertyValueFactory<>("agentId"));
        colAgentName.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        colAgentTelephone.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colAgentMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAgentCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));

        try {
            setAgentsToTable(new AgentController().getAllAgents());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        storeValidations();

        tblAgentDetails.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRowForRemove = (int) newValue;
        });

        tblAgentDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteAgent = newValue.getAgentId();
        });

    }

    ObservableList<Agent> agentObsList = FXCollections.observableArrayList();

    @FXML
    void addAgentOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(lblAgentId.getText().equals("X-XXXXX")){
            new Alert(Alert.AlertType.ERROR,"Please Refresh a Agent Id").show();
        }else{
            if(!txtAgentName.getText().isEmpty() && !txtCompany.getText().isEmpty() && !txtEmail.getText().isEmpty() && !txtPhoneNo.getText().isEmpty()){
                if(new AgentController().agentExists(lblAgentId.getText())){
                    new Alert(Alert.AlertType.ERROR,"This Agent Already Exists..").show();
                }else{
                    if(validate().isEmpty()){
                        Agent a1 = new Agent(
                                lblAgentId.getText(), txtAgentName.getText(), Integer.parseInt(txtPhoneNo.getText()), txtEmail.getText(),txtCompany.getText()
                        );

                        agentObsList.add(a1);

                        if(new AgentController().saveAgent(a1))
                            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                        else
                            new Alert(Alert.AlertType.ERROR, "Try Again..").show();
                    }
                }
            }else{
                new Alert(Alert.AlertType.ERROR, "Please Fill All Fields").show();
            }
        }
    }

    @FXML
    void deleteAgentOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (cartSelectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        }else{
            deleteCustomerFromTable(deleteAgent);
            agentObsList.remove(cartSelectedRowForRemove);
            tblAgentDetails.refresh();
        }
    }

    private void deleteCustomerFromTable(String agentId) throws SQLException, ClassNotFoundException {
        if (new AgentController().deleteAgent(agentId)){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    @FXML
    void generateIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new AgentController().generateAgentId();
        lblAgentId.setText(lastId);
    }

    @FXML
    void goBackOnAction(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("../view/AdminItemManageView.fxml");
        Parent load = FXMLLoader.load(resource);
        rootAgents.getChildren().clear();
        rootAgents.getChildren().add(load);
    }

    @FXML
    void loadEmailPaneOnAction(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/EmailSenderView.fxml"));
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

    private void setAgentsToTable(ArrayList<Agent> agents) {
        agents.forEach(e->{
            agentObsList.add(
                    new Agent(e.getAgentId(),e.getAgentName(),e.getPhoneNo(),e.getEmail(),e.getCompanyName()));
        });
        tblAgentDetails.setItems(agentObsList);
    }

    //---Valid Form-----------------------------------------------------------------------------------------------------

    private void storeValidations(){
        map.put(txtAgentName,agent_REGEX);
        map.put(txtCompany,agent_REGEX);
        map.put(txtPhoneNo,phoneNo_regex);
        map.put(txtEmail,EMAIL_REGEX);
    }

    public ArrayList<Object> validate(){
        ArrayList<Object> objTf = new ArrayList<>();
        for (TextField txtF : map.keySet()){
            Pattern regex = map.get(txtF);
            if(!regex.matcher(txtF.getText()).matches()){
                txtF.setStyle("-fx-border-color: red;");
                objTf.add(txtF);
            }else{
                txtF.setStyle("-fx-border-color: transparent;");
            }
        }
        return objTf;
    }
}
