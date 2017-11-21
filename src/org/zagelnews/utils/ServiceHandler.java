package org.zagelnews.utils;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.db.DatabaseHandler;
import org.zagelnews.dtos.NamedBinaryObject;

import android.app.Activity;

public class ServiceHandler {
	public final static int GET = 1;
	public final static int POST = 2;

	/**
	 * Making service call
	 * @url - url to make request
	 * */
	public static JSONObject makeServiceCall(Activity activity, String url, List<NameValuePair> params) {
		return makeServiceCall(activity, url, POST, params);
	}

	/**
	 * Making service call
	 * @url - url to make request
	 * */
	public static JSONObject makeServiceCall(Activity activity, String url) {
		return makeServiceCall(activity, url, POST, new ArrayList<NameValuePair>());
	}

	/**
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public static JSONObject makeServiceCall(Activity activity, String url, int method) {
		return makeServiceCall(activity, url, method, new ArrayList<NameValuePair>());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Making service call
	 * @url - url to make request
	 * */
	public static JSONArray makeServiceCallArr(Activity activity, String url, List<NameValuePair> params) {
		return makeServiceCallArr(activity, url, POST, params);
	}

	/**
	 * Making service call
	 * @url - url to make request
	 * */
	public static JSONArray makeServiceCallArr(Activity activity, String url) {
		return makeServiceCallArr(activity, url, POST, new ArrayList<NameValuePair>());
	}

	/**
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public static JSONArray makeServiceCallArr(Activity activity, String url, int method) {
		return makeServiceCallArr(activity, url, method, new ArrayList<NameValuePair>());
	}
	
	
	
	
	
	

	/**
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public static JSONObject makeServiceCall(Activity activity, String url, int method,
			List<NameValuePair> params) {
		//add default parameters to the request
		addDefaultParams(activity, params);

		JSONObject jObj = null;
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			// Checking http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("Accept-Language", ClientConfiguration.lang);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				}

				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept-Language", ClientConfiguration.lang);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();

			String response = EntityUtils.toString(httpEntity);
			jObj = new JSONObject(response);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			e=null;
		} catch (IOException e) {
			e.printStackTrace();
			e= null;
		} catch (JSONException e) {
			e.printStackTrace();
			e = null;
		}catch (Throwable e) {
			e.printStackTrace();
		}

		return jObj;

	}
	
	
	/**
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public static JSONArray makeServiceCallArr(Activity activity, String url, int method,
			List<NameValuePair> params) {
		//add default parameters to the request
		addDefaultParams(activity, params);

		JSONArray jObj = null;
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			// Checking http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("Accept-Language", ClientConfiguration.lang);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				}

				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept-Language", ClientConfiguration.lang);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();

			String response = EntityUtils.toString(httpEntity);
			jObj = new JSONArray(response);

		} catch (UnsupportedEncodingException e) {
		} catch (ClientProtocolException e) {
			e=null;
		} catch (IOException e) {
			e= null;
		} catch (JSONException e) {
			e = null;
		}

		return jObj;

	}

	/**
	 * add the default parameters to each request
	 * @param params
	 */
	private static void addDefaultParams(Activity activity, List<NameValuePair> params) {
		params.add(new BasicNameValuePair("zone", ClientConfiguration.zone));
		String activityName = activity.getClass().getSimpleName() ;
		if(activityName.equals("Register")||
				activityName.equals("LoginActivity")||
				activityName.equals("ForgetPassword")){
			return;
		}

		HashMap<String,String> user = ClientConfiguration.getUser(activity);
		if(user!=null){
			params.add(new BasicNameValuePair("userId", user.get(DatabaseHandler.KEY_ID)));
			params.add(new BasicNameValuePair("token", user.get(DatabaseHandler.KEY_TOKEN)));
		}
	}

	// constructor
	public ServiceHandler() {

	}



	public static JSONObject makeServiceCall(Activity activity, String url, List<NameValuePair> params, NamedBinaryObject ... data) {
		//add default parameters to the request
		addDefaultParams(activity, params);
		JSONObject jObj=null;
		try
		{
			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(url);
			post.addHeader("Accept-Language", ClientConfiguration.lang);

			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			Charset chars = Charset.forName("UTF-8");
			entityBuilder.setCharset(chars);

			ContentType contentType = ContentType.create("text/plain", chars);

			//add text parameters
			if(params!=null&&params.size()>0){
				for(NameValuePair param: params){
					entityBuilder.addTextBody(param.getName(), param.getValue(), contentType);
				}
			}

			ContentType binaryContentType = ContentType.create("image/jpeg");
			//add binary parameters
			if(data!=null&&data.length>0){
				for(int i=0; i< data.length; i++){
					NamedBinaryObject binaryObj = data[i];
					if(binaryObj!=null){
						byte [] byteArr =  binaryObj.getBinaryObj();
						String binaryTag =  binaryObj.getBinaryName();
						if(byteArr!=null&&byteArr.length>0){
							ByteArrayBody fb = new ByteArrayBody(byteArr, binaryContentType,binaryTag);
							entityBuilder.addPart(binaryTag, fb);
						}
					}
				}
			}

			HttpEntity entity = entityBuilder.build();

			post.setEntity(entity);

			HttpResponse response = client.execute(post);

			HttpEntity httpEntity = response.getEntity();

			String content = EntityUtils.toString(httpEntity);
			jObj = new JSONObject(content);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return jObj;
	}
}
