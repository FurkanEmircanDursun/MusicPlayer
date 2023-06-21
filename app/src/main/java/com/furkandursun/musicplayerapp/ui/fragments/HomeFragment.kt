package com.furkandursun.musicplayerapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkandursun.musicplayerapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.furkandursun.musicplayerapp.adapter.CategoryAdapter
import com.furkandursun.musicplayerapp.databinding.FragmentHomeBinding
import com.furkandursun.musicplayerapp.model.MusicCategory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        getMusicCategories().addOnSuccessListener { musicList ->
            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CategoryAdapter(musicList)

        }.addOnFailureListener { e ->

        }
        binding.favoritesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myFavoritesFragment)
        }
        auth = FirebaseAuth.getInstance()
        binding.signOutButton.setOnClickListener {

            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
        }
        return binding.root
    }

    private fun getMusicCategories(): Task<MutableList<MusicCategory>> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("musics")

        val newList = mutableListOf<MusicCategory>()

        return collectionRef.get()
            .continueWith { task ->
                val querySnapshot = task.result
                for (document in querySnapshot.documents) {
                    val data = document.toObject(MusicCategory::class.java)
                    data?.let {
                        newList.add(it)
                    }
                }
                newList
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
