package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.sql.Time;

// line 127 "model.ump"
// line 239 "model.ump"
public class AvatarLog extends Log
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //AvatarLog Associations
  private Avatar avatar;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public AvatarLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage, Avatar aAvatar)
  {
    super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage);
    boolean didAddAvatar = setAvatar(aAvatar);
    if (!didAddAvatar)
    {
      throw new RuntimeException("Unable to create avatarLog due to avatar. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Avatar getAvatar()
  {
    return avatar;
  }
  /* Code from template association_SetOneToMany */
  public boolean setAvatar(Avatar aAvatar)
  {
    boolean wasSet = false;
    if (aAvatar == null)
    {
      return wasSet;
    }

    Avatar existingAvatar = avatar;
    avatar = aAvatar;
    if (existingAvatar != null && !existingAvatar.equals(aAvatar))
    {
      existingAvatar.removeAvatarLog(this);
    }
    avatar.addAvatarLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Avatar placeholderAvatar = avatar;
    this.avatar = null;
    if(placeholderAvatar != null)
    {
      placeholderAvatar.removeAvatarLog(this);
    }
    super.delete();
  }

}


