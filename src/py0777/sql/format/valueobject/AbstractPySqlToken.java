package py0777.sql.format.valueobject;

public class AbstractPySqlToken
{
  private int fType;
 
  private String fString;
  
  private int fPos = -1;
  
  public void setType(int argType)
  {
    fType = argType;
  }
 
  public int getType()
  {
    return fType;
  }

  public void setString(String argString)
  {
    fString = argString;
  }
  
  public String getString()
  {
    return fString;
  }
  
  public void setPos(int argPos)
  {
    fPos = argPos;
  }

  public int getPos()
  {
    return fPos;
  }
  
  public String toString()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("blanco.commons.sql.format.valueobject.AbstractBlancoSqlToken[");
    buf.append("type=" + fType);
    buf.append(",string=" + fString);
    buf.append(",pos=" + fPos);
    buf.append("]");
    return buf.toString();
  }
}