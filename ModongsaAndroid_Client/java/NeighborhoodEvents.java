package smu.hw_network_team5_chatting_android;

public class NeighborhoodEvents {
    // 행사 정보 담아둘 객체
    // 멤버 변수
    String title = ""; // 제목
    String date = "";//언제 하는지
    String info = ""; // 정보
    int starScore = 0; // 별점 개수
    String whereURL = ""; //어디서 하는지 주소
    int image_path;
    int portNumber; // 각 기관에 따라 포트번호 달라짐


    public NeighborhoodEvents(String title, String releaseDate, String info, String whereURL, int image_path, int starScore, int portNumber) {
        this.title = title;
        this.date = releaseDate;
        this.info = info;
        this.starScore = starScore;
        this.whereURL = whereURL;
        this.image_path = image_path;
        this.portNumber = portNumber;
    }

    // get 함수들 둘다 만들긴 했지만 get만 주로 사용.
    public String getTitle() {

        return this.title;
    }

    public String getInfo() {

        return this.info;
    }

    public int getStarScore() {

        return this.starScore;
    }

    public int getImage_path() {
        return image_path;
    }

    public String getDate() {
        return date;
    }

    public String getWhereURL() {
        return whereURL;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
