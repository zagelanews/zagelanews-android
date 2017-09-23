package org.zagelnews.utils;



import java.io.BufferedReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.zagelnews.constants.ServerConstants;

public class JSONParser {



	// constructor
	public JSONParser() {

	}

	public static JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			HttpConnectionParams.setConnectionTimeout(httpParameters, ServerConstants.TIMEOUT_CONNECTION);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(httpParameters, ServerConstants.TIMEOUT_CONNECTION);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();





		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is,"UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
		}

		// return JSON String
		return jObj;


	}


	public static JSONObject getJSONFromUrlMultiPartReq(
			String url, 
			List<NameValuePair> params, 
			byte[]sampleProfileImage,
			byte[]fullProfileImage)  {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		Charset chars = Charset.forName("UTF-8");
		builder.setCharset(chars);

		ContentType contentType = ContentType.create("text/plain", chars);

		if(params!=null&&params.size()>0){
			for(NameValuePair param: params){
				builder.addTextBody(param.getName(), param.getValue(), contentType);
			}
		}

		if(sampleProfileImage!=null
				&&sampleProfileImage.length>0
				&&fullProfileImage!=null
				&&fullProfileImage.length>0){

			ByteArrayBody fb = new ByteArrayBody(sampleProfileImage, "PROFILE.sample");
			builder.addPart("PROFILE.sample", fb);

			fb = new ByteArrayBody(fullProfileImage, "PROFILE.full");
			builder.addPart("PROFILE.full", fb);

		}
		final HttpEntity yourEntity = builder.build();

		class ProgressiveEntity implements HttpEntity {
			@Override
			public void consumeContent() throws IOException {
				yourEntity.consumeContent();                
			}
			@Override
			public InputStream getContent() throws IOException,
			IllegalStateException {
				return yourEntity.getContent();
			}
			@Override
			public Header getContentEncoding() {             
				return yourEntity.getContentEncoding();
			}
			@Override
			public long getContentLength() {
				return yourEntity.getContentLength();
			}
			@Override
			public Header getContentType() {
				return yourEntity.getContentType();
			}
			@Override
			public boolean isChunked() {             
				return yourEntity.isChunked();
			}
			@Override
			public boolean isRepeatable() {
				return yourEntity.isRepeatable();
			}
			@Override
			public boolean isStreaming() {             
				return yourEntity.isStreaming();
			} // CONSIDER put a _real_ delegator into here!

			@Override
			public void writeTo(OutputStream outstream) throws IOException {

				class ProxyOutputStream extends FilterOutputStream {
					/**
					 * @author Stephen Colebourne
					 */

					public ProxyOutputStream(OutputStream proxy) {
						super(proxy);    
					}
					public void write(int idx) throws IOException {
						out.write(idx);
					}
					public void write(byte[] bts) throws IOException {
						out.write(bts);
					}
					public void write(byte[] bts, int st, int end) throws IOException {
						out.write(bts, st, end);
					}
					public void flush() throws IOException {
						out.flush();
					}
					public void close() throws IOException {
						out.close();
					}
				} // CONSIDER import this class (and risk more Jar File Hell)

				class ProgressiveOutputStream extends ProxyOutputStream {
					public ProgressiveOutputStream(OutputStream proxy) {
						super(proxy);
					}
					public void write(byte[] bts, int st, int end) throws IOException {

						// FIXME  Put your progress bar stuff here!

						out.write(bts, st, end);
					}
				}

				yourEntity.writeTo(new ProgressiveOutputStream(outstream));
			}

		};
		ProgressiveEntity myEntity = new ProgressiveEntity();
		post.setEntity(myEntity);
		JSONObject jObj=null;
		try {
			HttpResponse response = client.execute(post);        
			String content = getContent(response);

			// try parse the string to a JSON object
			jObj = new JSONObject(content);
		} catch (JSONException e) {
		} catch (IOException e) {
		}

		// return JSON String
		return jObj;
	} 

	public static String getContent(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String body = "";
		String content = "";

		while ((body = rd.readLine()) != null) 
		{
			content += body + "\n";
		}
		return content.trim();
	}
}
