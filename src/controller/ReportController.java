package controller;

import db.DbConnection;
import model.Customer;
import model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportController {
    public double getTotalOrderProfit(String date) throws SQLException, ClassNotFoundException {

        // Can Use keyword 'SUM'

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM orders WHERE orderDate=?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;
        while (rst.next()) {
            total+=rst.getDouble(8);

        }
        return total;
    }

    public double getTotalExternalChargeProfit(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM repairItem WHERE RecievedDate=?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;
        while (rst.next()) {
            total+=rst.getDouble(8);

        }
        return total;
    }

    public double getTotalWarrantedItemProfit(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM warrantedrepair_item WHERE ItemRecieved_Date=?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;
        while (rst.next()) {
            total+=rst.getDouble(8);

        }
        return total;
    }

    //--------------------------------------------------------------------------------------------------------

/*    public double getMonthly_TotalOrderProfit(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(OrderProfit) AS \"MonthlyProfit\"\n" +
                "FROM orders\n" +
                "WHERE monthname(curdate()) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total=rst.getDouble("MonthlyProfit");

        return total;
    }*/

/*    public double getMonthly_TotalExternalChargeProfit(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(externalCharge) AS \"MonthlyProfit\"\n" +
                "FROM repairItem\n" +
                "WHERE monthname(curdate()) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total+=rst.getDouble("MonthlyProfit");

        return total;
    }*/

/*    public double getMonthly_TotalWarrantedItemProfit(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(ExternalCost) AS \"MonthlyProfit\"\n" +
                "FROM warrantedrepair_item\n" +
                "WHERE monthname(curdate()) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total+=rst.getDouble("MonthlyProfit");

        return total;
    }*/

//---------------------------------------------------------------------------------------------------------------------

    public double getMonthly_TotalOrderProfit_toMonthNo(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(OrderProfit)\n" +
                "FROM orders\n" +
                "WHERE MONTH (OrderDate) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total=rst.getDouble(1);

        return total;
    }

    public double getMonthly_TotalExternalChargeProfit_toMonthNo(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(externalCharge)\n" +
                "FROM repairItem\n" +
                "WHERE MONTH (RecievedDate) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total+=rst.getDouble(1);

        return total;
    }

    public double getMonthly_TotalWarrantedItemProfit_toMonthNo(String date) throws SQLException, ClassNotFoundException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT  SUM(ExternalCost)\n" +
                "FROM warrantedrepair_item\n" +
                "WHERE MONTH (ItemRecieved_Date) =?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        double total = 0.0;

        while(rst.next())
            total+=rst.getDouble(1);

        return total;
    }

    public ResultSet getBrandDetails() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" select   agent.companyName, item.agentId, SUM(orderdetail.itemTotal) as 'TotalSales'\n" +
                " from orderdetail\n" +
                " inner join item on orderDetail.itemCode = item.itemCode\n" +
                " inner join agent on agent.agentId = item.agentId\n" +
                " group by agent.companyName\n" +
                " order by TotalSales asc limit 7");
        ResultSet rst = stm.executeQuery();
        return  rst;
    }
}

