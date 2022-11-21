/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mig35.insets.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mig35.insets.*
import com.mig35.insets.example.databinding.FragmentConversationBinding

/**
 * The main entry point for the sample. See [onViewCreated] for more information on how
 * the sample works.
 */
class ConversationFragment : Fragment() {
    private var _binding: FragmentConversationBinding? = null
    private val binding: FragmentConversationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set our conversation adapter on the RecyclerView
        binding.conversationRecyclerview.adapter = ConversationAdapter()

        binding.toolbar.applyTopInsetsAsPadding()
        binding.messageHolder.applyBottomImeInsets()
        binding.conversationRecyclerview.applyInsetsWithConfig(
            InsetsConfig(
                bottomConfig = BottomInsetConfig(
                    persistentType = PersistentInsetType.None,
                    imeInsetChange = BottomInsetConfig.ImeInsetChange.Animated,
                ),
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
