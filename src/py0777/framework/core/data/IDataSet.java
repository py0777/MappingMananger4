package py0777.framework.core.data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

public abstract interface IDataSet
  extends Serializable, Cloneable
{
  public abstract String getField(String paramString);
  
  public abstract String[] getFieldValues(String paramString);
  
  public abstract byte[] getByteArrayField(String paramString);
  
  public abstract Object getObjectField(String paramString);
  
  public abstract Iterator<String> getFieldKeys();
  
  public abstract Map<String, Object> getFieldMap();
  
  /**
   * @deprecated
   */
  public abstract Map<String, String[]> getFieldArrayMap();
  
  public abstract String putField(String paramString1, String paramString2);
  
  public abstract Object putField(String paramString, Object paramObject);
  
  public abstract short putField(String paramString, short paramShort);
  
  public abstract int putField(String paramString, int paramInt);
  
  public abstract long putField(String paramString, long paramLong);
  
  public abstract float putField(String paramString, float paramFloat);
  
  public abstract double putField(String paramString, double paramDouble);
  
  public abstract BigDecimal putField(String paramString, BigDecimal paramBigDecimal);
  
  public abstract byte[] putField(String paramString, byte[] paramArrayOfByte);
  
  /**
   * @deprecated
   */
  public abstract BigDecimal[] putFieldBigDecimal(String paramString, BigDecimal paramBigDecimal);
  
  /**
   * @deprecated
   */
  public abstract String[] putFieldValues(String paramString, String[] paramArrayOfString);
  
  /**
   * @deprecated
   */
  public abstract void putFieldArrayMap(Map<String, String[]> paramMap);
  
  public abstract void putFieldMap(Map<String, Object> paramMap);
  
  /**
   * @deprecated
   */
  public abstract void putFieldObjectMap(Map<String, Object> paramMap);
  
  /**
   * @deprecated
   */
  public abstract void putFieldObjectArrayMap(Map<String, Object[]> paramMap);
  
  public abstract boolean containsField(String paramString);
  
  public abstract boolean removeField(String paramString);
  
  public abstract int getFieldCount();
  
  /**
   * @deprecated
   */
  public abstract int getFieldValueSize(String paramString);
  
  public abstract void initField();
  
  public abstract IRecordSet getRecordSet(String paramString);
  
  public abstract Iterator<String> getRecordSetIds();
  
  public abstract Iterator<IRecordSet> getRecordSets();
  
  public abstract Map<String, IRecordSet> getRecordSetMap();
  
  public abstract IRecordSet putRecordSet(String paramString, IRecordSet paramIRecordSet);
  
  public abstract IRecordSet putRecordSet(IRecordSet paramIRecordSet);
  
  public abstract void putRecordSets(Map<String, IRecordSet> paramMap);
  
  public abstract boolean containsRecordSet(String paramString);
  
  public abstract boolean removeRecordSet(String paramString);
  
  public abstract int getRecordSetCount();
  
  public abstract void initRecordSet();
  
  public abstract Object clone();
  
  /**
   * @deprecated
   */
  public abstract void setObject(Object paramObject);
  
  /**
   * @deprecated
   */
  public abstract Object getObject();
  
  public abstract short getShortField(String paramString);
  
  public abstract int getIntField(String paramString);
  
  public abstract long getLongField(String paramString);
  
  public abstract float getFloatField(String paramString);
  
  public abstract double getDoubleField(String paramString);
  
  public abstract BigDecimal getBigDecimalField(String paramString);
  
  public abstract short getShortField(String paramString, short paramShort);
  
  public abstract int getIntField(String paramString, int paramInt);
  
  public abstract long getLongField(String paramString, long paramLong);
  
  public abstract float getFloatField(String paramString, float paramFloat);
  
  public abstract double getDoubleField(String paramString, double paramDouble);
  
  public abstract BigDecimal getBigDecimalField(String paramString, BigDecimal paramBigDecimal);
}

