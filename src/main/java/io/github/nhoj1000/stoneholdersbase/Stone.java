package io.github.nhoj1000.stoneholdersbase;

import java.util.ArrayList;
import java.util.List;

public interface Stone {
    List<Power> powers = new ArrayList<>();

    void registerPower();
    String toString();
}
