package me.heldplayer.ObsidianServer;

import java.util.ArrayList;

public class NetServerManager {
	private ArrayList<NetServerChild> children;
	private Server server;

	public NetServerManager(Server server) {
		this.server = server;
	}
}
