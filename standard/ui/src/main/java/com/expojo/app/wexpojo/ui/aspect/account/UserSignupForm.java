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
import com.expojo.app.wexpojo.ui.aspect.WebsitePanel;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// -[KeepBeforeClass]-
import com.sas.framework.iam.user.IUser;
import com.sas.framework.iam.user.expojo.UserRepository;
import com.sas.framework.system.Environment;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.sas.platform.pagebloom.common.auth.Credentials;



// -[Class]-

/**
 * Class Name : UserSignupForm
 * Diagram    : Account
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Describe here
 * 
 * @author Chris Colman
 */
public 
class UserSignupForm extends WebsitePanel
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
private Form form;



/**
 * NoDesc
 */
protected WebMarkupContainer personalDetailsContainer;



/**
 * True if email should be confirmed.
 */
private boolean confirmEmail;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LoggerFactory.getLogger(UserSignupForm.class);


// -[Methods]-



/**
 * Called by the form when it is submitted. May be left empty, or overridden for special
 * behaviour in either derived or inline classes.
 */
public void onFormSubmit()

{
}



/**
 * Returns the personal details WebMarkupContainer.
 */
public WebMarkupContainer getPersonalDetailsContainer()

{
	return personalDetailsContainer;
}



/**
 * Returns form
 */
public Form getForm()

{
    return form;
}

/**
 * Describe here
 */
public void onInitialize()
{
	super.onInitialize();

	// Form
	form = new Form("form");
	add(form);
	
	// Section container
	personalDetailsContainer = new WebMarkupContainer("personalDetailsContainer");
	form.add(personalDetailsContainer);

	// Email
	EmailTextField emailField = new EmailTextField("email");
	emailField.setRequired(true);
	emailField.add(new IValidator<String>()
	{
		@Override
		public void validate(IValidatable<String> validatable)
		{
			// Check if new username is unique
			IUser user = UserRepository.get().findUserByUsername(validatable.getValue());

			if (user != null)
			{
				if (Environment.get().getDemoMode())
					logger.warn("An account already exists for this email but in demo mode so allowing reregistration of same account");
				else
					error(validatable, "userAlreadyExistsForEmail");
			}

		}

		private void error(IValidatable<String> validatable, String errorKey)
		{
			ValidationError error = new ValidationError();
			error.addKey(getClass().getSimpleName() + "." + errorKey);
			validatable.error(error);
		}
	});
	personalDetailsContainer.add(emailField);

	if (confirmEmail)
	{
		// Confirm email
		EmailTextField confirmEmailField = new EmailTextField("confirmEmail");
		personalDetailsContainer.add(confirmEmailField);

		// Email validator
		form.add(new EqualInputValidator(emailField, confirmEmailField));
	}

	// Password
	PasswordTextField passwordField = new PasswordTextField("password");
	personalDetailsContainer.add(passwordField);

	// Confirm Password
	WebMarkupContainer confirmPasswordAlias;
	PasswordTextField confirmPasswordField = null;

	if (!Environment.get().getDemoMode())
	{
		confirmPasswordField = new PasswordTextField("confirmPassword");
		confirmPasswordField.setRequired(true);
		confirmPasswordField.setResetPassword(false);
		confirmPasswordAlias = confirmPasswordField;
	}
	else
	{
		confirmPasswordAlias = new WebMarkupContainer("confirmPassword");
		confirmPasswordAlias.setVisible(false);
	}

	personalDetailsContainer.add(confirmPasswordAlias);

	// Validators
	if (confirmPasswordField != null)
		form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));
}

/**
 * Constructs the object
 */
public UserSignupForm(String id, Credentials credentials, boolean confirmEmail)
{
	super(id);
	setDefaultModel(new CompoundPropertyModel<>(credentials));
	this.confirmEmail = confirmEmail;
}

}


