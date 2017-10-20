package com.asiainfo.omm.app.log.bo;

import java.sql.*;
import com.ai.appframe2.bo.DataContainer;
import com.ai.appframe2.common.DataContainerInterface;
import com.ai.appframe2.common.AIException;
import com.ai.appframe2.common.ServiceManager;
import com.ai.appframe2.common.ObjectType;
import com.ai.appframe2.common.DataType;

import com.asiainfo.omm.app.log.ivalues.*;

public class BOOmmLogBean extends DataContainer implements DataContainerInterface,IBOOmmLogValue{

  private static String  m_boName = "com.asiainfo.omm.app.log.bo.BOOmmLog";



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

  public static ObjectType S_TYPE = null;
  static{
    try {
      S_TYPE = ServiceManager.getObjectTypeFactory().getInstance(m_boName);
    }catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  public BOOmmLogBean() throws AIException{
      super(S_TYPE);
  }

 public static ObjectType getObjectTypeStatic() throws AIException{
   return S_TYPE;
 }

 public void setObjectType(ObjectType  value) throws AIException{
   //此种数据容器不能重置业务对象类型
   throw new AIException("Cannot reset ObjectType");
 }


  public void initRequesParam(String value){
     this.initProperty(S_RequesParam,value);
  }
  public  void setRequesParam(String value){
     this.set(S_RequesParam,value);
  }
  public  void setRequesParamNull(){
     this.set(S_RequesParam,null);
  }

  public String getRequesParam(){
       return DataType.getAsString(this.get(S_RequesParam));
  
  }
  public String getRequesParamInitialValue(){
        return DataType.getAsString(this.getOldObj(S_RequesParam));
      }

  public void initName(String value){
     this.initProperty(S_Name,value);
  }
  public  void setName(String value){
     this.set(S_Name,value);
  }
  public  void setNameNull(){
     this.set(S_Name,null);
  }

  public String getName(){
       return DataType.getAsString(this.get(S_Name));
  
  }
  public String getNameInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Name));
      }

  public void initRemark(String value){
     this.initProperty(S_Remark,value);
  }
  public  void setRemark(String value){
     this.set(S_Remark,value);
  }
  public  void setRemarkNull(){
     this.set(S_Remark,null);
  }

  public String getRemark(){
       return DataType.getAsString(this.get(S_Remark));
  
  }
  public String getRemarkInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Remark));
      }

  public void initPath(String value){
     this.initProperty(S_Path,value);
  }
  public  void setPath(String value){
     this.set(S_Path,value);
  }
  public  void setPathNull(){
     this.set(S_Path,null);
  }

  public String getPath(){
       return DataType.getAsString(this.get(S_Path));
  
  }
  public String getPathInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Path));
      }

  public void initDotime(Timestamp value){
     this.initProperty(S_Dotime,value);
  }
  public  void setDotime(Timestamp value){
     this.set(S_Dotime,value);
  }
  public  void setDotimeNull(){
     this.set(S_Dotime,null);
  }

  public Timestamp getDotime(){
        return DataType.getAsDateTime(this.get(S_Dotime));
  
  }
  public Timestamp getDotimeInitialValue(){
        return DataType.getAsDateTime(this.getOldObj(S_Dotime));
      }

  public void initAccount(String value){
     this.initProperty(S_Account,value);
  }
  public  void setAccount(String value){
     this.set(S_Account,value);
  }
  public  void setAccountNull(){
     this.set(S_Account,null);
  }

  public String getAccount(){
       return DataType.getAsString(this.get(S_Account));
  
  }
  public String getAccountInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Account));
      }

  public void initRequestIp(String value){
     this.initProperty(S_RequestIp,value);
  }
  public  void setRequestIp(String value){
     this.set(S_RequestIp,value);
  }
  public  void setRequestIpNull(){
     this.set(S_RequestIp,null);
  }

  public String getRequestIp(){
       return DataType.getAsString(this.get(S_RequestIp));
  
  }
  public String getRequestIpInitialValue(){
        return DataType.getAsString(this.getOldObj(S_RequestIp));
      }

  public void initLogDate(Timestamp value){
     this.initProperty(S_LogDate,value);
  }
  public  void setLogDate(Timestamp value){
     this.set(S_LogDate,value);
  }
  public  void setLogDateNull(){
     this.set(S_LogDate,null);
  }

  public Timestamp getLogDate(){
        return DataType.getAsDateTime(this.get(S_LogDate));
  
  }
  public Timestamp getLogDateInitialValue(){
        return DataType.getAsDateTime(this.getOldObj(S_LogDate));
      }

  public void initMsg(String value){
     this.initProperty(S_Msg,value);
  }
  public  void setMsg(String value){
     this.set(S_Msg,value);
  }
  public  void setMsgNull(){
     this.set(S_Msg,null);
  }

  public String getMsg(){
       return DataType.getAsString(this.get(S_Msg));
  
  }
  public String getMsgInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Msg));
      }

  public void initPathname(String value){
     this.initProperty(S_Pathname,value);
  }
  public  void setPathname(String value){
     this.set(S_Pathname,value);
  }
  public  void setPathnameNull(){
     this.set(S_Pathname,null);
  }

  public String getPathname(){
       return DataType.getAsString(this.get(S_Pathname));
  
  }
  public String getPathnameInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Pathname));
      }

  public void initSessionId(String value){
     this.initProperty(S_SessionId,value);
  }
  public  void setSessionId(String value){
     this.set(S_SessionId,value);
  }
  public  void setSessionIdNull(){
     this.set(S_SessionId,null);
  }

  public String getSessionId(){
       return DataType.getAsString(this.get(S_SessionId));
  
  }
  public String getSessionIdInitialValue(){
        return DataType.getAsString(this.getOldObj(S_SessionId));
      }

  public void initId(long value){
     this.initProperty(S_Id,new Long(value));
  }
  public  void setId(long value){
     this.set(S_Id,new Long(value));
  }
  public  void setIdNull(){
     this.set(S_Id,null);
  }

  public long getId(){
        return DataType.getAsLong(this.get(S_Id));
  
  }
  public long getIdInitialValue(){
        return DataType.getAsLong(this.getOldObj(S_Id));
      }

  public void initServiceIp(String value){
     this.initProperty(S_ServiceIp,value);
  }
  public  void setServiceIp(String value){
     this.set(S_ServiceIp,value);
  }
  public  void setServiceIpNull(){
     this.set(S_ServiceIp,null);
  }

  public String getServiceIp(){
       return DataType.getAsString(this.get(S_ServiceIp));
  
  }
  public String getServiceIpInitialValue(){
        return DataType.getAsString(this.getOldObj(S_ServiceIp));
      }

  public void initLogType(String value){
     this.initProperty(S_LogType,value);
  }
  public  void setLogType(String value){
     this.set(S_LogType,value);
  }
  public  void setLogTypeNull(){
     this.set(S_LogType,null);
  }

  public String getLogType(){
       return DataType.getAsString(this.get(S_LogType));
  
  }
  public String getLogTypeInitialValue(){
        return DataType.getAsString(this.getOldObj(S_LogType));
      }

  public void initExt1(String value){
     this.initProperty(S_Ext1,value);
  }
  public  void setExt1(String value){
     this.set(S_Ext1,value);
  }
  public  void setExt1Null(){
     this.set(S_Ext1,null);
  }

  public String getExt1(){
       return DataType.getAsString(this.get(S_Ext1));
  
  }
  public String getExt1InitialValue(){
        return DataType.getAsString(this.getOldObj(S_Ext1));
      }

  public void initExt2(String value){
     this.initProperty(S_Ext2,value);
  }
  public  void setExt2(String value){
     this.set(S_Ext2,value);
  }
  public  void setExt2Null(){
     this.set(S_Ext2,null);
  }

  public String getExt2(){
       return DataType.getAsString(this.get(S_Ext2));
  
  }
  public String getExt2InitialValue(){
        return DataType.getAsString(this.getOldObj(S_Ext2));
      }

  public void initExt3(String value){
     this.initProperty(S_Ext3,value);
  }
  public  void setExt3(String value){
     this.set(S_Ext3,value);
  }
  public  void setExt3Null(){
     this.set(S_Ext3,null);
  }

  public String getExt3(){
       return DataType.getAsString(this.get(S_Ext3));
  
  }
  public String getExt3InitialValue(){
        return DataType.getAsString(this.getOldObj(S_Ext3));
      }

  public void initCode(String value){
     this.initProperty(S_Code,value);
  }
  public  void setCode(String value){
     this.set(S_Code,value);
  }
  public  void setCodeNull(){
     this.set(S_Code,null);
  }

  public String getCode(){
       return DataType.getAsString(this.get(S_Code));
  
  }
  public String getCodeInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Code));
      }


 
 }

