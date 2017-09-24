import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		//Device d = new Device("1");
		//System.out.println(d.sendGet());
	//	//Thread.sleep(1000);
		//System.out.println(d.sendGet());
		//System.out.println(d.sendGet());
		//Ai.getInstance().pushData("data");
		
		TwillioManager manager = TwillioManager.getInstance();
		//System.out.println(manager.getMessagesAsArray(false, true));
		
		SmartHouse house = new SmartHouse();
		house.add(new Device("lights"));
	
		ThreadedResponse t = manager.waitForData(2);

		
		System.out.println("Received message: " + Arrays.toString(t.getData()));
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("lights", t.getData()[0]);
		message.put("toaster", t.getData()[1]);
		Map<String, Object> reply =  house.sendMessage(message);
		System.out.println("Reply: ");
		SmartHouse.printMap(reply);
		Ai.getInstance().push("datayay");

		
	}

}
