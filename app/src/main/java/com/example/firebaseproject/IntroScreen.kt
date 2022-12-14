package com.example.firebaseproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.firebaseproject.databinding.FragmentIntroScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroScreen : Fragment() {

    private var _binding: FragmentIntroScreenBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            val directions = IntroScreenDirections.actionIntroScreenToRegistrationFragment()
            findNavController().navigate(directions)
        }
        binding.tvlogin.setOnClickListener{
            findNavController().navigate(R.id.action_introScreen_to_loginFragment)
        }
    }

}