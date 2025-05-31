package fr.arthur_total.naturecollection

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.arthur_total.naturecollection.PlantRepository.Singleton.databaseRef
import fr.arthur_total.naturecollection.PlantRepository.Singleton.downloadUri
import fr.arthur_total.naturecollection.PlantRepository.Singleton.plantlist
import fr.arthur_total.naturecollection.PlantRepository.Singleton.storageReference
import java.net.URI
import java.util.UUID

class PlantRepository {

    object Singleton {
        // donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://project-nature-collection.firebasestorage.app"

        // se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la reference "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // créer une liste qui va contenir les plantes
        val plantlist = arrayListOf<PlantModel>()

        // contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }
    fun updateData(callback: ()-> Unit){
        // absorber les données depuis la database -> liste de plantes
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes plantes avant de faire la mise à jour
                plantlist.clear()
                // récolter la liste
                for (ds in snapshot.children) {
                    // construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // vérifier que la plante n'est pas null
                    if (plant != null) {
                        // ajouter la plante à notre liste
                        plantlist.add(plant)
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
    //mettre à jour un objet plante en bdd
    fun updatePlant(plant: PlantModel) {
        databaseRef.child(plant.id).setValue(plant)
    }

    //insérer une nouvelle plante en bdd
    fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // supprimer une plante de la base
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}