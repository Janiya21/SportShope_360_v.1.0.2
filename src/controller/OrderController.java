package controller;

import db.DbConnection;
import model.*;
import view.TM.OrderDetailTm;
import view.TM.RepairItemTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderController {
    public ArrayList<Item> getAllOrders() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getInt(6),
                    rst.getDouble(7),
                    rst.getDouble(8),
                    rst.getDouble(9)
            ));
        }
        return items;
    }

    public ArrayList<OrderDetailTm> getItem(String code) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Item.ItemCode, Item.Description, orderDetail.OrderQty, " +
                "Item.warrantyPeriod,orderDetail.ItemTotal,orderDetail.ItemProfit\n" +
                "FROM Item INNER JOIN orderDetail ON Item.ItemCode=orderDetail.ItemCode WHERE OrderId = ?;");
        stm.setObject(1, code);

        ResultSet rst = stm.executeQuery();

        ArrayList<OrderDetailTm> ord = new ArrayList<>();
        while (rst.next()) {
            ord.add(new OrderDetailTm(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getInt(4),
                    rst.getDouble(5),rst.getDouble(6)));
        }
        return ord;
    }

    public ArrayList<Order> getOrderDetails() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM `Orders` ORDER BY orderId DESC"
        ).executeQuery();
        ArrayList<Order> ord = new ArrayList<>();
        while (rst.next()) {
            ord.add(new Order(rst.getString(1),rst.getString(2),rst.getDouble(6),rst.getDouble(7)));
        }
        return ord;
    }

    public double getUnitProfit(String itemCode) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT UnitProfit FROM Item WHERE ItemCode='"+itemCode+"'");
        ResultSet rst = stm.executeQuery();
        double profit=0.0;
        while (rst.next()) {
            profit = rst.getDouble("UnitProfit");
        }
        return profit;
    }

    public String generateOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "O-0000"+tempId;
            }else if(tempId<99){
                return "O-000"+tempId;
            }else if(tempId<999){
                return "O-00"+tempId;
            }else if(tempId<9999){
                return "O-0"+tempId;
            }else{
                return "O-"+tempId;
            }

        }else{
            return "O-00001";
        }
    }

    public boolean placeOrder(Order order) {
        Connection con=null;
        try {
            con= DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm =con.prepareStatement("INSERT INTO `Orders` VALUES(?,?,?,?,?,?,?,?)");

            stm.setObject(1, order.getOrderId());
            stm.setObject(2, order.getUserId());
            stm.setObject(3, order.getCustomerId());
            stm.setObject(4, order.getDate());
            stm.setObject(5, order.getTotalAmount());
            stm.setObject(6, order.getFullDiscount());
            stm.setObject(7, order.getAbsoluteTotal());
            stm.setObject(8, order.getOrderProfit());

            if (stm.executeUpdate() > 0){
                if (saveOrderDetail(order.getOrderId(), order.getItems())){
                    con.commit();
                    return true;
                }else{
                    con.rollback();
                    return false;
                }
            }else{
                con.rollback();
                return false;
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                assert con != null;
                con.setAutoCommit(true);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

    private boolean saveOrderDetail(String orderId, ArrayList<ItemDetail> items) throws SQLException, ClassNotFoundException {
        for (ItemDetail temp : items) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `OrderDetail` VALUES(?,?,?,?,?)");

            stm.setObject(1, orderId);
            stm.setObject(2, temp.getItemCode());
            stm.setObject(3, temp.getOrderQty());
            stm.setObject(4, temp.getItemTot());
            stm.setObject(5, temp.getItemProfit());

            if (stm.executeUpdate() > 0) {

                if(updateQty(temp.getItemCode(), temp.getOrderQty())){

                }else{
                    return false;
                }

            } else {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement
                ("UPDATE Item SET QtyOnHand=(QtyOnHand-" + qty + ") WHERE ItemCode='" + itemCode + "'");
        return stm.executeUpdate()>0;
    }

    //------------------------------------------------------------------------------------------------------------------

    public ArrayList<Warranted_RepairOrders> getWarrantedItem_Orders() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM WarrantedRepair_Item");
        ResultSet rst = stm.executeQuery();
        ArrayList<Warranted_RepairOrders> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Warranted_RepairOrders(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getDouble(8)
            ));
        }
        return items;
    }

    public String generateBillNo() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT BillNumber FROM WarrantedRepair_Item ORDER BY BillNumber DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "B-0000"+tempId;
            }else if(tempId<99){
                return "B-000"+tempId;
            }else if(tempId<999){
                return "B-00"+tempId;
            }else if(tempId<9999){
                return "B-0"+tempId;
            }else{
                return "B-"+tempId;
            }

        }else{
            return "B-00001";
        }
    }

    public boolean saveWarrantedOrder(Warranted_RepairOrders i) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO warrantedrepair_item VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,i.getBillNo());
        stm.setObject(2,i.getOrderId());
        stm.setObject(3,i.getItemCode());
        stm.setObject(4,i.getExternalItemOrder());
        stm.setObject(5,i.getRepairNote());
        stm.setObject(6,i.getReceivedDate());
        stm.setObject(7,i.getReturnedDate());
        stm.setObject(8,i.getExternalCost());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Order> getRelevantOrder(String word) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE orderId LIKE '%"+word+"%'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (rst.next()){
            orders.add(new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(6),
                    rst.getDouble(7)
            ));
        }
        return orders;
    }

    public ArrayList<Order> getRelevantDateItems(String date) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders" +
                " WHERE orderDate='"+date+"'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (rst.next()){
            orders.add(new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(6),
                    rst.getDouble(7)
            ));
        }
        return orders;
    }

    //------------------------------------------------------------------------------------------------------

    public ArrayList<RepairItemTm> getRepairOrderDetails() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM RepairItem");
        ResultSet rst = stm.executeQuery();
        ArrayList<RepairItemTm> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new RepairItemTm(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getDouble(7),
                    rst.getDouble(8),
                    rst.getDouble(7)+rst.getDouble(8)
            ));
        }
        return items;
    }

    public ArrayList<RepairItemTm> srchRepairBillNo(String word) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM RepairItem WHERE Repair_ItemCode LIKE '%"+word+"%'");
        ResultSet rst = stm.executeQuery();
        ArrayList<RepairItemTm> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new RepairItemTm(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getDouble(7),
                    rst.getDouble(8),
                    rst.getDouble(7)+rst.getDouble(8)
            ));
        }
        return items;
    }


    public String generateRepairBillNo() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT BillNumber FROM RepairItem ORDER BY BillNumber DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "R-0000"+tempId;
            }else if(tempId<99){
                return "R-000"+tempId;
            }else if(tempId<999){
                return "R-00"+tempId;
            }else if(tempId<9999){
                return "R-0"+tempId;
            }else{
                return "R-"+tempId;
            }

        }else{
            return "R-00001";
        }
    }

    public boolean placeRepairOrder(RepairOrder repairOrder,RepairItemTm repairItemTm) {
        Connection con=null;
        try {
            con= DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm =con.prepareStatement("INSERT INTO RepairOrders VALUES(?,?,?)");

            stm.setObject(1, repairOrder.getBillNo());
            stm.setObject(2, repairOrder.getCustomerId());
            stm.setObject(3, repairOrder.getBillAmount());

            if (stm.executeUpdate() > 0){
                if (saveRepairItems(repairItemTm.getBillNo(),repairItemTm)){
                    con.commit();
                    return true;
                }else{
                    con.rollback();
                    return false;
                }
            }else{
                con.rollback();
                return false;
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                assert con != null;
                con.setAutoCommit(true);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

    private boolean saveRepairItems(String billNo, RepairItemTm repairItemTm) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO RepairItem VALUES(?,?,?,?,?,?,?,?)");

        stm.setObject(1, repairItemTm.getItemCode());
        stm.setObject(2, billNo);
        stm.setObject(3, repairItemTm.getExternalOrderId());
        stm.setObject(4, repairItemTm.getRepairNote());
        stm.setObject(5, repairItemTm.getReceivedDate());
        stm.setObject(6, repairItemTm.getReturnDate());
        stm.setObject(7, repairItemTm.getExternalOrder_cost());
        stm.setObject(8, repairItemTm.getExternalCharge());


        if (stm.executeUpdate() > 0) {
            System.out.println("ff");
        } else {
            return false;
        }
        return true;
    }
}
