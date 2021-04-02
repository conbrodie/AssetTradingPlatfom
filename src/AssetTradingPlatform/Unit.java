package AssetTradingPlatform;

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
     * @throws UnitException Unit name already exists
     */
    public Unit(String unitName){

    }

    /**
     * Add User to Unit
     * @param userName User to add
     * @param unitName Unit to add to
     * Check if unit leader. (Does user have permission to perform action)
     * Can user be part of multiple units?
     * @throws UnitException Username doesnt exist
     * @throws UnitException Unitname doesnt exist
     * @throws UnitException User already in unit
     */
    public void AddUser(String userName, String unitName){

    }

    /**
     * Remove User from Unit
     * @param userName User to remove
     * @param unitName Unit to remove from
     * Check if unit leader. (Does user have permission to perform action)
     * @throws UnitException Username doesnt exist
     * @throws UnitException Username not part of unit
     * @throws UnitException Unitname doesnt exist
     */
    public void RemoveUser(String userName, String unitName){

    }

    /**
     * Edit unit credits
     * @param unitName Unit to remove from
     * Check if admin (Does user have permission to perform action)
     * @creditAmount Amount to update units credits
     * @see AdminUser In user class
     * @throws UnitException User doesnt have permission
     * @throws UnitException Unitname doesnt exist
     * @throws UnitException Cant set credits to negative
     */
    public void EditCredits(String unitName, float creditAmount){

    }


    /**
     * Edit unit assets
     * @param unitName Unit to remove from
     * Check if admin (Does user have permission to perform action)
     * @param asset Asset to modify
     * @param assetAmount Update quantitiy of asset to this amount
     * @see AdminUser In user class
     * @throws UnitException User doesnt have permission
     * @throws UnitException Unitname doesnt exist
     * @throws UnitException Cant set owned asset amount to negative
     * @throws UnitException Asset not owned by unit
     */
    public void EditAssets(String unitName, String asset, float assetAmount){

    }

}
