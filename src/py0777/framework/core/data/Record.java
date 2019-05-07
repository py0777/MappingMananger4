package py0777.framework.core.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import py0777.framework.core.util.AppUtils;
import py0777.framework.core.util.DataSetUtils;

public class Record
  implements IRecord
{
  public static final String RECORD_REPLACE_NULL_RESULT_TO_EMPTY_STRING = "record.replaceNullResultToEmptyString";
  private static Boolean replaceNullResultToEmptyString;
  private static final long serialVersionUID = 4468002344708676419L;
  private IRecordSet recordSet = null;
  
  private ArrayList<Object> fields = null;
  


  public Record()
  {
    fields = new ArrayList();
  }
  

  public Record(int capacity)
  {
    fields = new ArrayList(capacity);
    for (int i = 0; i < capacity; i++) {
      fields.add(null);
    }
  }
  
  public String get(int index)
  {
    Object value = getObject(index);
    return DataSetUtils.getString(value, isReplaceNullResultToEmptyString());
  }
  

  public String get(String headerName)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getString(value, isReplaceNullResultToEmptyString());
  }
  

  public Iterator<String> iterator()
  {
    ArrayList<String> tmpList = new ArrayList();
    
    int size = size();
    
    for (int i = 0; i < size; i++) {
      tmpList.add(get(i));
    }
    
    return tmpList.iterator();
  }
  

  public String removeField(int fIndex)
  {
    return DataSetUtils.getString(fields.remove(fIndex));
  }
  

  public void addRecordSet(IRecordSet recordSet)
  {
    this.recordSet = recordSet;
  }
  

  public void removeRecordSet()
  {
    recordSet = null;
  }
  

  public String set(int index, String value)
  {
    Object prevValue = setObjectInternal(index, value);
    return DataSetUtils.getString(prevValue, isReplaceNullResultToEmptyString());
  }
  


  public int size()
  {
    return fields.size();
  }
  

  public Object clone()
  {
    int size = fields.size();
    
    Record newRecord = new Record(size);
    for (Object obj : fields)
    {
      if (obj != null) {
        if ((obj instanceof IRecordSet)) {
          obj = ((IRecordSet)obj).clone();
        }
        else if ((obj instanceof IDataSet)) {
          obj = ((IDataSet)obj).clone();
        }
      }
      fields.add(obj);
    }
    
    return newRecord;
  }
  

  public String toString()
  {
    if (fields.size() == 0) {
      return "\n";
    }
    
    StringBuilder sbuf = new StringBuilder();
    sbuf.append(DataSetUtils.getString(fields.get(0), isReplaceNullResultToEmptyString()));
    for (int i = 1; i < fields.size(); i++) {
      sbuf.append(" | " + DataSetUtils.getString(fields.get(i), isReplaceNullResultToEmptyString()));
    }
    sbuf.append("\n");
    return sbuf.toString();
  }
  
  public void clear()
  {
    fields.clear();
  }
  
  public void clearValues()
  {
    for (int i = 0; i < fields.size(); i++) {
      fields.set(i, (Object)null);
    }
  }
  
  public boolean containsKey(Object key)
  {
    String headerName = key.toString();
    validate(headerName);
    int fIndex = recordSet.getHeaderIndex(headerName);
    
    if (fIndex < 0) {
      return false;
    }
    return true;
  }
  
  public boolean containsValue(Object value)
  {
    return fields.contains(value.toString());
  }
  



  public Set<Map.Entry<String, Object>> entrySet()
  {
    if (recordSet == null) {
      throw new RuntimeException("This Record does not have a link to a RecordSet.");
    }
    
    int headerCnt = recordSet.getHeaderCount();
    Set<Map.Entry<String, Object>> set = new HashSet(headerCnt);
    
    for (int i = 0; i < headerCnt; i++) {
      Map.Entry<String, Object> entry = new Map.Entry() {
        String key = null;
        Object value = null;
        
        public String getKey() { return key; }
        

        public Object getValue() { return value; }
        
        public Map.Entry<String, Object> setKey(String key) {
          this.key = key;
          return this;
        }
        
        public Object setValue(Object value) { this.value = value;
          return value; } }.setKey(recordSet.getHeader(i).getName());
      

      entry.setValue(getObject(i));
      set.add(entry);
    }
    
    return set;
  }
  


  public Object get(Object key)
  {
    String headerName = key == null ? null : key.toString();
    validate(headerName);
    int fIndex = recordSet.getHeaderIndex(headerName);
    
    if (fIndex < 0) {
      return null;
    }
    return getObject(fIndex);
  }
  


  public boolean isEmpty()
  {
    return fields.isEmpty();
  }
  

  public Set<String> keySet()
  {
    if (recordSet == null) {
      throw new RuntimeException("This Record does not have a link to a RecordSet.");
    }
    
    int headerCnt = recordSet.getHeaderCount();
    

    Set<String> set = new LinkedHashSet(headerCnt);
    for (int i = 0; i < headerCnt; i++) {
      set.add(recordSet.getHeader(i).getName());
    }
    return set;
  }
  
  public Object put(Object key, Object value)
  {
    if (recordSet == null) {
      throw new RuntimeException("This Record does not have a link to a RecordSet.");
    }
    

    String headerName = key == null ? null : key.toString();
    return set(headerName, value);
  }
  
  public void putAll(Map t)
  {
    throw new UnsupportedOperationException("Record is not real map, just use for jstl & ibatis sqlMap parameter.");
  }
  


  public Object remove(Object key)
  {
    if (key == null) {
      return null;
    }
    String headerName = key.toString();
    validate(headerName);
    int fIndex = recordSet.getHeaderIndex(headerName);
    
    if (fIndex < 0) {
      return null;
    }
    return fields.remove(fIndex);
  }
  


  public Collection<Object> values()
  {
    return fields;
  }
  


  public String set(String headerName, String value)
  {
    Object prevValue = setObjectInternal(headerName, value);
    return DataSetUtils.getString(prevValue, isReplaceNullResultToEmptyString());
  }
  


  public IRecordSet getRecordSet()
  {
    return recordSet;
  }
  



  private void validate(String headerName)
  {
    if (recordSet == null) {
      throw new RuntimeException("Error in accessing headername '" + headerName + "'. No link to a recordSet in this record.");
    }
  }
  

  public short getShort(String headerName)
  {
    return getShort(headerName, (short)0);
  }
  


  public double getDouble(String headerName)
  {
    return getDouble(headerName, 0.0D);
  }
  

  public float getFloat(String headerName)
  {
    return getFloat(headerName, 0.0F);
  }
  

  public int getInt(String headerName)
  {
    return getInt(headerName, 0);
  }
  

  public long getLong(String headerName)
  {
    return getLong(headerName, 0L);
  }
  


  public BigDecimal getBigDecimal(String headerName)
  {
    return getBigDecimal(headerName, new BigDecimal(0));
  }
  

  public short getShort(int index)
  {
    return getShort(index, (short)0);
  }
  

  public double getDouble(int index)
  {
    return getDouble(index, 0.0D);
  }
  


  public float getFloat(int index)
  {
    return getFloat(index, 0.0F);
  }
  


  public int getInt(int index)
  {
    return getInt(index, 0);
  }
  


  public long getLong(int index)
  {
    return getLong(index, 0L);
  }
  
  public BigDecimal getBigDecimal(int index)
  {
    return getBigDecimal(index, new BigDecimal(0));
  }
  
  public short getShort(String headerName, short defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getShort(value, defaultValue);
  }
  


  public int getInt(String headerName, int defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getInt(value, defaultValue);
  }
  


  public long getLong(String headerName, long defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getLong(value, defaultValue);
  }
  
  public float getFloat(String headerName, float defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getFloat(value, defaultValue);
  }
  
  public double getDouble(String headerName, double defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getDouble(value, defaultValue);
  }
  
  public BigDecimal getBigDecimal(String headerName, BigDecimal defaultValue)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getBigDecimal(value, defaultValue);
  }
  


  public byte[] getByteArray(String headerName)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getByteArray(value);
  }
  


  public IRecordSet getRecordSet(String headerName)
  {
    Object value = getObject(headerName);
    return DataSetUtils.getRecordSet(value);
  }
  
  public Object getObject(String headerName)
  {
    validate(headerName);
    
    if (recordSet == null) {
      return null;
    }
    int fIndex = recordSet.getHeaderIndex(headerName);
    
    if (fIndex == -1) {
      throw new RuntimeException("No such Header '" + headerName + "' in IRecordSet(" + recordSet.getId() + ").");
    }
    

    return getObject(fIndex);
  }
  



  public short getShort(int index, short defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getShort(value, defaultValue);
  }
  


  public int getInt(int index, int defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getInt(value, defaultValue);
  }
  


  public long getLong(int index, long defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getLong(value, defaultValue);
  }
  


  public float getFloat(int index, float defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getFloat(value, defaultValue);
  }
  


  public double getDouble(int index, double defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getDouble(value, defaultValue);
  }
  
  public BigDecimal getBigDecimal(int index, BigDecimal defaultValue)
  {
    Object value = getObject(index);
    return DataSetUtils.getBigDecimal(value, defaultValue);
  }
  


  public byte[] getByteArray(int index)
  {
    Object value = getObject(index);
    return DataSetUtils.getByteArray(value);
  }
  


  public IRecordSet getRecordSet(int index)
  {
    Object value = getObject(index);
    return DataSetUtils.getRecordSet(value);
  }
  
  public Object getObject(int index)
  {
    if (index >= size()) {
      return null;
    }
    
    return fields.get(index);
  }
  
  public short set(String headerName, short value)
  {
    Object prevValue = setObjectInternal(headerName, new Short(value));
    return DataSetUtils.getShort(prevValue, (short)0);
  }
  


  public int set(String headerName, int value)
  {
    Object prevValue = setObjectInternal(headerName, new Integer(value));
    return DataSetUtils.getInt(prevValue, 0);
  }
  


  public long set(String headerName, long value)
  {
    Object prevValue = setObjectInternal(headerName, new Long(value));
    return DataSetUtils.getLong(prevValue, 0L);
  }
  


  public float set(String headerName, float value)
  {
    Object prevValue = setObjectInternal(headerName, new Float(value));
    return DataSetUtils.getFloat(prevValue, 0.0F);
  }
  


  public double set(String headerName, double value)
  {
    Object prevValue = setObjectInternal(headerName, new Double(value));
    return DataSetUtils.getDouble(prevValue, 0.0D);
  }
  
  public BigDecimal set(String headerName, BigDecimal value)
  {
    Object prevValue = setObjectInternal(headerName, value);
    return DataSetUtils.getBigDecimal(prevValue, new BigDecimal(0));
  }
  


  public byte[] set(String headerName, byte[] value)
  {
    Object prevValue = setObjectInternal(headerName, value);
    return DataSetUtils.getByteArray(prevValue);
  }
  


  public IRecordSet set(String headerName, IRecordSet value)
  {
    Object prevValue = setObjectInternal(headerName, value);
    return DataSetUtils.getRecordSet(prevValue);
  }
  


  public Object set(String headerName, Object value)
  {
    return setObjectInternal(headerName, value);
  }
  


  public short set(int index, short value)
  {
    Object prevValue = setObjectInternal(index, new Short(value));
    return DataSetUtils.getShort(prevValue, (short)0);
  }
  


  public int set(int index, int value)
  {
    Object prevValue = setObjectInternal(index, new Integer(value));
    return DataSetUtils.getInt(prevValue, 0);
  }
  


  public long set(int index, long value)
  {
    Object prevValue = setObjectInternal(index, new Long(value));
    return DataSetUtils.getLong(prevValue, 0L);
  }
  


  public float set(int index, float value)
  {
    Object prevValue = setObjectInternal(index, new Float(value));
    return DataSetUtils.getFloat(prevValue, 0.0F);
  }
  


  public double set(int index, double value)
  {
    Object prevValue = setObjectInternal(index, new Double(value));
    return DataSetUtils.getDouble(prevValue, 0.0D);
  }
  


  public BigDecimal set(int index, BigDecimal value)
  {
    Object prevValue = setObjectInternal(index, value);
    return DataSetUtils.getBigDecimal(prevValue, new BigDecimal(0));
  }
  


  public byte[] set(int index, byte[] value)
  {
    Object prevValue = setObjectInternal(index, value);
    return DataSetUtils.getByteArray(prevValue);
  }
  


  public IRecordSet set(int index, IRecordSet value)
  {
    Object prevValue = setObjectInternal(index, value);
    return DataSetUtils.getRecordSet(prevValue);
  }
  


  public Object set(int index, Object value)
  {
    return setObjectInternal(index, value);
  }
  
  void add(int fIndex, Object value)
  {
    fields.add(fIndex, value);
  }
  


  private Object setObjectInternal(String headerName, Object value)
  {
    validate(headerName);
    int index = recordSet.getHeaderIndex(headerName);
    
    if (index < 0) {
      return null;
    }
    return setObjectInternal(index, value);
  }
  

  private Object setObjectInternal(int index, Object value)
  {
    return fields.set(index, value);
  }
  

  private boolean isReplaceNullResultToEmptyString()
  {
    if (replaceNullResultToEmptyString == null) {
      String str = AppUtils.getProperty("record.replaceNullResultToEmptyString");
      if (str == null) {
        replaceNullResultToEmptyString = Boolean.valueOf(false);
      }
      replaceNullResultToEmptyString = Boolean.valueOf("true".equalsIgnoreCase(str));
    }
    return replaceNullResultToEmptyString.booleanValue();
  }
}

