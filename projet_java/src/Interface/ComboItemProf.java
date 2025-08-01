package Interface;

public class ComboItemProf {
    private String id;
    private String nom;

    public ComboItemProf(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return id + " - " + nom;
    }
}

