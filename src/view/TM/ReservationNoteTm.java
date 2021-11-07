package view.TM;

public class ReservationNoteTm {
    private String resId;
    private String CustomerId;
    private String CustomerName;
    private String context;
    private String date;

    public ReservationNoteTm(String resId, String customerId, String date,String context) {
        this.resId = resId;
        CustomerId = customerId;
        this.context = context;
        this.date = date;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
