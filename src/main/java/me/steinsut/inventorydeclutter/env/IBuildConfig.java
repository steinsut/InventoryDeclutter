package me.steinsut.inventorydeclutter.env;

public interface IBuildConfig {
    enum TargetDistType {
        UNKNOWN,
        FULL,
        CLIENT_ONLY
    }

    TargetDistType getTargetDist();
}
