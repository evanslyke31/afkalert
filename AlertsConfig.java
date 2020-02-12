package net.runelite.client.alerts;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("alerts")
public interface AlertsConfig extends Config
{
    @ConfigItem(
            keyName = "HPAlert",
            name = "Alert if low HP",
            description = "Screen will flash if hp is low."
    )
    default boolean HPAlert()
    {
        return false;
    }

    @ConfigItem(
            keyName = "PrayerAlert",
            name = "Alert if low Prayer",
            description = "Screen will flash if prayer is low."
    )
    default boolean PrayerAlert()
    {
        return false;
    }

    @ConfigItem(
            keyName = "NMZAlert",
            name = "Alert if Overload is expiring or HP regen",
            description = "Screen will flash if overload is about to expire or hp regen."
    )
    default boolean OverloadAlert()
    {
        return false;
    }
}
