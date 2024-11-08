package app.upryzing.crescent.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.Serializable

class Navigator {
    private val _sharedFlow =
        MutableSharedFlow<NavTarget>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun navigateTo(navTarget: NavTarget) {
        _sharedFlow.tryEmit(navTarget)
    }

    sealed class NavTarget {
        @Serializable
        data class Chat(val with: String, val type: String) : NavTarget()
        @Serializable
        data class MFADialog(val ticket: String) : NavTarget()
        @Serializable
        data object Login : NavTarget()
        @Serializable
        data object Settings : NavTarget()
        @Serializable
        data object StartConversation : NavTarget()
        @Serializable
        data object Profile : NavTarget()
        @Serializable
        data object Home : NavTarget()
    }
}