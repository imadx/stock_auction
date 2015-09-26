class Test{
	public static void main(String args[]){
		System.out.println("Test print");
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true)
				try{

				System.out.println(new java.util.Date());
				Thread.sleep(1000);
				} catch (InterruptedException e){
					System.out.println(e);
				}
			}
		});
		thread.start();
	}
}