// -[KeepHeading]-


// -[Copyright]-

/**
 * (c) 2008,2019. Step Ahead Software Pty Ltd. All rights reserved.
 * Usage is governed by the terms of the Apache 2 License.
 * 
 * Source file created and managed by Javelin (TM) Step Ahead Software.
 * To maintain code and model synchronization you may directly edit code in method bodies
 * and any sections starting with the 'Keep_*' marker. Make all other changes via Javelin.
 * See http://stepaheadsoftware.com for more details.
 */
package com.sas.platform.pagebloom.std.app;

import java.lang.*;
import com.expojo.app.wexpojo.WexpojoApplication;
    
import com.sas.app.wexpojo.biz.website.IWebsiteRepository;
    
import com.sas.app.wexpojo.biz.website.IWebsite;
    
import com.expojo.app.wexpojo.ui.session.IWexpojoSession;
    
import com.sas.platform.pagebloom.std.session.PagebloomStdSession;
    
import com.sas.framework.expojo.Ex;

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
    

// -[KeepBeforeClass]-
import javax.servlet.ServletContext;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import com.sas.framework.system.Environment;
import com.sas.framework.iam.authentication.expojo.ExpojoAuthenticationConfig;

import com.expojo.app.wexpojo.ui.aspect.account.AccountPage;
import com.expojo.app.wexpojo.ui.aspect.landing.HomePage;
import com.expojo.app.wexpojo.ui.aspect.dashboard.DashboardPage;
import com.expojo.app.wexpojo.ui.aspect.error.ErrorPage;
import com.expojo.app.wexpojo.ui.aspect.authenticate.AuthenticatePage;


// -[Class]-

/**
 * Class Name : PagebloomStdApplication
 * Diagram    : App Core
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * The Wicket application object used by pagebloom standard edition.
 * 
 * @author Chris Colman
 */
public 
class PagebloomStdApplication extends WexpojoApplication
{
// -[KeepWithinClass]-


// -[Fields]-

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LogManager.getLogger(PagebloomStdApplication.class);


// -[Methods]-

/**
 * Returns the base URL of the given website. e.g.
 * 
 * protocol://webistehostname:port
 * 
 * eg.,
 * https://www.mysite.com:8080
 * 
 * The protocol and port will typically be provided some some environmental parameters
 * (e.g. Tomcat's context.xml file)
 */
public String getBaseUrlOfWebsite(IWebsite website)
{
	Environment env = Environment.get();
	
	String protocol = env.getAppSecuredBySsl() ? "https://" : "http://";
	String baseUrl = protocol + website.getHostName();
	if (env.getAppPort() != null)
		baseUrl += ":" + env.getAppPort();

	return baseUrl;
	
}

/**
 * Returns ErrorPage.class
 */
public Class<? extends WebPage> getErrorPage()
{
	return ErrorPage.class;
}



/**
 * Returns the home page for this application.
 */
public Class<? extends WebPage> getHomePage()
 {
	return HomePage.class;
}




/**
 * Creates and returns an instance of the application's authenticate (log in) page for
 * this application.
 * Any application is free to use whatever class for the authenticate page so long as
 * it extends WebPage.
 */
public WebPage createAuthenticatePage(PageParameters pageParameters)
{
	return new AuthenticatePage(pageParameters, AuthenticatePage.acStandard);
}

/**
 * Initializes the app post construction of the application object.
 */
public void init()
{
	super.init();

	// Configure security
	ExpojoAuthenticationConfig eac = ExpojoAuthenticationConfig.get();

	// Mount pagess
	mountPage("/authenticate", AuthenticatePage.class);
	mountPage("/dashboard", DashboardPage.class);
	mountPage("/account", AccountPage.class);
}




/**
 * Creates a new WebSession (or extended) object that implements IWexpojoSession.
 * This is called by newSession and is made abstract to enforce derived classes to create
 * a WebSession that is an implementation of IWexpojoSession.
 */
protected IWexpojoSession newWexpojoSession(Request request, Response response)
{
	return new PagebloomStdSession(request, response);
}

/**
 * Creates the website repository.
 */
public IWebsiteRepository getWebsiteRepository()
{
	return Ex.c(IWebsiteRepository.class);
}

}
