package modele;

import fr.iut.annotations.Responsable;

public enum TypeLait {
    VACHE("Vache"), CHEVRE("Chèvre"), BREBIS("Brebis");

    private String typeDeLait;

    @Responsable("Hervé LEBLANC")
    private TypeLait(String typeDeLait) {
        this.typeDeLait = typeDeLait;
    }

    @Responsable("Hervé LEBLANC")
    public String getTypeDeLait() {
        return this.typeDeLait;
    }

    @Responsable("Hervé LEBLANC")
    public static TypeLait getTypeLait(String dénomination) {
        for (TypeLait t : TypeLait.values()) {
            if (t.getTypeDeLait().equals(dénomination)) {
                return t;
            }
        }
        return null;
    }

}
