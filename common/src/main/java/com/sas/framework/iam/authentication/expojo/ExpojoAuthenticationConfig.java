// -[KeepHeading]-


// -[Copyright]-

/**
 * (c) 2006, 2014 Step Ahead Software Pty Ltd. All rights reserved.
 * 
 * Source file created and managed by Javelin (TM) Step Ahead Software.
 * To maintain code and model synchronization you may directly edit code in method bodies
 * and any sections starting with the 'Keep_*' marker. Make all other changes via Javelin.
 * See http://stepaheadsoftware.com for more details.
 */
package com.sas.framework.iam.authentication.expojo;

import java.lang.*;
import com.sas.framework.iam.authentication.expojo.AbstractExpojoAuthenticationConfig;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
    
import com.sas.framework.iam.user.IUser;
    
import com.sas.framework.iam.authentication.IAuthenticator;
    
import com.sas.framework.iam.realm.IRealm;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;


// -[KeepBeforeClass]-
import java.util.*;

import com.sas.framework.iam.authentication.AuthenticationException;

// -[Class]-

/**
 * Class Name : ExpojoAuthenticationConfig
 * Diagram    : Authentication Impl
 * Project    : Entity Model Framework
 * Type       : concrete
 * A simple concrete implementation of ExpojoAuthenticationConfig.
 * 
 * @author Chris Colman
 */
public 
class ExpojoAuthenticationConfig extends AbstractExpojoAuthenticationConfig
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * Collection of authenticators.
 */
private List<IAuthenticator> authenticators = new ArrayList<IAuthenticator>();



/**
 * NoDesc
 */
private static ExpojoAuthenticationConfig self;
    
    protected IAuxiliaryChallenge auxiliaryChallenge;


// -[Methods]-

/**
 * Clears the list of authenticators.
 */
public void clearAuthenticators()
{
	authenticators.clear();
}




/**
 * Returns the auxiliary challenge if there is one or null if no auxiliary challenge
 * is available to be presented to the user.
 */
public IAuxiliaryChallenge getAuxiliaryChallenge()
{
	return auxiliaryChallenge;
}

/**
 * Describe here
 */
public void setAuxiliaryChallenge(IAuxiliaryChallenge iAuxiliaryChallenge)
{
	auxiliaryChallenge = iAuxiliaryChallenge;
}




/**
 * While an IAuxiliaryChallenge may be associated with this IAuthenticationService it
 * may not be required in every circumstance (e.g. business rules may be configured to
 * only require an auxiliary authentication challenge in certain cases: e.g. user has
 * logged in a device that they have never logged in from before i.e. a siteVisitorId
 * not previously encountered for this user).
 * This method will only return non null if the user is required to perform a challenge
 * for an auxiliary factor of authentication (e.g. enter a security code sent via email
 * or an SMS).
 */
public IAuxiliaryChallenge userRequiresAuxiliaryChallenge(IUser user, String deviceId, IRealm realm)
{
	return auxiliaryChallenge;
}



/**
 * Adds a new authenticator to the manager.
 */
public void addAuthenticator(IAuthenticator iAuthenticator)

{
	authenticators.add(iAuthenticator);
}

/**
 * Calls authenticateCredentials on each of the  IAuthenticators in the authenticators
 * list until one passes authentication or the list is exhausted. 
 */
public IUser authenticateCredentials(String username, String password, boolean digested, IRealm realm)
{
	IUser foundUser = null;
	
	if (authenticators.size() == 0)
		throw new AuthenticationException("Authentication failure: ExpojoAuthenticationConfig contains no authenticators - please add at least one authenticator during application initialization");
		
	for (IAuthenticator authenticator: authenticators)
	{
		IUser user = authenticator.authenticateCredentials(username, password, digested, realm);
		
		if ( user != null )
		{
			foundUser = user;
			break;
		}
	}

	if ( foundUser != null )
		foundUser.registerAuthentication();

	return foundUser;
}




/**
 * Allows basic addition of multiple IAuthenticatorS and an option auxiliary challenge.
 */
public static ExpojoAuthenticationConfig get()
{
	if ( self == null )
		new ExpojoAuthenticationConfig();
	return self;
}




/**
 * Constructs the object
 */
protected ExpojoAuthenticationConfig()
{
	self = this;
}

}


