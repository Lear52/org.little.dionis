@echo off
java -cp .;target\little-20210204-DIONIS-little-shade.jar   -Duser.region=US  -Dlog4j.configuration=log4j.xml -Dencoding=Cp866 -Dfile.encoding=UTF8  org.little.rcmd.rDionis -u adm -p 2wsxXSW@  -cfg littleorg_cfg.xml write
rem java -cp .;target\little-20210204-DIONIS-little-shade.jar   -Duser.region=US  -Dlog4j.configuration=log4j.xml -Dencoding=Cp866 -Dfile.encoding=UTF8  org.little.rcmd.rDionis  -help > aaaa
