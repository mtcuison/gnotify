package org.rmj.gnotify.base;

import java.sql.ResultSet;
import org.rmj.appdriver.GRider;

public interface iDashboard{
    public void setGRider(GRider foGRider);
    public void setCallback(iCallBack foCallBack);
    public ResultSet getData(String fsField, String fsValue);
}
