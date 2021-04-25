package compilador;

import java_cup.runtime.*;
import java.io.Reader;
      
%% //opciones
    
   
%class AnalizadorLexico


%line //Contador de lineas yyline
%column //Contador de columna yycolumn
%cup
   

%{
    //  Guarda el tipo de token 
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    // Simbolo del token y su valor
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
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

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {JUMP}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^\*] )*

%% //fin de opciones



  {INTEGER}      {   System.out.print(yytext()+ " -> INTEGER\n"); 
                    return symbol(sym.INTEGER, new Integer(yytext())); }
  {DECIMAL}      {   System.out.print(yytext()+ " -> DECIMAL\n"); 
                    return symbol(sym.DECIMAL, new Double(yytext())); }
  {CHARCHAIN}    {   System.out.print(yytext() + " -> CHARCHAIN\n"); 
                    return symbol(sym.CHARCHAIN, yytext()); }
  {CHARACTER}    {   System.out.print(yytext() + " -> CHARACTER\n"); 
                    return symbol(sym.CHARACTER, yytext()); }
  {IDENTIFIER}   {   System.out.print(yytext() + " -> IDENTIFIER\n"); 
                    return symbol(sym.IDENTIFIER, yytext()); }
  {Comment}      { /* ignora el espacio */ } 
  {WHITESPACE}   { /* ignora el espacio */ } 
}

/* Si el token contenido en la entrada no coincide con ninguna regla
    entonces se marca un token ilegal */
[^]              {System.err.print("\nError Léxico -> Caracter ilegal <"+yytext()+"> Línea: "+yyline+" Columna: "+yycolumn+"\n"); }
