package com.furkandursun.musicplayerapp.ui.fragments

import MyFavoritesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkandursun.musicplayerapp.R
import com.furkandursun.musicplayerapp.model.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MyFavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MyFavoritesAdapter
    private val musicList = mutableListOf<Music>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_favorites, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMusic)
        recyclerView.layoutManager = LinearLayoutManager(context)
        musicAdapter = MyFavoritesAdapter(musicList)
        recyclerView.adapter = musicAdapter

        loadMusicData()

        return view
    }

    private fun loadMusicData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val baseCat = document.getLong("baseCat")
                        val title = document.getString("title")
                        val url = document.getString("url")

                        val music = Music(baseCat, title, url)
                        musicList.add(music)
                    }
                    musicAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }
}