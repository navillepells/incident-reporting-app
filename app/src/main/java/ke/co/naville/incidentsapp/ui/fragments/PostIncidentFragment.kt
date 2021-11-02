package ke.co.naville.incidentsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.FragmentPostIncidentBinding

class PostIncidentFragment : Fragment(R.layout.fragment_post_incident) {

    private val binding: FragmentPostIncidentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}