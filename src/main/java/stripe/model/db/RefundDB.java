package stripe.model.db;

public class RefundDB {

    int id;
    String id_stripe;
    long amount;
    int transaction_id;

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
