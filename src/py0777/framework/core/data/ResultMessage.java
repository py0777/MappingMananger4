package py0777.framework.core.data;


public class ResultMessage
  implements IResultMessage
{
  private static final long serialVersionUID = 7488533948874957455L;
  

  private String messageId = null;
  

  private String[] messageParams = null;
  
  private int status = 0;
  
  private String title = null;
  



  private Throwable th = null;
  
  private int affectedRowCount = -1;
  



  private String recordSetId = null;
  



  private String recordId = null;
  



  private String fieldId = null;
  

  String additionalMessage;
  
  private String additionalMessageId;
  
  private String[] additionalMessageParams;


  public ResultMessage(int status, String messageId, String[] messageParams)
  {
    this(status, messageId, messageParams, null);
  }
  

  public ResultMessage(int status, String messageId, String[] messageParams, Throwable th)
  {
    this.messageId = messageId;
    this.messageParams = messageParams;
    this.status = status;
    this.th = th;
  }
  



  public String getMessageId()
  {
    return messageId;
  }
  



  public String[] getMessageParams()
  {
    return messageParams;
  }
  




  public int getStatus()
  {
    return status;
  }
  



  public Throwable getThrowable()
  {
    return th;
  }
  



  public void setThrowable(Throwable th)
  {
    this.th = th;
  }
  



  public String getTitle()
  {
    return title;
  }
  



  public void setTitle(String title)
  {
    this.title = title;
  }
  

  public int getAffectedRowCount()
  {
    return affectedRowCount;
  }
  



  public void setAffectedRowCount(int affectedRowCount)
  {
    this.affectedRowCount = affectedRowCount;
  }
  



  public String getErrorRecordSetId()
  {
    return recordSetId;
  }
  



  public void setErrorRecordSetId(String recordSetId)
  {
    this.recordSetId = recordSetId;
  }
  



  public String getErrorRecordId()
  {
    return recordId;
  }
  



  public void setErrorRecordId(String recordId)
  {
    this.recordId = recordId;
  }
  

  public Object clone()
  {
    String[] clonedMsgParams = null;
    
    if (messageParams != null) {
      clonedMsgParams = new String[messageParams.length];
      System.arraycopy(messageParams, 0, clonedMsgParams, 0, messageParams.length);
    }
    
    IResultMessage cloned = new ResultMessage(getStatus(), getMessageId(), clonedMsgParams, getThrowable());
    


    cloned.setTitle(getTitle());
    cloned.setAffectedRowCount(getAffectedRowCount());
    cloned.setErrorRecordSetId(getErrorRecordSetId());
    cloned.setErrorRecordId(getErrorRecordId());
    
    return cloned;
  }
  

  public String toString()
  {
    StringBuffer sbuf = new StringBuffer();
    
    sbuf.append("Message ID = " + getMessageId() + "\n");
    
    sbuf.append("Status = ");
    
    int tmpStatus = getStatus();
    
    if (tmpStatus == 1) {
      sbuf.append("OK\n");
    } else {
      sbuf.append("FAIL\n");
    }
    
    int paramCnt = 0;
    
    if (messageParams != null) {
      paramCnt = messageParams.length;
    }
    sbuf.append("Message Parameters[" + paramCnt + "] = {");
    
    if (paramCnt == 0) {
      sbuf.append("}\n");
    } else {
      sbuf.append(messageParams[0]);
      for (int i = 1; i < messageParams.length; i++) {
        sbuf.append(", " + messageParams[i]);
      }
      sbuf.append("}\n");
    }
    
    sbuf.append("Title = " + getTitle() + "\n");
    sbuf.append("Throwable = " + (getThrowable() == null ? "null" : new StringBuilder().append("{").append(getThrowable().getClass().getName()).append(" : ").append(getThrowable().getLocalizedMessage()).append("}").toString()) + "\n");
    

    sbuf.append("Affected row count = " + getAffectedRowCount() + "\n");
    sbuf.append("IRecoreSet ID(associated with error) = " + getErrorRecordSetId() + "\n");
    sbuf.append("IRecord ID(associated with error) = " + getErrorRecordId() + "\n");
    
    return sbuf.toString();
  }
  


  public String getErrorFieldId()
  {
    return fieldId;
  }
  


  public void setErrorFieldId(String fieldId)
  {
    this.fieldId = fieldId;
  }
  

  public ResultMessage(int status, String messageId, String[] messageParams, Throwable th, String additionalMsg)
  {
    this.status = status;
    this.messageId = messageId;
    this.messageParams = messageParams;
    this.th = th;
    additionalMessage = additionalMsg;
  }
  
  public String getAdditionalMessage()
  {
    return additionalMessage;
  }
  
  public void setAdditionalMessage(String additionalMessage) {
    this.additionalMessage = additionalMessage;
  }
  

  public String getAdditionalMessageId()
  {
    return additionalMessageId;
  }
  



  public String[] getAdditionalMessageParams()
  {
    return additionalMessageParams;
  }
  



  public void setAdditionalMessageId(String additionalMessageId)
  {
    this.additionalMessageId = additionalMessageId;
  }
  



  public void setAdditionalMessageParams(String... additionalMessageParams)
  {
    this.additionalMessageParams = additionalMessageParams;
  }
}
