package Item_Classes;

import Character_Classes.PlayableChar;

/**
 * Represents a permanent stat upgrade item (e.g., Stewshine, Mikan Mochi, Kurosawa Matcha).
 * Directly modifies character stats upon application.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class UpgradeItem extends Item {
    /** The specific character stat targeted for modification. */
    private String targetStat;

    /** The numerical value added or subtracted from the targeted stat. */
    private float upgradeAmt;

    /**
     * Constructs an UpgradeItem with a specified name, purchase price, target stat, and upgrade magnitude.
     *
     * @param name the descriptive name of the item
     * @param price the monetary gold cost of the item
     * @param targetStat the attribute stat targeted by this upgrade
     * @param upgradeAmt the magnitude of stat adjustment applied by this item
     */
    public UpgradeItem(String name, int price, String targetStat, float upgradeAmt) {
        super(name, price);
        this.targetStat = targetStat;
        this.upgradeAmt = upgradeAmt;
    }

    /**
     * Retrieves the target stat attribute string.
     *
     * @return the target stat name
     */
    public String getTargetStat() {
        return this.targetStat;
    }

    /**
     * Retrieves the numerical value of the stat upgrade.
     *
     * @return the magnitude of the upgrade amount
     */
    public float getUpgradeAmt() {
        return this.upgradeAmt;
    }

    /**
     * Applies the stat boost permanently to the specified player character.
     *
     * @param Yohane the target playable character receiving the upgrade
     */
    public void applyUpgrade(PlayableChar Yohane) {
        if (Yohane != null) {
            if ("Max Health".equalsIgnoreCase(this.targetStat)) {
                Yohane.setMaxHealth(Yohane.getMaxHealth() + this.upgradeAmt);
                Yohane.heal(this.upgradeAmt);
            } else if ("Attack".equalsIgnoreCase(this.targetStat)) {
                Yohane.setAttack(Yohane.getAttack() + this.upgradeAmt);
            }
        }
    }

    /**
     * Reverts the applied stat boost from the specified player character.
     *
     * @param Yohane the target playable character losing the upgrade
     */
    public void unapplyUpgrade(PlayableChar Yohane) {
        if (Yohane != null) {
            if ("Max Health".equalsIgnoreCase(this.targetStat)) {
                Yohane.setMaxHealth(Yohane.getMaxHealth() - this.upgradeAmt);
            } else if ("Attack".equalsIgnoreCase(this.targetStat)) {
                Yohane.setAttack(Yohane.getAttack() - this.upgradeAmt);
            }
        }
    }

    /**
     * Overrides the base use execution. Stat upgrades take effect automatically 
     * upon purchase via applyUpgrade rather than manual inventory usage.
     *
     * @param player the playable character interacting with the item
     * @return false always, as upgrades are applied immediately upon store purchase
     */
    @Override
    public boolean use(PlayableChar player) {
        return false;
    }
}