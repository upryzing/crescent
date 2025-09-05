package app.upryzing.crescent.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

/**
 * EventBus for decoupled communication using Kotlin Flows.
 * Allows subscribing to specific event types and unsubscribing.
 * Handler lambdas are stored by instance, so the same instance must be used for subscribe and unsubscribe.
 */
object EventBus {
    val _events = MutableSharedFlow<Any>()
    // Expose asSharedFlow for cases where direct collection might still be useful,
    // though primary interaction should be via subscribe/unsubscribe.
    val events = _events.asSharedFlow()

    // Scope for managing subscription coroutines.
    // SupervisorJob ensures that if one subscription's handler fails, it doesn't cancel the EventBus scope
    // or other subscription jobs.
    val busScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Stores active subscriptions: Key is (Event Type KClass, Handler Lambda instance), Value is the Job collecting events.
    val activeSubscriptions = mutableMapOf<Pair<KClass<*>, Any>, Job>()

    /**
     * Publishes an event to all subscribers of its type.
     * This is a suspend function as emitting to a SharedFlow can suspend.
     *
     * @param event The event object to publish.
     */
    suspend fun publish(event: Any) {
        _events.emit(event)
    }

    /**
     * Subscribes to events of type T.
     * The [onEvent] handler will be called for each event of type T.
     * The subscription remains active until explicitly unsubscribed using the same [onEvent] instance,
     * or until [clearAllSubscriptions] is called.
     *
     * @param T The type of event to subscribe to. Must be a non-nullable type.
     * @param onEvent The handler function to execute when an event of type T is received.
     *                It is crucial to pass the exact same instance of [onEvent] to [unsubscribe].
     */
    inline fun <reified T : Any> subscribe(noinline onEvent: (T) -> Unit) {
        val eventType = T::class
        // The key uses the handler's instance. Ensure the same instance is used for unsubscribing.
        val key = Pair(eventType, onEvent as Any)

        // Avoid duplicate subscriptions for the exact same handler instance and type.
        if (activeSubscriptions.containsKey(key)) {
            // Optionally, log or handle this case (e.g., if re-subscription should refresh something).
            return
        }

        val job = _events
            .filterIsInstance<T>() // Filters for events of the specified type T
            .onEach { event ->
                // The onEvent handler is called directly.
                // If onEvent is a long-running or blocking operation, or a suspend function
                // that should not block this collector, consider launching it in its own coroutine:
                // busScope.launch { onEvent(event) }
                // For most typical event handlers, direct invocation is appropriate.
                onEvent(event)
            }
            .launchIn(busScope) // Launches the collection coroutine in the EventBus's dedicated scope.

        activeSubscriptions[key] = job
    }

    /**
     * Unsubscribes the given [onEvent] handler from events of type T.
     * For unsubscription to be successful, [onEvent] must be the same instance
     * that was originally passed to [subscribe].
     *
     * @param T The type of event to unsubscribe from. Must be a non-nullable type.
     * @param onEvent The handler function instance that was originally passed to [subscribe].
     */
    inline fun <reified T : Any> unsubscribe(noinline onEvent: (T) -> Unit) {
        val eventType = T::class
        val key = Pair(eventType, onEvent as Any)

        activeSubscriptions.remove(key)?.cancel() // Cancel the job and remove it from the map.
    }

    /**
     * Clears all active subscriptions managed by this EventBus.
     * This will cancel all coroutines started by [subscribe].
     * As this EventBus is an object (singleton), its [busScope] typically lives as long as the application.
     * If there were a scenario where the EventBus itself needed to be fully "shut down" and restarted
     * (not typical for a singleton object), busScope.cancel() would also be necessary.
     */
    fun clearAllSubscriptions() {
        activeSubscriptions.values.forEach { it.cancel() } // Cancel each job.
        activeSubscriptions.clear()
    }
}
