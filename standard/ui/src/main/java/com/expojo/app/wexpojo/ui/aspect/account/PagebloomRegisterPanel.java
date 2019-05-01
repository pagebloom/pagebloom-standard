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
import com.expojo.app.wexpojo.ui.aspect.account.RegisterPanel;


// -[KeepBeforeClass]-

import com.expojo.app.wexpojo.ui.session.IWexpojoSession;
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
import com.sas.framework.iam.authentication.expojo.ExpojoAuthenticationService;
import com.sas.framework.iam.realm.IRealm;
import com.sas.framework.iam.user.AbstractUserSession;
import org.apache.wicket.protocol.http.WebSession;

import com.sas.framework.expojo.ExpojoContext;
import com.sas.framework.iam.user.IUser;
import com.sas.framework.iam.user.expojo.UserRepository;
import com.sas.framework.iam.user.expojo.UserService;
import com.sas.framework.iam.user.impl.User;
import com.sas.framework.system.Environment;

import com.sas.platform.pagebloom.common.auth.PagebloomRegisterCredentials;
import com.sas.platform.pagebloom.common.auth.RegisterCredentials;


// -[Class]-

/**
 * Class Name : PagebloomRegisterPanel
 * Diagram    : Account
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Describe here
 * 
 * @author Chris Colman
 */
public 
class PagebloomRegisterPanel extends RegisterPanel
{
// -[KeepWithinClass]-


// -[Fields]-


// -[Methods]-



/**
 * Creates the signup form for this registration.
 */
public UserSignupForm createSignupForm()
{
	return new PagebloomUserSignupForm("userSignupForm", registerCredentials);
}

/**
 * Registers the user based on the credentia;s they provided.
 */
public void registerUser()
{
	PagebloomRegisterCredentials pbCred = (PagebloomRegisterCredentials)registerCredentials;

	try
	{
		IWexpojoSession wexpojoSession = (IWexpojoSession)WebSession.get();

		// If auxiliary challenge is supported by the realm
		// then set the flag in the session to indicate
		// that we are waiting for the challenge to be completed.
		// Restricted pages should not allow access if the auxiliary challenge (or
		// appropriate configuration of that challenge eg., provide mobile number for
		// SMS authentication code) has not been successfully completed by the user

		IAuxiliaryChallenge auxiliaryChallenge = ExpojoAuthenticationService.get().getAuxiliaryChallengeForRealm(getWebsite().getRealm());
		if (auxiliaryChallenge != null)
		{
				wexpojoSession.setUnsatisfiedAuxiliaryChallenge(true);
		}

		AbstractUserSession userSession = (AbstractUserSession)wexpojoSession;

		// Check if new username is unique
		IUser iUser = UserRepository.get().findUserByUsername(pbCred.getEmail());

		if (iUser != null)
		{
			if (Environment.get().getDemoMode())
			{
				// just update password on existing user
				User user = (User)iUser;

				user.setPassword(pbCred.getPassword());

				ExpojoContext.pp().flush();


				userSession.setUser(user);
				userSession.setEntity(user.getEntity());
			}
		}
		else
			UserService.get().registerUser(getWebsite().getRealm(), wexpojoSession.getSiteVisitorId(), userSession,
				pbCred.getEmail(), pbCred.getPassword(), pbCred.getFirstName(), pbCred.getLastName());
	}
	catch(Exception e)
	{
		error(e.getMessage());
	}
}


/**
 * Creates the credentials model object. Overridden by derived classes to create extended
 * register credentials.
 */
public RegisterCredentials createCredentialsModel()
{
	return new PagebloomRegisterCredentials();
}


/**
 * Describe here
 */
public void onInitialize()
{
	super.onInitialize();
}


/**
 * Constructs the object
 */
public PagebloomRegisterPanel(String id)
{
	super(id);
}

}


