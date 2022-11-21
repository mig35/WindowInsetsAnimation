package com.mig35.insets

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import com.mig35.insets.controllers.InsetsControllerAnimatedImmediate
import com.mig35.insets.controllers.InsetsControllerAnimatedTranslate
import com.mig35.insets.controllers.InsetsControllerStatic


/**
 * This file contains methods to be used on every screen of the app that should react
 * on different insets changes. So, it's literally each screen.
 *
 * The top inset is usually the status bar.
 * The bottom inset is usually navigation bar or opened soft IME.
 *
 * !!! Attention !!!
 * If you call two different methods one after another on the same view,
 * only the latest will be executed.
 */

/**
 * Do some work when we need to apply insets
 */
fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Will add top system inset via padding
 */
fun View.applyTopInsetsAsPadding() {
    applyInsetsWithConfig(
        InsetsConfig(
            topConfig = TopInsetConfig.systemOnly,
        )
    )
}

/**
 * Will add bottom system inset via padding (no changes with ime open/hide)
 */
fun View.applyBottomInsetsAsPadding() {
    applyInsetsWithConfig(
        InsetsConfig(
            bottomConfig = BottomInsetConfig(
                persistentType = PersistentInsetType.Padding,
                imeInsetChange = BottomInsetConfig.ImeInsetChange.None
            ),
        )
    )
}

/**
 * Will add bottom system inset via translate (no changes with ime open/hide)
 */
fun View.applyBottomInsetsAsTranslate() {
    applyInsetsWithConfig(
        InsetsConfig(
            bottomConfig = BottomInsetConfig(
                persistentType = PersistentInsetType.Translate,
                imeInsetChange = BottomInsetConfig.ImeInsetChange.None
            )
        )
    )
}

/**
 * Will add bottom system inset via margin (no changes with ime open/hide)
 */
fun View.applyBottomInsetsAsMargin() {
    applyInsetsWithConfig(
        InsetsConfig(
            bottomConfig = BottomInsetConfig(
                persistentType = PersistentInsetType.Margin,
                imeInsetChange = BottomInsetConfig.ImeInsetChange.None
            )
        )
    )
}

/**
 * Will add bottom system inset via padding
 * Will add ime animation by translate when animation is in progress and fill ime space with padding in the end.
 */
fun View.applyBottomImeInsets() {
    applyInsetsWithConfig(
        InsetsConfig(
            bottomConfig = BottomInsetConfig(
                persistentType = PersistentInsetType.Padding,
                imeInsetChange = BottomInsetConfig.ImeInsetChange.Animated
            ),
        )
    )
}

fun View.applyInsetsWithConfig(
    insetsConfig: InsetsConfig,
) {
    when (insetsConfig.bottomConfig?.imeInsetChange) {
        null, BottomInsetConfig.ImeInsetChange.None ->
            InsetsControllerStatic(this, insetsConfig)
        BottomInsetConfig.ImeInsetChange.Animated ->
            InsetsControllerAnimatedTranslate(this, insetsConfig)
        BottomInsetConfig.ImeInsetChange.Immediate ->
            InsetsControllerAnimatedImmediate(this, insetsConfig)
    }
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
    doOnAttach {
        ViewCompat.requestApplyInsets(this)
    }
}
