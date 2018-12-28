package cn.gmuni.sc.model;

import java.io.Serializable;

public class CetScoreModel implements Serializable {
    private String name;

    private String school;

    private String type;

    //笔试准考证号
    private String writeCardNum;

    private String writeScore;

    //听力成绩
    private String listeningScore;

    //阅读
    private String readingScore;

    //写作与翻译
    private String writingScore;

    //口试准考证号
    private String oralCardNum;

    //口试成绩
    private String oralGrade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWriteCardNum() {
        return writeCardNum;
    }

    public void setWriteCardNum(String writeCardNum) {
        this.writeCardNum = writeCardNum;
    }

    public String getWriteScore() {
        return writeScore;
    }

    public void setWriteScore(String writeScore) {
        this.writeScore = writeScore;
    }

    public String getListeningScore() {
        return listeningScore;
    }

    public void setListeningScore(String listeningScore) {
        this.listeningScore = listeningScore;
    }

    public String getReadingScore() {
        return readingScore;
    }

    public void setReadingScore(String readingScore) {
        this.readingScore = readingScore;
    }

    public String getWritingScore() {
        return writingScore;
    }

    public void setWritingScore(String writingScore) {
        this.writingScore = writingScore;
    }

    public String getOralCardNum() {
        return oralCardNum;
    }

    public void setOralCardNum(String oralCardNum) {
        this.oralCardNum = oralCardNum;
    }

    public String getOralGrade() {
        return oralGrade;
    }

    public void setOralGrade(String oralGrade) {
        this.oralGrade = oralGrade;
    }
}
