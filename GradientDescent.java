import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class GradientDescent {

    static double x[][] = new double[7][300], y[] = new double[300];
    static double alpha = 0.01, theta[] = new double[7];

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        int t;
        int i = 0;
        int k = 0;
        int w = 0;
        double deriva;
        double theta0;
        double theta1;
        double theta2;
        double theta3;
        double theta4;
        double theta5;
        double theta6;
        double z[] = new double[100];

        //initialize theta array
        for (i = 0; i < 7; i++) {
            theta[i] = 0;
        }

        //Read input from "machine.txt" for training
        BufferedReader in = new BufferedReader(new FileReader("machine.txt"));

        while (in.ready()) {
            String text = in.readLine();
            StringTokenizer tokens = new StringTokenizer(text, ",");
            t = tokens.countTokens();
            x[0][k] = 1;
            for (i = 1; i < t; i++) {
                x[i][k] = Double.parseDouble(tokens.nextToken());
            }
            y[k] = Double.parseDouble(tokens.nextToken());
            k++;
        }
        //Doing feature Scaling
        int p = 0;
        for (i = 0; i < 7; i++) {
            double max = x[i][p];
            for (p = 1; p < k; p++) {
                if (x[i][p] > max) {
                    max = x[i][p];
                }
            }
            z[i] = z[i] / max;
            for (p = 0; p < k; p++) {
                x[i][p] = x[i][p] / max;
            }
        }
        do {
            theta0 = theta[0];
            theta1 = theta[1];
            theta2 = theta[2];
            theta3 = theta[3];
            theta4 = theta[4];
            theta5 = theta[5];
            theta6 = theta[6];
            for (i = 0; i < 7; i++) {
                //Calculating Simultaneous derivating and updating
                deriva = derivative(i, k, theta0, theta1, theta2, theta3, theta4, theta5, theta6);
                theta[i] = theta[i] - alpha * deriva;
            }
            w++;
            //System.out.println("theta0 is : " +theta[0]+ "theta1 is :" +theta[1]+ "theta2 is :" +theta[2]+ "theta3 is :" +theta[3]+ "theta4 is :" +theta[4]+ "theta5 is :" +theta[5]+ "theta6 is :" +theta[6]);
        }
        while (w < 400);
        System.out.println("Final Thetas Values::");
        System.out.println("theta 0 is : " + theta[0] + "\n" + "theta 1 is :" + theta[1] + "\n" + "theta 2 is :" + theta[2] + "\n" + "theta3 is :" + theta[3] + "\n" + "theta4 is :" + theta[4] + "\n" + "theta5 is :" + theta[5] + "\n" + "theta6 is :" + theta[6]);
        BufferedReader out = new BufferedReader(new FileReader("testing.txt"));
        while (out.ready()) {
            String text = out.readLine();
            StringTokenizer tokens = new StringTokenizer(text, ",");
            t = tokens.countTokens();
            z[0] = 1;
            for (i = 1; i <= t; i++) {
                z[i] = Double.parseDouble(tokens.nextToken());
            }
            double ans = theta[0] + (theta[1] * z[1]) / 480 + (theta[2] * z[2]) / 8000 + (theta[3] * z[3]) / 64000 + (z[4] * theta[4]) / 128 + (z[5] * theta[5]) / 52 + (z[6] * theta[6]) / 176;
            System.out.println("Expected Output :" + ans + " Actual Output :" + z[7]);
        }
        in.close();

    }

    private static double derivative(int i, int k, double a, double b, double c, double d, double e, double f, double h) {
        // TODO Auto-generated method stub
        int g;
        double sum = 0;
        for (g = 0; g < k; g++) {
            sum = sum + ((y[g] - (a * x[0][g] + b * x[1][g] + c * x[2][g] + d * x[3][g] + e * x[4][g] + f * x[5][g] + h * x[6][g])) * x[i][g]);
        }
        return -2 * sum / k;
    }

}
