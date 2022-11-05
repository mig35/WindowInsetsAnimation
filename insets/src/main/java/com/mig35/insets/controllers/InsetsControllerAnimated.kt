package com.mig35.insets.controllers

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DispatchMode
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.mig35.insets.BottomInsetConfig
import com.mig35.insets.InsetsConfig

abstract class InsetsControllerAnimated(
    view: View,
    insetsConfig: InsetsConfig,
    @DispatchMode windowInsetsAnimationCallbackDispatchMode: Int,
    @InsetsType persistentInsetsTypes: Int = WindowInsetsCompat.Type.systemBars(),
    @InsetsType protected val deferredInsetsTypes: Int = WindowInsetsCompat.Type.ime(),
) : InsetsControllerStatic(
    view = view,
    insetsConfig = insetsConfig,
    persistentInsetsTypes = persistentInsetsTypes,
) {

    private var animationTypeMask: Int = -1

    private val windowInsetsAnimationCallback =
        object : WindowInsetsAnimationCompat.Callback(windowInsetsAnimationCallbackDispatchMode) {
            private lateinit var windowInsetsBeforeAnimation: WindowInsetsCompat

            override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                if (animationTypeMask == -1 && animation.typeMask and deferredInsetsTypes != 0) {
                    animationTypeMask = animation.typeMask
                    windowInsetsBeforeAnimation = latestWindowInsets
                }
            }

            override fun onStart(
                animation: WindowInsetsAnimationCompat,
                bounds: WindowInsetsAnimationCompat.BoundsCompat
            ): WindowInsetsAnimationCompat.BoundsCompat {
                if (animationTypeMask == animation.typeMask)
                    applyInsetsDuringAnimation(windowInsetsBeforeAnimation)

                return super.onStart(animation, bounds)
            }

            override fun onProgress(
                insets: WindowInsetsCompat,
                runningAnimations: List<WindowInsetsAnimationCompat>
            ): WindowInsetsCompat =
                if (runningAnimations.any { it.typeMask == animationTypeMask })
                    applyInsetsDuringAnimation(insets)
                else
                    insets

            override fun onEnd(animation: WindowInsetsAnimationCompat) {
                if (animationTypeMask == animation.typeMask) {
                    animationTypeMask = -1
                    onImeEnd()
                    onApplyWindowInsets(latestWindowInsets)
                }
            }
        }

    init {
        ViewCompat.setWindowInsetsAnimationCallback(view, windowInsetsAnimationCallback)
    }

    protected abstract fun applyInsetsDuringAnimation(insets: WindowInsetsCompat): WindowInsetsCompat

    /**
     * In this method you should reset all view state changes that were applied in @see InsetsControllerAnimated.applyInsetsDuringAnimation
     */
    protected open fun onImeEnd() {
    }

    override fun onApplyWindowInsets(windowInsets: WindowInsetsCompat) {
        super.onApplyWindowInsets(windowInsets.removeImeInsetsIfImeAnimationIsInProgress())
    }

    override fun onApplyBottomWindowInsets(windowInsets: WindowInsetsCompat, bottomConfig: BottomInsetConfig) {
        val persistentInsets = windowInsets.getInsets(persistentInsetsTypes)
        val deferredInsets = windowInsets.getInsets(deferredInsetsTypes)

        val insets = if (windowInsets.isVisible(deferredInsetsTypes)) deferredInsets else persistentInsets

        applyBottomWindowInsets(insets, bottomConfig)
    }

    override fun onConsumeWindowInsets(windowInsets: WindowInsetsCompat): WindowInsetsCompat =
        windowInsets

    private fun WindowInsetsCompat.removeImeInsetsIfImeAnimationIsInProgress(): WindowInsetsCompat =
        if (animationTypeMask == -1) this
        else removeImeInsets()

    private fun WindowInsetsCompat.removeImeInsets(): WindowInsetsCompat =
        WindowInsetsCompat.Builder(this)
            .setInsets(WindowInsetsCompat.Type.ime(), Insets.NONE)
            .setVisible(WindowInsetsCompat.Type.ime(), false)
            .build()
}