package java_project;

import java.time.LocalDateTime;

public class EmploiDuTemps {
    private int idsalle;
    private String idprof;
    private String idclasse;
    private String cours;
    private LocalDateTime date;

    public EmploiDuTemps(int idsalle, String idprof, String idclasse, String cours, LocalDateTime date) {
        this.idsalle = idsalle;
        this.idprof = idprof;
        this.idclasse = idclasse;
        this.cours = cours;
        this.date = date;
    }

    public int getIdsalle() { return idsalle; }
    public String getIdprof() { return idprof; }
    public String getIdclasse() { return idclasse; }
    public String getCours() { return cours; }
    public LocalDateTime getDate() { return date; }

    public void setIdsalle(int idsalle) { this.idsalle = idsalle; }
    public void setIdprof(String idprof) { this.idprof = idprof; }
    public void setIdclasse(String idclasse) { this.idclasse = idclasse; }
    public void setCours(String cours) { this.cours = cours; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
