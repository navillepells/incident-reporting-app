package ke.co.naville.incidentsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentSplashScreenBinding
import ke.co.naville.incidentsapp.ui.MainActivity
import ke.co.naville.incidentsapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val binding: FragmentSplashScreenBinding by viewBinding()

    private val authViewModel: AuthViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        if (authViewModel.currentUser != null) {
            (requireActivity() as MainActivity).navigateToHomeActivity()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            delay(1000L)
            findNavController().navigate(
                R.id.action_splashScreenFragment_to_loginScreenFragment
            )
        }
    }
}