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
            System.out.println("Insufficient arguments passed. \n1. Please do provide the <userprofile>  <password> <server> to update");
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

        //Checking if the property is available
        if(prop.getProperty("server."+system) == null){
            System.out.println("Server resource not found. "+system);
            return;
        }

        String decPass = aes256.decrypt(prop.getProperty("password."+system),"secret");

        //System.out.println(prop.getProperty("server."+system) + prop.getProperty("username."+system) + decPass.length());

        AS400 as400 = new AS400(prop.getProperty("server."+system),prop.getProperty("username."+system),decPass);

        try {

            System.out.println("Connecting to :"+prop.getProperty("server."+system));
            as400.connectService(AS400.COMMAND);
            System.out.println("Connected:"+as400.isConnected());

            CommandCall cmd = new CommandCall(as400);
            //String cmdTxt = "dspusrprf usrprf("+userId+")";
            String cmdTxt = "chgusrprf usrprf("+userId+") password("+password+") status(*enabled) pwdexp(*yes)";
              //    cmdTxt ="chgusrprf usrprf(CCGV5R2EYO) password(OKTABOT) status(*enabled) pwdexp(*yes)";
            if(prop.getProperty("command."+system) != null){
                //cmdTxt = "Call resetprf parm("+userId+")";
                //Updated command as of 2-oct-2018
                //cmdTxt = "CALL PGM(BOTRESET01) PARM('" + userId + "' '"+ password+"')";
                cmdTxt = "CALL PGM(QGPL/BOTRESET01) PARM('" + userId.toUpperCase() + "' '"+ password+"')";
                System.out.println("Command used: " + cmdTxt);
                //System.out.println("Custom Command");
            }

            if(cmd.run(cmdTxt) != true){
                //System.out.println("Not a valid command");
                System.out.println("Issued Command");
            }
            else{
                System.out.println("Issued Command");
            }
            AS400Message[] messages = cmd.getMessageList();

            for (int i = 0; i < messages.length; i++) {
                if (messages[i].getText().contains(password)) {
                    System.out.println("Command ran successfully");
                }
                System.out.println(messages[i].getText());
            }



        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Disconnecting Service");
            as400.disconnectAllServices();
            System.out.println("Connected:"+as400.isConnected());
        }
    }
}
