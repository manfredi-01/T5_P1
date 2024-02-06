/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "orders")
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
    @NamedQuery(name = "Orders.findByOrderid", query = "SELECT o FROM Orders o WHERE o.orderid = :orderid"),
    @NamedQuery(name = "Orders.findByCustomerid", query = "SELECT o FROM Orders o WHERE o.customerid = :customerid"),
    @NamedQuery(name = "Orders.findByEmployerid", query = "SELECT o FROM Orders o WHERE o.employerid = :employerid"),
    @NamedQuery(name = "Orders.findByOrderdate", query = "SELECT o FROM Orders o WHERE o.orderdate = :orderdate"),
    @NamedQuery(name = "Orders.findByRequireddate", query = "SELECT o FROM Orders o WHERE o.requireddate = :requireddate"),
    @NamedQuery(name = "Orders.findByShippeddate", query = "SELECT o FROM Orders o WHERE o.shippeddate = :shippeddate"),
    @NamedQuery(name = "Orders.findByShipvia", query = "SELECT o FROM Orders o WHERE o.shipvia = :shipvia"),
    @NamedQuery(name = "Orders.findByFreight", query = "SELECT o FROM Orders o WHERE o.freight = :freight"),
    @NamedQuery(name = "Orders.findByShipname", query = "SELECT o FROM Orders o WHERE o.shipname = :shipname"),
    @NamedQuery(name = "Orders.findByShipaddress", query = "SELECT o FROM Orders o WHERE o.shipaddress = :shipaddress"),
    @NamedQuery(name = "Orders.findByShipcity", query = "SELECT o FROM Orders o WHERE o.shipcity = :shipcity"),
    @NamedQuery(name = "Orders.findByShipregion", query = "SELECT o FROM Orders o WHERE o.shipregion = :shipregion"),
    @NamedQuery(name = "Orders.findByShippostalcode", query = "SELECT o FROM Orders o WHERE o.shippostalcode = :shippostalcode"),
    @NamedQuery(name = "Orders.findByShipcountry", query = "SELECT o FROM Orders o WHERE o.shipcountry = :shipcountry")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ORDERID")
    private Integer orderid;
    @Basic(optional = false)
    @Column(name = "CUSTOMERID")
    private String customerid;
    @Basic(optional = false)
    @Column(name = "EMPLOYERID")
    private int employerid;
    @Column(name = "ORDERDATE")
    @Temporal(TemporalType.DATE)
    private Date orderdate;
    @Column(name = "REQUIREDDATE")
    @Temporal(TemporalType.DATE)
    private Date requireddate;
    @Column(name = "SHIPPEDDATE")
    @Temporal(TemporalType.DATE)
    private Date shippeddate;
    @Column(name = "SHIPVIA")
    private Integer shipvia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FREIGHT")
    private Double freight;
    @Column(name = "SHIPNAME")
    private String shipname;
    @Column(name = "SHIPADDRESS")
    private String shipaddress;
    @Column(name = "SHIPCITY")
    private String shipcity;
    @Column(name = "SHIPREGION")
    private String shipregion;
    @Column(name = "SHIPPOSTALCODE")
    private String shippostalcode;
    @Column(name = "SHIPCOUNTRY")
    private String shipcountry;

    public Orders() {
    }

    public Orders(Integer orderid) {
        this.orderid = orderid;
    }

    public Orders(Integer orderid, String customerid, int employerid) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.employerid = employerid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public int getEmployerid() {
        return employerid;
    }

    public void setEmployerid(int employerid) {
        this.employerid = employerid;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Date getRequireddate() {
        return requireddate;
    }

    public void setRequireddate(Date requireddate) {
        this.requireddate = requireddate;
    }

    public Date getShippeddate() {
        return shippeddate;
    }

    public void setShippeddate(Date shippeddate) {
        this.shippeddate = shippeddate;
    }

    public Integer getShipvia() {
        return shipvia;
    }

    public void setShipvia(Integer shipvia) {
        this.shipvia = shipvia;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getShipname() {
        return shipname;
    }

    public void setShipname(String shipname) {
        this.shipname = shipname;
    }

    public String getShipaddress() {
        return shipaddress;
    }

    public void setShipaddress(String shipaddress) {
        this.shipaddress = shipaddress;
    }

    public String getShipcity() {
        return shipcity;
    }

    public void setShipcity(String shipcity) {
        this.shipcity = shipcity;
    }

    public String getShipregion() {
        return shipregion;
    }

    public void setShipregion(String shipregion) {
        this.shipregion = shipregion;
    }

    public String getShippostalcode() {
        return shippostalcode;
    }

    public void setShippostalcode(String shippostalcode) {
        this.shippostalcode = shippostalcode;
    }

    public String getShipcountry() {
        return shipcountry;
    }

    public void setShipcountry(String shipcountry) {
        this.shipcountry = shipcountry;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderid != null ? orderid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.orderid == null && other.orderid != null) || (this.orderid != null && !this.orderid.equals(other.orderid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ies.torredelrey.modelo.Orders[ orderid=" + orderid + " ]";
    }
    
}
