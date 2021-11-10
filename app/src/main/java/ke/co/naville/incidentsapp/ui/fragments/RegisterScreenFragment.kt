package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentRegisterScreenBinding
import ke.co.naville.incidentsapp.ui.MainActivity
import ke.co.naville.incidentsapp.util.Resource
import ke.co.naville.incidentsapp.util.invisible
import ke.co.naville.incidentsapp.util.isValidEmail
import ke.co.naville.incidentsapp.util.visible
import ke.co.naville.incidentsapp.viewmodel.AuthViewModel
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel

@AndroidEntryPoint
class RegisterScreenFragment : Fragment(R.layout.fragment_register_screen) {

    private val binding: FragmentRegisterScreenBinding by viewBinding()

    private val authViewModel: AuthViewModel by viewModels()

    private val firestoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setupOnClickListener()
        setupDropdown()
    }

    private fun setupOnClickListener() {
        binding.register.setOnClickListener {
            register()
        }

        binding.loginToAccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerScreenFragment_to_loginScreenFragment
            )
        }
    }

    private fun setupDropdown() {
        val items = listOf("Female", "Male")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.autoComplete.setAdapter(adapter)
    }

    private fun register() {
        val email = binding.emailET.text.toString().trim()
        val firstName = binding.firstNameET.text.toString().trim()
        val lastName = binding.lastNameET.text.toString().trim()
        val gender = binding.autoComplete.text.toString()
        val location = binding.locationET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()
        val passwordRepeat = binding.repeatPasswordET.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || location.isEmpty() ||
            email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()
        ) {
            Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!email.isValidEmail()) {
            Toast.makeText(requireContext(), "Enter a valid email.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordRepeat) {
            Toast.makeText(requireContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        authViewModel.signUpWithEmailAndPassword(email, password)

        observerSignUpResult(firstName, lastName, gender, location)
    }

    private fun observerSignUpResult(
        firstName: String,
        lastName: String,
        gender: String,
        location: String
    ) {
        authViewModel.signUpResult.observe(viewLifecycleOwner) { event ->
            when (val resource = event.getContentIfNotHandled()) {
                is Resource.Success -> {
                    // show progress bar
                    firestoreViewModel.saveUserDetails(
                        auth.currentUser?.uid!!,
                        firstName,
                        lastName,
                        gender,
                        location
                    )
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
                    // show progress bar
                    binding.progressCircular.visible()
                }
            }
        }
    }
}