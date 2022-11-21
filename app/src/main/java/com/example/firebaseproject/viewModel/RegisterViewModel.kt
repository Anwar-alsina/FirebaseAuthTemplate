package com.example.firebaseproject.viewModel

import androidx.lifecycle.ViewModel
import com.example.firebaseproject.data.User
import com.example.firebaseproject.util.*
import com.example.firebaseproject.util.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreDb: FirebaseFirestore
): ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Loading())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountEmailAndPassword(user: User, password: String){
      if (checkValidation(user,password)) {

          firebaseAuth.createUserWithEmailAndPassword(user.email, password)
              .addOnSuccessListener {
                  it.user?.let {
                      saveUserInfo(it.uid,user)

                  }
              }.addOnFailureListener {
                  _register.value = Resource.Error(it.message.toString())
              }
      } else{
          val registerFieldsState = RegisterFieldsState(
              validateEmail(user.email), validatePassword(password)
          )
          runBlocking {
              _validation.send(registerFieldsState)
          }
      }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        firestoreDb.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener{
                _register.value = Resource.Error(it.message.toString())
            }


    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister

    }
}

