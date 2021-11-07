package controller;

import db.DbConnection;
import model.Customer;
import view.TM.ReservationNoteTm;
import view.TM.ReservationsTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationController {

    public ArrayList<ReservationNoteTm> getAllReservations() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM reservationnote");
        ResultSet rst = stm.executeQuery();
        ArrayList<ReservationNoteTm> reservations = new ArrayList<>();
        while (rst.next()) {
            reservations.add(new ReservationNoteTm(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(4),
                    rst.getString(3)
            ));
        }
        return reservations;
    }

    public boolean saveCustomer(ReservationNoteTm c) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="Insert into reservationnote values(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getResId());
        stm.setObject(2,c.getCustomerId());
        stm.setObject(3,c.getDate());
        stm.setObject(4,c.getContext());

        return stm.executeUpdate()>0;
    }

    public String generateNoteId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT ReservationNo FROM reservationnote ORDER BY ReservationNo DESC LIMIT 1").executeQuery();

        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "N-0000"+tempId;
            }else if(tempId<99){
                return "N-000"+tempId;
            }else if(tempId<999){
                return "N-00"+tempId;
            }else if(tempId<9999){
                return "N-0"+tempId;
            }else{
                return "N-"+tempId;
            }

        }else{
            return "N-00001";
        }
    }

    public ArrayList<ReservationsTm> getAllReservation_WithCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("select reservationnote.ReservationNo, reservationnote.CustomerId, customer.CustomerName, customer.email, customer.phoneNumber, " +
                "reservationNote.contexts, reservationNote.ReservationDate\n" +
                "from reservationNote\n" +
                "inner join customer on reservationNote.CustomerId = customer.CustomerId;");
        ResultSet rst = stm.executeQuery();
        ArrayList<ReservationsTm> reservations = new ArrayList<>();
        while (rst.next()) {
            reservations.add(new ReservationsTm(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return reservations;
    }

}
