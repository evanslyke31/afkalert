package net.runelite.client.alerts;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.StringUtils;
import net.runelite.client.plugins.regenmeter.*;

class AlertsOverlay extends Overlay
{
    private static final Color PRAYER_COLOR = new Color(50, 200, 200, 170);
    private static final Color HP_COLOR = new Color(255, 0, 0, 170);
    private static final Color OVERLOAD_COLOR = new Color(255, 0, 0, 170);
    private static final Color BOTH_COLOR = new Color(255, 255, 255, 170);

    private final Client client;
    private final net.runelite.client.plugins.alerts.AlertsConfig config;
    private final TooltipManager tooltipManager;
    private Instant startOfLastTick = Instant.now();
    private boolean trackTick = true;

    private boolean flick = false;

    @Inject
    private AlertsOverlay(final Client client, final TooltipManager tooltipManager, final AlertsConfig config)
    {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        final Widget xpOrb = client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
        final Rectangle bounds = xpOrb.getBounds();

        final int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
        final int maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
        final int currentHP = client.getBoostedSkillLevel(Skill.HITPOINTS);

        if(currentPrayer < 15 || currentHP < 15 || (currentHP > 50 && currentHP < 54)) {
            if(config.HPAlert() && !config.PrayerAlert())
                renderBar(graphics, 0, 0, 1920, 1080, HP_COLOR);
            else if(!config.HPAlert() && config.PrayerAlert())
                renderBar(graphics, 0, 0, 1920, 1080, PRAYER_COLOR);
            else if((config.HPAlert() && config.PrayerAlert()) || (config.OverloadAlert() && currentHP > 50 && currentHP < 54))
                renderBar(graphics, 0, 0, 1920, 1080, BOTH_COLOR);
        }

        return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
    }


    boolean toggle = false;
    private void renderBar(Graphics2D graphics, int x, int y, int width, int height, Color c) {

        toggle = !toggle;

        if(toggle) {
            graphics.setColor(c);
            graphics.drawRect(x, y, width, height);
            graphics.fillRect(x, y, width, height);
        }
    }
}
