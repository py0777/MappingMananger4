package py0777.sql.format.valueobject;

import blanco.commons.sql.format.valueobject.AbstractBlancoSqlToken;

public class PySqlToken
  extends AbstractBlancoSqlToken
{
  public PySqlToken(int argType, String argString, int argPos)
  {
    setType(argType);
    setString(argString);
    setPos(argPos);
  }

  public PySqlToken(int argType, String argString)
  {
    this(argType, argString, -1);
  }
}