package com.bde.twitter_storm;

import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcStatement;

public class SQLConnect {

    public Connection con;
    public PreparedStatement selectEmojiStatement;
    public PreparedStatement insertEmojiStatement;
    public PreparedStatement insertTweetStatement;

    public final String selectEmojiString = "select * from EMOJIS where EMOJI_NAME = ?";
    public final String insertEmojiString = "insert into EMOJIS(UNICODE_ID, EMOJI_NAME, EMOJI_DESC, HTML_HEX_CODE) VALUES (?,?,?,?)";
    public final String insertTweetString = "insert into TWEETS(TWEET_ID, TWEET_TEXT, EMOJI_NAME, TWEET_DATETIME) VALUES (?,?,?,?)";

    public SQLConnect() {

        Map<String, String> env = System.getenv();
        String user = env.get("BDE_SQL_USERNAME");
        String password = env.get("BDE_SQL_PASSWORD");
        System.out.println("SQL USER: " + user);
        System.out.println("SQL PASS: " + password);

        String url = "jdbc:mysql://google/EMOJI_CLOUD?cloudSqlInstance=big-data-energy:us-central1:big-data-energy-mysql&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user="
                + user + "&password=" + password + "&characterEncoding=UTF-8&connectionCollation=UTF-8";

        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");

            selectEmojiStatement = con.prepareStatement(selectEmojiString);
            insertEmojiStatement = con.prepareStatement(insertEmojiString);
            insertTweetStatement = con.prepareStatement(insertTweetString);

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }
}