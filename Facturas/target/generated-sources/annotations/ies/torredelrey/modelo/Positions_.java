package ies.torredelrey.modelo;

import ies.torredelrey.modelo.Document;
import ies.torredelrey.modelo.PositionsPK;
import ies.torredelrey.modelo.Product;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-02-06T14:29:22", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Positions.class)
public class Positions_ { 

    public static volatile SingularAttribute<Positions, Product> product;
    public static volatile SingularAttribute<Positions, Integer> quantity;
    public static volatile SingularAttribute<Positions, Double> price;
    public static volatile SingularAttribute<Positions, Document> document;
    public static volatile SingularAttribute<Positions, PositionsPK> positionsPK;

}