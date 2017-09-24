import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartHouse {

	private List<Device> devices;

	public SmartHouse() {
		this.devices = new ArrayList<Device>();
	}

	public boolean add(Device e) {
		return devices.add(e);
	}
	
	//id, msg
	public Map<String, Object> sendMessage(Map<String, Object> messages) {
		System.out.println("Sending Message to devices");
		Map<String, Object> replies = new HashMap<String, Object>();
		for(String id :messages.keySet()) {
			System.out.println("Attempting to send [" + messages.get(id) + "] to " + id);
			boolean found = false;
			for(Device d:devices) {
				if(d.getId().equals(id)) {
					replies.put(id, d.sendGet());
					found = true;
					break;
				}
			}
			if(!found) {
				System.err.println("Unable to find device: " + id + "\n");
				replies.put(id, "Could not find");
			}
		}
		return replies;
	}
	
	public static <K, V> void printMap(Map<K, V> map) {
		for(K key : map.keySet()) {
			System.out.println(key.toString() + " -> " + map.get(key).toString());
		}
	}
	
}
