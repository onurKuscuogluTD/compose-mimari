package com.example.bankingmigration.data.datasource

import android.content.Context
import com.example.bankingmigration.core.result.AppDispatchers
import com.example.bankingmigration.data.model.BankingMockDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BankingLocalDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val json: Json,
    private val dispatchers: AppDispatchers,
) {
    suspend fun getBankingPayload(): BankingMockDto =
        withContext(dispatchers.io) {
            val rawJson = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }
            json.decodeFromString<BankingMockDto>(rawJson)
        }

    private companion object {
        const val FILE_NAME = "banking_mock.json"
    }
}
