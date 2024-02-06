/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author usuario
 */
@Embeddable
public class PositionsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "DOCUMENTID")
    private int documentid;
    @Basic(optional = false)
    @Column(name = "PRODUCTID")
    private int productid;
    @Basic(optional = false)
    @Column(name = "POSITIONNO")
    private int positionno;

    public PositionsPK() {
    }

    public PositionsPK(int documentid, int productid, int positionno) {
        this.documentid = documentid;
        this.productid = productid;
        this.positionno = positionno;
    }

    public int getDocumentid() {
        return documentid;
    }

    public void setDocumentid(int documentid) {
        this.documentid = documentid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getPositionno() {
        return positionno;
    }

    public void setPositionno(int positionno) {
        this.positionno = positionno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documentid;
        hash += (int) productid;
        hash += (int) positionno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PositionsPK)) {
            return false;
        }
        PositionsPK other = (PositionsPK) object;
        if (this.documentid != other.documentid) {
            return false;
        }
        if (this.productid != other.productid) {
            return false;
        }
        if (this.positionno != other.positionno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ies.torredelrey.modelo.PositionsPK[ documentid=" + documentid + ", productid=" + productid + ", positionno=" + positionno + " ]";
    }
    
}
