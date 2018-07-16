package stripe.model.db;

public class Refund {

    int id;
    String id_stripe;
    int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_stripe() {
        return id_stripe;
    }

    public void setId_stripe(String id_stripe) {
        this.id_stripe = id_stripe;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
