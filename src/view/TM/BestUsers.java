package view.TM;

public class BestUsers {
    private String userId;
    private double tot;

    public BestUsers(String userId, double tot) {
        this.userId = userId;
        this.tot = tot;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTot() {
        return tot;
    }

    public void setTot(double tot) {
        this.tot = tot;
    }
}
