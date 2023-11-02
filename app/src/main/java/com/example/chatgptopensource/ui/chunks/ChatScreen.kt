package com.example.chatgptopensource.ui.chunks

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatgptopensource.R
import com.example.chatgptopensource.adapters.ChatAdapter
import com.example.chatgptopensource.apiCall.Message
import com.example.chatgptopensource.apiCall.RequestBodyObject
import com.example.chatgptopensource.data.remote.ChatViewModel
import com.example.chatgptopensource.databinding.FragmentChatScreenBinding
import com.example.chatgptopensource.db.MessageEntity
import com.example.chatgptopensource.extensions.showToast
import java.util.Locale

class ChatScreen : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatAdapter
    private var choices: ArrayList<Message> = ArrayList()
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var textToSpeech: TextToSpeech


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)


        adapter = ChatAdapter(choices)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        textToSpeech = TextToSpeech(requireContext(), this@ChatScreen)


        viewModel.messageFromUser.observe(viewLifecycleOwner) {
            if (it.content.isNotEmpty()) {
                binding.textView3.visibility = View.GONE
                choices.add(it)
                adapter.notifyItemInserted(choices.size - 1)
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                binding.tvTyping.text = getString(R.string.typing)
                getResponseFromChatAI(it)

                viewModel.isResponseArrived.postValue(false)
                //db insertion
                viewModel.insertMessages(requireContext(), MessageEntity(0, it.role, it.content))
            }
        }


        adapter.setOnClickListener(listener = object : ChatAdapter.OnClickListener {
            override fun onItemClick(message: String) {
                if (message.isNotEmpty()) {
                    speakText(message)
                }
            }

        })

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }




        return binding.root
    }

    private fun getResponseFromChatAI(message: Message) {
        val requestBody = RequestBodyObject(model = "gpt-3.5-turbo", messages = listOf(message))
        viewModel.getChatCompletion(requestBody, requireContext()).observe(viewLifecycleOwner)
        { response ->
            binding.tvTyping.text = getString(R.string.online)
            if (response.choices[0].message.content.isNotEmpty()) {
                viewModel.insertMessages(
                    requireContext(),
                    MessageEntity(0, "assistant", response.choices[0].message.content)
                )
                choices.add(Message("assistant", response.choices[0].message.content))
            } else {
                choices.add(Message("assistant", getString(R.string.something_went_wrong)))
                viewModel.insertMessages(
                    requireContext(),
                    MessageEntity(0, "assistant", response.choices[0].message.content)
                )
            }
            viewModel.isResponseArrived.postValue(true)
            adapter.notifyItemInserted(choices.size - 1)
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val message = Message("", "")
        viewModel.messageFromUser.postValue(message)
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