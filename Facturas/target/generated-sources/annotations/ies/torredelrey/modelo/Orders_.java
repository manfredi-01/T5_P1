package ies.torredelrey.modelo;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-02-06T14:29:22", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, String> shippostalcode;
    public static volatile SingularAttribute<Orders, String> shipcountry;
    public static volatile SingularAttribute<Orders, Integer> orderid;
    public static volatile SingularAttribute<Orders, Double> freight;
    public static volatile SingularAttribute<Orders, String> shipaddress;
    public static volatile SingularAttribute<Orders, Date> shippeddate;
    public static volatile SingularAttribute<Orders, Date> orderdate;
    public static volatile SingularAttribute<Orders, Integer> employerid;
    public static volatile SingularAttribute<Orders, Integer> shipvia;
    public static volatile SingularAttribute<Orders, String> shipcity;
    public static volatile SingularAttribute<Orders, String> customerid;
    public static volatile SingularAttribute<Orders, String> shipregion;
    public static volatile SingularAttribute<Orders, Date> requireddate;
    public static volatile SingularAttribute<Orders, String> shipname;

}