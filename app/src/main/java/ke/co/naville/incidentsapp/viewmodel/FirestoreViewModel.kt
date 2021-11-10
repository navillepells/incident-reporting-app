package ke.co.naville.incidentsapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.co.naville.incidentsapp.model.Incident
import ke.co.naville.incidentsapp.model.UserDetails
import ke.co.naville.incidentsapp.util.Constants.Companion.INCIDENTS_COLLECTION
import ke.co.naville.incidentsapp.util.Constants.Companion.USERS_COLLECTION
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    currentUser: FirebaseUser
) : ViewModel() {

    val currentUser = currentUser

    val uploadIncidentRes: MutableLiveData<String> = MutableLiveData()
    val uploadUserDetailsRes: MutableLiveData<String> = MutableLiveData()

    val usernameRes: MutableLiveData<String> = MutableLiveData()
    val userDetailsRes: MutableLiveData<UserDetails> = MutableLiveData()

    val myIncidentsList: MutableLiveData<List<Incident>> = MutableLiveData()
    val allIncidentsList: MutableLiveData<List<Incident>> = MutableLiveData()

    fun saveUserDetails(
        uid: String,
        firstName: String,
        lastName: String,
        gender: String,
        location: String
    ) {
        val data = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "gender" to gender,
            "location" to location
        )

        firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    uploadUserDetailsRes.postValue("Saved successfully!")
                } else {
                    uploadUserDetailsRes.postValue("Cannot save incident at the moment!")
                }
            }
            .addOnFailureListener {
                uploadUserDetailsRes.postValue(it.localizedMessage)
            }
    }

    fun getUserDetails(uid: String) {
        firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userDetails = UserDetails(
                        firstName = document.getString("firstName")!!,
                        lastName = document.getString("lastName")!!,
                        gender = document.getString("gender")!!,
                        location = document.getString("location")!!
                    )
                    userDetailsRes.postValue(userDetails)
                }
            }
    }

    fun saveIncident(uid: String, incident: Incident) {
        val data = hashMapOf(
            "title" to incident.title,
            "location" to incident.location,
            "description" to incident.category,
            "status" to incident.status,
            "time" to incident.time,
            "uid" to uid
        )
        firestore.collection(INCIDENTS_COLLECTION)
            .document()
            .set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    uploadIncidentRes.postValue("Saved successfully!")
                } else {
                    uploadIncidentRes.postValue("Cannot save incident at the moment!")
                }
            }
            .addOnFailureListener {
                uploadIncidentRes.postValue(it.localizedMessage)
            }
    }

    fun getUserName(uid: String) {
        firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val firstName = document["firstName"]
                    val lastName = document["lastName"]
                    usernameRes.postValue("$firstName $lastName")
                }
            }
    }

    fun getIndividualIncidents(uid: String) {
        firestore.collection(INCIDENTS_COLLECTION)
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                myIncidentsList.postValue(
                    documents.map {
                        Incident(
                            title = it.getString("title")!!,
                            location = it.getString("location")!!,
                            category = it.getString("description")!!,
                            time = it.getString("time")!!,
                            status = it.getBoolean("status")!!
                        )
                    }.toList()
                )
            }.addOnFailureListener {
                Log.w("Firestore", "Error getting documents: ", it)
            }
    }

    fun getAllIncidents() {
        firestore.collection(INCIDENTS_COLLECTION)
            .get()
            .addOnSuccessListener { documents ->
                allIncidentsList.postValue(
                    documents.map {
                        Incident(
                            title = it.getString("title")!!,
                            location = it.getString("location")!!,
                            category = it.getString("description")!!,
                            time = it.getString("time")!!,
                            status = it.getBoolean("status")!!
                        )
                    }.toList()
                )
            }.addOnFailureListener {
                Log.w("Firestore", "Error getting documents: ", it)
            }
    }
}