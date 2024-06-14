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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.expojo.app.wexpojo.ui.aspect.dashboard.DashboardPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.WebSession;

import com.expojo.app.wexpojo.ui.session.IWexpojoSession;

import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
import com.sas.framework.iam.authentication.expojo.ExpojoAuthenticationService;
import com.sas.framework.iam.realm.IRealm;
import com.sas.framework.iam.user.IUser;

import com.sas.platform.pagebloom.common.auth.RegisterCredentials;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;


// -[Class]-

/**
 * Class Name : RegisterPanel
 * Diagram    : Account
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : abstract
 * Describe here
 * 
 * @author Chris Colman
 */
public abstract 
class RegisterPanel extends WebsitePanel
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
protected Form<Void> form;



/**
 * Model used to store credentials during registration.
 */
protected RegisterCredentials registerCredentials;



/**
 * The form embedded in the panel.
 */
protected UserSignupForm formPanel;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LoggerFactory.getLogger(RegisterPanel.class);




// -[Methods]-


/**
 * Creates the signup form for this registration.
 */
public UserSignupForm createSignupForm()
{
	return new UserSignupForm("userSignupForm", registerCredentials, true);
}


/**
 * Registers the user based on the credentia;s they provided.
 */
public abstract void registerUser();


/**
 * Called when the registration form is submitted. Can be overridden to provide different
 * behaviour in derived classes.
 */
public void onFormSubmit()
{
	// Form validation was successful so register user
	registerUser();

	IRealm realm = getWebsite().getRealm();

	IAuxiliaryChallenge auxiliaryChallenge = ExpojoAuthenticationService.get().getAuxiliaryChallengeForRealm(realm);

	// if realm supports an auxiliary challenge then we must prompt user to provide whatever data is necessary
	// to support that challenge.
	// For example:
	// Auth codes sent via SMS: user must provide mobile phone number
	if (auxiliaryChallenge != null)
	{
		IWexpojoSession wexpojoSession = (IWexpojoSession)WebSession.get();
		IUser user = wexpojoSession.getUser();

		String url = auxiliaryChallenge.getRegistrationUrl(getProtocolAndDomain(), user, wexpojoSession.getSiteVisitorId(), realm);

		// Without the SC_SEE_OTHER response parameter encoded parameter values become UN-encoded!


		//throw new RedirectToUrlException(url);  // loses encoding on query parameters

		//throw new RedirectToUrlException(url, HttpServletResponse.SC_TEMPORARY_REDIRECT); fails - wicket doesn't support 307

		// Keeps encoding but gets CORS error if invoked in an AJAX request
		// The CORS issue is fixed in 7.x or later so we can revert to AJAX form submission when we migrate
		// up to that version or later
		throw new RedirectToUrlException(url, HttpServletResponse.SC_SEE_OTHER);

		/*
		HttpServletResponse response = (HttpServletResponse)getWebResponse().getContainerResponse();
		try
		{
			//response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
			response.sendRedirect(url);
		}
		catch (IOException ioe)
		{
			logger.error("Redirect failed", ioe);
		}
*/
	}
	else
	{
		// no configuration of an auxiliary challenge required to take user to the dashboard
		setResponsePage(DashboardPage.class);
	}
}


/**
 * Creates the credentials model object. Overridden by derived classes to create extended
 * register credentials.
 */
public RegisterCredentials createCredentialsModel()
{
	return new RegisterCredentials();
}


/**
 * Describe here
 */
protected void onInitialize()
{
	super.onInitialize();

	registerCredentials = createCredentialsModel();

	// Form
	form = new Form<Void>("form")
	{
		@Override
		protected void onSubmit()
		{
			// Call overridable submit handler on the panel
			RegisterPanel.this.onFormSubmit();
		}
	};

	add(form);

	// Feedback
	form.add(new FeedbackPanel("feedback").setOutputMarkupId(true));

	// Supplied subform
	formPanel = createSignupForm();
	form.add(formPanel);

	// Submit button
	// Can't use AJAX submit here because
	//
	// 1. We lose URL query parameter encoding if we use RedirectToUrlException with SC_MOVED_TEMPORARILY
	//   See ServletWebResponse:
	//   	sendRedirect(String url) {
	//   	try {
	//   		this.redirect = true;
	//   >>>> 'encode' is actually performing a decode in this line <<<<
	//   	url = this.encodeRedirectURL(url); this.disableCaching();
	//
	// 2. We get CORS errors if we use RedirectToUrlException with SC_SEE_OTHER
	//   SC_SEE_OTHER was suggested as a solution to the loss of encoding but gives CORS errors if invoked
	//   via an AJAX call.
	//
	// NOTE: Revert to AJAX form submission when migrated to 7.x see https://issues.apache.org/jira/browse/WICKET-6638

/*
	form.add(new AjaxFallbackButton("submit", form)
	{
		@Override
		public void onSubmit(AjaxRequestTarget target, Form<?> form)
		{
			// Call overridable submit handler on the panel
			RegisterPanel.this.onFormSubmit();
		}

		@Override
		public void onError(AjaxRequestTarget target, Form<?> form)
		{
			target.add(form.get("feedback"));
		}
	});
*/
}


/**
 * Constructs the object
 */
public RegisterPanel(String id)
{
	super(id);
}

}


