import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Test {
    public static void main(String[] args) {

        Vector<String> v = new Vector<String>();		//원본 파일
        Vector<MDT> mdt = new Vector<MDT>();			//MDT테이블
        Vector<MNT> mnt = new Vector<MNT>();			//MNT테이블
        Vector<String> imc = new Vector<String>();		//일반 코드
        boolean macroflag = false;						//매크로 식별 변수

        //파일 스캔
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "어셈블러 파일","s");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter(
                "텍스트 파일","txt");
        chooser.setFileFilter(filter2);
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        try {
            Scanner in = new Scanner(chooser.getSelectedFile());
            while(in.hasNext()) {
                v.add(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을수 없습니다.");
            System.exit(0);
        } catch(NullPointerException e) {
            System.out.println("파일 열기를 취소하였습니다.");
            System.out.println("종료하려면 아무 키나 눌러주세요");
            try {
                System.in.read();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }

        //원본 출력
        System.out.println("원본----------");
        for(int i=0; i < v.size(); i++) {
            System.out.println(v.get(i));
        }

        //pass1
        //매크로 발견
        System.out.println();
        System.out.println("PASS 1----------");
        for(int i=0; i < v.size();) {
            if(v.get(i).isEmpty()) {
                i++;
                continue;
            }
            //매크로 내부일때
            if(macroflag)
            {
                //
                if(v.get(i).contains("ENDM")) {
                    //System.out.println(i+"매크로 종료");
                    macroflag = false;
                }

                mdt.add(new MDT(i, v.get(i)));
            }
            //매크로 시작 변수
            else if(v.get(i).contains("MACRO")) {
                macroflag = true;
                Vector<String> arg = new Vector<String>();
                String[] t = v.get(i).split("\\,"+"\\s+");
                String[] t1 = t[0].split("\\s+");
                //가인수 추가
                arg.add(t1[2]);
                for(int j=1; j < t.length; j++) {
                    arg.add(t[j]);
                }
                mnt.add(new MNT(mnt.size(),t1[0],mdt.size(),arg));
            }
            //매크로 내부가 아닐때
            else {
                imc.add(v.get(i));
            }
            i++;
        }
        //MDT 출력
        System.out.println("MDT");
        for(int i=0; i < mdt.size(); i++) {
            System.out.println(i+" "+mdt.get(i).def);
        }

        //MNT 출력
        System.out.println();
        System.out.println("MNT");
        for(int i=0; i < mnt.size(); i++) {
            MNT mntparts = mnt.get(i);
            System.out.print((mntparts.index+1)+" "+(mntparts.definition)
                    +", MDT인덱스 : "+(mntparts.mdtInd)+", 가인수 : ");
            for(int j=0; j < mntparts.AAT.size(); j++) {
                System.out.print(mntparts.AAT.get(j) + " ");
            }
            System.out.println();
        }
        //일반코드 출력
        System.out.println();
        System.out.println("코드");
        for(int i=0; i < imc.size(); i++) {
            System.out.println(imc.get(i));
        }

        //Pass 2
        System.out.println();
        System.out.println("PASS 2----------");
        for(int i=0; i < imc.size();) {					//코드 줄만큼 반복
            for(int j=0; j < mnt.size(); j++) {			//MNT 수만큼 반복
                if(imc.get(i).contains(mnt.get(j).definition)) {	//매크로가 포함되면?
                    System.out.println((i+1)+"번째 줄에서 "+mnt.get(j).definition+"발견");

                    //실인수 변환
                    String[] temp = imc.get(i).split("\\,"+"\\s+");
                    String[] temp2 = temp[0].split("\\s+");
                    Vector<String> iag = new Vector<String>();
                    iag.add(temp2[1]);
                    for(int tempidx=1; tempidx < temp.length; tempidx++)
                        iag.add(temp[tempidx]);

                    imc.remove(i);
                    if((mnt.get(j).index) == (mnt.size()-1)) {
                        for(int k=mdt.size()-2; k >= mnt.get(j).mdtInd; k--) {
                            imc.add(i,mdt.get(k).def);
                            //치환
                            for(int l = 0; l < iag.size(); l++) {
                                System.out.println(mnt.get(j).AAT.get(l)+"를 "+iag.get(l)+"로 변환합니다.");
                                imc.set(i, imc.get(i).replace(mnt.get(j).AAT.get(l),iag.get(l)));
                            }
                        }
                    }

                    else {
                        for(int k=mnt.get(j+1).mdtInd-2; k >= mnt.get(j).mdtInd; k--) {
                            imc.add(i,mdt.get(k).def);
                            //치환
                            for(int l = 0; l < iag.size(); l++) {
                                System.out.println(mnt.get(j).AAT.get(l)+"를 "+iag.get(l)+"로 변환합니다.");
                                imc.set(i, imc.get(i).replace(mnt.get(j).AAT.get(l),iag.get(l)));
                            }
                        }

                    }

                }
            }
            i++;
        }


        System.out.println();
        System.out.println("변환 종료----------");

        //출력 테스트
        for(int i=0; i < imc.size(); i++) {
            System.out.println(imc.get(i));
        }

        //파일 출력
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setFileFilter(filter2);
        saveChooser.setFileFilter(filter);
        saveChooser.showSaveDialog(null);
        try {
            PrintWriter out = new PrintWriter(saveChooser.getSelectedFile());
            for(int i=0; i < imc.size(); i++) {
                out.println(imc.get(i));
            }
            out.close();
            System.out.println("저장되었습니다 : "+saveChooser.getSelectedFile());
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을수 없습니다.");
            System.exit(0);
        } catch(NullPointerException e) {
            System.out.println("파일 저장을 취소하였습니다.");
            System.out.println("종료하려면 아무 키나 눌러주세요");
            try {
                System.in.read();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }

        //종료
        System.out.println("종료하려면 아무 키나 눌러주세요");
        try {
            System.in.read();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}