import java.util.Vector;

/*
MNT 클래스
이 클래스는 MNT 테이블의 정보를 담고 있음
 */
class MNT{
    //fields
    int index, mdtInd;	//인덱스, mdt 인덱스
    String definition;	//문자
    Vector<String> AAT;

    //constructors
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
 */
class MDT{
    //fields
    int idx;		//인덱스
    String def;		//인덱스 속 매크로

    //constructors
    public MDT(int i, String string) {
        idx = i;
        def = string;
    }
}

// 메인 클래스
public class Main {
    //fields
    Pass1 pass1;
    Pass2 pass2;

    //constructor
    public static void main(String[] args){

    }

    //methods
}

// 파일 열기 담당 클래스
// 파일 여는건 자바 JFileChooser 사용함
// 어셈블리 파일의 확장자는 *.s
class FileOpen{
    public FileOpen(){

    }
}

//파일 저장 담당 클래스
class FileSave{
    public FileSave(){}
}
