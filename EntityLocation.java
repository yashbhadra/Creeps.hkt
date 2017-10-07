package com.creeps.hkthn;

public class EntityLocation {

	private String ID;
	private String lat;
	private String longitude;
	private int id;

	EntityLocation(int id,String ID,String lat,String longitude)
	{
		this.ID = ID;
		this.lat = lat;
		this.longitude = longitude;
		this.id=id;
	}


	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public String getLat() {
		lat = lat.substring(1, lat.length()-1);
		
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLongitude() {
		longitude = longitude.substring(1, longitude.length()-1);
		
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
