package org.little.rcmd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.little.rcmd.rsh.rShell;
import org.little.util.Logger;
import org.little.util.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class rDionis {

       private static Logger                  logger = LoggerFactory.getLogger(rDionis.class);
       private rShell                         sh;
       private BufferedInputStream            buf_input;
       private BufferedOutputStream           buf_output;
       private commonRCMD                     cfg;

       public rDionis() {
              sh     =new rShell();
              buf_input  =null;
              sh.setHost("127.0.0.1");
              cfg=new commonRCMD();
       }
       public void  setUser  (String user  ) {sh.setUser(user);}
       public void  setPasswd(String passwd) {sh.setPasswd(passwd);}
       
       public boolean open() {
              boolean ret;
              ret=sh.open();
              if(ret)buf_input = new BufferedInputStream(sh.getIN()); 
              buf_output = new BufferedOutputStream(System.out); 
              logger.trace("open channel to:"+sh.getHost()+" ret:"+ret);
              return ret;
       }
       public void close() {
              logger.trace("close channel to:"+sh.getHost());
              sh.close();
       }
 
       public String getDefNodeName(){ return "little";};
       public String getNodeName()   { return "littlecmd";}

       private Node findCFG(String cfg_filename) {
               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               try {
                     DocumentBuilder builder;
                     builder  = factory.newDocumentBuilder();
                     Document doc = builder.parse(cfg_filename);
                     logger.trace("open doc:"+cfg_filename);
                     //-----------------------------------------------------------------------
                     Node node_cfg = doc.getFirstChild();
                     //-----------------------------------------------------------------------
                     if(getNodeName().equals(node_cfg.getNodeName())){
                        logger.trace("config structure name:"+getNodeName());
                        return node_cfg;
                     }
                     //-----------------------------------------------------------------------
                     if(getDefNodeName().equals(node_cfg.getNodeName())){
                         logger.trace("default config structure name:"+getDefNodeName());
                         logger.trace("seach topic name:"+getNodeName());
                         NodeList glist=node_cfg.getChildNodes();     
                         for(int i=0;i<glist.getLength();i++){
                             Node n=glist.item(i);
                             //logger.trace("topic["+i+"] name:"+n.getNodeName());
                             if(getNodeName().equals(n.getNodeName())) {
                            	 //logger.trace("topic["+i+"] is name:"+getNodeName());
                            	 return n;
                             }
                         }
                     } 
                     //-----------------------------------------------------------------------
               }
               catch(Exception e) {
                     logger.error("Could not load xml config file:"+cfg_filename, e);
                     return null;
              }
              return null;
       }
       public boolean loadCFG(Node node_cfg) {
           if(node_cfg==null) {
         	 logger.error("no find topic:"+getNodeName());
         	 return false;
           }
           
           NodeList glist=node_cfg.getChildNodes();     
           for(int i=0;i<glist.getLength();i++){
               Node n=glist.item(i);
               if("global_option".equals(n.getNodeName()))cfg.init(n);
               if("commanddi".equals(n.getNodeName()))cfg.loadCMD(n);
           }
            if(cfg.getHost()  !=null)sh.setHost(cfg.getHost());
            if(cfg.getUser()  !=null)sh.setUser(cfg.getUser());
            if(cfg.getPasswd()!=null)sh.setPasswd(cfg.getPasswd());
           
           return true;
       }
       
       public boolean loadCFG(String cfg_filename) {
              Node node_cfg = findCFG(cfg_filename);
              return loadCFG(node_cfg);
       }
       public boolean runCMD(String name) {
    	      logger.trace("cmd:"+name);
    	      rDionisCommand cmd = cfg.getCMD().get(name);
              if(cmd==null)return false;
    	      logger.trace("run cmd:"+name);
              return cmd.run(sh,buf_input,buf_output);
       }
       //-------------------------------------------------------------------------------
       /*
       public static void run(rDionis apk,String[] arg,int cnt){
              boolean ret;
              ret=apk.loadCFG(iRun.xpath);
              if(ret==false) {
                 System.out.println("apk.loadCFG return:"+ret);     
                 return;     
              }
             
              ret=apk.open();
              if(ret==false) {
                 System.out.println("apk.open return:"+ret);     
                 return;     
              }

              int count=1;
              while(ret){
                    if(arg.length<=(cnt+count))break;
                    String cmd=arg[cnt+count];
                    System.out.println("apk."+cmd);     
                    ret=apk.runCMD(cmd);
                    count++;
                    System.out.println("apk."+cmd+" return:"+ret);     
              }
              apk.close();
              System.out.println("close connection"); 

       }
       */
       public static void main(String[] arg){
              rDionis apk = new rDionis();
              iRun.getOpt(arg);
              int cnt=2;
              if(iRun.xuser  !=null){apk.setUser(iRun.xuser);    cnt+=2;}
              if(iRun.xpasswd!=null){apk.setPasswd(iRun.xpasswd);cnt+=2;}
              
              iRun.run(apk,arg,cnt);
            
        }

}
