package com.tejas.firebasesocialapp.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tejas.firebasesocialapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private val db = FirebaseFirestore.getInstance()

    private val usersCollection = db.collection("Users")

    fun addUser(user : User?){
        user?.let {
            GlobalScope.launch (Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserByUid(Uid:String) : Task<DocumentSnapshot>  {
        return usersCollection.document(Uid).get()
    }
}