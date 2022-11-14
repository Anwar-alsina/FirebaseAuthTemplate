package com.example.firebaseproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.firebaseproject.data.User
import com.example.firebaseproject.databinding.FragmentRegistrationBinding
import com.example.firebaseproject.util.Resource
import com.example.firebaseproject.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCreateAccount.setOnClickListener {
                val user = User(
                    etFirstName.text.toString().trim(),
                    etLastName.text.toString().trim(),
                    etEmail.text.toString().trim(),
                )
                val password = etPassword.text.toString()
                viewModel.createAccountEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.btnCreateAccount
                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                    }
                    is Resource.Error -> {
                        Log.e("TAG",it.message.toString())
                    }
                }
            }
        }
    }
}