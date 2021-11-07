package model;

public class ItemDetail {
    private String itemCode;
    private int orderQty;
    private double itemTot;
    private double itemProfit;

    public ItemDetail(String itemCode, int orderQty, double itemTot, double itemProfit) {
        this.itemCode = itemCode;
        this.orderQty = orderQty;
        this.itemTot = itemTot;
        this.itemProfit = itemProfit;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getItemTot() {
        return itemTot;
    }

    public void setItemTot(double itemTot) {
        this.itemTot = itemTot;
    }

    public double getItemProfit() {
        return itemProfit;
    }

    public void setItemProfit(double itemProfit) {
        this.itemProfit = itemProfit;
    }
}
