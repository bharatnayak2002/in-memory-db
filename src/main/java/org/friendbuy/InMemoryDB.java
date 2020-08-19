package org.friendbuy;

import java.util.HashMap;
import java.util.Stack;

public class InMemoryDB {
    HashMap<String, Integer> imDB = new HashMap<String, Integer> ();
    HashMap<Integer, Integer> valueCounter = new HashMap<Integer, Integer> ();
    Stack<String> rollbackStack = new Stack<String>();
    boolean inTransactionMode = false;
    boolean inRollbackMode = false;

    public String executeCommand(String cmd) {
        String[] split = cmd.split(" ");
        String action = split[0];
        String returnMessage = "";
        switch(action) {
            case "SET":
                if(split.length == 3) {
                    int count;
                    String rollbackCmd;
                    String key = split[1];
                    String stringVal = split[2];

                    int value = Integer.parseInt(stringVal);
                    if( imDB.containsKey(key) ) {
                        int currentValue = imDB.get(key);
                        count = valueCounter.get(currentValue) - 1;
                        if(count == 0) {
                            valueCounter.remove(currentValue);
                        } else {
                            valueCounter.put(currentValue, count);
                        }

                        if(inTransactionMode && !inRollbackMode) {
                            rollbackCmd = "SET " + key+" "+currentValue;
                            rollbackStack.push(rollbackCmd);
                        }
                    } else {
                        if(inTransactionMode && !inRollbackMode) {
                            rollbackCmd = "UNSET " + key;
                            rollbackStack.push(rollbackCmd);
                        }
                    }
                    imDB.put(key, value);
                    count = valueCounter.containsKey(value) ? valueCounter.get(value) : 0;
                    valueCounter.put(value, count +1);
                } else {
                    returnMessage = "Wrong format " + cmd;
                    System.out.println(returnMessage);
                }
                break;
            case "UNSET":
                if(split.length == 2) {
                    String key = split[1];
                    int value = imDB.get(key);
                    imDB.remove(key);
                    int count = valueCounter.get(value) - 1;
                    if(count == 0) {
                        valueCounter.remove(value);
                    } else {
                        valueCounter.put(value, count);
                    }
                    if(inTransactionMode && !inRollbackMode) {
                        String rollbackCmd = "SET " + key+" "+value;
                        rollbackStack.push(rollbackCmd);
                    }
                } else {
                    returnMessage = "Wrong format " + cmd;
                    System.out.println(returnMessage);
                }
                break;
            case "GET":
                if(split.length == 2) {
                    String key = split[1];
                    if(imDB.containsKey(key)) {
                        returnMessage = imDB.get(key).toString();
                        System.out.println(returnMessage);
                    } else {
                        returnMessage = "NULL";
                        System.out.println(returnMessage);
                    }
                } else {
                    returnMessage = "Wrong format " + cmd;
                    System.out.println(returnMessage);
                }
                break;
            case "NUMEQUALTO":
                if(split.length == 2) {
                    int key = Integer.parseInt(split[1]);
                    if(valueCounter.containsKey(key)) {
                        returnMessage = valueCounter.get(key).toString();
                        System.out.println(returnMessage);
                        return returnMessage;
                    } else  {
                        returnMessage = "0";
                        System.out.println(returnMessage);
                        return returnMessage;
                    }
                } else {
                    returnMessage = "Wrong format " + cmd;
                    System.out.println(returnMessage);
                }
                break;
            case "BEGIN":
                inTransactionMode = true;
                rollbackStack.push("BEGIN");
                break;
            case "ROLLBACK":
                if(!inTransactionMode) {
                    returnMessage = "NO TRANSACTION";
                    System.out.println(returnMessage);
                    break;
                }
                inRollbackMode = true;
                while (true) {
                    String command = rollbackStack.pop();
                    if (command.equals("BEGIN")) {
                        break;
                    } else {
                        executeCommand(command);
                    }
                }
                inRollbackMode = false;
                if (rollbackStack.size() == 0) {
                    inTransactionMode = false;
                }
                break;
            case "COMMIT":
                rollbackStack.clear();
                inTransactionMode = false;
                break;
            case "END":
                System.exit(0);
                break;

            default:
                returnMessage = "Wrong format " + cmd;
                System.out.println(returnMessage);
        }
        return returnMessage;
    }
}
