public class Lab1 {
    public static void main(String[] args) {
        final int ARR1_SIZE = 20;
        final int ARR2_SIZE = 10;
        int[] d = new int[ARR1_SIZE];
        double[] x = new double[ARR1_SIZE];

        for (int i = 0; i < ARR1_SIZE; i++) {
            d[i] = i + 1;
        }

        for (int i = 0; i < ARR2_SIZE; i++) {
            x[i] = randInBounds(-3, 3);
        }

        double[][] r = new double[ARR1_SIZE][ARR2_SIZE];

        for (int i = 0; i < ARR1_SIZE; i++) {
            for (int j = 0; j < ARR2_SIZE; j++) {
                switch (d[i]) {
                    case 4:
                        r[i][j] = f1(x[j]);
                        break;
                    case 2: case 3: case 6: case 7:
                    case 9: case 12: case 15: case 17:
                    case 19: case 20:
                        r[i][j] = f2(x[j]);
                        break;
                    default:
                        r[i][j] = f3(x[j]);
                }
				printNum(r[i][j]);
            }
            System.out.println();
        }
    }

    public static double randInBounds(double x, double y) {
        return (double) (Math.random() * (y - x) - x);
    }
	public static double f1(double x) {
		return (double) Math.atan(Math.sin(3 * (x - 4)));
	}
	public static double f2(double x) {
		return (double) Math.exp(Math.pow(2 * Math.pow(x, (double)1/3), 2));
	}
	public static double f3(double x) {
		return (double) Math.tan(Math.log(Math.pow(Math.cos((double) 4 / ((double) 1 / 3 - Math.atan((double) x / 6))), 2)));
	}
	public static void printNum(double x) {
		if (x < 1.00e+10) {
			System.out.printf("'%4.2f' ", x);
        } 
		else {
			System.out.printf("'%4.2e' ", x);
        }
	}
}
