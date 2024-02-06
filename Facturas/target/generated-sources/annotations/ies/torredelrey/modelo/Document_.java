package ies.torredelrey.modelo;

import ies.torredelrey.modelo.Address;
import ies.torredelrey.modelo.Positions;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-02-06T14:29:22", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Document.class)
public class Document_ { 

    public static volatile SingularAttribute<Document, Double> total;
    public static volatile CollectionAttribute<Document, Positions> positionsCollection;
    public static volatile SingularAttribute<Document, Integer> id;
    public static volatile SingularAttribute<Document, Address> addressid;

}