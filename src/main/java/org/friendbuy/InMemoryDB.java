package org.friendbuy;

import java.util.HashMap;
import java.util.Stack;

public class InMemoryDB {
    HashMap<String, Integer> imDB = new HashMap<String, Integer> ();
    HashMap<Integer, Integer> valueCounter = new HashMap<Integer, Integer> ();
    Stack<String> rollbackStack = new Stack<String>();
    boolean inTransactionMode = false;
    boolean inRollbackMode = false;

    public void executeCommand(String cmd) {
        String[] split = cmd.split(" ");
        String action = split[0];
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
                    System.out.println("Wrong format " + cmd);
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
                    System.out.println("Wrong format");
                }
                // code block
                break;
            case "GET":
                if(split.length == 2) {
                    String key = split[1];
                    if(imDB.containsKey(key)) {
                        System.out.println(imDB.get(key));
                    } else {
                        System.out.println("NULL");
                    }
                    //imDB.remove(key);
                } else {
                    System.out.println("Wrong format " + cmd);
                }
                // code block
                break;
            case "NUMEQUALTO":
                if(split.length == 2) {
                    int key = Integer.parseInt(split[1]);
                    if(valueCounter.containsKey(key)) {
                        System.out.println(valueCounter.get(key));
                    } else  {
                        System.out.println("0");
                    }
                } else {
                    System.out.println("Wrong format " + cmd);
                }
                break;
            case "BEGIN":
                inTransactionMode = true;
                rollbackStack.push("BEGIN");
                break;
            case "ROLLBACK":
                if(!inTransactionMode) {
                    System.out.println("NO TRANSACTION");
                    break;
                }
                while (true) {
                    String command = rollbackStack.pop();
                    if (command.equals("BEGIN")) {
                        break;
                    } else {
                        System.out.println("Executing command "+ command);
                        executeCommand(command);
                    }
                }
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
                // code block
                break;

            default:
                System.out.println("Wrong format " + cmd);
                // code block
        }

    }
}
