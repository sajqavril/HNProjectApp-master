package as.hn.com.hnprojectapp;

import android.os.WorkSource;

import java.util.UUID;

public class FileEntity {
    public static final String PDF = "PDF";
    public static final String EXCEL = "EXCEL";
    public static final String PPT = "PPT";
    public static final String WORD = "WORD";
    private UUID FileId;
    protected String UploadUserCode;
    protected String FileType;
    protected boolean IfLocal;
    protected  String FileName;

    public FileEntity(){}

    public FileEntity(String owner, String fileType, String fileName){
        this.UploadUserCode = owner;
        this.FileType = fileType;
        this.FileId = UUID.randomUUID();
        //默认未放在本地，之后加上新的判断方法
        this.IfLocal = false;
        this.FileName = fileName;
    }

    public UUID getFileId() {
        return FileId;
    }

    public void setFileId(UUID fileId) {
        FileId = fileId;
    }

    public String getUploadUserCode() {
        return UploadUserCode;
    }

    public void setUploadUserCode(String uploadUserCode) {
        UploadUserCode = uploadUserCode;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public boolean isIfLocal() {
        return IfLocal;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
