package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 83 "model.ump"
// line 212 "model.ump"
public class Location
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Location Attributes
  private int locationID;
  private float longitude;
  private float latitude;
  private float altitude;
  private float slope;

  //Location Associations
  private Terrain terrain;
  private Avatar avatar;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Location(int aLocationID, float aLongitude, float aLatitude, float aAltitude, float aSlope, Terrain aTerrain)
  {
    locationID = aLocationID;
    longitude = aLongitude;
    latitude = aLatitude;
    altitude = aAltitude;
    slope = aSlope;
    boolean didAddTerrain = setTerrain(aTerrain);
    if (!didAddTerrain)
    {
      throw new RuntimeException("Unable to create location due to terrain. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLocationID(int aLocationID)
  {
    boolean wasSet = false;
    locationID = aLocationID;
    wasSet = true;
    return wasSet;
  }

  public boolean setLongitude(float aLongitude)
  {
    boolean wasSet = false;
    longitude = aLongitude;
    wasSet = true;
    return wasSet;
  }

  public boolean setLatitude(float aLatitude)
  {
    boolean wasSet = false;
    latitude = aLatitude;
    wasSet = true;
    return wasSet;
  }

  public boolean setAltitude(float aAltitude)
  {
    boolean wasSet = false;
    altitude = aAltitude;
    wasSet = true;
    return wasSet;
  }

  public boolean setSlope(float aSlope)
  {
    boolean wasSet = false;
    slope = aSlope;
    wasSet = true;
    return wasSet;
  }

  public int getLocationID()
  {
    return locationID;
  }

  public float getLongitude()
  {
    return longitude;
  }

  public float getLatitude()
  {
    return latitude;
  }

  public float getAltitude()
  {
    return altitude;
  }

  public float getSlope()
  {
    return slope;
  }
  /* Code from template association_GetOne */
  public Terrain getTerrain()
  {
    return terrain;
  }
  /* Code from template association_GetOne */
  public Avatar getAvatar()
  {
    return avatar;
  }

  public boolean hasAvatar()
  {
    boolean has = avatar != null;
    return has;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setTerrain(Terrain aTerrain)
  {
    boolean wasSet = false;
    //Must provide terrain to location
    if (aTerrain == null)
    {
      return wasSet;
    }

    if (terrain != null && terrain.numberOfLocations() <= Terrain.minimumNumberOfLocations())
    {
      return wasSet;
    }

    Terrain existingTerrain = terrain;
    terrain = aTerrain;
    if (existingTerrain != null && !existingTerrain.equals(aTerrain))
    {
      boolean didRemove = existingTerrain.removeLocation(this);
      if (!didRemove)
      {
        terrain = existingTerrain;
        return wasSet;
      }
    }
    terrain.addLocation(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setAvatar(Avatar aNewAvatar)
  {
    boolean wasSet = false;
    if (avatar != null && !avatar.equals(aNewAvatar) && equals(avatar.getLocation()))
    {
      //Unable to setAvatar, as existing avatar would become an orphan
      return wasSet;
    }

    avatar = aNewAvatar;
    Location anOldLocation = aNewAvatar != null ? aNewAvatar.getLocation() : null;

    if (!this.equals(anOldLocation))
    {
      if (anOldLocation != null)
      {
        anOldLocation.avatar = null;
      }
      if (avatar != null)
      {
        avatar.setLocation(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Terrain placeholderTerrain = terrain;
    this.terrain = null;
    if(placeholderTerrain != null)
    {
      placeholderTerrain.removeLocation(this);
    }
    Avatar existingAvatar = avatar;
    avatar = null;
    if (existingAvatar != null)
    {
      existingAvatar.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "locationID" + ":" + getLocationID()+ "," +
            "longitude" + ":" + getLongitude()+ "," +
            "latitude" + ":" + getLatitude()+ "," +
            "altitude" + ":" + getAltitude()+ "," +
            "slope" + ":" + getSlope()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "terrain = "+(getTerrain()!=null?Integer.toHexString(System.identityHashCode(getTerrain())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "avatar = "+(getAvatar()!=null?Integer.toHexString(System.identityHashCode(getAvatar())):"null");
  }
}


