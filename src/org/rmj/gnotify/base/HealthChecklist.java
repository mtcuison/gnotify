package org.rmj.gnotify.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.StringHelperMisc;

/**
 *
 * @author mac 2021.01.23
 */
public class HealthChecklist extends TimerTask implements iDashboard{  
    public HealthChecklist(){
        //get the high temperature value from config file
        String lsValue = System.getProperty("app.high.temp");
        if (lsValue == null || !StringHelperMisc.isNumeric(lsValue)) lsValue = "0.00";
        double lnHighTemp = Double.parseDouble(lsValue);
        
        pnHighTemp = lnHighTemp == 0.00 ? DEFAULT_HIGH_TEMP : lnHighTemp;
        
        //get the senior citizen age value from config file
        lsValue = System.getProperty("app.age.senior");
        if (lsValue == null || !StringHelperMisc.isNumeric(lsValue)) lsValue = "0";
        int lnSenior = Integer.parseInt(lsValue);
        
        pnSenior = lnSenior == 0 ? DEFAULT_SENIOR : lnSenior;
        
        //get the minor citizen age value from config file
        lsValue = System.getProperty("app.age.minor");
        if (lsValue == null || !StringHelperMisc.isNumeric(lsValue)) lsValue = "0";
        int lnMinor = Integer.parseInt(lsValue);
        
        pnMinor = lnMinor == 0 ? DEFAULT_MINOR : lnMinor;
        
        //get the always notify value from config file
        lsValue = System.getProperty("app.notification.always");
        if (lsValue == null) lsValue = "false";
        boolean lbNotify = Boolean.parseBoolean(lsValue);
        pbNotify = lbNotify;
    }
    
    @Override
    public void run() {
        try {
            String lsSQL = "SELECT" +
                                "  COUNT(sTransNox) xTransNox" +
                                ", SUM(IF(nTemprtre > " + pnHighTemp + ", '1', '0')) xHighTemp" +
                                ", SUM(cWithSore) xWithSore" +
                                ", SUM(cWithPain) xWithPain" +
                                ", SUM(cWithCghx) xWithCghx" +
                                ", SUM(cWithCold) xWithCold" +
                                ", SUM(cWithHdch) xWithHdch" +
                                ", SUM(IF(nCltAgexx >= " + pnSenior + ", '1', '0')) xSeniorsx" +
                                ", SUM(IF(nCltAgexx < " + pnMinor + ", '1', '0')) xMinorsxx" +
                            " FROM Health_Checklist" +
                            " WHERE dSubmittd >= " + SQLUtil.toSQL(SQLUtil.dateFormat(poGRider.getServerDate(), SQLUtil.FORMAT_SHORT_DATE) + " 00:00:01" ) +
                                " AND cRecdStat = '1'";
            
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            
            if (loRS.next()){
                int xTransNox = loRS.getInt("xTransNox");
                int xHighTemp = loRS.getInt("xHighTemp");
                int xWithSore = loRS.getInt("xWithSore");
                int xWithPain = loRS.getInt("xWithPain");
                int xWithCghx = loRS.getInt("xWithCghx");
                int xWithCold = loRS.getInt("xWithCold");
                int xWithHdch = loRS.getInt("xWithHdch");
                int xSeniorsx = loRS.getInt("xSeniorsx");
                int xMinorsxx = loRS.getInt("xMinorsxx");
                MiscUtil.close(loRS);
                
                poCallBack.MasterRetreive("xTransNox", xTransNox);
                poCallBack.MasterRetreive("xHighTemp", xHighTemp);
                poCallBack.MasterRetreive("xWithSore", xWithSore);
                poCallBack.MasterRetreive("xWithPain", xWithPain);
                poCallBack.MasterRetreive("xWithCghx", xWithCghx);
                poCallBack.MasterRetreive("xWithCold", xWithCold);
                poCallBack.MasterRetreive("xWithHdch", xWithHdch);
                poCallBack.MasterRetreive("xSeniorsx", xSeniorsx);
                poCallBack.MasterRetreive("xMinorsxx", xMinorsxx);
                
                int xWatchLst = xHighTemp + xWithSore + xWithPain + xWithCghx + xWithCold + xWithHdch;
                
                Notifications instance = Notifications.create()
                        .title("Health Checklist")                        
                        .text(xWatchLst == 0 ? "No clients are on watchlist at the moment." : "We have clients on the watchlist." +  "\n" +
                                "You can check dashboard for detailed information.")
                        .graphic(null)
                        .hideAfter(Duration.seconds(10))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (poCallBack != null){
                                    poCallBack.MasterRetreive("bLoadForm", true);
                                }
                            }
                        });
                instance.darkStyle();

                if (xWatchLst <= 0){
                    if (pbNotify){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                instance.showInformation();
                            }
                        });
                    }
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            instance.showInformation();
                        }
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    public ResultSet getData(String fsField, String fsValue){
        String lsSQL = "SELECT" +
                            "  b.sBranchNm" +
                            ", CONCAT(a.sLastName, ', ', a.sFrstName, ' ', IF(a.sSuffixNm = '', '', CONCAT(a.sSuffixNm, ' ')), a.sMiddName) xClientNm" +
                            ", a.nTemprtre" +
                            ", a.cGenderxx" +
                            ", a.nCltAgexx" +
                            ", a.sMobileNo" +
                            ", CONCAT(a.sAddressx, ' ', c.sTownName, ', ', d.sProvName) xAddressx" +
                            ", a.cWithSore" +
                            ", a.cWithPain" +
                            ", a.cWithCghx" +
                            ", a.cWithCold" +
                            ", a.cWithHdch" +
                            ", a.cStayedxx" +
                            ", a.cContactx" +
                            ", a.cTravelld" +
                            ", a.cTravlNCR" +
                            ", a.dSubmittd" + 
                        " FROM Health_Checklist a" +
                            " LEFT JOIN TownCity c" +
                                " ON a.sTownIDxx = c.sTownIDxx" +
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx" +
                            ", Branch b" +
                        " WHERE a.sBranchCd = b.sBranchCd" +
                            " AND a.dSubmittd >= " + SQLUtil.toSQL(SQLUtil.dateFormat(poGRider.getServerDate(), SQLUtil.FORMAT_SHORT_DATE) + " 00:00:01" ) +
                            " AND a.cRecdStat = '1'" +
                        " ORDER BY a.dSubmittd";
        
        if (fsField.isEmpty() && !fsValue.isEmpty()){
            lsSQL = MiscUtil.addCondition(lsSQL, fsValue);
        } else if (fsField.isEmpty() && fsValue.isEmpty()){
        } else {
            lsSQL = MiscUtil.addCondition(lsSQL, fsField + " = " + SQLUtil.toSQL(fsValue));
        }            
        
        return poGRider.executeQuery(lsSQL);
    }
    
    @Override
    public void setGRider(GRider foGRider) {
        poGRider = foGRider;
    }

    @Override
    public void setCallback(iCallBack foCallBack) {
        poCallBack = foCallBack;
    }
    
    GRider poGRider;
    iCallBack poCallBack;
    
    int pnSenior;
    int pnMinor;
    double pnHighTemp;
    boolean pbNotify;
    
    final int DEFAULT_SENIOR = 60;
    final int DEFAULT_MINOR = 18;
    final double DEFAULT_HIGH_TEMP = 37.00;
    final boolean DEFAULT_NOTIFICATION = true;
}
