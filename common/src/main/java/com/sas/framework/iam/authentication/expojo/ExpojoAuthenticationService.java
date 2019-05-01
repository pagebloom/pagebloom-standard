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
package com.sas.framework.iam.authentication.expojo;

import java.lang.*;
import com.sas.framework.iam.authentication.IAuthenticationService;
import com.sas.framework.expojo.ServiceComponent;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
    
import com.sas.framework.iam.user.IUser;
    
import com.sas.framework.iam.realm.IRealm;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
    
import com.sas.framework.iam.authentication.IAuthenticator;


// -[KeepBeforeClass]-

import com.sas.framework.expojo.ExpojoContext;

import com.sas.framework.expojo.Ex;

// -[Class]-

/**
 * Class Name : ExpojoAuthenticationService
 * Diagram    : Authentication Impl
 * Project    : pagebloom common
 * Type       : concrete
 * Core expojo based IAuthenticationService impl.
 * 
 * @author Chris Colman
 */
public 
class ExpojoAuthenticationService extends ServiceComponent implements IAuthenticationService
{
// -[KeepWithinClass]-


// -[Fields]-


// -[Methods]-

/**
 * Returns an IAuxiliaryChallenge for the given realm if available for the realm.
 * This should be used to determine if a newly registered account should proceed to registration
 * for the auxiliary challenge.
 * This should not be used to determine if, after a user passes first factor authentication
 * (eg., password), that they be challenged with the auxiliary authentication challenge.
 */
public IAuxiliaryChallenge getAuxiliaryChallengeForRealm(IRealm realm)
{
	if (realm.supportsAuxiliaryChallenge())
		return AbstractExpojoAuthenticationConfig.get().getAuxiliaryChallenge();
	return null;
}

/**
 * Retrieves the expojo component from the ExpojoContext.
 */
public static IAuthenticationService get()
{
	return Ex.c(IAuthenticationService.class);
}

/**
 * While an IAuxiliaryChallenge may be associated with this IAuthenticationService it
 * may not be required in every circumstance (e.g. business rules may be configured to
 * only require an auxiliary authentication challenge in certain cases: e.g. user has
 * logged in a device that they have never logged in from before - i.e. the siteVisitorId
 * will not have been encountered for this user before).
 * This method will only return non null if the user is required to perform a challenge
 * for an auxiliary factor of authentication (e.g. enter a security code sent via email
 * or an SMS).
 */
public IAuxiliaryChallenge userRequiresAuxiliaryChallenge(IUser user, String siteVisitorId, IRealm realm)
{
	if (realm.supportsAuxiliaryChallenge())
		return AbstractExpojoAuthenticationConfig.get().userRequiresAuxiliaryChallenge(user, siteVisitorId, realm);
	return null;
}

/**
 * Calls authenticateCredentials on each of the  IAuthenticators in the authenticators
 * list until one passes authentication or the list is exhausted. 
 */
public IUser authenticateCredentials(String username, String password, boolean digested, IRealm realm)
{
	return AbstractExpojoAuthenticationConfig.get().authenticateCredentials(username, password, digested, realm);
}

}


