package as400pwdrest.aesuser;
import com.ibm.as400.access.*;
import com.ibm.as400.security.*;

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
