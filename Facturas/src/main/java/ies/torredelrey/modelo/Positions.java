/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "positions")
@NamedQueries({
    @NamedQuery(name = "Positions.findAll", query = "SELECT p FROM Positions p"),
    @NamedQuery(name = "Positions.findByDocumentid", query = "SELECT p FROM Positions p WHERE p.positionsPK.documentid = :documentid"),
    @NamedQuery(name = "Positions.findByProductid", query = "SELECT p FROM Positions p WHERE p.positionsPK.productid = :productid"),
    @NamedQuery(name = "Positions.findByPositionno", query = "SELECT p FROM Positions p WHERE p.positionsPK.positionno = :positionno"),
    @NamedQuery(name = "Positions.findByQuantity", query = "SELECT p FROM Positions p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "Positions.findByPrice", query = "SELECT p FROM Positions p WHERE p.price = :price")})
public class Positions implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PositionsPK positionsPK;
    @Column(name = "QUANTITY")
    private Integer quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;
    @JoinColumn(name = "DOCUMENTID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Document document;
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public Positions() {
    }

    public Positions(PositionsPK positionsPK) {
        this.positionsPK = positionsPK;
    }

    public Positions(int documentid, int productid, int positionno) {
        this.positionsPK = new PositionsPK(documentid, productid, positionno);
    }

    public PositionsPK getPositionsPK() {
        return positionsPK;
    }

    public void setPositionsPK(PositionsPK positionsPK) {
        this.positionsPK = positionsPK;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (positionsPK != null ? positionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Positions)) {
            return false;
        }
        Positions other = (Positions) object;
        if ((this.positionsPK == null && other.positionsPK != null) || (this.positionsPK != null && !this.positionsPK.equals(other.positionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ies.torredelrey.modelo.Positions[ positionsPK=" + positionsPK + " ]";
    }
    
}
