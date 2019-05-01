// -[KeepHeading]-


// -[Copyright]-

/**
 * (c) 2007, 2019. Step Ahead Software Pty Ltd.
 * All rights reserved.
 * Distribution of this software in source or binary format is forbidden without an explicit
 * license from Step Ahead Software Pty Ltd
 * 
 * Source file created and managed by Javelin (TM) Step Ahead Software.
 * To maintain code and model synchronization you may directly edit code in method bodies
 * and any sections starting with the 'Keep_*' marker. Make all other changes via Javelin.
 * See http://stepaheadsoftware.com for more details.
 */
package com.sas.framework.system;

import java.lang.*;


// -[KeepBeforeClass]-


// -[Class]-

/**
 * Class Name : Environment
 * Diagram    : Environment
 * Project    : pagebloom standard biz
 * Type       : concrete
 * Used to hold configuration specific to a particular environment instance.
 * Configuration of an Environment may take place via a properties files, context.xml
 * etc.,
 * 
 * @author 
 */
public 
class Environment
{
// -[KeepWithinClass]-
public enum InstanceType
{
	Production,
	Uat,
	Integration,
	Development
}



// -[Fields]-



/**
 * NoDesc
 */
private static Environment self;



/**
 * Set to true if the app is running in a 'demo' mode. Security, authentication is a
 * lot loser as it is not a *real* system.
 */
private boolean demoMode = false;



/**
 * Instance type of this environment.
 */
private InstanceType instanceType;



/**
 * Hostname of app.
 */
private String appHost;



/**
 * An optional subdomain for the host domain name can be supplied.
 * eg., 
 * host is pagebloom.com
 * subdomain is sports
 * 
 * So full hostname is sports.pagebloom.com
 * 
 */
private String appSubdomain;



/**
 * Port number of app.
 */
private String appPort;



/**
 * True if secured by SSL.
 */
private boolean appSecuredBySsl = false;


// -[Methods]-

/**
 * Sets appSubdomain
 */
public void setAppSubdomain(String appSubdomain)
{
    this.appSubdomain = appSubdomain;
}

/**
 * Returns appSubdomain
 */
public String getAppSubdomain()
{
    return appSubdomain;
}




/**
 * Returns a string like:
 * 
 * https://www.mydomain.com:8080
 * 
 * (no port will be appended if the port is 80)
 */
public String getBaseUrl(String hostname)
{
	String protocol = appSecuredBySsl ? "https://" : "http://";
	String protocolAndHostname = protocol + hostname;
	if (appPort != null)
		protocolAndHostname += ":" + appPort;

	return protocolAndHostname;
}

/**
 * Sets appSecuredBySsl
 */
public void setAppSecuredBySsl(boolean appSecuredBySsl)
{
    this.appSecuredBySsl = appSecuredBySsl;
}

/**
 * Returns appSecuredBySsl
 */
public boolean getAppSecuredBySsl()
{
    return appSecuredBySsl;
}

/**
 * Sets appPort
 */
public void setAppPort(String appPort)
{
    this.appPort = appPort;
}

/**
 * Returns appPort
 */
public String getAppPort()
{
    return appPort;
}

/**
 * Sets appHost
 */
public void setAppHost(String appHost)
{
    this.appHost = appHost;
}

/**
 * Returns appHost
 */
public String getAppHost()
{
    return appHost;
}




/**
 * Returns a string like:
 * 
 * https://www.mydomain.com
 */
public String getBaseUrl()
{
	return getBaseUrl(appHost);
}

/**
 * Returns '' for prod. 'dev' for dev etc.,
 */
public String getHostnamePrefix()
{
	String subdomainPrefix = "";
	
	switch ( getInstanceType() )
	{
		case Production: subdomainPrefix = ""; break;
		// End users' only accessible test system so probably makes sense to default to 'test'		
		case Uat: subdomainPrefix = "test."; break;	
		case Integration: subdomainPrefix = "int."; break;
		case Development: subdomainPrefix = "dev."; break;
		default: throw new RuntimeException("Unknown environment instance type: " + getInstanceType().toString());
	}
	
	return subdomainPrefix;
}

/**
 * Sets instanceType
 */
public void setInstanceType(InstanceType instanceType)
{
    this.instanceType = instanceType;
}

/**
 * Returns instanceType
 */
public InstanceType getInstanceType()
{
    return instanceType;
}

/**
 * Sets demoMode
 */
public void setDemoMode(boolean demoMode)
{
    this.demoMode = demoMode;
}

/**
 * Returns demoMode
 */
public boolean getDemoMode()
{
    return demoMode;
}

/**
 * Constructs the object
 */
public Environment()
{
	if (self != null)
		throw new RuntimeException("Environment has already been established but attempting to create a new instance");

	self = this;
}

/**
 * Returns self
 */
public static Environment get()
{
	return self;
}

}


