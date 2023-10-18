package org.example;

import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class FractalExplorer {
    private int jImageDisplaySize;
    private JImageDisplay jImageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double rectangle2D;
    
    public FractalExplorer(int size) {
        jImageDisplaySize = size;
        fractalGenerator = new Mandelbrot();
        rectangle2D = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(rectangle2D);
        jImageDisplay = new JImageDisplay(jImageDisplaySize, jImageDisplaySize);
    }
    public void createAndShowGUI() {
        jImageDisplay.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer.");
        frame.add(jImageDisplay, BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset scale.");
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);
        frame.add(resetButton, BorderLayout.SOUTH);
        MouseHandler click = new MouseHandler();
        jImageDisplay.addMouseListener(click);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private class ResetHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            fractalGenerator.getInitialRange(rectangle2D);
            drawFractal();
        }
    }

    private void drawFractal() {
        for (int x = 0, y = 0; x < jImageDisplaySize; x++, y = 0)
            for (float i = (float) fractalGenerator.numIterations(FractalGenerator.getCoord(rectangle2D.x, rectangle2D.x +
                rectangle2D.width, jImageDisplaySize, x), FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y +
                rectangle2D.height, jImageDisplaySize, y)); y < jImageDisplaySize; y++, i = (float) fractalGenerator.numIterations(FractalGenerator.getCoord(rectangle2D.x, rectangle2D.x +
                rectangle2D.width, jImageDisplaySize, x), FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y +
                rectangle2D.height, jImageDisplaySize, y)))
                    jImageDisplay.drawPixel(x, y, i < 0 ?256:Color.HSBtoRGB(0.7f + i / 200f, 1f, 1f));
        jImageDisplay.repaint();
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            fractalGenerator.recenterAndZoomRange(rectangle2D, FractalGenerator.getCoord(rectangle2D.x,rectangle2D.x + rectangle2D.width, jImageDisplaySize, x),
                    FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y + rectangle2D.height, jImageDisplaySize, y), 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fractalGenerator = new FractalExplorer(800);
        fractalGenerator.createAndShowGUI();
        fractalGenerator.drawFractal();
    }
}