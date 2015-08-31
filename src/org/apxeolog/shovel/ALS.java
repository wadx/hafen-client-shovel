package org.apxeolog.shovel;

/**
 * Created by APXEOLOG on 31/08/2015.
 */
public class ALS {
    public static void alDebugPrint(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(String.valueOf(args[i]));
            builder.append(" ");
        }
        System.out.println(builder.toString());
    }
}
