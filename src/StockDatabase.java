import java.io.*;
import java.util.*;

class StockDatabase { 

    private static Map<String, Float> stockList; 
    private String [] fields; 
    private static boolean theFirstStart = true;

    public StockDatabase(String csvFile, String key, String val)  {
		FileReader fileRd=null; 
		BufferedReader reader=null; 


		if(theFirstStart){
			theFirstStart = false;
			try { 
			    fileRd = new FileReader(csvFile); 
			    reader = new BufferedReader(fileRd); 

			    /* read the CSV file's first line which has 
			     * the names of fields. 
			     */
			    String header = reader.readLine(); 
			    fields = header.split(",");// keep field names 

			    // find where the key and the value are 
			    int keyIndex = findIndexOf(key); 
			    int valIndex = findIndexOf(val); 

			    if(keyIndex == -1 || valIndex == -1) 
				throw new IOException("CSV file does not have data"); 
			    // note how you can throw a new exception 

			    // get a new hash map
			    stockList = new HashMap<String, Float>(); 

			    /* read each line, getting it split by , 
			     * use the indexes to get the key and value 
			     */
			    String [] tokens; 
			    for(String line = reader.readLine(); 
				line != null; 
				line = reader.readLine()) {
					tokens = line.split(","); 
					stockList.put(tokens[keyIndex], Float.parseFloat(tokens[valIndex])); 
			    }
			    
			    if(fileRd != null) fileRd.close();
			    if(reader != null) reader.close();
			    
			    // I can catch more than one exceptions 
			} catch (IOException e) { 
			    System.out.println(e);
			    System.exit(-1); 
			} catch (ArrayIndexOutOfBoundsException e) { 
			    System.out.println("Malformed CSV file");
			    System.out.println(e);
			}
		}
    }

    private int findIndexOf(String key) { 
	for(int i=0; i < fields.length; i++) 
	    if(fields[i].equals(key)) return i; 
	return -1; 
    }
	
    // public interface 
    public Float getPrice(String key) { 
    	Float price;
    	try{
    		price = stockList.get(key);
    	} catch (Exception e){
    		price = -1f;
    	}
    	return price;
    }
    public synchronized void setPrice(String key, float price){
    	stockList.put(key, price);
    }
    public synchronized void setPrice(String key, String price){
    	Float f_price;
    	try{
    		f_price = Float.parseFloat(price);	
    	} catch (Exception e){
    		f_price = stockList.get(key);
    	}
    	stockList.put(key, f_price);
    }
    public String availableStockList(){
	    Set keys = stockList.keySet();
	    StringBuilder sb = new StringBuilder("Available Stock Symbols are: \r\n");
		for (Iterator i = keys.iterator(); i.hasNext();){
			String key = (String) i.next();
			sb.append("-- ");
			sb.append(key);
			sb.append("\r\n");
			// String value = (String) map.get(key);
		}
		return sb.toString();
    }
    public Map getStockStats(){
	    return stockList;
    }


}