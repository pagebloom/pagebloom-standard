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


// -[KeepBeforeClass]-

import com.expojo.app.wexpojo.ui.session.IWexpojoSession;
import com.sas.app.wexpojo.biz.website.IWebsite;
import com.sas.framework.iam.user.IUser;
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
		error("A user is currently logged in - to register a new user please log out first and try again");
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


