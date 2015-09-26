import java.util.*;

class Text_StockStatDisplay implements StockStatDisplay{
	private StockDatabase stockStats;
	private Map currentStats;
	private String outputString;
	public Text_StockStatDisplay(){
		stockStats = new StockDatabase("availableStocks.csv","Symbol", "Price");
		streamData();
	}
	public void streamData(){
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true)
				try{
					getData();
					outputData();
					System.out.println(outputString);
					Thread.sleep(displayInterval);
				} catch (InterruptedException e){
					System.out.println(e);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		thread.start();
	}
	public void getData(){
		currentStats = stockStats.getStockStats();
	}
	public void outputData(){
		Set keys = currentStats.keySet();

	    StringBuilder sb = new StringBuilder((new java.util.Date()).toString());
	    sb.append("\r\n");
		for (Iterator i = keys.iterator(); i.hasNext();){
			String key = (String) i.next();
			sb.append("-- ");
			sb.append(key);
			sb.append(" : ");
			sb.append(currentStats.get(key).toString());
			// 
		}
		outputString = sb.toString();
	}
}