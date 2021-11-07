package model;

public class Item {
    private String itemCode;
    private String agentId;
    private String desc;
    private String category;
    private int warrantyMonths;
    private int qtyOnHand;
    private double supPrice;
    private double unitPrice;
    private double unitProfit;

    public Item(String itemCode, String desc) {
        this.itemCode = itemCode;
        this.desc = desc;
    }

    public Item(String desc, double unitProfit) {
        this.desc = desc;
        this.unitProfit = unitProfit;
    }

    public Item(int warrantyMonths, int qtyOnHand, double unitPrice) {
        this.warrantyMonths = warrantyMonths;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }

    public Item(String itemCode, String agentId, String desc, String category, int warrantyMonths, int qtyOnHand, double supPrice,
                double unitPrice, double unitProfit) {
        this.itemCode = itemCode;
        this.agentId = agentId;
        this.desc = desc;
        this.category = category;
        this.warrantyMonths = warrantyMonths;
        this.qtyOnHand = qtyOnHand;
        this.supPrice = supPrice;
        this.unitPrice = unitPrice;
        this.unitProfit = unitProfit;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        itemCode = itemCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyYear) {
        this.warrantyMonths = warrantyYear;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getSupPrice() {
        return supPrice;
    }

    public void setSupPrice(double supPrice) {
        this.supPrice = supPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitProfit() {
        return unitProfit;
    }

    public void setUnitProfit(double unitProfit) {
        this.unitProfit = unitProfit;
    }
}
