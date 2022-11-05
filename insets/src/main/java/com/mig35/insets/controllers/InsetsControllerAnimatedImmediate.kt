package com.mig35.insets.controllers

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.mig35.insets.BottomInsetConfig
import com.mig35.insets.InsetsConfig

open class InsetsControllerAnimatedImmediate(
    view: View,
    insetsConfig: InsetsConfig,
    @InsetsType protected val deferredInsetsTypes: Int = WindowInsetsCompat.Type.ime(),
) : InsetsControllerStatic(
    view = view,
    insetsConfig = insetsConfig,
) {

    override fun onApplyBottomWindowInsets(windowInsets: WindowInsetsCompat, bottomConfig: BottomInsetConfig) {
        val persistentInsets = windowInsets.getInsets(persistentInsetsTypes)
        val deferredInsets = windowInsets.getInsets(deferredInsetsTypes)

        val insets = Insets.max(persistentInsets, deferredInsets)

        applyBottomWindowInsets(insets, bottomConfig)
    }
}