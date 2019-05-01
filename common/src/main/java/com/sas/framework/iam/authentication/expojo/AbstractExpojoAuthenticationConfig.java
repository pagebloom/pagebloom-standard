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
    
import com.sas.framework.iam.user.IUser;
    
import com.sas.framework.iam.authentication.IAuthenticator;
    
import com.sas.framework.iam.realm.IRealm;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;


// -[KeepBeforeClass]-



// -[Class]-

/**
 * Class Name : AbstractExpojoAuthenticationConfig
 * Diagram    : Authentication Impl
 * Project    : Entity Model Framework
 * Type       : abstract
 * Configuration of authentication under expojo.
 * 
 * @author Chris Colman
 */
public abstract 
class AbstractExpojoAuthenticationConfig
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
private static AbstractExpojoAuthenticationConfig self;


// -[Methods]-




/**
 * Returns the auxiliary challenge if there is one or null if no auxiliary challenge
 * is available to be presented to the user.
 */
public abstract IAuxiliaryChallenge getAuxiliaryChallenge();





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
public abstract IAuxiliaryChallenge userRequiresAuxiliaryChallenge(IUser user, String siteVisitorId, IRealm realm);





/**
 * Calls authenticateCredentials on each of the  IAuthenticators in the authenticators
 * list until one passes authentication or the list is exhausted. 
 */
public abstract IUser authenticateCredentials(String username, String password, boolean digested, IRealm realm);




/**
 * Assigns 'self'.
 */
public AbstractExpojoAuthenticationConfig()
{
	self = this;
}

/**
 * Returns self
 */
public static AbstractExpojoAuthenticationConfig get()
{
	if (self == null)
		throw new RuntimeException("ExpojoAuthenticationConfig has not yet been established");
	return self;
}

}


