package org.rmj.gnotify.app;

import org.rmj.gnotify.views.MainStage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import org.rmj.appdriver.GProperty;
import org.rmj.appdriver.GRider;

public class Login {
    
   private static GRider poGRider = null;
    
    public void setGRider(GRider foGRider){poGRider = foGRider;}
    
    public static void main(String [] args){       
        if (!loadProperties()){
            System.err.println("Unable to load config.");
            System.exit(1);
        } else System.out.println("Config file loaded successfully.");
        
        String lsProdctID;
        String lsUserIDxx;
        
        if (System.getProperty("app.debug.mode").equals("0")){
            if(args.length != 2){
                System.err.println("Invalid credential parameter.");
                System.exit(1);
            }
            
            lsProdctID = args[0];
            lsUserIDxx = args[1];
        } else {
            lsProdctID = System.getProperty("app.product.id");
            lsUserIDxx = System.getProperty("user.id");
        }

        GRider poGRider = new GRider(lsProdctID);
        GProperty loProp = new GProperty("GhostRiderXP");

        if (!poGRider.loadEnv(lsProdctID)) {
            System.err.println(poGRider.getErrMsg());
            System.exit(1);
        }
        
        if (!poGRider.logUser(lsProdctID, lsUserIDxx)) {
            System.err.println(poGRider.getErrMsg());
            System.exit(1);
        }         
        
        MainStage instance = new MainStage();
        instance.setGRider(poGRider);

        Application.launch(instance.getClass());
    } 
    
    private static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream("D:\\GGC_Java_Systems\\config\\gnotify.properties"));
            
            System.setProperty("app.debug.mode", po_props.getProperty("app.debug.mode"));
            System.setProperty("user.id", po_props.getProperty("user.id"));
            System.setProperty("app.product.id", po_props.getProperty("app.product.id"));
            
            System.setProperty("app.notification.timeout", po_props.getProperty("app.notification.timeout"));
            System.setProperty("app.notification.always", po_props.getProperty("app.notification.always"));
            System.setProperty("app.high.temp", po_props.getProperty("app.high.temp"));
            System.setProperty("app.age.senior", po_props.getProperty("app.age.senior"));
            System.setProperty("app.age.minor", po_props.getProperty("app.age.minor"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }   
}
