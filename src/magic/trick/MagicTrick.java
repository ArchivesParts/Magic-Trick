/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magic.trick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gregory ANNE <ganne@digitaleo.com>
 */
public class MagicTrick {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(MagicTrick.class.getName());
        String inputFile = "A-small-attempt0.in";
        String outputFile = "A-small-attempt0.out";
        Integer nbTestCase = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            File file = new File(outputFile);

            // if file doesnt exists, then create it
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)));
            String line;
            Matrix current = null;
            MatrixSolver solver = null;
            while ((line = reader.readLine()) != null) {
                if (nbTestCase == null) {
                    nbTestCase = new Integer(line);
                    current = new Matrix();
                    solver = new MatrixSolver();
                } else {
                    if (!current.loadLine(line)) {
                        if (!solver.addMatrix(current)) {
                            writer.write(solver.toString());
                            if (!nbTestCase.equals(MatrixSolver.solverNumber)) {
                                writer.write("\n");
                            }
                            solver = new MatrixSolver();
                        }
                        current = new Matrix();
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}

/**
 *
 * @author Gregory ANNE <gregonmac@gmail.com>
 */
class MatrixSolver {

    public static Integer solverNumber = 0;
    private Matrix firstTry;
    private Matrix secondTry;
    private final Logger logger;

    public MatrixSolver() {
        solverNumber++;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public boolean addMatrix(Matrix matrix) {
        if (firstTry == null) {
            firstTry = matrix;
            return true;
        }
        secondTry = matrix;
        return false;
    }

    public Integer solve() throws Exception {
        Integer foundedValue = null;
        Set<Integer> firstSet = firstTry.getRow();
        Set<Integer> secondSet = secondTry.getRow();
        for (Iterator<Integer> it = firstSet.iterator(); it.hasNext();) {
            Integer value = it.next();
            if (secondSet.contains(value)) {
                if (foundedValue == null) {
                    foundedValue = value;
                } else {
                    throw new Exception("Bad magician!");
                }
            }
        }
        if (foundedValue == null) {
            throw new Exception("Volunteer cheated!");
        }
        return foundedValue;
    }

    @Override
    public String toString() {
        String reply = null;
        try {
            Integer result = this.solve();
            reply = new String("Case #" + solverNumber + ": " + result);
        } catch (Exception ex) {
            reply = new String("Case #" + solverNumber + ": " + ex.getMessage());
        }
        return reply;
    }

}

class Matrix {

    private static Integer matrixRef = 0;
    private Integer selection;
    private Integer[] row;

    private Integer lineToLoad = 0;

    private final Logger logger;

    public Matrix() {
        matrixRef++;
        row = new Integer[4];
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public boolean loadLine(String line) {
        if (selection == null) {
            selection = new Integer(line) - 1;
        } else {
            if (lineToLoad.equals(selection)) {
                Integer col = 0;
                for (StringTokenizer stringTokenizer = new StringTokenizer(line); stringTokenizer.hasMoreTokens();) {
                    String token = stringTokenizer.nextToken();
                    row[col++] = new Integer(token);
                }
            }
            lineToLoad++;
        }
        return !lineToLoad.equals(4);
    }

    public Set<Integer> getRow() {
        Set<Integer> set = new TreeSet<Integer>();
        set.addAll(Arrays.asList(row));
        return set;
    }

}
