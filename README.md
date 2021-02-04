# org.little.rcmd
- run command on APK DIONIS
----------------------------------------------------------------------------

usage: org.little.rcmd.rDionis
    --cfg <arg>   Run with cfg file.
    --help        Display command line help.
    --p <arg>     Run with user psw.
    --u <arg>     Run with user name.

example:
rem java -cp .;target\little-20210204-DIONIS-little-shade.jar   -Duser.region=US  -Dlog4j.configuration=log4j.xml -Dencoding=Cp866 -Dfile.encoding=UTF8  org.little.rcmd.rDionis -u adm -p 2wsxXSW@  -cfg littleorg_cfg.xml write clear exit

----------------------------------------------------------------------------
config file
littleorg_cfg.xml  


<little>
	<littlecmd>
		<global_option>
			<host>192.168.1.1</host>       <!-- ip address APK DIONIS (mast be)-->
			<user>adm</user>               <!-- user name admin APK DIONIS (may be)-->
			<password>2wsxXSW@</password>  <!-- password admin APK DIONIS (may be)-->
		</global_option>
		<commanddi>
		      <getgkey>
		         <!-- 
                             <cmd>
                              <req>command</req>
                              <res>wait output</res>
                             </cmd>
                         -->

		         <cmd><req>crypto access key init floppy0:/start.p15</req><res>[y/n]</res></cmd>
		         <cmd><req>y</req><res>NX#</res></cmd>
		         <cmd><req>crypto access key store floppy0</req><res>[y/n]</res></cmd>
		         <cmd><req>y</req><res>word:</res></cmd>
		         <cmd><req></req><res>epeat:</res></cmd>
		         <cmd><req></req><res>NX#</res></cmd>
		      </getgkey>
		      <loadkey>
		         <cmd><req>configure terminal</req><res>(config)#</res></cmd>
		         <cmd><req>crypto access key load floppy0</req><res>(config)#</res></cmd>
		         <cmd><req>do write</req><res>(config)#</res></cmd>
		         <cmd><req>end</req><res>NX#</res></cmd>
		      </loadkey>
		      <write>
		         <cmd><req>write</req><res>NX#</res></cmd>
		      </write>
		      <loadrootsert>
		         <cmd><req>crypto pki import root ca cert from flash0:/root.cer</req><res>[y/n]</res></cmd>
		         <cmd><req>y</req><res>NX# </res></cmd>
		      </loadrootsert>
		      <loadsrl>
		         <cmd><req>crypto pki import crl from flash0:/root.crl</req><res>[y/n]</res></cmd>
		         <cmd><req>y</req><res>NX# </res></cmd>
		      </loadsrl>
		      <genrequest>
		         <cmd><req> </req><res>[y/n]</res></cmd>
		         <cmd><req>y</req><res>NX# </res></cmd>
		      </genrequest>
		      <getrequest>
		         <get><remote>/out.bin</remote><local>out.bin</local></get>
		         <get><remote>/out.bin</remote><smtp>av@vip.cbr.ru</smtp></get>
		         <get><remote>/out.bin</remote><http>http://127.0.0.1/put</http></get>
		      </getrequest>
		      <putsertificate>
		         <put><remote>out.bin</remote><local>out.bin</local></put>
		      </putsertificate>
		      <exit>
		         <cmd><req>exit</req></cmd>
		      </exit>
		      <clear>
		         <cmd><req>clear alert</req><res>#</res></cmd>
		      </clear>
		</commanddi>
	</littlecmd>
</little>
