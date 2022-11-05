package com.mig35.insets.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mig35.insets.applyTopInsetsAsPadding
import com.mig35.insets.example.databinding.ActivityMainBinding

/**
 * The root activity for the sample. This Activity's layout contains a [ConversationFragment] which
 * is where the main entry point for this sample is.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tell the Window that our app is going to responsible for fitting for any system windows.
        // This is similar to the now deprecated:
        // view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN | LAYOUT_FULLSCREEN)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}