package com.baizhi;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

@Description(
        name = "hello",
        value = "_FUNC_(str1,str2) - from the input string"
                + "returns the value that is \"你好 $str1 ，$str2 好玩吗？ \" ",
        extended = "Example:\n"
                + " > SELECT _FUNC_(str1,str2) FROM src;"
)
public class HelloUDF extends UDF {
    public String evaluate(String str1, String str2) {
        try {
            return "Hello " + str1 + ", " + str2 + " 好玩吗？";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "ERROR";
        }
    }

}
