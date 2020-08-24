import java.io.IOException;
import java.io.Serializable;

public class Company implements Serializable {
    private String coName;
    private String coId;
    private String ABN;
    private String address;
    private String url;

    public Company(String coName, String coId,String ABN, String address, String url) {
        this.coName = coName;
        this.coId = coId;
        this.ABN = ABN;
        this.address = address;
        this.url = url;
        System.out.println("A record has been registered for "+this.coName+
                "\nThe automatic company ID generated for this company is :"+ this.coId);
    }
}
