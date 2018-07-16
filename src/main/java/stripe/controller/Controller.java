package stripe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stripe.model.db.Transaction;
import stripe.model.stripeResponse.ChargeRes;
import stripe.model.stripeResponse.RefundRes;
import stripe.service.StripeService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/stripe")
public class Controller {

    @Autowired
    StripeService service;

    @GetMapping("/transactions")
    public Transaction findTransaction (@RequestParam (value = "id", required = false) Integer id,
                                        @RequestParam(value = "id_stripe", required = false) String id_stripe){
        return service.findTransaction(id,id_stripe);
    }

    @PostMapping("/transactions")
    public Transaction creatingTransaction(@RequestBody Transaction trans ){
        Transaction nova = null;
        nova = service.creatingFirstTransaction(trans);
        return nova;
    }

    @RequestMapping("/test")
    public ChargeRes test (){
        ChargeRes charge = null;
        return  charge = service.testApi();
    }

    @RequestMapping("/Refund")
    public RefundRes refundTransaction(
            @RequestParam("id") String id,
            @RequestParam(  value = "amount", required = false) Integer amount) {
            if (amount == null){
                amount = -1;
            }
        return service.refundTransaction(id, amount);
    }
}
