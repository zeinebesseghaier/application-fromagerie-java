package modele;

import fr.iut.annotations.Responsable;

/**
 * Représente le panier de commande (version minimale pour P1).
 */
public class Panier {

    @Responsable("Zeineb ESSEGHAIER")
    public Panier() {
    }

    @Responsable("Zeineb ESSEGHAIER")
    public void ajouterArticle(Article article, int quantité) {
        article.setQuantitéEnStock(article.getQuantitéEnStock() - quantité);
    }
}