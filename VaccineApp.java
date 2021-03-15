import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Scanner;
public class VaccineApp
{
  int sqlCode=0;      // Variable to hold SQLCODE
  String sqlState="00000";  // Variable to hold SQLSTATE
  String url = "jdbc:db2://winter2021-comp421.cs.mcgill.ca:50000/cs421";
  String your_userid = System.getenv("SOCSUSER");
  String your_password = System.getenv("SOCSPASSWD");
  String todaysdate = LocalDate.now().toString();

    public static void main ( String [ ] args ) throws SQLException
    {
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }
        openMenu();
    }
    public static void openMenu() throws SQLException{
      VaccineApp app = new VaccineApp();
      Scanner scner = new Scanner(System.in);
      System.out.println("VaccineApp Main Menu \n\t 1. Add a Person \n\t 2. Assign a slot to a Person \n\t 3. Enter Vaccine Information \n\t 4. Exit Application \nPlease Enter Your Option(1, 2, 3, or 4): ");
      int opt = Integer.parseInt(scner.nextLine());
      if(opt == 1){
        app.addPerson();
      }
      if(opt == 2){
        app.assignSlot();
      }
      if(opt == 3){
        app.EnterVaccineInfo();
      }
      if(opt == 4){
        System.out.println("Exiting application...");
          System.exit(0);
        
      }
    }
    public void addPerson() throws SQLException{
      Statement statement;
      Connection con;
       con = DriverManager.getConnection (url,your_userid,your_password)  ;
      statement = con.createStatement ( ) ;
      Scanner scner = new Scanner(System.in);
      int hin; String name,  gender,  DOB,  phone,  city,  postalcd,  streetad,  regdate, cname;
      System.out.println("Please enter the HIN: ");
      hin = Integer.parseInt(scner.nextLine());
      String q = "SELECT hin FROM  Person WHERE hin=" + hin; 
      java.sql.ResultSet rs = statement.executeQuery (q); 
      if(rs.next() == true){ 
          System.out.println("The HIN you entered already exists, would you like to update the existing Person information?(y/n)");
          String yn = scner.nextLine();
          if(yn.equals("n")){
            statement.close();
            con.close ();
            VaccineApp.openMenu();
          }
          if(yn.equals("y")){
            statement.close();
            con.close ();
            updatePerson(hin);
         
      }}
      else{
        System.out.println("Please enter the name: ");
        name = scner.nextLine();
        System.out.println("Please enter the gender: ");
        gender = scner.nextLine();
        System.out.println("Please enter the DOB: ");
        DOB = scner.nextLine();
        System.out.println("Please enter the phone number: ");
        phone = scner.nextLine();
        System.out.println("Please enter the city: ");
        city = scner.nextLine();
        System.out.println("Please enter the postal code: ");
        postalcd = scner.nextLine();
        System.out.println("Please enter the street address: ");
        streetad = scner.nextLine();
        regdate = todaysdate;
        System.out.println("Please enter the category of this person: ");
        cname = scner.nextLine();
        String add = "INSERT INTO Person "+"VALUES ("+hin+", \'"+name+"\', \'"+gender+"\', \'"+DOB+"\', \'"+phone+"\', \'"+city+"\', \'"+postalcd+"\', \'"+streetad+"\', \'"+regdate+"\', \'"+cname+"\')";  
        statement.executeUpdate(add);
        System.out.println("***Done!** Returning to main menu...");
            statement.close();
            con.close ();
            VaccineApp.openMenu();
      }
    }
    public void updatePerson(int h) throws SQLException{
      int hin = h;
      Statement statement;
      Connection con;
       con = DriverManager.getConnection (url,your_userid,your_password)  ;
      statement = con.createStatement ( ) ;
      Scanner scner = new Scanner(System.in);
      System.out.println("What would you like to update? * (e.g. gender, DOB, phone):");
      String toUpdate = scner.nextLine();
      System.out.println("Please enter the new value for "+ toUpdate + ":");
      String val = scner.nextLine();
      String u = "UPDATE Person SET "+toUpdate+"="+"'"+val+"'"+" WHERE hin="+hin;
      statement.executeUpdate(u);
      System.out.println("***Done!***");
      System.out.println("Would you like to update another value?(y/n)");
      String cont = scner.nextLine();
      if(cont.equals("y")){
        updatePerson(h);
      }else{
      System.out.println("Returning to main menu...");
      statement.close();
      con.close ();
      VaccineApp.openMenu();
    }
    }
    public void assignSlot() throws SQLException{
      Statement statement;
      Connection con;
      con = DriverManager.getConnection (url,your_userid,your_password)  ;
      statement = con.createStatement ( ) ;
      String hin;
      Scanner scner = new Scanner(System.in);
      String slotID;
      int dosesRequired = 0;
      int dosesReceived = 0;
      System.out.println("Please enter the HIN of the person being assigned this slot: ");
      hin = scner.nextLine();
      System.out.println("Please enter the SlotID of the slot to assign them to: ");
      slotID = scner.nextLine();
      String checkifFull = "SELECT shin FROM SLOT WHERE slotID= '"+slotID+"'";
      java.sql.ResultSet t = statement.executeQuery(checkifFull);
      String sid ="";
    while(t.next()){
       sid = t.getString(1);
  }
    if(sid != null && !(sid.isEmpty())){
    System.out.println("ERROR: This slot has already been assigned to a person, returning to main menu....");
    statement.close();
    con.close ();
    VaccineApp.openMenu();
    }else{
      String getSlotData = "SELECT VDATE FROM SLOT WHERE SLOTID= "+ slotID;
      SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd");
      Date d = null;
      Date today = null;
      try{
        today = p.parse(todaysdate);}catch(Exception e){System.out.println("Error parsing date");}
        java.sql.ResultSet s = statement.executeQuery(getSlotData);
        while(s.next()){
        d = s.getDate(1);
      }
      if(d.before(today)){
          System.out.println("ERROR: The date of this slot is earlier than today's date, returning to main menu....");
          statement.close();
          con.close ();
          VaccineApp.openMenu();
      }else{
        String dRece = "SELECT * FROM SLOT WHERE shin= \'"+hin+"'"+" AND VNAME IS NOT NULL";
        String vname="";
        java.sql.ResultSet rs = statement.executeQuery (dRece); 
        while(rs.next()){dosesReceived++;}
          if (dosesReceived > 0){
           String getVname = "SELECT vname FROM SLOT WHERE shin= '"+hin+"'";
           java.sql.ResultSet res = statement.executeQuery(getVname); 
      
            while(res.next()){
              vname = res.getString(1);}
      String getNumDoses = "SELECT DOSES FROM VACCINFO WHERE vname=\'"+vname+"'";
      java.sql.ResultSet x = statement.executeQuery(getNumDoses);
      while(x.next()){
      dosesRequired = x.getInt(1);}

          if(dosesReceived >= dosesRequired && dosesRequired>0){
            System.out.println("This person has already received the correct number of doses of their vaccine, returning to main menu....");
            statement.close();
            con.close ();
            VaccineApp.openMenu();

          }
        }
          String asgn = "UPDATE SLOT SET shin="+hin+", asgndate="+ "'"+todaysdate+"'"+" WHERE slotID="+slotID;
           statement.executeUpdate(asgn);
           System.out.println("***Done!** Returning to main menu...");
           statement.close();
           con.close ();
           VaccineApp.openMenu();  
    }
  }
}
public void EnterVaccineInfo() throws SQLException{
  Statement statement;
  Connection con;
  con = DriverManager.getConnection (url,your_userid,your_password)  ;
  statement = con.createStatement ( ) ;
  Scanner scner = new Scanner(System.in);
  String vialnum, vname, batchnum, cnln, hin, sid;
  System.out.println("Please enter the vaccine name: ");
  vname = scner.nextLine();
  System.out.println("Please enter the HIN: ");
      hin = scner.nextLine();
      String q = "SELECT vname,shin FROM SLOT WHERE shin= '"+hin+"'";
      java.sql.ResultSet x = statement.executeQuery(q); 
      String checkifAsgned="";
      String check = "";
      while(x.next()){check = x.getString(1); checkifAsgned=x.getString(2);}
      if(checkifAsgned.isEmpty() || checkifAsgned == null){
        System.out.println("**This person has not been assigned to a slot, which is required to enter vaccine information** redirecting to assign slot..");
        statement.close();
        con.close ();
        assignSlot();
      }
      if(check == null){check="none";}
     
      if(check.equals(vname) || check.equals("none")){
        System.out.println("Please enter the slot ID: ");
          sid=scner.nextLine();
        System.out.println("Please enter the vial number: ");
          vialnum= scner.nextLine();
        System.out.println("Please enter the CNLN of the administering nurse: ");
        cnln = scner.nextLine();
        System.out.println("Please enter the batch number: ");
        batchnum = scner.nextLine();
        String vi = "INSERT INTO VIAL VALUES('"+vialnum+"','"+vname+"','"+batchnum+"')";
        statement.executeUpdate(vi);
        String vacc = "UPDATE SLOT SET CNLN = '"+cnln+"', VIALNUM=  '"+vialnum+"', VNAME= '"+vname+"', BATCHNUM= '"+batchnum+"'"+" WHERE SHIN= '"+hin+"' AND SLOTID ='"+sid+"'";
        statement.executeUpdate(vacc);
        System.out.println("***Done!** Returning to main menu...");
        statement.close();
        con.close ();
        VaccineApp.openMenu();
      }else{
        System.out.println("ERROR: This person has previously received a vaccine of a different type.. returning to main menu..");
        statement.close();
        con.close ();
        VaccineApp.openMenu();

      }
    }
}
