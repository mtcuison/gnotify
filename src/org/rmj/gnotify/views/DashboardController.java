package org.rmj.gnotify.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.gnotify.base.iCallBack;
import org.rmj.gnotify.base.iDashboard;
import org.rmj.gnotify.base.iDashboardTrans;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class DashboardController implements Initializable, iDashboardTrans {   
    @FXML
    private Label lblSummary;
    @FXML
    private Label lblSeniorsxx;
    @FXML
    private Button btnTotalList;
    @FXML
    private Button btnHighTempx;
    @FXML
    private Button btnSreThroat;
    @FXML
    private Button btnBodyPainx;
    @FXML
    private Button btnCoughxxxx;
    @FXML
    private Button btnColdsxxxx;
    @FXML
    private Button btnHeadachex;
    @FXML
    private Button btnMinorsxxx;
    @FXML
    private Button btnSeniorsxx;
    @FXML
    private Label lblTotalList;
    @FXML
    private Label lblHighTempx;
    @FXML
    private Label lblSreThroat;
    @FXML
    private Label lblBodyPainx;
    @FXML
    private Label lblCoughxxxx;
    @FXML
    private Label lblColdsxxxx;
    @FXML
    private Label lblHeadachex;
    @FXML
    private Label lblMinorsxxx;
    @FXML
    private TableView tblDetail;
    @FXML
    private TableColumn index01;
    @FXML
    private TableColumn index02;
    @FXML
    private TableColumn index03;
    @FXML
    private TableColumn index04;
    @FXML
    private Label cWithSore;
    @FXML
    private Label cWithPain;
    @FXML
    private Label cWithCghx;
    @FXML
    private Label cWithCold;
    @FXML
    private Label cWithHdch;
    @FXML
    private Label cStayedxx;
    @FXML
    private Label cContactx;
    @FXML
    private Label cTravelld;
    @FXML
    private Label cTravlNCR;
    @FXML
    private Label sBranchNm;
    @FXML
    private Label sFullName;
    @FXML
    private Label cGenderCd;
    @FXML
    private Label nTemprtre;
    @FXML
    private Label nCltAgexx;
    @FXML
    private Label sMobileNo;
    @FXML
    private Label sAddressx;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnBodyPainx.setOnAction(this::cmdButton_Click);
        btnColdsxxxx.setOnAction(this::cmdButton_Click);
        btnCoughxxxx.setOnAction(this::cmdButton_Click);
        btnHeadachex.setOnAction(this::cmdButton_Click);
        btnHighTempx.setOnAction(this::cmdButton_Click);
        btnMinorsxxx.setOnAction(this::cmdButton_Click);
        btnSeniorsxx.setOnAction(this::cmdButton_Click);
        btnSreThroat.setOnAction(this::cmdButton_Click);
        btnTotalList.setOnAction(this::cmdButton_Click);
        
        poTrans.setGRider(poGRider);
        poTrans.setCallback(poCallBack);
        
        initGrid();
        clearDetail();
    }    

    @Override
    public void setGRider(GRider foGRider) {
        poGRider = foGRider;
    }
    
    @Override
    public void setClass(iDashboard foValue) {
        poTrans = foValue;
    }
    
    @FXML
    private void tblDetail_Click(MouseEvent event) {
        int lnRow  = tblDetail.getSelectionModel().getSelectedIndex();
        
        clearDetail();
        
        if (poData != null && lnRow >= 0){
            try {
                poData.absolute(lnRow + 1);
                sBranchNm.setText(poData.getString("sBranchNm"));
                sFullName.setText(poData.getString("xClientNm"));
                sAddressx.setText(poData.getString("xAddressx"));
                sMobileNo.setText(poData.getString("sMobileNo"));
                nCltAgexx.setText(String.valueOf(poData.getInt("nCltAgexx")));
                
                switch(poData.getString("cGenderxx")){
                    case "0": cGenderCd.setText("Male"); break;
                    case "1": cGenderCd.setText("Female"); break;
                    default: 
                        cGenderCd.setText("LGBT");
                }

                nTemprtre.setText(String.valueOf(poData.getDouble("nTemprtre")));
                
                cWithSore.setText(poData.getString("cWithSore").equals("1") ? "YES" : "NO");
                cWithPain.setText(poData.getString("cWithPain").equals("1") ? "YES" : "NO");
                cWithCghx.setText(poData.getString("cWithCghx").equals("1") ? "YES" : "NO");
                cWithCold.setText(poData.getString("cWithCold").equals("1") ? "YES" : "NO");
                cWithHdch.setText(poData.getString("cWithHdch").equals("1") ? "YES" : "NO");
                cStayedxx.setText(poData.getString("cStayedxx").equals("1") ? "YES" : "NO");
                cContactx.setText(poData.getString("cContactx").equals("1") ? "YES" : "NO");
                cTravelld.setText(poData.getString("cTravelld").equals("1") ? "YES" : "NO");
                cTravlNCR.setText(poData.getString("cTravlNCR").equals("1") ? "YES" : "NO");      
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void clearDetail(){
        sBranchNm.setText("");
        sFullName.setText("");
        cGenderCd.setText("");
        nCltAgexx.setText("");
        nTemprtre.setText("");
        sAddressx.setText("");
        sMobileNo.setText("");
        cWithSore.setText("");
        cWithPain.setText("");
        cWithCghx.setText("");
        cWithCold.setText("");
        cWithHdch.setText("");
        cStayedxx.setText("");
        cContactx.setText("");
        cTravelld.setText("");
        cTravlNCR.setText("");        
    }
    
    private void loadDetail(){
        int lnCtr = 0;
        
        data.clear();
        
        try {
            while(poData.next()){
                lnCtr ++;
                data.add(new TableModel(String.valueOf(lnCtr),
                        poData.getString("sBranchNm"),
                        poData.getString("xClientNm"),
                        poData.getString("dSubmittd"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""));    
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }        
    }
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();    
        
        switch (lsButton){
            case "btnBodyPainx": poData = poTrans.getData("cWithPain", "1"); break;
            case "btnColdsxxxx": poData = poTrans.getData("cWithCold", "1"); break;
            case "btnCoughxxxx": poData = poTrans.getData("cWithCghx", "1"); break;
            case "btnHeadachex": poData = poTrans.getData("cWithHdch", "1"); break;
            case "btnHighTempx": poData = poTrans.getData("", "nTemprtre > 37.00"); break;
            case "btnMinorsxxx": poData = poTrans.getData("", "nCltAgexx < 18"); break;
            case "btnSeniorsxx": poData = poTrans.getData("", "nCltAgexx >= 60"); break;
            case "btnSreThroat": poData = poTrans.getData("cWithSore", "1"); break;
            case "btnTotalList": poData = poTrans.getData("", ""); break;
            default:
                poData = null;
        }
        clearDetail();
        if (poData != null) loadDetail();
    }
     
    public void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;");
        
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        
        /*Assigning data to table*/
        tblDetail.setItems(data);
    }
    
    iCallBack poCallBack = new iCallBack() {
        @Override
        public void MasterRetreive(String fsIndex, Object foValue) {
            switch (fsIndex){
                case "bLoadForm": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = (Stage) lblSummary.getScene().getWindow();
                            stage.setAlwaysOnTop(true);
                            stage.setMaximized(true);
                            stage.setMaximized(false);
                            stage.setAlwaysOnTop(false);
                        }
                    });
                    
                    break;
                case "xTransNox": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblTotalList.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xHighTemp": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblHighTempx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xWithSore": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblSreThroat.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xWithPain": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblBodyPainx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xWithCghx": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblCoughxxxx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xWithCold": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblColdsxxxx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xWithHdch": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblHeadachex.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xSeniorsx": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblSeniorsxx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
                case "xMinorsxx": 
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblMinorsxxx.setText(String.valueOf(foValue));
                        }
                    });
                    break;
            }
        }
    };
    
    GRider poGRider;
    iDashboard poTrans;
    ResultSet poData;
    
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
}
