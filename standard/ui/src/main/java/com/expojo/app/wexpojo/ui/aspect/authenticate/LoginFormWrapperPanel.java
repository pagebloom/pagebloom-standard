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
import com.expojo.app.wexpojo.ui.aspect.WebsitePanel;


// -[KeepBeforeClass]-


// -[Class]-

/**
 * Class Name : LoginFormWrapperPanel
 * Diagram    : Authenticate
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Panel located inside AuthenticatePage that wraps the LoginFormPanel. We wrap the LoginFormPanel
 * to allow it to appear as a 'sub module' of the page. The LoginFormWrapperPanel can
 * contain extra site specific text in it that is independent of the LoginFormPanel which
 * can remain simple with only the fields required for authentication.
 * 
 * @author Chris Colman
 */
public 
class LoginFormWrapperPanel extends WebsitePanel
{
// -[KeepWithinClass]-


// -[Fields]-


// -[Methods]-

/**
 * Creates the LoginFormPanel and adds it. Overridden in derived classes to create their
 * own custom LoginFormPanel(s) derivative.
 */
public void addAuthenticationPanels()
{
	add(new LoginFormPanel("loginForm"));
}

/**
 * Adds components
 */
protected void onInitialize()
{
	super.onInitialize();

	// Explicitly add this - forms included in wicket panels that are added by 
	// a component resolver do not work properly - submit fails with 'cannot find component'
	// errors
	addAuthenticationPanels();
}

/**
 * Constructs the object
 */
public LoginFormWrapperPanel(String id)
{
	super(id);
}

}


