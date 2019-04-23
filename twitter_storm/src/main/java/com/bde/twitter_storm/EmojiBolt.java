
package com.bde.twitter_storm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import twitter4j.Status;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;
import com.vdurmont.emoji.EmojiManager;

public class EmojiBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Status tweet = (Status) input.getValueByField("tweet");
        boolean isRT = tweet.isRetweet();
        if(!isRT) {
            List<String> emojiStrings = EmojiParser.extractEmojis(tweet.getText());
            for(String s : emojiStrings) {
                Emoji emoji = EmojiManager.getByUnicode(s);
                Date createdAt = tweet.getCreatedAt();
                String tweetId = Long.toString(tweet.getId());
                String tweetText = tweet.getText();
                String emojiName = emoji.getAliases().get(0);
                String emojiDescription = emoji.getDescription();
                String emojiHtmlHex = emoji.getHtmlHexadecimal();
                collector.emit(new Values(createdAt, tweetId, tweetText, s, emojiName, emojiDescription, emojiHtmlHex));
            }
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet_created_at", "tweet_id", "tweet_text", "emoji_unicode", "emoji_name", "emoji_description", "emoji_html_hex"));
    }
    
}