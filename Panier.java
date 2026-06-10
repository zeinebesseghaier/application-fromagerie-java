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
    
    /**
     * Calcule les frais de port du panier selon le transporteur choisi et la grille tarifaire.
     * indexTransporteur : 0 = Colissimo, 1 = Chrono Relais, 2 = Chronofresh
     */
    @Responsable("Zaynab BOULIFA")
    public double calculerFraisPort(int indexTransporteur) {
        double total = calculerTotalTTC();
        
        // Seuil de gratuité (franco de port à partir de 120 €) ou panier vide
        if (total == 0 || total >= 120.0) {
            return 0.0;
        }

        // 0 = Colissimo, 1 = Chrono Relais (Même grille tarifaire)
        if (indexTransporteur == 0 || indexTransporteur == 1) { 
            if (total < 60.0) {
                return 14.90;
            } else if (total < 90.0) {
                return 9.90;
            } else {
                return 4.90;
            }
        } 
        // 2 = Chronofresh
        else if (indexTransporteur == 2) { 
            if (total < 50.0) {
                return 23.80;
            } else if (total < 80.0) {
                return 17.80;
            } else {
                return 9.90;
            }
        }
        
        return 0.0;
    }

    /**
     * Calcule le montant total du panier transport compris
     */
    @Responsable("Zaynab BOULIFA")
    public double calculerTotalAvecTransport(int indexTransporteur) {
        double total = calculerTotalTTC();
        if (total == 0) {
            return 0.0;
        }
        return total + calculerFraisPort(indexTransporteur);
    }
}