package ru.forxy.fraud.db.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Currency exchange information
 */
@Document
class Currency {
    @Id
    String symbol
    Double usdRate
    Date updateDate
}
