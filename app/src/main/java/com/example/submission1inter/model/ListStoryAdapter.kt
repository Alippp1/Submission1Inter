package com.example.submission1inter.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1inter.databinding.StoryBinding
import retrofit2.Call

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    private val storyList = ArrayList<ListStoryItem>()
//    private val detailStory = ArrayList<DetailStoryResponse>()
    inner class ViewHolder(val binding: StoryBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickSwitch : Profil

    fun setProfil(onItemClickSwitch: Profil){
        this.onItemClickSwitch = onItemClickSwitch
    }
    interface Profil {
        fun onItemClicked(data: ListStoryItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       with(holder){
           with(storyList[position]){
               Glide.with(itemView.context)
                   .load(photoUrl)
                   .into(binding.imgItemPhoto)
               binding.tvItemName.text = name
               itemView.setOnClickListener{
                   onItemClickSwitch.onItemClicked(storyList[position])
               }
           }
       }
    }

    override fun getItemCount() = storyList.size

    fun setListStory(listStory:List<ListStoryItem>){
        this.storyList.clear()
        this.storyList.addAll(listStory)
        notifyDataSetChanged()
    }
//    fun setDetailStory(detailStory: DetailStoryResponse){
//        this.detailStory.clear()
//        this.detailStory.addAll(listOf(detailStory))
//        notifyDataSetChanged()
//    }
}