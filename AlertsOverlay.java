/*
 * Copyright (c) 2018, Ethan <https://github.com/shmeeps>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
