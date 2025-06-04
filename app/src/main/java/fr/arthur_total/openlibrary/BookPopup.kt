package fr.arthur_total.openlibrary

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.arthur_total.openlibrary.adapter.BookAdapter

class BookPopup(
    private val adapter: BookAdapter,
    private val  currentBook: BookModel
) : Dialog(adapter.context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_books_details)
        setupComponents()
        setupCloseButton()
        setupLikeButton()
    }

    private fun updateLike(button : ImageView) {
        if(currentBook.liked) {
            button.setImageResource(R.drawable.ic_like)
        }
        else {
            button.setImageResource(R.drawable.ic_unlike)
        }
    }

    private fun setupLikeButton() {
        // récupérer
        val likeButton = findViewById<ImageView>(R.id.like_button)
        updateLike(likeButton)

        // intéraction du like
        likeButton.setOnClickListener {
            currentBook.liked = !currentBook.liked
            val repo = BookRepository()
            repo.updateBook(currentBook)
            updateLike(likeButton)
        }

    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            //fermer la fenetre
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image du livre
        val bookImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentBook.imageUrl)).into(bookImage)

        // actiualiser le nom de la livre
        findViewById<TextView>(R.id.popup_book_name).text = currentBook.name

        //actualiser le résumé livre
        findViewById<TextView>(R.id.popup_book_summary_subtitle).text = currentBook.summary

        //actualiser le genre du livre
        findViewById<TextView>(R.id.popup_book_type_subtitle).text = currentBook.type

        //actualiser l'auteur du livre
        findViewById<TextView>(R.id.popup_book_author).text = currentBook.author

    }
}