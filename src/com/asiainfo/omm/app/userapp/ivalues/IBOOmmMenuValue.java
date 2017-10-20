package com.asiainfo.omm.app.userapp.ivalues;
import com.ai.appframe2.common.DataStructInterface;
import java.sql.Timestamp;
public interface IBOOmmMenuValue extends DataStructInterface{

  public final static  String S_State = "STATE";
  public final static  String S_Name = "NAME";
  public final static  String S_CreateTime = "CREATE_TIME";
  public final static  String S_MenuCode = "MENU_CODE";
  public final static  String S_MenuAuth = "MENU_AUTH";
  public final static  String S_Path = "PATH";
  public final static  String S_Remark = "REMARK";
  public final static  String S_UpdateTime = "UPDATE_TIME";
  public final static  String S_ExpiryTime = "EXPIRY_TIME";
  public final static  String S_MenuType = "MENU_TYPE";
  public final static  String S_Id = "ID";
  public final static  String S_Ext1 = "EXT1";
  public final static  String S_Ext2 = "EXT2";
  public final static  String S_Ext3 = "EXT3";


public String getState();

public String getName();

public Timestamp getCreateTime();

public String getMenuCode();

public String getMenuAuth();

public String getPath();

public String getRemark();

public Timestamp getUpdateTime();

public Timestamp getExpiryTime();

public String getMenuType();

public long getId();

public String getExt1();

public String getExt2();

public String getExt3();


public  void setState(String value);

public  void setName(String value);

public  void setCreateTime(Timestamp value);

public  void setMenuCode(String value);

public  void setMenuAuth(String value);

public  void setPath(String value);

public  void setRemark(String value);

public  void setUpdateTime(Timestamp value);

public  void setExpiryTime(Timestamp value);

public  void setMenuType(String value);

public  void setId(long value);

public  void setExt1(String value);

public  void setExt2(String value);

public  void setExt3(String value);
}
