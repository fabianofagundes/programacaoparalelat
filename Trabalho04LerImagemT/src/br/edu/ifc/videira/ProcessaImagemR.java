/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifc.videira;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Fabiano
 */
public class ProcessaImagemR implements Runnable {

    BufferedImage imagemR;
    private int w, h;

    public ProcessaImagemR(int w, int h, BufferedImage imagem) {
        this.w = w;
        this.h = h;
        this.imagemR = imagem;
    }

    @Override
    public void run() {
        System.out.println("Iniciada Thread: " + Thread.currentThread().getName());
        
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                int rgb = imagemR.getRGB(col, lin);
                Color cor = new Color(rgb);

                Main.pixelsR[w * lin + col] = new Color(cor.getRed(), 0, 0).getRGB();

            }     
        }
        
        imagemR.setRGB(0, 0, w, h, Main.pixelsR, 0, w);
        
        System.out.println("Salvando Red");
        
        try {
            ImageIO.write(imagemR, "PNG", new File("src/br/edu/ifc/videira/imagemR.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ProcessaImagemR.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
}