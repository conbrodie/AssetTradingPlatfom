package msgprotocol;

public class addOrgUnit implements sqlQuery {
    /**
     * Add Org unit - OU name, credits amount
     *
     */

    String org_unit_name;
    int credits;

    public addOrgUnit(String org_unit_name, int credits){
        this.org_unit_name = org_unit_name;
        this.credits = credits;
    }

    @Override
    public String SQL_query() {
        return "INSERT INTO org_unit (org_unit_name, credits)"
                + "VALUES ("+org_unit_name+", "+credits+")";
    }
}
