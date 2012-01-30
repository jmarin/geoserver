package org.geoserver.rest.tutorials.hello;

public class Hello {

    public Hello(String message){
        this.message = message;
    }
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
