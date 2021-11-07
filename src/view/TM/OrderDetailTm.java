package view.TM;

public class OrderDetailTm {
    private String itemCode;
    private String desc;
    private int qty;
    private int warranty;
    private double total;
    private double profit;

    public OrderDetailTm(String itemCode, String desc, int qty, int warranty, double total, double profit) {
        this.itemCode = itemCode;
        this.desc = desc;
        this.qty = qty;
        this.warranty = warranty;
        this.total = total;
        this.profit = profit;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
