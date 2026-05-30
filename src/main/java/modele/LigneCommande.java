package modele;

import fr.iut.annotations.Responsable;

public class LigneCommande {
    private Article article;
    private int quantité;

    @Responsable("Zaynab BOULIFA")
    public LigneCommande(Article article, int quantité) {
        this.article = article;
        this.quantité = quantité;
    }

    @Responsable("Zaynab BOULIFA")
    public Article getArticle() {
        return article;
    }
    
    @Responsable("Zaynab BOULIFA")
    public void setQuantité(int nouvelleQuantité) {
        if (nouvelleQuantité >= 0) {
            this.quantité = nouvelleQuantité;
        }
    }

    @Responsable("Zaynab BOULIFA")
    public int getQuantité() {
        return quantité;
    }

    @Responsable("Zaynab BOULIFA")
    public double getPrixTotalTTC() {
        // Retourne le prix unitaire TTC multiplié par la quantité
        return this.article.getPrixTTC() * this.quantité;
    }
}