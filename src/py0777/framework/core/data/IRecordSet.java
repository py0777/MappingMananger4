package py0777.framework.core.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract interface IRecordSet
  extends Serializable, Cloneable, IPagination
{
  public abstract void setId(String paramString);
  
  public abstract String getId();
  
  public abstract boolean addHeader(IRecordHeader paramIRecordHeader);
  
  public abstract void addHeader(int paramInt, IRecordHeader paramIRecordHeader);
  
  public abstract boolean addHeaders(Collection<IRecordHeader> paramCollection);
  
  public abstract IRecordHeader setHeader(int paramInt, IRecordHeader paramIRecordHeader);
  
  public abstract IRecordHeader getHeader(String paramString);
  
  public abstract IRecordHeader getHeader(int paramInt);
  
  public abstract Iterator<IRecordHeader> getHeaders();
  
  public abstract int getHeaderCount();
  
  public abstract boolean addRecord(IRecord paramIRecord);
  
  public abstract boolean addRecord(int paramInt, IRecord paramIRecord);
  
  public abstract boolean addRecords(Collection<IRecord> paramCollection);
  
  public abstract IRecord setRecord(int paramInt, IRecord paramIRecord);
  
  public abstract IRecord getRecord(int paramInt);
  
  public abstract Iterator<IRecord> getRecords();
  
  public abstract IRecord removeRecord(int paramInt);
  
  public abstract IRecord removeRecord(int paramInt, boolean paramBoolean);
  
  public abstract int getRecordCount();
  
  public abstract String get(int paramInt1, int paramInt2);
  
  public abstract String get(int paramInt, String paramString);
  
  public abstract int getHeaderIndex(String paramString);
  
  public abstract boolean removeColumnData(int paramInt);
  
  public abstract boolean removeColumnData(String paramString);
  
  public abstract void init();
  
  public abstract void initHeader();
  
  public abstract void initRecord();
  
  public abstract Object clone();
  
  public abstract Map<String, Object> getRecordMap(int paramInt);
  
  public abstract List<Map<String, Object>> getRecordMaps();
  
  public abstract void addColumnData(IRecordHeader paramIRecordHeader, String[] paramArrayOfString);
  
  public abstract void setPageNo(int paramInt);
  
  public abstract void setRecordCountPerPage(int paramInt);
  
  public abstract void setTotalRecordCount(int paramInt);
  
  public abstract IRecord newRecord();
  
  public abstract IRecord newRecord(int paramInt);
  
  public abstract IRecordSet clone(String paramString, String[] paramArrayOfString);
  
  public abstract void trimRecord(int paramInt);
}
