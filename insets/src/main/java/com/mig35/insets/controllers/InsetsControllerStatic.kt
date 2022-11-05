package com.mig35.insets.controllers

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.graphics.Insets
import androidx.core.view.*
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import com.mig35.insets.*

open class InsetsControllerStatic(
    protected val view: View,
    private val insetsConfig: InsetsConfig,
    private val onInsetsUpdated: ((insets: WindowInsetsCompat) -> Unit)? = null,
    @InsetsType protected val persistentInsetsTypes: Int = WindowInsetsCompat.Type.systemBars(),
) {

    protected lateinit var latestWindowInsets: WindowInsetsCompat
        private set

    private val onApplyWindowInsetsListener = OnApplyWindowInsetsListener { _, windowInsets ->
        latestWindowInsets = windowInsets

        onApplyWindowInsets(windowInsets)

        onConsumeWindowInsets(windowInsets)
    }

    protected val initialPadding = Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
    protected val initialMargin = (view.layoutParams as? ViewGroup.MarginLayoutParams)
        ?.let { Rect(it.leftMargin, it.topMargin, it.rightMargin, it.bottomMargin) }
        ?: Rect(0, 0, 0, 0)

    init {
        ViewCompat.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener)
        view.requestApplyInsetsWhenAttached()
    }

    protected open fun onApplyWindowInsets(windowInsets: WindowInsetsCompat) {
        if (insetsConfig.topConfig != null)
            onApplyTopWindowInsets(windowInsets, insetsConfig.topConfig)
        if (insetsConfig.bottomConfig != null)
            onApplyBottomWindowInsets(windowInsets, insetsConfig.bottomConfig)

        onInsetsUpdated?.invoke(windowInsets)
    }

    protected open fun onApplyTopWindowInsets(windowInsets: WindowInsetsCompat, topConfig: TopInsetConfig) {
        applyTopWindowInsets(windowInsets.getInsets(persistentInsetsTypes), topConfig)
    }

    protected open fun applyTopWindowInsets(insets: Insets, topConfig: TopInsetConfig) {
        when (topConfig.persistentType) {
            PersistentInsetType.None -> {
                // nothing
            }
            PersistentInsetType.Translate -> {
                view.translationY = insets.top.toFloat()
            }
            PersistentInsetType.Padding -> {
                view.updatePadding(top = initialPadding.top + insets.top)
            }
            PersistentInsetType.Margin -> {
                view.updateLayoutParams<MarginLayoutParams> { topMargin = initialMargin.top + insets.top }
            }
        }
    }

    protected open fun onApplyBottomWindowInsets(windowInsets: WindowInsetsCompat, bottomConfig: BottomInsetConfig) {
        applyBottomWindowInsets(windowInsets.getInsets(persistentInsetsTypes), bottomConfig)
    }

    protected open fun applyBottomWindowInsets(insets: Insets, bottomConfig: BottomInsetConfig) {
        when (bottomConfig.persistentType) {
            PersistentInsetType.None -> {
                // nothing
            }
            PersistentInsetType.Translate -> {
                view.translationY = -insets.bottom.toFloat()
            }
            PersistentInsetType.Padding -> {
                view.updatePadding(bottom = initialPadding.bottom + insets.bottom)
            }
            PersistentInsetType.Margin -> {
                view.updateLayoutParams<MarginLayoutParams> { bottomMargin = initialMargin.bottom + insets.bottom }
            }
        }
    }


    protected open fun onConsumeWindowInsets(windowInsets: WindowInsetsCompat): WindowInsetsCompat =
        windowInsets
}
