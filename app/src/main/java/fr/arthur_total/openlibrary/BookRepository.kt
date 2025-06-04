package fr.arthur_total.openlibrary

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.arthur_total.openlibrary.BookRepository.Singleton.booklist
import fr.arthur_total.openlibrary.BookRepository.Singleton.databaseRef
import fr.arthur_total.openlibrary.BookRepository.Singleton.downloadUri
import fr.arthur_total.openlibrary.BookRepository.Singleton.storageReference
import java.util.UUID

class BookRepository {

    object Singleton {
        // donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://project-open-library.firebasestorage.app"

        // se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la reference "books"
        val databaseRef = FirebaseDatabase.getInstance().getReference("books")

        // créer une liste qui va contenir les livres
        val booklist = arrayListOf<BookModel>()

        // contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }
    fun updateData(callback: ()-> Unit){
        // absorber les données depuis la database -> liste de livres
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciens livres avant de faire la mise à jour
                booklist.clear()
                // récolter la liste
                for (ds in snapshot.children) {
                    // construire un objet livre
                    val book = ds.getValue(BookModel::class.java)

                    // vérifier que le livre n'est pas null
                    if (book != null) {
                        // ajouter le livre à notre liste
                        booklist.add(book)
                    }
                }
                // actionner le callback
                callback()

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
    // creer une fonction pour envoyer des fichier sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // vérifier que le fichier n'est pas null
        if(file != null) {
            val filename = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(filename)
            val uploadTask = ref.putFile(file)

            //démarrer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // si il y a eu un problème lors de l'envoie du fichier
                if(!task.isSuccessful) {
                    task.exception?.let { throw it}
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { task ->
                // verifier si tout a bien fonctionné
                if(task.isSuccessful) {
                    // récupérer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }
    //mettre à jour un objet livre en bdd
    fun updateBook(book: BookModel) {
        databaseRef.child(book.id).setValue(book)
    }

    //insérer un nouveau livre en bdd
    fun insertBook(book: BookModel) = databaseRef.child(book.id).setValue(book)

    // supprimer un livre de la base
    fun deleteBook(book: BookModel) = databaseRef.child(book.id).removeValue()
}