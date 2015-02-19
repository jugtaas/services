/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jugtaas.services.zoefxports.db;

import org.jugtaas.services.zoefxports.IOC;
import org.jugtaas.services.zoefxports.model.beans.BeanAccess;
import org.jugtaas.services.zoefxports.model.beans.BeanClassAccess;
import org.jugtaas.services.zoefxports.rest.Operator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:50
 */
public class JPAManagerImpl<E> extends AbstractManager<E> implements Manager<E> {

    private Class<E> entityClass;
    private EntityManagerFactory entityManagerFactory=null;

    public JPAManagerImpl(EntityManagerFactory emf, Class<E> klass) {
        entityClass = klass;
        entityManagerFactory = emf;
    }

    @Override
    public E create() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createRow(String collectionName) {
        Database db = IOC.queryUtility(Database.class);
        BeanClassAccess beanClassAccess = new BeanClassAccess(entityClass, collectionName);
        Class<?> genericReturnType = beanClassAccess.getGenericReturnType();
        Manager<?> manager = db.createManager(genericReturnType);
        return manager.create();
    }

    @Override
    public E save(E entity) {
        parentize(entity);
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        E merged = em.merge(entity);
        em.getTransaction().commit();
        finalizeEntityManager(em);
        return merged;
    }

    @Override
    public void save(List<E> entities) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        for( E entity: entities ){
            em.merge(entity);
        }
        em.getTransaction().commit();
        finalizeEntityManager(em);
    }

    @Override
    public void delete(E entity) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        E merged = em.merge(entity);
        em.remove(merged);
        em.getTransaction().commit();
        finalizeEntityManager(em);
    }

    @Override
    public void deleteRow(Object row) {
        EntityManager em = createEntityManager();
        Object merged = em.merge(row);
        em.remove(merged);
        finalizeEntityManager(em);
    }

    @Override
    public void truncate(){
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM " + entityClass.getCanonicalName() + " e").executeUpdate();
        em.getTransaction().commit();
        finalizeEntityManager(em);
    }

    @Override
    public E get(Long id) {
        EntityManager em = createEntityManager();
        E entity = em.find(entityClass, id);
        finalizeEntityManager(em);
        return entity;
    }

    @Override
    public List<E> query() {
        EntityManager em = createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<E> query = em.createQuery(cq);
        List<E> store = query.getResultList();
        finalizeEntityManager(em);
        return store;
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderbys, List<Boolean> reverses, Integer size, Integer startindex) {
        EntityManager em = createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        List<Predicate> predicates = new ArrayList<Predicate>();
        for( String name: map.keySet() ){
            Predicate predicate=null;
            Path path = null;
            path = root.get(name.split("#")[0]);
            Object objectValue = map.get(name);
            if( objectValue instanceof String ){
                String value = (String) objectValue;
                value = value.replace("*", "%");
                if( !value.endsWith("%") ){
                    value += "%";
                }
                predicate = cb.like(cb.upper(path), value.toUpperCase());
            } else if( objectValue instanceof Boolean ){
                predicate = cb.equal(path, objectValue);
            } else if( objectValue instanceof List ){
                List<Object> range = (List<Object>) objectValue;
                if( range.get(0) instanceof Operator ){
                    Operator op = (Operator) range.get(0);
                    Object value = range.get(1);
                    if( Operator.eq.equals(op) ){
                        predicate = cb.equal(path, value);
                    } else if( value instanceof Comparable ) {
                        if( value instanceof Date ){
                            if( Operator.lt.equals(op) || Operator.le.equals(op) ){
                                value = lastMillisecond((Date) range.get(1));
                            } else if ( Operator.gt.equals(op) || Operator.ge.equals(op) ){
                                value = zeroMilliseconds((Date) range.get(1));
                            }
                        }
                        if (Operator.lt.equals(op)) {
                            predicate = cb.lessThan(path, (Comparable) value);
                        } else if (Operator.le.equals(op)) {
                            predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                        } else if (Operator.gt.equals(op)) {
                            predicate = cb.greaterThan(path, (Comparable) value);
                        } else if (Operator.ge.equals(op)) {
                            predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                        }
                    }
                }

            } else if( objectValue instanceof Object ){
                if( objectValue.getClass().isEnum() ) {
                    int value = ((Enum) objectValue).ordinal(); // XXX: and if EnumType.STRING??
                    predicate = cb.equal(path, value);
                } else {
                    predicate = cb.equal(path, objectValue);
                }
            }
            if( predicate != null ){
                predicates.add(predicate);
            }
        }

        cq.select(root);
        if( predicates.size()>0 ){
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        // order by
        if( orderbys.size()>0 ) {
            List<Order> orders = new ArrayList<Order>();
            for (int i = 0; i < orderbys.size(); i++) {
                String orderby = orderbys.get(i);
                Boolean reverse = reverses.get(i);
                if (reverse) {
                    orders.add(cb.desc(root.get(orderby)));
                } else {
                    orders.add(cb.asc(root.get(orderby)));
                }
            }
            cq.orderBy(orders);
        }

        TypedQuery<E> query = em.createQuery(cq);

        // startIndex
        if( startindex != null ){
            query = query.setFirstResult(startindex);
        }

        // limit
        if( size != null ){
            query = query.setMaxResults(size.intValue());
        }
        List<E> store = query.getResultList();
        finalizeEntityManager(em);
        return store;
    }

    private Date zeroMilliseconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date lastMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    protected EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private void finalizeEntityManager(EntityManager em) {
        em.close();
    }

    /*
     * The parentize method hooks the items of the collections to the parent
     * entity.
     */
    private void parentize(E entity){
        for(Field f: entityClass.getDeclaredFields()){
            for( Annotation a: f.getAnnotations()){
                // discover the OneToMany
                if( a.annotationType().equals(javax.persistence.OneToMany.class) ) {
                    String name = f.getName();
                    BeanAccess<Collection> collectionBeanAccess = new BeanAccess<Collection>(entity, name);
                    Collection collection = collectionBeanAccess.getValue();
                    if( collection != null && collection.size()>0 ){
                        // discover the "mapped by" foreign key
                        String foreignKey=null;
                        try {
                            Method mappedBy = a.annotationType().getDeclaredMethod("mappedBy");
                            foreignKey = (String) mappedBy.invoke(a);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if( foreignKey != null ) {
                            // parentize children
                            for (Iterator it = collection.iterator(); it.hasNext(); ) {
                                Object child = it.next();
                                BeanAccess<E> fkBeanAccess = new BeanAccess<E>(child, foreignKey);
                                fkBeanAccess.setValue(entity);
                            }
                        }
                    }
                }
            }
        }
    }

}
