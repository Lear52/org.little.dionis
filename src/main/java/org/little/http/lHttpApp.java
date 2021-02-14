package org.little.http;

import org.little.http.app.cmddionis.HttpCmdRequest;
import org.little.http.handler.lHttpRequest;


public class lHttpApp{

       public static lHttpRequest create(){

           if("appcmddionis".equals(commonHTTP.get().getAppName()))return  new HttpCmdRequest();
           else 
           return  new lHttpRequest();
       }


}
