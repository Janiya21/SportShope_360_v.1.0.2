package model;

public class RepairItem {
    private String itemCode;
    private String billNo;
    private String desc;
    private String receivedDate;
    private String returnedDate;

    public RepairItem(String itemCode, String billNo, String desc, String receivedDate, String returnedDate) {
        this.itemCode = itemCode;
        this.billNo = billNo;
        this.desc = desc;
        this.receivedDate = receivedDate;
        this.returnedDate = returnedDate;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
