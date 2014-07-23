package com.example.stethoscope;


public class SensorData {
	
	//private variables
    int _id;
    String _isManual;
    String _Date_Time;
    //declare audio and image
    String _AudioFileName;
    
 
    // Empty constructor
    public SensorData(){
    	this._isManual="";
    	this._Date_Time="";
    	
    }
    
    public SensorData(int id, String isManu, String dt, String afn){
    	this._id = id;
    	this._isManual=isManu;
    	this._Date_Time=dt;
    	this._AudioFileName=afn;
    }
    
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
    
    // getting user isManual either true or false
    public void setIsManual(String isManu){
        this._isManual=isManu;
    }
    
    // setting location
    public void setDateAndTime(String dt){
    	this._Date_Time=dt;
    }
    //setting audio file name
    public void setAudioFileName(String afn){
    	this._AudioFileName=afn;
    }
    
    // getting user isManual
    public String getIsManual(){
        return this._isManual;
    }
    
    // getting user Date
    public String getDateAndTime(){
        return this._Date_Time;
    }
    
    
    // getting audio file name
    public String getAudioFileName(){
        return this._AudioFileName;
    }
}

