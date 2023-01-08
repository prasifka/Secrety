package eu.hacraft.secrets.methods;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SecretCreator {
    public static File Cmd = new File("plugins/Secrets", "Secrets.yml");
    public static FileConfiguration cmdfile = YamlConfiguration.loadConfiguration(Cmd);

    public static void createSecret(String name) throws IOException {
        List<String> list = cmdfile.getStringList("Secrets");
        list.add(name);
        cmdfile.set("Secrets", list);
        cmdfile.save(Cmd);
    }
}
