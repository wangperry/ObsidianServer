package me.heldplayer.ObsidianServer.entities;

import me.heldplayer.ObsidianServer.ItemStack;
import me.heldplayer.ObsidianServer.NetServerChild;

public class Player {
	public final NetServerChild child;

	public byte playerSlot = 0;
	public byte hairStyle = 0;
	public byte gender = 0;
	public byte[] hairColor = new byte[3];
	public byte[] skinColor = new byte[3];
	public byte[] eyeColor = new byte[3];
	public byte[] shirtColor = new byte[3];
	public byte[] undershirtColor = new byte[3];
	public byte[] pantsColor = new byte[3];
	public byte[] shoeColor = new byte[3];
	public byte difficulty = 0;
	public String playerName = "";

	public short health = 100;
	public short maxHealth = 100;

	public short mana = 0;
	public short maxMana = 0;
	
	public ItemStack[] inventory;

	public Player(NetServerChild child) {
		this.child = child;
		
		this.inventory = new ItemStack[60];
	}
}
