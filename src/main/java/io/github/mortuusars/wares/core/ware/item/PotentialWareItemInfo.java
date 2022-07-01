package io.github.mortuusars.wares.core.ware.item;

import com.google.common.base.Preconditions;

import java.util.Random;

public class PotentialWareItemInfo extends WareItemInfo<PotentialWareItemInfo> {

    public double chance = 1f;
    public int countMin = 1;
    public int countMax = 1;

    public FixedWareItemInfo toFixed(){
        return new FixedWareItemInfo()
                .item(this.item)
                .tag(this.tag)
                .nbt(this.nbt)
                .ignoresNbt(this.ignoreNbt)
                .ignoresDamage(this.ignoreDamage)
                .count(getCount());
    }

    @Override
    public int getCount() {
        return new Random().nextInt(countMin, ++countMax);
    }

    public int getCount(Random random) {
        return random.nextInt(countMin, ++countMax);
    }

    /**
     * Determines whether the roll succeeded.<br>
     * Rolls from 0.0 to 1.0.
     * @return true if item was chosen.
     */
    public boolean rollForChance(Random random){
        return random.nextDouble() < chance;
    }

    public PotentialWareItemInfo chance(double chance){
        this.chance = chance;
        return this;
    }

    public PotentialWareItemInfo count(int count){
        Preconditions.checkArgument(count > 0, "count should be larger than 0. Value: {}", count);
        this.countMin = count;
        this.countMax = count;
        return this;
    }

    public PotentialWareItemInfo countRange(int min, int max){
        Preconditions.checkArgument(min > 0 && max > 0, "count min and max should be larger than 0. Value: min - {}, max - {}", min, max);
        Preconditions.checkArgument(min <= max, "min should be smaller or equal to max. Value: min - {}, max - {}", min, max);
        this.countMin = min;
        this.countMax = max;
        return this;
    }

    @Override
    public String toString() {
        return "PotentialWareItemInfo{" +
                "item='" + item + '\'' +
                ", tag='" + tag + '\'' +
                ", nbt='" + nbt + '\'' +
                ", chance=" + chance +
                ", countMin=" + countMin +
                ", countMax=" + countMax +
                ", ignoreNbt=" + ignoreNbt +
                ", ignoreDamage=" + ignoreDamage +
                '}';
    }
}
