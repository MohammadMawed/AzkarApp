package com.mohammadmawed.azkarapp

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class StringResource(
        val resId: Int
    )
}