package model;

public class Warranted_RepairOrders {
    private String billNo;
    private String orderId;
    private String itemCode;
    private String externalItemOrder;
    private String repairNote;
    private String receivedDate;
    private String returnedDate;
    private double externalCost;

    public Warranted_RepairOrders(String billNo, String orderId, String itemCode, String externalItemOrder, String repairNote, String receivedDate, String returnedDate, double externalCost) {
        this.billNo = billNo;
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.externalItemOrder = externalItemOrder;
        this.repairNote = repairNote;
        this.receivedDate = receivedDate;
        this.returnedDate = returnedDate;
        this.externalCost = externalCost;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getExternalItemOrder() {
        return externalItemOrder;
    }

    public void setExternalItemOrder(String externalItemOrder) {
        this.externalItemOrder = externalItemOrder;
    }

    public String getRepairNote() {
        return repairNote;
    }

    public void setRepairNote(String repairNote) {
        this.repairNote = repairNote;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(String returnedDate) {
        this.returnedDate = returnedDate;
    }

    public double getExternalCost() {
        return externalCost;
    }

    public void setExternalCost(double externalCost) {
        this.externalCost = externalCost;
    }
}
