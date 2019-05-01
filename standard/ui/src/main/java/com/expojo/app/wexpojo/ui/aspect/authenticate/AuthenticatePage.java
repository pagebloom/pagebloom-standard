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
package com.expojo.app.wexpojo.ui.aspect.authenticate;

import java.lang.*;
import com.expojo.app.wexpojo.ui.aspect.WebsitePage;


// -[KeepBeforeClass]-

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

// -[Class]-

/**
 * Class Name : AuthenticatePage
 * Diagram    : Authenticate
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Page that holds the log in form allowing users to authenticate with username (email)
 * and password.
 * 
 * @author Chris Colman
 */
public 
class AuthenticatePage extends WebsitePage
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * Standard authrorization: single logon form with email/password.
 */
public static final int acStandard = 0;



/**
 * Authentication configuration type.
 */
private int authConfig;


// -[Methods]-

/**
 * Pages returning true will not be redirected to log in page.
 * 
 * This should only ever be overridden by the page used for authenticating the user eg.,
 * a page with a log on form.
 */
public boolean isAuthenticatePage()
{
	return true;
}

/**
 * Sets authConfig
 */
public void setAuthConfig(int authConfig)
{
    this.authConfig = authConfig;
}

/**
 * Returns authConfig
 */
public int getAuthConfig()
{
    return authConfig;
}

/**
 * authConfig specifies the configuration of the auth panels.
 */
public AuthenticatePage(final PageParameters parameters, int iAuthConfig)
  throws StringValueConversionException
{
	super(parameters);

	authConfig = iAuthConfig;
}

/**
 * Describe here
 */
protected void onInitialize()
{
	super.onInitialize();

	if ( authConfig == acStandard )
		body.add(new LoginFormWrapperPanel("loginFormWrapper"));
	else
		throw new RuntimeException("Unsupported authConfig value: " + authConfig);
}

/**
 * 
 */
public AuthenticatePage(final PageParameters parameters)
  throws StringValueConversionException
{
	this(parameters, acStandard);
}

/**
 * 
 */
public AuthenticatePage()
  throws StringValueConversionException
{
	this(null, acStandard);
}

}


