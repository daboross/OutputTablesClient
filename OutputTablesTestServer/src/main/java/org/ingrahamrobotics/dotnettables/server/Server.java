package org.ingrahamrobotics.dotnettables.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.ingrahamrobotics.dotnettables.DotNetTable;
import org.ingrahamrobotics.dotnettables.DotNetTables;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        // Start NetworkTables
        try {
            DotNetTables.startServer();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        DotNetTable outputTables = DotNetTables.publish("output-tables");
        Map<String, DotNetTable> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String value = scanner.nextLine();
            String[] split = value.split(" ");
            if (split.length < 3) {
                System.err.println("ERR: args < 2");
                continue;
            }
            switch (split[0].toLowerCase()) {
                case "ctable":
                    String tableKeyTC = split[1];
                    String tableNameTC = split[2];
                    DotNetTable dnt = DotNetTables.publish(tableKeyTC);
                    outputTables.setValue(tableKeyTC, tableNameTC);
                    outputTables.send();
                    dnt.send();
                    map.put(tableKeyTC, dnt);
                    break;
                case "set":
                    if (split.length < 4) {
                        System.err.println("ERR: args < 3");
                        break;
                    }
                    String tableKeyTS = split[1];
                    String keyTS = split[2];
                    String valueTS = split[3];
                    DotNetTable tableTS = map.get(tableKeyTS);
                    if (tableTS == null) {
                        System.err.println("ERR: Unkown table");
                        break;
                    }
                    tableTS.setValue(keyTS, valueTS);
                    tableTS.send();
                    break;
                case "del":
                    String tableKeyTD = split[1];
                    String keyTD = split[2];
                    DotNetTable tableTD = map.get(tableKeyTD);
                    if (tableTD == null) {
                        System.err.println("ERR: Unkown table");
                        break;
                    }
                    tableTD.remove(keyTD);
                    tableTD.send();
                    break;
            }
        }
//        DotNetTable[] tables = new DotNetTable[3];
//        for (int i = 0; true; i++) {
//            if (tables[i % 3] == null) {
//                tables[i % 3] = DotNetTables.publish("table-" + i % 3);
//                outputTables.setValue("table-" + (i % 3), "Level" + i % 3);
//                Thread.sleep(500);
//                outputTables.send();
//            }
//            tables[i % 3].setValue("Key" + (i % 4), "Value" + i);
//            Thread.sleep(1000);
//            tables[i % 3].send();
//            if (i % 2 == 0) {
//                outputTables.send();
//            }
//        }
    }
}
