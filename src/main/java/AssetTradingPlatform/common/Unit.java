package AssetTradingPlatform.common;

import java.util.TreeSet;

/**
 * A class to manage an OU (Organisational Unit) details and functions related to OUs
 */
public class Unit {
    public String unitName;
    public float credits;
    public TreeSet ownedAssets;


    /**
     * Create a new Organisational Unit
     * A new unit is created and saved to a database of units
     * @param unitName New unit name
     * @throws Exception Unit name already exists
     */
    public Unit(String unitName) throws Exception{

    }


    /**
     * Add User to Unit
     * @param userName User to add
     * @param unitName Unit to add to
     * Check if unit leader. (Does user have permission to perform action)
     * Can user be part of multiple units?
     * @throws Exception Username doesnt exist
     * @throws Exception Unitname doesnt exist
     * @throws Exception User already in unit
     */
    public void addUser(String userName, String unitName) throws Exception{

    }


    /**
     * Remove User from Unit
     * @param userName User to remove
     * @param unitName Unit to remove from
     * Check if unit leader. (Does user have permission to perform action)
     * @throws Exception Username doesnt exist
     * @throws Exception Username not part of unit
     * @throws Exception Unitname doesnt exist
     */
    public void removeUser(String userName, String unitName) throws Exception{

    }


    /**
     * Edit unit credits
     * @param unitName Unit to remove from
     * Check if admin (Does user have permission to perform action)
     * @creditAmount Amount to update units credits
     * @see User#AdminUser Called to check that user performing edit has required admin privlidges
     * @throws Exception User doesnt have permission
     * @throws Exception Unitname doesnt exist
     * @throws Exception Cant set credits to negative
     */
    public void editCredits(String unitName, float creditAmount) throws Exception{

    }


    /**
     * Edit unit assets
     * @param unitName Unit to remove from
     * Check if admin (Does user have permission to perform action)
     * @param asset Asset to modify
     * @param assetAmount Update quantity of asset to this amount
     * @see User#AdminUser Called to check that user performing edit has required admin privlidges
     * @throws Exception User doesn't have permission
     * @throws Exception Unit name doesn't exist
     * @throws Exception Cant set owned asset amount to negative
     * @throws Exception Asset not owned by unit
     */
    public void EditAssets(String unitName, String asset, float assetAmount) throws Exception{

    }
}
