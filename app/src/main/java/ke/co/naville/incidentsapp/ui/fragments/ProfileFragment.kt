package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentProfileBinding
import ke.co.naville.incidentsapp.ui.HomeActivity
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    private val firestoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideFab()
        auth = FirebaseAuth.getInstance()
        firestoreViewModel.getUserDetails(auth.currentUser?.uid!!)
        observeUserDetails()
    }

    private fun hideFab() {
        (requireActivity() as HomeActivity).hideFab()
    }

    override fun onResume() {
        super.onResume()
        hideFab()
    }

    private fun observeUserDetails() {
        firestoreViewModel.userDetailsRes.observe(viewLifecycleOwner) { userDetails ->
            binding.apply {
                profileFirstName.text = userDetails.firstName
                profileLastName.text = userDetails.lastName
                profileGender.text = userDetails.gender
                profileLocation.text = userDetails.location
                profileEmail.text = auth.currentUser?.email!!
            }
        }
    }

}