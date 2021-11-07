package view.TM;

public class ReservationsTm {
    private String resId;
    private String cusId;
    private String cusName;
    private String email;
    private int tp;
    private String context;
    private String date;

    public ReservationsTm(String resId, String cusId, String cusName, String email, int tp, String context, String date) {
        this.resId = resId;
        this.cusId = cusId;
        this.cusName = cusName;
        this.email = email;
        this.tp = tp;
        this.context = context;
        this.date = date;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
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
