import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class GradientDescent {

    static final int NUM_FEATURES = 9;
    static final int MAX_TRAINING = 300;
    static final int MAX_TESTING = 100;
    static final double STEP_SIZE = 0.01;   //Determines the step size
    static final int MAX_ITERATIONS = 500000;
    static final double MAX_ERROR = 0.00001;
    static final int NUM_THETA = NUM_FEATURES*2+1;

    static double x[][] = new double[NUM_THETA][MAX_TRAINING];
    static double z[][] = new double[NUM_THETA][MAX_TESTING];
    static double y[] = new double[MAX_TRAINING];
    static double za[] = new double[MAX_TESTING];
    //static double x[][] = new double[7][300], y[] = new double[300];
    static double theta[] = new double[NUM_THETA];

    public static void main(String[] args) throws IOException {
        //int t;
        /*double theta0;
        double theta1;
        double theta2;
        double theta3;
        double theta4;
        double theta5;
        double theta6;*/


        //initialize theta array
        for (int i = 0; i < NUM_THETA; i++) {
            if (i <= NUM_FEATURES+1) {
                theta[i] = 1;
            } else {
                theta[i] = 0;
            }
        }

        //Read input from "machine.txt" for training
        BufferedReader in = new BufferedReader(new FileReader("condos_training.csv"));
        int curInput = 0;
        while (in.ready()) {
            String text = in.readLine();
            StringTokenizer tokens = new StringTokenizer(text, ",");
            int t = tokens.countTokens();
            //First input is 1
            x[0][curInput] = 1;
            //Reads in all the inputs, except for the last column
            for (int i = 1; i < t; i++) {
                x[i][curInput] = Double.parseDouble(tokens.nextToken());
                //Also store the square of the feature
                x[i+NUM_FEATURES*1][curInput] = Math.pow(x[i][curInput],2);
            }
            //The last input is read into the y vector
            y[curInput] = Double.parseDouble(tokens.nextToken());
            curInput++;
        }
        int numInputs = curInput;
        in.close();



        //Reads in testing file
        BufferedReader out = new BufferedReader(new FileReader("condos_testing.csv"));
        curInput = 0;
        while (out.ready()) {
            String text = out.readLine();
            StringTokenizer tokens = new StringTokenizer(text, ",");
            int t = tokens.countTokens();
            z[0][curInput] = 1;
            for (int i = 1; i < t; i++) {
                z[i][curInput] = Double.parseDouble(tokens.nextToken());
                //Also store the square of the feature
                z[i+NUM_FEATURES*1][curInput] = Math.pow(z[i][curInput],2);
            }
            za[curInput] = Double.parseDouble(tokens.nextToken());
            curInput++;
        }
        int numTest = curInput;
        out.close();

        //Doing feature Scaling
        //TODO convert this into max function
        //Find the maximum value


        for (int i = 0; i < NUM_THETA; i++) {   //0 to 7 must have to do with the theta going from 0 to 7
            //Find the max by theta and
            double max = x[i][0];
            //We looked at the first input for this theta above.  Now search through the rest of the inputs
            for (int j = 1; j < numInputs; j++) {
                if (x[i][j] > max) {
                    max = x[i][j];
                }
            }

            //Scale thetas in inputs from 0 to 1
            for (int j = 0; j < numInputs; j++) {
                if (x[i][j]!=0 || max!=0) {
                    x[i][j] = x[i][j] / max;
                }
            }
            //Scales thetas in test from 0 to 1
            for (int j = 0; j < numTest; j++) {   //0 to 7 must have to do with the theta going from 0 to 7
                if (z[i][j] != 0 || max != 0) {
                    z[i][j] = z[i][j] / max;      //This is originally set to all 0's
                }
            }
        }

        int iteration = 0;
        double derivative;
        double thetaDifference;
        double theta_old[] = new double[NUM_THETA];
        do {
            System.arraycopy(theta,0,theta_old,0,NUM_THETA);
            for (int i = 0; i < NUM_THETA; i++) {
                //Calculating simultaneous derivative and updating
                derivative = getDerivative(i, numInputs, theta);
                //derivative = derivative(i, k, theta0, theta1, theta2, theta3, theta4, theta5, theta6);
                theta[i] = theta[i] - STEP_SIZE * derivative;
            }
            thetaDifference = 0;
            for (int i = 0; i < NUM_THETA; i++) {
                thetaDifference += Math.abs((theta[i]-theta_old[i])/theta[i]);
            }
            thetaDifference = thetaDifference/(NUM_THETA);
            iteration++;
            //System.out.println("theta0 is : " +theta[0]+ "theta1 is :" +theta[1]+ "theta2 is :" +theta[2]+ "theta3 is :" +theta[3]+ "theta4 is :" +theta[4]+ "theta5 is :" +theta[5]+ "theta6 is :" +theta[6]);
        } while (iteration < MAX_ITERATIONS && thetaDifference > MAX_ERROR);    //execute the above loop 400 times

        System.out.println("Final Iterations: " + iteration);
        System.out.println("Final thetaDifference: " + thetaDifference);

        System.out.println("Final Thetas Values::");
        //System.out.println("theta 0 is : " + theta[0] + "\n" + "theta 1 is :" + theta[1] + "\n" + "theta 2 is :" + theta[2] + "\n" + "theta3 is :" + theta[3] + "\n" + "theta4 is :" + theta[4] + "\n" + "theta5 is :" + theta[5] + "\n" + "theta6 is :" + theta[6]);
        String output = "";
        for (int j=0; j < NUM_THETA; j++) {
            output = output + "theta " + j + " is : " + theta[j] + "\n";
        }
        System.out.println(output);

        //Show results from test set
        for (int i=0; i < numTest; i++) {
            //double ans = theta[0] + (theta[1] * z[1]) / 480 + (theta[2] * z[2]) / 8000 + (theta[3] * z[3]) / 64000 + (z[4] * theta[4]) / 128 + (z[5] * theta[5]) / 52 + (z[6] * theta[6]) / 176;
            double ans = 0;
            for (int j=0; j < NUM_THETA; j++) {
                ans += theta[j]*z[j][i];
            }
            System.out.println("Expected Output: " + ans + " Actual Output: " + za[i]);
        }
    }

    //This should be the derivative of the error function since this is what we are minimizing
    //private static double derivative(int i, int k, double a, double b, double c, double d, double e, double f, double h) {
    private static double getDerivative(int curTheta, int numInputs, double[] theta) {
        double sumWeightedError = 0;
        for (int curInput = 0; curInput < numInputs; curInput++) {
            //sum = sum + ((y[g] - (a * x[0][g] + b * x[1][g] + c * x[2][g] + d * x[3][g] + e * x[4][g] + f * x[5][g] + h * x[6][g])) * x[i][g]);
            double curEstimated = 0;
            for (int j = 0; j < NUM_THETA; j++) {
                curEstimated += theta[j] * x[j][curInput];
            }
            double curActual = y[curInput];
            double curError = curActual - curEstimated;
            //This is a weighted sum of the errors.
            //For each input value it calculates the error using the current theta.
            //Then it scales that takes the sum of those errors over all the inputs,
            //  scaled by the input that corresponds to the desired theta
            //Then it takes the average of the errors and returns -2 times that
            sumWeightedError = sumWeightedError + (curError * x[curTheta][curInput]);
        }
        double normalizedError = sumWeightedError / numInputs;      //Average curError*x[curTheta][curInput]
        //If the error is positive, reduce this theta to reduce the error from these points
        //Increasing theta will decrease the error since it will increase curEstimated
        //  We multiply by a negative factor to return the derivative when moving in a positive direction
        return -2 * normalizedError;
    }

}
