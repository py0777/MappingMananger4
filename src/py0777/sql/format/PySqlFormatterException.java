package py0777.sql.format;

import java.io.IOException;

public class PySqlFormatterException
  extends IOException
{
  public PySqlFormatterException() {}
  
  public PySqlFormatterException(String argMessage)
  {
    super(argMessage);
  }
}
