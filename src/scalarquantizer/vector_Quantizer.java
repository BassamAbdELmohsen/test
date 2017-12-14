

package scalarquantizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


public class vector_Quantizer {
    public static int rowsizeofpaded;
        public static int colsizeofpaded;
    
    public static void compress( int codebooksize ,int row ,int col )
        {       
            Node root = new Node();
            root.construct(codebooksize ,row ,col);
            
            LinkedList<Node> StebRes = root.Stebalizing(row,col);
            root.GenerateCompressCode(StebRes,row,col);
            
            root.Print();
            
            
        }
    
     public static LinkedList<int[][]> InitialisingBlocks(int row ,int col){
        
        
//================================ORG Matrix========================================
Controller main= new Controller();

int readimage[][] = main.readImage("D:\\cameraMan.jpg");

/*int readimage[][] = new int[6][6];

        readimage[0][0] = 1;
        readimage[0][1] = 2;
        readimage[0][2] = 7;
        readimage[0][3] = 9;
        readimage[0][4] = 4;
        readimage[0][5] = 11;
        
        readimage[1][0] = 3;
        readimage[1][1] = 4;
        readimage[1][2] = 6;
        readimage[1][3] = 6;
        readimage[1][4] = 12;
        readimage[1][5] = 12;
        
        readimage[2][0] = 4;
        readimage[2][1] = 9;
        readimage[2][2] = 15;
        readimage[2][3] = 14;
        readimage[2][4] = 9;
        readimage[2][5] = 9;
        
        readimage[3][0] = 10;
        readimage[3][1] = 10;
        readimage[3][2] = 20;
        readimage[3][3] = 18;
        readimage[3][4] = 8;
        readimage[3][5] = 8;
        
        readimage[4][0] = 4;
        readimage[4][1] = 3;
        readimage[4][2] = 17;
        readimage[4][3] = 16;
        readimage[4][4] = 1;
        readimage[4][5] = 4;
        
        readimage[5][0] = 4;
        readimage[5][1] = 5;
        readimage[5][2] = 18;
        readimage[5][3] = 18;
        readimage[5][4] = 5;
        readimage[5][5] = 6;
  
  */
        
        //================================ORG Matrix========================================
      //  int[][] readimage = readImage("F:\\ReadWriteImageClass\\cameraMan.jpg");

//================================ORG Dim========================================
        int orgrow = readimage.length;
        int orgcol = readimage[0].length;
        int orgsize = orgrow * orgcol;

//================================Block Dim========================================        
        int blockrow = row;
        int blockcol = col;        
        int blocksize = blockrow * blockcol;
//================================Handeling padding========================================
        double rowratio = orgrow / blockrow;
        double colratio = orgcol / blockcol;
        
        int fixedrow = 0;
        int fixedcol = 0;

        if (orgrow % blockrow != 0) 
        {
            fixedrow = (int) (rowratio + 1);
        } 
        
        else 
        {
            fixedrow = (int) rowratio;
        }

        if (orgcol % blockcol != 0) 
        {
            fixedcol = (int) (colratio + 1);
        } 
        else 
        {
            fixedcol = (int) colratio;
        }

        int padddedrow = fixedrow * blockrow;
        int paddedcol = fixedcol * blockcol;

        int paddedsize = padddedrow * paddedcol;
        int noBlocks = paddedsize / blocksize;

        int avgpadd = 0;
//================================Filling Padding========================================
        for (int i = 0; i < orgrow; i++) {
            for (int j = 0; j < orgcol; j++) {

                avgpadd += readimage[i][j];
            }
        }

        avgpadd = avgpadd / orgsize;

        int[][] padded = new int[padddedrow][paddedcol];

        for (int r = 0; r < padddedrow; r++) {
            for (int c = 0; c < paddedcol; c++) {
                if (r >= orgrow || c >= orgcol) {

                    padded[r][c] = avgpadd;

                } 
                else 
                {
                    padded[r][c] = readimage[r][c];
                }

            }
        }

        

//==================================Splitting into Array of 2d matrices=========
LinkedList<int[][]> listOfBlocks = new LinkedList<int[][]>();
int or = 0;
int oc = 0;
while(true){
         if(oc == paddedcol){
             or+=blockrow;
             oc=0;
         }   
         if(or == padddedrow){
             break;
         }
         int block[][] = new int[blockrow][blockcol];
         for (int i = or , br = 0; i < blockrow+or ; i++ , br++) {
             for (int j = oc , bc = 0; j < blockcol+oc; j++ , bc++) {
                 block[br][bc] = padded[i][j];
             }
    }
         oc+=blockcol;
         listOfBlocks.add(block);
         
        }
   rowsizeofpaded=padddedrow;
   colsizeofpaded=paddedcol;

    return listOfBlocks;
    }
        
        
public static int [][] Transform ( LinkedList<int [][]> ListOfBlocks, int imagerow, int imagecol)
{
    
   int [][] transformed_matrix = new int [imagerow][imagecol];
   
   int blockrow=ListOfBlocks.get(0).length;
   int blockcol=ListOfBlocks.get(0)[0].length;
   
   int paddedcol=imagecol;
   int padddedrow=imagerow;
   
      
int or = 0;
int oc = 0;
int k =0;

            
while(true){
    
         if(oc == paddedcol){
             or+=blockrow;
             oc=0;
         }   
         if(or == padddedrow || k==ListOfBlocks.size() ){
             break;
         }
         int [][] block =ListOfBlocks.get(k);
         for (int i = or , br = 0; i < blockrow+or ; i++ , br++) {
             for (int j = oc , bc = 0; j < blockcol+oc; j++ , bc++) {
                 transformed_matrix[i][j]= block[br][bc] ;
             }
             }
         oc+=blockcol;
         k++;
         
        }

   
   return transformed_matrix;
}



}
