package controller;

import db.DbConnection;
import model.Customer;
import model.Item;
import model.UserDetail;
import view.TM.BestUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {
    public ArrayList<UserDetail> getAllUsers(String accountType) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users WHERE AccountType='"+accountType+"'");
        ResultSet rst = stm.executeQuery();
        ArrayList<UserDetail> users = new ArrayList<>();
        while (rst.next()) {
            users.add(new UserDetail(
                    rst.getString(1),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return users;
    }

    public boolean getMails(String mail) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users Where Email = '"+mail+"'");
        ResultSet rst = stm.executeQuery();
        return rst.next();
    }

    public boolean saveUser(UserDetail c) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO users VALUES(?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getUserId());
        stm.setObject(2,c.getUserName());
        stm.setObject(3,c.getAddress());
        stm.setObject(4,c.getEmail());
        stm.setObject(5,c.getAccType());
        stm.setObject(6,c.getPassword());

        return stm.executeUpdate()>0;
    }

    public String generateUserId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT userId FROM users ORDER BY userId DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "U-0000"+tempId;
            }else if(tempId<99){
                return "U-000"+tempId;
            }else if(tempId<999){
                return "U-00"+tempId;
            }else if(tempId<9999){
                return "U-0"+tempId;
            }else{
                return "U-"+tempId;
            }

        }else{
            return "U-00001";
        }
    }

    //-----------------------------------------------------------------------------

    public ArrayList<UserDetail> getUsersToTable() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Users");
        ResultSet rst = stm.executeQuery();
        ArrayList<UserDetail> users = new ArrayList<>();
        while (rst.next()) {
            users.add(new UserDetail(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return users;
    }

    //------------------------------------------------------------------------------

    public ArrayList<BestUsers> getBestItemsUsers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("select users.UserName, SUM(orders.AbsoluteTotal)\n" +
                "from orders\n" +
                "inner join users on orders.UserId = users.UserId\n" +
                "group by users.userId;");
        ResultSet rst = stm.executeQuery();
        ArrayList<BestUsers> users = new ArrayList<>();
        while (rst.next()){
            users.add(new BestUsers(
                    rst.getString(1),
                    rst.getDouble(2)
            ));
        }
        return users;
    }
}
