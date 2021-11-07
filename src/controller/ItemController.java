package controller;

import db.DbConnection;
import model.Customer;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    public String generateItemId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT ItemCode FROM Item ORDER BY ItemCode DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "I-0000"+tempId;
            }else if(tempId<99){
                return "I-000"+tempId;
            }else if(tempId<999){
                return "I-00"+tempId;
            }else if(tempId<9999){
                return "I-0"+tempId;
            }else{
                return "I-"+tempId;
            }

        }else{
            return "I-00001";
        }
    }

    public boolean itemExists(String itemCode) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE itemCode = '"+itemCode+"'").executeQuery();
        return rst.next();
    }

    public ArrayList<Item> getRelevantItems(String word) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE Description LIKE '%"+word+"%'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()){
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

    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
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

    public List<String> getAgentIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Agent").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    public List<String> getCategories() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("select * from item group by categoryType").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(4)
            );
        }
        return ids;
    }

    public boolean saveItem(Item i) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Item VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,i.getItemCode());
        stm.setObject(2,i.getAgentId());
        stm.setObject(3,i.getDesc());
        stm.setObject(4,i.getCategory());
        stm.setObject(5,i.getWarrantyMonths());
        stm.setObject(6,i.getQtyOnHand());
        stm.setObject(7,i.getSupPrice());
        stm.setObject(8,i.getUnitPrice());
        stm.setObject(9,i.getUnitProfit());
        return stm.executeUpdate()>0;
    }

    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Item WHERE itemCode='"+code+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean updateItem(Item i,String code) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE Item SET AgentId=?, Description=?, CategoryType=?, WarrantyPeriod=?, QtyOnHand=?, UnitSuppliedPrice=?, UnitPrice=?, " +
                        "UnitProfit=? WHERE ItemCode=?");
        stm.setObject(1,i.getAgentId());
        stm.setObject(2,i.getDesc());
        stm.setObject(3,i.getCategory());
        stm.setObject(4,i.getWarrantyMonths());
        stm.setObject(5,i.getQtyOnHand());
        stm.setObject(6,i.getSupPrice());
        stm.setObject(7,i.getUnitPrice());
        stm.setObject(8,i.getUnitProfit());
        stm.setObject(9,code);

        return stm.executeUpdate()>0;
    }

    public List<String> getAllItemIds(String type) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT CategoryType FROM Item GROUP BY CategoryType").executeQuery();
        List<String> ids= new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    public ArrayList<Item> getCategorizedItems(String type) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE CategoryType='"+type+"'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()){
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

    //----------------------------------------------------------------------------------------------------------------------

    public String generateRepairItemCode() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT Repair_ItemCode FROM RepairItem ORDER BY Repair_ItemCode DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "T-0000"+tempId;
            }else if(tempId<99){
                return "T-000"+tempId;
            }else if(tempId<999){
                return "T-00"+tempId;
            }else if(tempId<9999){
                return "T-0"+tempId;
            }else{
                return "T-"+tempId;
            }

        }else{
            return "T-00001";
        }
    }

    //---------------------------------------------------------------------------------------------------------

    public ArrayList<Item> getBestItemsProfit() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("select item.Description, SUM(orderDetail.itemProfit)\n" +
                "from orderDetail\n" +
                "inner join item on orderDetail.itemCode = item.itemCode\n" +
                "Group by orderDetail.ItemProfit Order by orderDetail.itemProfit DESC Limit 6;");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()){
            items.add(new Item(
                    rst.getString(1),
                    rst.getDouble(2)
            ));
        }
        return items;
    }

}
