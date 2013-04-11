package org.sl.challenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 *
 * @author Stefan Lourens <stefan.lourens@gmail.com>
 */
public class Challenge {

    private enum Command {
        SUM, MIN, MAX, AVERAGE
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            String path = args[0];
            File file = new File(path);

            if (file.exists()) {
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        parseAndEval(line);
                    }

                } catch (IOException ioe) {
                    System.out.println(format("Error encountered while reading file: %s", ioe.getMessage()));
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException ioe) {
                        System.out.println("Problem closing file");
                    }
                }

            } else {
                System.out.println(format("Could not find file '%s'", path));
            }

        } else {
            System.out.println("Usage: java -jar challenge.jar /path/to/file");
        }
    }

    private static void parseAndEval(String line) {
        String[] parts = line.split(":");

        if (parts.length == 2) {
            String cmdString = parts[0].trim().toUpperCase();

            try {
                Command command = Command.valueOf(cmdString);
                String[] inputs = parts[1].split(",");

                if (inputs.length > 0) {
                    List<BigDecimal> numbers = new ArrayList<BigDecimal>();
                    
                    try {
                        for (String n : inputs) {
                            numbers.add(new BigDecimal(n.trim()));
                        }
                        
                        eval(command, numbers);
                        
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid input number encountered, line ignored");
                    }
                } else {
                    System.out.println("No input numbers, line ignored");
                }

            } catch (IllegalArgumentException iae) {
                System.out.println(format("Invalid command '%s', line ignored", cmdString));
            }
        } else {
            System.out.println("Invalid format, line ignored");
        }

    }

    private static void eval(Command command, List<BigDecimal> numbers) {
        BigDecimal result;

        switch (command) {
            case SUM:
                result = sum(numbers);
                break;
            case MIN:
                result = min(numbers);
                break;
            case MAX:
                result = max(numbers);
                break;
            case AVERAGE:
                result = avg(numbers);
                break;
            default:
                throw new AssertionError(format("No implementation for command  '%s'", command));
        }

        System.out.println(format("%s %s", command, result));
    }

    private static BigDecimal sum(List<BigDecimal> numbers) {
        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal num : numbers) {
            sum = sum.add(num);
        }

        return sum;
    }

    private static BigDecimal min(List<BigDecimal> numbers) {
        BigDecimal currMin = numbers.get(0);

        for (BigDecimal num : numbers) {
            currMin = num.min(currMin);
        }

        return currMin;
    }

    private static BigDecimal max(List<BigDecimal> numbers) {
        BigDecimal currMax = numbers.get(0);

        for (BigDecimal num : numbers) {
            currMax = num.max(currMax);
        }

        return currMax;
    }

    private static BigDecimal avg(List<BigDecimal> numbers) {
        return sum(numbers).divide(new BigDecimal(numbers.size()));
    }
}
