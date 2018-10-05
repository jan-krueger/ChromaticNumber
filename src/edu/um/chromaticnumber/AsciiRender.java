package edu.um.chromaticnumber;

public class AsciiRender {

    private char[][] pixels;

    public AsciiRender(int width, int height) {
        this.pixels = new char[width][height];
    }

    public void drawLine(int startX, int startY, int endX, int endY) {

        char c = '-';
        if(startY < endY) c = '\\';
        else if(startY > endY) c = '/';

        final double delta = (endY - startY) / (endX - startX * 1D) ;
        double y = startY;
        if(y >= 0) {
            for (int x = startX; x < endX && y < endY; x++) {
                this.pixels[x][(int) y] = c;
                y += delta;
            }
        } else {
            for (int x = startX; x < endX && y >= endY; x++) {
                this.pixels[x][(int) y] = c;
                y -= delta;
            }
        }
    }

    public String toFrame() {
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < pixels[0].length; y++) {
            for(int x = 0; x < pixels.length; x++) {
                builder.append(pixels[x][y] == 0 ? ' ' : pixels[x][y]);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
