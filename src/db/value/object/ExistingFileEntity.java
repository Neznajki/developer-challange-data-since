package db.value.object;

public class ExistingFileEntity {
    private Integer id;
    private String fileName;

    public ExistingFileEntity(Integer id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}
