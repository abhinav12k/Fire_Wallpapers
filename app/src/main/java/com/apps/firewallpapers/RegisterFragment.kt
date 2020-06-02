package com.apps.firewallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setting up nav controller
        navController = Navigation.findNavController(view)


        //check if user is already logged in
        if(firebaseAuth.currentUser!=null){
            //user is logged in
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }else{
            //user is not logged in create a new account
            register_text.text= "Creating New Account"
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful){
                    //Account successfully created
                    register_text.text = "Account created, Logging in"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                }else{
                    register_text.text = "Error : ${it.exception!!.message}"
                }
            }

        }
    }

}