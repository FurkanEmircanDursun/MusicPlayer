package com.furkandursun.musicplayerapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.furkandursun.musicplayerapp.R
import com.furkandursun.musicplayerapp.model.Music

class SubListAdapter(private val subcategoryList: List<Music>) :
    RecyclerView.Adapter<SubListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subCategoryTextView: TextView = itemView.findViewById(R.id.subtitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each subcategory item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sub_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subcategory = subcategoryList[position]

        // Set the subcategory title
        holder.subCategoryTextView.text = subcategory.title

        holder.subCategoryTextView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("music", subcategory)
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_homeFragment_to_songFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return subcategoryList.size
    }
}
