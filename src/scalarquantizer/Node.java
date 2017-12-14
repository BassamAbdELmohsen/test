package scalarquantizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import static scalarquantizer.vector_Quantizer.InitialisingBlocks;

public class Node {

    private int blockCol;
    private int blockRow;

    private Double[][] Avg_Block;
    
    private static LinkedList<Node> Treenodes = new LinkedList<Node>();
    private LinkedList<int[][]> List_Of_Blocks = new LinkedList<>();

    private Node left;
    private Node right;

    

    public int getBlockCol() {
        return blockCol;
    }

    public void setBlockCol(int blockCol) {
        this.blockCol = blockCol;
    }

    public int getBlockRow() {
        return blockRow;
    }

    public void setBlockRow(int blockRow) {
        this.blockRow = blockRow;
    }

   
//****************************************************************************    

    Node(Double avgArr[][]) {
        this.Avg_Block = avgArr;

    }

    public Node() {

    }

    public Double[][] getAvgBlock() {
        return this.Avg_Block;
    }

    public void setAvgBlock(Double avg1[][]) {
        this.Avg_Block = avg1;
    }

    public Double[][] Get_Settled_Average(Node node) {
        int row=node.getAvgBlock().length;
        int col=node.getAvgBlock()[0].length;
        int listsize=node.getList_Of_Blocks().size();
        
        Double avgBlock[][] = new Double[row][col];

        for (int j = 0; j < row; j++) {
            for (int k = 0; k < col; k++) {
                avgBlock[j][k] = 0.0;
            }
        }

        for (int i = 0; i < listsize ; i++) {
            for (int j = 0; j < row; j++) {

                for (int k = 0; k < col; k++) {

                    avgBlock[j][k] += node.getList_Of_Blocks().get(i)[j][k];
                }

            }
        }
        
        for (int i = 0; i < row; i++) {
            
            for (int j = 0; j < col; j++) {
                if (node.getList_Of_Blocks().size() != 0) 
                {
                    avgBlock[i][j] = avgBlock[i][j] / node.getList_Of_Blocks().size();
                }
            }
        }
        
        return avgBlock;
    }

    public LinkedList<int[][]> getList_Of_Blocks() {
        return List_Of_Blocks;
    }

    public void setList_Of_Blocks(LinkedList<int[][]> list1) {

        for (int i = 0; i < list1.size(); i++) {
            this.List_Of_Blocks.add(list1.get(i));
        }
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {

        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
    // split average

    public Double[][] GetAverageHistory(Double block[][], int number) {
        Double temp[][] = new Double[block.length][block[0].length];
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                temp[i][j] = block[i][j];
            }
        }
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {

                temp[i][j] += number;
            }
        }

        return temp;
    }

    public boolean ComparisonForConstruction(Double avg1[][], Double avg2[][], int blockToBeCompared[][]) {

        boolean shortestDistance_1 = false;
        if (avg1.length != 0) {
            int distance_1 = 0;
            int distance_2 = 0;

            Double blockDiff_1[][] = new Double[avg1.length][avg1[0].length];
            Double blockDiff_2[][] = new Double[avg2.length][avg2[0].length];

            for (int i = 0; i < avg1.length; i++) {
                for (int j = 0; j < avg1[0].length; j++) {
                    blockDiff_1[i][j] = Math.abs(blockToBeCompared[i][j] - avg1[i][j]);
                    blockDiff_2[i][j] = Math.abs(blockToBeCompared[i][j] - avg2[i][j]);
                }
            }
            for (int i = 0; i < avg1.length; i++) {
                for (int j = 0; j < avg1[0].length; j++) {
                    distance_1 += blockDiff_1[i][j];
                    distance_2 += blockDiff_2[i][j];
                }
            }

            if (distance_1 <= distance_2) {
                shortestDistance_1 = true;
            }
        }

        return shortestDistance_1;
    }

    public void construct( int numberOfLevels ,int row ,int col) {
        int c = 0;
        
            LinkedList<Double> diffList = new LinkedList<>();

            Node root =new Node();
            
            int blockrow=0;
            int blockcol =0;
            
            LinkedList<int[][]>listOfBlocks = InitialisingBlocks(row ,col);
            
            if(listOfBlocks.size()!=0)
            {
             blockrow = listOfBlocks.get(0).length;
             blockcol = listOfBlocks.get(0)[0].length;
            }
          
            int noBlocks = listOfBlocks.size();
            //========================Getting Avg Block=====================================
             Double avgBlock[][] =new Double[blockrow][blockcol];


            for (int j = 0; j < blockrow; j++) {
                for (int k = 0; k < blockcol; k++) {
                    avgBlock[j][k] = 0.0;
                }
            }
            
        

        for (int i = 0; i < listOfBlocks.size(); i++) {
            for (int j = 0; j < blockrow; j++) {
                for (int k = 0; k < blockcol; k++) {
                    avgBlock[j][k] += listOfBlocks.get(i)[j][k];
                }
            }
            
        }
        
        for (int i = 0; i < blockrow; i++) {
            for (int j = 0; j < blockcol; j++) {
                avgBlock[i][j] /= noBlocks;
            }
        }
//=================================================================================
 
           
            
            root.setAvgBlock(avgBlock);
            
            root.setBlockRow(blockrow);
            root.setBlockCol(blockcol);
            
            root.setList_Of_Blocks(listOfBlocks);


        LinkedList<int[][]> leftlistRoot = new LinkedList();
        LinkedList<int[][]> rightlistRoot = new LinkedList<>();

        Double historyAverage[][] = root.Get_Settled_Average(root);
        Node leftRoot = new Node(GetAverageHistory(historyAverage, -1));

        //LEFT&Right ROOT AvgBlock Didnt assigned yet!!!!!!!
        Node rightRoot = new Node(GetAverageHistory(historyAverage, 1));
       

        for (int i = 0; i < root.getList_Of_Blocks().size(); i++) {
            if (ComparisonForConstruction(leftRoot.getAvgBlock(), rightRoot.getAvgBlock(), root.getList_Of_Blocks().get(i))) {

                leftlistRoot.add(root.getList_Of_Blocks().get(i));

            } else {

                rightlistRoot.add(root.getList_Of_Blocks().get(i));
            }
        }

        leftRoot.setList_Of_Blocks(leftlistRoot);
        rightRoot.setList_Of_Blocks(rightlistRoot);


        

        root.setLeft(leftRoot);
        root.setRight(rightRoot);
        Treenodes.add(leftRoot);
        Treenodes.add(rightRoot);
        LinkedList<Node>TemporaryTreeNodes = new LinkedList<>();
        while (Treenodes.size() != numberOfLevels) {
            System.out.println("C = "+c);
            c++;
            for (int i = 0; i < Treenodes.size(); i++) {
                Node parent = Treenodes.get(i);
                Double historyAVG[][] = parent.Get_Settled_Average(parent);
                
                Node leftNode = new Node(GetAverageHistory(historyAVG, -1));
                
                leftNode.setBlockRow(blockrow);
                leftNode.setBlockCol(blockcol);
                
                Node rightNode = new Node(GetAverageHistory(historyAVG, 1));
                rightNode.setBlockRow(blockrow);
                rightNode.setBlockCol(blockcol);
                
                TemporaryTreeNodes.add(leftNode);
                TemporaryTreeNodes.add(rightNode);
            }
            Treenodes.clear();
            //***************************comparison block *****************************
                for (int j = 0; j < listOfBlocks.size(); j++) {
                    
                    for (int k = 0; k < TemporaryTreeNodes.size(); k++) {

                        Double diff = CompareDiffrence(listOfBlocks.get(j), TemporaryTreeNodes.get(k).getAvgBlock());
                        
                        diffList.add(diff);
                    }
                    int index = diffList.indexOf(Collections.min(diffList));
                    
                    TemporaryTreeNodes.get(index).getList_Of_Blocks().add(listOfBlocks.get(j));
                    
                    diffList.clear();
                }
           //========Copy Temproray to the TreeNode again to complete work with it===========
                for (int i = 0; i < TemporaryTreeNodes.size(); i++) {
                Treenodes.add(TemporaryTreeNodes.get(i));
                }
                TemporaryTreeNodes.clear();
            

        }
 
        //=================
        

    }

    public Double CompareDiffrence(int block[][], Double avg[][]) {
        Double distance_1 = 0.0;

        Double blockDiff_1[][] = new Double[block.length][block[0].length];

        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                blockDiff_1[i][j] = Math.abs(block[i][j] - avg[i][j]);

            }
        }
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                distance_1 += blockDiff_1[i][j];

            }
        }
        return distance_1;
    }

    
    
    
    public LinkedList<Node> Stebalizing(int row,int col) {

        LinkedList<Double> diffList = new LinkedList<>();
        vector_Quantizer c = new vector_Quantizer();
        LinkedList<int[][]> arrayOfBlocks = c.InitialisingBlocks(row ,col);


        int maxNumberOfItterations = 100;
        int i = 0;

        for (i = 0; i < maxNumberOfItterations; i++) {
            boolean error = false;
            for (int j = 0; j < Treenodes.size(); j++) {
                //If all the diffreneces between the average of the nodes and the average of the List of the same nodes
                //is greater than 0.5 then make the boolean error = true.
                if (Arrays.deepEquals(Treenodes.get(j).getAvgBlock(), Treenodes.get(j).Get_Settled_Average(Treenodes.get(j))) == false) {
                    error = true;
                    break;

                } else {
                    error = false;
                }
            }
            if (error == false) {
                break;
            } else if (error == true) {
                for (int j = 0; j < Treenodes.size(); j++) {
                    //the average of the node will be the average of it's List.
                  Double x[][] = Treenodes.get(j).Get_Settled_Average(Treenodes.get(j));
                    Treenodes.get(j).setAvgBlock(x);
                    //Clear the List_Of_Blocks of the node to fill it with new List.
                    Treenodes.get(j).getList_Of_Blocks().clear();
                }
//***************************comparison block *****************************
                for (int j = 0; j < arrayOfBlocks.size(); j++) {
                    //Get the diffrences of all of the leafNodes Average with every element in the newArray.
                    for (int k = 0; k < Treenodes.size(); k++) {

                        Double diff = CompareDiffrence(arrayOfBlocks.get(j), Treenodes.get(k).getAvgBlock());
                        //Add the difference to the DiffLinkedList.
                        diffList.add(diff);
                    }
                    //Get the Index of the Minimum element of the diffrences as that index will be 
                    //the same indexx of the leafe that i want to put the elemenet of the 
                    //new array in as the distance between them is very samall.
                    int index = diffList.indexOf(Collections.min(diffList));
                    //put that element in the List_Of_Blocks of this leafeNode.
                    Treenodes.get(index).getList_Of_Blocks().add(arrayOfBlocks.get(j));
                    //Clear the List of diffrences.
                    diffList.clear();
                }

            }
        }
        //return the last leafe Nodes to make the table and the compression.
        return Treenodes;
    }

    public void Print() {
        int row=0;
        int col=0;
        
        row=Treenodes.get(0).getAvgBlock().length;
        col=Treenodes.get(0).getAvgBlock()[0].length;
        
        
        System.out.println("____FINALS_________");
        for (int i = 0; i < Treenodes.size(); i++) {
            
            System.out.println("for each node "+(i+1)+" have those matrices ");
            for (int j = 0; j < Treenodes.get(i).getList_Of_Blocks().size(); j++)
            {
                System.out.println("matrix no "+(j+1));
                for (int k = 0; k < row; k++) {
                    for (int l = 0; l < col; l++) {
                        System.out.print(Treenodes.get(i).getList_Of_Blocks().get(j)[k][l]+"   ");
                    }
                    System.out.println();
                }
                System.out.println("=======");
            }
        }

    }
    
    static LinkedList<String> code = new LinkedList<>();
    public static void GenerateCompressCode(LinkedList<Node> TreeEnd,int row,int col){
        
        vector_Quantizer vq = new vector_Quantizer();
        LinkedList<int[][]> listOfData = vq.InitialisingBlocks(row ,col);
        for (int i = 0; i < listOfData.size(); i++) {
            for (int j = 0; j < TreeEnd.size(); j++) {
                for (int k = 0; k < TreeEnd.get(j).getList_Of_Blocks().size(); k++) {
                    if(Arrays.deepEquals(listOfData.get(i) , TreeEnd.get(j).getList_Of_Blocks().get(k))){
                        code.add(j+"");
                    }
                }
            }
        }
        
        
//        try{
//            File file = new File("");
//            
//        }catch(Exception e){
//            System.out.println(e.toString());
//        }
    
}
    
    
    public LinkedList<Double[][]> Decompress(){

        LinkedList<Double[][]> decom = new LinkedList<>();
        for (int i = 0; i < code.size(); i++) {
            for (int j = 0; j < Treenodes.size(); j++) {
                if(Integer.parseInt(code.get(i)) == j){
                    decom.add(Treenodes.get(j).getAvgBlock());
                    break;
                }
            }
        }
        
        System.out.println("DECOMP");
        for (int i = 0; i < decom.size(); i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    System.out.print(decom.get(i)[j][k]+"   ");
                }
                System.out.println();
            }
            System.out.println("------------------");
            
        }
        return decom;
    }

}
