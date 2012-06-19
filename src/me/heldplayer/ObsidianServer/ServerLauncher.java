package me.heldplayer.ObsidianServer;

public class ServerLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server instance = new Server();
		Thread serverThread = new Thread(instance, "Main server thread");
		serverThread.setDaemon(true);
		serverThread.start();

		while (instance.isRunning() && serverThread.isAlive()) {
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				System.out.println("Error while sleeping!");
				e.printStackTrace();
			}
		}

		if (instance.isRunning()) {
			instance.shutdown();
		}

		System.out.println("Server stopped!");
	}

}
