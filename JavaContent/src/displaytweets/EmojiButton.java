/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displaytweets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kenan
 */
public class EmojiButton extends javax.swing.JToggleButton{
    
    private int iconHeight;
    private int iconWidth;
    private String imgName;
    
    // Constructor for buttons - pass in the Filename of a picture
    public EmojiButton(File imgFile) {
        initButton(imgFile);
    }
    
    private void initButton(File imgFile) {
        // Set the icon size
        iconHeight = 20;
        iconWidth = 20;
        
        setImgName(imgFile);
        try {
            BufferedImage img;
            img = ImageIO.read(imgFile);
            if (img.getHeight() < img.getWidth())
                iconHeight = -1;
            else if (img.getHeight() > img.getWidth())
                iconWidth = -1;
            setIcon(new javax.swing.ImageIcon(img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            setText(imgName);
        }
    }
    
    private void setImgName(File imgFile) {
        String tempName = imgFile.getName().replace(".JPG", "");
        String resultName = "";
        int cur = 0;
        while (cur <= tempName.length()-4) {
            if (tempName.charAt(cur) == '1') {
                resultName = resultName.concat("U+").concat(tempName.substring(cur,cur+5));
                cur = cur + 5;
            } else {
                resultName = resultName.concat("U+").concat(tempName.substring(cur,cur+4));
                cur = cur + 4;
            }
        }
        
        imgName = resultName;
    }
    
    public String getImgName() {
        return imgName;
    }
}
