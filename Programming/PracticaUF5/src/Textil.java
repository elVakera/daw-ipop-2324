public class Textil extends Producte{
    String composicioTextil;
    public Textil(float preu, String nom, String codiBarres, String composicioTextil){
        super(preu,nom,codiBarres);
        this.composicioTextil = composicioTextil;
    }
}