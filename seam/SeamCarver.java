/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    // create a seam carver object based on the given picture
    private Picture currentPicture;
    private Picture changePicture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        currentPicture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return currentPicture;
    }

    // width of current picture
    public int width() {
        return currentPicture.width();
    }

    // height of current picture
    public int height() {
        return currentPicture.height();
    }

    private boolean vaildPixel(int col, int row) {
        if (row < 0 || row >= height())
            return false;
        if (col < 0 || col >= width())
            return false;
        return true;
    }

    private boolean inBound(int col, int row) {
        if (col == 0 || col == width() - 1)
            return true;
        if (row == 0 || row == height() - 1)
            return true;
        return false;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!vaildPixel(x, y))
            throw new IllegalArgumentException();
        // The case that the Pixel is in the border;
        if (inBound(x, y))
            return 1000;
        // The case that the Pixel in the picture except for the border;
        /**
         *    1
         *  0 x 2
         *    3
         */
        Color[] colors = new Color[4];
        int dx[] = { -1, 0, 1, 0 };
        int dy[] = { 0, -1, 0, 1 };
        for (int i = 0; i < 4; i++) {
            colors[i] = currentPicture.get(x + dx[i], y + dy[i]);
        }
        double xSq = (Math.abs(colors[0].getRed() - colors[2].getRed()) * Math.abs(
                colors[0].getRed() - colors[2].getRed())) +
                (Math.abs(colors[0].getGreen() - colors[2].getGreen()) * Math.abs(
                        colors[0].getGreen() - colors[2].getGreen())) +
                (Math.abs(colors[0].getBlue() - colors[2].getBlue()) * Math.abs(
                        colors[0].getBlue() - colors[2].getBlue()));

        double ySq = (Math.abs(colors[1].getRed() - colors[3].getRed()) * Math.abs(
                colors[1].getRed() - colors[3].getRed())) +
                (Math.abs(colors[1].getGreen() - colors[3].getGreen()) * Math.abs(
                        colors[1].getGreen() - colors[3].getGreen())) +
                (Math.abs(colors[1].getBlue() - colors[3].getBlue()) * Math.abs(
                        colors[1].getBlue() - colors[3].getBlue()));
        return Math.sqrt(xSq + ySq);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] En = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                En[i][j] = energy(i, j);
            }
        }

        double[][] energy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = En[j][i];
            }
        }
        int ptr = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < height(); i++) {
            ShortPath temp = new ShortPath(energy, i);
            if (temp.getTotalEnergy() < min) {
                ptr = i;
                min = temp.getTotalEnergy();
            }
        }
        ShortPath sp = new ShortPath(energy, ptr);
        return sp.getPath();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = energy(i, j);
            }
        }
        int ptr = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < width(); i++) {
            ShortPath temp = new ShortPath(energy, i);
            if (temp.getTotalEnergy() < min) {
                ptr = i;
                min = temp.getTotalEnergy();
            }
        }
        ShortPath sp = new ShortPath(energy, ptr);
        return sp.getPath();
    }

    private static boolean checkseam(int[] seam) {
        int n = seam.length;
        for (int i = 1; i < n; i++) {
            if (i + 1 < n && (Math.abs(seam[i] - seam[i - 1]) > 1
                    || Math.abs(seam[i] - seam[i + 1]) > 1))
                return false;
        }
        return true;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // Check for the arguments;
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != width())
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height())
                throw new IllegalArgumentException();
        }
        if (!checkseam(seam))
            throw new IllegalArgumentException();
        if (height() <= 1)
            throw new IllegalArgumentException();
        changePicture = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                changePicture.set(i, j, currentPicture.get(i, j));
            }
            for (int j = seam[i]; j < height() - 1; j++) {
                changePicture.set(i, j, currentPicture.get(i, j + 1));
            }
        }
        currentPicture = new Picture(changePicture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != height())
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width())
                throw new IllegalArgumentException();
        }
        if (width() <= 1)
            throw new IllegalArgumentException();
        if (!checkseam(seam))
            throw new IllegalArgumentException();
        changePicture = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                changePicture.set(j, i, currentPicture.get(j, i));
            }
            for (int j = seam[i]; j < width() - 1; j++) {
                changePicture.set(j, i, currentPicture.get(j + 1, i));
            }
        }
        currentPicture = new Picture(changePicture);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        int[] seam = { 1, 3, 2, 2, 3 };
        System.out.println(checkseam(seam));
        int[] seam1 = { 2, 3, 2, 2, 3 };
        System.out.println(checkseam(seam1));
    }
}
