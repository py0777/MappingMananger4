package py0777.framework.core.data;

import java.io.Serializable;

public abstract interface IResultMessage
  extends Serializable, Cloneable
{
  public static final int FAIL = 0;
  public static final int OK = 1;
  public static final String STR_FAIL = "FAIL";
  public static final String STR_OK = "OK";
  
  public abstract int getStatus();
  
  public abstract String getTitle();
  
  public abstract void setTitle(String paramString);
  
  public abstract Throwable getThrowable();
  
  public abstract void setThrowable(Throwable paramThrowable);
  
  public abstract Object clone();
  
  public abstract int getAffectedRowCount();
  
  public abstract void setAffectedRowCount(int paramInt);
  
  public abstract String getErrorRecordSetId();
  
  public abstract void setErrorRecordSetId(String paramString);
  
  public abstract String getErrorRecordId();
  
  public abstract void setErrorRecordId(String paramString);
  
  public abstract String getErrorFieldId();
  
  public abstract void setErrorFieldId(String paramString);
  
  public abstract String getAdditionalMessage();
  
  public abstract void setAdditionalMessage(String paramString);
  
  public abstract String getAdditionalMessageId();
  
  public abstract String[] getAdditionalMessageParams();
  
  public abstract void setAdditionalMessageId(String paramString);
  
  public abstract void setAdditionalMessageParams(String... paramVarArgs);

}
