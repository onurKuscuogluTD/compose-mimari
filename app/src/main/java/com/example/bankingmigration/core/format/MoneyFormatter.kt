package com.example.bankingmigration.core.format

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

class MoneyFormatter @Inject constructor() {
    fun format(amountCents: Long, currencyCode: String): String {
        val amount = amountCents.toBigDecimal().movePointLeft(2)
        return NumberFormat.getCurrencyInstance(LOCALE).apply {
            currency = Currency.getInstance(currencyCode)
        }.format(amount)
    }

    private companion object {
        val LOCALE: Locale = Locale.Builder()
            .setLanguage("tr")
            .setRegion("TR")
            .build()
    }
}
