package funny;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class Funny extends JFrame {

    //graphics 
    public Image fondo;
    public BufferedImage bufferedImage;
    public Graphics graPixel;
    //Booleanos
    public boolean thbool = true; //hilo
    public boolean fBool = true; //Fondo
    public boolean edBool = true; //edificios
    //Colores
    public int greenFondo = 255, blueFondo = 0, redFondo = 255;//fondo
    //Edificios {x,y}
    public int dimensionesEd[][] = new int[24][24];
    //public int dimensionesEd[][] = {{23, 15, 35}, {150, 75, 120}};
    public int xEdificios = 0;
    //luces
    public int cantidadX = 0, cantidadY = 0, windX = 4, windY = 5;

    public static void main(String[] args) {
        new Funny().setVisible(true);
    }

    public Funny() {
        initComponents();
        this.setContentPane(new Graficos());//llamar
        this.setLocationRelativeTo(null);
        Hilos comenzar = new Hilos();
        comenzar.start();
    }

    public void initComponents() {
        //Default JFrame 
        this.setTitle("Graficos Divertidos");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);
        //Dimensionamiento Jframe
        Toolkit myWindow = Toolkit.getDefaultToolkit();
        Dimension dimWindow = myWindow.getScreenSize();
        this.setSize(dimWindow.width / 2, dimWindow.height / 2);
        this.setLocation(dimWindow.width / 4, dimWindow.height / 4);
    }

    public class Hilos extends Thread implements Runnable {

        @Override
        public void run() {
            while (thbool) {
                try {
                    if (greenFondo == 0 || fBool == false) {
                        fBool = false;
                        fondoIncrement();
                    }
                    if (greenFondo == 255 || fBool == true) {
                        fBool = true;
                        fondoIncrese();
                    }
                    repaint();
                    sleep(100);
                } catch (Exception e) {
                    System.out.println("Error en el llamar paint en hilos");
                }
            }
        }

        //Cambiar COlor de Fondo
        public void fondoIncrese() {
            if (greenFondo <= 105) {
                redFondo--;
            }
            greenFondo--;
            blueFondo++;
        }

        public void fondoIncrement() {
            if (greenFondo <= 104) {
                redFondo++;
            }
            greenFondo++;
            blueFondo--;
        }

    }

    public class Graficos extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);

            fondo = createImage(getWidth(), getHeight());
            int alto = fondo.getHeight(this);
            int largo = fondo.getWidth(this);

            //asignarle los graficos 2D con el bufer de graphics
            graPixel = (Graphics2D) g;

            fondo(alto, largo);
            ciudad(alto, largo);

            update(g);
        }

        public void fondo(int alto, int largo) {
            Color color = new Color(redFondo, greenFondo, blueFondo);
            graPixel.setColor(color);
            graPixel.fillRect(0, 0, largo, alto);
        }

        public void ciudad(int alto, int largo) {
            //variables de los edificios
            int cantidadEdificios = dimensionesEd[0].length;
            int edPos = 0, edY = 0, edX = 0;
            //calcular cantidad de edificios por el layout
            if (edBool) {
                for (int i = 0; i < cantidadEdificios; i++) {
                    int largoEd = 15 + (int) (Math.random() * (largo * 0.055));
                    int altoEd = 15 + (int) (Math.random() * (alto * .40));
                    dimensionesEd[0][i] += largoEd;
                    dimensionesEd[1][i] += altoEd;
                    xEdificios += dimensionesEd[0][i];
                }
                edBool = false;
            }

            //dibujar edificios
            int jFrameEdificios = Math.abs(largo / xEdificios) * cantidadEdificios;
            for (int i = 0; i < jFrameEdificios; i++) {
                if (edPos > cantidadEdificios - 1) {
                    edPos = 0;
                }
                edY = alto - dimensionesEd[1][edPos];
                graPixel.setColor(Color.BLACK);//color
                graPixel.fillRect(edX, edY, dimensionesEd[0][edPos], dimensionesEd[1][edPos]);
                //dibujar luces
                dibujarLuces(edX, edY, dimensionesEd[0][edPos], dimensionesEd[1][edPos]);
                edX += dimensionesEd[0][edPos];
                edPos++;
            }
        }

        public void update(Graphics g) {

        }

        //dibujar puntos de estrellas
        public void dibujarLuces(int x, int y, int x1, int y1) {
            Random random = new Random();
            int redorridoY = y;
            graPixel.setColor(Color.white);
            //determinar cantidades
            cantidadX = Math.abs((x1) / (windX)) - 1;
            cantidadY = Math.abs((y1) / (windY)) + 2;
            //dibujar ventanas 
            for (int j = 0; j < cantidadY; j++) {
                int recorridoX = x;
                for (int i = 0; i <= cantidadX; i++) {
                    if (random.nextBoolean()) {
                        graPixel.fillRect(recorridoX + 1, redorridoY + 1, 1, 1);
                    }
                    recorridoX += windX;
                }
                redorridoY += windY;
            }
        }
    }
}
