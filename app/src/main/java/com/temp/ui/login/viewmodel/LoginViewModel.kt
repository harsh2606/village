package com.temp.ui.login.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.temp.R
import com.temp.apputils.Constant
import com.temp.apputils.Debug
import com.temp.apputils.Utils
import com.temp.base.viewmodel.BaseViewModel
import com.temp.databinding.ActivityLoginBinding
import com.temp.ui.home.view.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.temp.apputils.FirestoreTable
import com.temp.ui.login.datamodel.UserData

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var binder: ActivityLoginBinding
    private lateinit var mContext: Context
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 5001
    private val TAG = "LoginViewModel"
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
//    private var firebaseAuth: FirebaseAuth? = null
   var userData: UserData? = null


    fun setBinder(binder: ActivityLoginBinding) {
        this.binder = binder
        this.mContext = binder.root.context
        this.binder.viewModel = this
        this.binder.viewClickHandler = ViewClickHandler()
        init()
    }

    private fun init() {


        initGoogle()
        userData = UserData()
        signOutGoogle()
        revokeAccess()

    }

    private fun initGoogle() {
//        firebaseAuth = FirebaseAuth.getInstance()
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // Get signedIn user
            val user = auth?.currentUser

            //if user is signed in, we call a helper method to save the user details to Firebase
            if (user != null) {
                // User is signed in
                // you could place other firebase code
                //logic to save the user details to Firebase
                Debug.e(TAG, "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                // User is signed out
                Debug.e(TAG, "onAuthStateChanged:signed_out")
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken((mContext as Activity).getString(R.string.default_web_client_id))//you can also use R.string.default_web_client_id
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(mContext as Activity, gso)
    }

    private fun signOutGoogle() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(mContext as Activity) {
                        // signed out
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun revokeAccess() {
        try {
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(mContext as Activity) {
                        // ...
                    }
        } catch (e: Exception) {
        }
    }


    private fun loginGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        (mContext as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account)
//            updateUI(account)
//            getGoogleProfile(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Debug.e("", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        showDialog("", (mContext as Activity))
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth!!.signInWithCredential(credential)
                .addOnCompleteListener(mContext as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Debug.e("firebaseAuthWithGoogle", "signInWithCredential:success")
                        val user = auth!!.currentUser
//                    Utils.setPref(
//                        mContext,
//                        Constant.LOGIN_INFO,
//                        user.toString()
//                    )
                        FirebaseMessaging.getInstance().token
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        return@OnCompleteListener
                                    }
//                          Get new Instance ID token
                                    val token = task.result
                                    getUserList(account,token)
                                    Debug.e("fcm_token", token)
                                })
                        Debug.e(TAG, user.toString())
                    } else {
                        dismissDialog()
                        // If sign in fails, display a message to the user.
                        Debug.e(
                                "firebaseAuthWithGoogle",
                                "signInWithCredential:failure " + task.exception!!.localizedMessage
                        )
                    }

                    // ...
                }

    }
    private fun getUserList(account: GoogleSignInAccount, token: String) {
        try {
            val documentID = auth!!.currentUser!!.uid
            var query = db!!.collection(FirestoreTable.USERS).document(auth!!.currentUser!!.uid)
            query.get().addOnSuccessListener { result ->
                dismissDialog()
                if (result.exists()) {
                    if (result != null) {
                        val userData: UserData? = result.toObject(UserData::class.java)
                        val gson = Gson()
                        val user = gson.toJson(userData)
                        Utils.setPref(
                                mContext,
                                Constant.LOGIN_INFO,
                                user.toString()
                        )
                        if (userData?.isAdmin!!) {
                            moveNext(HomeActivity::class.java)
                        }
//                        if (userData?.isAdmin!!) {
//                            if (userData.shopID.isNullOrEmpty()) {
//                                moveNext(SelectShopActivity::class.java)
//                            } else if (!userData?.isApproved!!) {
//                                moveNext(VerificationActivity::class.java)
//                            } else {
//                                moveNext(HomeActivity::class.java)
//                            }
//                        } else {
//                            if (!userData?.isApproved!!) {
//                                moveNext(VerificationActivity::class.java)
//                            } else {
//                                moveNext(HomeActivity::class.java)
//                            }
                        }

                        Debug.e("UID Exist")
                    }
                 if (userData?.isAdmin!!){
                    userData?.id = auth!!.currentUser!!.uid
                    userData?.firstName = account.givenName
                    userData?.lastName = account.familyName
                    userData?.email = account.email
                    userData?.socialID = account.id
                    userData?.type = "google"
                    userData?.isAdmin = false

                    addUser(userData!!)
                    Debug.e("UID Doesn't Exist")
                }
            }.addOnFailureListener {
                it.printStackTrace()
                dismissDialog()
            }.addOnCompleteListener {
                dismissDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addUser(userData: UserData) {
        db!!.collection(FirestoreTable.USERS)
                .document(auth!!.currentUser!!.uid.toString())
                .set(userData)
                .addOnSuccessListener {
                    val gson = Gson()
                    val user = gson.toJson(userData)
                    Utils.setPref(
                            mContext,
                            Constant.LOGIN_INFO,
                            user.toString()
                    )
                    if (userData?.isAdmin!!) {
                        moveNext(HomeActivity::class.java)
                    } else {
                        moveNext(HomeActivity::class.java)
                    }

                    Debug.e(TAG, "User Added Successfully")
                    //getCustomers(catalogue)
                }
                .addOnFailureListener { exception ->
                    Debug.e(TAG, "User Added Fail")
                    exception.printStackTrace()
                }
    }

    fun moveNext(activity: Class<*>?) {
        val intent = Intent(mContext, activity)
        intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        (mContext as Activity).startActivity(intent)
    }


    private fun getGoogleProfile(acct: GoogleSignInAccount?) {
//        val acct = GoogleSignIn.getLastSignedInAccount(mContext)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val providerToken = acct.idToken
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl

            Debug.e("getGoogleProfile", "$personEmail $personName")

            // Do login social call here
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        return@OnCompleteListener
//                    }
//                    // Get new Instance ID token
//                    val token = task.result
//
//                })
        }
    }

    inner class ViewClickHandler {

        fun onSignInClick(view: View) {
            try {
                if (isValidate()) {
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun onGoogleLogin(view: View) {
            loginGoogle()
        }

        fun onLogin(view: View) {
            try {
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isValidate(): Boolean {
//        val emailValidator = EmailValidator(binder.edtUserEmail.text.toString())
//        if (!emailValidator.isValid()) {
//            showToast(emailValidator.msg)
//            return false
//        } else if (binder.edtPassword.text.isNullOrEmpty()) {
//            showToast(mContext.getString(R.string.password_field_is_require))
//            return false
//        }

        return true
    }

}
