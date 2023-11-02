package com.example.chatgptopensource.ui.chunks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chatgptopensource.R
import com.example.chatgptopensource.apiCall.Message
import com.example.chatgptopensource.data.remote.ChatViewModel
import com.example.chatgptopensource.databinding.FragmentHomeBinding


class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by activityViewModels()
    private var messaging: Message? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.tvQuery.isSelected = true

        viewModel.getLastMessage(requireContext())!!.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.tvQuery.text = it[1].content
                binding.tvQueryAnswer.text = it[0].content
                binding.textView2.text = getString(R.string.history_1)
            } else {
                binding.textView2.text = getString(R.string.history_0)
            }
        }


        binding.btnQuickChat.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_home2_to_chatScreen)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }


        binding.birthdayLetter.setOnClickListener {
            navigation(getString(R.string.write_me_a_birthday_letter))
        }

        binding.socialMediaPost.setOnClickListener {
            navigation(getString(R.string.write_a_social_media_post_about_something))
        }

        binding.jobLetter.setOnClickListener {
            navigation(getString(R.string.write_me_a_job_letter))
        }


        binding.completedMath.setOnClickListener {
            navigation(getString(R.string.completed_mathematical_explanations))
        }

        binding.letsLearnEnglish.setOnClickListener {
            navigation(getString(R.string.let_s_learn_english))
        }

        binding.scienceAndScientificHub.setOnClickListener {
            navigation(getString(R.string.science_and_scientific_hub))
        }

        binding.attractCustomers.setOnClickListener {
            navigation(getString(R.string.how_do_i_attract_customers))
        }

        binding.growBusiness.setOnClickListener {
            navigation(getString(R.string.how_do_i_grow_my_business))
        }

        binding.mostDemandedSkills.setOnClickListener {
            navigation(getString(R.string.what_are_the_most_demanded_skills))
        }

        binding.lostWeight.setOnClickListener {
            navigation(getString(R.string.how_do_i_lose_weight))
        }

        binding.tallerQuickly.setOnClickListener {
            navigation(getString(R.string.what_to_eat_to_grow_more_taller_quickly))
        }

        binding.moreMuscles.setOnClickListener {
            navigation(getString(R.string.what_should_i_do_to_gain_more_muscle))
        }

        binding.gainMoreWeight.setOnClickListener {
            navigation(getString(R.string.what_should_i_do_to_gain_more_weight))
        }

        binding.translator.setOnClickListener {
            navigation(getString(R.string.hi_i_will_provide_you_syntax_of_some_language_and_you_will_translate_it_into_english))
        }


        binding.topCard.setOnClickListener {
            try {
                viewModel.isEdiTextShown.postValue(true)
                findNavController().navigate(R.id.action_home2_to_history2)
            }catch (ex: Exception)
            {
                ex.printStackTrace()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigation(message: String) {
        try {
            messaging = Message("user", message)
            viewModel.messageFromUser.postValue(messaging)
            val bundle = Bundle()
            bundle.putBoolean("history", false)
            findNavController().navigate(R.id.action_home2_to_chatScreen, bundle)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}