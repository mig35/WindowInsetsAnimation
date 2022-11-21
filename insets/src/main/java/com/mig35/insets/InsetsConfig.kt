package com.mig35.insets

data class InsetsConfig(
    val topConfig: TopInsetConfig? = null,
    val bottomConfig: BottomInsetConfig? = null,
)

/**
 * Tells how to add a distance for persistent insets (like system bars or ime in the final state).
 */
enum class PersistentInsetType {
    /**
     * Nothing will be added additionally
     */
    None,

    /**
     * TranslateY will be used
     */
    Translate,

    /**
     * Padding (top or bottom) will be used
     */
    Padding,

    /**
     * Margin (top or bottom) will be used
     */
    Margin,
}

data class TopInsetConfig(
    /**
     * Define handle of top system bar insets. Usually padding.
     */
    val persistentType: PersistentInsetType,
) {

    companion object {
        val systemOnly = TopInsetConfig(persistentType = PersistentInsetType.Padding)
    }
}

data class BottomInsetConfig(
    /**
     * Define handle of bottom system bar or ime final state insets (if ImeInsetType is not None). Usually padding.
     */
    val persistentType: PersistentInsetType,
    /**
     * Define handle of ime animation and final state.
     */
    val imeInsetChange: ImeInsetChange,
) {

    enum class ImeInsetChange {
        /**
         * No ime actions
         */
        None,

        /**
         * Will animate ime open/hide with translate for best performance and will use persistentType for ime final state.
         */
        Animated,

        /**
         * Will apply ime open/hide immediately and will use persistentType for ime final state.
         */
        Immediate,
    }
}
