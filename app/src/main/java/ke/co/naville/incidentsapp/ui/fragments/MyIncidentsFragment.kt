package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.adapter.IncidentsRecyclerAdapter
import ke.co.naville.incidentsapp.databinding.FragmentMyIncidentsBinding
import ke.co.naville.incidentsapp.ui.HomeActivity
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel

@AndroidEntryPoint
class MyIncidentsFragment : Fragment(R.layout.fragment_my_incidents) {

    private val binding: FragmentMyIncidentsBinding by viewBinding()

    private val recyclerAdapter by lazy { IncidentsRecyclerAdapter() }

    private val firestoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestoreViewModel.getIndividualIncidents(auth.currentUser?.uid!!)
        observeMyIncidents()


        binding.incidentsRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).showFab()
    }

    private fun observeMyIncidents() {
        firestoreViewModel.myIncidentsList.observe(viewLifecycleOwner) {
            recyclerAdapter.setData(it)
        }
    }
}