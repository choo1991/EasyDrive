package com.example.drivingapp;

public class DataStorageClass {
	private long _time;
    private float _x;
    private float _y;
    private float _z;
    private float _speed;
    
    public DataStorageClass() {

	}
    
    public DataStorageClass(long time, float x, float y, float z, float speed) {
        this._time = time;
        this._x = x;
        this._y = y;
        this._z = z;
        this._speed = speed;
    }
    
    public long returnTime() {
    	return _time;
    }
    
    public void setTime(long time) {
    	this._time = time;
    }
    
    public float returnX() {
    	return _x;
    }
    
    public void setX(float x) {
    	this._x = x;
    }
    
    public float returnY() {
    	return _y;
    }
    
    public void setY(float y) {
    	this._y = y;
    }
    
    public float returnZ() {
    	return _z;
    }
    
    public void setZ(float z) {
    	this._z = z;
    }
    
    public float returnSpeed() {
    	return _speed;
    }
    
    public void setSpeed(float speed) {
    	this._speed = speed;
    }
}
