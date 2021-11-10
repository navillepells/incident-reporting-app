package ke.co.naville.incidentsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.co.naville.incidentsapp.util.Event
import ke.co.naville.incidentsapp.util.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    val currentUser = auth.currentUser

    val signInResult: MutableLiveData<Event<Resource<String>>> = MutableLiveData()
    val signUpResult: MutableLiveData<Event<Resource<String>>> = MutableLiveData()
    val resetPasswordResult: MutableLiveData<Event<Resource<String>>> = MutableLiveData()

    fun signInWithEmailAndPassword(email: String, password: String) {
        signInResult.postValue(Event(Resource.Loading()))
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signInResult.postValue(Event(Resource.Success("Signed in successfully!")))
                } else {
                    signInResult.postValue(Event(Resource.Error("Cannot sign in at the moment!")))
                }
            }
            .addOnFailureListener { exception ->
                signInResult.postValue(Event(Resource.Error(exception.message)))
            }
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        signUpResult.postValue(Event(Resource.Loading()))
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signUpResult.postValue(Event(Resource.Success("Signed up successfully!")))
                } else {
                    signUpResult.postValue(Event(Resource.Error("Cannot sign up at the moment!")))
                }
            }
            .addOnFailureListener { exception ->
                signUpResult.postValue(Event(Resource.Error(exception.message)))
            }
    }

    fun sendPasswordResetEmail(email: String) {
        resetPasswordResult.postValue(Event(Resource.Loading()))
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetPasswordResult.postValue(Event(Resource.Success("Please check your email!")))
                }
            }
            .addOnFailureListener { exception ->
                resetPasswordResult.postValue(Event(Resource.Error(exception.message)))
            }
    }

    fun logOut() = auth.signOut()

}