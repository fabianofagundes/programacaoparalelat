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
public class ProcessaImagemG implements Runnable {

    BufferedImage imagemG;
    private int w, h;

    public ProcessaImagemG(int w, int h, BufferedImage imagem) {
        this.w = w;
        this.h = h;
        this.imagemG = imagem;
    }

    @Override
    public void run() {
        System.out.println("Iniciada Thread: " + Thread.currentThread().getName());
        
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                int rgb = imagemG.getRGB(col, lin);
                Color cor = new Color(rgb);

                Main.pixelsG[w * lin + col] = new Color(0, cor.getGreen(), 0).getRGB();
            }
        }
        
        imagemG.setRGB(0, 0, w, h, Main.pixelsG, 0, w);
        
        System.out.println("Salvando Green");
        
        try {
            ImageIO.write(imagemG, "PNG", new File("src/br/edu/ifc/videira/imagemG.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ProcessaImagemR.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
}
