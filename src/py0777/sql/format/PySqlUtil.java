package py0777.sql.format;

public class PySqlUtil
{
  public static String replace(String argTargetString, String argFrom, String argTo)
  {
    String newStr = "";
    int lastpos = 0;
    for (;;)
    {
      int pos = argTargetString.indexOf(argFrom, lastpos);
      if (pos == -1) {
        break;
      }
      
      newStr = newStr + argTargetString.substring(lastpos, pos);
      newStr = newStr + argTo;
      lastpos = pos + argFrom.length();
    }
    
    return newStr + argTargetString.substring(lastpos);
  }
}