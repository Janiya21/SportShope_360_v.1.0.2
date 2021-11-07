package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Item;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AdminItemManageViewController implements Initializable {

    @FXML
    public Button brnAddNewAgent;

    @FXML
    public AnchorPane addItemRoot;

    @FXML
    public Label lblItemId;

    @FXML
    public JFXButton btnShowBestItems;

    @FXML
    public JFXTextField txtSearchItem;

    @FXML
    public JFXComboBox<String> cmbCategory;

    @FXML
    private JFXTextField txtDesc;

    public JFXComboBox<String> cmbAgentId;
    public JFXTextField txtCategory;
    public JFXTextField txtWarranty;
    public JFXTextField txtSuppliedPrice;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private TableView<Item> tblItemDetails;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colAgentId;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colWarranty;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSupPrice;

    @FXML
    private TableColumn<?, ?> colSellPrice;

    @FXML
    private TableColumn<?, ?> collCategory;

    @FXML
    private TableColumn<?, ?> colUnitProfit;

    int tblSelectedRowForRemove=-1;
    String deleteRowId;

    LinkedHashMap<TextField,Pattern> map = new LinkedHashMap();

    final Pattern desc_REGEX = Pattern.compile("^[A-Za-z0-9]\\w{2,64}?");
    final Pattern cat_REGEX = Pattern.compile("^[A-Za-z0-9]\\w{2,24}?");
    final Pattern war_REGEX = Pattern.compile("^[0-9]{10}$?");
    final Pattern Qty_REGEX = Pattern.compile("^[0-9]{10}$?");
    final Pattern Cost_REGEX = Pattern.compile("\\d{1,8}\\.\\d{0,8}?");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            List<String> ids = new ItemController().getAgentIds();
            ids.forEach(e->{
                cmbAgentId.getItems().add(e.toString());
            });
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        try {
            //cmbCategory.getItems().add("All");
            loadCategorizedItems();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colAgentId.setCellValueFactory(new PropertyValueFactory<>("agentId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colWarranty.setCellValueFactory(new PropertyValueFactory<>("warrantyMonths"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colSupPrice.setCellValueFactory(new PropertyValueFactory<>("supPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colUnitProfit.setCellValueFactory(new PropertyValueFactory<>("unitProfit"));
        collCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        try {
            setItemToTable(new ItemController().getAllItems());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        tblItemDetails.setItems(obList);

        tblItemDetails.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tblSelectedRowForRemove = (int) newValue;
            System.out.println(tblSelectedRowForRemove);
        });

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteRowId = newValue.getItemCode();
        });

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lblItemId.setText(newValue.getItemCode());
            cmbAgentId.getSelectionModel().clearSelection();
            cmbAgentId.setPromptText(newValue.getAgentId());
            txtCategory.setText(newValue.getCategory());
            txtDesc.setText(newValue.getDesc());
            txtWarranty.setText(String.valueOf(newValue.getWarrantyMonths()));
            txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
            txtSuppliedPrice.setText(String.valueOf(newValue.getSupPrice()));
            txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
        });

        // searchable textField

        try {
            TextFields.bindAutoCompletion(txtCategory,new ItemController().getCategories());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        storeValidations();
    }

    ObservableList<Item> obList = FXCollections.observableArrayList();

    @FXML
    void addItemOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if(lblItemId.getText().equals("X-XXXXX")){
            new Alert(Alert.AlertType.WARNING, "Please Generate ItemId..").show();
        }else{
            if(!txtWarranty.getText().equals("") && !txtDesc.getText().equals("")  && cmbAgentId.getValue()!=null  && !txtCategory.getText().equals("")  &&
                    !txtSuppliedPrice.getText().equals("")  && !txtQtyOnHand.getText().equals("")  && !txtUnitPrice.getText().equals("") ) {

                if(new ItemController().itemExists(lblItemId.getText())){
                    new Alert(Alert.AlertType.ERROR, "This Item Already Exists..").show();
                }
                else{
                    if(validate().isEmpty()){
                        Item i1 = new Item(
                                lblItemId.getText(),cmbAgentId.getValue(),txtDesc.getText(), txtCategory.getText(),Integer.parseInt(txtWarranty.getText()),
                                Integer.parseInt(txtQtyOnHand.getText()), Double.parseDouble(txtSuppliedPrice.getText()),Double.parseDouble(txtUnitPrice.getText()),
                                ( Double.parseDouble(txtUnitPrice.getText())-Double.parseDouble(txtSuppliedPrice.getText()))
                        );

                        obList.add(i1);

                        if(new ItemController().saveItem(i1))
                            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                        else
                            new Alert(Alert.AlertType.WARNING, "Try Again..").show();

                        tblItemDetails.refresh();
                    }
                }
            }else{
                new Alert(Alert.AlertType.ERROR,"Fill All Fields..").show();
            }
        }
    }

    @FXML
    void deleteItemOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (tblSelectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        }else{
            deleteItemFromTable(deleteRowId);
            obList.remove(tblSelectedRowForRemove);
            tblItemDetails.refresh();
        }
    }

    @FXML
    void updateItemOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (tblSelectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        }else{
            updateItemOfTable(lblItemId.getText());
            tblItemDetails.refresh();
        }
    }

    private void setItemToTable(ArrayList<Item> item) {
        item.forEach(e->{
            obList.add(
                    new Item(e.getItemCode(),e.getAgentId(),e.getDesc(),e.getCategory(),e.getWarrantyMonths(),e.getQtyOnHand(),e.getSupPrice(),
                    e.getUnitPrice(),e.getUnitProfit()));
        });
        tblItemDetails.setItems(obList);
    }

    private void deleteItemFromTable(String code) throws SQLException, ClassNotFoundException {
        if (new ItemController().deleteItem(code)){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private void updateItemOfTable(String code) throws SQLException, ClassNotFoundException {
        if(validate().isEmpty()){
            Item i1 = new Item(
                    lblItemId.getText(),cmbAgentId.getPromptText(),txtDesc.getText(), txtCategory.getText(),Integer.parseInt(txtWarranty.getText()),
                    Integer.parseInt(txtQtyOnHand.getText()), Double.parseDouble(txtSuppliedPrice.getText()),Double.parseDouble(txtUnitPrice.getText()),
                    ( Double.parseDouble(txtUnitPrice.getText())-Double.parseDouble(txtSuppliedPrice.getText()))
            );

            obList.remove(tblSelectedRowForRemove);
            obList.add((tblSelectedRowForRemove),i1);
            if (new ItemController().updateItem(i1,code))
                new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            else
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
        }
    }

    public void clearFieldsOnAction(ActionEvent event) {
        lblItemId.setText("X-XXXXX");
        txtUnitPrice.clear();
        txtCategory.clear();
        txtQtyOnHand.clear();
        txtSuppliedPrice.clear();
        cmbAgentId.setPromptText(null);
        txtWarranty.clear();
        txtDesc.clear();
    }

    public void addNewAgentOnAction(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/AgentDetailForm.fxml"));
        Parent parent =loader.load();
        AgentDetailFormController controller = loader.<AgentDetailFormController>getController();
        controller.btnGoBack.setDisable(false);
        addItemRoot.getChildren().clear();
        addItemRoot.getChildren().add(parent);
    }

    @FXML
    public void generateItemId(ActionEvent event) throws SQLException, ClassNotFoundException {
        String lastId = new ItemController().generateItemId();
        lblItemId.setText(lastId);
    }

    @FXML
    public void showBestItemsOnAction(ActionEvent event) throws IOException {
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../view/MostSoldItems.fxml"));
        assert root != null;
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    @FXML
    public void searchItemOnAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        String word = txtSearchItem.getText();
        System.out.println(txtSearchItem.getText());
        obList.clear();
        tblItemDetails.getItems().removeAll();
        setItemsToTable((new ItemController().getRelevantItems(word)));
    }

    @FXML
    public void CategorizingOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        obList.clear();
        tblItemDetails.getItems().removeAll();
        if(cmbCategory.getValue().equals("All")){
            setItemsToTable(new ItemController().getAllItems());
        }else{
            setCategorizedItems(new ItemController().getCategorizedItems(cmbCategory.getValue()));
        }
    }

    private void setItemsToTable(ArrayList<Item> items) {
        items.forEach(e->{
            if(e!=null){
                System.out.println("ite : " + e.getDesc());
                obList.add(
                        new Item(e.getItemCode(),e.getAgentId(),e.getDesc(),e.getCategory(),e.getWarrantyMonths(),
                                e.getQtyOnHand(),e.getSupPrice(),e.getUnitPrice(),e.getUnitProfit()));
            }
        });
        tblItemDetails.setItems(obList);
    }

    private void setCategorizedItems(ArrayList<Item> items){
        items.forEach(e->{
            if(e!=null){
                obList.add(
                        new Item(e.getItemCode(),e.getAgentId(),e.getDesc(),e.getCategory(),e.getWarrantyMonths(),
                                e.getQtyOnHand(),e.getSupPrice(),e.getUnitPrice(),e.getUnitProfit()));
            }
        });
        tblItemDetails.setItems(obList);
    }

    private void loadCategorizedItems() throws SQLException, ClassNotFoundException {
        List<String> itemIds = new ItemController().getAllItemIds(cmbCategory.getValue());
        cmbCategory.getItems().addAll(itemIds);
    }

    // Valid From ------------------------------------------------------------------------------------------------------

    private void storeValidations(){
        map.put(txtCategory,cat_REGEX);
        map.put(txtDesc,desc_REGEX);
        map.put(txtWarranty,war_REGEX);
        map.put(txtQtyOnHand,Qty_REGEX);
        map.put(txtSuppliedPrice,Cost_REGEX);
        map.put(txtUnitPrice,Cost_REGEX);
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
