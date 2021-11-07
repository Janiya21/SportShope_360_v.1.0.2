package controller;

import db.DbConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController{

    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    public boolean customerExists(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE CustomerId = '"+customerId+"'").executeQuery();
        return rst.next();
    }

    public boolean saveCustomer(Customer c) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Customer VALUES(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getCustomerId());
        stm.setObject(2,c.getName());
        stm.setObject(3,c.getEmail());
        stm.setObject(4,c.getTelephone());

        return stm.executeUpdate()>0;
    }

    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE CustomerId='" + id + "'").executeUpdate() > 0;
    }

    public Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Customer.CustomerId, Customer.CustomerName, " +
                "Customer.Email, Customer.PhoneNumber FROM Customer\n" +
                "INNER JOIN Orders ON Orders.CustomerId=Customer.CustomerId\n" +
                "WHERE OrderId=?;");
        stm.setObject(1, id);

        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );

        } else {
            return null;
        }
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
        ResultSet rst = stm.executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();
        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return customers;
    }

    public String  getLastId() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT CustomerId FROM Customer " +
                "WHERE CustomerId=(SELECT max(CustomerId) FROM Customer)");
        ResultSet rst = stm.executeQuery();
        String id = null;
        while (rst.next()){
            id = rst.getString(1);
        }
        return id;
    }

    public String generateCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT CustomerId FROM Customer ORDER BY CustomerId DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "C-0000"+tempId;
            }else if(tempId<99){
                return "C-000"+tempId;
            }else if(tempId<999){
                return "C-00"+tempId;
            }else if(tempId<9999){
                return "C-0"+tempId;
            }else{
                return "C-"+tempId;
            }

        }else{
            return "O-00001";
        }
    }

    public boolean updateCustomer(Customer c, String code) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE Customer SET CustomerName=?, Email=?, PhoneNumber=? WHERE CustomerId=?");
        stm.setObject(1,c.getName());
        stm.setObject(2,c.getEmail());
        stm.setObject(3,c.getTelephone());
        stm.setObject(4,code);

        return stm.executeUpdate()>0;
    }
}
