package model;

public class Agent {
    private String agentId;
    private String agentName;
    private int phoneNo;
    private String email;
    private String companyName;

    public Agent(String agentId, String agentName, int phoneNo, String email, String companyName) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.companyName = companyName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
