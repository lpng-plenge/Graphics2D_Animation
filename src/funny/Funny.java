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
    public boolean eBool = true; //Estrellas
    public boolean edBool = true; //edificios
    //Colores
    public int greenFondo = 255, blueFondo = 0, redFondo = 255;//fondo
    //Edificios {x,y} se modifica dimensiones
    public int dimensionesEd[][] = new int[25][25];
    public int xEdificios = 0;
    //ventanas solo se modifica wind
    public int cantidadX = 0, cantidadY = 0, windX = 9, windY = 15;
    //estrellas 
    public int cantEstrellas = 300; //entre mas mostrara menos
    //sol y luna
    public int posSunMoonX = 1, posSunMoonY = 1, whSunMoon = 145;
    //nubes
    public int nubesX[] = {0, 300,600};
    public int cantidadNubes=120;
    //planeta
    public int posPlanetX = -100;

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
        void fondoIncrese() {
            posSunMoonY++;
            //atardecer
            if (greenFondo <= 105) {
                redFondo--;
                greenFondo--;
                blueFondo++;
                //aparecer estrellas
                eBool = true;
            } else {
                greenFondo--;
                blueFondo++;
                //se oculta estrellas
                eBool = false;
            }
        }

        void fondoIncrement() {
            posSunMoonY--;
            //amanecer
            if (greenFondo <= 104) {
                redFondo++;
                greenFondo++;
                blueFondo--;
                //aparecer estrellas
                eBool = true;
            } else {
                greenFondo++;
                blueFondo--;
                eBool = false;//estrellas
            }
        }
    }

    public class Graficos extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);

            fondo = createImage(getWidth(), getHeight());
            int alto = fondo.getHeight(this);
            int largo = fondo.getWidth(this);

            update(g, largo, alto);
        }

        public void update(Graphics g, int largo, int alto) {
            //asignarle los graficos 2D con el bufer de graphics
            graPixel = (Graphics2D) g;
            graPixel.drawImage(fondo, alto, alto, this);
            fondo(alto, largo);
            estrellas(alto, largo);
            lunaSol(alto, largo);
            planeta(alto, largo);
            nubes(alto, largo);
            ciudad(alto, largo);
        }

        //Dibujar Fondo
        void fondo(int alto, int largo) {
            Color color = new Color(redFondo, greenFondo, blueFondo);
            graPixel.setColor(color);
            graPixel.fillRect(0, 0, largo, alto);
        }

        //Dibujar estrellas
        void estrellas(int alto, int largo) {
            if (eBool) {
                //Calcular las posiciones del Jframe
                int posicionesx = largo;
                Random random = new Random();
                graPixel.setColor(Color.LIGHT_GRAY);
                //Dibujar Estrellas
                for (int j = 0; j < alto; j++) {
                    int esX = 0;
                    if (random.nextBoolean()) {
                        for (int i = 0; i <= posicionesx; i++) {
                            if (random.nextBoolean()) {
                                graPixel.fillRect(esX, j, 1, 1);
                            }
                            esX += (int) Math.floor(Math.random() * (cantEstrellas) + 5);
                        }
                    }
                }
            }
        }

        //Dibujar Sol
        void lunaSol(int alto, int largo) {
            int xPosSol = (posSunMoonX) * Math.abs(largo / 255);
            int yPosSol = posSunMoonY * Math.abs(alto / 125);

            //xPosSol = posSunMoonX;
            if (xPosSol > largo) {
                posSunMoonX = 0;
            }
            //sol 
            graPixel.setColor(Color.WHITE);
            graPixel.fillOval(xPosSol, yPosSol, whSunMoon, whSunMoon);
            posSunMoonX++;
        }

        //Dibujar Planeta
        void planeta(int alto, int largo) {
            graPixel.setColor(Color.WHITE);
            int xpos = posPlanetX, ypos = Math.abs(alto / 10);

            if (xpos == largo) {
                posPlanetX = -100;
            } else {
                graPixel.setColor(new Color(217, 187, 184));
                graPixel.drawOval(xpos, ypos + 20, 100, 10);//anillos
                graPixel.setColor(new Color(191, 54, 4));
                graPixel.fillOval(xpos + 25, ypos + 5, 50, 40);//planeta
                posPlanetX++;
            }

        }

        //Dibujar Nubes
        void nubes(int alto, int largo) {
            //nubes
            graPixel.setColor(new Color(242, 242, 242));
            int altura = (int) alto / 10;
            int altura2 = (int) (alto / 10) - 5;
            int tam = nubesX.length;
            
            for (int i = 0; i < tam; i++) {
                if (nubesX[i] >= largo/2) {
                    nubesX[i] = -150;
                }
            }
            int tam2 = alto / cantidadNubes;
            for (int j = 0; j < tam2; j++) {
                for (int i = 0; i < tam; i++) {
                    int xPosNube = (nubesX[i]) * Math.abs(largo / 255);
                    int xPosTop = ((nubesX[i]) * Math.abs(largo / 255)) + 50;
                    int multiplo = (int) 60 * j;
                    graPixel.fillOval(xPosNube -multiplo, altura + multiplo, 150, 10);
                    graPixel.fillOval(xPosTop -multiplo, altura2 + multiplo, 50, 10);
                    nubesX[i] += 1;
                }
            }

        }

        //dibujar ciudad
        void ciudad(int alto, int largo) {
            //variables de los edificios
            int cantidadEdificios = dimensionesEd[0].length;
            int edPos = 0, edY = 0, edX = 0;
            //calcular cantidad de edificios por el layout
            if (edBool) {
                for (int i = 0; i < cantidadEdificios; i++) {
                    int largoEd = 18 + (int) (Math.random() * (largo * 0.035));
                    int altoEd = 15 + (int) (Math.random() * (alto * .60));
                    dimensionesEd[0][i] += largoEd;
                    dimensionesEd[1][i] += altoEd;
                    xEdificios += dimensionesEd[0][i];
                }
                edBool = false;
            }

            //Calcular la cantida de repeticiones
            int jFrameEdificios = Math.abs(largo / xEdificios) * cantidadEdificios;
            //Centrado
            if (jFrameEdificios == cantidadEdificios * 2) {
                edX = Math.abs(largo - xEdificios * 2) / 2;
            } else {
                edX = Math.abs(largo - xEdificios) / 2;
            }
            //Dibujar Edificios
            for (int i = 0; i < jFrameEdificios; i++) {
                if (edPos > cantidadEdificios - 1) {
                    edPos = 0;
                }
                edY = alto - dimensionesEd[1][edPos];
                graPixel.setColor(Color.BLACK);//color
                graPixel.fillRect(edX, edY, dimensionesEd[0][edPos], dimensionesEd[1][edPos]);
                //dibujar ventanas
                dibujarVentanas(edX, edY, dimensionesEd[0][edPos], dimensionesEd[1][edPos]);
                edX += dimensionesEd[0][edPos];
                edPos++;
            }
        }

        //dibujar ventanas
        void dibujarVentanas(int x, int y, int x1, int y1) {
            Random random = new Random();
            int redorridoY = y;
            //colors
            Color color = new Color(141,130,66);
            graPixel.setColor(color);

            //CAntidad de repeticiones
            cantidadX = Math.abs((x1) / (windX));
            cantidadY = Math.abs((y1) / (windY)) + 2;
            //dibujar ventanas 
            for (int j = 0; j < cantidadY; j++) {
                int recorridoX = x;
                for (int i = 0; i <= cantidadX; i++) {
                    if (random.nextBoolean()) {
                        graPixel.fillRect(recorridoX, redorridoY + 1, 1, 1);
                    }
                    recorridoX += windX;
                }
                redorridoY += windY;
            }
        }

    }
}
