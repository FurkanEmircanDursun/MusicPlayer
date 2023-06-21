package com.furkandursun.musicplayerapp.ui.fragments

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.furkandursun.musicplayerapp.R
import com.furkandursun.musicplayerapp.databinding.FragmentSongBinding
import com.furkandursun.musicplayerapp.model.Music
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SongFragment : Fragment() {
    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!
    private lateinit var music: Music

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        music = arguments?.getSerializable("music") as Music

        getMusicFavorites().addOnSuccessListener { musicList ->
            if (musicList.contains(music)){
                binding.btnAddFavorite.visibility= View.INVISIBLE
                binding.btnRemoveFavorite.visibility= View.VISIBLE
            }else{
                binding.btnAddFavorite.visibility= View.VISIBLE
                binding.btnRemoveFavorite.visibility= View.INVISIBLE
            }
        }.addOnFailureListener { e ->

        }

        try {
            val uri = Uri.parse(music.url)
            mediaPlayer = MediaPlayer.create(requireContext(), uri)
        } catch (e: Exception) {

        }
        val volume = getVolume()
        mediaPlayer.setVolume(volume, volume)
        binding.sbSound.progress = (volume * 100).toInt()
        mediaPlayer.start()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMusicTitle.text = music.title
        binding.btnPause.setOnClickListener {
            mediaPlayer.pause()
            binding.btnPause.visibility = View.INVISIBLE
            binding.btnPlay.visibility = View.VISIBLE

        }

        binding.btnPlay.setOnClickListener {
            mediaPlayer.start()
            binding.btnPause.visibility = View.VISIBLE
            binding.btnPlay.visibility = View.INVISIBLE

        }

        binding.btnAddFavorite.setOnClickListener {
            addFirebase()
            binding.btnAddFavorite.visibility = View.INVISIBLE
            binding.btnRemoveFavorite.visibility = View.VISIBLE
        }

        binding.sbSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress.toFloat() / 100
                mediaPlayer.setVolume(volume, volume)
                saveVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_songFragment_to_homeFragment)
                    mediaPlayer.stop()
                }
            })
    }


    private fun saveVolume(volume: Float) {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putFloat("Volume", volume)
        editor.apply()
    }

    private fun getVolume(): Float {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getFloat("Volume", 0.5f)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addFirebase() {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("favorites")
        collectionRef.add(music)
            .addOnSuccessListener { documentReferences ->


            }
            .addOnFailureListener { e ->

            }

    }
    private fun getMusicFavorites(): Task<MutableList<Music>> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("favorites")

        val newList = mutableListOf<Music>()

        return collectionRef.get()
            .continueWith { task ->
                val querySnapshot = task.result
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Music::class.java)
                    data?.let {
                        newList.add(it)
                    }
                }
                newList
            }
    }

    override fun onResume() {
        super.onResume()

        mediaPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.pause()
    }
}