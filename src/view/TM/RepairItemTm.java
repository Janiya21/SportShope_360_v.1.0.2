package view.TM;

public class RepairItemTm {
    private String itemCode;
    private String billNo;
    private String externalOrderId;
    private String repairNote;
    private String receivedDate;
    private String returnDate;
    private double externalOrder_cost;
    private double externalCharge;
    private double billTotal;

    public RepairItemTm(String itemCode, String billNo, String externalOrderId, String repairNote, String receivedDate,
                        String returnDate, double externalOrder_cost, double externalCharge, double billTotal) {
        this.itemCode = itemCode;
        this.billNo = billNo;
        this.externalOrderId = externalOrderId;
        this.repairNote = repairNote;
        this.receivedDate = receivedDate;
        this.returnDate = returnDate;
        this.externalOrder_cost = externalOrder_cost;
        this.externalCharge = externalCharge;
        this.billTotal=billTotal;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getExternalOrder_cost() {
        return externalOrder_cost;
    }

    public void setExternalOrder_cost(double externalOrder_cost) {
        this.externalOrder_cost = externalOrder_cost;
    }

    public double getExternalCharge() {
        return externalCharge;
    }

    public void setExternalCharge(double externalCharge) {
        this.externalCharge = externalCharge;
    }

    public double getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(double billTotal) {
        this.billTotal = billTotal;
    }
}
