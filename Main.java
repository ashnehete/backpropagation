import java.util.Scanner;
import java.io.*;
class Main {
	
	File file;
	FileWriter fileWriter;
	PrintWriter printWriter;
	
	public Main() throws IOException {
		file = new File("input.txt");
		fileWriter = new FileWriter("output.txt");
		printWriter = new PrintWriter(fileWriter);
	}
	
    public static void main(String[] args) {
        double precision = 0.99;
        if (args.length > 0) {
            precision = Double.parseDouble(args[0]);
        }

        Main main;
        try {
            main = new Main();
            main.run(precision);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(double precision) throws FileNotFoundException {
        // Input
        double x1, x2, t, alpha, v01, v02, v11, v12, v21, v22, w0, w1, w2;

        // Output
        double zIn1, zIn2, z1, z2, yIn, y = 0, dK, dW1, dW2, dW0, dIn1, dIn2, d1, d2, dV01, dV02, dV11, dV12, dV21, dV22;

        Scanner sc = new Scanner(new File("input.txt"));

        x1 = sc.nextDouble();
        x2 = sc.nextDouble();
        t = sc.nextDouble();
        alpha = sc.nextDouble();
        v01 = sc.nextDouble();
        v02 = sc.nextDouble();
        v11 = sc.nextDouble();
        v12 = sc.nextDouble();
        v21 = sc.nextDouble();
        v22 = sc.nextDouble();
        w0 = sc.nextDouble();
        w1 = sc.nextDouble();
        w2 = sc.nextDouble();

        printHeader();
		print(w0, w1, w2, v01, v02, v11, v12, v21, v22, y);

        int i = 0;
        while (y < precision) {
            zIn1 = v01 + x1 * v11 + x2 * v21;
            zIn2 = v02 + x1 * v12 + x2 * v22;

            z1 = sigmoidal(zIn1);
            z2 = sigmoidal(zIn2);

            yIn = w0 + z1 * w1 + z2 * w2;
            y = sigmoidal(yIn);

            dK = (t - y) * y * (1 - y);
            dW1 = alpha * dK * z1;
            dW2 = alpha * dK * z2;
            dW0 = alpha * dK;

            dIn1 = dK * w1;
            dIn2 = dK * w2;
            d1 = sigmoidal(dIn1);
            d2 = sigmoidal(dIn2);

            dV11 = dV12 = alpha * d1 * x1;
            dV21 = dV22 = alpha * d2 * x2;
            dV01 = alpha * d1;
            dV02 = alpha * d2;

            w0 += dW0;
            w1 += dW1;
            w2 += dW2;
            v01 += dV01;
            v02 += dV02;
            v11 += dV11;
            v12 += dV12;
            v21 += dV21;
            v22 += dV22;
			
            i++;
        }
        print(w0, w1, w2, v01, v02, v11, v12, v21, v22, y);
        System.out.println("Count: " + i);
    }

    public void printHeader() {
        System.out.println("w0\tw1\tw2\tv01\tv02\tv11\tv12\tv21\tv22\ty");
		printWriter.println("w0\tw1\tw2\tv01\tv02\tv11\tv12\tv21\tv22\ty");
    }

    public void print(double w0, double w1, double w2,
							 double v01, double v02, double v11, double v12, double v21, double v22,
							double y) {
        System.out.printf("%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\n",
            w0, w1, w2, v01, v02, v11, v12, v21, v22, y);
		printWriter.printf("%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\t%.4f\n",
            w0, w1, w2, v01, v02, v11, v12, v21, v22, y);
    }

    public double sigmoidal(double x) {
        return 1d / (1 + Math.exp(-x));
    }
}
