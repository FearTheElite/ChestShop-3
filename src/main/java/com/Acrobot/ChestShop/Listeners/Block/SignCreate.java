package com.Acrobot.ChestShop.Listeners.Block;

import com.Acrobot.Breeze.Utils.BlockUtil;
import com.Acrobot.ChestShop.ChestShop;
import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.Acrobot.ChestShop.Utils.uBlock;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author Acrobot
 */
public class SignCreate implements Listener {

    @EventHandler
    public static void onSignChange(SignChangeEvent event) {
        Block signBlock = event.getBlock();
        String[] line = event.getLines();
		
		line[0] = ChatColor.stripColor(line[0]);
		line[1] = ChatColor.stripColor(line[1]);
		line[2] = ChatColor.stripColor(line[2]);
		line[3] = ChatColor.stripColor(line[3]);

        if (!BlockUtil.isSign(signBlock)) {
            return;
        }

        if (!ChestShopSign.isValidPreparedSign(line)) {
            return;
        }

        PreShopCreationEvent preEvent = new PreShopCreationEvent(event.getPlayer(), (Sign) signBlock.getState(), line);
        ChestShop.callEvent(preEvent);

        if (preEvent.isCancelled()) {
            return;
        }

        for (byte i = 0; i < event.getLines().length; ++i) {
            event.setLine(i, ChatColor.stripColor(preEvent.getSignLine(i)));
        }

        ShopCreatedEvent postEvent = new ShopCreatedEvent(preEvent.getPlayer(), preEvent.getSign(), uBlock.findConnectedChest(preEvent.getSign()), preEvent.getSignLines());
        ChestShop.callEvent(postEvent);
    }
}
