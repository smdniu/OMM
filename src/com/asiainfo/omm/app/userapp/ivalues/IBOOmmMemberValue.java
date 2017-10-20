package com.asiainfo.omm.app.userapp.ivalues;
import com.ai.appframe2.common.DataStructInterface;
import java.sql.Timestamp;
public interface IBOOmmMemberValue extends DataStructInterface{

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


public String getState();

public String getName();

public Timestamp getCreateTime();

public String getRemark();

public String getEncryptType();

public Timestamp getUpdateTime();

public Timestamp getExpiryTime();

public String getAccount();

public String getDept();

public String getTel();

public long getId();

public String getEmail();

public String getExt1();

public String getPassword();

public String getExt2();

public String getExt3();

public String getIpAddress();


public  void setState(String value);

public  void setName(String value);

public  void setCreateTime(Timestamp value);

public  void setRemark(String value);

public  void setEncryptType(String value);

public  void setUpdateTime(Timestamp value);

public  void setExpiryTime(Timestamp value);

public  void setAccount(String value);

public  void setDept(String value);

public  void setTel(String value);

public  void setId(long value);

public  void setEmail(String value);

public  void setExt1(String value);

public  void setPassword(String value);

public  void setExt2(String value);

public  void setExt3(String value);

public  void setIpAddress(String value);
}
