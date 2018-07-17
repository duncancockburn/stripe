package stripe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stripe.exceptions.InvalidAPIKeyException;
import stripe.model.db.Transaction;
import stripe.model.db.UserDb;
import stripe.model.stripeResponse.ChargeRes;
import stripe.model.stripeResponse.RefundRes;
import stripe.service.SecurityService;
import stripe.service.StripeService;

@RestController
@RequestMapping ("/stripe")
public class Controller {

    @Autowired
    StripeService service;

    @Autowired
    SecurityService securityService;

    @PostMapping("/user")
    public UserDb createUser (@RequestBody UserDb userDb){
        UserDb newuser = service.createNewUser(userDb);
        return newuser;
    }

    @DeleteMapping("/transactions")
    public Transaction refundTotal (@RequestParam ("id") Integer id,
                                    @RequestParam("apiKey")String apiKey)throws InvalidAPIKeyException{
        if(securityService.authentic(apiKey)){
            Transaction response = service.refundTotal(id);
            return response;
        }
        else {
            throw new InvalidAPIKeyException("APIKey validation fail");
        }
    }

    @PatchMapping("/transactions")
    public Transaction refundPartial (@RequestParam ("id") Integer id,
                                      @RequestParam ("amount") Integer amount,
                                      @RequestParam("apiKey")String apiKey) throws InvalidAPIKeyException{
        if(securityService.authentic(apiKey)) {
            Transaction response = service.refundOneTransactionPartially(id, amount);
            return response;
        }
        else {
            throw new InvalidAPIKeyException("APIKey validation fail");
        }
    }

    @GetMapping("/transactions")
    public Transaction findTransaction (@RequestParam (value = "id", required = false) Integer id,
                                        @RequestParam(value = "id_stripe", required = false) String id_stripe,
                                        @RequestParam("apiKey")String apiKey) throws InvalidAPIKeyException{
        if(securityService.authentic(apiKey)) {
            return service.findTransaction(id,id_stripe);
        }
        else {
            throw new InvalidAPIKeyException("APIKey validation fail");
        }
    }

    @PostMapping("/transactions")
    public Transaction creatingTransaction(@RequestBody Transaction trans,
                                           @RequestParam("apiKey")String apiKey) throws InvalidAPIKeyException{
        if(securityService.authentic(apiKey)) {
            Transaction nova = null;
            nova = service.creatingFirstTransaction(trans);
            return nova;
        }
        else {
            throw new InvalidAPIKeyException("APIKey validation fail");
        }
    }

    @RequestMapping("/test")
    public ChargeRes test (){
        ChargeRes charge = null;
        return  charge = service.testApi();
    }

    @RequestMapping("/RefundDB")
    public RefundRes refundTransaction(
            @RequestParam("id") String id,
            @RequestParam(  value = "amount", required = false) Integer amount) {
            if (amount == null){
                amount = -1;
            }
        return service.refundTransaction(id, amount);
    }

}
