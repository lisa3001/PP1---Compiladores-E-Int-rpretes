package compilador;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileWriter;
import java.io.BufferedWriter ;
import java.io.FileNotFoundException;
import java.io.FileReader;
import producciones.InitProgram;


public class Compilador {

    public final static int GENERAR = 1;
    public final static int EJECUTAR = 2;
    public final static int SALIR = 3;

    /**
     Se crea un menú para generar el analizador léxico y el analizador 
     sintáctico o ejecutar el programa. 
     * @param args the command line arguments
    */
    public static void main(String[] args) {
        java.util.Scanner in = new Scanner(System.in);
        int valor = 0;
        do {
            System.out.print("-----------------------------------------------\n");
            System.out.println("Indique una opcion: ");
            System.out.println("1) Generar el analizador léxico y sintáctico.");
            System.out.println("2) Ejecutar el programa.");
            System.out.println("3) Salir.");
            System.out.print("-----------------------------------------------\n");
            System.out.print("Opción: ");
            valor = in.nextInt();
            System.out.print("\n");
            switch (valor) {
                
                /* Se genera el analizadr léxico a partir del archivo alexico.flex
                   Se genera el analizadr sintáctico a partir del archivo asintactico.cup
                */
                case GENERAR: {
                    String archLexico = "";
                    String archSintactico = "";
                    if (args.length > 0) {
                        archLexico = args[0];
                        archSintactico = args[1];
                    } else {
                        archLexico = "alexico.flex";
                        archSintactico = "asintactico.cup";
                    }
                    String[] alexico = {archLexico};
                    String[] asintactico = {"-parser", "AnalizadorSintactico", archSintactico};
                    jflex.Main.main(alexico);
                    try {
                        java_cup.Main.main(asintactico);
                    } catch (Exception ex) {
                        Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    /*Se mueven los archivos generados a una carpeta Compilador
                    en src
                    */
                    boolean mvAL = moverArch("AnalizadorLexico.java");
                    boolean mvAS = moverArch("AnalizadorSintactico.java");
                    boolean mvSym= moverArch("sym.java");
                    if(mvAL && mvAS && mvSym){
                        System.exit(0);
                    }
                    System.out.println("Programa generado");
                    break;
                }
                case EJECUTAR: {
                    
                    /* Se utiliza el archivo de texto test como archivo fuente
                    del programa
                    */
                    String[] archivoPrueba = {"test.txt"};
                    try {     
                        FileWriter fichero = new FileWriter("tokens.txt", false); 
                        BufferedWriter writer = new BufferedWriter (fichero);
                        writer.write("");
                        writer.close();

                      } catch (Exception ew) {
                        ew.printStackTrace();
                      }
                    
                    analisisSemantico();
                    break;
                }
                case SALIR: {
                    break;
                }
                default: {
                    System.out.println("Opción inválida!");
                    break;
                }
            }
        } while (valor != 3);

    }
    
    public static void analisisSemantico(){
        try {
            AnalizadorSintactico asin = new AnalizadorSintactico(new AnalizadorLexico(new FileReader("test.txt")));
            InitProgram programa = (InitProgram)asin.parse().value;
            if(!asin.errores){
                AnalizadorSemantico asem = new AnalizadorSemantico(programa);
                if(asem.verficar()){
                    System.out.println("Análisis semántico exitoso.");
                }
                else{
                    System.err.println("Error en la semántica. El archivo no se puede generar.");
                }
            }
            else{
                System.err.println("Error en la sintaxis. El archivo no se puede generar.");
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Enrada:Un archivo de tipo String
    Salida:No tiene
    Restricciones: Solo recibe documentos de tipo String 
    */
    public static boolean moverArch(String archNombre) {
        boolean efectuado = false;
        File arch = new File(archNombre);
        if (arch.exists()) {
            Path currentRelativePath = Paths.get("");
            String nuevoDir = currentRelativePath.toAbsolutePath().toString()
                    + File.separator + "src" + File.separator
                    + "compilador" + File.separator + arch.getName();
            File archViejo = new File(nuevoDir);
            archViejo.delete();
            if (arch.renameTo(new File(nuevoDir))) {
                efectuado = true;
            } else {
                System.out.println("\nError al mover el archivo\n" + archNombre + " ***\n");
            }

        } else {
            System.out.println("\nCodigo no existente\n");
        }
        return efectuado;
    }
    
}
