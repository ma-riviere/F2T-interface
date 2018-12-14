package main;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image {

	public static int SIZE=700;
	
	
	public String view=null;
	public String tactile=null;
	public String area=null;
	
	public BufferedImage view_img;
	public BufferedImage tactile_img;
	public BufferedImage area_img;
	
	public BufferedImage view_img_miniature;
	public BufferedImage tactile_img_miniature;
	public BufferedImage area_img_miniature;
	
	public int[][][] tactile_mat;
	
	public int[][][] area_mat;
	
	// initialize empty images
	public Image(){
		view=null;
		tactile=null;
		area=null;
		
		tactile_mat=new int[SIZE][SIZE][3];
		area_mat=new int[SIZE][SIZE][3];
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				tactile_mat[i][j][0]=0;
				tactile_mat[i][j][1]=0;
				tactile_mat[i][j][2]=0;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				area_mat[i][j][0]=0;
				area_mat[i][j][1]=0;
				area_mat[i][j][2]=0;
			}
		}
	}
	
	
	// set new picture
	public void setPicture(String picture){
		view=picture;
		if (view!=null){
			try {
				view_img = ImageIO.read(new File(Main.FILES+Main.IMG+view));
				view_img_miniature=scale(view_img,0.25);
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
				tactile_img_miniature=scale(tactile_img, 0.25);
			} catch (IOException e) {System.out.print(e);}
		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					tactile_mat[i][j][0]=(tactile_img.getRGB(i, j)>> 16) & 0x000000FF;
					tactile_mat[i][j][1]=(tactile_img.getRGB(i, j)>>  8) & 0x000000FF;
					tactile_mat[i][j][2]=(tactile_img.getRGB(i, j)     ) & 0x000000FF;
				}
			}
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
		}
	}
	

	
	// set new area map
	public void setArea(String areamap){
		area=areamap;
		
		if (area!=null){
			try {
				area_img = ImageIO.read(new File(Main.FILES+Main.AREA+area));
				area_img_miniature=scale(area_img, 0.25);
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
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	public static BufferedImage scale(BufferedImage bi, double scaleValue) {
        AffineTransform tx = new AffineTransform();
        tx.scale(scaleValue, scaleValue);
        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        BufferedImage biNew = new BufferedImage( (int) (bi.getWidth() * scaleValue),(int) (bi.getHeight() * scaleValue), bi.getType());
        return op.filter(bi, biNew);
	}
	
}
