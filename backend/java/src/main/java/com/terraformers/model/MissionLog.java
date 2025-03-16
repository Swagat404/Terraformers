package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.sql.Time;

// line 104 "model.ump"
// line 225 "model.ump"
public class MissionLog extends Log
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //MissionLog Associations
  private Mission mission;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public MissionLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage, Mission aMission)
  {
    super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage);
    boolean didAddMission = setMission(aMission);
    if (!didAddMission)
    {
      throw new RuntimeException("Unable to create missionLog due to mission. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Mission getMission()
  {
    return mission;
  }
  /* Code from template association_SetOneToMany */
  public boolean setMission(Mission aMission)
  {
    boolean wasSet = false;
    if (aMission == null)
    {
      return wasSet;
    }

    Mission existingMission = mission;
    mission = aMission;
    if (existingMission != null && !existingMission.equals(aMission))
    {
      existingMission.removeMissionLog(this);
    }
    mission.addMissionLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Mission placeholderMission = mission;
    this.mission = null;
    if(placeholderMission != null)
    {
      placeholderMission.removeMissionLog(this);
    }
    super.delete();
  }

}


