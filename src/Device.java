import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Device {

	
	private String id;
	private static final String url = "http://localhost:3304/devices/";

	/**
	 * @param id
	 */
	public Device(String id) {
		this.id = id;
	}
	
	public String sendGet() {
		Ai.getInstance().addUsage(id, "CHANGE");
		try {
			//return RestManager.sendGet(url + id);
			return "TODO un comment ^";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

	public String getId() {
		return id;
	}
	
	
	
	
	
}
