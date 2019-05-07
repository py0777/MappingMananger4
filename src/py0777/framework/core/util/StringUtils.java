package py0777.framework.core.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils
{
  private static final String[] DECIMAL_FORMAT_STRS = { "#", "#.#", "#.##", "#.###", "#.####", "#.#####", "#.######", "#.#######", "#.########", "#.#########", "#.##########", "#.###########", "#.############", "#.#############", "#.##############", "#.###############", "#.################", "#.#################" };
  


  public static String kscToAsc(String str)
  {
    try
    {
      return str != null ? new String(str.getBytes("KSC5601"), "8859_1") : str;
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static String ascToKsc(String str)
  {
    try
    {
      return str != null ? new String(str.getBytes("8859_1"), "KSC5601") : str;
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  public static String replaceAll(String input, String oldStr, String newStr)
  {
    int startIdx = 0;
    int idx = 0;
    int length = oldStr.length();
    
    StringBuffer sb = new StringBuffer((int)(input.length() * 1.2D));
    while ((idx = input.indexOf(oldStr, startIdx)) >= 0) {
      sb.append(input.substring(startIdx, idx));
      sb.append(newStr);
      startIdx = idx + length;
    }
    sb.append(input.substring(startIdx));
    return sb.toString();
  }
  

  public static String remove(String input, String oldStr)
  {
    int startIdx = 0;
    int idx = 0;
    int length = oldStr.length();
    
    StringBuffer sb = new StringBuffer((int)(input.length() * 1.2D));
    while ((idx = input.indexOf(oldStr, startIdx)) >= 0) {
      sb.append(input.substring(startIdx, idx));
      startIdx = idx + length;
    }
    sb.append(input.substring(startIdx));
    return sb.toString();
  }
  

  public static String fixLength(String content, int length, String suffix)
  {
    if (content == null) {
      return "";
    }
    if (content.getBytes().length > length) {
      int slen = 0;int blen = 0;
      int realLength = length - suffix.getBytes().length;
      while (blen < realLength) {
        blen++;
        if (content.charAt(slen) > '?') {
          blen++;
        }
        slen++;
      }
      return content.substring(0, slen) + suffix;
    }
    return content;
  }
  



  public static String abbreviate(String content, int maxWidth, String enc, String suffix)
  {
    if (content == null)
      return "";
    if (maxWidth < suffix.length())
    {
      throw new RuntimeException(new IllegalArgumentException()); }
    int ptr = maxWidth - suffix.length();
    String str = null;
    try {
      byte[] bytes = content.getBytes(enc);
      str = new String(bytes, 0, bytes.length < ptr ? bytes.length : ptr, enc);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
    
    ptr = (ptr = str.length() - 4) < 0 ? 0 : ptr;
    while ((ptr < str.length()) && (str.charAt(ptr) == content.charAt(ptr))) {
      ptr++;
    }
    return str.substring(0, ptr) + suffix;
  }
  


  public static boolean isEmpty(String input)
  {
    if (input == null) {
      return true;
    }
    


    for (int i = 0; i < input.length(); i++) {
      if (input.charAt(i) > ' ') {
        return false;
      }
    }
    
    return true;
  }
  

  public static boolean isNotEmpty(String input)
  {
    return !isEmpty(input);
  }
  



  public static String toCurrency(String currency)
  {
    try
    {
      NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
      return format.format(new Long(currency));
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }
  

  public static String toCurrency(long currency)
  {
    try
    {
      NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
      return format.format(new Long(currency));
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }
  

  public static String parseCurrency(String myString)
  {
    try
    {
      NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
      return format.parse(myString).toString();
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static String listToString(Object obj, String delimiter)
  {
    if (obj == null) {
      return "";
    }
    if (obj.getClass().isArray()) {
      StringBuffer buffer = new StringBuffer(512);
      int length = Array.getLength(obj);
      for (int i = 0; i < length; i++) {
        if (i != 0) {
          buffer.append(delimiter);
        }
        
        buffer.append(nvlObject(Array.get(obj, i), ""));
      }
      return buffer.toString(); }
    if ((obj instanceof Collection))
      return listToString(((Collection)obj).iterator(), delimiter);
    if ((obj instanceof Enumeration)) {
      StringBuffer buffer = new StringBuffer(512);
      boolean started = false;
      Enumeration it = (Enumeration)obj;
      while (it.hasMoreElements()) {
        if (started) {
          buffer.append(delimiter);
        } else {
          started = true;
        }
        
        buffer.append(nvlObject(it.nextElement(), ""));
      }
      return buffer.toString(); }
    if ((obj instanceof Iterator)) {
      StringBuffer buffer = new StringBuffer(512);
      boolean started = false;
      Iterator it = (Iterator)obj;
      while (it.hasNext()) {
        if (started) {
          buffer.append(delimiter);
        } else {
          started = true;
        }
        
        buffer.append(nvlObject(it.next(), ""));
      }
      return buffer.toString(); }
    if ((obj instanceof Map)) {
      return listToString(((Map)obj).values(), delimiter);
    }
    return obj.toString();
  }
  
  public static String nvl(String source, String alernative)
  {
    if (source == null) {
      return alernative;
    }
    return source;
  }
  


  public static String nvlEmpty(String source, String alternative)
  {
    if (isEmpty(source)) {
      return alternative;
    }
    return source;
  }
  


  public static String nvlObject(Object source, String alernative)
  {
    if (source == null) {
      return alernative;
    }
    return String.valueOf(source);
  }
  





  public static String formatNumber(String targetVal, String type)
  {
    int intVal = 0;
    double dblVal = 0.0D;
    String rtnVal = "0";
    
    if (isEmpty(targetVal)) {
      return "";
    }
    
    if (targetVal != null)
    {
      if (type.equals("INT"))
      {
        intVal = new Integer(targetVal).intValue();
        
        DecimalFormat dfInt = new DecimalFormat("#,##0");
        
        rtnVal = dfInt.format(intVal);
      }
      
      if (type.equals("FINT"))
      {
        intVal = Math.round(Float.parseFloat(targetVal));
        
        DecimalFormat dfInt = new DecimalFormat("#,##0");
        
        rtnVal = dfInt.format(intVal);
      }
      else if (type.equals("DBL"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("#,##0.00");
        
        rtnVal = dfDbl.format(dblVal);
      }
      else if (type.equals("IDBL"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("#,##0");
        
        rtnVal = dfDbl.format(dblVal);
      }
      else if (type.equals("DDBL"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("#,##0.0000");
        
        rtnVal = dfDbl.format(dblVal);
      } else if (type.equals("DDBL1"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("#,##0.0");
        
        rtnVal = dfDbl.format(dblVal);
      } else if (type.equals("DDBL3"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.000");
        
        rtnVal = dfDbl.format(dblVal);
      } else if (type.equals("DDBL6"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.000000");
        
        rtnVal = dfDbl.format(dblVal);
      } else if (type.equals("DDBL7"))
      {
        dblVal = new Double(targetVal).doubleValue();
        
        DecimalFormat dfDbl = new DecimalFormat("##,###,###,##0.0000000");
        

        rtnVal = dfDbl.format(dblVal);
      } else if (type.equals("INT4"))
      {
        int diff = 4 - targetVal.trim().length();
        for (int i = 0; i < diff; i++) {
          targetVal = "0" + targetVal;
        }
      }
    }
    
    return rtnVal;
  }
  



  public static String getFileName(String fullFileName)
  {
    try
    {
      return fullFileName.substring(fullFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
    }
    catch (Exception ex) {}
    return null;
  }
  

  /**
   * @deprecated
   */
  public static String decodeCharset(String value, String charset)
  {
    try
    {
      Charset set = Charset.forName(charset);
      return set.decode(ByteBuffer.wrap(value.getBytes())).toString();
    } catch (Exception ex) {}
    return null;
  }
  

 
  public static String[] toStringArray(Collection collection)
  {
    if (collection == null) {
      return null;
    }
    return (String[])collection.toArray(new String[collection.size()]);
  }
  


  public static String firstLetterUpper(String string)
  {
    if (null == string)
      return null;
    if (string.length() == 0) {
      return string;
    }
    return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
  }
  

  public static String firstLetterLower(String string)
  {
    if (null == string)
      return null;
    if (string.length() == 0) {
      return string;
    }
    return string.substring(0, 1).toLowerCase() + string.substring(1, string.length());
  }
  


  public static String toJavaName(String dbName)
  {
    StringBuffer buffer = new StringBuffer(dbName.length());
    boolean isFirstBlock = true;
    int index = 0;
    for (int i = 0; i < dbName.length(); i++) {
      char ch = dbName.charAt(i);
      if (ch == '_') {
        isFirstBlock = false;
        index = 0;
      }
      else {
        index++;
        
        if ((!isFirstBlock) && (index == 1)) {
          buffer.append(ch);
        } else
          buffer.append(Character.toLowerCase(ch));
      }
    }
    return buffer.toString();
  }
  


  public static String compactStringToVarchar256(String s)
  {
    if (s == null) return null;
    if (s.length() >= 128) {
      byte[] b = s.getBytes();
      if (b.length > 255) {
        String _return = new String(b, 0, 255);
        if (_return.length() < 1)
        {
          _return = new String(b, 0, 254);
        }
        return _return;
      }
      return s;
    }
    
    return s;
  }
  


  public static String lpad(String str, int length, String pad)
  {
    if (str == null) str = "";
    if (pad == null) pad = " ";
    int srclength = str.length();
    
    if (srclength > length)
      return str.substring(0, length);
    if (srclength == length) {
      return str;
    }
    int padlength = pad.length();
    int blklength = length - srclength;
    int count = blklength / padlength;
    int mod = blklength % padlength;
    StringBuffer sbuf = new StringBuffer();
    
    while (count > 0) {
      sbuf.append(pad);
      count--;
    }
    
    if (mod > 0) sbuf.append(pad.substring(0, mod));
    sbuf.append(str);
    return sbuf.toString();
  }
  
  public static String rpad(String str, int length, String pad)
  {
    if (str == null) str = "";
    if (pad == null) pad = " ";
    int srclength = str.length();
    
    if (srclength > length)
      return str.substring(srclength - length);
    if (srclength == length) {
      return str;
    }
    int padlength = pad.length();
    int blklength = length - srclength;
    int count = blklength / padlength;
    int mod = blklength % padlength;
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(str);
    
    while (count > 0) {
      sbuf.append(pad);
      count--;
    }
    
    if (mod > 0) sbuf.append(pad.substring(0, mod));
    return sbuf.toString();
  }
  
  public static String transferToString(Object value)
  {
    return transferToString(value, true);
  }
  

  public static String transferToString(Object value, boolean handleNull)
  {
    return transferToString(value, handleNull, -1);
  }
  



  public static String transferToString(Object value, boolean handleNull, int pointUnderLength)
  {
    if (value == null) {
      return handleNull ? "" : null;
    }
    if ((value instanceof String))
      return (String)value;
    if (((value instanceof BigDecimal)) || ((value instanceof Double)) || ((value instanceof Float)))
    {

      if (pointUnderLength == -1) {
        return new DecimalFormat("#.##############################").format(value);
      }
      
      if ((value instanceof BigDecimal)) {
        return getDecimalFormat(pointUnderLength).format(((BigDecimal)value).setScale(pointUnderLength, RoundingMode.DOWN));
      }
      return getDecimalFormat(pointUnderLength).format(BigDecimal.valueOf(((Number)value).doubleValue()).setScale(pointUnderLength, RoundingMode.DOWN));
    }
    
    if ((value instanceof Number))
      return ((Number)value).toString();
    if ((value instanceof byte[])) {
      return new String((byte[])value);
    }
    if ((value instanceof Clob)) {
      Clob clob = (Clob)value;
      try {
        int size = (int)clob.length();
        return clob.getSubString(1L, size);
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred when handling CLOB");
      }
    }
    if ((value instanceof Blob)) {
      Blob blob = (Blob)value;
      try {
        int size = (int)blob.length();
        byte[] ba = blob.getBytes(1L, size);
        return new String(ba, "8859_1");
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred when handling BLOB");
      }
      catch (UnsupportedEncodingException uee)
      {
        return "";
      } }
    if (value.getClass().getName().equals("oracle.sql.TIMESTAMPTZ")) {
      try
      {
        Method dateValueMethod = value.getClass().getMethod("dateValue", new Class[] { Connection.class });
        Date dateValue = (Date)dateValueMethod.invoke(value, new Object[] { null });
        return new Timestamp(dateValue.getTime()).toString();
      } catch (Exception e) {
        throw new RuntimeException("Error occurred when handling Oracle systimestamp");
      }
    }
    if (value.getClass().getName().equals("oracle.sql.TIMESTAMP")) {
      try
      {
        Method dateValueMethod = value.getClass().getMethod("dateValue", new Class[0]);
        Date dateValue = (Date)dateValueMethod.invoke(value, new Object[0]);
        return new Timestamp(dateValue.getTime()).toString();
      } catch (Exception e) {
        throw new RuntimeException("Error occurred when handling Oracle timestamp");
      }
    }
    
    return value.toString();
  }

  public static List<String> stringToList(String value, char delimiter)
  {
    List<String> tokens = new ArrayList();
    
    StringBuilder buff = new StringBuilder();
    char[] chars = value.toCharArray();
    for (char c : chars) {
      if (c == delimiter) {
        tokens.add(buff.toString());
        buff.setLength(0);
      }
      else {
        buff.append(c);
      }
    }
    tokens.add(buff.toString());
    return tokens;
  }
  

  public static String[] stringToArray(String value, char delimiter)
  {
    List<String> list = stringToList(value, delimiter);
    return (String[])list.toArray(new String[list.size()]);
  }
  


  public static boolean equals(String a, String b)
  {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }
  
  public static boolean equalsIgnoreCase(String a, String b)
  {
    if (a == null) {
      return b == null;
    }
    return a.equalsIgnoreCase(b);
  }
  

  public static boolean equalsIgnoreNull(String a, String b)
  {
    if (isEmpty(a)) {
      return isEmpty(b);
    }
    return a.equals(b);
  }
  


  public static boolean equalsIgnoreCaseIgnoreNull(String a, String b)
  {
    if (isEmpty(a)) {
      return isEmpty(b);
    }
    return a.equalsIgnoreCase(b);
  }
  


  private static DecimalFormat getDecimalFormat(int length)
  {
    if (length < 0) {
      length = 0;
    }
    
    if (DECIMAL_FORMAT_STRS.length > length) {
      return new DecimalFormat(DECIMAL_FORMAT_STRS[length]);
    }
    
    StringBuilder sb = new StringBuilder("#.");
    for (int i = 0; i < length; i++) {
      sb.append("#");
    }
    return new DecimalFormat(sb.toString());
  }
  





  public static String lpadByte(String src, char padChar, int len)
  {
    byte[] bb = src == null ? new byte[0] : src.getBytes();
    
    if (bb.length >= len) {
      return new String(bb, 0, len);
    }
    
    byte[] pad = new byte[len];
    int padLen = len - bb.length;
    System.arraycopy(bb, 0, pad, padLen, bb.length);
    
    for (int i = 0; i < padLen; i++) {
      pad[i] = ((byte)padChar);
    }
    return new String(pad);
  }
  





  public static String rpadByte(String src, char padChar, int len)
  {
    byte[] bb = src == null ? new byte[0] : src.getBytes();
    
    if (bb.length >= len) {
      return new String(bb, 0, len);
    }
    
    byte[] pad = new byte[len];
    System.arraycopy(bb, 0, pad, 0, bb.length);
    for (int i = bb.length; i < pad.length; i++) {
      pad[i] = ((byte)padChar);
    }
    return new String(pad);
  }
  


  public static String[] splitKorString(String str, int length)
  {
    char[] array = str.toCharArray();
    int len = array.length;
    if (length > len) {
      return new String[] { str };
    }
    List<String> list = new ArrayList();
    StringBuilder buff = new StringBuilder();
    int counter = 0;
    for (int i = 0; i < len; i++) {
      char c = array[i];
      int l = c > '' ? 2 : 1;
      if ((l == 2) && 
        (length < 2)) {
        throw new IllegalArgumentException("Length must be greater than 1.");
      }
      
      if ((counter > length - 1) || ((l == 2) && (counter + 1 > length - 1))) {
        list.add(buff.toString());
        buff.setLength(0);
        counter = 0;
      }
      buff.append(c);
      counter += l;
    }
    
    if (buff.length() > 0) {
      list.add(buff.toString());
    }
    return (String[])list.toArray(new String[list.size()]);
  }
  
  public static String convertToHalfChars(String source)
  {
    if (source == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    char c = '\000';
    int length = source.length();
    for (int i = 0; i < length; i++)
    {
      c = source.charAt(i);
      
      if ((c >= 65281) && (c <= 65374))
      {
        c = (char)(c - 65248);
      }
      else if (c == '　')
      {
        c = ' ';
      }
      
      sb.append(c);
    }
    return sb.toString();
  }
  


  public static String convertToFullChars(String source)
  {
    if (source == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    char c = '\000';
    int length = source.length();
    
    for (int i = 0; i < length; i++)
    {
      c = source.charAt(i);
      
      if ((c >= '!') && (c <= '~'))
      {
        c = (char)(c + 65248);

      }
      else if (c == ' ')
      {
        c = '　';
      }
      
      sb.append(c);
    }
    return sb.toString();
  }
  

  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  

  public static boolean isNumeric(String src)
  {
    if ((src == null) || (src.length() < 1)) {
      return false;
    }
    for (int i = 0; i < src.length(); i++) {
      if ("1234567890".indexOf(src.substring(i, i + 1)) < 0) {
        return false;
      }
    }
    return true;
  }
  



  public static String[] substringsBetween(String str, String open, String close)
  {
    if ((str == null) || (isEmpty(open)) || (isEmpty(close))) {
      return null;
    }
    int strLen = str.length();
    if (strLen == 0) {
      return EMPTY_STRING_ARRAY;
    }
    int closeLen = close.length();
    int openLen = open.length();
    List list = new ArrayList();
    int pos = 0;
    while (pos < strLen - closeLen) {
      int start = str.indexOf(open, pos);
      if (start < 0) {
        break;
      }
      start += openLen;
      int end = str.indexOf(close, start);
      if (end < 0) {
        break;
      }
      list.add(str.substring(start, end));
      pos = end + closeLen;
    }
    if (list.isEmpty()) {
      return null;
    }
    return (String[])list.toArray(new String[list.size()]);
  }
  

  public static String setMaskString(String str, int start)
  {
    return setMaskString(str, start, '*');
  }
  


  public static String setMaskString(String str, int start, char c)
  {
    return setMaskString(str, start, str.length() - start, c);
  }
  


  public static String setMaskString(String str, int start, int len)
  {
    return setMaskString(str, start, len, '*');
  }
  
  public static String setMaskString(String str, int start, int len, char c)
  {
    StringBuilder returnStr = new StringBuilder();
    returnStr.append(str.substring(0, start));
    for (int i = 0; i < len; i++) {
      returnStr.append(c);
    }
    returnStr.append(str.substring(start + len));
    return returnStr.toString();
  }
  
  public static String makeFormat(String format, String value)
  {
    int i = 0;
    int j = 0;
    String retn = "";
    
    if (format == null) return "";
    if (value == null) return "";
    if (format.length() == 0) return "";
    if (value.length() == 0) { return "";
    }
    while ((j < format.length()) && (i < value.length())) {
      if (format.charAt(j) == '#') {
        retn = retn + value.charAt(i);
        i++;
        j++;
      } else {
        retn = retn + format.charAt(j);
        j++;
      }
    }
    
    return retn;
  }
  
  public static String trim(String value)
  {
    if (value == null) {
      return null;
    }
    return value.trim();
  }
  




  public static String ltrim(String value)
  {
    if (value == null) {
      return null;
    }
    int st = 0;
    char[] val = value.toCharArray();
    int len = val.length;
    while ((st < len) && ((val[st] <= ' ') || (val[st] == '　'))) {
      st++;
    }
    return st > 0 ? value.substring(st) : value;
  }
  




  public static String rtrim(String value)
  {
    if (value == null) {
      return null;
    }
    int st = 0;
    char[] val = value.toCharArray();
    int count = val.length;
    int len = count;
    while ((st < len) && ((val[(len - 1)] <= ' ') || (val[(len - 1)] == '　'))) {
      len--;
    }
    return len < count ? value.substring(st, len) : value;
  }
  





  public static int length(String value)
  {
    if (value == null) {
      return 0;
    }
    return value.length();
  }
  




  public static String nvl(String value)
  {
    if (value == null) {
      return "";
    }
    return value;
  }
  
  public static boolean trimequals(String s1, String s2)
  {
    if ((s1 != null) && (s2 != null)) return s1.trim().equals(s2.trim());
    if ((s1 == null) && (s2 == null)) return true;
    return false;
  }
  
  public static String fixLength(String value, int length)
  {
    if (value == null) {
      return value;
    }
    
    if (value.length() * 3 < length) {
      return value;
    }
    byte[] tmp = value.getBytes();
    if (tmp.length <= length) {
      return value;
    }
    return new String(tmp, 0, length);
  }
  


  public static String toCSVString(String value, char dataSplitDelimiter, char dataWrapDelimiter)
  {
    if (value == null) {
      return "";
    }
    
    int dataSplitDelimiterIdx = value.indexOf(dataSplitDelimiter);
    int dataWrapDelimiterIdx = value.indexOf(dataWrapDelimiter);
    int lineDelimiterIdx = value.indexOf('\n');
    
    if ((dataSplitDelimiterIdx < 0) && (dataWrapDelimiterIdx < 0) && (lineDelimiterIdx < 0))
    {
      return value;
    }
    StringBuilder buff = new StringBuilder(value.length());
    buff.append(dataWrapDelimiter);
    if (dataWrapDelimiterIdx < 0)
    {
      buff.append(value);
    }
    else {
      for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (c == dataWrapDelimiter) {
          buff.append(dataWrapDelimiter);
          buff.append(dataWrapDelimiter);
        } else {
          buff.append(c);
        }
      }
    }
    buff.append(dataWrapDelimiter);
    return buff.toString();
  }
}

