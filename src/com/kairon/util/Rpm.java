package com.kairon.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

public class Rpm {
	public Rpm() {
		super();
	}

	public static void off(String IP, String username, String password) {
		String uri;
		String param = null;
		try {
			param = "Disconnect=" + URLEncoder.encode("¶Ï Ïß", "UTF-8")
					+ "&wan=1";
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		URI u = null;
		try {
			u = URIUtils.createURI("http", IP, 80, "/userRpm/StatusRpm.htm",
					param, null);
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		}
		uri = u.toString();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = null;
		HttpResponse response = null;
		try {
			client.getCredentialsProvider().setCredentials(
					new AuthScope(IP, 80),
					new UsernamePasswordCredentials(username, password));
			get = new HttpGet(uri);
			response = client.execute(get);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	public static void on(String uri, String IP, String username,
			String password) {
		// String uri;
		String param = null;
		// try {
		// param = "Connect=" + URLEncoder.encode("Á¬ ½Ó", "UTF-8") + "&wan=1";
		// } catch (UnsupportedEncodingException e2) {
		// e2.printStackTrace();
		// }
		// URI u = null;
		// try {
		// u = URIUtils.createURI("http", "192.168.1.1", 80,
		// "/userRpm/StatusRpm.htm", param, null);
		// } catch (URISyntaxException e2) {
		// e2.printStackTrace();
		// }
		// uri = u.toString();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = null;
		HttpResponse response = null;
		try {
			client.getCredentialsProvider().setCredentials(
					new AuthScope(IP, 80),
					new UsernamePasswordCredentials(username, password));
			get = new HttpGet(uri);
			response = client.execute(get);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}
}
