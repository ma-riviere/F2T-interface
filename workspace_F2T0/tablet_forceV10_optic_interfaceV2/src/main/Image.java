package main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image {

	public static int SIZE=700;
	
	
	public String view=null;
	public String tactile=null;
	public String flow=null;
	public String rail=null;
	public String area=null;
	
	public BufferedImage view_img;
	public BufferedImage tactile_img;
	public BufferedImage flow_img;
	public BufferedImage rail_img;
	public BufferedImage area_img;
	
	public int[][][] tactile_mat;
	
	public int[][][] flow_mat;
	
	public int[][][] rail_mat;
	
	public int[][][] area_mat;
	
	// initialize empty images
	public Image(){
		view=null;
		tactile=null;
		flow=null;
		rail=null;
		area=null;
		
		tactile_mat=new int[SIZE][SIZE][3];
		flow_mat=new int[SIZE][SIZE][2];
		rail_mat=new int[SIZE][SIZE][2];
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
				flow_mat[i][j][0]=125;
				flow_mat[i][j][1]=125;
			}
		}
		
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				rail_mat[i][j][0]=125;
				rail_mat[i][j][1]=125;
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
			} catch (IOException e) {System.out.print(e);}
		}
	}
	
	// set new tactile image
	public void setTactile(String tactileimage){
		tactile=tactileimage;
		
		if (tactile!=null){
			try {
				tactile_img = ImageIO.read(new File(Main.FILES+Main.TACTILE+tactile));
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
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					tactile_mat[i][j][0]=0;
					tactile_mat[i][j][1]=0;
					tactile_mat[i][j][2]=0;
				}
			}
		}
	}
	
	
	// set new vector flow
	public void setFlow(String vectorflow){
		flow=vectorflow;
		
		if (flow!=null){
			try {
				flow_img = ImageIO.read(new File(Main.FILES+Main.FLOW+flow));
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					flow_mat[i][j][0]=(flow_img.getRGB(i, j)>> 16) & 0x000000FF;
					flow_mat[i][j][1]=(flow_img.getRGB(i, j)>>  8) & 0x000000FF;
				}
			}
		}
		else{
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					flow_mat[i][j][0]=125;
					flow_mat[i][j][1]=125;
				}
			}
		}
	}
	
	// set new vector rails
	public void setRail(String vectorrail){
		rail=vectorrail;
		
		if (rail!=null){
			try {
				rail_img = ImageIO.read(new File(Main.FILES+Main.RAIL+rail));
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					rail_mat[i][j][0]=(rail_img.getRGB(i, j)>> 16) & 0x000000FF;
					rail_mat[i][j][1]=(rail_img.getRGB(i, j)>>  8) & 0x000000FF;
				}
			}
		}
		else{
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					rail_mat[i][j][0]=125;
					rail_mat[i][j][1]=125;
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
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					area_mat[i][j][0]=0;
					area_mat[i][j][1]=0;
					area_mat[i][j][2]=0;
				}
			}
		}
	}
	
}
