package com.something.volkswagentechtask.navigation

sealed interface Destination {
    val route: String

    data object Home : Destination {
        override val route: String = "home"
    }

    data class Detail(val itemId: Long) : Destination {
        override val route: String = "$ROUTE_BASE/$itemId"

        companion object Companion {
            const val ROUTE_BASE = "detail"
            const val ARG_ITEM_ID = "itemId"
            const val ROUTE_PATTERN = "$ROUTE_BASE/{$ARG_ITEM_ID}"
        }
    }
}
