package com.example.stethoscope;


public class SensorData {
	
	//private variables
    int _id;
    String _isManual;
    String _Date_Time;
    String _Latitude;
    String _Longitude;
    String _Altitude;
    String _Provider;
    String _Speed;
    String _Bearing;
    //declare audio and image
    String _AudioFileName;
    String _ImageFileName;
    String _ImageContent;
    //Context Information
    String _Question0;
    String _Question1;
    String _Question2;
    String _Question3;
    String _Question4;
    String _Question5;
    String _Question6;
    String _Question7;
    String _Question8;
    String _Question9;
    String _Question10;
    String _Question11;
    String _Question12;
    
 
    // Empty constructor
    public SensorData(){
    	this._isManual="";
    	this._Date_Time="";
    	this._Latitude="";
    	this._Longitude="";
    	this._Altitude="";
    	this._Provider="";
    	this._Speed="";
    	this._Bearing="";
    	this._AudioFileName="";
    	this._ImageFileName="";
    	this._ImageContent="";
    	this._Question0="";
    	this._Question1="";
    	this._Question2="";
    	this._Question3="";
    	this._Question4="";
    	this._Question5="";
    	this._Question6="";
    	this._Question7="";
    	this._Question8="";
    	this._Question9="";
    	this._Question10="";
    	this._Question11="";
    	this._Question12="";
    	
    }
    
    public SensorData(int id, String isManu, String dt, String lat, String lon, String alt, String pro, String spe, String bea,
    		String afn, String ifn, String imageContent, String q0, String q1, String q2, String q3, String q4, String q5, 
    		String q6, String q7, String q8, String q9, String q10, String q11, String q12){
    	this._id = id;
    	this._isManual=isManu;
    	this._Date_Time=dt;
    	this._Latitude=lat;
    	this._Longitude=lon;
    	this._Altitude=alt;
    	this._Provider=pro;
    	this._Speed=spe;
    	this._Bearing=bea;
    	this._AudioFileName=afn;
    	this._ImageFileName=ifn;
    	this._ImageContent=imageContent;
    	this._Question0=q0;
    	this._Question1=q1;
    	this._Question2=q2;
    	this._Question3=q3;
    	this._Question4=q4;
    	this._Question5=q5;
    	this._Question6=q6;
    	this._Question7=q7;
    	this._Question8=q8;
    	this._Question9=q9;
    	this._Question10=q10;
    	this._Question11=q11;
    	this._Question12=q12;
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
    //setting image file name
    public void setImageFileName(String ifn){
    	this._ImageFileName=ifn;
    }
    //setting image content
    public void setImageContent(String ic){
    	this._ImageContent=ic;
    }
    //setting audio file name
    public void setAudioFileName(String afn){
    	this._AudioFileName=afn;
    }
    // setting location
    public void setGPSData(double lat, double lon, double alt, String pro, float spe, float bea){
    	this._Latitude=Double.toString(lat);
    	this._Longitude=Double.toString(lon);
    	this._Altitude=Double.toString(alt);
    	this._Provider=pro;
    	this._Speed=Float.toString(spe);
    	this._Bearing=Float.toString(bea);
    }
    
    public void setGPSData(String lat, String lon, String alt, String pro, String spe, String bea){
    	this._Latitude=lat;
    	this._Longitude=lon;
    	this._Altitude=alt;
    	this._Provider=pro;
    	this._Speed=spe;
    	this._Bearing=bea;
    }
    
    //Set Questions
    public void setQuestions(String q0,String q1,String q2,String q3,String q4,
    		String q5,String q6,String q7,String q8,String q9,String q10,String q11,String q12){
    	this._Question0=q0;
    	this._Question1=q1;
    	this._Question2=q2;
    	this._Question3=q3;
    	this._Question4=q4;
    	this._Question5=q5;
    	this._Question6=q6;
    	this._Question7=q7;
    	this._Question8=q8;
    	this._Question9=q9;
    	this._Question10=q10;
    	this._Question11=q11;
    	this._Question12=q12;
    }
    
    // getting user isManual
    public String getIsManual(){
        return this._isManual;
    }
    
    // getting user Date
    public String getDateAndTime(){
        return this._Date_Time;
    }
    
    // getting user Lat
    public String getLatitude(){
        return this._Latitude;
    }
    
    // getting long
    public String getLongitude(){
        return this._Longitude;
    }
    
    // getting alt
    public String getAltitude(){
        return this._Altitude;
    }
    
    // getting prov
    public String getProvider(){
        return this._Provider;
    }
    
    // getting speed
    public String getSpeed(){
        return this._Speed;
    }
    
    // getting bearing
    public String getBearing(){
        return this._Bearing;
    }
    
    // getting audio file name
    public String getAudioFileName(){
        return this._AudioFileName;
    }
    
    // getting image file name
    public String getImageFileName(){
        return this._ImageFileName;
    }
    
    //getting imageContent
    public String getImageContent(){
    	return this._ImageContent;
    }
    
    // getting question0 name
    public String getQuestion0(){
        return this._Question0;
    }
    
    // getting question1 name
    public String getQuestion1(){
        return this._Question1;
    }
    
    // getting question2 name
    public String getQuestion2(){
        return this._Question2;
    }
    
    // getting question3 name
    public String getQuestion3(){
        return this._Question3;
    }
    
    // getting question4 name
    public String getQuestion4(){
        return this._Question4;
    }
    
    // getting question5 name
    public String getQuestion5(){
        return this._Question5;
    }
    
    // getting question6 name
    public String getQuestion6(){
        return this._Question6;
    }
    
    // getting question7 name
    public String getQuestion7(){
        return this._Question7;
    }
    
    // getting question8 name
    public String getQuestion8(){
        return this._Question8;
    }
    
    // getting question9 name
    public String getQuestion9(){
        return this._Question9;
    }
    
    // getting question10 name
    public String getQuestion10(){
        return this._Question10;
    }
    
    // getting question11 name
    public String getQuestion11(){
        return this._Question11;
    }
    
    // getting question12 name
    public String getQuestion12(){
        return this._Question12;
    }
}

