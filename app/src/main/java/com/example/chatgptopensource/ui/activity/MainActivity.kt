package com.example.chatgptopensource.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.chatgptopensource.R
import com.example.chatgptopensource.apiCall.Message
import com.example.chatgptopensource.data.remote.ChatViewModel
import com.example.chatgptopensource.databinding.ActivityMainBinding
import com.example.chatgptopensource.extensions.showToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ChatViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    if (s!!.isNotEmpty()) {
                        findNavController(R.id.navigation_host_fragment).navigate(R.id.action_home2_to_chatScreen)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        })


        binding.imgSend.setOnClickListener {
            if (binding.editTextText.text.toString().isNotEmpty()) {
                viewModel.messageFromUser.postValue(
                    Message(
                        "user",
                        binding.editTextText.text.toString()
                    )
                )

                binding.editTextText.setText("")
            } else {
                showToast(getString(R.string.please_write_something_first))
            }
        }


        viewModel.isEdiTextShown.observe(this, Observer {
            if (it) {
                binding.edInput.visibility = View.GONE
            } else {
                binding.edInput.visibility = View.VISIBLE
            }
        })


        viewModel.isResponseArrived.observe(this, Observer {
            if (it) {
                binding.imgSend.isClickable = true
                binding.imgSend.isEnabled = true
            } else {
                binding.imgSend.isClickable = false
                binding.imgSend.isEnabled = false
            }
        })


    }

}