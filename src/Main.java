import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {

		
		

		
		
		
		
		//System.exit(1);
		
		
		
		
		TwillioManager manager = TwillioManager.getInstance();
		
		SmartHouse house = new SmartHouse();
		house.add(new Device("lights"));
		house.add(new Device("fan"));
		house.add(new Device("coffeeMachine"));
		house.add(new Device("auto"));
	
		ThreadedResponse t = manager.waitForData(4);
		
		
		System.out.println("Received message: " + Arrays.toString(t.getData()));
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("lights", t.getData()[0]);
		message.put("fan", t.getData()[1]);
		message.put("coffee machine", t.getData()[2]);
		message.put("auto", t.getData()[3]);
		Map<String, Object> reply =  house.sendMessage(message);
		System.out.println("Reply: ");
		SmartHouse.printMap(reply);
		//Ai.getInstance().push("datayay");

		
	}

}
