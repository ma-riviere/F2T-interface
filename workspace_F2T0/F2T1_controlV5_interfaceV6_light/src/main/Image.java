package main;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Image is the tactile and haptic environment, characterized by a set of picture files
 * @author simon gay
 */

public class Image {

	public static int SIZE=700;
	
	public static float SCALE=0.4f;
	
	public String view=null;
	public String tactile=null;
	public String flow=null;
	public String rail=null;
	public String area=null;
	public String magnetic=null;
	
	public BufferedImage view_img;
	public BufferedImage tactile_img;
	public BufferedImage flow_img;
	public BufferedImage rail_img;
	public BufferedImage area_img;
	public BufferedImage magnetic_img;
	
	public BufferedImage view_img_miniature;
	public BufferedImage tactile_img_miniature;
	public BufferedImage flow_img_miniature;
	public BufferedImage rail_img_miniature;
	public BufferedImage area_img_miniature;
	public BufferedImage magnetic_img_miniature;
	
	public int[][][] tactile_mat;
	public float[][][] gradient;
	
	public int[][][] flow_mat;
	
	public int[][][] rail_mat;
	
	public int[][][] area_mat;
	
	public int[][] magnetic_mat;
	
	// initialize empty images
	public Image(){

		view=null;
		tactile=null;
		flow=null;
		rail=null;
		area=null;
		magnetic=null;
		
		tactile_mat=new int[SIZE][SIZE][3];
		gradient=new float[SIZE][SIZE][2];
		flow_mat=new int[SIZE][SIZE][2];
		rail_mat=new int[SIZE][SIZE][2];
		area_mat=new int[SIZE][SIZE][3];
		magnetic_mat=new int[SIZE][SIZE];
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				tactile_mat[i][j][0]=0;
				tactile_mat[i][j][1]=0;
				tactile_mat[i][j][2]=0;
				
				gradient[i][j][0]=0;
				gradient[i][j][1]=0;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				flow_mat[i][j][0]=128;
				flow_mat[i][j][1]=128;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				rail_mat[i][j][0]=128;
				rail_mat[i][j][1]=128;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				area_mat[i][j][0]=0;
				area_mat[i][j][1]=0;
				area_mat[i][j][2]=0;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				magnetic_mat[i][j]=0;
			}
		}
	}
	
	
	// set new picture
	public void setPicture(String picture){
		view=picture;
		if (view!=null){
			try {
				view_img = ImageIO.read(new File(Main.FILES+Main.IMG+view));
				view_img_miniature=scale(view_img,SCALE);
			} catch (IOException e) {System.out.print(e);}
		}
		else view_img_miniature=null;
	}
	
	// set new tactile image
	public void setTactile(String tactileimage){
		tactile=tactileimage;
		
		if (tactile!=null){
			try {
				tactile_img = ImageIO.read(new File(Main.FILES+Main.TACTILE+tactile));
				tactile_img_miniature=scale(tactile_img, SCALE);
			} catch (IOException e) {System.out.print(e);}
		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					tactile_mat[i][j][0]=(tactile_img.getRGB(i, j)>> 16) & 0x000000FF;
					tactile_mat[i][j][1]=(tactile_img.getRGB(i, j)>>  8) & 0x000000FF;
					tactile_mat[i][j][2]=(tactile_img.getRGB(i, j)     ) & 0x000000FF;
				}
			}
			setGradientMap();
		}
		else{
			tactile_img_miniature=null;
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					tactile_mat[i][j][0]=0;
					tactile_mat[i][j][1]=0;
					tactile_mat[i][j][2]=0;
				}
			}
			clearGradientMap();
		}
	}
	
	
	// set new vector flow
	public void setFlow(String vectorflow){
		flow=vectorflow;
		
		if (flow!=null){
			try {
				flow_img = ImageIO.read(new File(Main.FILES+Main.FLOW+flow));
				flow_img_miniature=scale(flow_img, SCALE);
			} catch (IOException e) {System.out.print(e);}

			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					flow_mat[i][j][0]=(flow_img.getRGB(i, j)>> 16) & 0x000000FF;
					flow_mat[i][j][1]=(flow_img.getRGB(i, j)>>  8) & 0x000000FF;
				}
			}
		}
		else{
			flow_img_miniature=null;
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					flow_mat[i][j][0]=125;
					flow_mat[i][j][1]=125;
				}
			}
		}
	}
	
	public void setFlow(float[][][] map){
		flow="__testing__";
	
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				flow_mat[i][j][0]=(int)(Math.max(0, Math.min(255, map[i][j][0]*128+128)));
				flow_mat[i][j][1]=(int)(Math.max(0, Math.min(255, -map[i][j][1]*128+128)));
			}
		}
	}
	
	// set new vector rails
	public void setRail(String vectorrail){
		rail=vectorrail;
		
		if (rail!=null){
			try {
				rail_img = ImageIO.read(new File(Main.FILES+Main.RAIL+rail));
				rail_img_miniature=scale(rail_img, SCALE);
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					rail_mat[i][j][0]=(rail_img.getRGB(i, j)>> 16) & 0x000000FF;
					rail_mat[i][j][1]=(rail_img.getRGB(i, j)>>  8) & 0x000000FF;
				}
			}
		}
		else{
			rail_img_miniature=null;
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					rail_mat[i][j][0]=125;
					rail_mat[i][j][1]=125;
				}
			}
		}
	}
	
	public void setRail(float[][][] map){
		rail="__testing__";
	
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				rail_mat[i][j][0]=(int)(Math.max(0, Math.min(255, map[i][j][0]*128+128)));
				rail_mat[i][j][1]=(int)(Math.max(0, Math.min(255, -map[i][j][1]*128+128)));
			}
		}
	}
	
	public void clearFlow(){
		flow=null;
		rail=null;
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				flow_mat[i][j][0]=128;
				flow_mat[i][j][1]=128;
				rail_mat[i][j][0]=128;
				rail_mat[i][j][1]=128;
			}
		}
	}
	
	// set new area map
	public void setArea(String areamap){
		area=areamap;
		
		if (area!=null){
			try {
				area_img = ImageIO.read(new File(Main.FILES+Main.AREA+area));
				area_img_miniature=scale(area_img, SCALE);
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					area_mat[i][j][0]=(area_img.getRGB(i, j)>> 16) & 0x000000FF;
					area_mat[i][j][0]=(area_mat[i][j][0]+4)/10;
					area_mat[i][j][1]=(area_img.getRGB(i, j)>>  8) & 0x000000FF;
					area_mat[i][j][1]=(area_mat[i][j][1]+4)/10;
					area_mat[i][j][2]=(area_img.getRGB(i, j)     ) & 0x000000FF;
					area_mat[i][j][2]=(area_mat[i][j][2]+4)/10;
				}
			}
		}
		else{
			area_img_miniature=null;
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					area_mat[i][j][0]=0;
					area_mat[i][j][1]=0;
					area_mat[i][j][2]=0;
				}
			}
		}
	}
	
	// set new magnetic map
	public void setMagnetic(String magneticmap){
		magnetic=magneticmap;
		
		if (magnetic!=null){
			try {
				magnetic_img = ImageIO.read(new File(Main.FILES+Main.MAGNETIC+magnetic));
				magnetic_img_miniature=scale(magnetic_img, SCALE);
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					if ( ( ( magnetic_img.getRGB(i, j)>> 16 ) & 0x000000FF)>0){
						magnetic_mat[i][j]=1;
					} else{
						magnetic_mat[i][j]=0;
					}
				}
			}
		}
		else{
			magnetic_img_miniature=null;
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					magnetic_mat[i][j]=0;
				}
			}
		}
	}
	
	
	public void setGradientMap(){
		
		float height=0;
		float gradientX=0;
		float gradientY=0;
		float sum=0;
		
		for (int i2=0;i2<Image.SIZE;i2++){
			for (int j2=0;j2<Image.SIZE;j2++){
				
				gradientX=0;
				gradientY=0;
				sum=0;
				
				height=(float)tactile_mat[i2][699-(j2)][1]/255;
				
				for (int i=-2;i<=2;i++){
					for (int j=-2;j<=2;j++){
						if ((i2+i)>=0 && (i2+i)<700 && 699-(j2+j)>=0 && 699-(j2+j)<700){
							
							float h=(float)tactile_mat[i2+i][699-(j2+j)][1]/255;
							
							float d=(float)Math.sqrt(i*i+j*j);
							if (Math.abs((h-height) * i/d)>0.00001) gradientX+=(h-height) * i/d;
							if (Math.abs((h-height) * j/d)>0.00001) gradientY+=(h-height) * j/d;
							sum++;
						}
					}
				}
				
				if (sum>2){
					if (Math.abs(gradientX/sum)>0.00001) gradient[i2][j2][0]=gradientX/sum;
					else gradient[i2][j2][0]=0;
					if (Math.abs(gradientY/sum)>0.00001) gradient[i2][j2][1]=gradientY/sum;
					else gradient[i2][j2][1]=0;
				}
				else{
					gradient[i2][j2][0]=0;
					gradient[i2][j2][1]=0;
				}
			}
		}
	}
	
	public void clearGradientMap(){
		for (int i=0;i<Image.SIZE;i++){
			for (int j=0;j<Image.SIZE;j++){
				gradient[i][j][0]=0;
				gradient[i][j][1]=0;
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	public static BufferedImage scale(BufferedImage bi, double scaleValue) {
        AffineTransform tx = new AffineTransform();
        tx.scale(scaleValue, scaleValue);
        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        BufferedImage biNew = new BufferedImage( (int) (bi.getWidth() * scaleValue),(int) (bi.getHeight() * scaleValue), bi.getType());
        return op.filter(bi, biNew);
	}
	
}
