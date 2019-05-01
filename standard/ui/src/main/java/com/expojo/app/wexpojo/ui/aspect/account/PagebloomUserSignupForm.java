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
import com.expojo.app.wexpojo.ui.aspect.account.UserSignupForm;


// -[KeepBeforeClass]-
import com.sas.platform.pagebloom.common.auth.Credentials;
import org.apache.wicket.markup.html.form.TextField;


// -[Class]-

/**
 * Class Name : PagebloomUserSignupForm
 * Diagram    : Account
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Describe here
 * 
 * @author Chris Colman
 */
public 
class PagebloomUserSignupForm extends UserSignupForm
{
// -[KeepWithinClass]-


// -[Fields]-


// -[Methods]-

/**
 * Describe here
 */
public void onInitialize()
{
	super.onInitialize();

	// Additional fields
	TextField<String> firstNameField = new TextField<>("firstName");
	firstNameField.setRequired(true);
	personalDetailsContainer.add(firstNameField);

	TextField<String> lastNameField = new TextField<>("lastName");
	lastNameField.setRequired(true);
	personalDetailsContainer.add(lastNameField);
}




/**
 * Constructs the object
 */
public PagebloomUserSignupForm(String id, Credentials credentials)
{
	super(id, credentials, false);
}

}


