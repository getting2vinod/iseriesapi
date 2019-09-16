package as400pwdrest.aesuser;
import com.ibm.as400.access.*;
import com.ibm.as400.security.*;

import java.sql.Struct;

public class Auser {
    private AS400 as400;
    private User cxtuser;
    private String server;

    public String getProfileName() {
        return profileName;
    }



    public void setProfileName(String profileName) {
        //check if the profile exists
        try {
            cxtuser = new User(as400,profileName);
            if(cxtuser.exists()){
                System.out.println("User Exists");
            }
            else{
                cxtuser.setName(profileName);
                UserList ul = new UserList();
                //System.out.println("User Object Created");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }




        this.profileName = profileName;
    }

    //    EmpFname         Character format with a length of 15 – This is the Employee first name
//        EmpMinit            Character format with a length of 1 – This is the Employee middle initial (it can be null)
//        EmpLname         Character format with a length of 20 – This is the Employee Last name (surname)
//        Fromtmplt          Character format with a length of 1 – This is whether or not to create the profile from a template. It expects a value of Y or N (it can be null).
//        PrfTmplt              Character format with a length of 10 – This is the template profile or the profile name that it should be copied from. This is a required field so it cannot be null.
//        EmpNetID           Character format with a length of 13 – This is the employee network ID (AD). This is optional so it can be null.
//        EmpID                   Character format with a length of 6 – This is the employee ID number assigned by HR. This is optional so it can be null.
//        EmpLoc                Character format with a length of 20 – This is the employee primary location. This is optional so it can be null.
//        EmpMngr            Character format with a length of 36 – This is the employee manager. This is optional so it can be null. It’s expecting this in full name format (First name, Middle initial, last name) but will accepted any value that’s 35 characters of less.
    public String createAccount(String EmpFname, String EmpMinit, String  EmpLname, String Fromtmplt, String PrfTmplt, String EmpNetID, String EmpID, String EmpLoc, String EmpMngr){
        String retMsg = "";
        try {
            retMsg = "Account created";
        }catch (Exception e){
            System.out.println(e.getMessage());
            retMsg = e.getMessage();
        }
        return (retMsg);
    }

    private String profileName;

    public Auser(String server,String username, String password) {
        as400 = new AS400(server,username,password);
        try {
            as400.connectService(AS400.CENTRAL);
            System.out.println("Connected:"+as400.isConnected());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
    public void close(){
        System.out.println("Disconnecting Service");
        as400.disconnectAllServices();
        System.out.println("Connected:"+as400.isConnected());
    }



}
