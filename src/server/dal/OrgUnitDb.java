package server.dal;

public interface OrgUnitDb {
    String getOrgUnits();
    boolean createOrgUnit(String org_unit_name,int credits);
    boolean updateOrgUnit(int org_unit_id, String org_unit_name,int credits);
}
