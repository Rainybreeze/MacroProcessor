/*
Pass1
패스 1은 원본 데이터에서 매크로를 탐지하는 역할을 합니다.

 */


import java.util.Arrays;
import java.util.Vector;

class Pass1 {

    // 매크로 식별 부호, 이게 True면 매크로 함수 내부이다는 뜻이다.
    private boolean macroflag = false;

    // MDT 테이블
    private final Vector<MDT> mdtVector = new Vector<MDT>();
    // MNT 테이블
    private final Vector<MNT> mntVector = new Vector<MNT>();
    // 매크로가 아닌 코드들이 들어있는 테이블
    private final Vector<String> imc = new Vector<String>();


    public Pass1(Vector<String> originData){
        System.out.format("\nPASS1----------");
        
        //한줄씩 읽어서 매크로를 찾음
        for(int i=0; i < originData.size();){
            //한줄을 읽어옴
            String originDataLine = originData.get(i);

            // 한줄 읽었는데 빈줄일 경우 다음줄로 이동
            if(originDataLine.isEmpty()){
                i++;
                continue;
            }

            // 매크로 함수 내부일 경우
            if(macroflag){
                
                // 매크로 종료 식별을 찾았을 경우 매크로 함수를 벗어났다고 변경
                if(originDataLine.contains("ENDM")){
                    macroflag = false;
                }

                // MDT 테이블에 추가
                mdtVector.add(new MDT(i, originDataLine));
            }

            //한줄 읽었는데 MACRO 함수의 시작지점을 찾은 경우
            else if(originDataLine.contains("MACRO")){
                macroflag = true;
                Vector<String> arg = new Vector<String>();
                
                // 가인수 분해
                String[] t = originDataLine.split("\\,"+"\\s+");
                String[] t1 = t[0].split("\\s+");

                // 분해된 가인수 추가
                arg.add(t1[2]);
                arg.addAll(Arrays.asList(t).subList(1, t.length));

                // MNT 테이블 추가
                mntVector.add(new MNT(mntVector.size(), t1[0], mdtVector.size(), arg));
            }

            // 만약 읽어들인 줄이 매크로가 아닌 일반 코드일경우 줄 전체를 imc 테이블에 넣음
            else {
                imc.add(originDataLine);
            }
            
            //다음줄로 넘김
            i++;
        }

        // MDT 테이블 출력
        System.out.format("MDT\n");
        for(int i=0; i < mdtVector.size(); i++){
            System.out.format("%d %s\n", i, mdtVector.get(i).def);
        }

        // MNT 테이블 출력
        System.out.format("\nMNT\n");
        for (MNT mntPart : mntVector) {
            System.out.format(
                    "%d %s, MDT 인덱스 : %s, 가인수 : ",
                    (mntPart.index + 1),
                    mntPart.definition,
                    mntPart.mdtInd
            );
            for (int j = 0; j < mntPart.AAT.size(); j++) {
                System.out.format("%s ", mntPart.AAT.size());
            }
            System.out.format("\n");
        }

        // 매크로가 아닌 코드 출력
        System.out.format("\n코드\n");
        for (String s : imc) {
            System.out.format("%s\n", s);
        }
    }

    public Vector<MDT> getMdtVector(){return mdtVector;}
    public Vector<MNT> getMntVector(){return mntVector;}
    public Vector<String> getImc(){return imc;}

}
