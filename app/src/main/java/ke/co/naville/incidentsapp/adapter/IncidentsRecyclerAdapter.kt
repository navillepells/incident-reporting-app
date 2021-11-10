package ke.co.naville.incidentsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ke.co.naville.incidentsapp.R
import ke.co.naville.incidentsapp.databinding.IncidentItemBinding
import ke.co.naville.incidentsapp.model.Incident
import ke.co.naville.incidentsapp.util.MyDiffUtil

class IncidentsRecyclerAdapter : RecyclerView.Adapter<IncidentsRecyclerAdapter.ViewHolder>() {

    private var incidents: List<Incident> = emptyList()

    class ViewHolder(val binding: IncidentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            binding.apply {
                title.text = incident.title
                incidentDescription.text = incident.category
                location.text = incident.location
                time.text = incident.time

                if (incident.status) {
                    statusIcon.setImageResource(R.drawable.ic_status_green)
                } else {
                    statusIcon.setImageResource(R.drawable.ic_status_red)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IncidentItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIncident = incidents[position]
        holder.bind(currentIncident)
    }

    override fun getItemCount(): Int = incidents.size

    fun setData(newListOfIncidents: List<Incident>) {
        val diffUtil = MyDiffUtil(incidents, newListOfIncidents)
        val diffUtilRes = DiffUtil.calculateDiff(diffUtil)
        incidents = newListOfIncidents
        diffUtilRes.dispatchUpdatesTo(this)
    }
}