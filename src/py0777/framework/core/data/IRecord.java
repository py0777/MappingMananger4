package py0777.framework.core.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

public abstract interface IRecord
  extends Serializable, Cloneable, Map
{
  public abstract int size();
  
  public abstract String get(int paramInt);
  
  public abstract String get(String paramString);
  
  public abstract Iterator<String> iterator();
  
  public abstract String set(int paramInt, String paramString);
  
  public abstract String set(String paramString1, String paramString2);
  
  public abstract String removeField(int paramInt);
  
  public abstract void addRecordSet(IRecordSet paramIRecordSet);
  
  public abstract void removeRecordSet();
  
  public abstract Object clone();
  
  public abstract IRecordSet getRecordSet();
  
  public abstract void clearValues();
  
  public abstract short getShort(String paramString);
  
  public abstract int getInt(String paramString);
  
  public abstract long getLong(String paramString);
  
  public abstract float getFloat(String paramString);
  
  public abstract double getDouble(String paramString);
  
  public abstract BigDecimal getBigDecimal(String paramString);
  
  public abstract byte[] getByteArray(String paramString);
  
  public abstract IRecordSet getRecordSet(String paramString);
  
  public abstract Object getObject(String paramString);
  
  public abstract short getShort(int paramInt);
  
  public abstract int getInt(int paramInt);
  
  public abstract long getLong(int paramInt);
  
  public abstract float getFloat(int paramInt);
  
  public abstract double getDouble(int paramInt);
  
  public abstract BigDecimal getBigDecimal(int paramInt);
  
  public abstract byte[] getByteArray(int paramInt);
  
  public abstract IRecordSet getRecordSet(int paramInt);
  
  public abstract Object getObject(int paramInt);
  
  public abstract short getShort(String paramString, short paramShort);
  
  public abstract int getInt(String paramString, int paramInt);
  
  public abstract long getLong(String paramString, long paramLong);
  
  public abstract float getFloat(String paramString, float paramFloat);
  
  public abstract double getDouble(String paramString, double paramDouble);
  
  public abstract BigDecimal getBigDecimal(String paramString, BigDecimal paramBigDecimal);
  
  public abstract short getShort(int paramInt, short paramShort);
  
  public abstract int getInt(int paramInt1, int paramInt2);
  
  public abstract long getLong(int paramInt, long paramLong);
  
  public abstract float getFloat(int paramInt, float paramFloat);
  
  public abstract double getDouble(int paramInt, double paramDouble);
  
  public abstract BigDecimal getBigDecimal(int paramInt, BigDecimal paramBigDecimal);
  
  public abstract short set(String paramString, short paramShort);
  
  public abstract int set(String paramString, int paramInt);
  
  public abstract long set(String paramString, long paramLong);
  
  public abstract float set(String paramString, float paramFloat);
  
  public abstract double set(String paramString, double paramDouble);
  
  public abstract BigDecimal set(String paramString, BigDecimal paramBigDecimal);
  
  public abstract byte[] set(String paramString, byte[] paramArrayOfByte);
  
  public abstract IRecordSet set(String paramString, IRecordSet paramIRecordSet);
  
  public abstract Object set(String paramString, Object paramObject);
  
  public abstract short set(int paramInt, short paramShort);
  
  public abstract int set(int paramInt1, int paramInt2);
  
  public abstract long set(int paramInt, long paramLong);
  
  public abstract float set(int paramInt, float paramFloat);
  
  public abstract double set(int paramInt, double paramDouble);
  
  public abstract BigDecimal set(int paramInt, BigDecimal paramBigDecimal);
  
  public abstract byte[] set(int paramInt, byte[] paramArrayOfByte);
  
  public abstract IRecordSet set(int paramInt, IRecordSet paramIRecordSet);
  
  public abstract Object set(int paramInt, Object paramObject);
}
