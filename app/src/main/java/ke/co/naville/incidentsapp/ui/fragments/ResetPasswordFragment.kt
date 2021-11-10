package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentResetPasswordBinding
import ke.co.naville.incidentsapp.util.Resource
import ke.co.naville.incidentsapp.util.invisible
import ke.co.naville.incidentsapp.util.isValidEmail
import ke.co.naville.incidentsapp.util.visible
import ke.co.naville.incidentsapp.viewmodel.AuthViewModel

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val binding: FragmentResetPasswordBinding by viewBinding()

    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetPasswordBtn.setOnClickListener {
            sendResetPasswordEmail()
        }
    }

    private fun sendResetPasswordEmail() {
        val email = binding.emailET.text.toString().trim()

        if (!email.isValidEmail()) {
            Toast.makeText(requireContext(), "Please enter a valid email!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        authViewModel.sendPasswordResetEmail(email)

        observePasswordResetRes()
    }

    private fun observePasswordResetRes() {
        authViewModel.resetPasswordResult.observe(viewLifecycleOwner) { event ->
            when (val res = event.getContentIfNotHandled()) {
                is Resource.Success -> {
                    // show progress bar
                    binding.progressCircular.invisible()
                    res.data?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        // navigate to login screen
                    }
                }

                is Resource.Error -> {
                    // hide progress bar
                    binding.progressCircular.invisible()
                    res.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    // show progress bar
                    binding.progressCircular.visible()
                }
            }
        }
    }

}