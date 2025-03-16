package com.terraformers.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 195 "model.ump"
// line 277 "model.ump"

import java.time.LocalDateTime;

public class Reading
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reading Attributes
  private int readingID;
  private LocalDateTime timeStamp;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reading(int aReadingID, LocalDateTime aTimeStamp)
  {
    readingID = aReadingID;
    timeStamp = aTimeStamp;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReadingID(int aReadingID)
  {
    boolean wasSet = false;
    readingID = aReadingID;
    wasSet = true;
    return wasSet;
  }

  public boolean setTimeStamp(LocalDateTime aTimeStamp)
  {
    boolean wasSet = false;
    timeStamp = aTimeStamp;
    wasSet = true;
    return wasSet;
  }

  public int getReadingID()
  {
    return readingID;
  }

  public LocalDateTime getTimeStamp()
  {
    return timeStamp;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "readingID" + ":" + getReadingID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "timeStamp" + "=" + (getTimeStamp() != null ? !getTimeStamp().equals(this)  ? getTimeStamp().toString().replaceAll("  ","    ") : "this" : "null");
  }
}


