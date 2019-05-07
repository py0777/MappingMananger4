package py0777.framework.core.data;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import py0777.framework.core.util.DataSetUtils;


public class DataSet
  implements IDataSet
{
  private static final long serialVersionUID = -7300481403080606765L;
  private Map<String, Object> fieldMap = new CaseIgnoreHashMap();
  
  private Map<String, IRecordSet> recordSetMap = new LinkedHashMap();
  



  private IResultMessage resultMsg = null;
  



  private Object object = null;
 

  public String getField(String key)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getString(value);
  }
  





  public Iterator<String> getFieldKeys()
  {
    return fieldMap.keySet().iterator();
  }
  






  public boolean containsField(String key)
  {
    return fieldMap.containsKey(key);
  }
  






  public boolean removeField(String key)
  {
    if (containsField(key)) {
      fieldMap.remove(key);
      return true;
    }
    return false;
  }
  
  public int getFieldCount()
  {
    return fieldMap.size();
  }
  
  public int getFieldValueSize(String key)
  {
    if (containsField(key))
    {

      Object value = getObjectField(key);
      if (value == null) {
        return 0;
      }
      if (value.getClass().isArray()) {
        return Array.getLength(value);
      }
      return 1;
    }
    return -1;
  }
  

  public String[] getFieldValues(String key)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getStringArray(value);
  }
 

  public String putField(String key, String value)
  {
    Object prevValue = putObjectFieldInternal(key, value);
    return DataSetUtils.getString(prevValue);
  }
  

  public String[] putFieldValues(String key, String[] values)
  {
    Object prevValue = putObjectFieldInternal(key, values);
    return DataSetUtils.getStringArray(prevValue);
  }
  

  public void putFieldArrayMap(Map map)
  {
    Iterator keyIter = map.keySet().iterator();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      
      putFieldValues(key, (String[])map.get(key));
    }
  }
  


  public void putFieldMap(Map map)
  {
    Iterator keyIter = map.keySet().iterator();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      putField(key, map.get(key));
    }
  }
  
  public void putFieldObjectArrayMap(Map map)
  {
    Iterator keyIter = map.keySet().iterator();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      Object[] values = (Object[])map.get(key);
      
      if (values == null) {
        putField(key, (String)null);
      } else {
        String[] dest = new String[values.length];
        for (int i = 0; i < values.length; i++) {
          Object tObj = values[i];
          if (tObj == null) {
            dest[i] = null;
          } else {
            dest[i] = tObj.toString();
          }
        }
        putFieldValues(key, dest);
      }
    }
  }
  






  public void putFieldObjectMap(Map map)
  {
    Iterator keyIter = map.keySet().iterator();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      



      putField(key, map.get(key));
    }
  }
  
  /**
   * @deprecated
   */
  public Map<String, String[]> getFieldArrayMap()
  {
    Map<String, String[]> tMap = new CaseIgnoreHashMap();
    
    Iterator<String> keyIter = getFieldKeys();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      
      tMap.put(key, getFieldValues(key));
    }
    
    return tMap;
  }
  
  public Map<String, Object> getFieldMap()
  {
    Map<String, Object> tMap = new CaseIgnoreHashMap();
    
    Iterator<String> keyIter = getFieldKeys();
    
    while (keyIter.hasNext()) {
      String key = (String)keyIter.next();
      
      tMap.put(key, getObjectField(key));
    }
    
    return tMap;
  }
  




  public void initField()
  {
    fieldMap.clear();
  }
  




  public IRecordSet getRecordSet(String id)
  {
    return (IRecordSet)recordSetMap.get(id);
  }
  

  public Iterator getRecordSetIds()
  {
    return recordSetMap.keySet().iterator();
  }
  

  public Iterator getRecordSets()
  {
    return recordSetMap.values().iterator();
  }
  
  public Map getRecordSetMap()
  {
    return recordSetMap;
  }
  
  public IRecordSet putRecordSet(String id, IRecordSet recordSet)
  {
    if (id == null) {
      throw new NullPointerException("RecordSet must have a valid id.");
    }
    
    if (recordSet == null) {
      throw new NullPointerException("RecordSet must not be null.");
    }
    
    recordSet.setId(id);
    


    return (IRecordSet)recordSetMap.put(id, recordSet);
  }
  




  public IRecordSet putRecordSet(IRecordSet recordSet)
  {
    if (recordSet == null) {
      throw new NullPointerException("RecordSet must not be null.");
    }
    
    return putRecordSet(recordSet.getId(), recordSet);
  }
  






  public void putRecordSets(Map recordSetMap)
  {
    if (recordSetMap != null) {
      Iterator<Map.Entry> entrys = recordSetMap.entrySet().iterator();
      while (entrys.hasNext()) {
        Map.Entry entry = (Map.Entry)entrys.next();
        String id = (String)entry.getKey();
        IRecordSet recordSet = (IRecordSet)entry.getValue();
        putRecordSet(id, recordSet);
      }
    }
  }
  





  public boolean containsRecordSet(String id)
  {
    return recordSetMap.containsKey(id);
  }
  
  public boolean removeRecordSet(String id)
  {
    if (containsRecordSet(id)) {
      IRecordSet rs = getRecordSet(id);
      
      if (rs != null) {
        int rowCnt = rs.getRecordCount();
        



        while (0 < rowCnt--) {
          rs.removeRecord(rowCnt);
        }
      }
      
      recordSetMap.remove(id);
      
      return true;
    }
    return false;
  }
  





  public int getRecordSetCount()
  {
    return recordSetMap.size();
  }
  
  public void initRecordSet()
  {
    Iterator idIter = new ArrayList(recordSetMap.keySet()).iterator();
    
    while (idIter.hasNext()) {
      removeRecordSet((String)idIter.next());
    }
  }
  

  public IResultMessage getResultMessage()
  {
    return resultMsg;
  }
  



  public void setResultMessage(IResultMessage message)
  {
    resultMsg = message;
  }
  




  public void initResultMessage()
  {
    setResultMessage(null);
  }
  




  public Object clone()
  {
    IDataSet cloned = new DataSet();
    

    if (getFieldCount() > 0)
    {


      Iterator paramIter = getFieldKeys();
      
      while (paramIter.hasNext()) {
        String pKey = (String)paramIter.next();
        Object pValue = getObjectField(pKey);
        if ((pValue != null) && ((pValue instanceof IDataSet))) {
          pValue = ((IDataSet)pValue).clone();
        }
        cloned.putField(pKey, pValue);
      }
    }
    

    if (getRecordSetCount() > 0)
    {


      Iterator recordSetIter = getRecordSetIds();
      
      while (recordSetIter.hasNext()) {
        String id = (String)recordSetIter.next();
        cloned.putRecordSet((IRecordSet)getRecordSet(id).clone());
      }
    }
    

    if (getResultMessage() != null) {
      //cloned.setResultMessage((IResultMessage)getResultMessage().clone());
    }
    


    return cloned;
  }
  





  public String toString()
  {
    StringBuffer sbuf = new StringBuffer();
    
    sbuf.append("[BEGIN PRINT IDATASET]\n");
    



    sbuf.append("================================================================================\n");
    
    sbuf.append("FIELD CNT. = " + getFieldCount() + "\n");
    sbuf.append("--------------------------------------------------------------------------------\n");
    



    Iterator fieldIter = getFieldKeys();
    while (fieldIter.hasNext())
    {

      String key = (String)fieldIter.next();
      
      sbuf.append(key + " = ");
      

      String[] values = getFieldValues(key);
      
      if (values.length == 0) {
        sbuf.append("{}\n");
      } else {
        sbuf.append("{" + values[0]);
        
        for (int i = 1; i < values.length; i++) {
          sbuf.append(", " + values[i]);
        }
        
        sbuf.append("}\n");
      }
    }
    sbuf.append("================================================================================\n\n");
    




    sbuf.append("RECORD SET CNT. = " + getRecordSetCount() + "\n");
    


    Iterator rSetIter = getRecordSets();
    
    while (rSetIter.hasNext())
    {

      IRecordSet element = (IRecordSet)rSetIter.next();
      sbuf.append(element.toString() + "\n");
    }
    





    if (getObject() != null) {
      sbuf.append("OBJECT = " + getObject().toString() + "\n");
    } else {
      sbuf.append("OBJECT = NONE\n");
    }
    



    IResultMessage msg = getResultMessage();
    
    if (msg == null) {
      sbuf.append("MESSAGE = NONE\n");
    } else {
      sbuf.append("MESSAGE = " + msg.toString() + "\n");
    }
    
    sbuf.append("[-END- PRINT IDATASET]");
    
    return sbuf.toString();
  }
  
  public void setOkResultMessage(String messageId, String[] messageParams)
  {
    setResultMessage(1, messageId, messageParams, null);
  }
  


  public void setResultMessage(int status, String messageId, String[] messageParams, Throwable th)
  {
    IResultMessage msgObj = new ResultMessage(status, messageId, messageParams, th);
    setResultMessage(msgObj);
  }
  



  public void setObject(Object obj)
  {
    object = obj;
  }
  


  public Object getObject()
  {
    return object;
  }
  



  public void setOkResultMessage(String messageId, String[] messageParams, String addtionalMessage)
  {
    setOkResultMessage(messageId, messageParams);
  }
  




  public void setResultMessage(int status, String messageId, String[] messageParams, Throwable th, String additionalMessage)
  {
    IResultMessage msgObj = new ResultMessage(status, messageId, messageParams, th, additionalMessage);
    setResultMessage(msgObj);
  }
  




  public void setAdditionalMessage(String additionalMessageId, String... additionalMessageParams)
  {
    IResultMessage msgObj = getResultMessage();
    msgObj.setAdditionalMessageId(additionalMessageId);
    msgObj.setAdditionalMessageParams(additionalMessageParams);
  }
  






  public short getShortField(String key, short defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getShort(value, defaultValue);
  }
  


  public int getIntField(String key, int defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getInt(value, defaultValue);
  }
  


  public long getLongField(String key, long defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getLong(value, defaultValue);
  }
  


  public float getFloatField(String key, float defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getFloat(value, defaultValue);
  }
  


  public double getDoubleField(String key, double defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getDouble(value, defaultValue);
  }
  


  public BigDecimal getBigDecimalField(String key, BigDecimal defaultValue)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getBigDecimal(value, defaultValue);
  }
  



  public byte[] getByteArrayField(String key)
  {
    Object value = getObjectField(key);
    return DataSetUtils.getByteArray(value);
  }
  


  public Object getObjectField(String key)
  {
    return fieldMap.get(key);
  }
  


  public double getDoubleField(String key)
  {
    return getDoubleField(key, 0.0D);
  }
  


  public float getFloatField(String key)
  {
    return getFloatField(key, 0.0F);
  }
  


  public int getIntField(String key)
  {
    return getIntField(key, 0);
  }
  


  public long getLongField(String key)
  {
    return getLongField(key, 0L);
  }
  




  public short getShortField(String key)
  {
    return getShortField(key, (short)0);
  }
  



  public BigDecimal getBigDecimalField(String key)
  {
    return getBigDecimalField(key, new BigDecimal(0));
  }
  

  public short putField(String key, short value)
  {
    Object prevValue = putObjectFieldInternal(key, new Short(value));
    
    return DataSetUtils.getShort(prevValue, (short)0);
  }
  


  public int putField(String key, int value)
  {
    Object prevValue = putObjectFieldInternal(key, new Integer(value));
    
    return DataSetUtils.getInt(prevValue, 0);
  }
  


  public long putField(String key, long value)
  {
    Object prevValue = putObjectFieldInternal(key, new Long(value));
    
    return DataSetUtils.getLong(prevValue, 0L);
  }
  


  public float putField(String key, float value)
  {
    Object prevValue = putObjectFieldInternal(key, new Float(value));
    
    return DataSetUtils.getFloat(prevValue, 0.0F);
  }
  


  public double putField(String key, double value)
  {
    Object prevValue = putObjectFieldInternal(key, new Double(value));
    
    return DataSetUtils.getDouble(prevValue, 0.0D);
  }
  


  public BigDecimal putField(String key, BigDecimal value)
  {
    Object prevValue = putObjectFieldInternal(key, value);
    return DataSetUtils.getBigDecimal(prevValue, new BigDecimal(0));
  }
  
  public byte[] putField(String key, byte[] value)
  {
    Object prevValue = putObjectFieldInternal(key, value);
    return DataSetUtils.getByteArray(prevValue);
  }
  


  public Object putField(String key, Object value)
  {
    return putObjectFieldInternal(key, value);
  }
  


  public BigDecimal[] putFieldBigDecimal(String key, BigDecimal value)
  {
    String[] prevValue = putFieldValues(key, new String[] { value.toString() });
    BigDecimal[] ret = new BigDecimal[prevValue.length];
    for (int i = 0; i < ret.length; i++) {
      if (prevValue[i] != null) {
        ret[i] = new BigDecimal(prevValue[i]);
      }
    }
    return ret;
  }
  
  private Object putObjectFieldInternal(String key, Object value)
  {
    return fieldMap.put(key, value);
  }
}

