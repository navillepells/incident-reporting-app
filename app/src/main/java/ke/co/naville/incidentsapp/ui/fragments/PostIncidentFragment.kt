package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.util.Log
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
import ke.co.naville.incidentsapp.databinding.FragmentPostIncidentBinding
import ke.co.naville.incidentsapp.model.Incident
import ke.co.naville.incidentsapp.ui.HomeActivity
import ke.co.naville.incidentsapp.util.CategoryItemsList.Companion.categoryItems
import ke.co.naville.incidentsapp.util.LocationItemsList.Companion.locationItems
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel
import java.util.*

@AndroidEntryPoint
class PostIncidentFragment : Fragment(R.layout.fragment_post_incident) {

    private val binding: FragmentPostIncidentBinding by viewBinding()

    private val firestoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideFab()
        auth = FirebaseAuth.getInstance()
        setupOnClickListeners()
        setupDropdown()
    }

    private fun setupOnClickListeners() {
        binding.saveFab.setOnClickListener {
            saveIncident()
        }
    }

    private fun setupDropdown() {
        val locationAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, locationItems)
        binding.locationAutoComplete.setAdapter(locationAdapter)

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryItems)
        binding.categoryAutoComplete.setAdapter(categoryAdapter)
    }

    private fun saveIncident() {
        val title = binding.titleET.text.toString().trim()
        val location = binding.locationAutoComplete.text.toString().trim()
        val category = binding.categoryAutoComplete.text.toString().trim()

        if (title.isEmpty() || location.isEmpty() || category.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val min = Calendar.getInstance().get(Calendar.MINUTE)
        val time = "$hr:$min"

        Log.d("PostIncidentFragment", "saveIncident: time = $time")

        val incident = Incident(title, category, location, time)

        firestoreViewModel.saveIncident(auth.currentUser?.uid!!, incident)
        observeUploadResponse()
    }

    private fun observeUploadResponse() {
        firestoreViewModel.uploadIncidentRes.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            findNavController()
                .navigate(
                    R.id.action_global_myIncidentsFragment
                )
        }
    }

    override fun onResume() {
        super.onResume()
        hideFab()
    }

    private fun hideFab() {
        (requireActivity() as HomeActivity).hideFab()
    }
}