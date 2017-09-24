import java.util.concurrent.CountDownLatch;

public class ThreadedResponse {

	private CountDownLatch latch;
	private Object[] data;
	
	public ThreadedResponse(CountDownLatch latch, Object[] data) {
		this.latch = latch;
		this.data = data;
	}
	


	public CountDownLatch getLatch() {
		return latch;
	}

	public Object[] getData() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	
	
	
}
