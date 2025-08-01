package Interface;

public class ComboItemClasse {
    private String id;
    private String niveau;

    public ComboItemClasse(String id, String niveau) {
        this.id = id;
        this.niveau = niveau;
    }

    public String getId() {
        return id;
    }

    public String getNiveau() {
        return niveau;
    }

    @Override
    public String toString() {
        return niveau;
    }
}
