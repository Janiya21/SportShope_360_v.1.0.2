package model;

import java.util.ArrayList;

public class Order {

    private String OrderId;
    private String userId;
    private String customerId;
    private String date;
    private double totalAmount;
    private double fullDiscount;
    private double absoluteTotal;
    private double orderProfit;
    private ArrayList<ItemDetail> items;

    public Order(String OrderId, String userId, double fullDiscount, double absoluteTotal) {
        this.OrderId = OrderId;
        this.userId = userId;
        this.fullDiscount = fullDiscount;
        this.absoluteTotal = absoluteTotal;
    }

    public Order(String OrderId, String userId, String customerId, String date, double totalAmount, double fullDiscount,
                 double absoluteTotal, double orderProfit, ArrayList<ItemDetail> items) {
        this.OrderId = OrderId;
        this.userId = userId;
        this.customerId = customerId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.fullDiscount = fullDiscount;
        this.absoluteTotal = absoluteTotal;
        this.orderProfit = orderProfit;
        this.items = items;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFullDiscount() {
        return fullDiscount;
    }

    public void setFullDiscount(double fullDiscount) {
        this.fullDiscount = fullDiscount;
    }

    public double getAbsoluteTotal() {
        return absoluteTotal;
    }

    public void setAbsoluteTotal(double absoluteTotal) {
        this.absoluteTotal = absoluteTotal;
    }

    public double getOrderProfit() {
        return orderProfit;
    }

    public void setOrderProfit(double orderProfit) {
        this.orderProfit = orderProfit;
    }

    public ArrayList<ItemDetail> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDetail> items) {
        this.items = items;
    }
}
