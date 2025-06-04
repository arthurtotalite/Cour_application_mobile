package fr.arthur_total.openlibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.arthur_total.openlibrary.BookRepository.Singleton.booklist
import fr.arthur_total.openlibrary.MainActivity
import fr.arthur_total.openlibrary.R
import fr.arthur_total.openlibrary.adapter.BookAdapter
import fr.arthur_total.openlibrary.adapter.BookItemDecoration

class HomeFragment (
    private val context: MainActivity
): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        // on récupère le premier recyclerview
        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView?.adapter = BookAdapter(context, booklist.filter { !it.liked } , R.layout.item_horizontal_book)

        //on récupère le second recyclerview
        val verticalRecyclerView = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView?.adapter = BookAdapter(context, booklist , R.layout.item_vertical_book)
        verticalRecyclerView?.addItemDecoration(BookItemDecoration())

        return view
    }

}
