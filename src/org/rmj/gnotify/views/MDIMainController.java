package org.rmj.gnotify.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.StringHelperMisc;
import org.rmj.appdriver.agent.MsgBox;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.gnotify.base.HealthChecklist;
import org.rmj.gnotify.base.iDashboardTrans;

public class MDIMainController implements Initializable {
    @FXML
    private MenuItem mnuClose;
    @FXML
    private Label lblUser;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnMinimize;
    @FXML
    private MenuBar mnuBar;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblCompany;
    @FXML
    private FontAwesomeIconView file;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Menu mnuFiles;
    @FXML
    private AnchorPane mnuMain;
    @FXML
    private AnchorPane mainTitle;
    @FXML
    private StackPane stackBody;
    @FXML
    private MenuItem mnuDashboard;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        lblFormTitle.setText(MainStage.pxeMainFormTitle);
        lblCompany.setText(poGRider.getClientName());
        
        btnExit.setTooltip(new Tooltip("Exit"));
        btnMinimize.setTooltip(new Tooltip("Minimize"));
        Tooltip.install(btnExit, new Tooltip("Exit"));
        Tooltip.install(btnMinimize, new Tooltip("Minimize"));
        
        getTime();
        loadRecord();
        
        poTrans.setGRider(poGRider);
        poTrans.setGRider(null);
        
        //set dashboard as main screen
        setScene(loadAnimate("Dashboard.fxml"));
        
        //get the timeout set from the config file
        String lsTimeOut = System.getProperty("app.notification.timeout");
        if (lsTimeOut == null || !StringHelperMisc.isNumeric(lsTimeOut)) lsTimeOut = "0";
        long lnTimeout = Long.parseLong(lsTimeOut);
        
        //initialize loop for notification
        Timer timer = new Timer();
        timer.schedule(poTrans, 0, lnTimeout == 0 ? DEFAULT_TIMEOUT : lnTimeout);
    }
    
    public void loadRecord(){
        ResultSet name;
        String lsQuery = "SELECT a.sCompnyNm "+
                            " FROM Client_Master a" +
                            " LEFT JOIN xxxSysUser b" + 
                                " ON a.sClientID = b.sEmployNo" + 
                            " WHERE sUserIDxx = " + SQLUtil.toSQL(poGRider.getUserID());
        name = poGRider.executeQuery(lsQuery);
        try {
            if(name.next()){
                lblUser.setText(name.getString("sCompnyNm"));
                System.setProperty("user.name", name.getString("sCompnyNm"));
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) throws IOException {
        if (MsgBox.showYesNo("Are you sure you want to exit?") == MsgBox.RESP_YES_OK){
            CommonUtils.closeStage(btnExit);
            System.exit(0);
        }   
    }

    @FXML
    private void btnMinimize_Click(ActionEvent event) {
        CommonUtils.minimizeStage(btnMinimize);
    }

    @FXML
    private void mnuMain_KeyPressed(KeyEvent event) {  
    }

    @FXML
    private void btnExit_Click(ActionEvent event) {
        if (MsgBox.showYesNo("Are you sure you want to exit?") == MsgBox.RESP_YES_OK){
            CommonUtils.closeStage(btnExit);
            System.exit(0);
        } 
    }

    @FXML
    private void mnuDashboard_Click(ActionEvent event) {
        setScene(loadAnimate("Dashboard.fxml"));
    }
       
    public static class MouseGestures {
        class DragContext {
            double x;
            double y;
        }

        DragContext dragContext = new DragContext();

        public void makeDraggable(VBox pane, VBox node) {
            node.setOnMousePressed(onMousePressedEventHandler);
            node.setOnMouseDragged(onMouseDraggedEventHandler);
            
            double centerXPosition = (pane.getWidth() - node.getPrefWidth())/2;
            double centerYPosition = (pane.getHeight() - node.getPrefHeight())/2;

            node.setTranslateX(centerXPosition);
            node.setTranslateY(centerYPosition);
        }

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            Node node = ((Node) (event.getSource()));
            
            dragContext.x = node.getTranslateX() - event.getSceneX();
            dragContext.y = node.getTranslateY() - event.getSceneY();
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            Node node = ((Node) (event.getSource()));
            
            node.setTranslateX( dragContext.x + event.getSceneX());            
            if (dragContext.y + event.getSceneY() > 0)
                node.setTranslateY(dragContext.y + event.getSceneY());
        };
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "MDIMainController";
    private static GRider poGRider;
    
    
    private void getTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);        
        String temp = "" + second;
        
        Date date = new Date();
        String strTimeFormat = "hh:mm:";
        String strDateFormat = "MMMM dd, yyyy";
        String secondFormat = "ss";
        
        DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        
        String formattedTime= timeFormat.format(date);
        String formattedDate= dateFormat.format(date);
        
        lblDate.setText(formattedDate+ " || " + formattedTime);
        
        }),
         new KeyFrame(Duration.seconds(1))
        );
        
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    private void setScene(AnchorPane foPane){
        stackBody.getChildren().clear();
        stackBody.getChildren().add(foPane);
    }
    
    private AnchorPane loadAnimate(String fsFormName){
        iDashboardTrans fxObj = getController(fsFormName);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);      
   
        AnchorPane root;
        try {
            root = (AnchorPane) fxmlLoader.load();
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(root);
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();

            return root;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
    private iDashboardTrans getController(String fsValue){                
        iDashboardTrans instance;
        
        switch (fsValue.replace(".fxml", "")){
            //start of main forms
            case "Dashboard":
                instance = new DashboardController();
                instance.setGRider(poGRider);
                instance.setClass(poTrans);
                return instance;
            default: 
                return null;
        }
    }
    
    HealthChecklist poTrans = new HealthChecklist();
    final long DEFAULT_TIMEOUT = 60000;
}
