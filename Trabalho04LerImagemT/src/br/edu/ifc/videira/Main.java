/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifc.videira;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Fabiano
 */
public class Main {

    public static int[] pixelsR;
    public static int[] pixelsG;
    public static int[] pixelsB;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        BufferedImage imagemR = null;
        BufferedImage imagemG = null;
        BufferedImage imagemB = null;        

        try {
            imagemR = ImageIO.read(new File("src/br/edu/ifc/videira/imagem04.jpg"));
            //imagemG = ImageIO.read(new File("src/br/edu/ifc/videira/imagem04.jpg"));
            //imagemB = ImageIO.read(new File("src/br/edu/ifc/videira/imagem04.jpg"));
            imagemG = deepCopy(imagemR);
            imagemB = deepCopy(imagemR);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int w = imagemR.getWidth(); //Largura
        int h = imagemR.getHeight();//Altura
        
        Main.pixelsR = imagemR.getRGB(0, 0, w, h, null, 0, w);
        Main.pixelsG = imagemG.getRGB(0, 0, w, h, null, 0, w);
        Main.pixelsB = imagemB.getRGB(0, 0, w, h, null, 0, w);        
                
        Thread tr = new Thread(new ProcessaImagemR(w, h, imagemR), "Thread: Red"); 
        tr.start();
        
        Thread tg = new Thread(new ProcessaImagemG(w, h, imagemG), "Thread: Green"); 
        tg.start();
        
        Thread tb = new Thread(new ProcessaImagemB(w, h, imagemB), "Thread: Blue"); 
        tb.start();
    }
    
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
