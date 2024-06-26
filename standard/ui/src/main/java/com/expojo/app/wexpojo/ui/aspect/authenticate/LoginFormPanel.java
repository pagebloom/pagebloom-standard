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
    
import com.sas.framework.iam.authentication.ICredentialSpec;
    
import com.sas.app.wexpojo.biz.website.IWebsite;
    
import com.sas.platform.pagebloom.std.session.PagebloomStdSession;
    
import com.sas.framework.iam.authentication.IAuxiliaryChallenge;
    
import com.sas.framework.iam.authentication.expojo.ExpojoAuthenticationService;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// -[KeepBeforeClass]-
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.expojo.app.wexpojo.ui.aspect.landing.HomePage;
import com.expojo.app.wexpojo.ui.session.IWexpojoSession;

import com.sas.framework.iam.realm.IRealm;
import com.sas.framework.iam.authentication.AuthenticationException;
import com.sas.platform.pagebloom.common.auth.Credentials;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.*;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.*;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;

import org.apache.wicket.markup.html.pages.RedirectPage;

import com.sas.framework.iam.user.IUser;
import org.datanucleus.util.StringUtils;


// -[Class]-

/**
 * Class Name : LoginFormPanel
 * Diagram    : Authenticate
 * Project    : WexPOJO - Wicket + exPOJO application core
 * Type       : concrete
 * Describe here
 * 
 * @author Chris Colman
 */
public 
class LoginFormPanel extends WebsitePanel
{
// -[KeepWithinClass]-


// -[Fields]-



/**
 * NoDesc
 */
protected transient Button loginBtn;



/**
 * Credential specifications
 */
protected ICredentialSpec credSpec;



/**
 * NoDesc
 */
private Credentials credentials;



/**
 * NoDesc
 */
private Form form;



/**
 * NoDesc
 */
private CheckBox tAndCBox;

// [Added by Code Injection Wizard: SLF4J Logging Support]
// Do not edit code injected by the wizard directly in the source file as
// as it will be overwritten during subsequent updates. 
private static final Logger logger = LoggerFactory.getLogger(LoginFormPanel.class);


// -[Methods]-



/**
 * Invokes the auxiliary authentication challenge by forming the necessary URL that the
 * auxiliary challenge requires which may possibly include, for example, a success URL
 * and a fail URL. The details of the format of the URL is specific to any auxiliary
 * challenge.
 */
public void invokeAuxiliaryChallenge(IAuxiliaryChallenge auxiliaryChallenge, String protocolAndDomain, IWexpojoSession wexpojoSession, IRealm realm)
{
	// As we are invoking an auxiliary challenge we must mark the session as having
	// an unsatisfied auxiliary challenge
	wexpojoSession.setUnsatisfiedAuxiliaryChallenge(true);

	// UserID should be something immutable, unlike username....
	IUser user = wexpojoSession.getUser();
	String siteVisitorId = wexpojoSession.getSiteVisitorId();

	String url = auxiliaryChallenge.getChallengeUrl(protocolAndDomain, user, siteVisitorId, realm);

	// Without the SC_SEE_OTHER response parameter encoded parameter values become UN-encoded!
	throw new RedirectToUrlException(url, HttpServletResponse.SC_SEE_OTHER);
}



/**
 * Overridden in alternative authentication forms to return true.
 */
public boolean isAlternativeAuthentication()

{
	return false;
}



/**
 * Describe here
 */
public void submitForm(AjaxRequestTarget target)
{
	IWebsite website = getWebsite();

	if (StringUtils.isEmpty(credentials.getEmail()) || (credSpec.isPasswordRequired() && StringUtils.isEmpty(credentials.getPassword())))
	{
		form.error("Please provide both an email address and a password.");
		return;
	}

	IWexpojoSession wexpojoSession = PagebloomStdSession.get();
	IUser iUser;

	try
	{
		iUser = wexpojoSession.authenticate(credentials.getEmail(), credentials.getPassword(), credentials.getAutoLogin(), website.getRealm());

		if (iUser != null)
		{
			boolean okToContinue = true;
			boolean loggedOn = false;

			// Checkbox check - why are we asking to agree to terms and conditions on the log in screen - should only
			// be for registration screen or when terms and conditions have changed and we want user to agree to the
			// changes

			if (okToContinue && tAndCBox != null && !tAndCBox.getModelObject())
			{
				form.error("Please read the terms and conditions and tick the Terms and Conditions check box to acknowledge your agreement before continuing.");
				okToContinue = false;
			}
			else
				 loggedOn = true;


			if (okToContinue)
			{
				IAuxiliaryChallenge auxiliaryChallenge = ExpojoAuthenticationService.get().userRequiresAuxiliaryChallenge(iUser, wexpojoSession.getSiteVisitorId(), website.getRealm());

				boolean invokeAuxiliaryChallenge = false;

				// Are we in login page due to an intercept?
				Url url = RestartResponseAtInterceptPageException.getOriginalUrl();
				if (loggedOn && url != null)
				{
					if (auxiliaryChallenge == null)
						continueToOriginalDestination();
					else
					{
						invokeAuxiliaryChallenge = true;
					}
				}

				// Was not redirected to this authentication page

				if (auxiliaryChallenge != null)
				{
					invokeAuxiliaryChallenge = true;
				}
				else
					setResponsePage(HomePage.class);


				if (invokeAuxiliaryChallenge)
				{
					// we must redirect to the (potentially) external auxiliary challenge URL
					invokeAuxiliaryChallenge(auxiliaryChallenge, getProtocolAndDomain(), wexpojoSession, website.getRealm());
				}
			}
		}
		else
		{
			String errorMsg = "Invalid " + credSpec.getIdFieldName();

			if (credSpec.isPasswordRequired())
				 errorMsg += " or " + credSpec.getPasswordFieldName();

			form.error(errorMsg);

			// Wrong password - delay response to hinder the effort of brute force
			// attempt to guess a username/password.
			// Potentially a DDOS susceptibility - don't delay just refuse if too many attempts
			// Delaying ties up connections and a threads (in a non async impl)!
			try { Thread.sleep(100); } catch (InterruptedException e) { /* no-op */ }

			HttpServletResponse resp = (HttpServletResponse)getRequestCycle().getResponse().getContainerResponse();
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	catch (AuthenticationException ae)
	{
		form.error(ae.getMessage());
	}
}


/**
 * Adds components
 */
protected void onInitialize()
{
	super.onInitialize();

	IWebsite website = getWebsite();

	if (website != null)
	{
		credSpec = website.getCredentialSpec();
	}

	form = new Form(isAlternativeAuthentication() ? "orgRoleLoginForm" : "loginForm37")
	{
		@Override
		protected void onSubmit()
		{
		    submitForm(null);
		}
	};

	FeedbackPanel errorLabel = new FeedbackPanel(isAlternativeAuthentication() ? "orgRoleLoginFeedback" : "feedback",
			new ContainerFeedbackMessageFilter(form));
	form.add(errorLabel);


	form.add(new AjaxLink("signUp")
	{
		public void onClick(AjaxRequestTarget target)
		{
			logger.error("--------- Sign up not yet supported!! ---------");

			IWexpojoSession wexPojoSession = PagebloomStdSession.get();

			if (wexPojoSession.getUser() == null) {
//				AuthenticatePanel authenticatePanel = new AuthenticatePanel(getModalWindow(target), getOrg(), AuthenticatePanel.APM_signUp, AuthenticatePanel.SA_continueToDestination);
//				authenticatePanel.show(target);
			}
			else
			{
//              MessageBox.show(target, "You are already logged in. If you wish to sign up under a different account then first log off.", "Sign up not possible while logged in");
			}
		}
	});


	// Email Address
	final TextField<String> emailField = new TextField<>(isAlternativeAuthentication() ? "idField" : "emailField", new PropertyModel<String>(credentials, "email"));
	if (credSpec.isIdFieldEmailAddress())
	{
		emailField.add(RfcCompliantEmailAddressValidator.getInstance());
	}
	emailField.setRequired(true);
	emailField.setLabel(new Model<>(credSpec.getIdFieldName()));
	emailField.setOutputMarkupId(true);
	form.add(emailField);

	// If JS enabled set focus to this field
	Page page = getPage();
	page.add(new AjaxEventBehavior("onload")
	{
		@Override
		protected void onEvent(AjaxRequestTarget target)
		{
			target.focusComponent(emailField);
		}
	});

	// Password
	PasswordTextField passwordField = new PasswordTextField("passwordField", new PropertyModel<String>(credentials, "password"));
	passwordField.setRequired(credSpec.isPasswordRequired());
	passwordField.setLabel(new Model<>(credSpec.getPasswordFieldName()));
	form.add(passwordField);
	
	CheckBox autoLogin = new CheckBox("autoLoginBox", new PropertyModel<Boolean>(credentials, "autoLogin"));
	form.add(autoLogin);

	WebMarkupContainer passwordResetLink = new WebMarkupContainer("passwordReset");
	passwordResetLink.setVisible(false);
	form.add(passwordResetLink);

	add(form);
}

/**
 * Constructs the object
 */
public LoginFormPanel(String id)
{
	super(id);
	
	credentials = new Credentials();
}

}


