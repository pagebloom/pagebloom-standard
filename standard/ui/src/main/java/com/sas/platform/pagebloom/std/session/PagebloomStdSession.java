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
package com.sas.platform.pagebloom.std.session;

import java.lang.*;
import com.expojo.app.wexpojo.ui.session.IWexpojoSession;
import org.apache.wicket.protocol.http.WebSession;
import com.sas.framework.iam.user.AbstractUserSession;
    
import com.sas.framework.iam.user.IUser;
    
import com.sas.framework.iam.authentication.expojo.ExpojoAuthenticationService;
    
import com.sas.app.wexpojo.ui.session.IWorkflowOrchestrator;

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

// -[KeepBeforeClass]-
import java.util.*;

import javax.servlet.http.*;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.INamedParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.sas.framework.expojo.Ex;
import com.sas.framework.iam.realm.IRealm;
import com.sas.framework.iam.authentication.IAuthenticationService;
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
import com.sas.framework.entity.IEntity;

import com.expojo.app.wexpojo.ui.aspect.WebsitePage;





// -[Class]-

/**
 * Class Name : PagebloomStdSession
 * Diagram    : Session
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * The session used by pagebloom standard.
 * 
 * @author Chris Colman
 */
public 
class PagebloomStdSession extends WebSession implements IWexpojoSession, AbstractUserSession
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * Set to true if authentication service mandated an auxiliary challenge.
 * Set to false if/when that challenge becomes satisfied.
 */
private boolean unsatisfiedAuxiliaryChallenge = false;
    
    protected IUser user;
    
    protected IWorkflowOrchestrator workflowOrchestrator;

// [Added by Code Injection Wizard: Log4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LogManager.getLogger(PagebloomStdSession.class);


// -[Methods]-

/**
 * Detaches the session.
 */
public void detach()
{
	// Only detach session objects if a ModelExposer is present. This should prevent detaches on static resources causing
	// session-related issues.
	//
	// NOTE: detach can be called due to a session timeout in which can there will be no ModelExposer attached
	// to the thread.
	// We could wrap these detaches in an Model exposer wrapper - hmmm?

	if (Ex.get() != null)
	{
		if (workflowOrchestrator != null)
			workflowOrchestrator.detach();
	}
		
	super.detach();
}




/**
 * Describe here
 */
public IWorkflowOrchestrator getWorkflowOrchestrator()
{
	return workflowOrchestrator;
}

/**
 * Describe here
 */
public void setWorkflowOrchestrator(IWorkflowOrchestrator iWorkflowOrchestrator)
{
	workflowOrchestrator = iWorkflowOrchestrator;
}

/**
 * Allows a general app wide processing of certain generic page parameters if required.
 * Any page specific parameters will likely not be handled by this method.
 */
public void processGenericPageParameters(WebPage webPage, PageParameters pageParameters)
{
	if (unsatisfiedAuxiliaryChallenge)
	{
		IRealm realm = ((WebsitePage)webPage).getWebsite().getRealm();
		IUser user = getUser();

		IAuxiliaryChallenge auxiliaryChallenge = ExpojoAuthenticationService.get().getAuxiliaryChallengeForRealm(realm);
		
		if (auxiliaryChallenge != null)
		{
			// Convert the Wicket page parameters to a generic Map because
			// IAM's IAuxiliaryChallenge can not have any UI dependencies
			Map<String, String> parameterMap = new HashMap<>();

			for(INamedParameters.NamedPair namedPair: pageParameters.getAllNamed())
				parameterMap.put(namedPair.getKey(), namedPair.getValue());

			if (auxiliaryChallenge.processGenericPageParameters(user, realm, parameterMap))
				setUnsatisfiedAuxiliaryChallenge(false);
		}
	}
}

/**
 * Sets unsatisfiedAuxiliaryChallenge
 */
public void setUnsatisfiedAuxiliaryChallenge(boolean unsatisfiedAuxiliaryChallenge)
{
    this.unsatisfiedAuxiliaryChallenge = unsatisfiedAuxiliaryChallenge;
}




/**
 * Returns unsatisfiedAuxiliaryChallenge
 */
public boolean hasUnsatisfiedAuxiliaryChallenge()
{
    return unsatisfiedAuxiliaryChallenge;
}



/**
 * Sets the entity.
 */
public void setEntity(IEntity entity) {
}

/**
 * Returns the entity.
 */
public IEntity getEntity()
{
	return null;
}

/**
 * Attempts to authenticate with the given username and password.
 * Returns an implementation of IUser if successful or null if not.
 * realm is an optional parameter that may be null. If not null then the authentication
 * process is widened to consider a match for username and password in any org specific
 * roles within the realm.
 * Note: the realm parameter is not used for pagebloom standard. It is currently a pagebloom
 * enterprise feature only.
 */
public IUser authenticate(String username, String password, boolean enableAutoSignIn, IRealm realm)
{
	IAuthenticationService authenticationService = ExpojoAuthenticationService.get();
	
	IUser user = authenticationService.authenticateCredentials(username, password, enableAutoSignIn, realm);
	
	if (user != null)
	{
		logger.info("Authentication success for user: " + user);
	
		setUser(user);
	}
	else
		logger.info("Authentication failed for user: " + user);

	return user;
}

/**
 * Returns a unique identifier for the visitor visiting this website.
 * This value is typically stored as a cookie in the user's browser.
 * A single user may access the website from multiple browsers across a range of different
 * PCs, Laptops and handheld devices. There will be a separate, unique site visitor Id
 * for each of these browsers.
 */
public String getSiteVisitorId()
{
	// #BUG This is wrong - this will return the session ID but as it can be shared around various
	// apps this is a security risk - instead a unique ID representing the user's site visitor ID
	// from their current browser (cookie stored) should be returned instead.
	return getId();
}




/**
 * Describe here
 */
public static PagebloomStdSession get()
{
	return (PagebloomStdSession)WebSession.get();
}

/**
 * Describe here
 */
public void setUser(IUser iUser)
{
	user = iUser;
}

/**
 * Constructs the object
 */
public PagebloomStdSession(Request request, Response response)
{
	super(request);

	HttpServletRequest httpServletRequest = ((ServletWebRequest)request).getContainerRequest();
	
	// We must call getSession() to establish a session instance if none is already created even
	// if we don't go on to use the session immediately
	HttpSession session = httpServletRequest.getSession();
}

/**
 * Returns the authenticated user associated with this session.
 * 
 * This will return null unless all required authentication factors have been processed
 * and succeeded.
 * 
 * NOTE: it is possible that an implementation may have a reference to the IUser but
 * should NOT return it due to not all authentication factor processes being completed
 * successfully.
 */
public IUser getUser()
{
	return user;
}

}


