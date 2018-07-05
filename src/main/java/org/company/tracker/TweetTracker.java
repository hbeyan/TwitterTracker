package org.company.tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.company.oauth.twitter.TwitterAuthenticationException;
import org.company.oauth.twitter.TwitterAuthenticator;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

public class TweetTracker {
	final static String CONSUMER_KEY = "*";
	final static String CONSUMER_SECRET = "*";
	
	final static int TWEET_LIMIT = 100;
	final static int TIME_LIMIT_IN_SECONDS = 60;
	final static String TRACKING_PARAM = "twitter";
	
	public static void main(String[] args) {
		TwitterAuthenticator twitterAuthenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);
		
		HttpRequestFactory httpRequestFactory = null;
		try {
			httpRequestFactory = twitterAuthenticator.getAuthorizedHttpRequestFactory();
		} catch (TwitterAuthenticationException e) {
        	System.out.println("Twitter Authentication Exception " + e + "\nStopping the program");
        	System.exit(1);
		}

		GenericUrl url = new GenericUrl("https://stream.twitter.com/1.1/statuses/filter.json?track=" + TRACKING_PARAM);
		HashMap<String, User> users = new HashMap<>();
		
		try {
			HttpRequest request = httpRequestFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
			
            try {
            	int count = 0;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getContent()));
                
                String line;
                long start = System.currentTimeMillis();
                long end = start + TIME_LIMIT_IN_SECONDS*1000;     
                
                System.out.println("Tracking tweets...");
                
                while ((line = bufferedReader.readLine()) != null && count < TWEET_LIMIT && System.currentTimeMillis() < end) {
                	if(!line.isEmpty()){
                        JSONObject msgJson = new JSONObject(line);
                        JSONObject userJson = msgJson.getJSONObject("user");
                        
                        Date userDate = parseDate(userJson.getString("created_at"));
                        User user = new User(userJson.getString("id_str"), userJson.getString("created_at"), userJson.getString("screen_name"), userDate);
                        
                        Date msgDate = parseDate(msgJson.getString("created_at"));
                        Message msg = new Message(msgJson.getString("id_str"),msgJson.getString("created_at"), msgJson.getString("text"), userJson.getString("id_str"), msgDate);
                        
                        if(users.get(user.getId()) == null){
                            user.addMessagae(msg);
                        	users.put(user.getId(),user);
                        }
                        else{
                        	users.get(user.getId()).addMessagae(msg);
                        }                    
                        count++;
                	}         
                }

            } catch (JSONException e) {
            	System.out.println("JSON exception " + e);
			} catch (ParseException e) {
            	System.out.println("Parse exception " + e);
			} finally {
                response.disconnect();
            }
            
            ArrayList<User> userList =  new ArrayList<>(users.values());
            userList.sort((u1, u2) -> u1.getFormattedDate().compareTo(u2.getFormattedDate()));
            
            for(User u:userList){
            	System.out.println( "User Id:"+ u.getId() + " Screen Name:" + u.getName() + " Created at:" + u.getDate().toString());
            	
            	u.sortMessages();
            	u.printMessages();
            }

		} catch (IOException e) {
        	System.out.println("IO exception" + e);
		} 		
		
	}
	
	public static Date parseDate(String date) throws ParseException{
		String format="EEE MMM dd HH:mm:ss ZZZZZ yyyy";

		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		sdf.setLenient(true);
		return sdf.parse(date);
	}


}
