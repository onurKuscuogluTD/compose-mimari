package com.example.bankingmigration.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <E : MviEffect> CollectEffectWithLifecycle(
    effect: Flow<E>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEffect: suspend (E) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEffect by rememberUpdatedState(onEffect)

    LaunchedEffect(effect, lifecycleOwner, minActiveState) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            effect.collect { currentOnEffect(it) }
        }
    }
}
