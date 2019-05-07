package py0777.framework.core.data;

public abstract interface IValueObject
{
  public abstract IResultMessage getResultMessage();
  
  public abstract void setResultMessage(IResultMessage paramIResultMessage);
  
  public abstract void initResultMessage();
  
  public abstract void setOkResultMessage(String paramString, String[] paramArrayOfString);
  
  public abstract void setOkResultMessage(String paramString1, String[] paramArrayOfString, String paramString2);
  
  public abstract void setResultMessage(int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable);
  
  public abstract void setResultMessage(int paramInt, String paramString1, String[] paramArrayOfString, Throwable paramThrowable, String paramString2);
  
  public abstract void setAdditionalMessage(String paramString, String... paramVarArgs);
}