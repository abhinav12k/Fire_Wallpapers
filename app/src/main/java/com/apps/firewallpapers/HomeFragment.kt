package com.apps.firewallpapers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    private val firebaseRepository = FirebaseRepository()
    private var navController: NavController? = null

    private var wallpapersList: List<WallpapersModel> = ArrayList()
    private val wallpapersListAdapter: WallpapersListAdapter = WallpapersListAdapter(wallpapersList,this)

    private var isLoading = true

    private val wallpaperViewModel: WallpapersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing nav controller
        navController = Navigation.findNavController(view)

        //Initializing action bar
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Fire Wallpapers"

        //checking if user is logged in
        if(firebaseRepository.getUser()==null){
            //User not logged in go to register page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }

        //Initialize recycler view
        wallpaper_list_view.setHasFixedSize(true)
        wallpaper_list_view.layoutManager = GridLayoutManager(context,3)
        wallpaper_list_view.adapter = wallpapersListAdapter

        //Reached bottom of recycler view
        wallpaper_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    //user reached at bottom and is not scrolling anymore

                    if(!isLoading){

                        //Load next page
                        wallpaperViewModel.loadWallpapersData()

                        isLoading = true

                        Log.d("Home_Fragment_Log","Reached bottom Loading new content")
                    }

                }
            }
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpaperViewModel.getWallpaperList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            //Loading is complete
            isLoading = false
        })

    }

    override fun invoke(wallpaper: WallpapersModel) {
        //Clicked on wallpaper Item from the list, Navigate to details fragment
        Log.d("HOME_FRAMGNE_LOG", "Clicked on Item : ${wallpaper.name}")

        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)
        navController!!.navigate(action)

    }

}