package co.shree.krishna.shreekrishna.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.shree.krishna.shreekrishna.data.ChatData
import co.shree.krishna.shreekrishna.data.Event
import co.shree.krishna.shreekrishna.data.USER_NODE
import co.shree.krishna.shreekrishna.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    private val  auth : FirebaseAuth,
    private var db : FirebaseFirestore,
    private var storage: FirebaseStorage
) : ViewModel() {

    var isProgress = mutableStateOf(false)
    var inProgressChat = mutableStateOf(false)
    var eventMutableState = mutableStateOf<Event<String>?>(null)
    val signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())


    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun signUp(name : String, number: String, email: String, password : String) {
        isProgress.value = true

        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handelException(msg =  "Please Fill all details")
            return
        }


        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Vijay", "signUp: Logged IN")
                        signIn.value = true
                        createOrUpdateProfile(name,number)
                    }
                    else {
                        Log.d("Vijay", "$it")
                        handelException(it.exception, "SignUp Failed")
                    }
                }
            }
            else {
                handelException(msg = "Number already exist")
                isProgress.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handelException(msg = "Please fill all the details")
            return
        }

        isProgress.value = true
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (!it.isSuccessful) {
                handelException(it.exception, "Login failed")
            }
            signIn.value = true
            isProgress.value = false
            auth.currentUser?.uid?.let {it1->
                getUserData(it1)
            }
        }
    }

    fun createOrUpdateProfile(name: String? = null  , number: String? = null, imageUrl: String? = null) {
        val uid = auth.currentUser?.uid
        val user = UserData(userId = uid,
            name= name ?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )

        uid.let {
            isProgress.value = true
            if (uid != null) {
                db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                    if (it.exists()){
                        db.collection(USER_NODE).document(uid).update(user.toMap())
                        Log.d("Vijay", "createOrUpdateProfile: $user")
                        isProgress.value = false
                    } else {
                        db.collection(USER_NODE).document(uid).set(user)
                        isProgress.value = false
                        getUserData(uid)
                    }
                } .addOnFailureListener { handelException(it,"Cannot Retrieve user") }
            }
        }
    }

    private fun getUserData(uid: String) {
        isProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handelException(error, "Cannot retrieve user")
            }
            if (value != null){

                val user = value.toObject<UserData>()
                Log.d("Vijay", "getUserData: $value -- $user")
                userData.value = user
                isProgress.value = false
            }
        }
    }

    private fun handelException(e : Exception? = null, msg : String = "" ){
        Log.e("Vijay", "handelException: LiveChat Exception ", e)
        e?.printStackTrace()
        Log.d("Vijay", "handelException: $e  -- $msg")
        val errorMsg = e?.localizedMessage ?: ""
        val message = msg.ifEmpty { errorMsg }
        eventMutableState.value = Event(message)
        isProgress.value = false
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }


    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        isProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("image/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            isProgress.value = false
        }.addOnFailureListener{
            handelException(it)
        }
    }

    fun logOut() {
        auth.signOut()
        signIn.value = false
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(it: String) {

    }
}

