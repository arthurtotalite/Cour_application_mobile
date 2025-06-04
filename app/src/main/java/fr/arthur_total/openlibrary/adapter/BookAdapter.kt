package fr.arthur_total.openlibrary.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.arthur_total.openlibrary.BookModel
import fr.arthur_total.openlibrary.MainActivity
import fr.arthur_total.openlibrary.R
import fr.arthur_total.openlibrary.BookPopup
import fr.arthur_total.openlibrary.BookRepository

class BookAdapter (
    val context: MainActivity,
    private val booklist: List<BookModel>,
    private val layoutId: Int,
): RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    //  boite pour ranger tout les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val bookImage = view.findViewById<ImageView>(R.id.image_item)
        val bookName:TextView? = view.findViewById(R.id.name_item)
        val bookAuthor:TextView?= view.findViewById(R.id.author_item)
        val likeIcon = view.findViewById<ImageView>(R.id.like_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // récupérer les informations du livre
        val currentBook = booklist[position]

        //récuperer le repository
        val repo = BookRepository()

        // utiliser glide pour recuperer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentBook.imageUrl)).into(holder.bookImage)

        // mettre à jour le nom des livres
        holder.bookName?.text = currentBook.name

        // mettre à jour l'auteur des livres
        holder.bookAuthor?.text = currentBook.author

        // mettre à jour le résumé des livres ?

        // vérifier si le livre à été liké
        if(currentBook.liked){
            holder.likeIcon.setImageResource(R.drawable.ic_like)
        }
        else{
            holder.likeIcon.setImageResource(R.drawable.ic_unlike)
        }
        // rajouter une intéraction sur le coeur
        holder.likeIcon.setOnClickListener {
            // inversé si le bouton est like ou pas
            currentBook.liked = !currentBook.liked
            // mettre à jour l'objet livre
            repo.updateBook(currentBook)
        }
        // intéraction lors du clic sur un livre
        holder.itemView.setOnClickListener {
            // afficher la popup
            BookPopup(this, currentBook).show()
        }

    }

    override fun getItemCount(): Int = booklist.size
}