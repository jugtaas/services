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

package org.jugtaas.services.zoefxports;

import java.util.HashMap;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:46
 */
public class IOC {

    private static HashMap<String, HashMap<String, Object>> utilities = new HashMap<String, HashMap<String, Object>>();

    /**
     * Registers the unnamed utility for the given interface.
     *
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     *
     */
    public static synchronized void registerUtility(Object utility, Class iface){
        IOC.registerUtility(utility, iface, ".");

    }

    /**
     * Registers the named utility for the given interface.
     *
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     * @param name The string name
     *
     */
    public static synchronized void registerUtility(Object utility, Class iface, String name){
        HashMap<String, Object> hm = IOC.utilities.get(iface.getSimpleName());
        if( hm == null ){
            hm = new HashMap<String, Object>();
        }
        hm.put(name, utility);
        IOC.utilities.put(iface.getSimpleName(), hm);
    }

    /**
     * Query the unnamed utility with the given interface.
     *
     * @param iface The interface implemented by the utility
     * @return  The utility
     *
     */
    public static <T> T queryUtility(Class<T> iface){
        return IOC.queryUtility(iface, ".");
    }

    /**
     * Query the named utility with the given interface.
     *
     * @param iface The interface implemented by the utility
     * @param name The string name
     * @return  The utility
     *
     */
    public static <T> T queryUtility(Class<T> iface, String name){
        T utility = null;
        HashMap<String, Object> hm = IOC.utilities.get(iface.getSimpleName());
        if( hm != null ){
            utility = (T) hm.get(name);
        }
        return utility;
    }


}
