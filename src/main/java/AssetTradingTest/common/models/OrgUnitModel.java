package AssetTradingTest.common.models;

/**
 * Used to provide a model for the data contained in a OrgUnit table record
 */
public class OrgUnitModel {
    private int org_unit_id;
    private String org_unit_name;
    private int credits;

    public OrgUnitModel() { }

    public OrgUnitModel (String org_unit_name, int credits) {
        this.org_unit_name = org_unit_name;
        this.credits = credits;
    }

    public OrgUnitModel(int org_unit_id, String org_unit_name, int credits) {
        this.org_unit_id = org_unit_id;
        this.org_unit_name = org_unit_name;
        this.credits = credits;
    }

    public int getOrg_unit_id() {
        return org_unit_id;
    }

    public void setOrg_unit_id(int org_unit_id) {
        this.org_unit_id = org_unit_id;
    }

    public String getOrg_unit_name() {
        return org_unit_name;
    }

    public void setOrg_unit_name(String org_unit_name) {
        this.org_unit_name = org_unit_name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "[" +
                "org_unit_id=" + org_unit_id +
                ", org_unit_name='" + org_unit_name + '\'' +
                ", credits=" + credits +
                ']';
    }
}
