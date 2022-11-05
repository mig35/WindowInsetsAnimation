package com.mig35.insets.controllers

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DispatchMode
import androidx.core.view.WindowInsetsCompat
import com.mig35.insets.InsetsConfig

open class InsetsControllerAnimatedTranslate(
    view: View,
    insetsConfig: InsetsConfig,
    @DispatchMode windowInsetsAnimationCallbackDispatchMode: Int =
        WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE,
) : InsetsControllerAnimated(
    view = view,
    insetsConfig = insetsConfig,
    windowInsetsAnimationCallbackDispatchMode = windowInsetsAnimationCallbackDispatchMode,
) {

    override fun applyInsetsDuringAnimation(insets: WindowInsetsCompat): WindowInsetsCompat {
        val deferredInsets = insets.getInsets(deferredInsetsTypes)
        val persistentInsets = insets.getInsets(persistentInsetsTypes)

        val diff = Insets.subtract(deferredInsets, persistentInsets)
            .let { Insets.max(it, Insets.NONE) }

        view.translationY = (diff.top - diff.bottom).toFloat()

        return insets
    }

    override fun onImeEnd() {
        super.onImeEnd()

        view.translationY = 0f
    }
}