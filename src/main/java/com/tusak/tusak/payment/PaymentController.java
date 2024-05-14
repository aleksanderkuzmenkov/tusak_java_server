package com.tusak.tusak.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.tusak.tusak.stripe.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/charge")
    public Charge chargeCard(@RequestParam String token, @RequestParam Double amount) throws StripeException {
        return stripeService.chargeNewCard(token, amount);
    }
}