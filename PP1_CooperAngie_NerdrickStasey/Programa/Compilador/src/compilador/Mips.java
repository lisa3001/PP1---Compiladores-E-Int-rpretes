package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author Angie Cooper
 */
public class Mips {
    private String codigo3d;
    
    public Mips(String pCodigo3d){
        codigo3d = pCodigo3d;
    }
    
    public void generarCodigo(){
        try {
            FileWriter fichero = new FileWriter("mips.asm", false); 
            BufferedWriter writer = new BufferedWriter (fichero);
            writer.write("");
            writer.close();
        }
        catch(Exception e){
            System.out.print(e);
        }
    }

    public String getCodigo3d() {
        return codigo3d;
    }

    public void setCodigo3d(String codigo3d) {
        this.codigo3d = codigo3d;
    }
    
    
    
}
