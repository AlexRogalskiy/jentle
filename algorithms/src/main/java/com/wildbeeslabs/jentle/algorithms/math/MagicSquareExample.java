package com.wildbeeslabs.jentle.algorithms.math;

import Jama.EigenvalueDecomposition;
import Jama.LUDecomposition;
import Jama.Matrix;
import Jama.QRDecomposition;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

/**
 * Example of use of Matrix Class, featuring magic squares.
 **/

public class MagicSquareExample {

    public static Matrix magic(int n) {
        double[][] M = new double[n][n];
        if ((n % 2) == 1) {
            int a = (n + 1) / 2;
            int b = (n + 1);
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    M[i][j] = n * ((i + j + a) % n) + ((i + 2 * j + b) % n) + 1;
                }
            }
        } else if ((n % 4) == 0) {
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (((i + 1) / 2) % 2 == ((j + 1) / 2) % 2) {
                        M[i][j] = n * n - n * i - j;
                    } else {
                        M[i][j] = n * i + j + 1;
                    }
                }
            }
        } else {
            int p = n / 2;
            int k = (n - 2) / 4;
            Matrix A = magic(p);
            for (int j = 0; j < p; j++) {
                for (int i = 0; i < p; i++) {
                    double aij = A.get(i, j);
                    M[i][j] = aij;
                    M[i][j + p] = aij + 2 * p * p;
                    M[i + p][j] = aij + 3 * p * p;
                    M[i + p][j + p] = aij + p * p;
                }
            }
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < k; j++) {
                    double t = M[i][j];
                    M[i][j] = M[i + p][j];
                    M[i + p][j] = t;
                }
                for (int j = n - k + 1; j < n; j++) {
                    double t = M[i][j];
                    M[i][j] = M[i + p][j];
                    M[i + p][j] = t;
                }
            }
            double t = M[k][0];
            M[k][0] = M[k + p][0];
            M[k + p][0] = t;
            t = M[k][k];
            M[k][k] = M[k + p][k];
            M[k + p][k] = t;
        }
        return new Matrix(M);
    }

    public static void main(final String argv[]) {
       /*
        | Tests LU, QR, SVD and symmetric Eig decompositions.
        |
        |   n       = order of magic square.
        |   trace   = diagonal sum, should be the magic sum, (n^3 + n)/2.
        |   max_eig = maximum eigenvalue of (A + A')/2, should equal trace.
        |   rank    = linear algebraic rank,
        |             should equal n if n is odd, be less than n if n is even.
        |   cond    = L_2 condition number, ratio of singular values.
        |   lu_res  = test of LU factorization, norm1(L*U-A(p,:))/(n*eps).
        |   qr_res  = test of QR factorization, norm1(Q*R-A)/(n*eps).
        */
        double eps = Math.pow(2.0, -52.0);
        final StringBuffer sb = new StringBuffer();
        for (int n = 3; n <= 32; n++) {
            sb.append(fixedWidthIntegerToString(n, 7));

            final Matrix M = magic(n);
            int t = (int) M.trace();
            sb.append(fixedWidthIntegerToString(t, 10));

            EigenvalueDecomposition E = new EigenvalueDecomposition(M.plus(M.transpose()).times(0.5));
            double[] d = E.getRealEigenvalues();
            sb.append(fixedWidthDoubleToString(d[n - 1], 14, 3));

            int r = M.rank();
            sb.append(fixedWidthIntegerToString(r, 7));

            double c = M.cond();
            sb.append(c < 1 / eps ? fixedWidthDoubleToString(c, 12, 3) : Double.POSITIVE_INFINITY);

            LUDecomposition LU = new LUDecomposition(M);
            Matrix L = LU.getL();
            Matrix U = LU.getU();
            int[] p = LU.getPivot();
            Matrix R = L.times(U).minus(M.getMatrix(p, 0, n - 1));
            double res = R.norm1() / (n * eps);
            sb.append(fixedWidthDoubleToString(res, 12, 3));

            QRDecomposition QR = new QRDecomposition(M);
            Matrix Q = QR.getQ();
            R = QR.getR();
            R = Q.times(R).minus(M);
            res = R.norm1() / (n * eps);
            sb.append(fixedWidthDoubleToString(res, 12, 3));
            sb.append(StringUtils.LF);
        }
    }

    public static String fixedWidthDoubleToString(double x, int w, int d) {
        final DecimalFormat fmt = new DecimalFormat();
        fmt.setMaximumFractionDigits(d);
        fmt.setMinimumFractionDigits(d);
        fmt.setGroupingUsed(false);

        final StringBuffer sb = new StringBuffer(w);
        final String s = fmt.format(x);
        while (s.length() < w) {
            sb.append(StringUtils.SPACE);
        }
        sb.append(s);
        return sb.toString();
    }

    public static String fixedWidthIntegerToString(int n, int w) {
        final StringBuffer sb = new StringBuffer(w);
        final String s = Integer.toString(n);
        while (s.length() < w) {
            sb.append(StringUtils.SPACE);
        }
        sb.append(s);
        return sb.toString();
    }
}
