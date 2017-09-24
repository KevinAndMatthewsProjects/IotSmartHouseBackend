import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import com.nexosis.impl.NexosisClient;
import com.nexosis.impl.NexosisClientException;
import com.nexosis.model.Columns;
import com.nexosis.model.DataRole;
import com.nexosis.model.DataSetData;
import com.nexosis.model.DataSetList;
import com.nexosis.model.DataType;
import com.nexosis.model.ResultInterval;
import com.nexosis.model.SessionResponse;
import com.nexosis.model.SessionResult;
import com.nexosis.model.SessionStatus;
public class Ai {

	private NexosisClient client;
	private final String API_KEY = "1ce89ee08da34daf9c5901b141ac231d";

	private List<Map<String, String>> data = new ArrayList<>();

	private static Ai ai;
	private static DateTime startDate = new DateTime(new Date(2017, 9, 22));
	private static DateTime endDate = new DateTime(new Date(2017, 9, 24));

	public static Ai getInstance() {
		
		if (ai == null) {
			System.out.println("Creating instance of Ai");
			ai = new Ai();
		}
		return ai;

	}

	private Ai() {
		//client = new NexosisClient();//API_KEY, "https://ml.nexosis.com/v1/");
	}

	public void addUsage(String device, String value) {

		Map<String, String> row = new LinkedHashMap<String, String>();
		Date d = new Date();
		row.put("timestamp", d.toString());
		row.put("device", device);

		data.add(row);

		printData();

	}

	public void forcast() {
		System.out.println("Starting a forcast session");
		try {
			SessionResponse response = client.getSessions().createForecast("OtherDataSet", "device", startDate, endDate,
					ResultInterval.HOUR);
			SessionResult results = new SessionResult();
			System.out.print("Loading");
			while (results.getStatus() != SessionStatus.COMPLETED) {
				System.out.print(".");
				try {
					Thread.sleep(2000);
					results = client.getSessions().getResults(response.getSessionId());
				} catch (InterruptedException | NexosisClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			System.out.println("\nDone!");
			
			printData(results.getData());
			

		} catch (NexosisClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public DataSetList pullData(String name) {
		try {
			return client.getDataSets().list();
		} catch (NexosisClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void push(String name) {
		 HttpClient httpclient = HttpClients.createDefault();

	        try
	        {
	            URIBuilder builder = new URIBuilder("https://ml.nexosis.com/v1/data/" + name);


	            URI uri = builder.build();
	            HttpPut request = new HttpPut(uri);
	            request.setHeader("Content-Type", "application/json");
	            request.setHeader("api-key", API_KEY);
	            

	            // Request body
	            StringEntity reqEntity = new StringEntity("{body}");
	            request.setEntity(reqEntity);
	            System.out.println(request.toString());
	            HttpResponse response = httpclient.execute(request);
	            HttpEntity entity = response.getEntity();

	            if (entity != null)
	            {
	                System.out.println(EntityUtils.toString(entity));
	            }
	        }
	        catch (Exception e)
	        {
	            System.out.println(e.getMessage());
	        }
	}

	public void pushData(String name) {
		DataSetData set = new DataSetData();
		set.setData(data);
		Columns cols = new Columns();
		cols.setColumnMetadata("timestamp", DataType.DATE, DataRole.TIMESTAMP);
		cols.setColumnMetadata("device", DataType.STRING, DataRole.TARGET);
		// Add the Columns Metadata to the DataSet
		set.setColumns(cols);

		// Send the dataset to the API endpoint
		try {
			
			client.getDataSets().create(name, set);
		} catch (NexosisClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printData(List<Map<String, String>> data) {
		System.out.println("\nCurrent Table:");
		for (Map<String, String> m : data) {
			System.out.println(m.values().toString());
		}
		System.out.println();
	}

	public void printData() {

		printData(data);
	}

}
