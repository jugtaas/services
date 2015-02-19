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

package org.jugtaas.services.zoefxports.model.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 13/04/14
 * Time: 14:56
 */
public class BeanClassAccess {
    protected String name;
    protected AccessType accessType;
    protected Field field;
    protected Method getter=null;
    protected Method setter=null;
    protected Class<?> beanClass;
    private Class<?> returnType;

    public BeanClassAccess(Class beanClass, String name) {
        this.name = name;
        this.beanClass = beanClass;
        inspectBeanField();
    }

    protected void inspectBeanField() {
        Boolean getterOk=Boolean.FALSE;

        // getter
        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        try {
            getter = beanClass.getMethod(getterName);
            returnType = getter.getReturnType();
            getterOk = Boolean.TRUE;
        } catch (NoSuchMethodException e) {

        }
        // setter
        String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        if( getterOk ) {
            try {
                setter = beanClass.getMethod(setterName, returnType);
                accessType = AccessType.METHOD;
                return;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        // try to access the field
        try {
            field = beanClass.getField(name);
            returnType = field.getType();
            accessType = AccessType.FIELD;
            return;
        } catch (NoSuchFieldException e) {

        }

    }

    public Class<?> getGenericReturnType() {
        if( Collection.class.isAssignableFrom(returnType) ){
            if( accessType.equals(AccessType.FIELD) ){
                Field field = null;
                try {
                    field = beanClass.getField(name);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Type type = field.getGenericType();
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Class pClass = (Class) actualTypeArguments[0];
                return pClass;
            } else if( accessType.equals(AccessType.METHOD) ){
                try {
                    Method method = beanClass.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                    ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
                    Type[] actualTypeArguments = pt.getActualTypeArguments();
                    Class pClass = (Class) actualTypeArguments[0];
                    return pClass;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getName() {
        return name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
