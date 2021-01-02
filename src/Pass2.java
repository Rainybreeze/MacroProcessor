import java.util.Arrays;
import java.util.Vector;

class Pass2 {

    private final Vector<String> transformedData;

    public Pass2(Vector<MDT> mdtVector, Vector<MNT> mntVector, Vector<String> imcVector){
        System.out.format("\nPass2----------\n");

        for(int i=0; i < imcVector.size();){
            for(int j=0; j < mntVector.size(); j++){
                if(imcVector.get(i).contains(mntVector.get(j).definition)){
                    System.out.format("%d번째 줄에서 %s 발견\n",(i+1), mntVector.get(j).definition);

                    // 실인수 변환
                    String[] temp = imcVector.get(i).split("\\,"+"\\s+");
                    String[] temp2 = temp[0].split("\\s+");
                    Vector<String> iag = new Vector<String>();
                    iag.add(temp2[1]);
                    iag.addAll(Arrays.asList(temp).subList(1, temp.length));

                    imcVector.remove(i);
                    if((mntVector.get(j).index) == (mntVector.size()-1)) {
                        for(int k=mdtVector.size()-2; k >= mntVector.get(j).mdtInd; k--) {
                            imcVector.add(i,mdtVector.get(k).def);

                            //치환
                            for(int l=0; i < iag.size(); l++){
                                System.out.format("%s를 %s로 변환합니다.\n", mntVector.get(j).AAT.get(l), iag.get(l));
                                imcVector.set(i,imcVector.get(i).replace(mntVector.get(j).AAT.get(l),iag.get(l)));
                            }
                        }
                    }

                    else {
                        for(int k = mntVector.get(j+1).mdtInd-2; k >= mntVector.get(j).mdtInd; k--){
                            imcVector.add(i, mdtVector.get(k).def);

                            //치환
                            for(int l=0; l < iag.size(); l++){
                                System.out.format("%s를 %s로 변환합니다.",mntVector.get(j).AAT.get(l), iag.get(l));
                                imcVector.set(i, imcVector.get(i).replace(mntVector.get(j).AAT.get(l),iag.get(l)));
                            }
                        }
                    }

                }
            }
            i++;
        }

        // 변환 결과 출력
        System.out.format("\n변환 종료----------");
        for (String s : imcVector) {
            System.out.format("%s\n", s);
        }
        transformedData = imcVector;
    }

    public Vector<String> getTransformedData(){return transformedData;}
}
