package com.jse.proyectoretotecnico.proxy;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class PaymentBehaviorClientMock implements PaymentBehaviorClient{
    private final Random random = new Random();

    @Override
    public String getPaymentBehavior(String uniqueCode, double amount) {
        List<String> behaviors = Arrays.asList("CASH", "CREDIT_CARD_DIRECT", "CREDIT_CARD_INSTALLMENTS");
        int randomIndex = random.nextInt(behaviors.size());
        return behaviors.get(randomIndex);
    }
}
