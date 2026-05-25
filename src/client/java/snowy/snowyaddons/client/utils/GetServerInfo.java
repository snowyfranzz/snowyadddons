package snowy.snowyaddons.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.*;

public class GetServerInfo {

    public static boolean isInSkyBlock() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return false;
        }

        Scoreboard scoreboard = mc.level.getScoreboard();

        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);

        if (objective != null) {
            String title = objective.getDisplayName().getString();
            String cleanTitle = title.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();

            return cleanTitle.contains("SKYBLOCK");
        }

        return false;
    }

    public static boolean isInDungeon() {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return false;
        }

        Scoreboard scoreboard = mc.level.getScoreboard();
        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);

        if (objective != null) {

            for (PlayerScoreEntry entry : scoreboard.listPlayerScores(objective)) {
                String scoreHolder = entry.owner();
                PlayerTeam team = scoreboard.getPlayersTeam(scoreHolder);

                String fullLineText = scoreHolder;

                if (team != null) {
                    fullLineText = team.getPlayerPrefix().getString() + scoreHolder + team.getPlayerSuffix().getString();
                }

                String cleanLine = fullLineText.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();

                if (cleanLine.contains("THE CATACOMBS")){
                    return true;
                }
            }
        }
        return false;
    }
}
