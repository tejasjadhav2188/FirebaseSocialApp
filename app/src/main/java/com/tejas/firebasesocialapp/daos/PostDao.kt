package com.tejas.firebasesocialapp.daos

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tejas.firebasesocialapp.models.Post
import com.tejas.firebasesocialapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("Posts")
    private val auth = Firebase.auth

    fun addPost(text : String){
        val currentUserId = auth.currentUser!!.uid
//        Log.i("TAG", "abc $currentUserId")


        GlobalScope.launch {
            val userDao  = UserDao()
            val user = userDao.getUserByUid(currentUserId).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text,user,currentTime)
            postCollection.document().set(post)
        }

    }

    fun getPostById(Id:String) : Task<DocumentSnapshot> {
        return postCollection.document(Id).get()
    }
    fun updateLikes(postId : String){

        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likes.contains(currentUserId)

            if(isLiked){
                post.likes.remove(currentUserId)
            }
            else{
                post.likes.add(currentUserId)
            }
            postCollection.document(postId).set(post)

        }

    }
}