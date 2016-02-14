package net.highersoft.mstats.model;

import java.util.Date;


/**
 * 
 * @author chengzhong
 *
 */
public class VisitorData  {
    private static final long serialVersionUID = 1L;
    private int id;
    private int systemId;
    private String menuPath;
    private String erpId;    
    private Date visitDate;
    private int pathType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    

    public int getPathType() {
		return pathType;
	}

	public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

	public void setPathType(int pathType) {
		this.pathType = pathType;
	}
    
}
