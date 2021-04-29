package AssetTradingPlatform.common;

import java.util.Map;

/**
 * A class to create and manage an OU (Organisational Unit)
 */
public class Unit {
    public int org_unit_id;
    public String org_unit_name;
    public int credits;
    /**
     * Temporary set to store a units assets and their amounts until connected to DB
     */
    public Map<String, Integer> org_assets = null;



    /**
     * Create a new Organisational Unit
     * New unit is created and saved to database
     * @param org_unit_name New unit name
     * @throws Exception Unit name already exists
     */
    public Unit(String org_unit_name, int credits) throws Exception{

    }


}
