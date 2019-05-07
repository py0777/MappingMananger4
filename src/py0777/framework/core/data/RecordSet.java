package py0777.framework.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import py0777.framework.core.util.AppUtils;
import py0777.framework.core.util.StringUtils;

public class RecordSet
  implements IRecordSet
{
  public static final String RECORDSET_MAX_RECORD_LIMIT = "recordset.max.record.limit";
  public static final String RECORDSET_MAX_RECORD_PER_PAGE_LIMIT = "recordset.max.recordPerPage.limit";
  private static final long serialVersionUID = -2406858111150703912L;
  private String id = null;
  
  private int pageNo = -1;
  
  private int recordCountPerPage = -1;
  
  private int totalRecordCount = -1;
  
  private boolean paging;
  
  private boolean nextPage;
  
  private ArrayList<IRecordHeader> headers = new ArrayList();
  private Map<String, Integer> headerIndexes = new HashMap();
  
  private ArrayList<IRecord> records = new ArrayList();
  

  private static int maxRecordLimit = -1;
  


  private String referenceTransactionId;
  

  private String referenceRequestId;
  

  private String referenceStatementId;
  


  public RecordSet(String id)
  {
    this(id, (String[])null);
  }
  


  public RecordSet(String id, Collection<IRecordHeader> headers)
  {
    this(id, headers, null);
  }
  


  public RecordSet(String id, Collection<IRecordHeader> headers, boolean headerDupIgnore)
  {
    this(id, headers, null, headerDupIgnore);
  }
  


  /**
   * @deprecated
   */
  public RecordSet(String id, Collection<IRecordHeader> headers, Collection<IRecord> records)
  {
    this(id, headers, records, false);
  }
  



  /**
   * @deprecated
   */
  public RecordSet(String id, Collection<IRecordHeader> headers, Collection<IRecord> records, boolean headerDupIgnore)
  {
    setId(id);
    
    checkRecordMax(records == null ? 0 : records.size());
    
    if (headers != null) {
      if (headerDupIgnore) {
        for (IRecordHeader header : headers) {
          setHeader(header);
        }
        
      } else {
        for (IRecordHeader header : headers) {
          addHeader(header);
        }
      }
    }
    
    setupRecordMaxLimit();
    
    if (records != null) {
      for (IRecord record : records) {
        addRecord(record);
      }
    }
  }
  

  public RecordSet(String id, String[] headerNames)
  {
    setId(id);
    if (headerNames != null) {
      for (String headerName : headerNames) {
        addHeader(new RecordHeader(headerName));
      }
    }
    setupRecordMaxLimit();
  }
  


  public RecordSet(String id, Map<Object, Object> map)
  {
    setId(id);
    
    IRecord record = new Record(getHeaderCount());
    addRecord(record);
    

    if (map != null) {
      for (Map.Entry<Object, Object> entry : map.entrySet()) {
        String headerName = StringUtils.transferToString(entry.getKey());
        addHeader(new RecordHeader(headerName));
        record.set(headerName, entry.getValue());
      }
    }
    setupRecordMaxLimit();
  }
  



  public String getId()
  {
    return id;
  }
  


  public void setId(String id)
  {
    if (id == null) {
      throw new NullPointerException("RecordSet must have a valid id.");
    }
    this.id = id;
  }
  


  public boolean addHeader(IRecordHeader header)
  {
    if (header == null) {
      throw new NullPointerException("RecordHeader must not be null.");
    }
    

    checkContainsHeader(header);
    
    boolean flag = headers.add(header);
    int fIndex; if (flag) {
      fIndex = headers.size() - 1;
      
      headerIndexes.put(header.getName(), Integer.valueOf(fIndex));
      
      for (IRecord r : records) {
        ((Record)r).add(fIndex, (Object)null);
      }
    }
    return flag;
  }
  


  public void addHeader(int fIndex, IRecordHeader header)
  {
    if (header == null) {
      throw new NullPointerException("RecordHeader must not be null.");
    }
    

    checkContainsHeader(header);
    try
    {
      headers.add(fIndex, header);
      
      headerIndexes.put(header.getName(), Integer.valueOf(fIndex));
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") Index: " + fIndex + ", HeaderCount: " + getHeaderCount());
    }
    

    for (IRecord r : records) {
      ((Record)r).add(fIndex, (Object)null);
    }
  }
  
  private void checkContainsHeader(IRecordHeader header) {
    if (headers.contains(header)) {
      throw new RuntimeException("The same record header name(" + header.getName() + ") already exist.");
    }
  }
  



  public boolean addHeaders(Collection<IRecordHeader> collection)
  {
    if (collection == null) {
      return false;
    }
    
    int old = getHeaderCount();
    
    for (IRecordHeader h : collection) {
      addHeader(h);
    }
    
    return getHeaderCount() > old;
  }
  
  public IRecordHeader setHeader(int fIndex, IRecordHeader header)
  {
    try
    {
      IRecordHeader headerOld = (IRecordHeader)headers.set(fIndex, header);
      
      headerIndexes.put(header.getName(), Integer.valueOf(fIndex));
      return headerOld;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") Index: " + fIndex + ", HeaderCount: " + getHeaderCount());
    }
  }
  
  private void setHeader(IRecordHeader header) {
    boolean flag = headers.add(header);
    if (flag)
    {
      headerIndexes.put(header.getName(), Integer.valueOf(headers.size() - 1));
    }
  }
  
  public IRecordHeader getHeader(String headerName)
  {
    int fIndex = getHeaderIndex(headerName);
    
    if (fIndex < 0) {
      return null;
    }
    return getHeader(fIndex);
  }
  

  public IRecordHeader getHeader(int fIndex)
  {
    try
    {
      return (IRecordHeader)headers.get(fIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") Index: " + fIndex + ", HeaderCount: " + getHeaderCount());
    }
  }
  


  public Iterator<IRecordHeader> getHeaders()
  {
    return headers.iterator();
  }
  


  public int getHeaderCount()
  {
    return headers.size();
  }
  



  public boolean addRecord(IRecord record)
  {
    if (record == null) {
      throw new NullPointerException("Record must not be null.");
    }
    
    checkRecordMax();
    
    if ((this == record.getRecordSet()) && 
      (records.contains(record))) {
      return false;
    }
    

    boolean b = records.add(record);
    record.addRecordSet(this);
    
    return b;
  }
  


  public boolean addRecord(int rowIndex, IRecord record)
  {
    if (record == null) {
      throw new NullPointerException("Record must not be null.");
    }
    
    checkRecordMax();
    
    if ((this == record.getRecordSet()) && 
      (records.contains(record))) {
      return false;
    }
    
    try
    {
      records.add(rowIndex, record);
      record.addRecordSet(this);
      return true;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") RecordIndex: " + rowIndex + ", RecordCount: " + getRecordCount());
    }
  }
  


  public boolean addRecords(Collection<IRecord> collection)
  {
    if (collection == null) {
      return false;
    }
    
    int old = getRecordCount();
    
    for (Object obj : collection) {
      addRecord((IRecord)obj);
    }
    
    return getRecordCount() > old;
  }
  


  public IRecord setRecord(int rowIndex, IRecord record)
  {
    if (record == null) {
      throw new NullPointerException("Record must not be null.");
    }
    try
    {
      IRecord oldValue = (IRecord)records.set(rowIndex, record);
      record.addRecordSet(this);
      return oldValue;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") RecordIndex: " + rowIndex + ", RecordCount: " + getRecordCount());
    }
  }
  

  public IRecord getRecord(int rowIndex)
  {
    try
    {
      return (IRecord)records.get(rowIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") RecordIndex: " + rowIndex + ", RecordCount: " + getRecordCount());
    }
  }
  


  public Iterator<IRecord> getRecords()
  {
    return records.iterator();
  }
  


  public IRecord removeRecord(int rowIndex)
  {
    return removeRecord(rowIndex, true);
  }
  

  public IRecord removeRecord(int rowIndex, boolean removeRecordSet)
  {
    try
    {
      IRecord record = (IRecord)records.remove(rowIndex);
      if ((removeRecordSet) && 
        (record.getRecordSet() == this)) {
        record.removeRecordSet();
      }
      
      return record;
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") RecordIndex: " + rowIndex + ", RecordCount: " + getRecordCount());
    }
  }
  


  public int getRecordCount()
  {
    return records.size();
  }
  



  public String get(int rowIndex, int fIndex)
  {
    IRecord record = getRecord(rowIndex);
    return record.get(fIndex);
  }
  

  public String get(int rowIndex, String headerName)
  {
    int fIndex = getHeaderIndex(headerName);
    
    if (fIndex == -1) {
      return null;
    }
    return get(rowIndex, fIndex);
  }
  


  public int getHeaderIndex(String headerName)
  {
    int headerSize = getHeaderCount();
    
    if (headerSize == 0) {
      return -1;
    }
    
   Integer fIndex = (Integer)headerIndexes.get(headerName);
    if (fIndex == null) {
      for (int i = 0; i < headerSize; i++) {
        IRecordHeader header = getHeader(i);
        if (header.equals(headerName)) {
          fIndex = Integer.valueOf(i);
          headerIndexes.put(headerName, Integer.valueOf(i));
          break;
        }
      }
    }
    
    return fIndex == null ? -1 : fIndex.intValue();
  }
  


  public boolean removeColumnData(int fIndex)
  {
    if (fIndex < 0) {
      return false;
    }
    try
    {
      headers.remove(fIndex);
      
      List<String> removeList = new ArrayList(headerIndexes.size());
      Iterator<Map.Entry<String, Integer>> entrys = headerIndexes.entrySet().iterator();
      while (entrys.hasNext()) {
        Map.Entry<String, Integer> entry = (Map.Entry)entrys.next();
        if (((Integer)entry.getValue()).intValue() == fIndex) {
          removeList.add(entry.getKey());
        }
      }
      for (String name : removeList) {
        headerIndexes.remove(name);
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException("RecordSet(" + id + ") HeaderIndex: " + fIndex + ", HeaderCount: " + getHeaderCount());
    }
    
    int recordSize = getRecordCount();
    
    for (int i = 0; i < recordSize; i++)
    {


      getRecord(i).removeField(fIndex);
    }
    
    return true;
  }
  
  public boolean removeColumnData(String headerName)
  {
    int fIndex = getHeaderIndex(headerName);
    
    return removeColumnData(fIndex);
  }
  

  public Object clone()
  {
    RecordSet cloned = new RecordSet(getId());
    
    Iterator iterHeaders = getHeaders();
    
    while (iterHeaders.hasNext())
    {

      IRecordHeader header = (IRecordHeader)iterHeaders.next();
      cloned.addHeader((IRecordHeader)header.clone());
    }
    
    Iterator iterRecords = getRecords();
    
    while (iterRecords.hasNext())
    {

      IRecord record = (IRecord)iterRecords.next();
      cloned.addRecord((IRecord)record.clone());
    }
    
    return cloned;
  }
  

  public String toString()
  {
    StringBuffer sbuf = new StringBuffer();
    
    sbuf.append("================================================================================\n");
    
    int recordCnt = getRecordCount();
    int headerCnt = getHeaderCount();
    int width = 0;
    
    if (recordCnt == 0) {
      width = headerCnt;
    } else {
      IRecord record = getRecord(0);
      width = record.size();
    }
    
    sbuf.append(getId() + " RECORD SET [" + recordCnt + "]X[" + width + "]\n");
    sbuf.append("--------------------------------------------------------------------------------\n");
    

    if (headerCnt == 0) {
      sbuf.append("NO HEADER INFORMATION.\n");
    } else {
      sbuf.append(getHeader(0).getName());
      
      for (int i = 1; i < headerCnt; i++) {
        sbuf.append(" | " + getHeader(i).getName());
      }
      
      sbuf.append("\n");
    }
    sbuf.append("--------------------------------------------------------------------------------\n");
    

    if (recordCnt == 0) {
      sbuf.append("NO RECORD.\n");
    }
    else {
      for (int i = 0; i < recordCnt; i++) {
        sbuf.append(getRecord(i).toString());
      }
    }
    
    sbuf.append("================================================================================\n");
    

    return sbuf.toString();
  }
  


  public Map<String, Object> getRecordMap(int rowIndex)
  {
    if (getRecordCount() == 0)
    {

      return new CaseIgnoreHashMap(0);
    }
    
    int headerSize = getHeaderCount();
    
    if (headerSize == 0) {
      throw new RuntimeException("RecordHeader is undefined.");
    }
    


    Map<String, Object> retMap = new CaseIgnoreHashMap(headerSize);
    
    IRecord record = getRecord(rowIndex);
    
    for (int fIndex = 0; fIndex < headerSize; fIndex++)
    {
      retMap.put(getHeader(fIndex).getName(), record.getObject(fIndex));
    }
    
    return retMap;
  }
  


  public List<Map<String, Object>> getRecordMaps()
  {
    int rowSize = getRecordCount();
    
    if (rowSize == 0)
    {

      return new ArrayList(0);
    }
    


    List<Map<String, Object>> rList = new ArrayList(rowSize);
    
    for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
      rList.add(getRecordMap(rowIndex));
    }
    
    return rList;
  }
  

  public void addColumnData(IRecordHeader header, String[] columnData)
  {
    if (getRecordCount() == 0) {
      for (int i = 0; i < columnData.length; i++) {
        addRecord(new Record());
      }
      if (getHeaderCount() > 0) {
        initHeader();
      }
    }
    


    int recordCnt = getRecordCount();
    
    if (columnData.length != recordCnt) {
      throw new RuntimeException("The count of Record and input column data size must be the same. But the current count of record is " + recordCnt + " and the input coumn data size is " + columnData.length);
    }
    


    addHeader(header);
    

    if (recordCnt > 0) {
      int headerCount = getHeaderCount();
      for (int i = 0; i < recordCnt; i++) {
        IRecord record = getRecord(i);
        if (headerCount != record.size()) {
          throw new RuntimeException("The Header Count (" + headerCount + ") does not equal to the Record Size (" + record.size() + ") in the " + i + "-th Record.");
        }
        


        record.set(record.size() - 1, columnData[i]);
      }
    }
  }
  

  public void init()
  {
    initHeader();
    initRecord();
    setId(null);
  }
  


  public void initHeader()
  {
    headers.clear();
    headerIndexes.clear();
  }
  


  public void initRecord()
  {
    int recordSize = getRecordCount();
    
    for (int i = 0; i < recordSize; i++) {
      IRecord record = getRecord(i);
      record.removeRecordSet();
    }
    
    records.clear();
  }
  



  public int getPageNo()
  {
    return pageNo;
  }
  


  public int getRecordCountPerPage()
  {
    return recordCountPerPage;
  }
  


  public int getTotalRecordCount()
  {
    return totalRecordCount;
  }
  
  public boolean isPaging() {
    return paging;
  }
  
  public void setPaging(boolean paging) {
    this.paging = paging;
  }
  
  public boolean hasNextPage() {
    return nextPage;
  }
  
  public void setNextPage(boolean nextPage) {
    this.nextPage = nextPage;
  }
  


  public void setPageNo(int pageNo)
  {
    this.pageNo = pageNo;
  }
  


  public void setRecordCountPerPage(int recordCountPerPage)
  {
    this.recordCountPerPage = recordCountPerPage;
  }
  


  public void setTotalRecordCount(int totalRecordCount)
  {
    this.totalRecordCount = totalRecordCount;
  }
  


  public IRecord newRecord()
  {
    Record record = new Record(getHeaderCount());
    


    addRecord(record);
    return record;
  }
  


  public IRecord newRecord(int index)
  {
    Record record = new Record(getHeaderCount());
    


    addRecord(index, record);
    return record;
  }
  



  public IRecordSet clone(String id, String[] headerNames)
  {
    IRecordSet newRS = new RecordSet(id);
    
    for (int i = 0; i < headerNames.length; i++) {
      IRecordHeader hdr = new RecordHeader(headerNames[i]);
      newRS.addHeader(hdr);
    }
    
    Iterator oldRowIter = getRecords();
    while (oldRowIter.hasNext()) {
      IRecord oldRow = (IRecord)oldRowIter.next();
      IRecord newRow = newRS.newRecord();
      
      for (int i = 0; i < headerNames.length; i++) {
        try {
          newRow.set(i, oldRow.get(headerNames[i]));
        } catch (RuntimeException e) {
          if (oldRow.containsKey(headerNames[i]))
          {

            throw e;
          }
        }
      }
    }
    return newRS;
  }
  


  public void trimRecord(int remainerRecordCount)
  {
    int initSize = getRecordCount();
    if ((remainerRecordCount > -1) && 
      (remainerRecordCount < initSize)) {
      for (int i = initSize - 1; i >= remainerRecordCount; i--) {
        removeRecord(i);
      }
    }
  }
  
  private void setupRecordMaxLimit()
  {
    if (maxRecordLimit == -1)
    {
      String maxRecordLimitProperty = AppUtils.getProperty("recordset.max.record.limit");
      if (maxRecordLimitProperty == null) {
        maxRecordLimit = 0;
      } else {
        maxRecordLimit = Integer.parseInt(maxRecordLimitProperty);
      }
    }
  }
  
  private void checkRecordMax(int i) {
    if (maxRecordLimit > 0)
    {
      if (i >= maxRecordLimit) {
        
        throw new IndexOutOfBoundsException("checkRecordMax(" + i + ")");
      }
    }
  }
  
  private void checkRecordMax() {
    if (maxRecordLimit > 0) {
      checkRecordMax(records.size());
    }
  }
  


  public String getReferenceTransactionId()
  {
    return referenceTransactionId;
  }
  
  public void setReferenceTransactionId(String referenceTransactionId) {
    this.referenceTransactionId = referenceTransactionId;
  }
  
  public String getReferenceRequestId() {
    return referenceRequestId;
  }
  
  public void setReferenceRequestId(String referenceRequestId) {
    this.referenceRequestId = referenceRequestId;
  }
  
  public String getReferenceStatementId() {
    return referenceStatementId;
  }
  
  public void setReferenceStatementId(String referenceStatementId) {
    this.referenceStatementId = referenceStatementId;
  }
}
