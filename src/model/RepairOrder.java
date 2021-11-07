package model;

public class RepairOrder {
    private String billNo;
    private String customerId;
    private double billAmount;

    public RepairOrder(String billNo, String customerId, double billAmount) {
        this.billNo = billNo;
        this.customerId = customerId;
        this.billAmount = billAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }
}
