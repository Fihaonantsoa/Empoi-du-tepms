package java_project;

public class Professeur {
    private String idprof;
    private String nom;
    private String prenoms;
    private String grade;

    public Professeur(String idprof, String nom, String prenoms, String grade) {
        this.idprof = idprof;
        this.nom = nom;
        this.prenoms = prenoms;
        this.grade = grade;
    }


    public String getIdprof() { return idprof; }
    public void setIdprof(String idprof) { this.idprof = idprof; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "Professeur{idprof='" + idprof + "', nom='" + nom + "', prenoms='" + prenoms + "', grade='" + grade + "'}";
    }
}
