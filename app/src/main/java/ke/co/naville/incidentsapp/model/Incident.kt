package ke.co.naville.incidentsapp.model

data class Incident(
    val title: String,
    val category: String,
    val location: String,
    val time: String,
    val status: Boolean = false
)
