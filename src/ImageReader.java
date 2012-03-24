import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ImageReader implements MouseListener, MouseMotionListener 
{  
	private static final int TYPE_INT_ARGB = 0;
	BufferedImage scaledimg;
	
	static String fileName;
	static int xtiles=3;
	static int ytiles=3;
	int sheight,swidth;
	JFrame frame;
	JLabel label;
	JLabel[] label1;
	JPanel videoPanel, tileimage;
	BufferedImage img;
	BufferedImage[] timgs;
	boolean split = false;
	boolean initialize = false;
	int rand; 
	ArrayList<Integer> list;
	boolean reset =false;
	static String type;									
	static float scalef;
	
   public static void main(String[] args) 
   {
	   	String fileName = args[0];
   		xtiles = Integer.parseInt(args[1]);
   		ytiles = Integer.parseInt(args[2]);
   		scalef = Float.parseFloat(args[3]);
   		
   		int width = 960;
   		int height = 540;
   	
   		try
   		{
	        File file = new File(fileName);	
	        InputStream is = new FileInputStream(file);
	       
	        //Checking if the file is a video file or an image file
	        
	        if(file.length() == 1555200)
	        {
	        	//System.out.println("Image");
	        	type = "Image";
	        }
	        else if (file.length()>1555200)
	        {
	        
	        	//System.out.println("Video");
	        	type = "Video";
	        }
   		}catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (@SuppressWarnings("hiding") IOException e){
            e.printStackTrace();
        }
       
   		ImageReader ir = new ImageReader(width, height, fileName, type );
   }
   
   public ImageReader(int width, int height, String fileName, String type)
   {	   
	   swidth = (int) (width * scalef);
	   sheight = (int) (height * scalef);
  	
	   if(type == "Image")
	   {
		   BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		   scaledimg = new BufferedImage(swidth, sheight, BufferedImage.TYPE_INT_RGB);
		   //Reading File
		   try 
		   {
			   File file = new File(fileName);
			   InputStream is = new FileInputStream(file);
			  	
			   long len = file.length();
			   byte[] bytes = new byte[(int)len];
			  		    
			   int offset = 0;
			   int numRead = 0;
			   while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			  	            offset += numRead;
	  	   }
	  	   int ind = 0; 		
	  	   for(int y=0; y < height; y++)
	  	   {
	  		   for(int x= 0; x < width; x++)
	  		   { 
	  			   byte a = 0;
	  			   byte r = bytes[ind];
	  			   byte g = bytes[ind+height*width];
	  			   byte b = bytes[ind+height*width*2]; 
	  					
	  			   int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	  			   //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
	  			   img.setRGB(x,y,pix);
	  			   
	  			   //Scaling the image according to the given scalefactor
	  			   scaledimg.setRGB((int)(x*scalef),(int)(y*scalef),pix);
	  			   ind++;
	  			}
	  	   }		
		   } catch (FileNotFoundException e) {
	  	      e.printStackTrace();
	  	    } catch (IOException e) {
	  	      e.printStackTrace();
	  	    }
	  	  
	  	    
		    // Use a label to display the image
		     frame = new JFrame();
		     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		     label = new JLabel(new ImageIcon(scaledimg));
		     label.setPreferredSize(new Dimension(swidth,sheight));
		     frame.getContentPane().add(label, BorderLayout.CENTER);

		     // Buttons
			 JPanel buttonPanel = new JPanel();
			 buttonPanel.setPreferredSize(new Dimension((int)(width*scalef), 50));
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
	   }
	   else 
	   {
		   img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
	       frame = new JFrame();
	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	
	       label = new JLabel(new ImageIcon(img));
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
	      
	   while(true)
       {
		   ind=0;
		   try
		   {
	           File file = new File(fileName);
	           InputStream is = new FileInputStream(file);
	
	           long len = file.length();
	           //System.out.println(len);
	           byte[] bytes = new byte[(int)len];
	       
	           int offset = 0;
	           int numRead = 0;
	           while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	               offset += numRead;
           }
        	   
           for(int i=1;i<=100; i++)
           {
       
               int c = height*width;
               //System.out.println("ind = " + ind);
               for(int y = 0; y < height; y++)
               {
                   for(int x = 0; x < width; x++)
                   {
                       byte a = 0;
                       byte r = bytes[ind];
                       byte g = bytes[ind+c];
                       byte b = bytes[ind+c*2]; 
                       int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                   
                       img.setRGB(x,y,pix);
                      
                       if(ind<len)
                    	   ind++;
                    }
                }
               

               c=c + height*width*2;
               ind=ind + height*width*2;
              
               if(ind>=len)
               		ind=0;
                   
                try
                {
                    Thread.sleep(100);
                }catch(Exception e){
                  
                } 
                
                if((split==false && initialize == false) || reset==true) 
                {
                	// Use a label to display the image
                	//when the puzzle has just started
                	
                	label.setIcon(null);
                	label.revalidate();         
                	label.setIcon(new ImageIcon(img));
                	label.setPreferredSize(new Dimension(width,height));
                	frame.getContentPane().add(label, BorderLayout.CENTER);
                	label.revalidate();
                	frame.pack();
                	frame.setVisible(true);
                }   
                else
                {
                	//Splitting the video
                	
	               	int chunks = xtiles * ytiles;  //Total no. of tiles the image/video is broken into
	       			int h2 = sheight/ytiles;
	       			int w2 = swidth/xtiles;
	       		  
	       			int count = 0;
	       			int x,y,pix1,x1,y1;
	       			x=x1 =0;
	       			y=y1=0;
	       		    timgs = new BufferedImage[chunks]; //Image array to hold image chunks  
	       		 	timgs[count] = new BufferedImage(w2,h2,BufferedImage.TYPE_INT_ARGB );  
	       		 		
	       		 	for (int rows=0; rows<ytiles; rows++)
	       		 	{		
	       		 		x = 0;
       			    	for (int cols=0; cols<xtiles; cols++,count++)
       			    	{
       			    		timgs[count] = new BufferedImage(w2,h2,img.getType());
       			    		for (int k=0; k<h2; k++,y++)
       			    		{
       			    			for (int l=0; l<w2; l++,x++)
       			    			{
       			    				pix1 = img.getRGB(x, y);
       			    				timgs[count].setRGB(l, k,pix1);
       			    			}
       			    			x=x1; //Getting to the top of next tile image
       			    		}
       			    		y=y1;
       			    		//Adding the width of one tile(w2) to reach the starting of next column of tiles
       			    		x = (cols+1)*w2; 
       			    		x1=x;
       			    	}
       			    	//Adding the height of next tile to reach to next row of tile
       			    	y = (rows+1)*h2;
       		    		y1=y;
       		    		x1=0;
       			    }
       		       
	       		 	if(split==true  && initialize==false)
	       		 	{
	       		 		
	       		 		//Displaying the split video
	       		 		
	       		 		frame.remove(label);
	       		 		label1 = new JLabel[chunks];
	       		 		videoPanel = new JPanel(new GridLayout(ytiles,xtiles,1,1));
	       		 		videoPanel.setPreferredSize(new Dimension(swidth,sheight));			
	       			    frame.getContentPane().add(videoPanel);
	       			    count = 0;
	       			    
	       			    for (count=0; count<ytiles*xtiles; count++)
	       			    {
	       			    	label1[count]= new JLabel (new ImageIcon(timgs[count]));
       			    		label1[count].setPreferredSize(new Dimension(w2,h2));
       			    		videoPanel.add(label1[count]);
	       			    }
	       			    videoPanel.revalidate();
	       			    frame.pack();
	       			    frame.setVisible(true);
	       		 	}
		       		else if(split == true && initialize == true)
		       		{
			       		//Displaying the splitted video after it has been splitted 
	                	
		       			frame.remove(videoPanel);
	                	
	                	videoPanel = new JPanel(new GridLayout(ytiles,xtiles,1,1));
	        			videoPanel.setPreferredSize(new Dimension(swidth,sheight));
	        			  
	        			frame.getContentPane().add(videoPanel);
	        			
	        			int k=0;
	                	for(int each : list)
	                	{	
	                		 label1[k].setIcon(null);
	                         label1[k].revalidate();         
	                         label1[k].setIcon(new ImageIcon(timgs[each]));
	                         label1[k].setPreferredSize(new Dimension(width,height));            
	                         label1[k].revalidate();
	                         videoPanel.add(label1[k]);
	                         label1[k].addMouseListener(this);
	                         k++;
					         
	                	}
	                	
	                	label1[rand].setVisible(false);			
	       			    frame.pack();
	       			    frame.setVisible(true);	       		        
		       	}
    		}
           	if(reset == true)
	       	{
	       			 //System.out.println("reset clicked");
	       		reset = false;
	       		if (split == true || initialize == true)
	       		{
	       			
	       			//Reseting the video to display as shown(without splitting)
	       			
	       			frame.dispose();
	       			img = new BufferedImage(swidth, sheight, BufferedImage.TYPE_INT_RGB);
	       		
	       			frame=new JFrame();
	       			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       			label = new JLabel(new ImageIcon(img));
	       		
	                label.setPreferredSize(new Dimension(swidth,sheight));
		            frame.getContentPane().add(label, BorderLayout.CENTER);
		            label.revalidate();
		            
		 	       // Buttons
		 	       buttonPanel = new JPanel();
		 	       buttonPanel.setPreferredSize(new Dimension(width, 50));
		 	       frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		 	       
		 	       splitButton = new MyButton("Split");
		 	       buttonPanel.add(splitButton, BorderLayout.WEST);
		 	
		 	       initButton = new MyButton("Initialize");
		 	       buttonPanel.add(initButton, BorderLayout.WEST);
		 	       
		 	       resetButton = new MyButton("Reset");
		 	       buttonPanel.add(resetButton, BorderLayout.WEST);
		 	       
		 	       closeButton = new MyButton("Close");
		 	       buttonPanel.add(closeButton, BorderLayout.WEST);    
	
		           frame.pack();
		           frame.setVisible(true);
	       	}
	       	split=false;
	       	initialize=false;
	       	}
           	
	     }
     }catch (FileNotFoundException e) 
     {
         e.printStackTrace();
     }catch (IOException e) 
     {
         e.printStackTrace();
     }
  }
}
}
   
   // Function calls
	public void buttonPressed(String name)
	{
		if (name.equals("Split"))
		{
			if (type == "Image")
			{
				int chunks = xtiles * ytiles; 
				int h2 = sheight/ytiles;
				int w2 = swidth/xtiles;
			   
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
			    label = new JLabel(new ImageIcon(scaledimg));
			    label.setPreferredSize(new Dimension(swidth,sheight));
			    frame.getContentPane().add(label, BorderLayout.CENTER);
			    frame.pack();
			    frame.setVisible(true); 	
				          
				int count = 0;
				int x,y,pix1,x1,y1;
				x=x1 =0;
				y=y1=0;
			    timgs = new BufferedImage[chunks]; //Image array to hold image chunks  
			 	timgs[count] = new BufferedImage(w2,h2,BufferedImage.TYPE_INT_ARGB );  
			 
			 	for (int i=0; i<ytiles; i++)
			 	{		
			 		x = 0;
				    for (int j=0; j<xtiles; j++,count++)
				    {		
				    	timgs[count] = new BufferedImage(w2,h2,scaledimg.getType());
				    	for (int k=0; k<h2; k++,y++)
				    	{
				    		for (int l=0; l<w2; l++,x++)
				    		{
				    				pix1 = scaledimg.getRGB(x,y);
				    				timgs[count].setRGB(l, k,pix1);
				    		}
				    			x=x1; //Getting to the top of next tile image
				    	}
				    	y=y1;
				    	//Adding the width of one tile(w2) to reach the starting of next column of tiles
				    	x = (j+1)*w2; 
				    	x1=x;
				    }
				    //Adding the height of next tile to reach to next row of tile
				    y = (i+1)*h2;
			    	y1=y;
			    	x1=0;
			 	}
			       
			 	frame.remove(label);
				label1 = new JLabel[chunks];
				tileimage = new JPanel(new GridLayout(ytiles,xtiles,1,1));
				tileimage.setPreferredSize(new Dimension(swidth,sheight));
				 	
				frame.getContentPane().add(tileimage);
				count = 0;
				    
				for (int i=0;i<ytiles*xtiles;i++)
				{
					label1[i]= new JLabel (new ImageIcon(timgs[i]));
				    label1[i].setPreferredSize(new Dimension(w2,h2));
				    tileimage.add(label1[i]);
				}
			    frame.pack();
			    frame.setVisible(true);
			    split = true;
			    initialize = false;
			    //System.out.println("Splitting done");				
			}
			else
			{
				split = true;
				reset = false;
				initialize = false;
			}
		}
		 else if (name.equals("Initialize"))
		{
			//System.out.println("Initialize");
			if (type == "Image" && !initialize )
			{
				//System.out.println("Initialize");
				
				initializeImage();
			}
			else if (type == "Video" && !initialize )
			{
				initialize = true;
				Random g = new Random();
				rand = g.nextInt(xtiles*ytiles);
				if(split == false)
				{
					//System.out.println("Error: Press the split first");
				}
				else
				{
					//Finding a random integer
					randomarraylist();
				}   
			}	
		} else if (name.equals("Reset"))
		{
			if (type == "Image")
			{
				//System.out.println("reset clicked");
				tileimage.removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
				label = new JLabel(new ImageIcon(scaledimg));
				label.setPreferredSize(new Dimension(swidth,sheight));
				frame.getContentPane().add(label, BorderLayout.CENTER);
				
				frame.pack();
				frame.setVisible(true);
			}
			else if (type == "Video")
			{
				//System.out.println("Reset");
				reset = true;
			}
		} else if (name.equals("Close"))
		{
			//System.out.println("Close");
			System.exit(0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
		int k;
		for(k=0;k<xtiles*ytiles;k++)
		{
			if(arg0.getSource()==label1[k])
			{
				//System.out.println("Clicked on label" + k);	
				break;
			}
		}
		
		find_neighbour_white(k);	
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
	
	public void initializeImage()
	{
		Random g = new Random();
		rand = g.nextInt(xtiles*ytiles);
		if(split == false)
		{
			System.out.println("Error: Press the split first");
		}
		else
		{
			//Finding a random integer for the tile to be rendered white
			//initialising a random array list
			randomarraylist();
	        
	       	frame.remove(tileimage);
	        tileimage = new JPanel(new GridLayout(ytiles,xtiles,1,1));
			tileimage.setPreferredSize(new Dimension(swidth,sheight));
			frame.getContentPane().add(tileimage);
	     
		    int k= 0;
		    
		    //Shuffling the contents of the list
		     
			for(int each : list)
			{			
				label1[k]= new JLabel (new ImageIcon(timgs[each]));
			    label1[k].setPreferredSize(new Dimension(swidth/xtiles,sheight/ytiles));
			    tileimage.add(label1[k],BorderLayout.WEST);
			    label1[k].addMouseListener(this);
			    k++;    	
			 }
	         label1[rand].setVisible(false);
	         frame.pack();
	         frame.setVisible(true);       
		}
		initialize = true;
	}
	
	public void randomarraylist() {
		// TODO Auto-generated method stub

        list = new ArrayList<Integer>(xtiles*ytiles);
        
        for(int i = 0; i <xtiles*ytiles ; i++) 
        {
            list.add(i);
        }
        
        Collections.shuffle(list); 
         
	}

	public int splitting()
	{
		

		int chunks = xtiles * ytiles; 
		int h2 = sheight/ytiles;
		int w2 = swidth/xtiles;
		
		frame.remove(label);
	   
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	    if(type == "Image")
	    { 	
	    	label = new JLabel(new ImageIcon(scaledimg));
	    }
	    
	    label.setPreferredSize(new Dimension(swidth,sheight));
	    frame.getContentPane().add(label, BorderLayout.CENTER);
	    frame.pack();
	    frame.setVisible(true); 	
		          
		int count = 0;
		int x,y,pix1,x1,y1;
		x=x1 =0;
		y=y1=0;
	    timgs = new BufferedImage[chunks]; //Image array to hold image chunks  
	 	timgs[count] = new BufferedImage(w2,h2,BufferedImage.TYPE_INT_ARGB );  
	 
	 	for (int i=0; i<ytiles; i++)
	 	{		
	 		x = 0;
		    for (int j=0; j<xtiles; j++,count++)
		    {		
		    	timgs[count] = new BufferedImage(w2,h2,scaledimg.getType());
		    	for (int k=0; k<h2; k++,y++)
		    	{
		    		for (int l=0; l<w2; l++,x++)
		    		{
		    				pix1 = scaledimg.getRGB(x,y);
		    				timgs[count].setRGB(l, k,pix1);
		    		}
		    			x=x1; //Getting to the top of next tile image
		    	}
		    	y=y1;
		    	//Adding the width of one tile(w2) to reach the starting of next column of tiles
		    	x = (j+1)*w2; 
		    	x1=x;
		    }
		    //Adding the height of next tile to reach to next row of tile
		    y = (i+1)*h2;
	    	y1=y;
	    	x1=0;
	 	}
	       
	 	frame.remove(label);
		label1 = new JLabel[chunks];
		tileimage = new JPanel(new GridLayout(ytiles,xtiles,1,1));
		tileimage.setPreferredSize(new Dimension(swidth,sheight));
		 	
		frame.getContentPane().add(tileimage);
		count = 0;
		    
		for (int i=0;i<ytiles*xtiles;i++)
		{
			label1[i]= new JLabel (new ImageIcon(timgs[i]));
		    label1[i].setPreferredSize(new Dimension(w2,h2));
		    tileimage.add(label1[i]);
		}
	    frame.pack();
	    frame.setVisible(true);
	    
	    return(1);
	}
	
	
	public void find_neighbour_white(int k)
	{
	
		//This function is used to find the white tile if it surround the label k
		
		boolean whitetile = false;
		
		//Checking all the neighbouring tiles
		
		if(whitetile == true)
		{
			System.out.println("white tile found");
		}
		else
		{
			if(((k-1)%xtiles != (xtiles-1)) && ((k-1)%xtiles >= 0 ))
			{
				
				if(k-1 == rand)
				{
					whitetile = true;
					
					Integer one = (Integer) list.get(k-1);        
				    Integer two = (Integer)list.get(k); 			    
				    
				    label1[k].setIcon(null);
				    label1[k-1].setIcon(null);
				    
				    label1[k].revalidate();
				    label1[k-1].revalidate();
				    
				    
				    label1[k].setIcon(new ImageIcon(timgs[one]));
				    label1[k-1].setIcon(new ImageIcon(timgs[two]));
				    
				    label1[k].revalidate();
				    label1[k-1].revalidate();
				   
				    label1[k-1].setVisible(true);
				    label1[k].setVisible(false);
				    
				    rand = k;
				  
				    
				    list.set(k-1, two);
				    list.set(k, one);    
				}
			}
			if( ( (k+1)%xtiles!=0 ) && (whitetile == false) )
			{
				if((k+1) == rand )
				{
					
					Integer one = (Integer) list.get(k+1);        
					Integer two = (Integer)list.get(k); 
			     
			      	label1[k].setIcon(null);
				    label1[k+1].setIcon(null);
				    
				    label1[k].revalidate();
				    label1[k+1].revalidate();
				      
				    label1[k].setIcon(new ImageIcon(timgs[one]));
				    label1[k+1].setIcon(new ImageIcon(timgs[two]));
				 
				    
				    label1[k].revalidate();
				    label1[k+1].revalidate();
				    
				   
				    label1[k+1].setVisible(true);
				    label1[k].setVisible(false);
				    
				    rand = k;
				    
				    list.set(k+1, two);
				    list.set(k, one);
				}
			}
			if( ( (k-xtiles)>=0)  && (whitetile == false) )
			{
				//System.out.println("top label" + (k-xtiles));
				if((k-xtiles) == rand )
				{
					whitetile = true;
					Integer one = (Integer) list.get(k-xtiles);       
			        Integer two = (Integer)list.get(k); 
			    
			        label1[k].setIcon(null);
				    label1[k-xtiles].setIcon(null);
				    
				    //label1[k].revalidate();
				    //label1[k-xtiles].revalidate();
				      
				    label1[k].setIcon(new ImageIcon(timgs[one]));
				    label1[k-xtiles].setIcon(new ImageIcon(timgs[two]));
				   
				    label1[k].revalidate();
				    label1[k-xtiles].revalidate();
				    
				    label1[k-xtiles].setVisible(true);
				    label1[k].setVisible(false);
				    rand = k;
				 
				    list.set(k-xtiles, two);
				    list.set(k, one);
			    }
			}
			if( ( (k+xtiles)<(xtiles*ytiles))  && (whitetile == false) )
			{
				if((k+xtiles) == rand )
				{
					whitetile = true;
					Integer one = (Integer) list.get(k+xtiles);        
					Integer two = (Integer)list.get(k); 
			 
					label1[k].setIcon(null);
				    label1[k+xtiles].setIcon(null);
				    
				    label1[k].revalidate();
				    label1[k+xtiles].revalidate();
				      
				    label1[k].setIcon(new ImageIcon(timgs[one]));
				    label1[k+xtiles].setIcon(new ImageIcon(timgs[two]));
				    
				    label1[k].revalidate();
				    label1[k+xtiles].revalidate();
				    
				    label1[k+xtiles].setVisible(true);
				    label1[k].setVisible(false);
				    
				    rand = k;
				  
				    
				    list.set(k+xtiles, two);
				    list.set(k, one);			     
				}
			}
		}
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