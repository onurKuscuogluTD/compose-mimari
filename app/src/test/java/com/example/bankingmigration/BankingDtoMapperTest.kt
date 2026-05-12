package com.example.bankingmigration

import com.example.bankingmigration.data.mapper.toDomain
import com.example.bankingmigration.data.model.BankingMockDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class BankingDtoMapperTest {

    @Test
    fun `json payload parses and maps to domain`() {
        val dto = Json.decodeFromString<BankingMockDto>(RAW_JSON)

        val dashboard = dto.toDomain()

        assertEquals("user-001", dashboard.user.id)
        assertEquals("acc-try-main", dashboard.accounts.first().id)
        assertEquals("rec-001", dashboard.recentRecipients.first().id)
        assertEquals("act-001", dashboard.suggestedActions.first().id)
    }

    private companion object {
        const val RAW_JSON = """
            {
              "user": {
                "id": "user-001",
                "name": "Onur Kuscuoglu",
                "segment": "Premium"
              },
              "accounts": [
                {
                  "id": "acc-try-main",
                  "name": "Vadesiz TL Hesabi",
                  "iban": "TR12 0001 0000 0000 0000 0001",
                  "currency": "TRY",
                  "balanceCents": 12543075,
                  "type": "Current"
                }
              ],
              "recentRecipients": [
                {
                  "id": "rec-001",
                  "displayName": "Ayse Demir",
                  "iban": "TR44 0001 0000 0000 0000 1001",
                  "bankName": "Ornek Bank"
                }
              ],
              "suggestedActions": [
                {
                  "id": "act-001",
                  "title": "Fatura talimatlarini kontrol et",
                  "description": "Yaklasan otomatik odemelerini incele."
                }
              ]
            }
        """
    }
}
