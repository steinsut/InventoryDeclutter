package me.steinsut.inventorydeclutter.env;

public class BuildConfig implements IBuildConfig {
    public static final BuildConfig instance = new BuildConfig();

    @Override
    public TargetDistType getTargetDist() {
        return TargetDistType.FULL;
    }
}
