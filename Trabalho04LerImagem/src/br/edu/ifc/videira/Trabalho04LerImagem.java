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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Fabiano
 */
public class Trabalho04LerImagem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        BufferedImage imagemR = null;
        BufferedImage imagemG = null;
        BufferedImage imagemB = null;        

        try {
            imagemR = ImageIO.read(new File("src/br/edu/ifc/videira/imagem03.jpg"));
            imagemG = ImageIO.read(new File("src/br/edu/ifc/videira/imagem03.jpg"));
            imagemB = ImageIO.read(new File("src/br/edu/ifc/videira/imagem03.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(Trabalho04LerImagem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int w = imagemR.getWidth(); //Largura
        int h = imagemR.getHeight();//Altura
        
        int[] pixelsR = imagemR.getRGB(0, 0, w, h, null, 0, w);
        int[] pixelsG = imagemG.getRGB(0, 0, w, h, null, 0, w);
        int[] pixelsB = imagemB.getRGB(0, 0, w, h, null, 0, w);        
        
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                int rgb = imagemR.getRGB(col,lin);
                Color cor = new Color(rgb);
                
                pixelsR[w * lin + col] = new Color(cor.getRed(), 0, 0).getRGB();
                pixelsG[w * lin + col] = new Color(0, cor.getGreen(), 0).getRGB();
                pixelsB[w * lin + col] = new Color(0, 0, cor.getBlue()).getRGB();
                
            }
        }
        
        imagemR.setRGB(0, 0, w, h, pixelsR, 0, w);
        imagemG.setRGB(0, 0, w, h, pixelsG, 0, w);
        imagemB.setRGB(0, 0, w, h, pixelsB, 0, w);
        
        try {
            ImageIO.write(imagemR, "PNG", new File("src/br/edu/ifc/videira/imagemR.jpg"));
            ImageIO.write(imagemG, "PNG", new File("src/br/edu/ifc/videira/imagemG.jpg"));
            ImageIO.write(imagemB, "PNG", new File("src/br/edu/ifc/videira/imagemB.jpg"));
            
        } catch (IOException ex) {
            Logger.getLogger(Trabalho04LerImagem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
