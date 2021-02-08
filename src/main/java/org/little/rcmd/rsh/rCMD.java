package org.little.rcmd.rsh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;


public interface rCMD {

        public boolean run(rShell sh);
        public boolean run(rShell sh,BufferedInputStream buf_input,BufferedOutputStream buf_output);

        public String type();


}