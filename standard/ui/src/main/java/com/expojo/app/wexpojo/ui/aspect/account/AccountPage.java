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
package com.expojo.app.wexpojo.ui.aspect.account;

import java.lang.*;
import com.expojo.app.wexpojo.ui.aspect.WebsitePage;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// -[KeepBeforeClass]-

import com.expojo.app.wexpojo.ui.aspect.error.ErrorPage;
import com.expojo.app.wexpojo.ui.session.IWexpojoSession;
import com.sas.app.wexpojo.biz.website.IWebsite;
import com.sas.framework.iam.user.IUser;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

// -[Class]-

/**
 * Class Name : AccountPage
 * Diagram    : Account
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Page including account related processes - eg., registration, log on etc.,
 * 
 * @author Chris Colman
 */
public 
class AccountPage extends WebsitePage
{
// -[KeepWithinClass]-
/**
 * Standard authentication: single logon form with email/password.
 */
public static final int acStandard = 0;



// -[Fields]-

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LoggerFactory.getLogger(AccountPage.class);


// -[Methods]-

/**
 * Describe here
 */
protected void onInitialize()
{
	super.onInitialize();

	IWebsite website = getWebsite();

	// Register requires a valid IUser
	IWexpojoSession wexpojoSession = (IWexpojoSession) WebSession.get();
	IUser iUser = wexpojoSession.getUser();

	if (iUser != null)
	{
		String errMsg = "A user is currently logged in - to register a new user please log out first and try again";
		error(errMsg);
		logger.error(errMsg);
		throw new RestartResponseException(new PageProvider(ErrorPage.class), RenderPageRequestHandler.RedirectPolicy.NEVER_REDIRECT);
	}
	else
		body.add(new PagebloomRegisterPanel("contentPanel"));
}



/**
 * Constructs the object
 */
public AccountPage()
{
	this(null);
}

/**
 * Constructs the object
 */
public AccountPage(PageParameters parameters)
{
	super(parameters);
}

}


