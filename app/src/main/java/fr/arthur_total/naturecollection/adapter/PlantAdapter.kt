package fr.arthur_total.naturecollection.adapter

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.arthur_total.naturecollection.MainActivity
import fr.arthur_total.naturecollection.PlantModel
import fr.arthur_total.naturecollection.R
import androidx.core.net.toUri
import fr.arthur_total.naturecollection.PlantPopup
import fr.arthur_total.naturecollection.PlantRepository

class PlantAdapter (
    val context: MainActivity,
    private val plantlist: List<PlantModel>,
    private val layoutId: Int,
): RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    //  boite pour ranger tout les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById(R.id.name_item)
        val plantDescritpion:TextView?= view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // récupérer les informations de la plante
        val currentPlant = plantlist[position]

        //récuperer le repository
        val repo = PlantRepository()

        // utiliser glide pour recuperer l'image à partir de son lien-> composant
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // mettre à jour le nom des plantes
        holder.plantName?.text = currentPlant.name

        // mettre à jour la description des plantes
        holder.plantDescritpion?.text = currentPlant.description

        // vérifier si la plante à été liké
        if(currentPlant.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }
        // rajouter une intéraction sur cette étoile
        holder.starIcon.setOnClickListener {
            // inversé si le bouton est like ou pas
            currentPlant.liked = !currentPlant.liked
            // mettre à jour l'objet plante
            repo.updatePlant(currentPlant)
        }
        // intéraction lors du clic sur une plante
        holder.itemView.setOnClickListener {
            // afficher la popup
            PlantPopup(this, currentPlant).show()
        }

    }

    override fun getItemCount(): Int = plantlist.size
}