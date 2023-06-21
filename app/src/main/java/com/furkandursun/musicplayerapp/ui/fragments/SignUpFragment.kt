package com.furkandursun.musicplayerapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.furkandursun.musicplayerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.furkandursun.musicplayerapp.config.ApiClient
import com.furkandursun.musicplayerapp.databinding.FragmentSignUpBinding
import com.furkandursun.musicplayerapp.model.MusicCategory
import com.furkandursun.musicplayerapp.model.MusicResponse
import com.furkandursun.musicplayerapp.service.MusicService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {


    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    lateinit var service: MusicService
    lateinit var list: List<MusicCategory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.signUpButtonSignUp.setOnClickListener {
            email = binding.mailEditTextSignUp.text.toString()
            password = binding.passwordEditText.text.toString()
            passwordAgain = binding.passwordAgainEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordAgain.isNotEmpty()) {
                if (password.length >= 6) {
                    if (password == passwordAgain) {
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            service = ApiClient.getClient().create(MusicService::class.java)

                            service.getMusics().enqueue(object : Callback<MusicResponse> {
                                override fun onResponse(call: Call<MusicResponse>, response: Response<MusicResponse>) {
                                    val data = response.body()
                                    list = data!!.musicCategories!!
                                    val db = FirebaseFirestore.getInstance()
                                    val collectionRef =
                                        db.collection("users").document(auth.currentUser!!.uid).collection("musics")
                                    for (i in list) {
                                        collectionRef.add(i)
                                            .addOnSuccessListener { documentReferences ->
                                                // Başarılı bir şekilde eklenmişse burası çalışır.
                                                Log.d("music", "Success.")
                                            }
                                            .addOnFailureListener { e ->
                                                // Bir hata oluşursa burası çalışır.
                                                Log.d("music", "Error: $e")
                                            }
                                    }
                                }

                                override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            })
                            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                        }.addOnFailureListener {

                        }
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }





}