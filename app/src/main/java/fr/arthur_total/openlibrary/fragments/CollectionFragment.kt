package fr.arthur_total.openlibrary.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.arthur_total.openlibrary.BookRepository.Singleton.booklist
import fr.arthur_total.openlibrary.MainActivity
import fr.arthur_total.openlibrary.R
import fr.arthur_total.openlibrary.adapter.BookAdapter
import fr.arthur_total.openlibrary.adapter.BookItemDecoration

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
        collectionRecyclerView?.adapter = BookAdapter(context, booklist.filter { it.liked }, R.layout.item_vertical_book)
        collectionRecyclerView?.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView?.addItemDecoration(BookItemDecoration())

        return view
    }
}
