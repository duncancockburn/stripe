package stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stripe.mapper.MapperTransRef;
import stripe.model.db.RefundDB;
import stripe.model.db.Transaction;
import stripe.model.stripeResponse.ChargeRes;
import stripe.model.stripeResponse.RefundRes;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Autowired
    MapperTransRef mapper;

    public ChargeRes testApi(){

//        String url = "https://api.stripe.com";

        Stripe.apiKey = "sk_test_79GzgijpvKOdvfI57XPoC6gC";

        Map<String, Object> params = new HashMap<>();
        params.put("amount", 999);
        params.put("currency", "usd");
        params.put("source", "tok_visa");
        params.put("receipt_email", "jenny.rosen@sahdvaisdv.com");
        Charge charge = null;

        try {
             charge = Charge.create(params);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }


        ChargeRes response = new ChargeRes();

        response.setId(charge.getId());
        response.setAmount(Math.toIntExact(charge.getAmount()));
        response.setCaptured(charge.getCaptured());

        return response;

    }

    public RefundRes refundTransaction(String id, int amount) {
        Stripe.apiKey = "sk_test_79GzgijpvKOdvfI57XPoC6gC";

        Map<String, Object> params = new HashMap<>();
        params.put("charge", id);
        if (amount > 0) {
            params.put("amount", amount);
        }
        Refund refund = new Refund();
        try {
             refund = Refund.create(params);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

        RefundRes response = new RefundRes();


        response.setTransaction_id(id);
        response.setRefund_id(refund.getId());
        response.setStatus(refund.getStatus());
        response.setAmount(refund.getAmount());

        return response;
    }

    public RefundRes refundTransaction(String id) {
       return refundTransaction(id,-1);
    }

    public ChargeRes sendTransToStripe(Transaction trans) {

        Stripe.apiKey = "sk_test_79GzgijpvKOdvfI57XPoC6gC";

        Map<String, Object> params = new HashMap<>();
        params.put("amount", trans.getAmount());
        params.put("currency", trans.getCurrency());
        params.put("source", "tok_visa");
        params.put("receipt_email", trans.getReceipt_email());
        Charge charge = null;

        try {
            charge = Charge.create(params);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

        ChargeRes response = new ChargeRes();
        response.setId(charge.getId());
        response.setAmount(Math.toIntExact(charge.getAmount()));
        response.setCaptured(charge.getCaptured());

        return response;
    }

    public void createTransDB(Transaction trans) {
        mapper.insertTransaction(trans);
        System.out.println("transaction inserted in DB");
    }

    public Transaction creatingFirstTransaction (Transaction trans){
        ChargeRes charge = null;

        // sending the received transaction to stripe
        charge = sendTransToStripe(trans);

        Transaction nova = new Transaction();

        // getting the data from the received transaction
        nova.setAmount(trans.getAmount());
        nova.setCurrency(trans.getCurrency());
        nova.setReceipt_email(trans.getReceipt_email());
        nova.setSource(trans.getReceipt_email());

        // getting the data from Stripe response
        nova.setId_stripe(charge.getId());
        nova.setCaptured(charge.isCaptured());
        nova.setAmount_refunded(charge.getAmount_refunded());

        // recoding the transaction in the DB
        createTransDB(nova);
        // getting the transactions in the DB

        nova = findTransaction(null, nova.getId_stripe());

        return nova;
    }

    public Transaction findTransaction(Integer id, String id_stripe) {
        Transaction response = new Transaction();
        if (id == null){
            response = mapper.findPerTxID(-1, id_stripe);
        } else {
            response = mapper.findPerTxID(id, id_stripe);
        }
        return response;
    }

    public Transaction refundOneTransactionPartially(Integer id, Integer amount) {
        // finding the transaction in the database
        Transaction toBerefunded = findTransaction(id, null);
        RefundRes refundTemp = null;
        // check if refund is possible
        if (checkRefundPossible(toBerefunded,amount)) {
            // refund that transaction partially
            refundTemp = refundTransaction(toBerefunded.getId_stripe(), amount);
            // record refund on the refundDb
            recordRefund(refundTemp, id);
            // if the refund was success, then update the transaction on the database
            if (refundTemp.getStatus().equals("succeeded")) {
                // making a new request to Stripe to get the total amount refund per transaction
                ChargeRes chargeRes = new ChargeRes();
                try {
                    chargeRes = retriveCharge(toBerefunded.getId_stripe());
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                }
                // updating the transaction
                mapper.updateRefundAmount(id, chargeRes.getAmount_refunded(), chargeRes.isRefund());
                // getting the transaction back
                toBerefunded = findTransaction(id, null);
            }
        }
        return toBerefunded;
    }

    private boolean checkRefundPossible(Transaction toBerefunded, int amount) {
        if (toBerefunded.isRefund() == true){
            return false;
        }
        if (toBerefunded.getAmount() < (toBerefunded.getAmount_refunded() + amount)){
            return false;
        }
        return true;
    }

    private void recordRefund(RefundRes refunTemp, int id ) {
        // create a new refund
        RefundDB toBeRecordRefund = new RefundDB ();
        // copying from the response to the refund that will be record
        toBeRecordRefund.setAmount(refunTemp.getAmount());
        toBeRecordRefund.setId_stripe(refunTemp.getRefund_id());
        toBeRecordRefund.setTransaction_id(id);
        // recording in the Database
        mapper.insertRefund(toBeRecordRefund);
    }

    private ChargeRes retriveCharge(String id_stripe) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        //calling stripe api one more time
        Stripe.apiKey = "sk_test_79GzgijpvKOdvfI57XPoC6gC";
        Charge unicorn = null;
        // passing the id of the transaction that has been update
        unicorn = Charge.retrieve(id_stripe);
        ChargeRes chargeRes = new ChargeRes();
        chargeRes.setAmount_refunded(unicorn.getAmountRefunded());
        chargeRes.setRefund(unicorn.getRefunded());
        // giving back the response with the amount RefundDB update
        return chargeRes;
    }

    public Transaction refundTotal(Integer id) {
        // the total refund is a partial without the amount
        Transaction response = null;
        // amount is set to -1, so it does a complete refund
        response = refundOneTransactionPartially(id,-1);
        return response;
    }
}
