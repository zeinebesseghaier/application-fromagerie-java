package modele;

import fr.iut.annotations.Responsable;
import java.util.ArrayList;
import java.util.List;

public class Panier {

    private List<LigneCommande> lignesCommande;

    @Responsable("Zeineb ESSEGHAIER")
    public Panier() {
        this.lignesCommande = new ArrayList<>();
    }

    @Responsable("Zeineb ESSEGHAIER")
    public void ajouterArticle(Article article, int quantité) {
        article.setQuantitéEnStock(article.getQuantitéEnStock() - quantité);
        this.lignesCommande.add(new LigneCommande(article, quantité));
    }

    @Responsable("Zaynab BOULIFA")
    public List<LigneCommande> getLignesCommande() {
        return this.lignesCommande;
    }

    @Responsable("Zaynab BOULIFA")
    public double calculerTotalTTC() {
        double total = 0;
        for (LigneCommande lc : lignesCommande) {
            total += lc.getPrixTotalTTC();
        }
        return total;
    }
}