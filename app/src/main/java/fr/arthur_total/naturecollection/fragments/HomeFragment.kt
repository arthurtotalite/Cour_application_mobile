package fr.arthur_total.naturecollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.arthur_total.naturecollection.MainActivity
import fr.arthur_total.naturecollection.PlantModel
import fr.arthur_total.naturecollection.R
import fr.arthur_total.naturecollection.adapter.PlantAdapter
import fr.arthur_total.naturecollection.adapter.PlantItemDecoration
import fr.arthur_total.naturecollection.PlantRepository.Singleton.plantlist

class HomeFragment (
    private val context: MainActivity
): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        // on récupère le premier recyclerview
        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView?.adapter = PlantAdapter(context,plantlist.filter { !it.liked } , R.layout.item_horizontal_plant)

        //on récupère le second recyclerview
        val verticalRecyclerView = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView?.adapter = PlantAdapter(context,plantlist , R.layout.item_vertical_plant)
        verticalRecyclerView?.addItemDecoration(PlantItemDecoration())

        return view
    }

}
