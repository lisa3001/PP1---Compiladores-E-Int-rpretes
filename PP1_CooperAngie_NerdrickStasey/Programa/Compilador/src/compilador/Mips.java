package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author Angie Cooper
 */
public class Mips {
    private String[] codigo3d;
    private String data = ".data\n";
    private ArrayList<String> datos = new ArrayList<String>();
    private String[] sentences;
    private String main = "\n.text\n.globl main\n";
    private String funciones = "";
    private ArrayList<Object> bloquemain = new ArrayList();
    private ArrayList<Object> bloquefunc = new ArrayList();
    private int registroA = 0;
    //Se precargan las funciones de print(string y entero)
    private String printFunctions = "print_str:\n" +
"	li $v0, 4\n" +
"     	syscall  \n" +
"	jr $ra\n" +
        "print_int:\n" +
"	li $v0, 1\n" +
"     	syscall  \n" +
"	jr $ra\n";
    //Se precargan las funciones de read(string y entero)
   private String readFunctions = "read_int:\n" +
"	li $v0, 5\n" +
"	syscall\n" +
"	jr $ra\n" +
        "read_str:\n" +
"	li $v0, 8\n" +
"	syscall\n" +
"	jr $ra\n";
 
    //Se precarga la función de almacenar el contenido de los registros en la pila
   private String guardarRegistros = "guardarRegistros:\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t0, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t1, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t2, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t3, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t4, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t5, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t6, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t7, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t8, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +
"     sw  $t9, 0($sp)\n" +
"     sub $sp, $sp, 4\n" +  
"     sw  $v1, 0($sp)\n" +
"     jr $ra\n";
   
   //Se precarga la función de cargar el contenido de la pila en los registros
   private String cargarRegistros = "cargarRegistros:\n" +
"     lw $v1, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t9, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t8, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t7, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t6, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t5, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t4, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t3, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t2, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t1, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     lw $t0, 0($sp)\n" +
"     addi $sp, $sp, 4\n" +
"     jr $ra\n";
   
   //Se precarga la función de pontencia
   private String potenciaFunc = "Potencia:\n" +
"	move $v1, $ra\n" +
"	move $t1, $a0\n" +
"	move $t2, $a1\n" +
"	seq $t3, $t2, $zero\n" +
"	bgtz $t3, ExponenteCero\n" +
"	li $t4, 1\n" +
"	seq $t3, $t2, $t4\n" +
"	neg $t3, $t3\n" +
"	beq $t3, -1 ,ExponenteUno\n" +
"	sub $t2, $t2, 1 \n" +
"	move $t4, $t1\n" +
"	li   $t5, 0\n" +
"	jal ForPotencia\n" +
"\n" +
"ExponenteCero:\n" +
"	li $v0, 1 \n" +
"	jr $v1\n" +
"\n" +
"ExponenteUno:\n" +
"	move $v0, $t1 \n" +
"	jr $v1\n" +
"\n" +
"ForPotencia:\n" +
"	seq $t3, $t5, $t2\n" +
"	bgtz $t3, FinForPotencia\n" +
"	mulo $t4, $t4, $t1\n" +
"	addi $t5, $t5, 1\n" +
"	jal ForPotencia\n" +
"\n" +
"FinForPotencia:\n" +
"	move $v0, $t4 \n" +
"	jr $v1\n";
   
    
    
    //Constructor de la clase
    public Mips(String pCodigo3d){
        codigo3d = pCodigo3d.split("\n");
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de limpiar el archivo en el que se va a escribir el código en 3 direcciones
    public void limpiarArchivo(){
        try {     
            FileWriter fichero = new FileWriter("mips.s", false); 
            BufferedWriter writer = new BufferedWriter (fichero);
            writer.write("");
            writer.close();

          } catch (Exception ew) {
            ew.printStackTrace();
          }
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de escribir en un archivo de texto el código en 3 direcciones generado
    public void guardarCodigoMips()
    {
      try {    
        limpiarArchivo();
        FileWriter fichero = new FileWriter("mips.s", true); 
        BufferedWriter writer = new BufferedWriter (fichero);
        writer.write(data + main + funciones);
        writer.close();

      } catch (Exception ew) {
        ew.printStackTrace();
      }
    }
    
   //Entrada: No tiene
   //Salida: No tiene
    //Objetivo: Leer el código intermedio y generar el código mips
    public void generarCodigoMips(){
      int esMain = 0;
      for (String linea: codigo3d){
          if (linea.contains("func begin")){
              String[] instruccion = linea.split(" ");
              String function = isFunction(instruccion);
              if (function.equals("main")){
                  main += function + ":\n";
                  esMain = 1;
              }
              else{
                  funciones += function +":\n";
                  funciones += "     move $v1, $ra\n";
                  esMain = 0;
              }
              registroA = 0;
          }
          //RETURN
          //Objetivo: Crear instrucciones de return mips, a partir de código intermedio
          else if(linea.contains("return") && !linea.contains("=")){
              String[] instruccion = linea.split(" ");
              instruccion[0] = instruccion[0].trim();
              instruccion[1] = instruccion[1].trim();
              String inst = "";
              String registro = obtenerRegistro(instruccion[1]);
              inst += "     move  ";
              inst += "$v0" + ", $" + registro;
              if (esMain == 1)main += inst + "\n";
              else{
                  inst += "\n     jr $v1";
                  funciones += inst + "\n";
              } 
          }
          //Parámetros de función
          //Objetivo: Crear instrucciones de parámetros de funciones, a partir de código intermedio
          else if((linea.split(" "))[0].contains("_fpa")){
              String[] instruccion = linea.split(" ");
              String registroTemp = instruccion[0].replace("_fpa", "");
              String registro = obtenerRegistro(registroTemp);
              String inst = "";
              inst += "     move $"+ registro+ ", $a" +registroA;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";   
              registroA+=1;
          }
          else if( linea.contains("createarray") ){
              String[] instruccion = linea.split("_");
              String nombre = instruccion[1];
              String largo = instruccion[2];
              String inst = nombre + ":      .space " + largo + "\n";
              data += inst;
          }
          //Parámetros para llamar función
          //Objetivo: Se guardan el valor de los parámetros para utilizarlos cuandos se llaman funciones
          else if( linea.contains("param") && linea.contains("=") && !linea.contains("print_param") && !linea.contains("read_param")){
              
              String[] instruccion = linea.split(" ");
              String[] registro = instruccion[0].split("_");
              String numRegistro = registro[0].replace("param", "");
              String result = "";
              String tipoInstruccion = "";
              try{
                  int num = Integer.parseInt(instruccion[2]);
                  result += instruccion[2];
                  tipoInstruccion += "li";
              }catch (Exception e){
                  String regis = obtenerRegistro(instruccion[2]);
                  result += "$" + regis;
                  tipoInstruccion += "move";
              }
              
              String inst = "";
              inst += "     "+tipoInstruccion+" $a"+ numRegistro+ ", " +result;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";               
          }
          //Llamar una función
          //Objetivo: Se realiza un read, se reliza un print o se llama una fucnión, dependiendo de la instrucción
          else if( linea.contains("call") ){
              String[] instruccion = linea.split(" ");
              String inst = "";
              if (instruccion[1].contains("print") || instruccion[1].contains("read")){
                  inst += "     jal "+ instruccion[1];
              }else{
                  inst += "     jal guardarRegistros\n";
                  inst += "     jal "+ instruccion[1] + "\n";
                  inst += "     jal cargarRegistros";
              }
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";  
          }
          //If go
          //Objetivo: Realiza un if al verficar una operación con 1 o 0, dependiendo de si se usa en
          //un IF o un FOR
          else if(linea.contains("if_go")){
              String[] instruccion = linea.split(" ");
              String temp = instruccion[1].replace("(", "");
              String cond = temp.replace(")", "");
              String registro = obtenerRegistro(cond);
              String temp2 = instruccion[3].replace("(", "");
              String tack = temp2.replace(")", "");   
              String inst = "";
              if(linea.contains("end_for")){
                inst += "     beq $"+ registro+ ", 0, "+tack;
              }else{
                inst += "     beq $"+ registro+ ", 1, "+tack;
              }
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          //GO TO
          //Objetivo: Realiza un salto a la etiqueta indicada en la linea
          else if(linea.contains("goto")){
              String[] instruccion = linea.split(" ");
              String temp = instruccion[1].replace("(", "");
              String tack = temp.replace(")", "");
              String inst = "";
              inst += "     j "+tack;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          //Crea etiquetas
          //Objetivo: Crea una etiqueta con el valor de la línea
          else if(linea.contains("IF_") || linea.contains("ELIF_") || linea.contains("ELSE_") || linea.contains("FOR_")
                  || linea.contains("end_for") ){
              String inst = "";
              inst += linea;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          //Intrucciones que tienen el símbolo =
          //Objetivo: Crea un split en las instrucciones que tienen una igualdad 
          //para realizar acciones a partir de esto
          else if (linea.contains("=") ){
              String[] instruccion = linea.split("=");
              instruccion[0] = instruccion[0].trim();
              instruccion[1] = instruccion[1].trim();
              String inst = "";
              String registro = obtenerRegistro(instruccion[0]);
              //Guarda valor de strings
              //Objetivo: Sí el elemento es de tipo string lo guarda en el .data de mips
              if (instruccion[1].contains("\"")){
                  inst += "     la  ";
                  data += instruccion[0] + ":   .asciiz " + instruccion[1] + "\n";
                  inst += "$" + registro + ", " + instruccion[0];
              //Guarda valor de chars
              //Objetivo: Sí el elemento es de tipo char lo guarda en el .data de mips
              }else if ( instruccion[1].contains("\'") ){
                  inst += "     la  ";
                  data += instruccion[0] + ":   .byte " + instruccion[1] + "\n";
                  inst += "$" + registro + ", " + instruccion[0];
              //Gestiona cuando se encuentra un null
              //Objetivo: Colocar la instrucción indicada dependiendo del nulo
              }else if (instruccion[1].contains("null")){
                  if (instruccion[1].contains("null_str")){
                    inst += "     la  ";
                    data += instruccion[0] + ":   .space 50 \n";
                    inst += "$" + registro + ", " + instruccion[0];
                  }else{
                    inst += "     li  ";
                    inst += "$" + registro + ", 0";   
                  }
              }else{
                  //SUMA
                  //Objetivo: Crea la instrucción de suma para mips, a partir del código intermedio
                  if (instruccion[1].contains("+") && !instruccion[1].contains("++")){
                     String[] operandos = instruccion[1].split("\\+"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     add ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  }
                  //RESTA
                  //Objetivo: Crea la instrucción de resta para mips, a partir del código intermedio
                  else if (instruccion[1].contains("-")){
                     instruccion[1] = instruccion[1].trim();
                     String[] operandos = instruccion[1].split("\\-");
                     if (!operandos[0].equals("")){
                        String operando1 = obtenerRegistro(operandos[0]);
                        String operando2 = obtenerRegistro(operandos[1]);
                        inst += "     sub ";
                        inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                     }else{
                         inst += "     mulo ";
                         inst += "$" + registro + ", $" + operandos[1] + ",-1";
                     } 
                  //MULTIPLICACIÓN
                  //Objetivo: Crea la instrucción de multiplicación para mips, a partir del código intermedio
                  }else if (instruccion[1].contains("*") && !instruccion[1].contains("**")){
                     String[] operandos = instruccion[1].split("\\*"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     mulo ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //POTENCIA
                  //Objetivo: Crea la instrucción de potencia para mips, a partir del código intermedio
                  }else if (instruccion[1].contains("**")){
                     String[] operandos = instruccion[1].split(" "); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[2]);
                     inst += "     move $a0, $" + operando1 + "\n";
                     inst += "     move $a1, $" + operando2 + "\n";
                     inst += "     jal guardarRegistros \n";
                     inst += "     jal Potencia \n";
                     inst += "     jal cargarRegistros \n";
                     inst += "     move ";
                     inst += "$" + registro + ", $v0";
                  //DIVISIÓN
                  //Objetivo: Crea la instrucción de división para mips, a partir del código intermedio
                  }else if (instruccion[1].contains("/")){
                     String[] operandos = instruccion[1].split("/"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     div ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //SUMA UNARIA
                  //Objetivo: Crea la instrucción de suma unaria para mips, a partir del código intermedio
                  }else if (instruccion[1].contains("++")){
                     String[] operandos = instruccion[1].split("\\++");
                     String operando1 = obtenerRegistro(operandos[1]);
                     inst += "     add ";
                     inst += "$" + registro + ", $" + operando1 + ", 1";
                  //RESTA UNARIA
                  //Objetivo: Crea la instrucción de resta unaria para mips, a partir del código intermedio
                  }else if (instruccion[1].contains("--")){
                     String[] operandos = instruccion[1].split("\\--");
                     String operando1 = obtenerRegistro(operandos[1]);
                     inst += "     sub ";
                     inst += "$" + registro + ", $" + operando1 + ", 1";
                  //AND
                  //Objetivo: Crea la instrucción de and para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("&")){
                     String[] operandos = instruccion[1].split("\\&"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     and ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //OR
                  //Objetivo: Crea la instrucción or para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("|")){
                     String[] operandos = instruccion[1].split("\\|"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     or ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //MÓDULO
                  //Objetivo: Crea la instrucción de módulo para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("~") ){
                     String[] operandos = instruccion[1].split("\\~"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     rem ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //MENOR (operación relacional)
                  //Objetivo: Crea la instrucción de menor para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("<") && !instruccion[1].contains("<<") ){
                     String[] operandos = instruccion[1].split("\\<"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sltu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //MENOR O IGUAL (operación relacional)
                  //Objetivo: Crea la instrucción de menor o igual para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("<<") ){
                     String[] operandos = instruccion[1].split("\\<<"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sleu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //MAYOR (operación relacional)
                  //Objetivo: Crea la instrucción de mayor para mips, a partir del código intermedio
                   }else if ( instruccion[1].contains(">") && !instruccion[1].contains(">>") ){
                     String[] operandos = instruccion[1].split("\\>"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sgtu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //MAYOR O IGUAL (operación relacional)
                  //Objetivo: Crea la instrucción de mayor o igual para mips, a partir del código intermedio
                   }else if (instruccion[1].contains(">>") ){
                     String[] operandos = instruccion[1].split("\\>>"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sgeu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //IGULADAD (operación relacional)
                  //Objetivo: Crea la instrucción de igualdad para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("@@") ){
                     String[] operandos = instruccion[1].split("\\@@"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     seq ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //DIFERENTE (operación relacional)
                  //Objetivo: Crea la instrucción de diferente para mips, a partir del código intermedio
                   }else if (instruccion[1].contains("!@") ){
                     String[] operandos = instruccion[1].split("\\!@"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sne ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  //Parámetros de la función print
                  //Objetivo: Crear la instrucción para almacenar el valor a imprimir en los registros de los parámetro
                  }else if (instruccion[0].contains("print_param") ){ 
                     String operando1 = obtenerRegistro(instruccion[1]);
                     inst += "     move ";
                     inst += "$a0" + ", $" + operando1;
                  //Parámetros de la función read
                  //Objetivo: Crear la instrucción para almacenar el valor obtenido de la función read en los registros de los parámetro
                  }else if (instruccion[0].contains("read_param") ){ 
                     String operando1 = obtenerRegistro(instruccion[1]);
                     inst += "     move ";
                     inst += "$a0" + ", $" + operando1 + "\n";
                     inst += "     li ";
                     inst += "$a1" + ", 50";
                  //Parámetros de la función print
                  //Objetivo: Crear la instrucción para guardar el retorno de una llamada a una función
                  }else if (instruccion[1].equals("return")){
                     inst += "     move ";
                     inst += "$" + registro + ", $v0";
                  }else{
                      String asignacion = obtenerRegistro(instruccion[1]);
                      if (asignacion != ""){
                         inst += "     move  ";
                         inst += "$" + registro + ", $" + asignacion;
                      }else{
                         inst += "     li  ";
                         inst += "$" + registro + ", " + instruccion[1]; 
                      }
                  }
              }
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n"; 
          }
      }
      main += "     j end\n";
      funciones += printFunctions;
      funciones += readFunctions;
      funciones += guardarRegistros;
      funciones += cargarRegistros;
      funciones += potenciaFunc;
      funciones += "end:\n      li $v0, 10\n       syscall";
      guardarCodigoMips();
    }
    
    //Entrada: Un array
    //Salida: String con el nombre de la función, si la linea es el inicio de una función
    //Objetivo: Verificar si una línea del código intermedio es el inicio de una función
    public String isFunction(String[] inst){
        String resultado = "";
        if (inst.length >= 3){
          if (inst[0].equals("func") && inst[1].equals("begin")) resultado = inst[2];  
        }
        return resultado;
    }
    
    //Entrada: Un char
    //Salida: Booleano
    //Objetivo: Verifica si el char se puede convertir a número entero.
    public boolean isInteger(char var){
        try 
        {
            Integer.parseInt(String.valueOf(var));
            return true;
        } 
        catch (NumberFormatException e) 
        {
            return false;
        }
    }
    
    //Entrada: Una etiqueta(string)
    //Salida: Registro de mips(string)
    //Objetivo: Obtener el registro de la etiqueta recibida
    public String obtenerRegistro(String etiqueta){
        String resultado = "";
        int contador = etiqueta.lastIndexOf("t");
        boolean probar = false;
        if (contador >= 0){
            if (contador < etiqueta.length() - 1){
                if (!isInteger(etiqueta.charAt(contador + 1))){
                    contador = -1;
                    probar = true;
                }
            }
        }else probar = true;
        if (probar){
            contador = etiqueta.lastIndexOf("s");
            if (contador >= 0){
                if (contador < etiqueta.length() - 1){
                    if (!isInteger(etiqueta.charAt(contador + 1))){
                        contador = -1;
                    }
                }
            }
        }
        if (contador >= 0){
            while(contador<etiqueta.length()){
            if (etiqueta.charAt(contador) == '_') contador = etiqueta.length();
            else{
                resultado += etiqueta.charAt(contador);
                contador += 1;
            }
         }  
        }
        return resultado;
    }
    
}
