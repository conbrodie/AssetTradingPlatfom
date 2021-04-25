package msgprotocol;

public class moduser implements sqlQuery {

    String username;
    String password;

    public moduser(String username, String password){
        this.username = username;
        this.password = password;

    }

    @Override
    public String SQL_query() {
        return "UPDATE user "
                +"SET password ="+password+""
                +"WHERE username ='"+username+"'";

    }
}