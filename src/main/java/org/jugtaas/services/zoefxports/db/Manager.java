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

import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 21:49
 */
public interface Manager<E> {

    Object getId(E entity);

    E save(E entity);

    void save(List<E> entities);

    void delete(E entity);

    void deleteRow(Object row);

    void truncate();

    E get(Long id);

    List<E> getAll();

    List<E> query();

    List<E> query(Integer size);

    List<E> query(Integer size, Integer startindex);

    List<E> query(String orderby);

    List<E> query(String orderby, Boolean reverse);

    List<E> query(List<String> orderby);

    List<E> query(List<String> orderby, List<Boolean> reverse);

    List<E> query(String orderby, Integer size);

    List<E> query(List<String> orderby, Integer size);

    List<E> query(String orderby, Boolean reverse, Integer size);

    List<E> query(List<String> orderby, List<Boolean> reverse, Integer size);

    List<E> query(String orderby, Integer size, Integer startindex);

    List<E> query(List<String> orderby, Integer size, Integer startindex);

    List<E> query(String orderby, Boolean reverse, Integer size, Integer startindex);

    List<E> query(List<String> orderby, List<Boolean> reverse, Integer size, Integer startindex);

    List<E> query(Map<String, Object> map);

    List<E> query(Map<String, Object> map, Integer size);

    List<E> query(Map<String, Object> map, Integer size, Integer startindex);

    List<E> query(Map<String, Object> map, String orderby);

    List<E> query(Map<String, Object> map, String orderby, Boolean reverse);

    List<E> query(Map<String, Object> map, List<String> orderby);

    List<E> query(Map<String, Object> map, List<String> orderby, List<Boolean> reverse);

    List<E> query(Map<String, Object> map, String orderby, Integer size);

    List<E> query(Map<String, Object> map, List<String> orderby, Integer size);

    List<E> query(Map<String, Object> map, String orderby, Boolean reverse, Integer size);

    List<E> query(Map<String, Object> map, List<String> orderby, List<Boolean> reverse, Integer size);

    List<E> query(Map<String, Object> map, String orderby, Integer size, Integer startindex);

    List<E> query(Map<String, Object> map, List<String> orderby, Integer size, Integer startindex);

    List<E> query(Map<String, Object> map, String orderby, Boolean reverse, Integer size, Integer startindex);

    List<E> query(Map<String, Object> map, List<String> orderby, List<Boolean> reverse, Integer size, Integer startindex);

    E create();

    Object createRow(String collectionName);

}
