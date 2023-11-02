package com.example.chatgptopensource.ui.chunks

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chatgptopensource.R
import com.example.chatgptopensource.adapters.ChatAdapter
import com.example.chatgptopensource.adapters.HistoryAdapter
import com.example.chatgptopensource.data.remote.ChatViewModel
import com.example.chatgptopensource.databinding.FragmentHistoryBinding
import com.example.chatgptopensource.extensions.showToast
import java.util.Locale

class History : Fragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var adapter: HistoryAdapter
    private lateinit var textToSpeech: TextToSpeech


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        textToSpeech = TextToSpeech(requireContext(), this@History)
        adapter = HistoryAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getAllMessages(requireContext())!!.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        adapter.setOnClickListener(listener = object : HistoryAdapter.OnClickListener {
            override fun onItemClick(message: String) {
                if (message.isNotEmpty()) {
                    speakText(message)
                }
            }

        })

        binding.textView.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.isEdiTextShown.postValue(false)
        _binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                showToast(getString(R.string.language_not_supported))
            }
        } else {
            showToast(getString(R.string.speak_feature_is_not_available_right_now_please_try_again_later))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
    }

    fun speakText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

}