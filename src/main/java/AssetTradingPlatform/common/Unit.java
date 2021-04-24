package AssetTradingPlatform.common;

import java.util.TreeMap;
import java.util.TreeSet;

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
    public TreeMap org_assets;


    /**
     * Create a new Organisational Unit
     * New unit is created and saved to database
     * @param org_unit_name New unit name
     * @throws Exception Unit name already exists
     */
    public Unit(String org_unit_name) throws Exception{

    }


    /**
     * Add User to Unit
     * @param username User to add
     * @param org_unit_name Unit to add to
     * Check if unit leader. (Does user have permission to perform action)
     * Can user be part of multiple units?
     * @throws Exception username doesn't exist
     * @throws Exception org_unit_name doesn't exist
     * @throws Exception username already in unit
     */
    public void addUser(String username, String org_unit_name) throws Exception{

    }


    /**
     * Remove User from Unit
     * @param username User to remove
     * @param org_unit_name Unit to remove from
     * Check if unit leader. (Does user have permission to perform action)
     * @throws Exception username doesn't exist
     * @throws Exception username not part of unit
     * @throws Exception org_unit_name doesn't exist
     */
    public void removeUser(String username, String org_unit_name) throws Exception{

    }


    /**
     * Edit unit credits
     * @param org_unit_name Unit to remove from
     * @param credits Amount to update units credits
     * @see AssetTradingPlatform.server.UserController#adminUser Called to check that user performing edit has required admin privlidges
     * @throws Exception username doesn't have permission
     * @throws Exception org_unit_name doesn't exist
     * @throws Exception Can't set credits to negative
     */
    public void editCredits(String org_unit_name, float credits) throws Exception{

    }


    /**
     * Edit unit assets
     * @param org_unit_name Unit to remove from
     * @param asset Asset to modify
     * @param assetAmount Update quantity of asset to this amount
     * @see AssetTradingPlatform.server.UserController#adminUser Called to check that user performing edit has required admin privlidges
     * @throws Exception User doesn't have permission
     * @throws Exception org_unit_name doesn't exist
     * @throws Exception Cant set owned asset amount to negative
     * @throws Exception Asset not owned by unit
     */
    public void EditAssets(String org_unit_name, String asset, int assetAmount) throws Exception{

    }
}
