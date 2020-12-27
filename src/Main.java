import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/*
MNT 클래스
이 클래스는 MNT 테이블의 정보를 담고 있음
이 MNT 클래스를 Vector 함수로 감싸서 테이블을 구현
 */
class MNT{
    //fields(필드)
    int index, mdtInd;	//인덱스, mdt 인덱스
    String definition;	//문자
    Vector<String> AAT;

    //constructors(생성자)
    public MNT(int idx, String def, int mdtInd, Vector<String> arg) {
        index = idx;
        definition = def;
        this.mdtInd = mdtInd;
        AAT = arg;
    }
}

/*
MDT 클래스
이 클래스는 MDT 테이블의 정보를 담고 있음
이 MDT 클래스를 Vector 함수로 감싸서 테이블을 구현
 */
class MDT{
    //fields(필드)
    int idx;		//인덱스
    String def;		//인덱스 속 매크로

    //constructors(생성자)
    public MDT(int i, String string) {
        idx = i;
        def = string;
    }
}

// 메인 클래스
public class Main {
    public static void main(String[] args){
        // 파일을 열어서 내용물을 불러옴
        // 자세한 과정은 FileOpen 클래스를 참조할 것
        FileOpen fileOpen = new FileOpen();
        Vector<String> origindata = fileOpen.getOriginalData();
    }

    //methods(메소드)
}

/*
파일 열기 담당 클래스
파일 여는건 자바 JFileChooser 사용함
어셈블리 파일의 확장자는 *.s 또는 *.asm
혹시나 어셈블러 파일이 없어서 급조한 텍스트 파일을 사용할 사람을 위해 텍스트 파일도 읽을수 있게 처리함
*/
class FileOpen{

    //fields(필드)
    //원본 어셈블러 파일 내용
    Vector<String> originalData = new Vector<String>();


    //constructor(생성자)
    public FileOpen(){
        // 자바 파일선택
        JFileChooser chooser = new JFileChooser();

        // 파일 선택할 때 확장자 필터 설정
        FileNameExtensionFilter filterFileS = new FileNameExtensionFilter(
                "어셈블러 파일","s");
        FileNameExtensionFilter filterFileAsm = new FileNameExtensionFilter(
                "어셈블러 파일","asm");
        FileNameExtensionFilter filterFileText = new FileNameExtensionFilter(
                "텍스트 파일","txt");

        // 확장자 필터 적용 - 이 필터를 적용하게 되면 위의 필터에 적용된 확장자로밖에 열지 못합니다.
        chooser.setFileFilter(filterFileText);
        chooser.setFileFilter(filterFileAsm);
        chooser.setFileFilter(filterFileS);
        
        // 파일 열기 창 생성
        chooser.showOpenDialog(null);
        
        // 선택한 파일 열기
        try {
            Scanner in = new Scanner(chooser.getSelectedFile());
            
            // 내부 파일을 다 읽을때까지
            while(in.hasNext()) {
                //원본 내용을 담는 Vector에 내용 저장
                originalData.add(in.nextLine());
            }
        
        // 파일을 열지 못했을 경우    
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을수 없습니다.");
            System.exit(0);
            
        //파일 열기를 취소했을 경우    
        } catch(NullPointerException e) {
            System.out.println("파일 열기를 취소하였습니다.");
            System.out.println("종료하려면 아무 키나 눌러주세요");
            try {System.in.read();
            } catch (IOException e1) {e1.printStackTrace();}
            System.exit(0);
        }

        // 원본 내용을 잘 읽어왔는지 확인하기 위해 콘솔에 출력
        System.out.println("원본----------");
        for(int i=0; i < originalData.size(); i++) {
            System.out.println(originalData.get(i));
        }
    }

    //methods(메소드)
    //메인 클래스에 원본 데이터를 보내기 위한 메소드
    public Vector<String> getOriginalData(){
        return originalData;
    }
}

//파일 저장 담당 클래스
class FileSave{
    public FileSave(){}
}
