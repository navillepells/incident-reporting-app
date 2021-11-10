package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentLoginScreenBinding
import ke.co.naville.incidentsapp.ui.MainActivity
import ke.co.naville.incidentsapp.util.Resource
import ke.co.naville.incidentsapp.util.invisible
import ke.co.naville.incidentsapp.util.isValidEmail
import ke.co.naville.incidentsapp.util.visible
import ke.co.naville.incidentsapp.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginScreenFragment : Fragment(R.layout.fragment_login_screen) {

    private val binding: FragmentLoginScreenBinding by viewBinding()

    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.login.setOnClickListener {
            login()
        }

        binding.createAccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginScreenFragment_to_registerScreenFragment
            )
        }
    }

    private fun login() {
        val email = binding.emailET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (!email.isValidEmail()) {
            Toast.makeText(requireContext(), "Enter a valid email!", Toast.LENGTH_SHORT).show()
            return
        }

        authViewModel.signInWithEmailAndPassword(email, password)
        observeSignInResult()
    }

    private fun observeSignInResult() {
        authViewModel.signInResult.observe(viewLifecycleOwner, { event ->
            when (val resource = event.getContentIfNotHandled()) {
                is Resource.Success -> {
                    // hide progress bar
                    binding.progressCircular.invisible()
                    resource.data?.let { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        (requireActivity() as MainActivity).navigateToHomeActivity()
                    }
                }

                is Resource.Error -> {
                    // hide progress bar
                    binding.progressCircular.invisible()
                    resource.message?.let { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    // show progress
                    binding.progressCircular.visible()
                }
            }
        })
    }
}