package as400pwdrest;

public class auserObj {
    public  auserObj(){

    }
    public auserObj(String empFname, String empMinit, String empLname, String fromtmplt, String prfTmplt, String empNetID, String empID, String empLoc, String empMngr) {
        EmpFname = empFname;
        EmpMinit = empMinit;
        EmpLname = empLname;
        Fromtmplt = fromtmplt;
        PrfTmplt = prfTmplt;
        EmpNetID = empNetID;
        EmpID = empID;
        EmpLoc = empLoc;
        EmpMngr = empMngr;
    }

    private String EmpFname; //       Character format with a length of 15 – This is the Employee first name
    private String EmpMinit; //               Character format with a length of 1 – This is the Employee middle initial (it can be null)
    private String EmpLname; //            Character format with a length of 20 – This is the Employee Last name (surname)
    private String Fromtmplt; //             Character format with a length of 1 – This is whether or not to create the profile from a template. It expects a value of Y or N (it can be null).
    private String PrfTmplt; //                 Character format with a length of 10 – This is the template profile or the profile name that it should be copied from. This is a required field so it cannot be null.
    private String EmpNetID; //              Character format with a length of 13 – This is the employee network ID (AD). This is optional so it can be null.
    private String EmpID; //                      Character format with a length of 6 – This is the employee ID number assigned by HR. This is optional so it can be null.
    private String EmpLoc; //                   Character format with a length of 20 – This is the employee primary location. This is optional so it can be null.
    private String EmpMngr; //               Character format with a length of 36 – This is the employee manager. This is optional so it can be null. It’s expecting this in full name format (First name, Middle initial, last name) but will accepted any value that’s 35 characters of less.

    public  String generateParam(){
        //return("'"+EmpFname+"' '"+EmpMinit+"' '"+EmpLname+"' '"+Fromtmplt+"' '"+PrfTmplt+"' '"+EmpNetID+"' '"+EmpID+"' '"+EmpLoc+"' '"+EmpMngr+"'");
        //removing Fromtmplt
        return("'"+EmpFname+"' '"+EmpMinit+"' '"+EmpLname+"' '"+PrfTmplt+"' '"+EmpNetID+"' '"+EmpID+"' '"+EmpLoc+"' '"+EmpMngr+"'");
    }
    public auserObj getAuserObj() {
        return this;
    }
}
