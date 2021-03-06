import java.awt.LinearGradientPaint;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TwillioManager {

	private final String ACCOUNT_SID = "AC51ef9aeed0bb7e5d8ce4f9f927cd29a9";
	private final String AUTH_TOKEN = "2313cfd304e822b5c855dbb0f113e7df";

	private final String baseUrl = "https://api.twilio.com/2010-04-01/Accounts/";

	private final String numberSid = "PN60d014a311a39143b77f5ab349a232d5";

	// https://{AC51ef9aeed0bb7e5d8ce4f9f927cd29a9}:{2313cfd304e822b5c855dbb0f113e7df}@api.twilio.com/2010-04-01/Accounts

	private int currentCount = 0;

	private static TwillioManager manager;
	
	public static TwillioManager getInstance() {
		if(manager == null) {
			manager = new TwillioManager();
			return getInstance();
		} else {
			return manager;
		}
	}
	
	private TwillioManager() {
		RestManager.setCredencials(ACCOUNT_SID, AUTH_TOKEN);
		 System.out.println("Starting with: " + getMessagesAsArray().size());
	}

	public String runAccounts() {
		try {
			return RestManager.sendGet(baseUrl + ".json");
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error";
	}
	
	//do stuff till, use latch.await() right be4 u need the stuff in data[] to wait on it (handles in threadedresponse rn)
	public ThreadedResponse waitForData(int size) {
		 final CountDownLatch latch = new CountDownLatch(size);
		 final Object[] data = new Object[size];
		 ThreadedResponse reply = new ThreadedResponse(latch, data);
		   Thread t = new Thread(new Runnable() {
			
			   int counter = 0;
			  // String name = Math.random() + "";
			@Override
			public void run() {
				//System.out.println("Running thread " + name);
				while(true) {
					try {
						//System.out.println("Running Thrread loop " + name);
						List<String> msgs = getMessagesAsArray(true, true);
						
						//System.out.println("Found " + msgs.size() + " parts");
						for(int i = 0; i < msgs.size(); i++) {
							String s = msgs.get(i);
							if(counter >= data.length) {
								latch.countDown();
								return;
							}
							data[counter++] = s;
							latch.countDown();
						}
						System.out.println("Current data " + Arrays.toString(data));
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(counter >= data.length) {
						latch.countDown();
						return;
					}
					//System.out.println("Counter is " + counter + " while we need " + data.length + " parts, latch is at " + latch.getCount());
				}
				
			}
		});
		t.start();
		return reply;
	}

	public String getMessages() {
		try {
			// return RestManager.sendPost(baseUrl + ACCOUNT_SID + "/Messages.json",
			// queries, params);
			return RestManager.sendGet(baseUrl + ACCOUNT_SID + "/Messages.json?PageSize=9999");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error D:";

	}

	public JSONObject getMessagesAsJSON() {
		try {
			return (JSONObject) new JSONParser().parse(getMessages());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getMessagesAsArray() {
		return getMessagesAsArray(true, true);
	}

	public List<String> getMessagesAsArray(boolean latest, boolean update) {
		JSONObject obj = getMessagesAsJSON();
		//System.out.println(obj.toString());
		JSONArray messages = (JSONArray) obj.get("messages");
		java.util.Iterator<JSONObject> it = messages.iterator();
		List<String> responces = new ArrayList<String>();

		while (it.hasNext()) {
			JSONObject line = it.next();
			//System.out.println(line.get("body").toString());
			//System.out.println(Arrays.toString(it.next().get("body").toString().split("Sent from your Twilio trial account - ")));
			try {
			responces.add(line.get("body").toString().split("Sent from your Twilio trial account - ")[1]);
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		int count = (responces.size());
		System.out.println("Responcse size: " + count);
		//System.out.println(responces.get(0));
		if (latest) {
			responces = copy(responces.subList(0, responces.size() - currentCount));
		}
		if (update) {
			currentCount = count;
		}
		System.out.println("Current size: " + currentCount);
		return responces;
	}
	
	public <T> List copy(List<T> og) { 
		List<T> list = new ArrayList<T>(og.size());
		for(T t : og) {
			list.add(t);
		}
		return list;
	}
}
