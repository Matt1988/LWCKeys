package me.Matt1988.lwckeys;




import com.griefcraft.lwc.LWCPlugin;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class LWCKeysServerListener extends ServerListener {

    private LWCKeys plugin;

    public LWCKeysServerListener(LWCKeys plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (plugin.isInitialized()) {
            return;
        }

        Plugin plugin = event.getPlugin();

        if (plugin instanceof LWCPlugin) {
            this.plugin.init();
        }
    }

}
