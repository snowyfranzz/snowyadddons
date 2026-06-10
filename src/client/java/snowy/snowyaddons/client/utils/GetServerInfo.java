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
        if (!isInSkyBlock()) return false;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return false;

        Scoreboard scoreboard = mc.level.getScoreboard();
        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);

        if (objective != null) {
            for (net.minecraft.world.scores.PlayerScoreEntry entry : scoreboard.listPlayerScores(objective)) {
                String fullLineText = "";

                // check modern 1.21 entry display component first
                if (entry.display() != null) {
                    fullLineText = entry.display().getString();
                } else {
                    // fallback to legacy team mapping values if display wrapper is absent
                    String scoreHolder = entry.owner();
                    PlayerTeam team = scoreboard.getPlayersTeam(scoreHolder);
                    if (team != null) {
                        fullLineText = team.getPlayerPrefix().getString() + scoreHolder + team.getPlayerSuffix().getString();
                    } else {
                        fullLineText = scoreHolder;
                    }
                }

                String cleanLine = fullLineText.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();
                if (cleanLine.contains("CATACOMBS") || cleanLine.contains("CLEARED:")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getCurrentFloor() {
        if (!isInDungeon()) return null;

        Minecraft mc = Minecraft.getInstance();
        Scoreboard scoreboard = mc.level.getScoreboard();
        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);

        if (objective != null) {
            for (PlayerScoreEntry entry : scoreboard.listPlayerScores(objective)) {
                String fullLineText = "";

                if (entry.display() != null) {
                    fullLineText = entry.display().getString();
                } else {
                    String scoreHolder = entry.owner();
                    PlayerTeam team = scoreboard.getPlayersTeam(scoreHolder);
                    if (team != null) {
                        fullLineText = team.getPlayerPrefix().getString() + scoreHolder + team.getPlayerSuffix().getString();
                    } else {
                        fullLineText = scoreHolder;
                    }
                }

                String cleanLine = fullLineText.replaceAll("(?i)§[0-9a-fk-or]", "");

                // eg. -> ⏣ The Catacombs (M2)
                if (cleanLine.contains("The Catacombs (")) {
                    int start = cleanLine.indexOf("(") + 1;
                    int end = cleanLine.indexOf(")");
                    if (start > 0 && end > start) {
                        return cleanLine.substring(start, end).toUpperCase();
                    }
                }
            }
        }
        return null;
    }
}
