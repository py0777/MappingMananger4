package py0777.orasql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import py0777.sql.format.PySqlFormatter;
import py0777.sql.format.PySqlFormatterException;
import py0777.sql.format.PySqlRule;

public class SQLBeautifier
{
  static final String[] functionsList = { "ABS","ACOS","ASIN","ATAN","ATAN2","BITAND"
      ,"CEIL","COS","COSH","EXP","FLOOR","LN"
      ,"LOG","MOD","NANVL","POWER","REMAINDER"
      ,"ROUND","SIGN","SIN","SINH","SQRT","TAN"
      ,"TANH","TRUNC","WIDTH_BUCKET","CHR","CONCAT"
      ,"INITCAP","LOWER","LPAD","LTRIM","NLS_INITCAP"
      ,"NLS_LOWER","NLSSORT","NLS_UPPER","REGEXP_REPLACE"
      ,"REGEXP_SUBSTR","REPLACE","RPAD","RTRIM","SOUNDEX"
      ,"SUBSTR","TRANSLATE","TREAT","TRIM","UPPER"
      ,"ASCII","INSTR","LENGTH","REGEXP_INSTR","ADD_MONTHS"
      ,"CURRENT_DATE","CURRENT_TIMESTAMP","DBTIMEZONE"
      ,"EXTRACT","FROM_TZ","LAST_DAY","LOCALTIMESTAMP"
      ,"MONTHS_BETWEEN","NEW_TIME","NEXT_DAY","NUMTODSINTERVAL"
      ,"NUMTOYMINTERVAL","ROUND","SESSIONTIMEZONE"
      ,"SYS_EXTRACT_UTC","SYSDATE","SYSTIMESTAMP","TO_CHAR"
      ,"TO_TIMESTAMP","TO_TIMESTAMP_TZ","TO_DSINTERVAL"
      ,"TO_YMINTERVAL","TRUNC","TZ_OFFSET","GREATEST"
      ,"LEAST","ASCIISTR","BIN_TO_NUM","CAST","CHARTOROWID"
      ,"COMPOSE","CONVERT","DECOMPOSE","HEXTORAW"
      ,"NUMTODSINTERVAL","NUMTOYMINTERVAL","RAWTOHEX"
      ,"RAWTONHEX","ROWIDTOCHAR","ROWIDTONCHAR","SCN_TO_TIMESTAMP"
      ,"TIMESTAMP_TO_SCN","TO_BINARY_DOUBLE","TO_BINARY_FLOAT"
      ,"TO_CHAR","TO_CLOB","TO_DATE","TO_DSINTERVAL"
      ,"TO_LOB","TO_MULTI_BYTE","TO_NCHAR","TO_NCLOB"
      ,"TO_NUMBER","TO_DSINTERVAL","TO_SINGLE_BYTE","TO_TIMESTAMP"
      ,"TO_TIMESTAMP_TZ","TO_YMINTERVAL","TO_YMINTERVAL"
      ,"UNISTR","COALESCE","LNNVL","NULLIF","NVL"
      ,"NVL2","AVG","COLLECT","CORR","CORR_*","COUNT"
      ,"COVAR_POP","COVAR_SAMP","CUME_DIST","DENSE_RANK"
      ,"FIRST","GROUP_ID","GROUPING","GROUPING_ID","LAST"
      ,"MAX","MEDIAN","MIN","PERCENTILE_CONT","PERCENTILE_DISC"
      ,"PERCENT_RANK","RANK","STATS_BINOMIAL_TEST","STATS_CROSSTAB"
      ,"STATS_F_TEST","STATS_KS_TEST","STATS_MODE","STATS_MW_TEST"
      ,"STATS_ONE_WAY_ANOVA","STATS_T_TEST_*","STATS_WSR_TEST","STDDEV"
      ,"STDDEV_POP","STDDEV_SAMP","SUM","VAR_POP","VAR_SAMP","VARIANCE" };
  
  public static String beautify(String sqlText)
  {
    String formatedResult = "";
    PySqlRule rule = new PySqlRule();
    rule.setFunctionNames(functionsList);
    PySqlFormatter formatter = new PySqlFormatter(rule);
    try
    {
      formatedResult = formatter.format(sqlText);
    } catch (PySqlFormatterException e) {
      formatedResult = "SQL이 정상적이지 않습니다. ')'괄호 갯수가 맞는지 확인해 보세요..." + System.getProperty("line.separator") ;
    }
    


    return formatedResult;
  }
  
  public static String getInputSQL(String[] args) throws IOException
  {
    BufferedReader br = null;
    if (args.length > 0) {
      br = new BufferedReader(new FileReader(args[0]));
    } else {
      br = new BufferedReader(new InputStreamReader(System.in));
    }
    String sqlText = "";
    String x;
    while ((x = br.readLine()) != null) {
      sqlText = sqlText + x + System.getProperty("line.separator");
    }
    return sqlText;
  }
  
  public static void main(String[] args) {
    String inputString = null;
    try {
      inputString = getInputSQL(args);
    } catch (IOException e) {
      System.err.println("Cannot read input SQL!");
      e.printStackTrace();
    }
    System.out.print(beautify(inputString));
  }
}
