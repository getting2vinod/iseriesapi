package as400pwdrest;
import com.ibm.as400.access.*;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class asuser {

    private static ResourceBundle rb = ResourceBundle.getBundle("app");

    public static void main(String args[]) {
//        String systemName = args[0];
//        String userId = args[1];
//        String password = args[2];
//        String userFilter = args[3];

       // appProps.load(new FileInputStream(appConfigPath));

       // prop.setProperty("")
        rb.
        System.out.println(rb.getString("server") + rb.getString("username") +rb.getString("password"));

        AS400 as400 = new AS400(rb.getString("server"),rb.getString("username"),rb.getString("password"));
        if(args.length <= 0){
            System.out.println("Insufficient arguments passed. \nPlease do provide the <userprofile> and <password> to update");
            return;
        }
        String userId = args[0];
        String password = args[1];


        try {


            as400.connectService(AS400.COMMAND);
            System.out.println("Connected:"+as400.isConnected());

            CommandCall cmd = new CommandCall(as400);
            String cmdTxt = "dspusrprf usrprf("+userId+")";
           // String cmdTxt = "chgusrprf usrprf("+userId+") password("+password+") status(*enabled)";
              //    cmdTxt ="chgusrprf usrprf(CCGV5R2EYO) password(OKTABOT) status(*enabled)";
            if(cmd.run(cmdTxt) != true){
                System.out.println("Not a valid command");
            }
            else{
                System.out.println("Command ran successfully");
            }
            AS400Message[] messages = cmd.getMessageList();
            for(int i = 0;i < messages.length;i++){
                System.out.println(messages[i].getText());
            }


        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }
        finally {
            System.out.println("Disconnecting Service");
            as400.disconnectAllServices();
            System.out.println("Connected:"+as400.isConnected());
        }
    }
}
