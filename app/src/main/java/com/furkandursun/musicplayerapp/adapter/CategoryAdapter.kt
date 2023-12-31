package com.furkandursun.musicplayerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkandursun.musicplayerapp.R

import com.furkandursun.musicplayerapp.model.MusicCategory


class CategoryAdapter(private val categoryList: MutableList<MusicCategory>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val subCategoryRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewSubs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryWithSubcategories = categoryList[position]

        holder.titleTextView.text = categoryWithSubcategories.baseTitle
        holder.subCategoryRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        categoryWithSubcategories.items?.let {
            holder.subCategoryRecyclerView.adapter = SubListAdapter(categoryWithSubcategories.items)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}