package stripe.model.db;

public class Transaction {
    int id;
    int amount;
    String currency;
    String source;
    String receipt_email;
    String id_stripe;
    long amount_refunded;
    boolean captured;
    boolean refund;
    boolean paid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReceipt_email() {
        return receipt_email;
    }

    public void setReceipt_email(String receipt_email) {
        this.receipt_email = receipt_email;
    }

    public String getId_stripe() {
        return id_stripe;
    }

    public void setId_stripe(String id_stripe) {
        this.id_stripe = id_stripe;
    }

    public long getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(long amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isRefund() {
        return refund;
    }

    public void setRefund(boolean refund) {
        this.refund = refund;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
