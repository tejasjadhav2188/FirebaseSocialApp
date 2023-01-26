package com.tejas.firebasesocialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tejas.firebasesocialapp.models.Post

class postAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: iPostAdapter) :
    FirestoreRecyclerAdapter<Post, postAdapter.postViewHolder>(options) {


    class postViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {


        val viewholder = postViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
        viewholder.likeButton.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewholder.absoluteAdapterPosition).id)
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: postViewHolder, position: Int, model: Post) {
        holder.postText.text  = model.text
        holder.userText.text = model.createBy.displayName
        Glide.with(holder.userImage.context).load(model.createBy.imageUrl).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likes.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likes.contains(currentUserId)

        if(isLiked){
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.ic_baseline_favorite_24))
        }else{
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.ic_baseline_favorite_border_24))
        }

    }
}

interface iPostAdapter{
    fun onLikeClicked(postId:String)
}