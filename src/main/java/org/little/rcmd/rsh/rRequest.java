package org.little.rcmd.rsh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import org.little.util.Logger;
import org.little.util.LoggerFactory;

public class rRequest implements rCMD {
       private static Logger logger = LoggerFactory.getLogger(rRequest.class);

       private sequences seq;
       private String    request;
       private String    response;
       private int       index;
       private String    name;

       public static final String    response_ok          ="ok";
       public static final String    response_error       ="error";
       public static final String    response_syntax_error="syntax_error";
       public static final String    response_info        ="info";
       public static final String    response_warn        ="warn";
       public static final String    response_text        ="text";

       
       
       public rRequest(String name,int index,String request,String response) {

              this.request=request;
              this.response=response;
              this.name=name;
              this.index=index;

              seq=new sequences();
              seq.add(response_ok          ,response);
              seq.add(response_error       ,"Error:");
              seq.add(response_syntax_error,"Syntax error:");
              seq.add(response_info        ,"Info:");
              seq.add(response_warn        ,"Warning:");
/*
adm@DionisNX# show crypto disec conn
[#!]NAME         ID    SRC             DST             SN         LOC   REM   A B
adm@DionisNX#

adm@DionisNX# show crypto ike conns

adm@DionisNX#
*/


       }
       @Override
       public String type() {return getClass().getName();}
       
       protected boolean sendRequest(rShell sh)  {
              if(request==null){
                 logger.trace("request is null");
                 return true;
              }
              try {
                  if(request.length()>0)sh.getOUT().write(request.getBytes());
                  else logger.trace("request is 0");
                  
                  sh.getOUT().write("\r\n".getBytes());
                  sh.getOUT().flush();
              } catch (IOException e) {
                  logger.error("sendRequest "+toString()+" ex:"+e);
                  return false;
              }

              rlog.print("\nsend request:"+request+"\n");
            
              return true;
       }
       protected String getString(BufferedInputStream buf_input){
              //System.out.println("begin get string");
              StringBuilder buf=new StringBuilder();
              int c;
              try {
                    while((c=buf_input.read()) >= 0){
                          char _c=(char)c;
                          rlog.print(_c);
                          if(_c=='\r' || _c=='\n')break;
                          buf.append(_c);
                    }
                            
              } catch (IOException e) {
                 logger.error("getString "+toString()+" ex:"+e);
              }
            
              return buf.toString();
        }
 
        public boolean run(rShell sh)  {
            BufferedInputStream buf_input = new BufferedInputStream(sh.getIN());
            return run(sh,buf_input,null);
        }
        public boolean run(rShell sh,BufferedInputStream buf_input,BufferedOutputStream buf_output)  {
               StringBuilder str_out=new StringBuilder();
               sendRequest(sh);
               
               int c;
               if(response==null)return true;
               try {
                    while((c=buf_input.read()) >= 0){
                          char _c=(char)c;
                          byte _b=(byte)c;

                          rlog.print(_c);
                          //System.out.print(_c);
                          str_out.append(_c);

                          if(_c=='\r'||_c=='\n'){
                             if(str_out.length()>0){
                                //System.out.println("*> "+str_out.toString());
                                if(buf_output!=null && str_out.length()>0) {
                                   buf_output.write(str_out.toString().getBytes());
                                   buf_output.flush();
                                }
                                str_out.setLength(0);
                                str_out.trimToSize();
                             }
                          }

                          sequence s=seq.put(_b);

                          if(s==null)continue;
                          //System.out.println("find:"+s.getName());
                          //rlog.print("find response");
                          if(response_ok.equals(s.getName())){
                             // received the expected response
                             //String str=getString(buf_input); // get end string
                             logger.trace("run  cmd:"+s.getName()+" Ok");
                             return true;
                          }
                          else
                          if(response_warn.equals(s.getName())){
                              // received warn response
                             String str=getString(buf_input);// get string to end
                             str_out.setLength(0);
                             str_out.trimToSize();
                             logger.trace("run:"+toString()+" for:"+s.getName()+" ret:"+str);
                             continue;
                          }
                          else
                          if(response_info.equals(s.getName())){
                             // received info response
                             String str=getString(buf_input);// get string to end
                             str_out.setLength(0);
                             str_out.trimToSize();
                             logger.trace("run:"+toString()+" for:"+s.getName()+" ret:"+str);
                             continue;
                          }
                          else
                          if(response_error.equals(s.getName())){
                             // received error response
                             String str=getString(buf_input);// get string to end
                             str_out.setLength(0);
                             str_out.trimToSize();
                             logger.trace("run:"+toString()+" for:"+s.getName()+" ret:"+str);
                             continue;
                          }
                          else
                          if(response_syntax_error.equals(s.getName())){
                             // received syntax error response
                             String str=getString(buf_input);// get string to end
                             str_out.setLength(0);
                             str_out.trimToSize();
                             logger.trace("run:"+toString()+" for:"+s.getName()+" ret:"+str);
                             continue;
                          }
                          /*
                          else{ 
                             logger.trace("run:"+toString()+" for:"+s.getName()+" ret:false");
                             rlog.print(">:"+s.getName()+"\n");
                             String str=getString(buf_input);// get string to end
                             rlog.print("text:"+str+"\n");

                             return false;
                          }
                          */
                    }
                            
               } catch (IOException e) {
                    logger.error("sendRequest "+toString()+" ex:"+e);
                    return false;
               }
               return true;
        }
        
        
        public String toString(){
               return "rcmd:"+name+" index:"+index+" request:"+request+" "+"response:"+response;
        }  


        public static void main(String[] arg){
            boolean ret;
            rShell sh=new rShell();
            
            rRequest cmd0=new rRequest("",1,null,"#");
            rRequest cmd1=new rRequest("",2,"configure terminal","(config)#");
            rRequest cmd2=new rRequest("",3,"exit",null);
               
            if(arg.length>0)sh.setHost(arg[0]);
            else            sh.setHost("127.0.0.1");
            
            if(arg.length>1)sh.setUser(arg[1]);
            else            sh.setUser("root");
            
            if(arg.length>2)sh.setPasswd(arg[2]);
            else sh.setPasswd("biglear14");
            
            ret=sh.open();
            System.out.println("open connection:"+ret); 

            BufferedInputStream buf_input = new BufferedInputStream(sh.getIN());

            System.out.println("----------------"); 
            ret=cmd0.run(sh,buf_input,null);            
            System.out.println("----------------"+ret); 
            ret=cmd1.run(sh,buf_input,null);            
            System.out.println("----------------"+ret); 
            ret=cmd2.run(sh,buf_input,null);            
            System.out.println("----------------"+ret); 

            sh.close();
            System.out.println("close connection"); 
            
            
        }

}