package com.asiainfo.omm.app.userapp.bo;

import java.sql.*;
import com.ai.appframe2.bo.DataContainer;
import com.ai.appframe2.common.DataContainerInterface;
import com.ai.appframe2.common.AIException;
import com.ai.appframe2.common.ServiceManager;
import com.ai.appframe2.common.ObjectType;
import com.ai.appframe2.common.DataType;

import com.asiainfo.omm.app.userapp.ivalues.*;

public class BOOmmMemberBean extends DataContainer implements DataContainerInterface,IBOOmmMemberValue{

  private static String  m_boName = "com.asiainfo.omm.app.userapp.bo.BOOmmMember";



  public final static  String S_State = "STATE";
  public final static  String S_Name = "NAME";
  public final static  String S_CreateTime = "CREATE_TIME";
  public final static  String S_Remark = "REMARK";
  public final static  String S_EncryptType = "ENCRYPT_TYPE";
  public final static  String S_UpdateTime = "UPDATE_TIME";
  public final static  String S_ExpiryTime = "EXPIRY_TIME";
  public final static  String S_Account = "ACCOUNT";
  public final static  String S_Dept = "DEPT";
  public final static  String S_Tel = "TEL";
  public final static  String S_Id = "ID";
  public final static  String S_Email = "EMAIL";
  public final static  String S_Ext1 = "EXT1";
  public final static  String S_Password = "PASSWORD";
  public final static  String S_Ext2 = "EXT2";
  public final static  String S_Ext3 = "EXT3";
  public final static  String S_IpAddress = "IP_ADDRESS";

  public static ObjectType S_TYPE = null;
  static{
    try {
      S_TYPE = ServiceManager.getObjectTypeFactory().getInstance(m_boName);
    }catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  public BOOmmMemberBean() throws AIException{
      super(S_TYPE);
  }

 public static ObjectType getObjectTypeStatic() throws AIException{
   return S_TYPE;
 }

 public void setObjectType(ObjectType  value) throws AIException{
   //�������������������ҵ���������
   throw new AIException("Cannot reset ObjectType");
 }


  public void initState(String value){
     this.initProperty(S_State,value);
  }
  public  void setState(String value){
     this.set(S_State,value);
  }
  public  void setStateNull(){
     this.set(S_State,null);
  }

  public String getState(){
       return DataType.getAsString(this.get(S_State));
  
  }
  public String getStateInitialValue(){
        return DataType.getAsString(this.getOldObj(S_State));
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

  public void initCreateTime(Timestamp value){
     this.initProperty(S_CreateTime,value);
  }
  public  void setCreateTime(Timestamp value){
     this.set(S_CreateTime,value);
  }
  public  void setCreateTimeNull(){
     this.set(S_CreateTime,null);
  }

  public Timestamp getCreateTime(){
        return DataType.getAsDateTime(this.get(S_CreateTime));
  
  }
  public Timestamp getCreateTimeInitialValue(){
        return DataType.getAsDateTime(this.getOldObj(S_CreateTime));
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

  public void initEncryptType(String value){
     this.initProperty(S_EncryptType,value);
  }
  public  void setEncryptType(String value){
     this.set(S_EncryptType,value);
  }
  public  void setEncryptTypeNull(){
     this.set(S_EncryptType,null);
  }

  public String getEncryptType(){
       return DataType.getAsString(this.get(S_EncryptType));
  
  }
  public String getEncryptTypeInitialValue(){
        return DataType.getAsString(this.getOldObj(S_EncryptType));
      }

  public void initUpdateTime(Timestamp value){
     this.initProperty(S_UpdateTime,value);
  }
  public  void setUpdateTime(Timestamp value){
     this.set(S_UpdateTime,value);
  }
  public  void setUpdateTimeNull(){
     this.set(S_UpdateTime,null);
  }

  public Timestamp getUpdateTime(){
        return DataType.getAsDateTime(this.get(S_UpdateTime));
  
  }
  public Timestamp getUpdateTimeInitialValue(){
        return DataType.getAsDateTime(this.getOldObj(S_UpdateTime));
      }

  public void initExpiryTime(Timestamp value){
     this.initProperty(S_ExpiryTime,value);
  }
  public  void setExpiryTime(Timestamp value){
     this.set(S_ExpiryTime,value);
  }
  public  void setExpiryTimeNull(){
     this.set(S_ExpiryTime,null);
  }

  public Timestamp getExpiryTime(){
        return DataType.getAsDateTime(this.get(S_ExpiryTime));
  
  }
  public Timestamp getExpiryTimeInitialValue(){
        return DataType.getAsDateTime(this.getOldObj(S_ExpiryTime));
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

  public void initDept(String value){
     this.initProperty(S_Dept,value);
  }
  public  void setDept(String value){
     this.set(S_Dept,value);
  }
  public  void setDeptNull(){
     this.set(S_Dept,null);
  }

  public String getDept(){
       return DataType.getAsString(this.get(S_Dept));
  
  }
  public String getDeptInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Dept));
      }

  public void initTel(String value){
     this.initProperty(S_Tel,value);
  }
  public  void setTel(String value){
     this.set(S_Tel,value);
  }
  public  void setTelNull(){
     this.set(S_Tel,null);
  }

  public String getTel(){
       return DataType.getAsString(this.get(S_Tel));
  
  }
  public String getTelInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Tel));
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

  public void initEmail(String value){
     this.initProperty(S_Email,value);
  }
  public  void setEmail(String value){
     this.set(S_Email,value);
  }
  public  void setEmailNull(){
     this.set(S_Email,null);
  }

  public String getEmail(){
       return DataType.getAsString(this.get(S_Email));
  
  }
  public String getEmailInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Email));
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

  public void initPassword(String value){
     this.initProperty(S_Password,value);
  }
  public  void setPassword(String value){
     this.set(S_Password,value);
  }
  public  void setPasswordNull(){
     this.set(S_Password,null);
  }

  public String getPassword(){
       return DataType.getAsString(this.get(S_Password));
  
  }
  public String getPasswordInitialValue(){
        return DataType.getAsString(this.getOldObj(S_Password));
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

  public void initIpAddress(String value){
     this.initProperty(S_IpAddress,value);
  }
  public  void setIpAddress(String value){
     this.set(S_IpAddress,value);
  }
  public  void setIpAddressNull(){
     this.set(S_IpAddress,null);
  }

  public String getIpAddress(){
       return DataType.getAsString(this.get(S_IpAddress));
  
  }
  public String getIpAddressInitialValue(){
        return DataType.getAsString(this.getOldObj(S_IpAddress));
      }


 
 }

