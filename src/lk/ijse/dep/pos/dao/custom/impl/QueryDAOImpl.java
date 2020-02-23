package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public CustomEntity getOrderInfo(int orderId) throws Exception {
        return (CustomEntity) session.createQuery("SELECT NEW lk.ijse.dep.pos.entity.CustomEntity(o.id, c.id,c.name,o.date) FROM Customer c INNER JOIN c.orders o WHERE o.id=?1")
                .setParameter(1, orderId)
                .uniqueResult();
    }

    @Override
    public CustomEntity getOrderInfo2(int orderId) throws Exception {
        return null;
/*        ResultSet rst = CrudUtil.execute("SELECT O.id, C.customerId, C.name, O.date, SUM(OD.qty * OD.unitPrice) AS Total  FROM Customer C INNER JOIN `Order` O ON C.customerId=O.customerId\" +\n" +
                "                \" INNER JOIN OrderDetail OD on O.id = OD.orderId WHERE O.id=? GROUP BY orderId", orderId);
        if (rst.next()){
            return new CustomEntity(rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getDouble(5));
        }else{
            return null;
        }*/
    }

    @Override
    public List<CustomEntity> getOrdersInfo(String query) throws Exception {
/*
        List<Object[]> resultList = session.createNativeQuery("SELECT O.id, C.id, C.name, O.date, SUM(OD.qty * OD.unit_price) AS Total  FROM Customer C INNER JOIN `Order` O ON C.id=O.customer_id " +
                "INNER JOIN OrderDetail OD on O.id = OD.order_id WHERE O.id LIKE ?1 OR C.id LIKE ?1 OR C.name LIKE ?1 OR O.date LIKE ?1 GROUP BY O.id")
                .setParameter(1, query)
                .list();
        List<CustomEntity> al = new ArrayList<>();
        for (Object[] cols : resultList) {
            al.add(new CustomEntity((int) cols[0], (String) cols[1], (String) cols[2],
                    (Date) cols[3], (Double) cols[4]));
        }
        return al;
*/



        NativeQuery nativeQuery = session.createNativeQuery("SELECT O.id as orderId ,O.date as orderDate, C.id as customerId,C.name as customerName, SUM(OD.qty * OD.unit_price) as orderTotal FROM `Order` O INNER JOIN OrderDetail OD on O.id = OD.order_id INNER JOIN Customer C on C.id = O.customer_id GROUP BY O.id");

        Query<CustomEntity> query2 = nativeQuery.
                setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query2.list();
        list.forEach(System.out::println);

        return list;
    }
}
