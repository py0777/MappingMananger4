package py0777.sql.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import nexcore.framework.core.data.RecordSet;
import py0777.framework.core.data.IRecord;
import py0777.framework.core.data.IRecordSet;
import py0777.framework.core.data.Record;
import py0777.sql.format.valueobject.PySqlToken;
import py0777.sql.format.valueobject.SqlLineToken;
import py0777.sql.format.PySqlFormatter;
import py0777.sql.format.PySqlFormatterException;
import py0777.sql.format.PySqlParser;
import py0777.sql.format.PySqlRule;

public class PySqlFormatter
{
	private final PySqlParser	fParser			= new PySqlParser();

	private PySqlRule			fRule			= null;

	private Stack<Boolean>		functionBracket	= new Stack();

	public PySqlFormatter(PySqlRule argRule)
	{
		fRule = argRule;
	}

	public String format(String argSql) throws PySqlFormatterException
	{
		StringBuffer sb = new StringBuffer();

		/* Stack Clear */
		functionBracket.clear();
		try
		{
			boolean isSqlEndsWithNewLine = false;
			if (argSql.endsWith("\n"))
			{
				isSqlEndsWithNewLine = true;
			}
			/* SQL Token처리 */
			List<PySqlToken> list = fParser.parse(argSql);

			/* Sql format 모듈 호출 */
			list = format(list);

			/***********************************************
			 * 주석자동 달기 메타와 연동시 표준용어로 주석생성 메타 시스템 존재고객에게만...
			 ***********************************************/
			// String[] tmp;
			// BufferedReader reader1 = new BufferedReader(new FileReader(
			// "data.dat"));
			//
			// String test = "";
			// while (reader1.ready()) {
			// String line = reader1.readLine();
			// if (line == null)
			// break;
			// test = test + line;
			// }
			// reader1.close();
			// tmp = test.split(",");
			//
			// for (int i=list.size()-2; i > 0; i--) {
			// PySqlToken token = (PySqlToken) list.get(i);
			// PySqlToken next = (PySqlToken) list.get(i+1);
			//
			// if(token.getType() == 3 && next.getString().equals("\n")){
			// System.out.println("token.getType()" + token.getType() + "]");
			// for(int j = 0; j <tmp.length ; j++){
			// if(tmp[j].equalsIgnoreCase(token.getString())){
			//
			// list.add(i+1, new PySqlToken(0, " "));
			// list.add(i+2, new PySqlToken(5,
			// "/* ".concat(tmp[j+1]).concat(" */")));
			// continue;
			// }
			// }
			// }
			// }
			/***********************************************
			 * ALIAS AS 위치 설정하기 offset 30 이하의 as는30에 맞추기
			 ***********************************************/
			int l_asPos = 0;
			int l_findPos = 0;

			for (int i = 0; i < list.size(); i++)
			{
				PySqlToken token = (PySqlToken) list.get(i);
				String str = token.getString();
				if ("\n".equalsIgnoreCase(token.getString()))
				{
					l_findPos = -1;
				}
				l_findPos += token.getString().length();
				if ("AS".equalsIgnoreCase(token.getString()))
				{
					System.out.println("[l_findPos] [" + l_findPos + "]");
					for (int j = 0; j < (30 - l_findPos); j++)
					{

						str = " " + str;
					}
					token.setString(str);
				}
			}
			/***********************************************
			 * 주석 위치 설정하기 offset 50 이하의 주석은 50에 맞추기
			 ***********************************************/

			l_findPos = 0;
			for (int i = 1; i < list.size(); i++)
			{
				PySqlToken token = (PySqlToken) list.get(i);
				PySqlToken prev = (PySqlToken) list.get(i - 1);
				String str = token.getString();
				if ("\n".equalsIgnoreCase(prev.getString()))
				{
					l_findPos = -1;
				}
				l_findPos += prev.getString().length();
				if (" ".equals(prev.getString()) && token.getType() == 5)
				{
					System.out.println("[l_findPos] [" + l_findPos + "]");
					for (int j = 0; j < (50 - l_findPos); j++)
					{

						str = " " + str;
					}
					token.setString(str);
				}
			}
			/***********************************************
			 * RETURN 위치 설정
			 ***********************************************/
			String after = "";
			for (int i = 0; i < list.size(); i++)
			{
				PySqlToken token = (PySqlToken) list.get(i);
				after = after + token.getString();

			}

			if (isSqlEndsWithNewLine)
			{
			}
			return after + "\n";

		}
		catch (Exception ex)
		{
			PySqlFormatterException sqlException = new PySqlFormatterException(ex.toString());

			sqlException.initCause(ex);
			throw sqlException;
		}
	}

	public List<PySqlToken> format(List<PySqlToken> argList)
	{
		/***********************************************
		 * Declare Var
		 ***********************************************/

		List<SqlLineToken> list = new ArrayList();
		int indent = 0; /* 간격띄우기 */

		PySqlToken token = (PySqlToken) argList.get(0);
		if (token.getType() == 0)
		{
			/* 첫문자 앞 공백 문자는 모두 지우기 */
			argList.remove(0);
		}

		token = (PySqlToken) argList.get(argList.size() - 1);
		if (token.getType() == 0)
		{
			/* 마지막문자 뒤공백 문자는 모두 지우기 */
			argList.remove(argList.size() - 1);
		}

		/***********************************************
		 * 키워드 대문자 처리
		 ***********************************************/
		for (int i = 0; i < argList.size(); i++)
		{
			token = (PySqlToken) argList.get(i);
			if (token.getType() == 2)
			{
				switch (fRule.keyword)
				{
				case 0:
					break;
				case 1:
					token.setString(token.getString().toUpperCase());
					break;
				case 2:
					token.setString(token.getString().toLowerCase());
				}

			}
		}

		/***********************************************
		 * 공백문자 처리
		 ***********************************************/
		for (int index = argList.size() - 1; index >= 1; index--)
		{
			token = (PySqlToken) argList.get(index);
			PySqlToken prevToken = (PySqlToken) argList.get(index - 1);
			if ((token.getType() == 0) && ((prevToken.getType() == 1) || (prevToken.getType() == 5)))
			{
				/* symbol또는 comment 뒤 공백문자는 list에서 삭제 */
				argList.remove(index);
			}
			else if (((token.getType() == 1) || (token.getType() == 5)) && (prevToken.getType() == 0))
			{
				/* symbol또는 comment 앞 공백문자는 list에서 삭제 */
				argList.remove(index - 1);
			}
			else if (token.getType() == 0)
			{
				/* 공백문자는 " "로 치환 */
				token.setString(" ");
			}
		}

		/***********************************************
		 * : binding 변수 처리
		 ***********************************************/
		// for (int i = 0; i < argList.size()-1; i++) {
		// PySqlToken t0= (PySqlToken) argList.get(i);
		// PySqlToken t1 = (PySqlToken) argList.get(i + 1);
		// if (":".equals(t0.getString()) ) {
		//
		// t0.setString(t0.getString().concat(t1.getString()));
		// argList.remove(i+1);
		// System.out.println(argList.get(i).getString());
		// }
		// System.out.println("["+argList.get(i).getType()+","+argList.get(i).getString()+"]");
		//
		// }
		/* ORDER BY , GROUP BY, (+) 문자 처리 */
		for (int index = 0; index < argList.size() - 2; index++)
		{
			PySqlToken t0 = (PySqlToken) argList.get(index);
			PySqlToken t1 = (PySqlToken) argList.get(index + 1);
			PySqlToken t2 = (PySqlToken) argList.get(index + 2);

			if (t0.getString().equals("(") && (t1.getString().equals("+") || t1.getString().equals("*")) && t2.getString().equals(")"))
			{

				if (t1.getString().equals("+"))
				{
					t0.setString("(+)");
				}
				else
				{
					t0.setString("(*)");
				}
				argList.remove(index + 1);
				argList.remove(index + 1);
			}

		}

		PySqlToken prev = new PySqlToken(0, " ");
		PySqlToken next = new PySqlToken(0, " ");
		PySqlToken next1 = new PySqlToken(0, " ");
		boolean encounterBetween = false;
		boolean encounterWhen = false;

		for (int index = 0; index < argList.size(); index++)
		{
			token = (PySqlToken) argList.get(index);

			/* SYMBOL 처리 */
			if (token.getType() == 1)
			{
				if ("(".equals(token.getString()))
				{
					next = (PySqlToken) argList.get(index + 1);

					if (list.size() > 0 && ("INTO".equalsIgnoreCase(list.get(list.size() - 1).getValue()) || "VALUES".equalsIgnoreCase(list.get(list.size() - 1).getValue())))
					{
						functionBracket.push(Boolean.FALSE);
						index += insertReturnAndIndent(argList, list, index);
					}
					else
					{
						if (fRule.isFunction(next.getString()))
						{
							functionBracket.push(Boolean.TRUE);
						}
						else if (next.getType() == 2)
						{
							functionBracket.push(Boolean.FALSE);
						}
						else if (next.getType() == 5)
						{
							next1 = (PySqlToken) argList.get(index + 2);
							if (next1.getType() == 2)
							{
								functionBracket.push(Boolean.FALSE);
							}
							else
							{
								functionBracket.push(Boolean.TRUE);
							}
						}
						else if (fRule.isFunction(prev.getString()))
						{
							functionBracket.push(Boolean.TRUE);
						}
						else if (next.getType() == 3)
						{
							functionBracket.push(Boolean.TRUE);
						}
						else if (next.getType() == 1)
						{
							functionBracket.push(Boolean.TRUE);
						}
						else
						{
							functionBracket.push(Boolean.TRUE);
						}

						index += insertReturnAndIndent(argList, list, index + 1);
					}

				}
				else if (token.getString().equals(")"))
				{

					index += insertReturnAndIndent(argList, list, index);

					functionBracket.pop();

				}
				else if (token.getString().equals(","))
				{
					insertReturnAndIndent(argList, list, index);

				}
				else if (token.getString().equals(";"))
				{
					insertReturnAndIndent(argList, list, index);
				}
			}
			/* KEYWORD 처리 */
			else if (token.getType() == 2)
			{
				if ((token.getString().equalsIgnoreCase("DELETE")) || (token.getString().equalsIgnoreCase("SELECT")) || (token.getString().equalsIgnoreCase("UPDATE"))
						|| (token.getString().equalsIgnoreCase("INSERT")) || (token.getString().equalsIgnoreCase("MERGE")) || (token.getString().equalsIgnoreCase("CREATE"))
						|| (token.getString().equalsIgnoreCase("DROP")) || (token.getString().equalsIgnoreCase("TRUNCATE")) || (token.getString().equalsIgnoreCase("TABLE")))
				{

					insertReturnAndIndent(argList, list, index);
				}

				if ((token.getString().equalsIgnoreCase("INTO")))
				{

					insertReturnAndIndent(argList, list, index);
				}

				if ((token.getString().equalsIgnoreCase("FROM")) || (token.getString().equalsIgnoreCase("WHERE")) || (token.getString().equalsIgnoreCase("SET"))
						|| (token.getString().equalsIgnoreCase("ORDER")) || (token.getString().equalsIgnoreCase("GROUP")) || (token.getString().equalsIgnoreCase("HAVING"))
						|| (token.getString().equalsIgnoreCase("BY")))
				{

					insertReturnAndIndent(argList, list, index);

				}

				if (token.getString().equalsIgnoreCase("VALUES"))
				{

					insertReturnAndIndent(argList, list, index);
				}

				if (token.getString().equalsIgnoreCase("END"))
				{

					insertReturnAndIndent(argList, list, index);
					encounterWhen = false;
				}
				if (token.getString().equalsIgnoreCase("CASE"))
				{
					if (!",".equals(prev.getString()))
					{
						insertReturnAndIndent(argList, list, index);
					}
				}

				if (token.getString().equalsIgnoreCase("WHEN"))
				{

					PySqlToken prevToken = (PySqlToken) argList.get(index - 2);
					if (!prevToken.getString().equalsIgnoreCase("CASE"))
					{

						// index += insertReturnAndIndent(argList, index,indent
						// + 1);
						insertReturnAndIndent(argList, list, index);
					}
					encounterWhen = true;
				}
				if (token.getString().equalsIgnoreCase("ELSE"))
				{
					insertReturnAndIndent(argList, list, index);
				}

				if ((token.getString().equalsIgnoreCase("ON")) || (token.getString().equalsIgnoreCase("USING")) || (token.getString().equalsIgnoreCase("ALL")))
				{

					insertReturnAndIndent(argList, list, index);
				}

				if ((token.getString().equalsIgnoreCase("UNION")) || (token.getString().equalsIgnoreCase("INTERSECT")) || (token.getString().equalsIgnoreCase("EXCEPT")))
				{

					insertReturnAndIndent(argList, list, index);
				}
				if (token.getString().equalsIgnoreCase("OR"))
				{

					insertReturnAndIndent(argList, list, index);
				}
				if (token.getString().equalsIgnoreCase("BETWEEN"))
				{
					encounterBetween = true;
				}
				if (token.getString().equalsIgnoreCase("AND"))
				{
					// System.out.println("[AND] ["+ encounterBetween +"]["+
					// encounterWhen +"]");
					if (!encounterBetween && !encounterWhen)
					{
						// index += insertReturnAndIndent(argList, index,
						// indent);
						insertReturnAndIndent(argList, list, index);

					}
					encounterBetween = false;
				}

			}
			/* COMMENT 처리 */
			else if ((token.getType() == 5) && (token.getString().startsWith("/*")))
			{
				// index += insertReturnAndIndent(argList, list, index);
			}

			System.out.println("[token] [" + token.getType() + "][" + token.getString() + "]");
			prev = token;
		}

		/***********************************************
		 * list값 출력해보기
		 ***********************************************/
		int l_indent1 = 0;
		List<SqlLineToken> arrIndent1 = new ArrayList();
		for (int i = 0; i < list.size(); i++)
		{

			if (list.get(i).getIndent() > 0)
			{
				arrIndent1.add(list.get(i));
				l_indent1 += list.get(i).getIndent();
			}
			else if (list.get(i).getIndent() < 0 && arrIndent1.size() > 0)
			{
				l_indent1 += arrIndent1.get(arrIndent1.size() - 1).getIndent() * (-1);
				arrIndent1.remove(arrIndent1.size() - 1);
			}

			PySqlToken sqlToken = (PySqlToken) argList.get(list.get(i).getIndex());
			String str = sqlToken.getString();

			System.out.println("[str] [" + i + "][" + str + "][" + list.get(i).getValue() + "][" + l_indent1 + "]");

		}
		/***********************************************
		 * 공백설정
		 ***********************************************/
		int l_indent = 0;
		List<SqlLineToken> arrIndent = new ArrayList();
		for (int i = 0; i < list.size(); i++)
		{

			if (list.get(i).getIndent() > 0)
			{
				arrIndent.add(list.get(i));
				l_indent += list.get(i).getIndent();
			}
			else if (list.get(i).getIndent() < 0 && arrIndent.size() > 0)
			{
				l_indent += arrIndent.get(arrIndent.size() - 1).getIndent() * (-1);
				arrIndent.remove(arrIndent.size() - 1);
			}

			PySqlToken sqlToken = (PySqlToken) argList.get(list.get(i).getIndex());
			String str = sqlToken.getString();

			for (int j = 0; j < l_indent - sqlToken.getString().length(); j++)
			{
				str = " " + str;
			}
			sqlToken.setString(str);

		}

		for (int i = 1; i < argList.size(); i++)
		{
			prev = (PySqlToken) argList.get(i - 1);
			token = (PySqlToken) argList.get(i);

			if ((prev.getType() != 0) && (token.getType() != 0))
			{

				// if (!prev.getString().equals(",")) {

				if ((!fRule.isFunction(prev.getString())) || (!token.getString().equals("(")))
				{

					argList.add(i, new PySqlToken(0, " "));
				}
				// }
			}
		}
		return argList;
	}

	private int insertReturnAndIndent(List<PySqlToken> argList, List<SqlLineToken> list, int argIndex)
	{

		int findPos = 0;

		try
		{
			String s = "\n";
			PySqlToken token = (PySqlToken) argList.get(argIndex);

//			functionBracket.contains(Boolean.TRUE)
			/* Stack의 마지막이 TRUE일 경우만 return*/
			if (functionBracket.size() > 0  && functionBracket.lastElement() && !"OR".equalsIgnoreCase(token.getString()))
			{
				return 0;
			}

			if (argIndex == 0)
			{
				if ("SELECT".equalsIgnoreCase(token.getString()) || "UPDATE".equalsIgnoreCase(token.getString()) || "INSERT".equalsIgnoreCase(token.getString())
						|| "DELETE".equalsIgnoreCase(token.getString()))
				{
					list.add(new SqlLineToken(6, argIndex, token.getString()));

				}
				else if ("MERGE".equalsIgnoreCase(token.getString()))
				{
					list.add(new SqlLineToken(5, argIndex, token.getString()));
				}

				return 0;
			}
			PySqlToken prevToken = (PySqlToken) argList.get(argIndex - 1);

			if (prevToken.getType() == 0)
			{
				prevToken.setString(s);

				if ("SELECT".equalsIgnoreCase(token.getString()))
				{
					if ("ALL".equalsIgnoreCase(list.get(list.size() - 1).getValue()) || "MINUS".equalsIgnoreCase(list.get(list.size() - 1).getValue())
							|| "UNION".equalsIgnoreCase(list.get(list.size() - 1).getValue()))
					{
						list.add(new SqlLineToken(0, argIndex, token.getString()));
					}
					else
					{
						list.add(new SqlLineToken(6, argIndex, token.getString()));
					}

				}
				else if ("CASE".equalsIgnoreCase(token.getString()))
				{
					list.add(new SqlLineToken(5, argIndex, token.getString()));
				}
				else if ("WHEN".equalsIgnoreCase(token.getString()))
				{
					findPos = findString(argList, "WHEN", list.get(list.size() - 1).getIndex());
					list.add(new SqlLineToken(findPos, argIndex, token.getString()));
					if (findPos > 5)
					{
						list.add(new SqlLineToken(4, argIndex, token.getString()));
					}

				}
				else if ("ELSE".equalsIgnoreCase(token.getString()))
				{
					if ("WHEN".equalsIgnoreCase(list.get(list.size() - 1).getValue()))
					{
						list.add(new SqlLineToken(0, argIndex, token.getString()));
					}
					else
					{
						findPos = findString(argList, "CASE", list.get(list.size() - 1).getIndex());
						list.add(new SqlLineToken(findPos, argIndex, token.getString()));
						list.add(new SqlLineToken(5, argIndex, token.getString()));
					}
				}
				else if ("END".equalsIgnoreCase(token.getString()))
				{
					list.add(new SqlLineToken(-1, argIndex, token.getString()));

				}
				else if ("OR".equalsIgnoreCase(token.getString()) && !"OR".equalsIgnoreCase(list.get(list.size() - 1).getValue()))
				{
					findPos = findString(argList, "(", list.get(list.size() - 1).getIndex());
					list.add(new SqlLineToken(findPos, argIndex, token.getString()));
					// list.add(new SqlLineToken(2, argIndex,
					// token.getString()));
				}
				else if ("VALUES".equalsIgnoreCase(token.getString()))
				{
					list.add(new SqlLineToken(0, argIndex, token.getString()));

				}
				else if (!"OR".equalsIgnoreCase(token.getString()) && "OR".equalsIgnoreCase(list.get(list.size() - 1).getValue()))
				{

					list.add(new SqlLineToken(-1, argIndex, token.getString()));
					// list.add(new SqlLineToken(-1, argIndex,
					// token.getString()));

				}
				else if ("END".equals(list.get(list.size() - 1).getValue()))
				{
					list.add(new SqlLineToken(-1, argIndex, token.getString()));
				}
				else if (")".equals(list.get(list.size() - 1).getValue()))
				{
					list.add(new SqlLineToken(-1, argIndex, token.getString()));
				}
				else
				{
					list.add(new SqlLineToken(0, argIndex, token.getString()));
				}
				return 0;
			}
			else if (prevToken.getType() == 1)
			{
				if ("(".equals(prevToken.getString()))
				{
					list.add(new SqlLineToken(2, argIndex, token.getString()));
				}
			}
			if ("(".equals(token.getString()))
			{
				list.add(new SqlLineToken(2, argIndex + 1, token.getString()));
			}

			if (")".equals(token.getString()))
			{
				list.add(new SqlLineToken(-1, argIndex + 1, token.getString()));

			}

			argList.add(argIndex, new PySqlToken(0, s));

			return 1;
		}
		catch (IndexOutOfBoundsException e)
		{
		}
		return 0;
	}

	private int findString(List<PySqlToken> argList, String agrStr, int argIndex)
	{
		int tmplen = 0;

		for (int i = 0; i < argList.size() - argIndex; i++)
		{
			if (agrStr.equalsIgnoreCase(argList.get(argIndex + i).getString()))
			{
				break;
			}
			else
			{
				tmplen += argList.get(argIndex + i).getString().length();
			}
		}
		return tmplen;
	}

	public static void main(String[] args) throws Exception
	{
		PySqlRule rule = new PySqlRule();
		long keyword = 1;
		String indentString = "  ";
		String[] mySqlFuncs = { "ABS", "ACOS", "ASIN", "ATAN", "ATAN2", "ADD_MONTHS", "TO_CHAR", "COUNT", "SUM", "BIT_COUNT", "CEILING", "COS", "COT", "DEGREES", "EXP", "FLOOR", "LOG", "LOG10",
				"MAX", "MIN", "MOD", "PI", "POW", "POWER", "RADIANS", "RAND", "ROUND", "SIN", "SQRT", "TAN", "TRUNCATE", "ASCII", "BIN", "BIT_LENGTH", "CHAR", "CHARACTER_LENGTH", "CHAR_LENGTH",
				"CONCAT", "CONCAT_WS", "CONV", "ELT", "EXPORT_SET", "FIELD", "FIND_IN_SET", "HEX,INSERT", "INSTR", "LCASE", "LEFT", "LENGTH", "LOAD_FILE", "LOCATE", "LOCATE", "LOWER", "LPAD",
				"LTRIM", "MAKE_SET", "MATCH", "MID", "OCT", "OCTET_LENGTH", "ORD", "POSITION", "QUOTE", "REPEAT", "REPLACE", "REVERSE", "RIGHT", "RPAD", "RTRIM", "SOUNDEX", "SPACE", "STRCMP",
				"SUBSTRING", "SUBSTRING", "SUBSTRING", "SUBSTRING", "SUBSTRING_INDEX", "TRIM", "UCASE", "UPPER", "DATABASE", "USER", "SYSTEM_USER", "SESSION_USER", "PASSWORD", "ENCRYPT",
				"LAST_INSERT_ID", "VERSION", "DAYOFWEEK", "WEEKDAY", "DAYOFMONTH", "DAYOFYEAR", "MONTH", "DAYNAME", "MONTHNAME", "QUARTER", "WEEK", "YEAR", "HOUR", "MINUTE", "SECOND", "PERIOD_ADD",
				"PERIOD_DIFF", "TO_DAYS", "FROM_DAYS", "DATE_FORMAT", "TIME_FORMAT", "CURDATE", "CURRENT_DATE", "CURTIME", "CURRENT_TIME", "NOW", "SYSDATE", "CURRENT_TIMESTAMP", "UNIX_TIMESTAMP",
				"FROM_UNIXTIME", "SEC_TO_TIME", "TIME_TO_SEC" };

		String[] oraSqlFuncs = { "ABS", "ACOS", "ASIN", "ATAN", "ATAN2", "BITAND", "CEIL", "COS", "COSH", "EXP", "FLOOR", "LN", "LOG", "MOD", "NANVL", "POWER", "REMAINDER", "ROUND", "SIGN", "SIN",
				"SINH", "SQRT", "TAN", "TANH", "TRUNC", "WIDTH_BUCKET", "CHR", "CONCAT", "INITCAP", "LOWER", "LPAD", "LTRIM", "NLS_INITCAP", "NLS_LOWER", "NLSSORT", "NLS_UPPER", "REGEXP_REPLACE",
				"REGEXP_SUBSTR", "REPLACE", "RPAD", "RTRIM", "SOUNDEX", "SUBSTR", "TRANSLATE", "TREAT", "TRIM", "UPPER", "ASCII", "INSTR", "LENGTH", "REGEXP_INSTR", "ADD_MONTHS", "CURRENT_DATE",
				"CURRENT_TIMESTAMP", "DBTIMEZONE", "EXTRACT", "FROM_TZ", "LAST_DAY", "LOCALTIMESTAMP", "MONTHS_BETWEEN", "NEW_TIME", "NEXT_DAY", "NUMTODSINTERVAL", "NUMTOYMINTERVAL", "ROUND",
				"SESSIONTIMEZONE", "SYS_EXTRACT_UTC", "SYSDATE", "SYSTIMESTAMP", "TO_CHAR", "TO_TIMESTAMP", "TO_TIMESTAMP_TZ", "TO_DSINTERVAL", "TO_YMINTERVAL", "TRUNC", "TZ_OFFSET", "GREATEST",
				"LEAST", "ASCIISTR", "BIN_TO_NUM", "CAST", "CHARTOROWID", "COMPOSE", "CONVERT", "DECOMPOSE", "HEXTORAW", "NUMTODSINTERVAL", "NUMTOYMINTERVAL", "RAWTOHEX", "RAWTONHEX", "ROWIDTOCHAR",
				"ROWIDTONCHAR", "SCN_TO_TIMESTAMP", "TIMESTAMP_TO_SCN", "TO_BINARY_DOUBLE", "TO_BINARY_FLOAT", "TO_CHAR", "TO_CLOB", "TO_DATE", "TO_DSINTERVAL", "TO_LOB", "TO_MULTI_BYTE", "TO_NCHAR",
				"TO_NCLOB", "TO_NUMBER", "TO_DSINTERVAL", "TO_SINGLE_BYTE", "TO_TIMESTAMP", "TO_TIMESTAMP_TZ", "TO_YMINTERVAL", "TO_YMINTERVAL", "UNISTR", "COALESCE", "LNNVL", "NULLIF", "NVL",
				"NVL2", "AVG", "COLLECT", "CORR", "CORR_*", "COUNT", "COVAR_POP", "COVAR_SAMP", "CUME_DIST", "DENSE_RANK", "FIRST", "GROUP_ID", "GROUPING", "GROUPING_ID", "LAST", "MAX", "MEDIAN",
				"MIN", "PERCENTILE_CONT", "PERCENTILE_DISC", "PERCENT_RANK", "RANK", "STATS_BINOMIAL_TEST", "STATS_CROSSTAB", "STATS_F_TEST", "STATS_KS_TEST", "STATS_MODE", "STATS_MW_TEST",
				"STATS_ONE_WAY_ANOVA", "STATS_T_TEST_*", "STATS_WSR_TEST", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE" };

		rule.setFunctionNames(oraSqlFuncs);
		PySqlFormatter formatter = new PySqlFormatter(rule);

		BufferedReader reader = new BufferedReader(new FileReader("test.sql"));
		// //C:\\javaide\\workspace\\MappingManager4\\src\\py0777\\sql\\format\\
		File cdw = new File(".");
		System.out.println(cdw.getAbsoluteFile());
		System.out.println(cdw.getAbsolutePath());
		String before = "";
		while (reader.ready())
		{
			String line = reader.readLine();
			if (line == null) break;
			before = before + line + "\n";
		}
		reader.close();

		System.out.println("[before]\n" + before);
		String after = formatter.format(before);
		System.out.println("[after]\n" + after);

	}
}