import java.io.Serializable;

public class ProjOwner implements Serializable {
    private String firstName;
    private String surName;
    private String ownerID;
    private String role;
    private String email;
    private Company company;

    public ProjOwner(String firstName, String surName, String ownerID, String role, String email, Company company) {
        this.firstName = firstName;
        this.surName = surName;
        this.ownerID = ownerID;
        this.role = role;
        this.email = email;
        this.company = company;
        System.out.println("A record has registered for owner ID "+ this.ownerID+ "\n");
    }


    public Company getCompany() {
        return company;
    }
}
