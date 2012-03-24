                                                                                                                                          
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ImageReaderV implements MouseListener, MouseMotionListener 
{  
   public static void main(String[] args) 
   {
           //String fileName = args[0];
           //int width = Integer.parseInt(args[1]);
           //int height = Integer.parseInt(args[2]);
           
           int width = 960;
           int height = 540;
           String fileName = "C:/Users/tanya/Desktop/vdo.rgb";
           
           ImageReaderV ir = new ImageReaderV(width, height, fileName);
   }
   
   public ImageReaderV(int width, int height, String fileName)
   {
    
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        JLabel label = new JLabel(new ImageIcon(img));
        label.setPreferredSize(new Dimension(width,height));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        label.addMouseListener(this);
        label.addMouseMotionListener(this);

        // Bottons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(width, 50));
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        MyButton splitButton = new MyButton("Split");
        buttonPanel.add(splitButton, BorderLayout.WEST);

        MyButton initButton = new MyButton("Initialize");
        buttonPanel.add(initButton, BorderLayout.WEST);
        
        MyButton resetButton = new MyButton("Reset");
        buttonPanel.add(resetButton, BorderLayout.WEST);
        
        MyButton closeButton = new MyButton("Close");
        buttonPanel.add(closeButton, BorderLayout.WEST);    
        
        frame.pack();
        frame.setVisible(true);
        int ind=0;
       // byte[] bytes;
        
        try
        {
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);

            long len = file.length();
            System.out.println(len);
            byte[] bytes = new byte[(int)len];
        
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
      //  height =320;
       // width = 240;
        
            while(true)
            {
                for(int i=1;i<=500; i++)
                {
        
                    int temp = height*width;
     
                    for(int y = 0; y < height; y++)
                    {
        
                        for(int x = 0; x < width; x++)
                        {
             
                                byte a = 0;
                                byte r = bytes[ind];
                                byte g = bytes[ind+temp];
                                byte b = bytes[ind+temp*2]; 
                    
                                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    
                                img.setRGB(x,y,pix);
                                if(ind<len)
                                ind++;
                        }
                    }
                    
                     // Use a label to display the image
                    label.setIcon(null);
                    label.revalidate();         
                    label.setIcon(new ImageIcon(img));
                    label.setPreferredSize(new Dimension(width,height));
                    frame.getContentPane().add(label, BorderLayout.CENTER);
                    label.revalidate();
                    frame.revalidate();
                    frame.pack();
                    frame.setVisible(true);
            
                    //if(i==10)
                    try{
                        Thread.sleep(50L);}catch(Exception e){}
                    temp=temp + height*width*2;
                    ind=ind + height*width*2;
                    if(ind>=len)
                        break;
                    System.out.println(ind +" "+ height*width + " "+ i);
                }
                break;
            }
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
  
   }
   
   // Function calls
    public void buttonPressed(String name)
    {
        if (name.equals("Split"))
        {
            //System.out.println("Split");
        } else if (name.equals("Initialize"))
        {
            //System.out.println("Initialize");
        } else if (name.equals("Reset"))
        {
            //System.out.println("Reset");
        } else if (name.equals("Close"))
        {
            //System.out.println("Close");
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        //System.out.println(arg0.getX() + " and " + arg0.getY());
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
        //System.out.println("Moving");
    } 
    
    class MyButton extends JButton {
        MyButton(String label){
            setFont(new Font("Helvetica", Font.BOLD, 10));
            setText(label);
            addMouseListener(
                new MouseAdapter() {
                      public void mousePressed(MouseEvent e) 
                      {
                        buttonPressed(getText());
                    }
                }
            );
        }
        
        MyButton(String label, ImageIcon icon){
            Image img = icon.getImage();
            Image scaleimg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaleimg));
            setText(label);
            setFont(new Font("Helvetica", Font.PLAIN, 0));
            addMouseListener(
                new MouseAdapter() {
                      public void mousePressed(MouseEvent e) {
                        buttonPressed(getText());
                    }
                }
            );
        }
    }
}
