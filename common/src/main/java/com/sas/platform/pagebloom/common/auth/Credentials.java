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
package com.sas.platform.pagebloom.common.auth;

import java.lang.*;
import java.io.Serializable;


// -[KeepBeforeClass]-


// -[Class]-

/**
 * Class Name : Credentials
 * Diagram    : Credentials
 * Project    : pagebloom common
 * Type       : concrete
 * Describe here
 * 
 * @author Michael Moore, Chris Colman
 */
public 
class Credentials implements Serializable
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
protected transient String email;



/**
 * NoDesc
 */
protected transient String password;



/**
 * true if user selects autoLogin check box.
 */
private boolean autoLogin;



/**
 * Terms and Conditions check box if required.
 */
private boolean tAndC = false;


// -[Methods]-

/**
 * Called to detach any detachable references on the object.
 */
public void detach()
{
}

/**
 * Sets tAndC
 */
public void setTAndC(boolean tAndC)
{
    this.tAndC = tAndC;
}

/**
 * Returns tAndC
 */
public boolean getTAndC()
{
    return tAndC;
}

/**
 * Sets autoLogin
 */
public void setAutoLogin(boolean autoLogin)
{
    this.autoLogin = autoLogin;
}

/**
 * Returns autoLogin
 */
public boolean getAutoLogin()
{
    return autoLogin;
}

/**
 * Sets password
 */
public void setPassword(String password)
{
	// Wicket's PasswordTextField defaults to "resetPassword=true" which calls setPassword(null) on
	// the  model object (this object) when the form is detached to prevent the password from
	// be serialized when the page is serialized - that's good security policy so we should keep it!!
	// However ... this means we'll lose the password the user entered in the model - it's ok to keep
	// the password in the model as the password field is marked as transient and so won't be 
	// serialized anyway
	if (password != null)
		this.password = password;
}

/**
 * Returns password
 */
public String getPassword()
{
    return password;
}

/**
 * Sets email
 */
public void setEmail(String email)
{
    this.email = email;
}

/**
 * Returns email
 */
public String getEmail()
{
    return email;
}

}


