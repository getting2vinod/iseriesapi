package as400pwdrest;
import com.ibm.as400.access.*;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;





public class asuser {

    private static Properties prop = new Properties();

    public static AS400 as400;

    public class UserProfile {
        String userName;
        Boolean active;
        String notes;
        Boolean error;

        public UserProfile(String userName, Boolean active, String notes) {
            this.userName = userName;
            this.active = active;
            this.notes = notes;
        }

        public void setNotes(String notes){
            this.notes += notes;
        }

        public void setError(String error){
            this.error = true;
            this.notes = error;
        }

        public UserProfile(){
            userName = "";
            active = false;
            notes = "";
            error = false;
        }


        public String getUserName() {
            return userName;
        }

        public Boolean getActive() {
            return active;
        }

        public String getNotes() {
            return notes;
        }
    }

    public UserProfile getUserProfile(String userName){


        try {
            as400.connectService(AS400.COMMAND);
            System.out.println("Connected:"+as400.isConnected());

            CommandCall cmd = new CommandCall(as400);
            String command = "DSPUSRPRF userprf("+userName+")";
            if(cmd.run(command) == true){

                AS400Message[] messages = cmd.getMessageList();
                for(int i = 0;i < messages.length;i++){
                    up.setNotes(messages[i].getText());
                    System.out.println(messages[i].getText());
                }

            }
        }
        catch (Exception e) {
           up.setError(e.getMessage());
        }
        finally {
            System.out.println("Disconnecting Service");
            as400.disconnectAllServices();
            System.out.println("Connected:"+as400.isConnected());
        }

        return (up);

    }

    public UserProfile up = new asuser.UserProfile();

    public static Boolean runCommand(String command){
        Boolean returnState = true;
        try {

            as400.connectService(AS400.COMMAND);
            System.out.println("Connected:"+as400.isConnected());

            CommandCall cmd = new CommandCall(as400);
            //String cmdTxt = "dspusrprf usrprf("+userId+")";

            //    cmdTxt ="chgusrprf usrprf(CCGV5R2EYO) password(OKTABOT) status(*enabled) pwdexp(*yes)";
            if(cmd.run(command) != true){

                returnState = false;
            }

            AS400Message[] messages = cmd.getMessageList();
            for(int i = 0;i < messages.length;i++){
                System.out.println(messages[i].getText());
            }
            return returnState;

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return returnState;
        }
        finally {
            System.out.println("Disconnecting Service");
            as400.disconnectAllServices();
            System.out.println("Connected:"+as400.isConnected());
        }
    }

    public static void main(String args[]) throws Exception {
//        String systemName = args[0];
//        String userId = args[1];
//        String password = args[2];
//        String userFilter = args[3];

       // appProps.load(new FileInputStream(appConfigPath));

       // prop.setProperty("")
       // System.out.println(System.getProperty("user.dir"));
        prop.load(new FileInputStream(new File(System.getProperty("user.dir")+"\\app.properties")));

        as400pwdrest.AES256 aes256 = new as400pwdrest.AES256();



        if(args.length <= 0){
            System.out.println("Insufficient arguments passed. \n1. Please do provide the <userprofile>  <password> <server> <enable : true / false> <expire password: true / false> to update");
            System.out.println(" \nAccount password would be expired and enabled by default");
            System.out.println(" \n2. To generate an encrypted key pass the key text as the first argument");
            return;
        }
        if(args.length == 1){
            //section for encrypting a passphrase
            System.out.println(aes256.encrypt(args[0],"secret"));
            return;

        }
        String userId = args[0];
        String password = args[1];
        String system = args[2];
        String expire = "pwdexp(*yes)";
        String enable = "status(*enabled)";

        if(args.length > 3) {
            if (args[3] != null) {
                if (args[3].equals("false")) {
                    enable = "";
                }
            }

            if (args[4] != null) {
                if (args[4].equals("false")) {
                    expire = "";
                }
            }
        }

        //Checking if the property is available
        if(prop.getProperty("server."+system) == null){
            System.out.println("Server resource not found. "+system);
            return;
        }

        String decPass = aes256.decrypt(prop.getProperty("password."+system),"secret");

        //System.out.println(prop.getProperty("server."+system) + prop.getProperty("username."+system) + decPass.length());

        as400 = new AS400(prop.getProperty("server."+system),prop.getProperty("username."+system),decPass);
        System.out.println("Connecting to :"+prop.getProperty("server."+system));

        //fetching userprofile

       up = new asuser().getUserProfile("OKTABOT");

        String cmdTxt = "chgusrprf usrprf("+userId+") password("+password+") "+enable+ " "+expire;

        if(runCommand(cmdTxt)){
            System.out.println("Command Ran Successfully");
        }
        else{
            System.out.println("Not a valid command");
        }

    }
}
