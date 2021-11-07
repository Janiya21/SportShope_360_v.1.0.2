package controller;

import db.DbConnection;
import model.Agent;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AgentController {

    public ArrayList<Agent> getAllAgents() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Agent");
        ResultSet rst = stm.executeQuery();
        ArrayList<Agent> agents = new ArrayList<>();
        while (rst.next()) {
            agents.add(new Agent(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return agents;
    }

    public boolean deleteAgent(String id) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Agent WHERE AgentId='" + id + "'").executeUpdate() > 0;
    }

    public boolean agentExists(String agentId) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Agent WHERE AgentId = '"+agentId+"'").executeQuery();
        return rst.next();
    }

    public boolean saveAgent(Agent c) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="INSERT INTO Agent VALUES(?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getAgentId());
        stm.setObject(2,c.getAgentName());
        stm.setObject(3,c.getPhoneNo());
        stm.setObject(4,c.getEmail());
        stm.setObject(5,c.getCompanyName());

        return stm.executeUpdate()>0;
    }

    public String generateAgentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT agentId FROM Agent ORDER BY agentId DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "A-0000"+tempId;
            }else if(tempId<99){
                return "A-000"+tempId;
            }else if(tempId<999){
                return "A-00"+tempId;
            }else if(tempId<9999){
                return "A-0"+tempId;
            }else{
                return "A-"+tempId;
            }

        }else{
            return "A-00001";
        }
    }
}
