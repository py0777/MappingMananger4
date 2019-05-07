package py0777.sql.format;

import blanco.commons.sql.format.BlancoSqlConstants;

import java.util.ArrayList;
import java.util.List;

import py0777.sql.format.valueobject.PySqlToken;


public class PySqlParser
{
  private String fBefore;
  private char fChar;
  private int fPos;
  private static final String[] twoCharacterSymbol = { "<>", "<=", ">=", "||" };
  

  public static boolean isSpace(char argChar)
  {
    return (argChar == ' ') || (argChar == '\t') || (argChar == '\n') || (argChar == '\r') || (argChar == 65535);
  }
  
  public static boolean isLetter(char argChar)
  {
    if (isSpace(argChar)) {
      return false;
    }
    if (isDigit(argChar)) {
      return false;
    }
    if (isSymbol(argChar)) {
      return false;
    }
    return true;
  }
  

  public static boolean isDigit(char argChar)
  {
    return ('0' <= argChar) && (argChar <= '9');
  }
  

  public static boolean isSymbol(char argChar)
  {
    switch (argChar)
    {
    case '"': 
    case '%': 
    case '&': 
    case '\'': 
    case '(': 
    case ')': 
    case '*': 
    case '+': 
    case ',': 
    case '-': 
    case '.': 
    case '/': 
    case ':': 
    case ';': 
    case '<': 
    case '=': 
    case '>': 
    case '?': 
    case '|':
    case '{':
    case '}':
    case '#':
      return true; }
    
    return false;
  }
  

  PySqlToken nextToken()
  {
    int start_pos = fPos;
    if (fPos >= fBefore.length()) {
      fPos += 1;
      return new PySqlToken(6, "", start_pos);
    }
    

    fChar = fBefore.charAt(fPos);
    
    if (isSpace(fChar)) {
      String workString = "";
      do {
        workString = workString + fChar;
        fChar = fBefore.charAt(fPos);
        if (!isSpace(fChar)) {
          return new PySqlToken(0, workString, start_pos);
        }
        
        fPos += 1;
      } while (fPos < fBefore.length());
      return new PySqlToken(0, workString, start_pos);
    }
    

    if (fChar == ';') {
      fPos += 1;
      
      return new PySqlToken(1, ";", start_pos);
    }
    if (isDigit(fChar)) {
      String s = "";
      while ((isDigit(fChar)) || (fChar == '.'))
      {
        s = s + fChar;
        fPos += 1;
        
        if (fPos >= fBefore.length()) {
          break;
        }
        

        fChar = fBefore.charAt(fPos);
      }
      return new PySqlToken(4, s, start_pos);
    }
    if (isLetter(fChar)) {
      String s = "";
      
      while ((isLetter(fChar)) || (isDigit(fChar)) || (fChar == '.')) {
        s = s + fChar;
        fPos += 1;
        if (fPos >= fBefore.length()) {
          break;
        }
        
        fChar = fBefore.charAt(fPos);
      }
      for (int i = 0; i < BlancoSqlConstants.SQL_RESERVED_WORDS.length; i++) {
        if (s.compareToIgnoreCase(BlancoSqlConstants.SQL_RESERVED_WORDS[i]) == 0)
        {
          return new PySqlToken(2, s, start_pos);
        }
      }
      
      return new PySqlToken(3, s, start_pos);
    }
    

    if (fChar == '-') {
      fPos += 1;
      char ch2 = fBefore.charAt(fPos);
      
      if (ch2 != '-') {
        return new PySqlToken(1, "-", start_pos);
      }
      
      fPos += 1;
      String s = "--";
      do {
        fChar = fBefore.charAt(fPos);
        s = s + fChar;
        fPos += 1;
      } while ((fChar != '\n') && (fPos < fBefore.length()));
      return new PySqlToken(5, s, start_pos);
    }
    



    if (fChar == '/') {
      fPos += 1;
      char ch2 = fBefore.charAt(fPos);
      
      if (ch2 != '*') {
        return new PySqlToken(1, "/", start_pos);
      }
      

      String s = "/*";
      fPos += 1;
      int ch0 = -1;
      do {
        ch0 = fChar;
        fChar = fBefore.charAt(fPos);
        s = s + fChar;
        fPos += 1;
      } while ((ch0 != 42) || (fChar != '/'));
      return new PySqlToken(5, s, start_pos);
    }
    
    /*INPUT값 신규추가 시작*/
    if (fChar == '#') {
        fPos += 1;
        char ch2 = fBefore.charAt(fPos);
        
        if (ch2 != '{') {
          return new PySqlToken(1, "#", start_pos);
        }
        
        String s = "#{";
        fPos += 1;
        int ch0 = -1;
        do {
          ch0 = fChar;
          fChar = fBefore.charAt(fPos);
          System.out.println("[fChar]["+fChar+"]["+ ch0+"]");
          /* 개행문자는 삭제함*/
          if(!isSpace(fChar)){
        	  s = s + fChar;
          }
          
          fPos += 1;

        } while ( fChar != '}');
        return new PySqlToken(8, s, start_pos);
      }

    
    if (fChar == ':') {

        String s = "";
        
        do {
          fChar = fBefore.charAt(fPos);
          s = s + fChar;
          fPos += 1;         
        } while (!isSpace(fChar));
        return new PySqlToken(8, s, start_pos);
      }
    /*INPUT값 신규추가 종료*/ 
    
    if (fChar == '\'') {
      fPos += 1;
      String s = "'";
      do {
        fChar = fBefore.charAt(fPos);
        s = s + fChar;
        fPos += 1;
      } while (fChar != '\'');
      return new PySqlToken(4, s, start_pos);
    }
    

    if (fChar == '"') {
      fPos += 1;
      String s = "\"";
      do {
        fChar = fBefore.charAt(fPos);
        s = s + fChar;
        fPos += 1;
      } while (fChar != '"');
      return new PySqlToken(3, s, start_pos);
    }
    

    if (isSymbol(fChar))
    {
      String s = "" + fChar;
      fPos += 1;
      if (fPos >= fBefore.length()) {
        return new PySqlToken(1, s, start_pos);
      }
      

      char ch2 = fBefore.charAt(fPos);
      for (int i = 0; i < twoCharacterSymbol.length; i++) {
        if ((twoCharacterSymbol[i].charAt(0) == fChar) && (twoCharacterSymbol[i].charAt(1) == ch2))
        {
          fPos += 1;
          s = s + ch2;
          break;
        }
      }
      return new PySqlToken(1, s, start_pos);
    }
    
    fPos += 1;
    return new PySqlToken(7, "" + fChar, start_pos);
  }
  

  public List<PySqlToken> parse(String argSql)
  {
    fPos = 0;
    fBefore = argSql;
    
    List<PySqlToken> list = new ArrayList();
    for (;;) {
      PySqlToken token = nextToken();
      if (token.getType() == 6) {
        break;
      }
      
      list.add(token);
    }
    return list;
  }
}
