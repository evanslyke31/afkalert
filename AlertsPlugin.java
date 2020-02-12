/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Raqes <j.raqes@gmail.com>
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

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Prayer;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.alerts.*;
import net.runelite.client.plugins.alerts.AlertsConfig;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@PluginDescriptor(
        name = "Alerts",
        description = "Alerts player of critical conditions",
        enabledByDefault = false
)
public class AlertsPlugin extends Plugin
{

    private Instant startOfLastTick = Instant.now();

    @Getter(AccessLevel.PACKAGE)
    private boolean prayersActive = false;

    @Inject
    private Client client;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private SpriteManager spriteManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private AlertsOverlay alertOverlay;

    @Inject
    private net.runelite.client.plugins.alerts.AlertsConfig config;

    @Provides
    net.runelite.client.plugins.alerts.AlertsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AlertsConfig.class);
    }

    @Override
    protected void startUp()
    {
        overlayManager.add(alertOverlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(alertOverlay);
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {

    }



}
