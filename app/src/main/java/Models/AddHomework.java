package Models;

public class AddHomework {
    public long subjectId;
    public long ClassId;
    public String title;
    public String body;

    public AddHomework(long subjectId, long classId, String title, String body) {
        this.subjectId = subjectId;
        ClassId = classId;
        this.title = title;
        this.body = body;
    }
}
