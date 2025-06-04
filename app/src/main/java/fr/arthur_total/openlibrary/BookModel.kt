package fr.arthur_total.openlibrary

class BookModel (
    val id: String = "book0",
    val name: String = "Romance1",
    val summary: String = "Résumé du livre1",
    val imageUrl: String = "https://media.istockphoto.com/id/937221630/fr/vectoriel/livre-vintage-vert-avec-serrure.jpg?s=612x612&w=0&k=20&c=7T8WBifDv_KZ3DUcz9SOse7Lhdk7uqKJVwGL9jbqABY=",
    val type: String = "Romance",
    val author: String = "auteur1",
    var liked: Boolean = false
)
