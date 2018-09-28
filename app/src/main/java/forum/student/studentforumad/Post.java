package forum.student.studentforumad;

public class Post {
    public String uid, time, date, title, name, image, body;

    public Post(String uid, String time, String date, String title, String name, String image, String body) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.title = title;
        this.name = name;
        this.image = image;
        this.body = body;
    }

    public Post(){

    }

    public String getBody(){
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
