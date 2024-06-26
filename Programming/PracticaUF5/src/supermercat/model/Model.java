package supermercat.model;
import supermercat.constructors.*;
import supermercat.vista.Vista;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Clase de manipulacio de dades, aquesta clase s'encarrega de gestionar les dades del programa per a tal de fer
 * el que calgui per modificarles segons les indicacions del controlador
 */
public class Model {
    //variables constants
    private static final int CAPACITAT_MAXIMA = 100;
    private static final ArrayList<Producte> CARRO = new ArrayList<>();
    private static final ArrayList<Producte> CARRO_TEXTIL = new ArrayList<>();
    private static final HashMap<String, String> DOCUMENT = new HashMap<>();
    private static final LinkedHashMap<String,String[]> HASH_CARRO = new LinkedHashMap<>();
    private static final LinkedHashMap<String,String[]> HASH_CARRO_CAIXA = new LinkedHashMap<>();

    /**
     * Funcion comprovarCapacitat de carro, comproba si el carro ha arribat a la seva maxima capacitat
     * @return Retorna cert o fals segons si la capacitat es la maxima
     */
    private static boolean comprovaCapacitat(){
        return CARRO.size() == CAPACITAT_MAXIMA;
    }

    /**
     * Funcio per comprovar el codi de barres, aquesta funcio ens dira si existeix el codi de barres al carro
     * @param codiBarres Parametre d'entrada de tipus String que compararem
     * @return retornara true si existeix al carro o false si no
     */
    public static boolean comprovarCodiBarres(String codiBarres){
        boolean existeix = false;
        for (Producte p : CARRO_TEXTIL){
            if(p.getCodiBarres().matches(codiBarres)){
                existeix = true;
                break;
            }
        }
        return existeix;
    }

    /**
     * Funcio per comprovar data, a partir d'un parametre d'entrada es comprovara si es pot parsejar la data amb un
     * format determinat
     * @param dataIn Parametre d'entrada de tipus String que volem comprovar
     * @return Retorna el parametre d'entrada parsejat a tipus Date
     * @throws ParseException Si no es pot parsejar perque no te el format d'String correcte es llençara una excepcio
     */
    public static Date comprovarData(String dataIn) throws ParseException{
        Pattern format = Pattern.compile("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format.pattern());
        return formato.parse(dataIn);
    }

    /**
     * Funcio busca producte, a partir d'un parametre d'entrada comprovarem si el carro es buit, si no ho esta
     * comprovara el codi de barres de tots els productes amb el parametre d'entrada, si el troba et dira el seu nom i
     * sino avisara que no ho ha trobat
     * @param codiBarres Parametre d'entrada de tipus String que es comparara amb els codis de barres del carro
     * @return Retorna verdader o fals depenent de si el troba o no
     */
    public static Boolean trobarProducte(String codiBarres){
        boolean existeix = false;

        if (!(CARRO.isEmpty())){
            Optional<Producte> producte = CARRO.stream().filter(finder -> codiBarres.equals(finder.getCodiBarres()))
                                                                                    .findFirst();                       //buscardins del carro si existeix

            if (producte.isPresent()) {
                Vista.mostrarMisatge("El producte existeix i es: " + producte.get().getNom());
                existeix = true;

            } else {
                System.out.println("El codi introduït no existeix al carro");
                existeix = false;
            }
        } else {
            System.out.println("El carro és buit");

        }

        return existeix;
    }

    /**
     * Funcio per afegir un aliment, a partir d'uns parametres d'entrada es creara i s'incloura un aliment a un
     * arraylist, aixo nomes sera posible si la capacitat de l'arraylist determinada per comprovarCapacitat ho permet,
     * tan si es pot com si no es mostrara un misatge amb el resultat de l'operacio
     * @param preu Parametre d'entradade tipus float que permetra la construccio d'un producte alimentacio
     * @param nom Parametre d'entradade tipus String que permetra la construccio d'un producte alimentacio
     * @param codiBarres Parametre d'entradade tipus String que permetra la construccio d'un producte alimentacio
     * @param dataCaducitat Parametre d'entradade tipus Date que permetra la construccio d'un producte alimentacio
     */
    public static void afegirAliment(float preu, String nom, String codiBarres, Date dataCaducitat){
        if(!comprovaCapacitat()){
            CARRO.add(new Alimentacio(preu, nom, codiBarres, dataCaducitat));
            Vista.mostrarMisatge("Aliment afegit al carro");

        }else {
            Vista.mostrarMisatge("Carro complet");
        }
    }

    /**
     * Funcio per afegir un textil, a partir d'uns parametres d'entrada es creara i s'incloura un textil a un
     * arraylist, aixo nomes sera posible si la capacitat de l'arraylist determinada per comprovarCapacitat ho permet,
     * tan si es pot com si no es mostrara un misatge amb el resultat de l'operacio
     * @param preu Parametre d'entradade tipus float que permetra la construccio d'un producte textil
     * @param nom Parametre d'entradade tipus String que permetra la construccio d'un producte textil
     * @param codiBarres Parametre d'entradade tipus String que permetra la construccio d'un producte textil
     * @param composicio Parametre d'entradade tipus String que permetra la construccio d'un producte textil
     */
    public static void afegirTextil(float preu, String nom, String codiBarres, String composicio){
        if(!comprovaCapacitat()){
            CARRO_TEXTIL.add(new Textil(preu, nom, codiBarres, composicio));
            CARRO.add(new Textil(preu, nom, codiBarres, composicio));
            Vista.mostrarMisatge("Textil afegit al carro");

        }else {
            Vista.mostrarMisatge("Carro complet");
        }
    }

    /**
     * Funcio per afegir electonica, a partir d'uns parametres d'entrada es creara i s'incloura electronica a un
     * arraylist, aixo nomes sera posible si la capacitat de l'arraylist determinada per comprovarCapacitat ho permet,
     * tan si es pot com si no es mostrara un misatge amb el resultat de l'operacio
     * @param preu Parametre d'entradade tipus float que permetra la construccio d'un producte electronica
     * @param nom Parametre d'entradade tipus String que permetra la construccio d'un producte electronica
     * @param codiBarres Parametre d'entradade tipus String que permetra la construccio d'un producte electronica
     * @param diesGarantia Parametre d'entradade tipus int que permetra la construccio d'un producte electronica
     */
    public static void afegirElectronica(float preu, String nom, String codiBarres, int diesGarantia){
        if (!comprovaCapacitat()) {
            CARRO.add(new Electronica(preu, nom, codiBarres, diesGarantia));
            Vista.mostrarMisatge("Electronica afegida al carro");

        }else {
            Vista.mostrarMisatge("Carro complet");
        }
    }

    /**
     * Funcio per afegir al carro els productes per mostrar
     * @param p Parametre d'entrada de tipus Producte que volem afegir a un HashMap
     */
    public static void afegirAlHash(Producte p){
        int unitats = 1;
        String key = p.getCodiBarres() + p.getPreu();

        if(!(HASH_CARRO_CAIXA.containsKey(key))){
            String[] valueCaixa = new String[3];
            valueCaixa[0] = p.getNom();
            valueCaixa[1] = unitats + "";
            valueCaixa[2] = p.getPreu() + "";

            //si pertenece a textil
            if(p instanceof Textil){
                for(Map.Entry<String, String> doc : DOCUMENT.entrySet()){
                    String valorDoc = doc.getValue();   //valor preu
                    String valorKey = doc.getKey();     //valor codi barres

                    //si coinciden los codigos de barras pero no el precio
                    if(valorKey.equals(p.getCodiBarres())){
                        if(!(p.getPreu() + "").equals(valorDoc)){
                            valueCaixa[2] = valorDoc;       //modifica el valor al del doc

                        }else {
                            valueCaixa[2] = p.getPreu() + "";   //modifica el valor al del preu
                        }
                    }
                }
            }

            HASH_CARRO_CAIXA.put(key, valueCaixa);

        }else {
            String[] valueHash = new String[3];
            valueHash[0] = HASH_CARRO_CAIXA.get(key)[0];
            valueHash[1] = (Integer.parseInt(HASH_CARRO_CAIXA.get(key)[1]) + unitats) + "";
            valueHash[2] = p.getPreu() + "";

            HASH_CARRO_CAIXA.replace(key, valueHash);
        }
        if(!(HASH_CARRO.containsKey(p.getCodiBarres()))){
            String[] valueHash = new String[2];
            valueHash[0] = p.getNom();
            valueHash[1] = unitats + "";

            HASH_CARRO.put(p.getCodiBarres(), valueHash);

        }else {
            String[] valueHash = new String[2];
            valueHash[0] = HASH_CARRO.get(p.getCodiBarres())[0];
            valueHash[1] = (Integer.parseInt(HASH_CARRO.get(p.getCodiBarres())[1]) + unitats) + "";

            HASH_CARRO.replace(p.getCodiBarres(), valueHash);
        }
    }

    /**
     * Funcio per mostrar la data actual, a partir de un LocalDateTime es calcula la data actual amb la funcio interna
     * now i retornata la data actual en un format determinat
     * @return Retorna la data actual formatada (dd/MM/yyyy hh:mm:ss)
     */
    public static String mostrarDataActual(){
        LocalDateTime avui = LocalDateTime.now();
        return avui.getDayOfMonth() + "/" + avui.getMonthValue() + "/" + avui.getYear() + "\t" + avui.getHour() +
                                      ":" + avui.getMinute() + ":" + avui.getSecond();
    }

    /**
     * Funcio calcula el preu total, aquesta funcio calcula el preu total a partir de la multiplicacio del preu i les
     * unitats de tots eles productes i despres suma aquest resultat i el guarda en una variable de referencia atomica
     * @return Retorna el valor de la variable String total que te el resultat del calcul del preu total
     */
    private static String calcularPreuTotal(){
        String total;
        AtomicReference<Float> totalPagar = new AtomicReference<>(0f);
        HASH_CARRO_CAIXA.forEach((key, nomUniPre) -> totalPagar.updateAndGet(suma -> suma + (Float.parseFloat(nomUniPre[2]) *
                                                                                        Float.parseFloat(nomUniPre[1]))));
        total = totalPagar + " €";
        return total;
    }

    /**
     * Funcio que calcula el preu de l'electronica, a partir d'uns parametres d'entrada es calculara seguint una funcio
     * el preu que tindra un producte d'electronica
     * @param preu Parametre d'entrada definit al constructor d'electronica per definir el preu d'un producte
     *             d'electronica
     * @param diesGarantia Parametre d'entrada definit al constructor d'electronica per definir els dies de garantia
     *                    d'un producte d'electronica
     * @return Retorna el resultat numeric am decimals del que definira el preu d'un producte d'electronica
     */
    public static float preuElectronica(float preu, int diesGarantia){
        return (float) (preu + preu * (diesGarantia  / 365) * 0.1);
    }

    /**
     * Funcio que calcula el preu de l'laliment, a partir d'uns parametres d'entrada es calculara seguint una funcio el
     * preu que tindra un producte d'alimentacio
     * @param preu Parametre d'entrada definit al constructor d'alimentacio per definir el preu d'un producte
     *             d'alimentacio
     * @param dataCaducitat Parametre d'entrada definit al constructor d'alimentacio per definir la data de caducitat
     *                     d'un producte d'alimentacio
     * @return Retorna el resultat numeric amb decimals que definira el preu d'un producte d'alimentacio
     */
    public static float preuAlimentacio(float preu, Date dataCaducitat){
        Date actual = new Date();
        long milisegonsDif = dataCaducitat.getTime() - actual.getTime();
        int dataDif = (int) TimeUnit.MILLISECONDS.toDays(milisegonsDif);

        return (float) (preu - preu * (1 / (dataDif + 1)) - (preu * 0.1));
    }

    /**
     * Mostra el contingut del carro, si el carro no es buit mostra una capçalera i tots els productes del carro
     * formatats, si el carro es buit mostrara un misatge
     */
    public static void carroActual(){
        Collections.sort(CARRO);

        for(Producte p: CARRO){
            if(p instanceof Textil){

            }
            afegirAlHash(p);
        }

        if(!HASH_CARRO.isEmpty()){
            Vista.mostrarCapcaleraCarro();
            HASH_CARRO.forEach((codiBarres, nomUnitats) -> Vista.mostrarCarro(nomUnitats[0], nomUnitats[1]));

        }else {
            Vista.mostrarMisatge("El carro es buit");
        }
        HASH_CARRO_CAIXA.clear();
        HASH_CARRO.clear();
    }

    /**
     * Mostra un menu en forma de ticket amb totes les dades de la compra, am totes les modificacions pertinents i
     * esborra totes les llistes
     */
    public static void pasarPerCaixa(){
        Collections.sort(CARRO);

        for(Producte p: CARRO){
            afegirAlHash(p);
        }

        Vista.mostrarCompra();
        HASH_CARRO_CAIXA.forEach((key, nomUniPre) -> Vista.mostrarCaixa(nomUniPre[0] ,
                                                                         nomUniPre[1] ,
                                                                         nomUniPre[2] ,
                                                                         ((Float.parseFloat(nomUniPre[2]) *
                                                                                 Float.parseFloat(nomUniPre[1])) + "")));
        Vista.mostrarPreuFinal(calcularPreuTotal());
        Vista.mostrarMisatge(" ");
        CARRO.clear();
        CARRO_TEXTIL.clear();
        HASH_CARRO_CAIXA.clear();
        HASH_CARRO.clear();
    }

    /**
     * Funcio que inicialitza el super, a partir dunes rutes definides dins del programa es generan carpetes i fitxers
     * per tractar eventualitats del supermercat
     */
    public static void inicialitzaSuper(){
        final String ARREL = "./";
        File carpetaUpdate = new File(ARREL + "Updates");
        File carpetaLogs = new File(ARREL + "Logs");
        File fitxerUpdate = new File(ARREL + "Updates/UpdateTextilPrices.dat");
        File fitxerLogs = new File(ARREL + "Logs/Exceptions.dat");

        try {
            creaCarpeta(carpetaLogs);
            creaCarpeta(carpetaUpdate);
            creaFitxers(fitxerLogs);
            creaFitxers(fitxerUpdate);
            guardarDocument();

        }catch (IOException e){
            Vista.mostrarMisatge(e.getMessage());
            omplenaRegistreExcepcions(e);

        }catch (Exception e) {
            omplenaRegistreExcepcions(e);
            throw new RuntimeException(e);

        }
    }

    /**
     * Funcio per crear una carpeta, a partir d'una ruta es creara una carpeta, nomes sera posible si la carpeta no
     * existeix, tan si es pot com si no es mostrara un misatge el resultat de l'oeracio
     * @param carpeta Parametre d'entrada de tipus File que defineix el desti i nom de la carpeta
     * @throws FileNotFoundException Si no troba la ruta es pot produir una excepcio
     */
    private static void creaCarpeta(File carpeta) throws FileNotFoundException{
        if(carpeta.mkdirs()){
            Vista.mostrarMisatge("La carpeta " + carpeta.getName() + " creada correctament");
        }else {
            Vista.mostrarMisatge("La carpeta " + carpeta.getName() + " ja existeix");
        }
    }

    /**
     * Funcio per a crear un fitxer, a partir d'una ruta es crea un fitxer, nomes sera posible si el fixer no existeix,
     * tan si es pot com si no es mostrara un misatge am el resultat de l'operacio
     * @param arxiu Parametre d'entrada de tipus File que defineix el desti i nom del fixer
     * @throws Exception Es pot produir una excepcio a l'hora de crear el fitxer amb la ruta
     */
    private static void creaFitxers(File arxiu)throws Exception{
        if(arxiu.createNewFile()){
            Vista.mostrarMisatge("Arxiu " + arxiu.getName() + " creat correctament");
        }else {
            Vista.mostrarMisatge("L'arxiu " + arxiu.getName() + " ja existeix");
        }
    }

    /**
     * Funcio per omplenar el registre d'excepcions, a partir d'un parametre d'entrada s'omplira un document
     * d'excepcions
     * @param eIn Parametre d'entrada de tipus Exception que s'utilitzara per escriure el document
     */
    public static void omplenaRegistreExcepcions(Exception eIn){
        try {
            final String ARREL_LOGS = "./Logs/Exceptions.dat";
            File rutaLogs = new File(ARREL_LOGS);
            FileOutputStream obrirAlFinal = new FileOutputStream(rutaLogs, true);   //evitar sobreescritura de l'arxiu
            PrintStream writerLogs = new PrintStream(obrirAlFinal);

            writerLogs.println("Exception trobada a " + mostrarDataActual() + ": " + eIn.getMessage());
            writerLogs.close();

        }catch (FileNotFoundException e){
            Vista.mostrarMisatge("No s'ha trobat l'arxiu/carpeta");
            omplenaRegistreExcepcions(e);
        }
    }

    /**
     * Funcion guarda un document, aquesta funcio llegeix i guarda un document dins d'un HashMap
     * @throws IOException Aquesta funcio por llençar una Excepcio si no troba el fitxer o no pot llegir una linea
     */
    private static void guardarDocument() throws IOException{
        File rutaFitxer = new File("./Updates/UpdateTextilPrices.dat");
        String linea, separador = ";";
        FileReader rdr = new FileReader(rutaFitxer);
        BufferedReader brdr = new BufferedReader(rdr);

        while ((linea = brdr.readLine()) != null){
            String[] valor = linea.split(separador);
            DOCUMENT.put(valor[0], valor[1]);
        }
    }
}
