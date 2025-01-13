import java.util.List;

public class Invoice {
    private String customer;
    private List<Performance> performances;

    public Invoice(final String customer, final List<Performance> performances) {
        this.customer = customer;
        this.performances = performances;
    }

    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }
}
