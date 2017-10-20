package com.asiainfo.omm.app.userapp.bo;

import java.sql.*;
import com.ai.appframe2.bo.DataContainer;
import com.ai.appframe2.common.DataContainerInterface;
import com.ai.appframe2.common.AIException;
import com.ai.appframe2.common.ServiceManager;
import com.ai.appframe2.common.ObjectType;
import com.ai.appframe2.common.DataType;

import com.asiainfo.omm.app.userapp.ivalues.*;

public class BOOmmMenuRelatRoleBean extends DataContainer implements DataContainerInterface,IBOOmmMenuRelatRoleValue{

  private static String  m_boName = "com.asiainfo.omm.app.userapp.bo.BOOmmMenuRelatRole";



  public final static  String S_State = "STATE";
  public final static  String S_CreateTime = "CREATE_TIME";
  public final static  String S_Id = "ID";
  public final static  String S_Remark = "REMARK";
  public final static  String S_MenuId = "MENU_ID";
  public final static  String S_UpdateTime = "UPDATE_TIME";
  public final static  String S_LimitRank = "LIMIT_RANK";
  public final static  String S_ExpiryTime = "EXPIRY_TIME";
  public final static  String S_Ext1 = "EXT1";
  public final static  String S_Ext2 = "EXT2";
  public final static  String S_RoleId = "ROLE_ID";
  public final static  String S_Ext3 = "EXT3";

  public static ObjectType S_TYPE = null;
  static{
    try {
      S_TYPE = ServiceManager.getObjectTypeFactory().getInstance(m_boName);
    }catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  public BOOmmMenuRelatRoleBean() throws AIException{
      super(S_TYPE);
  }

 public static ObjectType getObjectTypeStatic() throws AIException{
   return S_TYPE;
 }

 public void setObjectType(ObjectType  value) throws AIException{
   //此种数据容器不能重置业务对象类型
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

  public void initMenuId(String value){
     this.initProperty(S_MenuId,value);
  }
  public  void setMenuId(String value){
     this.set(S_MenuId,value);
  }
  public  void setMenuIdNull(){
     this.set(S_MenuId,null);
  }

  public String getMenuId(){
       return DataType.getAsString(this.get(S_MenuId));
  
  }
  public String getMenuIdInitialValue(){
        return DataType.getAsString(this.getOldObj(S_MenuId));
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

  public void initLimitRank(String value){
     this.initProperty(S_LimitRank,value);
  }
  public  void setLimitRank(String value){
     this.set(S_LimitRank,value);
  }
  public  void setLimitRankNull(){
     this.set(S_LimitRank,null);
  }

  public String getLimitRank(){
       return DataType.getAsString(this.get(S_LimitRank));
  
  }
  public String getLimitRankInitialValue(){
        return DataType.getAsString(this.getOldObj(S_LimitRank));
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

  public void initRoleId(String value){
     this.initProperty(S_RoleId,value);
  }
  public  void setRoleId(String value){
     this.set(S_RoleId,value);
  }
  public  void setRoleIdNull(){
     this.set(S_RoleId,null);
  }

  public String getRoleId(){
       return DataType.getAsString(this.get(S_RoleId));
  
  }
  public String getRoleIdInitialValue(){
        return DataType.getAsString(this.getOldObj(S_RoleId));
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


 
 }

