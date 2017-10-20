package com.asiainfo.omm.app.userapp.ivalues;
import com.ai.appframe2.common.DataStructInterface;
import java.sql.Timestamp;
public interface IBOOmmMenuRelatRoleValue extends DataStructInterface{

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


public String getState();

public Timestamp getCreateTime();

public long getId();

public String getRemark();

public String getMenuId();

public Timestamp getUpdateTime();

public String getLimitRank();

public Timestamp getExpiryTime();

public String getExt1();

public String getExt2();

public String getRoleId();

public String getExt3();


public  void setState(String value);

public  void setCreateTime(Timestamp value);

public  void setId(long value);

public  void setRemark(String value);

public  void setMenuId(String value);

public  void setUpdateTime(Timestamp value);

public  void setLimitRank(String value);

public  void setExpiryTime(Timestamp value);

public  void setExt1(String value);

public  void setExt2(String value);

public  void setRoleId(String value);

public  void setExt3(String value);
}
