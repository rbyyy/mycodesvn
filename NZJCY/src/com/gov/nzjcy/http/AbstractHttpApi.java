package com.gov.nzjcy.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.Uri;

import com.gov.nzjcy.entity.ApplicationEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.parser.Parser;
import com.gov.nzjcy.util.DebugConfig;
import com.gov.nzjcy.util.JSONUtils;

abstract public class AbstractHttpApi implements HttpApi{
    protected static final Logger   LOG                    = Logger.getLogger(AbstractHttpApi.class.getCanonicalName());
    protected static final boolean  DEBUG                  = DebugConfig.DEBUG;

    private static final String     DEFAULT_CLIENT_VERSION = "";
    private static final String     CLIENT_VERSION_HEADER  = "User-Agent";
    private static final int        TIMEOUT                = 20;

    private final DefaultHttpClient mHttpClient;
    private final String            mClientVersion;

    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion){
        mHttpClient = httpClient;
        if(clientVersion != null){
            mClientVersion = clientVersion;
        }else{
            mClientVersion = DEFAULT_CLIENT_VERSION;
        }
    }

    public ApplicationEntity executeHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends ApplicationEntity> parser) throws ParseException, BaseException, IOException{
        if(DEBUG) LOG.log(Level.FINE, "doHttpRequest: " + httpRequest.getURI());

        HttpResponse response = executeHttpRequest(httpRequest);
        if(DEBUG) LOG.log(Level.FINE, "executed HttpRequest for: "
                + httpRequest.getURI().toString());

        int statusCode = response.getStatusLine().getStatusCode();
        switch(statusCode){
            case 200:
                return JSONUtils.consume(parser, EntityUtils.toString(response.getEntity()));

            case 400:
                if(DEBUG) LOG.log(Level.FINE, "HTTP Code: 400");
                throw new BaseException(response.getStatusLine().toString());

            case 401:
                response.getEntity().consumeContent();
                if(DEBUG) LOG.log(Level.FINE, "HTTP Code: 401");
                throw new BaseException(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                if(DEBUG) LOG.log(Level.FINE, "HTTP Code: 404");
                throw new BaseException(response.getStatusLine().toString());

            case 500:
                response.getEntity().consumeContent();
                if(DEBUG) LOG.log(Level.FINE, "HTTP Code: 500");
                throw new BaseException("server is down. Try again later.");

            default:
                if(DEBUG)
                    LOG.log(Level.FINE, "Default case for status code reached: " + response.getStatusLine().toString());
                response.getEntity().consumeContent();
                throw new BaseException("Error connecting to server: " + statusCode + ". Try again later.");
        }
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     * 
     * @param httpRequest
     * @return
     * @throws IOException
     */
    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException{
        if(DEBUG) LOG.log(Level.FINE, "executing HttpRequest for: "
                + httpRequest.getURI().toString());
        try{
            mHttpClient.getConnectionManager().closeExpiredConnections();
            return mHttpClient.execute(httpRequest);
        }catch(IOException e){
            httpRequest.abort();
            throw e;
        }
    }

    public HttpGet createHttpGet(String url, NameValuePair...nameValuePairs){
        if(DEBUG) LOG.log(Level.FINE, "creating HttpGet for: " + url);
        String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
        HttpGet httpGet = new HttpGet(url + "?" + query);
        httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        if(DEBUG) LOG.log(Level.FINE, "Created: " + httpGet.getURI());
        return httpGet;
    }

    public HttpPost createHttpPost(String url, NameValuePair...nameValuePairs){
        if(DEBUG) LOG.log(Level.FINE, "creating HttpPost for: " + url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), HTTP.UTF_8));
        }catch(UnsupportedEncodingException e1){
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
        if(DEBUG) LOG.log(Level.FINE, "Created: " + httpPost);
        return httpPost;
    }
    
    
//    /**
//	 * 使用Apache HttpClient
//	 * @param jsonParam
//	 * @return
//	 */
//	public String doPost(String url, String jsonParam) {
//		try {
//			StringEntity entity = new StringEntity(jsonParam, "UTF-8");
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(url);
//			httpPost.setHeader("Content-Type","application/x-www-form-urlencoded"); 
//			httpPost.setEntity(entity);
//			
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				InputStream inputStream = httpResponse.getEntity().getContent();
//
//				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//				byte[] data = new byte[1024];
//				int len = 0;
//				String result = null;
//				if (inputStream != null) {
//					try {
//						while ((len = inputStream.read(data)) != -1) {
//							outputStream.write(data, 0, len);
//						}
//						result = new String(outputStream.toByteArray(), "UTF-8");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				
//				return result;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
    
    

    public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary)
            throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIMEOUT * 1000);
        conn.setRequestMethod("POST");

        conn.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        return conn;
    }

    private List<NameValuePair> stripNulls(NameValuePair...nameValuePairs){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(int i = 0; i < nameValuePairs.length; i++){
            NameValuePair param = nameValuePairs[i];
            if(param.getValue() != null){
                if(DEBUG) LOG.log(Level.FINE, "Param: " + param);
                System.out.println("Param: " + param);
                params.add(param);
            }
        }
        return params;
    }

    /**
     * Create a thread-safe client. This client does not do redirecting, to
     * allow us to capture correct "error" codes.
     * 
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpClient(){
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);
//        httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("192.168.1.10",8888));

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams(){

        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        return params;
    }

}
