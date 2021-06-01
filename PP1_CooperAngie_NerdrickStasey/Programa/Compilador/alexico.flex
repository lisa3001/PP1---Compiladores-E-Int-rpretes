package compilador;

import java_cup.runtime.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter ;
import producciones.*;
      
%% //opciones
    
   
%class AnalizadorLexico


%line //Contador de lineas yyline
%column //Contador de columna yycolumn
%cup

   

%{
    ArrayList<String> identifiers = new ArrayList<String>();

    //  Guarda el tipo de token 
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    // Simbolo del token y su valor
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private void GuardarToken (String t){
        if (identifiers.size() == 0 || identifiers.indexOf(t) < 0){
            System.out.print(t + " -> IDENTIFIER |Se guardará en la tabla de símbolos con nombre, tipo, valor, ámbito y rol \n");
            identifiers.add(t);
        }else{
            System.out.print(t + " -> IDENTIFIER \n");
        }
    }

    public static void guardarTokenTxt(String contenido, String ruta)
    {
      try {     
        FileWriter fichero = new FileWriter(ruta, true); 
        BufferedWriter writer = new BufferedWriter (fichero);
        writer.write(contenido);
        writer.close();

      } catch (Exception ew) {
        ew.printStackTrace();
      }
    }

%}
   

//------------------Expresiones regulares  
POINT = \.
DIGIT = [0-9]
DIGITLIM = [1-9]
INTEGER = 0 | {DIGITLIM} {DIGIT}*
DECIMAL = (0|{INTEGER}) {POINT} 0* {INTEGER}
LETTERS = [a-zA-Z]
CHARCHAIN = \" [^\"]* \"
CHARACTER = '.'
IDENTIFIER = {LETTERS}({LETTERS}|{INTEGER})*
JUMP = \r|\n|\r\n
WHITESPACE = [ \t]|{JUMP}
InputCharacter = [^\r\n]

//comments
COMMENT = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {JUMP}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^\*] )*

%% //fin de opciones

// SIMBOLOS DEL PROGRAMA   

//Estado incial del analizador.
<YYINITIAL> {
   
  // En esta sección, para cada símbolo indica que el token declarado en la clase sym fue encontrado. */

  "," {
        System.out.print(", -> COMMA\n"); 
        guardarTokenTxt(", -> COMMA\n", "tokens.txt");
        return new Symbol(sym.COMMA, new Token(",", yyline+1, yycolumn+1));}

  ";" {
        System.out.print("; -> FINAL\n"); 
        guardarTokenTxt("; -> FINAL\n", "tokens.txt");
        return new Symbol(sym.FINAL, new Token(";", yyline+1, yycolumn+1));}

  "=" {
        System.out.print("= -> EQUAL\n");
        guardarTokenTxt("= -> EQUAL\n", "tokens.txt");
        return new Symbol(sym.EQUAL, new Token("=", yyline+1, yycolumn+1));}

  "{" {
        System.out.print("{ -> OPEN_KEY\n"); 
        guardarTokenTxt("{ -> OPEN_KEY\n", "tokens.txt");
        return new Symbol(sym.OPEN_KEY, new Token("{", yyline+1, yycolumn+1));}

  "}" {
        System.out.print("} -> CLOSE_KEY\n"); 
        guardarTokenTxt("} -> CLOSE_KEY\n", "tokens.txt");
        return new Symbol(sym.CLOSE_KEY, new Token("}", yyline+1, yycolumn+1));}

  "[" {
        System.out.print("[ -> OPEN_BRACKET\n"); 
        guardarTokenTxt("[ -> OPEN_BRACKET\n", "tokens.txt");
       return new Symbol(sym.OPEN_BRACKET, new Token("[", yyline+1, yycolumn+1));}

  "]" {
        System.out.print("] -> CLOSE_BRACKET\n");
        guardarTokenTxt("] -> CLOSE_BRACKET\n", "tokens.txt");
        return new Symbol(sym.CLOSE_BRACKET, new Token("]", yyline+1, yycolumn+1));}

  "(" {
        System.out.print("( -> OPEN_PAREN\n");
        guardarTokenTxt("( -> OPEN_PAREN\n", "tokens.txt");
        return new Symbol(sym.OPEN_PAREN, new Token("(", yyline+1, yycolumn+1));}

  ")" {
        System.out.print(") -> CLOSE_PAREN\n"); 
        guardarTokenTxt(") -> CLOSE_PAREN\n", "tokens.txt");
        return new Symbol(sym.CLOSE_PAREN, new Token(")", yyline+1, yycolumn+1));}

  //  Operadores
  "+" {
        System.out.print("+ -> PLUS\n");
        guardarTokenTxt("+ -> PLUS\n", "tokens.txt");
        return new Symbol(sym.PLUS, new Token("+", yyline+1, yycolumn+1));}

  "-" {
        System.out.print("- -> MINUS\n");
        guardarTokenTxt("- -> MINUS\n", "tokens.txt");
        return new Symbol(sym.MINUS, new Token("-", yyline+1, yycolumn+1));}

  "/" {
        System.out.print("/ -> DIVIDE\n");
        guardarTokenTxt("/ -> DIVIDE\n", "tokens.txt");
        return new Symbol(sym.DIVIDE, new Token("/", yyline+1, yycolumn+1));}

  "*" {
        System.out.print("* -> MULTI\n");
        guardarTokenTxt("* -> MULTI\n", "tokens.txt");
        return new Symbol(sym.MULTI, new Token("*", yyline+1, yycolumn+1));}

  "~" {
        System.out.print("~ -> MODULE\n");
        guardarTokenTxt("~ -> MODULE\n", "tokens.txt");
        return new Symbol(sym.MODULE, new Token("~", yyline+1, yycolumn+1));}

  "**" {
        System.out.print("** -> POWER\n");
        guardarTokenTxt("** -> POWER\n", "tokens.txt");
        return new Symbol(sym.POWER, new Token("**", yyline+1, yycolumn+1));}

  //Operadores unarios
  "++" {
        System.out.print("++ -> PLUS_PLUS\n");
        guardarTokenTxt("++ -> PLUS_PLUS\n", "tokens.txt");
        return new Symbol(sym.PLUS_PLUS, new Token("++", yyline+1, yycolumn+1));}

  "--" {
        System.out.print("-- -> MINUS_MINUS\n");
        guardarTokenTxt("-- -> MINUS_MINUS\n", "tokens.txt");
        return new Symbol(sym.MINUS_MINUS, new Token("--", yyline+1, yycolumn+1));}

  //Operadores relacionales
  ">" {
        System.out.print("> -> GREATER\n");
        guardarTokenTxt("> -> GREATER\n", "tokens.txt");
        return new Symbol(sym.GREATER, new Token(">", yyline+1, yycolumn+1));}

  ">=" {
        System.out.print(">= -> GREATER_EQUAL\n");
        guardarTokenTxt(">= -> GREATER_EQUAL\n", "tokens.txt");
        return new Symbol(sym.GREATER_EQUAL, new Token(">=", yyline+1, yycolumn+1));}

  "<" {
        System.out.print("< -> MINOR\n");
        guardarTokenTxt("< -> MINOR\n", "tokens.txt");
        return new Symbol(sym.MINOR, new Token("<", yyline+1, yycolumn+1));}

  "<=" {
        System.out.print("<= -> MINOR_EQUAL\n");
        guardarTokenTxt("<= -> MINOR_EQUAL\n", "tokens.txt");
        return new Symbol(sym.MINOR_EQUAL, new Token("<=", yyline+1, yycolumn+1));}

  "==" {
        System.out.print("== -> EQUAL_EQUAL\n");
        guardarTokenTxt("== -> EQUAL_EQUAL\n", "tokens.txt");
        return new Symbol(sym.EQUAL_EQUAL, new Token("==", yyline+1, yycolumn+1));}

  "!=" {
        System.out.print("!= -> DIFFERENT\n");
        guardarTokenTxt("!= -> DIFFERENT\n", "tokens.txt");
        return new Symbol(sym.DIFFERENT, new Token("!=", yyline+1, yycolumn+1));}

  //Operadores lógicos
  "&" {
        System.out.print("& -> AND\n");
        guardarTokenTxt("& -> AND\n", "tokens.txt");
        return new Symbol(sym.AND, new Token("&", yyline+1, yycolumn+1));}

  "|" {
        System.out.print("| -> OR\n");
        guardarTokenTxt("| -> OR\n", "tokens.txt");
        return new Symbol(sym.OR, new Token("|", yyline+1, yycolumn+1));}

  "main" {
        System.out.print("main -> MAIN\n");
        guardarTokenTxt("main -> MAIN\n", "tokens.txt");
        return new Symbol(sym.MAIN, new Token("main", yyline+1, yycolumn+1));}

  "not" {
        System.out.print("not -> NOT\n"); 
        guardarTokenTxt("not -> NOT\n", "tokens.txt");
        return new Symbol(sym.NOT, new Token("not", yyline+1, yycolumn+1));}

  "return" {
        System.out.print("return -> RETURN\n"); 
        guardarTokenTxt("return -> RETURN\n", "tokens.txt");
        return new Symbol(sym.RETURN, new Token("return", yyline+1, yycolumn+1));}

  "read" {
        System.out.print("read -> READ\n");
        guardarTokenTxt("read -> READ\n", "tokens.txt");
        return new Symbol(sym.READ, new Token("read", yyline+1, yycolumn+1));}

  "print" {
        System.out.print("print -> PRINT\n"); 
        guardarTokenTxt("print -> PRINT\n", "tokens.txt");
        return new Symbol(sym.PRINT, new Token("print", yyline+1, yycolumn+1));}

  "true" {
        System.out.print("true -> TRUE\n"); 
        guardarTokenTxt("true -> TRUE\n", "tokens.txt");
        return new Symbol(sym.TRUE, new Token("true", yyline+1, yycolumn+1));}

  "false" {
        System.out.print("false -> FALSE\n"); 
        guardarTokenTxt("false -> FALSE\n", "tokens.txt");
        return new Symbol(sym.FALSE, new Token("false", yyline+1, yycolumn+1));}
  
  //Tipos
  "int" {
        System.out.print("int -> INT\n"); 
        guardarTokenTxt("int -> INT\n", "tokens.txt");
        return new Symbol(sym.INT, new Token("int", yyline+1, yycolumn+1));}

  "float" {
        System.out.print("float -> FLOAT\n");
        guardarTokenTxt("float -> FLOAT\n", "tokens.txt");
        return new Symbol(sym.FLOAT, new Token("float", yyline+1, yycolumn+1));}

  "bool" {
        System.out.print("bool -> BOOL\n"); 
        guardarTokenTxt("bool -> BOOL\n", "tokens.txt");
        return new Symbol(sym.BOOL, new Token("bool", yyline+1, yycolumn+1));}

  "char" {
        System.out.print("char -> CHAR\n"); 
        guardarTokenTxt("char -> CHAR\n", "tokens.txt");
        return new Symbol(sym.CHAR, new Token("char", yyline+1, yycolumn+1));}

  "string" {
        System.out.print("string -> STRING\n"); 
        guardarTokenTxt("string -> STRING\n", "tokens.txt");
        return new Symbol(sym.STRING, new Token("string", yyline+1, yycolumn+1));}

  "null" {
        System.out.print("null -> NULL\n"); 
        guardarTokenTxt("null -> NULL\n", "tokens.txt");
        return new Symbol(sym.NULL, new Token("null", yyline+1, yycolumn+1));}


  //Condicionales
  "if" {
        System.out.print("if -> IF\n"); 
        guardarTokenTxt("if -> IF\n", "tokens.txt");
        return new Symbol(sym.IF, new Token("if", yyline+1, yycolumn+1));}

  "else" {
        System.out.print("else -> ELSE\n");
        guardarTokenTxt("else -> ELSE\n", "tokens.txt");
        return new Symbol(sym.ELSE, new Token("else", yyline+1, yycolumn+1));}

  "elif" {
        System.out.print("elif -> ELIF\n");
        guardarTokenTxt("elif -> ELIF\n", "tokens.txt");
        return new Symbol(sym.ELIF, new Token("elif", yyline+1, yycolumn+1));}

  "for" {
        System.out.print("for -> FOR\n");
        guardarTokenTxt("for -> FOR\n", "tokens.txt");
        return new Symbol(sym.FOR, new Token("for", yyline+1, yycolumn+1));}

  "break" {
        System.out.print("break -> BREAK\n");
        guardarTokenTxt("break -> BREAK\n", "tokens.txt");
        return new Symbol(sym.BREAK, new Token("break", yyline+1, yycolumn+1));}
 
   
  // YYTEXT DE EXPRESIONES REGULARES

  /* yytext es el token encontrado.
     Al encontrar una expresión regular se imprime su valor (el valor que se obtuvo de
     la cadena yytext) y este valor se convierte al tipo correspondiente.  
  
  */

  {INTEGER}      {  System.out.print(yytext()+ " -> INTEGER\n"); 
                    guardarTokenTxt(yytext()+ " -> INTEGER\n", "tokens.txt");
                    return new Symbol(sym.INTEGER, new Token(new Integer(Integer.parseInt(yytext())), yyline+1, yycolumn+1)); }


  {DECIMAL}      {   System.out.print(yytext()+ " -> DECIMAL\n"); 
                    guardarTokenTxt(yytext()+ " -> DECIMAL\n", "tokens.txt");
                    return new Symbol(sym.DECIMAL, new Token(new Float(Float.parseFloat(yytext())), yyline+1, yycolumn+1)); }


  {CHARCHAIN}    {   System.out.print(yytext() + " -> CHARCHAIN\n"); 
                    guardarTokenTxt(yytext()+ " -> CHARCHAIN\n", "tokens.txt");
                    return new Symbol(sym.CHARCHAIN, new Token(yytext(), yyline+1, yycolumn+1)); }

  {CHARACTER}    {   System.out.print(yytext() + " -> CHARACTER\n");
                    guardarTokenTxt(yytext()+ " -> CHARACTER\n", "tokens.txt"); 
                    return new Symbol(sym.CHARACTER, new Token(yytext(), yyline+1, yycolumn+1)); }


  {IDENTIFIER}   {  System.out.print(yytext()+ " -> IDENTIFIER\n");
                    guardarTokenTxt(yytext()+ " -> IDENTIFIER\n", "tokens.txt");
                    return new Symbol(sym.IDENTIFIER, new Token(yytext(), yyline+1, yycolumn+1)); }


  {COMMENT}      { /* ignora el espacio */ } 
  {WHITESPACE}   { /* ignora el espacio */ } 
}

//Si el token contenido en la entrada no coincide con ninguna regla entonces se marca un token ilegal
[^]              {System.err.print("\nError Léxico -> Caracter ilegal <"+yytext()+"> Línea: "+yyline+" Columna: "+yycolumn+"\n"); }
