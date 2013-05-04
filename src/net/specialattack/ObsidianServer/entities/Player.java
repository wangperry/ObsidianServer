
package net.specialattack.ObsidianServer.entities;

import net.specialattack.ObsidianServer.item.ItemStack;
import net.specialattack.ObsidianServer.network.NetServerChild;

public class Player {
    public final NetServerChild child;

    public int playerSlot = 0;
    public int hairStyle = 0;
    public int gender = 0;
    public int[] hairColor = new int[3];
    public int[] skinColor = new int[3];
    public int[] eyeColor = new int[3];
    public int[] shirtColor = new int[3];
    public int[] undershirtColor = new int[3];
    public int[] pantsColor = new int[3];
    public int[] shoeColor = new int[3];
    public int difficulty = 0;
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
