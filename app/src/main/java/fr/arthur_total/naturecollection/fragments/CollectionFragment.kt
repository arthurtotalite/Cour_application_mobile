package fr.arthur_total.naturecollection.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur_total.naturecollection.MainActivity
import fr.arthur_total.naturecollection.PlantRepository.Singleton.plantlist
import fr.arthur_total.naturecollection.R
import fr.arthur_total.naturecollection.adapter.PlantAdapter
import fr.arthur_total.naturecollection.adapter.PlantItemDecoration

class CollectionFragment(
    private val context: MainActivity
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        //récupérer ma view
        val collectionRecyclerView = view?.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView?.adapter = PlantAdapter(context, plantlist.filter { it.liked }, R.layout.item_vertical_plant)
        collectionRecyclerView?.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView?.addItemDecoration(PlantItemDecoration())

        return view
    }
}
