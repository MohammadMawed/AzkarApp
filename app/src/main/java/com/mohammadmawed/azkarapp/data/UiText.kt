package com.mohammadmawed.azkarapp.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StyleRes


sealed class UiText{
    data class DynamicString(val value: String): UiText()
    class StringResource(
        @StyleRes val resID: Int,
        vararg var args: Any
    ):UiText()

    @SuppressLint("ResourceType")
    fun asString(context: Context): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> context.getString(resID, *args)
        }
    }
}
