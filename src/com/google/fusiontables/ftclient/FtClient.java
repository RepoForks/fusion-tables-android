// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.fusiontables.ftclient;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;

import com.google.fusiontables.url.RequestHandler;

/**
 * Handles requests to Fusion Tables.
 * 
 * @author kbrisbin@google.com (Kathryn Hurley)
 */
public class FtClient {
  public static final String REQUEST_URL =
      "https://www.google.com/fusiontables/api/query";
  private String token;

  /**
   * Constructor, sets the auth token.
   *
   * @param token  the ClientLogin token
   */
  public FtClient(String token) {
    this.token = token;
  }
  
  public void query(String q){
	  new QueryTask().execute(q);
  }

  /**
   * Send the query to Fusion Tables.
   *
   * @param query  the query to send
   * @return the results of the query
   */
  public class QueryTask extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		String query = params[0];
		String result = "";
		
		// Create the auth header
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "GoogleLogin auth=" + token);
		
		// Convert to lower for comparison below
		String lower = query.toLowerCase();
		// Encode the query
		query = "sql=" + URLEncoder.encode(query);
		
		// Determine POST or GET based on query
		if (lower.startsWith("select") ||
				lower.startsWith("show") ||
				lower.startsWith("describe")) {
			
			result = RequestHandler.sendHttpRequest(REQUEST_URL + "?" + query,
					"GET", null, headers);
			
		} else {
			result = RequestHandler.sendHttpRequest(REQUEST_URL,
					"POST", query, headers);
		}
		
		return result;
	}
	  
  }
}
