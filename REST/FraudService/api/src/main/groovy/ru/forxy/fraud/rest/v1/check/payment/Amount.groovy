package ru.forxy.fraud.rest.v1.check.payment

import ru.forxy.fraud.rest.v1.check.Entity

class Amount extends Entity {
    BigDecimal value;
    BigDecimal usdValue;
    String currency;
}
