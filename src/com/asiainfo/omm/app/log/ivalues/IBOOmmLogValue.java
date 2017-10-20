package com.asiainfo.omm.app.log.ivalues;
import com.ai.appframe2.common.DataStructInterface;
import java.sql.Timestamp;
public interface IBOOmmLogValue extends DataStructInterface{

  public final static  String S_RequesParam = "REQUES_PARAM";
  public final static  String S_Name = "NAME";
  public final static  String S_Remark = "REMARK";
  public final static  String S_Path = "PATH";
  public final static  String S_Dotime = "DOTIME";
  public final static  String S_Account = "ACCOUNT";
  public final static  String S_RequestIp = "REQUEST_IP";
  public final static  String S_LogDate = "LOG_DATE";
  public final static  String S_Msg = "MSG";
  public final static  String S_Pathname = "PATHNAME";
  public final static  String S_SessionId = "SESSION_ID";
  public final static  String S_Id = "ID";
  public final static  String S_ServiceIp = "SERVICE_IP";
  public final static  String S_LogType = "LOG_TYPE";
  public final static  String S_Ext1 = "EXT1";
  public final static  String S_Ext2 = "EXT2";
  public final static  String S_Ext3 = "EXT3";
  public final static  String S_Code = "CODE";


public String getRequesParam();

public String getName();

public String getRemark();

public String getPath();

public Timestamp getDotime();

public String getAccount();

public String getRequestIp();

public Timestamp getLogDate();

public String getMsg();

public String getPathname();

public String getSessionId();

public long getId();

public String getServiceIp();

public String getLogType();

public String getExt1();

public String getExt2();

public String getExt3();

public String getCode();


public  void setRequesParam(String value);

public  void setName(String value);

public  void setRemark(String value);

public  void setPath(String value);

public  void setDotime(Timestamp value);

public  void setAccount(String value);

public  void setRequestIp(String value);

public  void setLogDate(Timestamp value);

public  void setMsg(String value);

public  void setPathname(String value);

public  void setSessionId(String value);

public  void setId(long value);

public  void setServiceIp(String value);

public  void setLogType(String value);

public  void setExt1(String value);

public  void setExt2(String value);

public  void setExt3(String value);

public  void setCode(String value);
}
