package AssetTradingPlatform.common;

import java.util.Map;
import java.util.TreeMap;

/**
 * A class to create and manage an OU (Organisational Unit)
 */
public class Unit {
    private int org_unit_id;
    private String org_unit_name;
    private int credits;
    /**
     * Temporary set to store a units assets and their amounts until connected to DB
     */
    private Map<String, Integer> org_assets = new TreeMap<>();

    //Temporary id counter to increment
    private int counter_id = 0;

    /**
     * Create a new Organisational Unit
     * New unit is created and saved to database
     * @param name New unit name
     * @throws RuntimeException Unit name already exists
     */
    public Unit(String name, int credits) throws RuntimeException{
        org_unit_name = name;
        org_unit_id = counter_id + 1;
        counter_id += 1;
        this.credits = credits;
    }

    public int getOrgUnitId() {
        return org_unit_id;
    }

    public String getOrgUnitName() {
        return org_unit_name;
    }

    public int getCredits() {
        return credits;
    }

    public Map<String, Integer> getOrgAssets() {
        return org_assets;
    }
}
