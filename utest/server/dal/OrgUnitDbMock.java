package server.dal;

public class OrgUnitDbMock implements OrgUnitDb {

    private String getOrgUnitsReturn;
    private Boolean createOrgUnitReturn;
    private Boolean updateOrgUnitReturn;

    public OrgUnitDbMock(String getOrgUnitsReturn, Boolean createOrgUnitReturn, Boolean updateOrgUnitReturn) {
        this.getOrgUnitsReturn = getOrgUnitsReturn;
        this.createOrgUnitReturn = createOrgUnitReturn;
        this.updateOrgUnitReturn = updateOrgUnitReturn;
    }

    @Override
    public String getOrgUnits() {
        return getOrgUnitsReturn;
    }

    @Override
    public boolean createOrgUnit(String org_unit_name, int credits) {
        return createOrgUnitReturn;
    }

    @Override
    public boolean updateOrgUnit(int org_unit_id, String org_unit_name, int credits) {
        return updateOrgUnitReturn;
    }
}
