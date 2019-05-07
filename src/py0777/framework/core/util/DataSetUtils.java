package py0777.framework.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import py0777.framework.core.data.IRecord;
import py0777.framework.core.data.IRecordSet;
import py0777.framework.core.data.Record;
import py0777.framework.core.data.RecordHeader;
import py0777.framework.core.data.RecordSet;




public class DataSetUtils
{
  public static IRecordSet collectionToRecordSet(String id, Collection c, boolean validate)
  {
    IRecordSet rs = new RecordSet(id);
    if ((c == null) || (c.size() == 0)) {
      return rs;
    }
    Class klass = validate ? validate(c) : c.iterator().next().getClass();
    return transferCollectionToRecordSet(rs, id, c, klass);
  }
  

  public static IRecordSet collectionToRecordSet(String id, Collection c, Class klass, boolean validate)
  {
    IRecordSet rs = new RecordSet(id);
    if ((c == null) || (c.size() == 0)) {
      return rs;
    }
    if (validate) {
      validate(c, klass);
    }
    return transferCollectionToRecordSet(rs, id, c, klass);
  }
  





  private static IRecordSet transferCollectionToRecordSet(IRecordSet rs, String id, Collection c, Class klass)
  {
    boolean isHeaderFilled = false;
//    for (Iterator it = c.iterator(); it.hasNext();) {
//      Object obj = it.next();
//      if ((obj instanceof Map)) {
//        Map map = (Map)obj;
//        IRecord record = new Record(map.size());
//        addMapToRecord(rs, record, map, !isHeaderFilled);
//        rs.addRecord(record);
//        
//        isHeaderFilled = true;
//      } else {
//        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(klass);
//        
//        IRecord record = new Record(pds.length);
//        addObjectToRecord(rs, record, obj, klass, !isHeaderFilled);
//        rs.addRecord(record);
//        
//        isHeaderFilled = true;
//      }
//    }
    
    return rs;
  }
  





  private static void addMapToRecord(IRecordSet rs, IRecord record, Map map, boolean willSetHeader)
  {
    for (Iterator jt = map.entrySet().iterator(); jt.hasNext();) {
      Map.Entry en = (Map.Entry)jt.next();
      String propertyName = (String)en.getKey();
      Object o = en.getValue();
      String propertyValue = StringUtils.transferToString(o);
      if (willSetHeader) {
        rs.addHeader(new RecordHeader(propertyName, 12));
      }
      
      record.set(propertyName, propertyValue);
    }
  }
  






  private static void addObjectToRecord(IRecordSet rs, IRecord record, Object obj, Class klass, boolean willSetHeader)
  {
//    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(klass);
//    for (int i = 0; i < pds.length; i++) {
//      String propertyName = pds[i].getName();
//      if (!"class".equals(propertyName)) {
//        Object o = BeanPropertyUtils.getProperty(obj, propertyName);
//        String propertyValue = StringUtils.transferToString(o);
//        if (willSetHeader) {
//          rs.addHeader(new RecordHeader(propertyName, 12));
//        }
//        
//        record.set(propertyName, propertyValue);
//      }
//    }
  }
  




  private static Class validate(Collection c)
  {
    if ((c == null) || (c.isEmpty())) {
      return null;
    }
    Class klazz = null;
    for (Iterator it = c.iterator(); it.hasNext();) {
      Class thisClass = it.next().getClass();
      if (klazz != null) {
        if (!klazz.equals(thisClass)) {
          throw new RuntimeException("validation fail");
        }
      } else {
        klazz = thisClass;
      }
    }
    return klazz;
  }
  





  private static void validate(Collection c, Class klazz)
  {
    if ((c == null) || (c.isEmpty())) {
      return;
    }
    if (klazz == null) {
      throw new RuntimeException("can't validate : class type not given");
    }
    for (Iterator it = c.iterator(); it.hasNext();) {
      Class thisClass = it.next().getClass();
      if ((klazz != null) && 
        (!klazz.isAssignableFrom(thisClass))) {
        throw new RuntimeException("validation fail");
      }
    }
  }
  
  private static Object getTargetValue(Object value)
  {
    if (value == null) {
      return null;
    }
    if (value.getClass().isArray()) {
      if (Array.getLength(value) > 0) {
        return Array.get(value, 0);
      }
      return null;
    }
    
    return value;
  }
  
  public static String getString(Object value)
  {
    return StringUtils.transferToString(getTargetValue(value), false);
  }
  

  public static String getString(Object value, boolean handleNull)
  {
    return StringUtils.transferToString(getTargetValue(value), handleNull);
  }
  

  public static String getString(Object value, int pointUnderLength)
  {
    return StringUtils.transferToString(getTargetValue(value), false, pointUnderLength);
  }
  






  public static short getShort(Object value, short defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof Number))
      return ((Number)targetValue).shortValue();
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr))
        return defaultValue;
      if (valueStr.indexOf('.') > -1) {
        try {
          return (short)(int)Double.parseDouble(valueStr.trim());
        } catch (NumberFormatException e) {
          return defaultValue;
        }
      }
      try {
        return Short.parseShort(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to short");
  }
  






  public static int getInt(Object value, int defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof Number))
      return ((Number)targetValue).intValue();
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr))
        return defaultValue;
      if (valueStr.indexOf('.') > -1) {
        try {
          return (int)Double.parseDouble(valueStr.trim());
        } catch (NumberFormatException e) {
          return defaultValue;
        }
      }
      try {
        return Integer.parseInt(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to int");
  }
  






  public static long getLong(Object value, long defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof Number))
      return ((Number)targetValue).longValue();
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr))
        return defaultValue;
      if (valueStr.indexOf('.') > -1) {
        try {
          return Long.parseLong(valueStr.trim());
        } catch (NumberFormatException e) {
          return defaultValue;
        }
      }
      try {
        return Long.parseLong(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to long");
  }
  






  public static float getFloat(Object value, float defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof Number))
      return ((Number)targetValue).floatValue();
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr)) {
        return defaultValue;
      }
      try {
        return Float.parseFloat(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to float");
  }
  






  public static double getDouble(Object value, double defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof Number))
      return ((Number)targetValue).doubleValue();
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr)) {
        return defaultValue;
      }
      try {
        return Double.parseDouble(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to double");
  }
  






  public static BigDecimal getBigDecimal(Object value, BigDecimal defaultValue)
  {
    Object targetValue = getTargetValue(value);
    
    if (targetValue == null)
      return defaultValue;
    if ((targetValue instanceof BigDecimal))
      return (BigDecimal)targetValue;
    if (((targetValue instanceof Short)) || ((targetValue instanceof Integer)) || ((targetValue instanceof Long)))
      return BigDecimal.valueOf(((Number)targetValue).longValue());
    if ((targetValue instanceof Float))
      return new BigDecimal(String.valueOf(targetValue));
    if ((targetValue instanceof Number))
      return BigDecimal.valueOf(((Number)targetValue).doubleValue());
    if ((targetValue instanceof String)) {
      String valueStr = (String)targetValue;
      if (StringUtils.isEmpty(valueStr)) {
        return defaultValue;
      }
      try {
        return new BigDecimal(valueStr.trim());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }
    

    throw new ClassCastException(value.getClass().getName() + " cannot be converted to BigDecimal");
  }
  


  public static byte[] getByteArray(Object value)
  {
    if ((value instanceof byte[])) {
      return (byte[])value;
    }
    
    if ((value instanceof String)) {
      return ((String)value).getBytes();
    }
    
    if ((value instanceof String[])) {
      return ((String[])(String[])value)[0].getBytes();
    }
    
    if (value == null) {
      return null;
    }
    
    throw new ClassCastException(value.getClass().getName() + " cannot be converted to byte[]");
  }
  





  public static String[] getStringArray(Object value)
  {
    if ((value instanceof String[])) {
      return (String[])value;
    }
    
    return new String[] { getString(value) };
  }
  



  public static Object convertLob(Object value)
  {
    if (value == null) {
      return value;
    }
    if ((value instanceof Clob)) {
      Clob clob = (Clob)value;
      try {
        int size = (int)clob.length();
        return clob.getSubString(1L, size);
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred when handling CLOB");
      } }
    if ((value instanceof Blob)) {
      Blob blob = (Blob)value;
      try {
        int size = (int)blob.length();
        return blob.getBytes(1L, size);
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred when handling BLOB");
      }
    }
    return value;
  }
  




  public static IRecordSet getRecordSet(Object value)
  {
    if (value == null) {
      return null;
    }
    

    if (((value instanceof String)) && 
      ("".equals((String)value))) {
      return null;
    }
    

    if ((value instanceof IRecordSet)) {
      return (IRecordSet)value;
    }
    
    throw new ClassCastException(value.getClass().getName() + " cannot be converted to IRecordSet");
  }
}

