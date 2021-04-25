package compilador;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
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
