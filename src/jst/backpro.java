/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jst;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Indra Eka M
 */
public class backpro {

    int jumlahInput = 9;
    int jumlahNode = 3;
    int jumlahData = 600;
    int jumlahDataTesting = 83;
    double v[][] = new double[jumlahNode][jumlahInput];//inisialisasi bobot random awal
    double zn[] = new double[jumlahNode];//nilai v optimasi
    double w[][] = new double[jumlahNode][jumlahInput];//inisialisasi bobot baru optimasi
    double biasNode[] = new double[jumlahNode];//bias
    double wNode[] = new double[jumlahNode];//bobot node ke output
    double biasOutput;//bias output
    double zin[] = new double[jumlahNode];//z_in 
    double z[] = new double[jumlahNode];//z dari fung z_in
    double yin;//nilai yin
    double y;//aktifasi y
    double ohmk;//nilai ohmk
    double fAksen;//nilai f'
    double fAksenZ;// nilai f' z untuk aktifasi ohm k
    double alpha = 0.1;//inisialisasi alpha
    double deltaWNode[] = new double[jumlahNode];
    double deltaWBias;
    double deltaV[][] = new double[jumlahNode][jumlahInput];
    double ohm_in[] = new double[jumlahNode];
    double ohmj[] = new double[jumlahNode];
    double deltaVBias[] = new double[jumlahNode];
    double input[][] = new double[jumlahData][jumlahInput];
    int target[] = new int[jumlahData];
    double testing[][] = new double [jumlahDataTesting][jumlahInput];
    int targetTesting[] = new int[jumlahDataTesting];
    double mse;
    double nilaiToleransi = 0.01;
    int jumlahMaxEpoch = 10;
    int count = 0;
    float akurasi;
    int a ;

    public static void main(String[] args) {

        backpro backPropagation = new backpro();

        backPropagation.initBobot();
        backPropagation.file();
        backPropagation.mse = 1.0;
        int count = 0;
        while (backPropagation.mse > backPropagation.nilaiToleransi && count < backPropagation.jumlahMaxEpoch) {
            for (int i = 0; i < backPropagation.jumlahData; i++) {
                backPropagation.perhitungan(i);

            }
            
            count++;
            System.out.println("EPOCH : " + count);
            backPropagation.mse = backPropagation.mse / backPropagation.jumlahData;
            System.out.println("MSE : " + backPropagation.mse);
            
        }
        System.out.println("Bobot Untuk Testing :");
        for (int i = 0; i < backPropagation.jumlahNode; i++) {
            for (int j = 0; j < backPropagation.jumlahInput; j++) {
                System.out.print(backPropagation.w[i][j] + "\t");
            }
            System.out.println("");
        }
        for (int i = 0; i < backPropagation.jumlahDataTesting; i++) {
            backPropagation.test(i);
        }
        
        System.out.print("Akurasi ");
        backPropagation.akurasi = ((backPropagation.jumlahDataTesting - backPropagation.count)/backPropagation.jumlahDataTesting) * 100;
        System.out.println(backPropagation.akurasi);

    }

    public void initBobot() {
        double beta;
        beta = 0.7 * Math.pow(jumlahNode, (1 /(double) jumlahInput));

        //bobot random
//        System.out.println(" BOBOT RANDOM : ");
        for (int i = 0; i < jumlahNode; i++) {
            for (int j = 0; j < jumlahInput; j++) {
                v[i][j] = (float) Math.random() * 1;
//                System.out.print(v[i][j] + "\t");
                //zn[j] = 0.0;
            }
//            System.out.println();
        }

        //NILAI V optimasi
        for (int i = 0; i < jumlahNode; i++) {
            for (int j = 0; j < jumlahInput; j++) {
                zn[i] += (Math.pow(v[i][j], 2));
            }
            zn[i] = Math.sqrt(zn[i]);
        }

        System.out.println();

        System.out.println(" NILAI V : ");
        for (int i = 0; i < jumlahNode; i++) {
            System.out.print(zn[i] + "\t");
        }

        //BOBOT BARU
        System.out.println();
        System.out.println();
        System.out.println(" BOBOT BARU : ");
        System.out.println();
        for (int i = 0; i < jumlahNode; i++) {
            for (int j = 0; j < jumlahInput; j++) {
                w[i][j] = (beta * v[i][j]) / zn[i];
                System.out.print(w[i][j] + "\t");
            }
            System.out.println();
        }

        //NILAI BIAS
        System.out.println("Bias Node : ");
        for (int i = 0; i < jumlahNode; i++) {
            biasNode[i] = Math.random() * 1;
            System.out.println(biasNode[i]);
        }

        //BOBOT NODE
        System.out.println("Bobot Node : ");
        for (int i = 0; i < jumlahNode; i++) {
            wNode[i] = Math.random() * 1;
            System.out.println(wNode[i]);
        }

        //BIAS OUTPUT
        System.out.println("Bias Output : ");
        biasOutput = Math.random() * 1;
        System.out.println(biasOutput);
    }

    public void file() {
        //untuk input
        System.out.println("");
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")));
            String line;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                String data[] = line.split(",");
                for (int j = 0; j < data.length; j++) {
                    input[baris][j] = Double.parseDouble(data[j]);
                }
                baris++;
            }

        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        
        //untuk target
        System.out.println("Target  : ");
           try {
            BufferedReader br = new BufferedReader(new FileReader(new File("target.txt")));
            String line;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                target[baris] = Integer.parseInt(line);
//                System.out.println(target[baris]);
                baris++;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
           
        //untuk input testing
        System.out.println("Input Testing : ");
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("inputTesting.txt")));
            String line;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                String data[] = line.split(",");
                for (int j = 0; j < data.length; j++) {
                    testing[baris][j] = Double.parseDouble(data[j]);
                }
                baris++;
            }
//            for (int j = 0; j < baris; j++) {
//                for (int k = 0; k < 9; k++) {
//                    System.out.print(testing[j][k] + "\t");
//                }
//                System.out.println("");
//            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
           
        //untuk target testing
         System.out.println("Target  Testing: ");
           try {
            BufferedReader br = new BufferedReader(new FileReader(new File("targetTesting.txt")));
            String line;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                targetTesting[baris] = Integer.parseInt(line);
//                System.out.println(target[baris]);
                baris++;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        
    }

    public void perhitungan(int x) {

        double tampung[] = new double[jumlahNode];
//        int count = 0;
//        System.out.println("Z_in : ");

        for (int j = 0; j < jumlahInput; j++) {
            for (int k = 0; k < jumlahNode; k++) {
                tampung[k] += (input[x][j] * w[k][j]);

            }
//                if (count < jumlahNode) {
//                    tampung[count] += (input[i][j] * w[count][j]);
//                }
//                count ++;

//            count = 0;
        }

        for (int i = 0; i < jumlahNode; i++) {
            zin[i] += biasNode[i] + tampung[i];
//            System.out.println(zin[i]);
        }

//        System.out.println("Z : ");
        for (int i = 0; i < jumlahNode; i++) {

            z[i] = (1 / (1 + Math.exp(-zin[i])));
//            System.out.println(z[i]);

        }

        for (int i = 0; i < jumlahNode; i++) {
            tampung[i] += z[i] * wNode[i];
        }
        yin = biasOutput + tampung[0] + tampung[1] + tampung[2];
//        System.out.println("Y_in : " + yin);

        y = (1 / (1 + Math.exp(-yin)));
//        System.out.println("Y : " + y);
        
        //Hitung MSE
        mse += Math.pow((target[x] - y), 2);
        
        
        
        // untuk mecari ohmk
        fAksen = y * (1 - y);// harus cari f' dulu untuk dikalikan dengan input- y
//        System.out.println("F Aksen : " + fAksen);
        ohmk = (target[x] - y) * fAksen;//ohmk ( masih bermasalah )
//        System.out.println("Ohm : " + ohmk);

        //delta node 
        for (int i = 0; i < jumlahNode; i++) {
            deltaWNode[i] = alpha * ohmk * z[i];
        }

        //delta bias
        deltaWBias = alpha * ohmk;

        //nilai ohm_inj
        for (int i = 0; i < jumlahNode; i++) {
            ohm_in[i] += ohmk * wNode[i];
        }

        //aktifasi nilai ohm j
        for (int i = 0; i < jumlahNode; i++) {
            fAksenZ = 0.5 * (1 + z[i]) * (1 - z[i]);
            ohmj[i] = ohm_in[i] * fAksenZ;
        }

        //delta Vij dan deltaVBias
        for (int i = 0; i < jumlahNode; i++) {
            for (int j = 0; j < jumlahInput; j++) {
                deltaV[i][j] = alpha * ohmj[i] * input[x][j];
                deltaVBias[i] = alpha * ohmj[i];
            }
        }

        //update bobot input ke node dan bias
//        System.out.println("Bobot Baru : ");
        for (int i = 0; i < jumlahNode; i++) {
            for (int j = 0; j < jumlahInput; j++) {
                w[i][j] = w[i][j] + deltaV[i][j];
//                System.out.print(w[i][j] + "\t");
            }
//            System.out.println();
        }

        for (int i = 0; i < jumlahNode; i++) {
            biasNode[i] = biasNode[i] + deltaVBias[i];
        }

        //update bobot node ke output
//        System.out.println("Bobot Node Baru :  ");
        for (int i = 0; i < jumlahNode; i++) {
            wNode[i] = wNode[i] + deltaWNode[i];
//            System.out.println(wNode[i]);

        }
//        System.out.println("Bias Ouput : ");
        biasOutput = biasOutput + deltaWBias;
//        System.out.println(biasOutput);
    }
    
    public void test(int x){
         double tampung[] = new double[jumlahNode];

//        System.out.println("Z_in : ");

        for (int j = 0; j < jumlahInput; j++) {
            for (int k = 0; k < jumlahNode; k++) {
                tampung[k] += (testing[x][j] * w[k][j]);

            }

        }

        for (int i = 0; i < jumlahNode; i++) {
            zin[i] += biasNode[i] + tampung[i];
//            System.out.println(zin[i]);
        }

//        System.out.println("Z : ");
        for (int i = 0; i < jumlahNode; i++) {

            z[i] = (1 / (1 + Math.exp(-zin[i])));
//            System.out.println(z[i]);

        }

        for (int i = 0; i < jumlahNode; i++) {
            tampung[i] += z[i] * wNode[i];
        }
        yin = biasOutput + tampung[0] + tampung[1] + tampung[2];
//        System.out.println("Y_in : " + yin);

        y = (1 / (1 + Math.exp(-yin)));
//        System.out.println("Y : " + y);
        
        System.out.println("Diagnosis : ");
        
        if (y > 0.5) {
            System.out.println("Y : " + y);
            System.out.print("Tumor Ganas : ");
             a = 1;
            System.out.println(a);
        }else{
            System.out.println("Y : " + y);
            System.out.print("Tumor Jinak : ");
             a = 0;
             System.out.println(a);
            
        }
        
        if (a == targetTesting[x]) {
                System.out.println("Benar");
                 count += count; 
        }else{
            System.out.println("Salah");
        }
        
                
    }

}
