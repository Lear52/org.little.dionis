package org.little.rcmd;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.lang3.StringUtils;
import org.little.util.Logger;
import org.little.util.LoggerFactory;

public class iRun{
       public static final Logger     _LOG = LoggerFactory.getLogger(iRun.class);
      
       public static final String      OPTION_CFG       = "cfg";
       public static final String      OPTION_HELP      = "help";
       public static final String      OPTION_USER      = "u";
       public static final String      OPTION_PASSWORD  = "p";
       public static       String      xpath;
       public static       String      xuser;
       public static       String      xpasswd;
      
       
       public static void printHelp(final Options options,final String errorMessage) {
              if (!StringUtils.isBlank(errorMessage)) {
                  _LOG.error(errorMessage);
              }
             
              final HelpFormatter formatter = new HelpFormatter();
              formatter.printHelp("org.little.rcmd.rDionis", options);
       }
    
       public static void getOpt(String args[]) {
              Options           options;
              CommandLineParser parser;
              CommandLine       cmd;
             
              options = new Options();
              parser  = new DefaultParser();

              xpath   ="./littleorg_cfg.xml";
              xuser   =null;
              xpasswd =null;
             
              options.addOption(null, OPTION_CFG       , true, "Run with cfg file.");
              options.addOption(null, OPTION_HELP      , false,"Display command line help.");
              options.addOption(null, OPTION_USER      , true, "Run with user name.");
              options.addOption(null, OPTION_PASSWORD  , true, "Run with user psw.");

              _LOG.trace("default config file: " + xpath);
             
              try {
                  cmd = parser.parse(options, args);
              } catch (final ParseException e) {
                  _LOG.error("Could not parse command line: " + Arrays.asList(args), e);
                  printHelp(options,"Could not parse command line: " + Arrays.asList(args));
                  return ;
              }
             
              if (cmd.hasOption(OPTION_HELP)) {
                  printHelp(options, null);
                  System.exit(0);
              }
              if (cmd.hasOption(OPTION_CFG)) {
                  xpath = cmd.getOptionValue(OPTION_CFG);
                  _LOG.trace("config file: " + xpath);
              } 
              if (cmd.hasOption(OPTION_USER)) {
                  xuser = cmd.getOptionValue(OPTION_USER);
                  _LOG.trace("config user: " + xuser);
              } 
              if (cmd.hasOption(OPTION_PASSWORD)) {
                  xpasswd = cmd.getOptionValue(OPTION_PASSWORD);
                  _LOG.trace("config password:***** ");
              } 
              return ;
    }

       public static void run(rDionis apk,String[] arg,int cnt){
           boolean ret;
           ret=apk.loadCFG(iRun.xpath);
           if(ret==false) {
              System.out.println("apk.loadCFG return:"+ret);     
              return;     
           }
           _LOG.trace("load config for apk dionis");
          
           ret=apk.open();
           if(ret==false) {
              System.out.println("apk.open return:"+ret);     
              return;     
           }
           _LOG.trace("open apk dionis");

           int count=0;
           ret=true;
           while(ret){
                 //System.out.println("arg.length:"+arg.length+" cnt+count:"+(cnt+count));     
                 if(arg.length<=(cnt+count))break;
                 String cmd=arg[cnt+count];
                 //System.out.println("apk cmd:"+cmd);     
                 ret=apk.runCMD(cmd);
                 count++;
                 //System.out.println("apk."+cmd+" return:"+ret);     
           }
           apk.close();
           System.out.println("close connection"); 

    }
    public static void main(String[] arg){
           rDionis apk = new rDionis();
           iRun.getOpt(arg);
           int cnt=2;
           if(iRun.xuser  !=null){apk.setUser(iRun.xuser);    cnt+=2;}
           if(iRun.xpasswd!=null){apk.setPasswd(iRun.xpasswd);cnt+=2;}
           
           run(apk,arg,cnt);
         
     }

}

