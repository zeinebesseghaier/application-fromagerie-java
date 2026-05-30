package modele;

import fr.iut.annotations.Responsable;

/**
 * la classe Article représente une facon de vendre un fromage
 */
public class Article {

    private Fromage fromage;
    private String clé;
    private float prixTTC;
    private int quantitéEnStock;

    /**
     * construit un nouveau fromage (non encore en stock
     * 
     * @param fromage le fromage dont l'article est issu
     * @param clé     sa clé, son identifiant
     * @param prixTTC son prix TTC
     */
    @Responsable("Hervé LEBLANC")
    public Article(Fromage fromage, String clé, float prixTTC) {
        this(fromage, clé, prixTTC, 0);
    }

    /**
     * construit un nouveau fromage à partir de sa lecture en JSon
     * 
     * @param fromage  le fromage dont l'article est issu
     * @param clé      sa clé, son identifiant
     * @param prixTTC  son prix TTC
     * @param quantité sa quantité en stock au moment de sa création
     */
    @Responsable("Hervé LEBLANC")
    public Article(Fromage fromage, String clé, float prixTTC, int quantité) {
        this.fromage = fromage;
        this.clé = clé;
        this.prixTTC = prixTTC;
        this.quantitéEnStock = quantité;
    }

    /**
     * @return le fromage dont l'article est issu
     */
    @Responsable("Hervé LEBLANC")
    public Fromage getFromage() {
        return this.fromage;
    }

    /**
     * @return son prix TTC
     */
    @Responsable("Hervé LEBLANC")
    public float getPrixTTC() {
        return this.prixTTC;
    }

    /**
     * @return sa quantité en stock disponible au moment de la demande
     */
    @Responsable("Hervé LEBLANC")
    public int getQuantitéEnStock() {
        return this.quantitéEnStock;
    }

    /**
     * @return sa clé, son identifiant
     */
    @Responsable("Hervé LEBLANC")
    public String getClé() {
        return this.clé;
    }

    /**
     * Mets à jour la quantité disponible en stock
     * 
     * @param quantitéEnStock la nouvelle quantité disponible
     */
    @Responsable("Hervé LEBLANC")
    public void setQuantitéEnStock(int quantitéEnStock) {
        this.quantitéEnStock = quantitéEnStock;
    }

    @Responsable("Hervé LEBLANC")
    @Override
    public String toString() {
        if (this.clé.equals("")) {
            return this.fromage.getDésignation() + ", Prix TTC : "
                    + this.getPrixTTC() + " €";
        } else {
            return this.fromage.getDésignation() + ", " + this.clé
                    + ", Prix TTC : " + this.getPrixTTC() + " €";
        }
    }

    /**
     * @return la chaîne à afficher dans la commande ou le panier
     */
    @Responsable("Hervé LEBLANC")
    public String toStringIHM() {
        if (this.clé.equals("")) {
            return "Prix TTC : " + this.getPrixTTC() + " €";
        } else {
            return this.clé + ", Prix TTC : " + this.getPrixTTC() + " €";
        }
    }

    /**
     * @return la chaîne à afficher à la console pour le deboggage de la gestion
     *         de stocks
     */
    @Responsable("Hervé LEBLANC")
    public String toStringAvecStock() {
        return this.toString() + ", Quantité en stock : "
                + this.quantitéEnStock;
    }

    @Responsable("Hervé LEBLANC")
    @Override
    public boolean equals(Object obj) {
        Article other = (Article) obj;
        return (this.fromage.equals(other.fromage)
                && this.clé.equals(other.clé));
    }

}
