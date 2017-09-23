package org.zagelnews.constants;


public interface ServerConstants {

	//API Server
	public String SERVER_URL = "http://AWS_SERVER_NAME:8080";
	
	//Web Server
	public String WEB_URL = "http://AWS_SERVER_NAME/";	
	
	
	//S3
	String S3_URL = "https://s3-us-west-2.amazonaws.com/";
	// Set the default socket timeout (SO_TIMEOUT) 
	// in milliseconds which is the timeout for waiting for data.
	int TIMEOUT_SOCKET = 60000;
	// Set the timeout in milliseconds until a connection is established.
	// The default value is zero, that means the timeout is not used.
	int TIMEOUT_CONNECTION = 40000;
}
