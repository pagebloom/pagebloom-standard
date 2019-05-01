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
import com.sas.platform.pagebloom.common.auth.RegisterCredentials;


// -[KeepBeforeClass]-


// -[Class]-

/**
 * Class Name : PagebloomRegisterCredentials
 * Diagram    : Credentials
 * Project    : pagebloom common
 * Type       : concrete
 * Describe here
 * 
 * @author Michael Moore, Chris Colman
 */
public 
class PagebloomRegisterCredentials extends RegisterCredentials
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
private String firstName;



/**
 * NoDesc
 */
private String lastName;


// -[Methods]-

/**
 * Sets firstName
 */
public void setFirstName(String firstName)
{
    this.firstName = firstName;
}

/**
 * Returns firstName
 */
public String getFirstName()
{
    return firstName;
}

/**
 * Sets lastName
 */
public void setLastName(String lastName)
{
    this.lastName = lastName;
}

/**
 * Returns lastName
 */
public String getLastName()
{
    return lastName;
}

}


