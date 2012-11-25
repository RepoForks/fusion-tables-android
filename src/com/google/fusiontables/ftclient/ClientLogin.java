// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.fusiontables.ftclient;

import java.net.URLEncoder;
import java.util.Map;

import android.os.AsyncTask;

import com.google.fusiontables.url.RequestHandler;

/**
 * Helper class for getting auth token.
 * 
 * @author kbrisbin@google.com (Kathryn Hurley)
 */
public class ClientLogin extends AsyncTask<String, Void, String> {
	private final static String authURI = "https://www.google.com/accounts/ClientLogin";
	private LoginCallback callback;

	public ClientLogin(LoginCallback callback) {
		this.callback = callback;
	}
	
	/**
	 * Returns the auth token if the user is successfully authorized.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the client login token, or null if not authorized
	 */

	@Override
	protected String doInBackground(String... params) {
		String username = params[0];
		String password = params[1];
		
		// Encode the body
		String body = "Email=" + URLEncoder.encode(username) + "&" + "Passwd="
				+ URLEncoder.encode(password) + "&" + "service=fusiontables&"
				+ "accountType=HOSTED_OR_GOOGLE";

		// Send the response and parse results to get token

		String response = RequestHandler.sendHttpRequest(authURI, "POST", body, null);
		
		// If the response is length 4, the authorization was successful
		String[] splitResponse = response.trim().split("=");
		if (splitResponse.length == 4) {
			return splitResponse[3];
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onFinish(result);
	}

	public interface LoginCallback{
		public void onFinish(String result);
	}
}
