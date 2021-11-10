package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.adapter.IncidentsRecyclerAdapter
import ke.co.naville.incidentsapp.databinding.FragmentAllIncidentsBinding
import ke.co.naville.incidentsapp.ui.HomeActivity
import ke.co.naville.incidentsapp.viewmodel.FirestoreViewModel

@AndroidEntryPoint
class AllIncidentsFragment : Fragment(R.layout.fragment_all_incidents) {

    private val binding: FragmentAllIncidentsBinding by viewBinding()

    private val firestoreViewModel: FirestoreViewModel by viewModels()

    private val recyclerAdapter by lazy { IncidentsRecyclerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreViewModel.getAllIncidents()
        observeMyIncidents()

        binding.allIncidentsRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

        (requireActivity() as HomeActivity).hideFab()
    }

    private fun observeMyIncidents() {
        firestoreViewModel.allIncidentsList.observe(viewLifecycleOwner) {
            recyclerAdapter.setData(it)
        }
    }
}