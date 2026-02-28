package me.scpl.java.games.mcplugins.cubebusiness.commands;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods.colourMsg;

public class ColourCodesCommand implements CommandExecutor {

    CubeBusiness plugin;

    public ColourCodesCommand(CubeBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(colourMsg(new String[] {
                    "Color Code:",
                    "&44 \\u00A74 \"DARK_RED\"",
                    "&cc \\u00A7c \"red\"",
                    "&66 \\u00A76 \"gold\"",
                    "&ee \\u00A7e \"yellow\"",
                    "&22 \\u00A72 \"dark_green\"",
                    "&aa \\u00A7a \"green\"",
                    "&bb \\u00A7b \"aqua\"",
                    "&33 \\u00A73 \"dark_aqua\"",
                    "&11 \\u00A71 \"dark_blue\"",
                    "&99 \\u00A79 \"blue\"",
                    "&dd \\u00A7d \"light_purple\"",
                    "&55 \\u00A75 \"dark_purple\"",
                    "&ff \\u00A7f \"white\"",
                    "&77 \\u00A77 \"gray\"",
                    "&88 \\u00A78 \"dark_gray\"",
                    "&00 \\u00A70 \"black\"",
                    "&rr \\u00A7r \"reset\" Reset color to default",
                    "l \\u00A7l &lBold",
                    "o \\u00A7o &oItalic",
                    "n \\u00A7n &nUnderline",
                    "m \\u00A7m &mStrike",
                    "k \\u00A7k &kRandom chars"
            }));
        }

        return true;
    }

}
