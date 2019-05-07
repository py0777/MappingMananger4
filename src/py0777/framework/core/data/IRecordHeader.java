package py0777.framework.core.data;

import java.io.Serializable;

public abstract interface IRecordHeader
  extends Serializable, Cloneable
{
  public abstract String getName();
  
  public abstract String getLabel();
  
  public abstract int getType();
  
  public abstract void setName(String paramString);
  
  public abstract void setLabel(String paramString);
  
  public abstract void setType(int paramInt);
  
  public abstract Object clone();
}
