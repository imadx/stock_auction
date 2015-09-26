class StartServer{
	// Starts the server - The entry point

	public static void main(String args[]){
		int BASE_PORT = 2000;	// set BASE_PORT to 2000 at start
		System.out.println("Initiating Auction Server on port " + BASE_PORT + "...");

		if(args.length>0){
			if(args[0].equals("text")){
				Text_StockStatDisplay txtDisp = new Text_StockStatDisplay();
			}
			else if(args[0].equals("none")){// or whatever
				// do nothing
			}
		} else {
			new Thread() {
	            @Override
	            public void run() {
	                javafx.application.Application.launch(Visual_StockStatDisplay.class);
	            }
	        }.start();
		}
		try{
			AuctionServer auctionServer = new AuctionServer(BASE_PORT);
		} catch (Exception e){
			System.out.println(e);
		}
		
	}
}