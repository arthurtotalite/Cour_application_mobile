package fr.arthur_total.openlibrary.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import fr.arthur_total.openlibrary.BookModel
import fr.arthur_total.openlibrary.MainActivity
import fr.arthur_total.openlibrary.BookRepository
import fr.arthur_total.openlibrary.BookRepository.Singleton.downloadUri
import fr.arthur_total.openlibrary.R
import java.util.UUID


class AddBookFragment(
    private val context: MainActivity
) : Fragment() {

    private var file:Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_add_book, container, false)

        // recuperer uploadedImage pour lui associé son composant
        uploadedImage = view?.findViewById(R.id.preview_image)

        // récuperer le bouton pour charger l'image
        val pickupImageButton = view?.findViewById<Button>(R.id.button_upload)

        // lorsqu'on clique dessus ça ouvre les images du telephone
        pickupImageButton?.setOnClickListener { pickupImage() }

        // récupérer le bouton confirmer
        val confirmation = view?.findViewById<Button>(R.id.confirm_button)
        confirmation?.setOnClickListener {sendform(view)}


        return view
    }

    private fun sendform(view: View) {
        val repo = BookRepository()
        repo.uploadImage(file!!) {
            val bookName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val bookSummary = view.findViewById<EditText>(R.id.summary_input).text.toString()
            val bookAuthor = view.findViewById<EditText>(R.id.author_input).text.toString()
            val type = view.findViewById<Spinner>(R.id.type_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            // créer un nouvle objet BookModel
            val book = BookModel(
                UUID.randomUUID().toString(),
                bookName,
                bookSummary,
                downloadImageUrl.toString(),
                type,
                bookAuthor
            )

            // envoyer en bdd
            repo.insertBook(book)
        }
    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult( Intent.createChooser(intent, "select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 47 && resultCode == Activity.RESULT_OK){

            // verifier si les données sont nulles
            if(data == null || data.data == null) return

            // recuperer l'image
            file = data.data

            // mettre à jour l'aperçu de l'image
            uploadedImage?.setImageURI(file)
        }
    }
}