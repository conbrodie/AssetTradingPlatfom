package server.dal;

public class AccountTypeDbMock implements AccountTypeDb {
    private String result;

    public AccountTypeDbMock(String result) {
        this.result = result;
    }

    @Override
    public String getAccounts() {
        return result;
    }
}
