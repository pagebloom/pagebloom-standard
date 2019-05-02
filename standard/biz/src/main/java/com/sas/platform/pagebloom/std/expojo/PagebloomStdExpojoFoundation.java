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
package com.sas.platform.pagebloom.std.expojo;

import java.lang.*;
import com.sas.framework.expojo.servlet.ExpojoFoundation;
    
import com.sas.framework.expojo.jdo.datanucleus.NucleusJdoExpojoContextFactory;
    
import javax.servlet.ServletContextEvent;
    
import com.sas.framework.expojo.ExpojoContextFactory;
    
import com.sas.framework.system.IModulesProvider;
    
import com.sas.platform.pagebloom.std.iam.module.PagebloomStdIdentityAccessModule;
    
import com.sas.app.wexpojo.biz.website.IWebsiteRepository;
    
import com.sas.framework.expojo.Ex;
    
import com.sas.app.wexpojo.biz.website.Website;

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


// -[KeepBeforeClass]-
import com.sas.framework.iam.module.jdo.IdentityAccessModuleJdo;
import javax.servlet.ServletContext;

import com.sas.framework.system.Environment;

import com.sas.app.wexpojo.biz.website.IWebsiteBehaviour;


// -[Class]-

/**
 * Class Name : PagebloomStdExpojoFoundation
 * Diagram    : Expojo Foundation and Context Factory
 * Project    : pagebloom standard biz
 * Type       : concrete
 * Foundation of the pagebloom standard application.
 * 
 * @author Chris Colman
 */
public 
class PagebloomStdExpojoFoundation extends ExpojoFoundation
{
// -[KeepWithinClass]-


// -[Fields]-

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LogManager.getLogger(PagebloomStdExpojoFoundation.class);


// -[Methods]-

/**
 * Constructs a new environment object.
 * Override this method in derived classes to use an enhanced Environment class tailored
 * for the specific needs of the application.
 */
public Environment newEnvironment()
{
	return new Environment();
}

/**
 * Called during app/context initialization.
 * For example this method will typically be overridden to perform the reading of parameters
 * configured in context.xml or some other config file.
 * It can be assumed that the persistence framework has not yet been initialized by the
 * time this method is called and therefore initialization of objects that can determine
 * the persistence framework initialization behaviour can occur in this method.
 */
protected void readConfigParameters(ServletContext servletContext)
{
	Environment environment = Environment.get();

	environment.setContextPath(servletContext.getContextPath());

	Environment.InstanceType instanceType;

	String instanceTypeStr = servletContext.getInitParameter("instance.type");
	if ( instanceTypeStr == null )
	{
		// Default to productionInstance if no parameter is supplied
		instanceType = Environment.InstanceType.Production;
	}
	else
	{
		// Value only has to start with the first 3 letters of the name and is case insensitive
		instanceTypeStr = instanceTypeStr.toLowerCase();
		
		if ( instanceTypeStr.startsWith("uat") )
			instanceType = Environment.InstanceType.Uat;
		else if ( instanceTypeStr.startsWith("dev") )
			instanceType = Environment.InstanceType.Development;
		else if ( instanceTypeStr.startsWith("int") )
			instanceType = Environment.InstanceType.Integration;
		else
			instanceType = Environment.InstanceType.Production;
	}
		
	environment.setInstanceType(instanceType);
	
	// Demo mode
	String demoModeStr = servletContext.getInitParameter("demoMode");
	boolean demoMode = demoModeStr != null ? demoModeStr.equals("true") : false;
	
	environment.setDemoMode(demoMode);
	
	// Host/Port/Protocol details
	
	// retrieve app server parameters
	// #IDEA app.host could be derived from a the hostname of 'default' IWebsite instance
	String appHost = servletContext.getInitParameter("app.host");
	if ( appHost == null )
	{
		throw new RuntimeException("Mandatory 'app.host' configuration parameter has not been set");
	}

	environment.setAppHost(appHost);

	String appSubdomain = servletContext.getInitParameter("app.subdomain");
	if ( appSubdomain != null )
	{
		environment.setAppSubdomain(appSubdomain);
	}


	// if empty then port 80 is assumed
	String appPort = servletContext.getInitParameter("app.port");

	if (appPort != null)
		environment.setAppPort(appPort);
	
	// appSecuredBySsl true indicates that the app is secured by SSL independently or via an upstream
	// dependency/load balancer etc.,
	boolean appSecuredBySsl = false;

	String appSecuredBySslStr = servletContext.getInitParameter("app.ssl");
	if (appSecuredBySslStr != null)
	{
		if ( appSecuredBySslStr.equals("true") )
			appSecuredBySsl = true;
	}

	environment.setAppSecuredBySsl(appSecuredBySsl);
	
	logger.info("Using parameter 'instance.type' = '" + environment.getInstanceType() + "'");
	logger.info("Using parameter 'demoMode' = '" + demoMode + "'");
	logger.info("Using parameter 'app.host' = '" + appHost + "'");
	logger.info("Using parameter 'app.port' = '" + (appPort != null ? appPort : "") + "'");
	logger.info("Using parameter 'app.ssl' = '" + appSecuredBySsl + "'");
}

/**
 * Describe here
 */
public void addWebsite(IWebsiteRepository websiteRepository, int id, String name, String subdomain, String domainName, int templateId, String theme, IWebsiteBehaviour behaviour)
{
	String hostnamePrefix = Environment.get().getHostnamePrefix();
	
	Website website = new Website(id, name, subdomain + "." + hostnamePrefix + domainName, behaviour);
	website.setTemplateId(templateId);
	website.setThemeId(theme);
	
	websiteRepository.addWebsite(website);
}

/**
 * Override to add websites to the repository.
 */
public void addWebsites(IWebsiteRepository websiteRepository)
{
	// You need to ensure that any domain names are appropriately mapped in either your local config for development (e.g. hosts files) or
	// external DNS servers (your domain name registry's DNS settings or your own DNS server if you host your own)
	websiteRepository.addWebsite(new Website(1, "WexPOJO Demo website", "localhost"));
}

/**
 * Initializatoin of the model.
 * Prerequisite: data model already exists.
 * Use: this method can be used to perform data migration etc., if required.
 */
public void initDataModel()
{
	super.initDataModel();

	IWebsiteRepository websiteRepository = Ex.c(IWebsiteRepository.class);
	
	addWebsites(websiteRepository);
}

/**
 * Adds the IModule required for the application.
 */
public void addModules(IModulesProvider modulesProvider)
{
	modulesProvider.addModule(new PagebloomStdIdentityAccessModule());
}

/**
 * Returns true if the datastore already has the genesis objects (i.e. if it is not a
 * new, blank system).
 * If this method returns false then establishGenesisObjects will be called to establish
 * genesis objects for a new blank system.
 * NOTE: This method is called each time the servlet context starts up so it is important
 * to check if a well known 'key' genesis object already exists (via a query method in
 * an appropriate repository) and only return false if this key object can not be found.
 */
public boolean hasGenesisObjects()
{
	return true;
}

/**
 * Performs the parts of the app/context initialization that must occur before the persistence
 * framework has been initialized.
 * For example this method will typically be overridden to perform the reading of parameters
 * configured in context.xml or some other config file.
 * It can be assumed that the persistence framework has not yet been initialized by the
 * time this method is called and therefore initialization of objects that can determine
 * the persistence framework initialization behaviour can occur in this method.
 */
protected void initContextPrePersistenceInit(final ServletContextEvent event)
{
	// This should only ever initialize expojo related things, not application related things because the ordering of 
	// context initialization via the listener mechanism seems to be random in Tomcat (observed in 7.x but may affect other 
	// versions also) but is very predictable (order of initialization in web.xml) in Jetty.
	// In other words the application can no depend on anything being initialized here first
	
	// Establish the new environment object
	newEnvironment();
	
	readConfigParameters(event.getServletContext());	
}

/**
 * Overridden in a derived class to create the ExpojoContextFactory.
 */
public ExpojoContextFactory createExpojoContextFactory(ServletContextEvent event)
{
	ExpojoContextFactory expojoContextFactory = new NucleusJdoExpojoContextFactory();

	addModules(expojoContextFactory.getModulesProvider());

	return expojoContextFactory;
}

}


