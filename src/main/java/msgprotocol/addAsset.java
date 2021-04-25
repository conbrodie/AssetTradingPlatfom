package msgprotocol;

public class addAsset implements sqlQuery {
    /** Adding an asset to the DB
     *  requires a new asset name
     */
    String asset_name;

    public addAsset(String asset_name){ this.asset_name = asset_name;}

    @Override
    public String SQL_query() {
        return "INSERT INTO asset (asset_name)"
                + "VALUES ("+asset_name+")";
    }
}
