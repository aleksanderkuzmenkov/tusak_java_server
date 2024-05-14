package com.tusak.tusak.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey; // Инициализация ключа API после инъекции значения
    }

    public Charge chargeNewCard(String token, double amount) throws StripeException {
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount((long) (amount * 100)) // charge amount in cents
                .setCurrency("usd")
                .setDescription("Example charge")
                .setSource(token)
                .build();

        return Charge.create(params);
    }
}