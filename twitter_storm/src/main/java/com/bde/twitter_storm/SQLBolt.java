package com.bde.twitter_storm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.JdbcPreparedStatement;
import java.sql.Timestamp;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class SQLBolt extends BaseRichBolt {
    SQLConnect sql;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        sql = new SQLConnect();
    }

    @Override
    public void execute(Tuple input) {
        Date createdAt = (Date) input.getValueByField("tweet_created_at");
        String twitterId = input.getStringByField("tweet_id");
        String tweetText = input.getStringByField("tweet_text");
        String emojiUnicode = input.getStringByField("emoji_unicode");
        String emojiName = input.getStringByField("emoji_name");
        String emojiDescription = input.getStringByField("emoji_description");
        String emojiHtmlHex = input.getStringByField("emoji_html_hex");
        insertEmoji(emojiUnicode, emojiName, emojiDescription, emojiHtmlHex);
        insertTweet(twitterId, tweetText, emojiUnicode, createdAt);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public void insertTweet(String twitterID, String text, String emojiId, Date date) {
        try {
            sql.insertTweetStatement.setString(1, twitterID);
            sql.insertTweetStatement.setString(2, text);
            sql.insertTweetStatement.setString(3, emojiId);
            sql.insertTweetStatement.setTimestamp(4, new Timestamp(date.getTime()));
            sql.insertTweetStatement.executeUpdate();
            System.out.println("Great success inserting tweet!");
        } catch(SQLException e) {
            System.out.println(e);
            System.out.println("ERROR INSERTING TWEET");
        }
    }

    // returns emoji row key
    public void insertEmoji(String unicodeId, String name, String desc, String hex) {

        try {
            sql.selectEmojiStatement.setString(1, unicodeId);
            ResultSet rs = sql.selectEmojiStatement.executeQuery();
            
            if(rs.next()) {
                if(!rs.isLast()) {
                    System.out.println("****ERROR: TWO OR MORE EMOJI ROWS RETURNED ******");
                }
                System.out.println("Emoji " + name + " already exists in table.");
            }
            else {
                System.out.println("no results for: " + name + ". Inserting new emoji");
                sql.insertEmojiStatement.setString(1, unicodeId);
                sql.insertEmojiStatement.setString(2, name);
                sql.insertEmojiStatement.setString(3, desc);
                sql.insertEmojiStatement.setString(4, hex);
                sql.insertEmojiStatement.executeUpdate();
                System.out.println("Great success inserting Emoji!");
            }
            
        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(JdbcPreparedStatement.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } 
    } 

}